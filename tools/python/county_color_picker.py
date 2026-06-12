import json
import random

states_data = {
    '01': {
        'abbreviation': 'AL',
        'name': 'Alabama',
        'neighbors': ['12', '13', '28', '47', '13',],
    },
    '02': {
        'abbreviation': 'AK',
        'name': 'Alaska',
        'neighbors': [],
    },
    '04': {
        'abbreviation': 'AZ',
        'name': 'Arizona',
        'neighbors': ['35', '06', '08', '49', '32',],
    },
    '05': {
        'abbreviation': 'AR',
        'name': 'Ar20',
        'neighbors': ['28', '29', '40', '48', '22', '47',],
    },
    '06': {
        'abbreviation': 'CA',
        'name': 'California',
        'neighbors': ['41', '32', '04',],
    },
    '08': {
        'abbreviation': 'CO',
        'name': 'Colorado',
        'neighbors': ['56', '31', '20', '40', '35', '04', '49',],
    },
    '09': {
        'abbreviation': 'CT',
        'name': 'Connecticut',
        'neighbors': ['44', '25', '36',],
    },
    '10': {
        'abbreviation': 'DE',
        'name': 'Delaware',
        'neighbors': ['34', '42', '24',],
    },
    '11': {
        'abbreviation': 'DC',
        'name': 'District of Columbia',
        'neighbors': ['24', '51',],
    },
    '12': {
        'abbreviation': 'FL',
        'name': 'Florida',
        'neighbors': ['13', '01',],
    },
    '13': {
        'abbreviation': 'GA',
        'name': 'Georgia',
        'neighbors': ['45', '47', '01', '12',],
    },
    '15': {
        'abbreviation': 'HI',
        'name': 'Hawaii',
        'neighbors': [],
    },
    '16': {
        'abbreviation': 'ID',
        'name': 'Idaho',
        'neighbors': ['30', '56', '49', '32', '41', '53',],
    },
    '17': {
        'abbreviation': 'IL',
        'name': 'Illinois',
        'neighbors': ['18', '21', '29', '19', '55',],
    },
    '18': {
        'abbreviation': 'IN',
        'name': 'Indiana',
        'neighbors': ['26', '39', '21', '17',],
    },
    '19': {
        'abbreviation': 'IA',
        'name': 'Iowa',
        'neighbors': ['55', '17', '29', '31', '46', '27',],
    },
    '20': {
        'abbreviation': 'KS',
        'name': 'Kansas',
        'neighbors': ['31', '29', '40', '08',],
    },
    '21': {
        'abbreviation': 'KY',
        'name': 'Kentucky',
        'neighbors': ['39', '54', '51', '47', '29', '17', '18',],
    },
    '22': {
        'abbreviation': 'LA',
        'name': 'Louisiana',
        'neighbors': ['48', '05', '28',],
    },
    '23': {
        'abbreviation': 'ME',
        'name': 'Maine',
        'neighbors': ['33',],
    },
    '24': {
        'abbreviation': 'MD',
        'name': 'Maryland',
        'neighbors': ['42', '10', '11', '51', '54',],
    },
    '25': {
        'abbreviation': 'MA',
        'name': 'Massachusetts',
        'neighbors': ['33', '50', '36', '09', '44',],
    },
    '26': {
        'abbreviation': 'MI',
        'name': 'Michigan',
        'neighbors': ['39', '18', '55',],
    },
    '27': {
        'abbreviation': 'MN',
        'name': 'Minnesota',
        'neighbors': ['55', '19', '46', '38',],
    },
    '28': {
        'abbreviation': 'MS',
        'name': 'Mississippi',
        'neighbors': ['22', '05', '47', '01',],
    },
    '29': {
        'abbreviation': 'MO',
        'name': 'Missouri',
        'neighbors': ['19', '17', '21', '47', '05', '40', '20', '31',],
    },
    '30': {
        'abbreviation': 'MT',
        'name': 'Montana',
        'neighbors': ['38', '46', '56', '16',],
    },
    '31': {
        'abbreviation': 'NE',
        'name': 'Nebraska',
        'neighbors': ['46', '19', '29', '20', '08', '56',],
    },
    '32': {
        'abbreviation': 'NV',
        'name': 'Nevada',
        'neighbors': ['16', '49', '04', '08', '41',],
    },
    '33': {
        'abbreviation': 'NH',
        'name': 'New Hampshire',
        'neighbors': ['23', '25', '50',],
    },
    '34': {
        'abbreviation': 'NJ',
        'name': 'New Jersey',
        'neighbors': ['36', '42', '10',],
    },
    '35': {
        'abbreviation': 'NM',
        'name': 'New Mexico',
        'neighbors': ['08', '40', '48', '04', '49',],
    },
    '36': {
        'abbreviation': 'NY',
        'name': 'New York',
        'neighbors': ['50', '25', '09', '34', '42',],
    },
    '37': {
        'abbreviation': 'NC',
        'name': 'North Carolina',
        'neighbors': ['51', '47', '13', '45',],
    },
    '38': {
        'abbreviation': 'ND',
        'name': 'North Dakota',
        'neighbors': ['27', '46', '30',],
    },
    '39': {
        'abbreviation': 'OH',
        'name': 'Ohio',
        'neighbors': ['42', '54', '21', '18', '26',],
    },
    '40': {
        'abbreviation': 'OK',
        'name': 'Oklahoma',
        'neighbors': ['20', '29', '05', '48', '35', '08',],
    },
    '41': {
        'abbreviation': 'OR',
        'name': 'Oregon',
        'neighbors': ['53', '16', '32', '06',],
    },
    '42': {
        'abbreviation': 'PA',
        'name': 'Pennsylvania',
        'neighbors': ['36', '34', '10', '24', '54', '39',],
    },
    '44': {
        'abbreviation': 'RI',
        'name': 'Rhode Island',
        'neighbors': ['25', '09',],
    },
    '45': {
        'abbreviation': 'SC',
        'name': 'South Carolina',
        'neighbors': ['37', '13',],
    },
    '46': {
        'abbreviation': 'SD',
        'name': 'South Dakota',
        'neighbors': ['38', '27', '19', '31', '56', '30',],
    },
    '47': {
        'abbreviation': 'TN',
        'name': 'Tennessee',
        'neighbors': ['21', '51', '37', '13', '01', '28', '05', '29',],
    },
    '48': {
        'abbreviation': 'TX',
        'name': 'Texas',
        'neighbors': ['40', '05', '22', '35',],
    },
    '49': {
        'abbreviation': 'UT',
        'name': 'Utah',
        'neighbors': ['16', '56', '08', '35', '04', '32',],
    },
    '50': {
        'abbreviation': 'VT',
        'name': 'Vermont',
        'neighbors': ['33', '25', '36',],
    },
    '51': {
        'abbreviation': 'VA',
        'name': 'Virginia',
        'neighbors': ['24', '11', '37', '47', '21', '54',],
    },
    '53': {
        'abbreviation': 'WA',
        'name': 'Washington',
        'neighbors': ['16', '41',],
    },
    '54': {
        'abbreviation': 'WV',
        'name': 'West Virginia',
        'neighbors': ['39', '42', '51', '21',],
    },
    '55': {
        'abbreviation': 'WI',
        'name': 'Wisconsin',
        'neighbors': ['26', '17', '19', '27',],
    },
    '56': {
        'abbreviation': 'WY',
        'name': 'Wyoming',
        'neighbors': ['30', '46', '31', '08', '49', '16',],
    },
    '60': {
        'abbreviation': 'AS',
        'name': 'American Samoa',
        'neighbors': [],
    },
    '66': {
        'abbreviation': 'GU',
        'name': 'Guam',
        'neighbors': ['69',],
    },
    '69': {
        'abbreviation': 'MP',
        'name': 'Northern Mariana Islands',
        'neighbors': ['66',],
    },
    '72': {
        'abbreviation': 'PR',
        'name': 'Puerto Rico',
        'neighbors': ['78',],
    },
    '74': {
        'abbreviation': 'UM',
        'name': 'U.S. Minor Outlying Islands',
        'neighbors': [],
    },
    '78': {
        'abbreviation': 'VI',
        'name': 'U.S. Virgin Islands',
        'neighbors': ['72',],
    },
}

