from math import ceil, floor
from datetime import datetime, timedelta
from random import random
import re
from typing import Any, Dict, List
import json

class State:
    def __init__(self, name: str, fips: str, neighbors: List[str], *,
        representatives: int = 1, r_representatives: int = 0, d_representatives: int = 0, r_senators: int = 0, d_senators: int = 0, r_governor: bool = False,
        r_vote_2020: int = 0, d_vote_2020: int = 0, r_vote_2024: int = 0, d_vote_2024: int = 0,
        state_representatives: int = 1, state_r_representatives: int = 0, state_d_representatives: int = 0,
        state_senators: int = 1, state_r_senators: int = 0, state_d_senators: int = 0,
        proportional: bool = True, r_carveout: bool = False, d_prewindow: bool = False,
        is_state: bool = True, is_dc: bool = False
    ):
        self.name = name
        self.fips = fips
        self.neighbors = neighbors
        self.representatives = representatives
        self.r_representatives = r_representatives
        self.d_representatives = d_representatives
        self.senators = 2
        @property
        def electors(self): return self.representatives + self.senators
        self.r_senators = r_senators
        self.d_senators = d_senators
        self.r_governor = r_governor
        @property
        def d_governor(self): return not self.r_governor
        @d_governor.setter
        def d_governor(self, value): self.r_governor = not value
        self.r_vote_2020 = r_vote_2020
        self.d_vote_2020 = d_vote_2020
        self.r_vote_2024 = r_vote_2024
        self.d_vote_2024 = d_vote_2024
        self.state_representatives = state_representatives
        self.state_r_representatives = state_r_representatives
        self.state_d_representatives = state_d_representatives
        self.state_senators = state_senators
        self.state_r_senators = state_r_senators
        self.state_d_senators = state_d_senators
        self.proportional = proportional
        self.r_carveout = r_carveout
        self.d_prewindow = d_prewindow
        self.is_state = is_state
        self.is_dc = is_dc
    def __str__(self) -> str:
        return re.sub(r"\s+", " ",
            f"{self.name} ({self.fips}): {{ neighbors={self.neighbors}, \
                representatives={self.representatives}, \
                r_representatives={self.r_representatives}, \
                d_representatives={self.d_representatives}, \
                senators={self.senators}, \
                r_senators={self.r_senators}, \
                d_senators={self.d_senators}, \
                r_governor={self.r_governor}, \
                d_governor={not self.r_governor}, \
                r_vote_2020={self.r_vote_2020}, \
                d_vote_2020={self.d_vote_2020}, \
                r_vote_2024={self.r_vote_2024}, \
                d_vote_2024={self.d_vote_2024}, \
                state_representatives={self.state_representatives}, \
                state_r_representatives={self.state_r_representatives}, \
                state_d_representatives={self.state_d_representatives}, \
                state_senators={self.state_senators}, \
                state_r_senators={self.state_r_senators}, \
                state_d_senators={self.state_d_senators}, \
                proportional={self.proportional}, \
                r_carveout={self.r_carveout}, \
                d_prewindow={self.d_prewindow}, \
                is_state={self.is_state}, \
                is_dc={self.is_dc}, \
            }}")

RERPUBLICAN_TERRITORY_DELEGATES = {
    "American Samoa" : 6,
    "District of Columbia" : 16,
    "Guam" : 6,
    "Northern Mariana Islands" : 6,
    "Puerto Rico" : 20,
    "Virgin Islands" : 6,
}
DEMOCRATIC_TERRITORY_VOTES_AT_LARGE = {
    "American Samoa" : 6,
    "Democrats Abroad" : 12,
    "Guam" : 3,
    "Northern Marianas" : 6,
    "Puerto Rico" : 44,
    "Virgin Islands" : 6,
}
DEMOCRATIC_TERRITORY_VOTES_PLEDGED = {"Democrats Abroad" : 1}
DEMOCRATIC_TERRITORY_VOTES_UNPLEDGED = {"Northern Marianas" : 5}
DEMOCRATIC_SUPERDELEGATES = {
    "Alabama" : 8,
    "Alaska" : 4,
    "American Samoa" : 5,
    "Arizona" : 13,
    "Arkansas" : 5,
    "California" : 79,
    "Colorado" : 12,
    "Connecticut" : 15,
    "Delaware" : 11,
    "Democrats Abroad" : 4,
    "District of Columbia" : 25,
    "Florida" : 30,
    "Georgia" : 13,
    "Guam" : 6,
    "Hawaii" : 9,
    "Idaho" : 5,
    "Illinois" : 27,
    "Indiana" : 7,
    "Iowa" : 8,
    "Kansas" : 6,
    "Kentucky" : 6,
    "Louisiana" : 6,
    "Maine" : 8,
    "Maryland" : 24,
    "Massachusetts" : 23,
    "Michigan" : 22,
    "Minnesota" : 16,
    "Mississippi" : 5,
    "Missouri" : 11,
    "Montana" : 6,
    "Nebraska" : 4,
    "Nevada" : 13,
    "New Hampshire" : 9,
    "New Jersey" : 20,
    "New Mexico" : 12,
    "New York" : 50,
    "North Carolina" : 12,
    "North Dakota" : 4,
    "Northern Marianas" : 5,
    "Ohio" : 18,
    "Oklahoma" : 6,
    "Oregon" : 13,
    "Pennsylvania" : 24,
    "Puerto Rico" : 7,
    "Rhode Island" : 9,
    "South Carolina" : 10,
    "South Dakota" : 5,
    "Tennessee" : 9,
    "Texas" : 32,
    "Unassigned" : 2,
    "Utah" : 5,
    "Vermont" : 8,
    "Virgin Islands" : 6,
    "Virginia" : 25,
    "Washington" : 20,
    "West Virginia" : 6,
    "Wisconsin" : 13,
    "Wyoming" : 4
}

