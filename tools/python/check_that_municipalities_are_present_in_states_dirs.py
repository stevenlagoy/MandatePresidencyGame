"""
Checks that the municipalities in resources/map/municipalities.json are present in the municipalities.json file for each state in its own directory.
"""

import json
from pathlib import Path
from typing import List

MUNICIPALITIES_FILE = f"{Path.cwd()}\\..\\..\\core\\src\\main\\resources\\map\\municipalities.json"
STATES_DIR = f"{Path.cwd()}\\..\\..\\core\\src\\main\\resources\\map\\states\\"

def get_state(key: str) -> str:
    parts = key.split(",")
    return parts[-1]

def get_all_city_names() -> List[str]:
    all_city_names = []
    for i in range(78):
        try:
            state_municipalities_path = f"{STATES_DIR}\\{i}\\{i}_municipalities.json"
            for city in get_state_city_names(state_municipalities_path): all_city_names.append(city)
        except FileNotFoundError as e:
            continue
    return all_city_names

def get_state_city_names(path: str) -> List[str]:
    with open(path, 'r', encoding='utf-8') as state_cities:
        return [key for key in json.load(state_cities)]

def main():
    municipalities_file_cities = []
    with open(MUNICIPALITIES_FILE, 'r', encoding='utf-8') as file:
        municipalities_json = json.load(file)
        for key in municipalities_json: municipalities_file_cities.append(key)

    known_city_names = get_all_city_names()
    for city in municipalities_file_cities:
        if city not in known_city_names:
            print(city)
            pass

if __name__ == "__main__":
    main()
