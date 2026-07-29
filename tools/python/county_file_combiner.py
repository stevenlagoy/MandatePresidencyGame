import json
import os
import re

from data.states_data import states_data

def main() -> None:

    for state, state_data in states_data.items():
        print(state)
        state_dir = f'../../assets_raw/data/map/states/{state}-{state_data['abbreviation']}'

        open(os.path.join(state_dir, f'{state}_county_subdivisions.json'), 'w')

        county_files = [file for file in os.listdir(state_dir) if re.match('[0-9]{5}\\.json', file)]
        state_counties_json = {}
        for county_file in county_files:
            with open(os.path.join(state_dir, county_file), 'r', encoding='utf-8') as f:
                contents = json.load(f)
                fips = contents['FIPS']
                state_counties_json[fips] = contents

        with open(os.path.join(state_dir, f'{state}_counties.json'), 'w', encoding='utf-8') as f:
            json.dump(state_counties_json, f, indent=4, separators=(',',' : '))

        congressional_district_files = [file for file in os.listdir(state_dir) if re.match('[A-Z]{2}-[0-9]+\\.json', file)]
        state_congressional_districts_json = {}
        for congressional_district_file in congressional_district_files:
            with open(os.path.join(state_dir, congressional_district_file), 'r', encoding='utf-8') as f:
                contents = json.load(f)
                office_id = contents['officeID']
                state_congressional_districts_json[office_id] = contents

        with open(os.path.join(state_dir, f'{state}_congressional_districts.json'), 'w', encoding='utf-8') as f:
            json.dump(state_congressional_districts_json, f, indent=4, separators=(',',' : '))

        with open(os.path.join(state_dir, f'{state}.json'), 'r', encoding='utf-8') as f:
            with open(os.path.join(state_dir, f'{state}_state.json'), 'w', encoding='utf-8') as out:
                out.write(f.read())

    return

if __name__ == '__main__':
    main()
