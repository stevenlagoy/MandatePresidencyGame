import csv
import json


def main():

    county_colors = {}

    with open('../javascript/colors_viewer/county_colors.csv') as f:
        csv_reader = csv.reader(f)
        csv_header = next(csv_reader) # Skip header
        for row in csv_reader:
            county_colors[row[0]] = row[1]

        for fips, color in county_colors.items():
            state_fips = fips[:2]
            path = f'../../core/src/main/resources/map/states/{state_fips}/{fips}.json'
            with open(path, 'r', encoding='utf-8') as f:
                contents = json.load(f)
            contents['color'] = str(color).upper()
            with open(path, 'w', encoding='utf-8') as f:
                json.dump(contents, f, ensure_ascii=False, indent=4, separators=(',', ' : '))


if __name__ == "__main__":
    main()