def calculate_delegates(state: State, extra_violation: bool = False) -> tuple[int, int, int]:
    """Calculate total Republican delegates for a state or territory."""

    # Base state/territory delegates
    territory_delegates = RERPUBLICAN_TERRITORY_DELEGATES.get(state.name, 0)
    statehood_delegates = 10 if state.is_state else 0
    rnc_members = 3 # Always three

    district_delegates = state.representatives * 3

    pres_bonus = (
        ceil(4.5 + 0.6 * (state.representatives + 2))
        if state.r_vote_2024 > state.d_vote_2024 and state.is_state
        else 0
    )

    governor_bonus = 1 if state.r_governor else 0
    house_majority_bonus = 1 if state.r_representatives / max(state.representatives, 1) >= 0.5 else 0
    legislature_bonus = 0
    if state.state_r_representatives > state.state_d_representatives or state.state_r_senators > state.state_d_senators:
        legislature_bonus += 1
    if state.state_r_representatives > state.state_d_representatives and state.state_r_senators > state.state_d_senators:
        legislature_bonus += 1

    senator_bonus = state.r_senators

    dc_bonus = (
        ceil(4.5 + 0.3 * territory_delegates)
        if state.is_dc and state.r_vote_2024 > state.d_vote_2024
        else 0
    )

    at_large = sum([
        territory_delegates,
        statehood_delegates,
        pres_bonus,
        governor_bonus,
        house_majority_bonus,
        legislature_bonus,
        senator_bonus,
        dc_bonus
    ])

    total = rnc_members + at_large + district_delegates

    violated_rules = False

    if violated_rules:
        if extra_violation:
            return (rnc_members, 9 if total >= 30 else 6, 0)
        else:
            # Number of delegates reduced by 50%, min 2
            total = floor(total / 2) - rnc_members
            if total < 0:
                return (2, 0, 0)
            else:
                return (rnc_members, total, 0)
    else:
        return (rnc_members, at_large, district_delegates)

    # end calculate_delegates

def order_score(r_control: float, d_control: float, carveout: bool, prewindow: bool, neighbor_count: int, proportional: bool, set_day: bool, exempted: bool) -> int:
    ''' Determines the order score for a state with the passed info. States with higher order scores should choose their dates first. '''
    score = 0
    score += 15 if set_day else 0
    score += 80 if not exempted else 0
    score += neighbor_count * 2
    if r_control > d_control:
        score += 20 if proportional else 0
        score += 30 if not carveout else 0
        score += 5 if not prewindow else 0
    else:
        score += 10
        score += 30 if not prewindow else 0
        score += 5 if not carveout else 0
    return score

def determine_control(house_control: bool, senate_control: bool, governor: bool, vote2020: bool, vote2024: bool) -> float:
    ''' For a certain party with the passed control info, determines the control ratio. '''
    return (house_control * 10 + senate_control * 10 + governor * 5 + vote2020 * 3 + vote2024 * 3) / 31