num_counties_in_each_state = {
    '02': 29,
    '48': 254,
    '10': 3,
    '32': 17,
    '29': 115,
    '22': 64,
    '06': 58,
    '30': 56,
    '35': 33,
    '04': 15,
    '08': 64,
    '41': 36,
    '56': 23,
    '26': 83,
    '27': 87,
    '49': 29,
    '16': 44,
    '20': 105,
    '31': 93,
    '46': 66,
    '53': 39,
    '38': 53,
    '12': 67,
    '40': 77,
    '13': 159,
    '55': 72,
   	'17': 102,
    '19': 99,
    '36': 62,
    '37': 100,
    '51': 133,
    '05': 75,
    '01': 67,
    '28': 82,
    '42': 67,
    '39': 88,
    '47': 95,
    '21': 120,
    '23': 16,
    '18': 92,
    '45': 46,
    '54': 55,
    '24': 24,
    '15': 5,
    '25': 14,
    '50': 14,
    '33': 10,
    '34': 21,
    '09': 8,
    '44': 5,
    '72': 78, # Puerto Rico's municipalities
    '11': 1, # District of Columbia
    '60': 15, # American Samoa
    '66': 1, # Guam
    '69': 4, # Northern Mariana Islands
    '78': 3, # US Virgin Isles
}

