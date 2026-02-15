"""
map_data_generator.py
Steven LaGoy
14 February 2026

INPUTS:
STATES_FILE: JSON data describing States
DISTRICTS_FILE: JSON data describing Congressional Districts
COUNTIES_FILE: JSON data describing Counties

OUTPUTS:
OUTPUT_DIR: directory for split data

Splits the contents of input files into separate JSON files, one for each state
and county. These are placed in OUTPUT_DIR with each state having its own folder. nation.json will
also be placed in this directory.
"""


from pathlib import Path
import json
import os
from typing import Dict, List, Tuple, Any

STATES_FILE = f"{Path.cwd()}\\..\\raw\\states.json"
DISTRICTS_FILE = f"{Path.cwd()}\\..\\raw\\congressional_districts.json"
COUNTIES_FILE = f"{Path.cwd()}\\..\\raw\\counties.json"
OUTPUT_DIR = f"{Path.cwd()}\\..\\generated\\map"

def make_states_jsons() -> Dict[str, Tuple[str, str]]:
    states_data: Dict[str, Dict[str, Any]] = json.load(open(STATES_FILE, 'r', encoding='utf-8'))
    states_FIPS_abbrs: Dict[str, Tuple[str, str]] = {}
    for data in states_data.values():
        FIPS: str = data["FIPS"]
        abbr: str = data["abbreviation"]
        states_FIPS_abbrs[data["common_name"]] = (FIPS, abbr)
        state_dir = f"{OUTPUT_DIR}\\{FIPS}"
        if not os.path.exists(state_dir):
            os.makedirs(state_dir)
        state_outfile = f"{state_dir}\\{FIPS}.json"
        with open(state_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(data, indent=4, separators=(", ", " : ")))
    return states_FIPS_abbrs

def get_ordinal(number: int) -> str:
    if number in [11, 12, 13]:
        return f"{number}th"
    elif number % 10 == 1:
        return f"{number}st"
    elif number % 10 == 2:
        return f"{number}nd"
    elif number % 10 == 3:
        return f"{number}rd"
    else: return f"{number}th"

def make_districts_jsons(states_FIPS_abbrs: Dict[str, Tuple[str, str]]):
    districts_data: Dict[str, Dict[str, Any]] = json.load(open(DISTRICTS_FILE, 'r', encoding='utf-8'))
    states_districts: Dict[str, List[str]] = {}
    for district, data in districts_data.items():
        state = data["state"]
        officeID = data["office_ID"]
        number = int(data["district_num"])
        data["district_num"] = number
        name = data["name"]
        is_at_large = "(at Large)" in name
        if is_at_large:
            data["name"] = f"{state}'s at-large congressional district"
        else:
            data["name"] = f"{state}'s {get_ordinal(number)} congressional district"
        states_districts.setdefault(state, [])
        states_districts[state].append(officeID)
        district_outfile = f"{OUTPUT_DIR}\\{states_FIPS_abbrs[state][0]}\\{states_FIPS_abbrs[state][1]}-{number}.json"
        with open(district_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(data, indent=4, separators=(", ", " : ")))

def make_counties_jsons(states_FIPS_abbrs: Dict[str, Tuple[str, str]]) -> Tuple[int, Dict[str, List[str]]]:
    counties_data: Dict[str, Dict[str, Any]] = json.load(open(COUNTIES_FILE, 'r', encoding='utf-8'))
    states_counties: Dict[str, List[str]] = {}
    population = 0
    for data in counties_data.values():
        FIPS: str = data["FIPS"]
        print(FIPS)
        states_counties.setdefault(FIPS[:2], [])
        states_counties[FIPS[:2]].append(FIPS)
        population += data["population"]
        county_outfile = f"{OUTPUT_DIR}\\{states_FIPS_abbrs[data["state"]][0]}\\{FIPS}.json"
        with open(county_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(data, indent=4, separators=(", ", " : ")))
    return population, states_counties

def make_nation_json(population: int, statesFIPS: List[str]):
    with open(f"{OUTPUT_DIR}\\nation.json", 'w', encoding='utf-8') as out:
        out.write(json.dumps({
            "full_name" : "United States of America",
            "common_name" : "United States",
            "population" : population,
            "land_area" : 3_532_316,
            "capital" : "Washington, DC",
            "flag_loc" : "united_states_flag.png",
            "abbreviation" : "USA",
            "motto" : "In God We Trust",
            "states" : statesFIPS
        }, indent=4, separators=(", ", " : ")))

def add_counties_to_states(states_counties: Dict[str, List[str]]):
    for state, counties in states_counties.items():
        state_outfile = f"{OUTPUT_DIR}\\{state}\\{state}.json"
        state_data = json.load(open(state_outfile, 'r', encoding='utf-8'))
        state_data['counties'] = counties
        with open(state_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(state_data, indent=4, separators=(", ", " : ")))

def main():
    states_FIPS_abbrs = make_states_jsons()
    make_districts_jsons(states_FIPS_abbrs)
    population, states_counties = make_counties_jsons(states_FIPS_abbrs)
    add_counties_to_states(states_counties)
    make_nation_json(population, [v[0] for k, v in states_FIPS_abbrs.items()])
    print("Done!")

if __name__ == '__main__':
    main()
