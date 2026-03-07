"""
Splits up large single city_data.json file into state-specific municipality JSON files.
"""

from typing import Set, Dict, Any
import json
from pathlib import Path

MUNICIPALITIES_FILE = f"{Path.cwd()}\\..\\raw\\cities_data.json"
OUTPUT_DIR = f"{Path.cwd()}\\..\\generated\\map"

def get_municipality_data(filepath=MUNICIPALITIES_FILE) -> Dict[str, Any]:
    return json.load(open(filepath, 'r', encoding='utf-8'))

def list_states_FIPS(municipalities: Dict[str, Any]) -> Set[str]:
    return {
        municipality['FIPS'].split('-')[0]
        for name, municipality in municipalities.items()
        if municipality['FIPS'].split('-')[0] != 'None'
    }

def divide_municipalities_by_state(municipalities: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
    states_names_to_FIPS: Dict[str, str] = {}
    states_municipalities: Dict[str, Dict[str, Any]] = {}
    for name, municipality in municipalities.items():
        state_name = municipality['state']
        state_FIPS = municipality['FIPS'].split('-')[0]
        if state_FIPS == 'None':
            state_FIPS = states_names_to_FIPS[state_name]
        states_names_to_FIPS[state_name] = state_FIPS
        states_municipalities.setdefault(state_FIPS, {})
        states_municipalities[state_FIPS][name] = municipality
    return states_municipalities

def write_state_municipalities(state_FIPS: str, municipalities: Dict[str, Any]):
    with open(f'{OUTPUT_DIR}\\states\\{state_FIPS}\\{state_FIPS}_municipalities.json', 'w', encoding='utf-8') as out:
        json.dump(municipalities, out, indent='    ', separators=(',',' : '))

def main() -> None:
    municipalities = get_municipality_data()
    states_municipalities = divide_municipalities_by_state(municipalities)
    print(states_municipalities)
    for state_FIPS, state_municipalities in states_municipalities.items():
        write_state_municipalities(state_FIPS, state_municipalities)

if __name__ == "__main__":
    main()