def read_states_info() -> List[State]:
    states: List[State] = []

    names: List[str] = []
    fips: List[str] = []
    neighbors: List[List[str]] = []
    representatives: List[int] = []
    r_representatives: List[int] = []
    d_representatives: List[int] = []
    r_senators: List[int] = []
    d_senators: List[int] = []
    r_governor: List[bool] = []
    r_votes_2020: List[int] = []
    d_votes_2020: List[int] = []
    r_votes_2024: List[int] = []
    d_votes_2024: List[int] = []
    states_representatives: List[int] = []
    states_r_representatives: List[int] = []
    states_d_representatives: List[int] = []
    states_senators: List[int] = []
    states_r_senators: List[int] = []
    states_d_senators: List[int] = []
    proportional: List[bool] = []
    r_carveout: List[bool] = []
    d_prewindow: List[bool] = []
    is_state: List[bool] = []
    is_dc: List[bool] = []

    electoral_history: Dict[str, Any] = {}
    with open('src\\main\\resources\\conventions\\electoral_history.json', 'r') as electoral_data:
        electoral_history = json.loads(electoral_data.read())
    for k, v in electoral_history.items():
        if len(k) == 2:
            names.append(v['state'])
            fips.append(v['fips'])
            try:
                r_votes_2020.append(v['results']['2020']['result'][0]['votes'])
            except KeyError:
                r_votes_2020.append(0)
            try:
                d_votes_2020.append(v['results']['2020']['result'][2]['votes'])
            except KeyError:
                d_votes_2020.append(0)
            try:
                r_votes_2024.append(v['results']['2024']['result'][0]['votes'])
            except KeyError:
                r_votes_2024.append(0)
            try:
                d_votes_2024.append(v['results']['2024']['result'][2]['votes'])
            except KeyError:
                d_votes_2024.append(0)

    state_info: Dict[str, Any] = {}
    with open('src\\main\\resources\\conventions\\2025_state_info.json', 'r') as state_data:
        state_info = json.loads(state_data.read())
    for k, v in state_info.items():
        index = names.index(k.upper())
        try:
            states_representatives.append(v['legislature']['total_seats'])
            states_r_representatives.append(v['legislature']['republican_seats'])
            states_d_representatives.append(v['legislature']['democrat_seats'])
            states_senators.append(v['legislature']['total_seats'])
            states_r_senators.append(v['legislature']['republican_seats'])
            states_d_senators.append(v['legislature']['democrat_seats'])
        except KeyError:
            states_representatives.append(v['legislature']['lower_house']['total_seats'])
            states_r_representatives.append(v['legislature']['lower_house']['republican_seats'])
            states_d_representatives.append(v['legislature']['lower_house']['democrat_seats'])
            states_senators.append(v['legislature']['upper_house']['total_seats'])
            states_r_senators.append(v['legislature']['upper_house']['republican_seats'])
            states_d_senators.append(v['legislature']['upper_house']['democrat_seats'])
        try:
            r_senators.append(v['senators']['republican_senators'])
            d_senators.append(v['senators']['democrat_senators'])
            representatives.append(v['representatives']['representatives'])
            r_representatives.append(v['representatives']['republican_representatives'])
            d_representatives.append(v['representatives']['democrat_representatives'])
        except KeyError:
            representatives.append(0)
            r_representatives.append(0)
            d_representatives.append(0)
            r_senators.append(0)
            d_senators.append(0)
        r_governor.append(v['governorship']['party'] == 'Republican')
        
    primary_info: Dict[str, Any] = {}
    with open('src\\main\\resources\\conventions\\presidential_preference_conventions.json', 'r') as primary_data:
        primary_info = json.loads(primary_data.read())
    for k, v in primary_info.items():
        if k.upper() not in names:
            continue
        n = []
        for neighbor in v['neighbors']:
            index = names.index(neighbor.upper())
            n.append(fips[index])
        neighbors.append(n)
        proportional_types = ['proportional', 'proportional triggered', 'proportional loophole']
        proportional.append(
            v['republican_district_allocation_rule'] in proportional_types and
            v['republican_at_large_allocation_rule'] in proportional_types
        )
        r_carveout.append(v.get('republican_carveout', False))
        d_prewindow.append(v.get('democratic_prewindow', False))
    
    # States, District, and Territories
    non_states = ['AMERICAN SAMOA', 'DISTRICT OF COLUMBIA', 'GUAM', 'NORTHERN MARIANA ISLANDS', 'PUERTO RICO', 'US VIRGIN ISLANDS']
    for i in range(len(names)):
        # DC
        is_dc.append(names[i] == 'DISTRICT OF COLUMBIA')
        # States
        is_state.append(names[i] not in non_states)

    for i in range(len(names)):
        states.append(State(
            names[i], fips[i], neighbors[i],
            representatives=representatives[i], r_representatives=r_representatives[i], d_representatives=d_representatives[i],
            r_senators=r_senators[i], d_senators=d_senators[i], r_governor=r_governor[i],
            r_vote_2020=r_votes_2020[i], d_vote_2020=d_votes_2020[i], r_vote_2024=r_votes_2024[i], d_vote_2024=d_votes_2024[i],
            state_representatives=states_representatives[i], state_r_representatives=states_r_representatives[i], state_d_representatives=states_d_representatives[i],
            state_senators=states_senators[i], state_r_senators=states_r_senators[i], state_d_senators=states_d_senators[i],
            proportional=proportional[i], r_carveout=r_carveout[i], d_prewindow=d_prewindow[i],
            is_state=is_state[i], is_dc=is_dc[i]
        ))

    return states

