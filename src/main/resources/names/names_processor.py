from typing import Dict
import json

def process_raw_names_file():

    decade = '1880s'
    nametype = 'firstnames'

    with open('src\\main\\resources\\names\\names.in') as file:
        content = file.readlines()
    male_names: Dict[str, int] = {}
    female_names: Dict[str, int] = {}

    for line in content:
        data = line.split()
        if len(data) != 5:
            print(data)
        male_names[data[1]] = int(data[2].replace(',',''))
        female_names[data[3]] = int(data[4].replace(',',''))

    male_file_name = decade + '_males_' + nametype + '.json'
    female_file_name = decade + '_females_' + nametype + '.json'
    with open('src\\main\\resources\\names\\' + male_file_name, 'w', encoding='utf-8') as out:
        out.write('{')
        first = True
        for k, v in male_names.items():
            if first is False:
                out.write(",")
            out.write(f'\n\t\"{k}\" : {v}')
            first = False
        out.write('\n}')

    with open('src\\main\\resources\\names\\' + female_file_name, 'w', encoding='utf-8') as out:
        out.write('{')
        first = True
        for k, v in female_names.items():
            if first is False:
                out.write(",")
            out.write(f'\n\t\"{k}\" : {v}')
            first = False
        out.write('\n}')

def combine_name_files():
    starting_decade = 1880
    ending_decade = 2010

    decades = [str(i) + "s" for i in range(starting_decade, ending_decade + 10, 10)]

    male_combined: Dict[str, Dict[str, int]] = {}
    female_combined: Dict[str, Dict[str, int]] = {}

    for decade in decades:
        male_filename = f'{decade}_males_firstnames.json'
        female_filename = f'{decade}_females_firstnames.json'
        with open('src\\main\\resources\\names\\' + male_filename, 'r', encoding='utf-8') as file:
            data = json.loads(file.read())
            male_combined[decade] = data

        with open('src\\main\\resources\\names\\' + female_filename, 'r', encoding='utf-8') as file:
            data = json.loads(file.read())
            female_combined[decade] = data

    combined = {'male': male_combined, 'female': female_combined}
    print(combined)

    with open('src\\main\\resources\\names\\decade_names.json', 'w', encoding='utf-8') as out:
        out.write(json.dumps(combined, indent=2, separators=(',',' : ')))

def main() -> None:
    combine_name_files()

if __name__ == "__main__":
    main()