states_colors = {k: '' for k in num_counties_in_each_state.keys()}

used_colors = set()

channel_permutation_distance = 64
neighbor_closness = 0.6
intrastate_closeness = 0.6
state_color_brightness = 0.35

def color_closeness(color1: str, color2: str) -> float:
    r_closeness = 1 - abs(int(color1[1:3], 16) - int(color2[1:3], 16))/256
    g_closeness = 1 - abs(int(color1[3:5], 16) - int(color2[3:5], 16))/256
    b_closeness = 1 - abs(int(color1[5:7], 16) - int(color2[5:7], 16))/256
    return ((r_closeness + g_closeness + b_closeness) / 3) ** 2

def saturation(r_channel, g_channel, b_channel) -> float:
    max_sat = max(r_channel, g_channel, b_channel)
    min_sat = min(r_channel, g_channel, b_channel)
    delta = max_sat - min_sat
    saturation = 0
    if max_sat != 0:
        saturation = delta / max_sat
    return saturation

def brightness(r_channel, g_channel, b_channel) -> float:
    return ((r_channel/255) + (g_channel/255) + (b_channel/255))/3

def generate_color_channel(channel_permute: str = None) -> int:
    if not channel_permute:
        return random.randint(0, 255)
    return min(random.randint(-channel_permutation_distance, channel_permutation_distance) + int(channel_permute, 16), 255)

def generate_color(color_permute: str = None, targ_saturation: float = -1.0, targ_brightness: float = 0.0, *, reference_colors: list[str] = None, min_closeness: float = 0, max_closeness: float = 1) -> str:
    r_channel = generate_color_channel(color_permute[1:3] if color_permute else None)
    g_channel = generate_color_channel(color_permute[3:5] if color_permute else None)
    b_channel = generate_color_channel(color_permute[5:7] if color_permute else None)
    sat = saturation(r_channel, g_channel, b_channel)
    bright = brightness(r_channel, g_channel, b_channel)
    if sat < targ_saturation or bright < targ_brightness:
        return generate_color(color_permute, targ_saturation, targ_brightness, reference_colors=reference_colors, min_closeness=min_closeness, max_closeness=max_closeness)

    def code(channel: int) -> str: return hex(channel).split('x')[1].rjust(2, '0')
    color = f'#{code(r_channel)}{code(g_channel)}{code(b_channel)}'
    if color in used_colors: return generate_color(color_permute, targ_saturation, targ_brightness, reference_colors=reference_colors, min_closeness=min_closeness, max_closeness=max_closeness)

    for reference_color in reference_colors:
        closeness = color_closeness(reference_color, color)
        if closeness < min_closeness or closeness > max_closeness:
            return generate_color(color_permute, targ_saturation, targ_brightness, reference_colors=reference_colors, min_closeness=min_closeness, max_closeness=max_closeness)

    return color

def generate_colors_for_state(state_name: str) -> tuple[str, set[str]]:
    global used_colors
    this_states_colors = set()
    state_color = states_colors[state_name]
    if not state_color:
        neighbors_colors = [states_colors[neighborFIPS] for neighborFIPS in states_data[state_name]['neighbors'] if states_colors[neighborFIPS]]
        state_color = generate_color(None, 0.9, state_color_brightness, reference_colors=neighbors_colors, min_closeness=0, max_closeness=neighbor_closness)
        states_colors[state_name] = state_color
        used_colors.add(state_color)
    for i in range(num_counties_in_each_state[state_name]):
        county_color = generate_color(state_color, reference_colors=[state_color], min_closeness=intrastate_closeness, max_closeness=1)
        this_states_colors.add(county_color)
        used_colors.add(county_color)
    return state_color, this_states_colors

def generate_all_colors():
    result = {}
    for state_name in states_colors.keys():
        state_color, state_colors = generate_colors_for_state(state_name)
        result[state_name] = {
            'state_color': state_color,
            'county_colors': list(state_colors)
        }
    with open('../javascript/colors_viewer/colors.json', 'w') as out:
        json.dump(result, out)

if __name__ == '__main__':
    random.seed(4)
    generate_all_colors()

'''
Criteria: (Most important first)
Nearby states should have different colors
Counties within states should be differentiable
An even mix of colors should be represented
Colors should be bright and saturated
Counties within a state should have a nearby color to their state
Colors on a lot of the map shouldn't be ugly

Good options:
32 distance
seed 1

48 distance
seed 4
seed 20
seed 23
seed 32
seed 49

'''