def choose_date(previous_dates: Dict[str, datetime] | None = None, day: str = "") -> datetime:
    # Consider picking an existing date
    existing_date_weight = 0.5 # % Chance that an existing date is selected
    if previous_dates is not None and len(previous_dates) > 0:
        ...
    # Select a day
    day_weights = {"Monday" : 0.04, "Tuesday" : 0.80, "Wednesday" : 0.04, "Thursday" : 0.04, "Friday" : 0.04, "Saturday" : 0.04};
    if not day:
        weight = random() * sum(day_weights.values())
        for day_p in day_weights:
            weight -= day_weights[day_p]
            if weight <= 0:
                day = day_p
    
    return datetime(2028, 1, 1)

def main() -> None:
    
    '''
    Sources:
    https://www.frontloadinghq.com/p/2024-republican-delegate-allocation.html?utm_source=chatgpt.com
    https://ballotpedia.org/Republican_delegate_rules%2C_2024?utm_source=chatgpt.com
    https://en.wikipedia.org/wiki/2024_Republican_Party_presidential_primaries?utm_source=chatgpt.com
    https://ballotpedia.org/Republican_delegate_rules%2C_2024?utm_source=chatgpt.com
    https://www.thegreenpapers.com/P24/R-Alloc.phtml
    https://prod-static.gop.com/media/documents/2024_Call_of_the_Convention_as_adopted_11.20.23_1700517775.pdf?utm_source=chatgpt.com
    https://prod-static.gop.com/media/Rules_Of_The_Republican_Party.pdf
    https://www.thegreenpapers.com/P24/R-HS.phtml
    https://www.house.gov/representatives
    '''

    states = read_states_info()
    for state in states:
        print(str(state))

    # Choose dates
    super_early_date = datetime(2028, 1, 18) # Third Tuesday in January - earliest possible data
    early_date = datetime(2028, 3, 1) # Earliest date for Republican primaries
    stage_i_start = datetime(2020, 3, 3) # Earliest date for Democratic primaries
    proportional_date = datetime(2028, 3, 15) # Earliest date for Republican non-proportional primaries
    stage_ii_start = datetime(2020, 4, 1) # Democratic stage II bonus applies
    stage_iii_start = datetime(2020, 5, 1) # Democratic stage II bonus applies
    late_date_1 = datetime(2028, 6, 10) # Second Saturday in June, one possible Republican latest date
    republican_national_convention_date = datetime(2028, 7, 15) # Third Monday in July - Republican National Convention typical date
    democratic_national_convention_date = datetime(2028, 8, 21) # Third Monday in August - Democratic National Convention typical date
    late_date_2 = republican_national_convention_date - timedelta(days=45) # Another possible Republican latest date
    late_date = min(late_date_1, late_date_2)
    exempt = ["Iowa", "New Hampshire", "South Carolina", "Nevada"]
    saturdays = ["South Carolina", "Louisiana"]

    '''
    Choose a convention date first- party of the current president goes first (usually)
    July or August, occasionally early September
    Avoid events like the Olympics, other sports events
    States have dates in their laws for when candidates can be put on the ballot
        IE Ohio law requires 90 days between ballot submission and general election (early August)
    Must secure venue and logistics, based on funding from private party accounts and contributions
    Early enough to gain momentum, but late enough for late primary results. Expectations of more contested nominations might result in later timings
    
    Usually start on a Monday and last 4 days

    '''

    dates: dict[str, datetime] = {}
    # Loop through non-exempt states which allocate non-proportionally and select a date in [proportional_date, late_date]
    # Favor clustering temporally and geographically, also favor earlier dates
    # Choose a Tuesday 80% of the time. South Carolina and Louisiana should choose a Saturday

    # Loop through non-exempt proportional states and select a date in [early_date, late_date]
    # Try to choose dates that have already been chosen by other states- Super Tuesday

    # Loop through exempt states and select a date in [super_early_end_date, late_date]
    # Pick Iowa and New Hampshire last, select date in [super_early_date, late_date]
    # And ensure New Hampshire's date is one week before the next scheduled primary (not caucus)

if __name__ == "__main__":
    main()