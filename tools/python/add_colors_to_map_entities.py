import csv
import json
from data.states_data import states_data, states_abbreviations

maps_dir = '../../assets_raw/maps/'
watersheds_path = '../../GIS/__outputs/watersheds.csv'
congressional_districts_path = '../../GIS/__outputs/congressional_districts.csv'
places_path = '../../GIS/__outputs/places.csv'
watersheds_output = "../../assets_raw/data/map/watersheds.json"
states_dir = '../../assets_raw/data/map/states/'

class MapLayer:
    def __init__(self, name: str, path: str) -> None:
        self.name = name
        self.path = path

layers = [
    MapLayer('CongressionalDistricts', maps_dir + 'CongressionalDistricts.png'),
    MapLayer('CountySubdivisions', maps_dir + 'CountySubdivisions'),
    MapLayer('HistoricalProvinces', maps_dir + 'HistoricalProvinces'),
    MapLayer('Hydrology', maps_dir + 'Hydrology'),
    MapLayer('Physiography', maps_dir + 'Physiography'),
    MapLayer('Places', maps_dir + 'Places'),
    MapLayer('Precipitation', maps_dir + 'Precipitation'),
    MapLayer('TimeZones', maps_dir + 'TimeZones'),
    MapLayer('TopographyBathymetry', maps_dir + 'TopographyBathymetry'),
]

class Watershed:
    def __init__(self, naw1: str, naw2: str, naw3: str, naw4: str, fed_ref: str, color: str) -> None:
        self.naw1 = naw1
        self.naw2 = naw2
        self.naw3 = naw3
        self.naw4 = naw4
        self.fed_ref = fed_ref
        self.color = color
        self.name = fed_ref if fed_ref else naw4 if naw4 else naw3 if naw3 else naw2 if naw2 else naw1

    def to_dict(self) -> dict:
        return {
            'level1Watershed': self.naw1,
            'level2Watershed': self.naw2,
            'level3Watershed': self.naw3,
            'level4Watershed': self.naw4,
            'federalReference': self.fed_ref,
            'color': self.color,
        }

class CongressionalDistrict:
    def __init__(self, name: str, state: str, state_abbreviation: str, district_number: int, office_id: str, geo_id: str, color: str = '') -> None:
        self.name = name
        self.state = state
        self.state_abbreviation = state_abbreviation
        self.district_number = district_number
        self.office_id = office_id
        self.geo_id = geo_id
        self.color = color
        self.descriptors = [state.upper()]
        self.congressionalSession = 119

    def to_dict(self) -> dict:
        return {
            'name': self.name,
            'state': self.state,
            'districtNumber': self.district_number,
            'officeID': self.state_abbreviation + self.geo_id[2:4],
            'geoID': self.geo_id,
            'color': self.color,
            'descriptors': self.descriptors,
            'congressionalSession': self.congressionalSession,
        }

class Place:
    def __init__(self, state: str, fips: str, name: str, color: str) -> None:
        self.state = state
        self.fips = fips
        self.name = name
        self.color = color

def read_csv(path: str) -> tuple[list[str], list[list[str]]]:
    reader = csv.reader(open(path, 'r', encoding='utf-8'), delimiter=',')
    header = next(reader)
    rows = [_ for _ in reader]
    return header, rows

def process_watersheds() -> None:
    header, rows = read_csv(watersheds_path)
    watersheds = {}
    for row in rows:
        watershed = Watershed(
            row[0], row[1], row[2], row[3], row[4], row[-1],
        )
        name = watershed.name
        extra = 0
        while True:
            if name not in watersheds:
                watersheds[name] = watershed.to_dict()
                break
            if name + "-" + str(extra) not in watersheds:
                watersheds[name + "-" + str(extra)] = watershed.to_dict()
                break
            extra += 1
    with open(watersheds_output, 'w', encoding='utf-8') as f:
        json.dump(watersheds, f, ensure_ascii=False, indent=4)

def process_congressional_districts() -> None:
    # Get colors for Congressional Districts
    header, rows = read_csv(congressional_districts_path)
    congressional_district_colors = {}
    colors = []
    for row in rows:
        geoid = row[2]
        color = row[-1]
        if color in colors:
            print("Duplicate color for row: " + str(row))
        colors.append(color)
        congressional_district_colors[geoid] = color
    print("Read colors for " + str(len(congressional_district_colors)) + " congressional districts and found " + str(len(colors)) + " distinct colors")

    used_colors = []
    for fips, data in states_data.items():
        congressional_districts = []
        print("Processing state " + str(fips))
        state_congressional_districts_path = states_dir + fips + '-' + data['abbreviation'] + '/' + fips + '_congressional_districts.json'
        with open(state_congressional_districts_path, 'r', encoding='utf-8') as f:
            state_districts = json.load(f)
            for geoid, district in state_districts.items():
                congressional_districts.append(CongressionalDistrict(
                    district['name'],
                    district['state'],
                    states_abbreviations[district['state']],
                    district['districtNumber'],
                    district['officeID'],
                    geoid,
                    congressional_district_colors[geoid],
                ))
                used_colors.append(congressional_district_colors[geoid])
        congressional_districts_data = {district.geo_id: district.to_dict() for district in congressional_districts}
        with open(state_congressional_districts_path, 'w', encoding='utf-8') as f:
            json.dump(congressional_districts_data, f, ensure_ascii=False, indent=4)

    for color in congressional_district_colors.values():
        if color not in used_colors:
            print('Color ' + color + ' was not used')

def geoid_to_officeid(geoid: str) -> str:
    state_fips = geoid[0:2]
    number = geoid[2:4]
    state_abbr = states_data[state_fips]['abbreviation']
    officeid = state_abbr + number
    return officeid

def officeid_to_geoid(officeid: str) -> str:
    state_abbr = officeid[0:2]
    number = officeid[2:4]
    state_fips = ''
    for fips, data in states_data.items():
        if data['abbreviation'] == state_abbr:
            state_fips = fips
            break
    else:
        print('Could not find a state with abbreviation: ' + state_abbr)
        return ''
    geoid = state_fips + number
    return geoid

def main() -> None:
    pass

if __name__ == '__main__':
    main()
