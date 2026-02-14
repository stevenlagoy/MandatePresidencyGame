from pathlib import Path
import json
import os
from typing import Dict, List, Any

STATES_FILE = f"{Path.cwd()}\\..\\raw\\states.json"
COUNTIES_FILE = f"{Path.cwd()}\\..\\raw\\counties.json"
OUTPUT_DIR = f"{Path.cwd()}\\..\\generated\\map"

class State:
    def __init__(self,
        FIPS: str, common_name: str, full_name: str, map_color: str, flag_loc: str,
        population: int, land_area: float, abbreviation: str, motto: str, nickname: str,
        capital: str, descriptors: List[str]
    ):
        self.FIPS = FIPS
        self.common_name = common_name
        self.full_name = full_name
        self.map_color = map_color
        self.flag_loc = flag_loc
        self.population = population
        self.land_area = land_area
        self.abbreviation = abbreviation
        self.motto = motto
        self.nickname = nickname
        self.capital = capital
        self.descriptors = descriptors

    def to_json(self) -> Dict[str, Any]:
        return {
            "FIPS": self.FIPS,
            "common_name": self.common_name,
            "full_name": self.full_name,
            "map_color": self.map_color,
            "flag_loc": self.flag_loc,
            "population": self.population,
            "land_area": self.land_area,
            "abbreviation": self.abbreviation,
            "motto": self.motto,
            "nickname": self.nickname,
            "capital": self.capital,
            "descriptors": self.descriptors
        }

class County:
    def __init__(self,
        FIPS: str, full_name: str, common_name: str, state: str, county_seat: str,
        population: int, land_area: float, descriptors: List[str]
    ):
        self.FIPS = FIPS
        self.full_name = full_name
        self.common_name = common_name
        self.state = state
        self.county_seat = county_seat
        self.population = population
        self.land_area = land_area
        self.descriptors = descriptors

def make_states_jsons() -> Dict[str, str]:
    states_data: Dict[str, Dict[str, Any]] = json.load(open(STATES_FILE, 'r', encoding='utf-8'))
    statesFIPS: Dict[str, str] = {}
    for data in states_data.values():
        FIPS: str = data["FIPS"]
        statesFIPS[data["common_name"]] = FIPS
        state_dir = f"{OUTPUT_DIR}\\{FIPS}"
        if not os.path.exists(state_dir):
            os.makedirs(state_dir)
        state_outfile = f"{state_dir}\\{FIPS}.json"
        with open(state_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(data, indent=4, separators=(", ", " : ")))
    return statesFIPS

def make_counties_jsons(statesFIPS: Dict[str, str]) -> int:
    counties_data: Dict[str, Dict[str, Any]] = json.load(open(COUNTIES_FILE, 'r', encoding='utf-8'))
    population = 0
    for data in counties_data.values():
        FIPS: str = data["FIPS"]
        population += data["population"]
        county_outfile = f"{OUTPUT_DIR}\\{statesFIPS[data["state"]]}\\{FIPS}.json"
        with open(county_outfile, 'w', encoding='utf-8') as out:
            out.write(json.dumps(data, indent=4, separators=(", ", " : ")))

def make_nation_json(population: int):
    with open(f"{OUTPUT_DIR}\\nation.json", 'w', encoding='utf-8') as out:
        out.write(json.dumps({
            "full_name" : "United States of America",
            "common_name" : "United States",
            "population" : population,
            "land_area" : 3_532_316,
            "capital" : "Washington, DC",
            "flag_loc" : "united_states_flag.png",
            "abbreviation" : "USA",
            "motto" : "In God We Trust"
        }, indent=4, separators=(", ", " : ")))

def main():
    statesFIPS = make_states_jsons()
    population = make_counties_jsons(statesFIPS)
    make_nation_json(population)
    print("Done!")


if __name__ == '__main__':
    main()
