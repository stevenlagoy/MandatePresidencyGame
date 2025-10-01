from math import ceil, floor

class State:
    def __init__(self, name: str, representatives: int, r_representatives: int, r_senators: int, r_governor: bool, voted_r: bool, state_house_r_maj: bool, state_senate_r_maj: bool, is_state: bool = True, is_dc: bool = False, violated_rules = False):
        self.name = name
        self.representatives = representatives
        self.r_representatives = r_representatives
        self.r_senators = r_senators
        self.r_governor = r_governor
        self.voted_r = voted_r
        self.state_house_r_maj = state_house_r_maj
        self.state_senate_r_maj = state_senate_r_maj
        self.is_state = is_state
        self.is_dc = is_dc
        self.violated_rules = violated_rules

TERRITORY_DELEGATES = {
    "American Samoa" : 6,
    "District of Columbia" : 16,
    "Guam" : 6,
    "Northern Mariana Islands" : 6,
    "Puerto Rico" : 20,
    "Virgin Islands" : 6
}

def calculate_delegates(state: State, extra_violation: bool = False) -> tuple[int, int, int]:
    """Calculate total Republican delegates for a state or territory."""

    # Base state/territory delegates
    territory_delegates = TERRITORY_DELEGATES.get(state.name, 0)
    statehood_delegates = 10 if state.is_state else 0
    rnc_members = 3 # Always three

    district_delegates = state.representatives * 3

    pres_bonus = (
        ceil(4.5 + 0.6 * (state.representatives + 2))
        if state.voted_r and state.is_state
        else 0
    )

    governor_bonus = 1 if state.r_governor else 0
    house_majority_bonus = 1 if state.r_representatives / max(state.representatives, 1) >= 0.5 else 0
    legislature_bonus = 0
    if state.state_house_r_maj or state.state_senate_r_maj:
        legislature_bonus += 1
    if state.state_house_r_maj and state.state_senate_r_maj:
        legislature_bonus += 1

    senator_bonus = state.r_senators

    dc_bonus = (
        ceil(4.5 + 0.3 * territory_delegates)
        if state.is_dc and state.voted_r
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

    if state.violated_rules:
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
    '''

    states = [
        State("Alaska", 1, 0, 2, True, True, True, True),  # Split legislature control
        State("Florida", 28, 20, 2, True, True, True, True),
        State("Georgia", 14, 9, 2, True, False, True, True),
        State("Kansas", 4, 3, 2, True, True, True, True),
        State("Kentucky", 6, 5, 2, True, True, True, True),
        State("Louisiana", 6, 5, 1, True, True, True, True),  # 1 R senator in Nov 2023
        State("Montana", 2, 2, 2, True, True, True, True),
        State("West Virginia", 2, 2, 1, True, True, True, True),  # 1 R senator in Nov 2023
        State("North Carolina", 14, 7, 2, False, True, True, True),  # Dem governor
        State("Pennsylvania", 17, 9, 1, False, False, False, True),  # Dem governor, split legislature
        State("Wisconsin", 8, 6, 1, False, False, False, False),  # Dem governor in 2023
        State("Colorado", 8, 3, 0, False, False, False, False),
        State("Maine", 2, 1, 1, False, False, False, False),  # Split districts
        State("Michigan", 13, 7, 0, False, False, False, False),  # Won by Biden
        State("Nevada", 4, 3, 2, False, False, False, False),  # Won by Biden
        State("New Hampshire", 2, 2, 0, False, False, True, False),
        State("New Jersey", 12, 3, 0, False, False, False, False, True, False, True),
        State("Virginia", 11, 5, 0, False, False, False, False),

        # Solid Republican states that voted for Trump in 2020
        State("Alabama", 7, 6, 2, True, True, True, True),
        State("Arkansas", 4, 4, 2, True, True, True, True),
        State("Idaho", 2, 2, 2, True, True, True, True),
        State("Indiana", 9, 7, 2, True, True, True, True),
        State("Iowa", 4, 4, 2, True, True, True, True),
        State("Mississippi", 4, 3, 2, True, True, True, True),
        State("Missouri", 8, 6, 2, True, True, True, True),
        State("Nebraska", 3, 3, 2, True, True, True, True),  # Unicameral, nonpartisan officially
        State("North Dakota", 1, 1, 2, True, True, True, True),
        State("Ohio", 15, 10, 2, True, True, True, True),
        State("Oklahoma", 5, 5, 2, True, True, True, True),
        State("South Carolina", 7, 6, 2, True, True, True, True),
        State("South Dakota", 1, 1, 2, True, True, True, True),
        State("Tennessee", 9, 8, 2, True, True, True, True),
        State("Texas", 38, 25, 2, True, True, True, True),
        State("Utah", 4, 4, 2, True, True, True, True),
        State("Wyoming", 1, 1, 2, True, True, True, True),

        # Swing/competitive states
        State("Arizona", 9, 6, 2, False, False, False, False),  # Dem governor in 2023

        # Democratic states
        State("California", 52, 11, 0, False, False, False, False),
        State("Connecticut", 5, 0, 0, False, False, False, False),
        State("Delaware", 1, 0, 0, False, False, False, False),
        State("Illinois", 17, 5, 0, False, False, False, False),
        State("Maryland", 8, 1, 0, False, False, False, False),
        State("Massachusetts", 9, 0, 0, False, False, False, False),
        State("Minnesota", 8, 4, 1, False, False, False, False),
        State("New Mexico", 3, 1, 0, False, False, False, False),
        State("New York", 26, 11, 0, False, False, False, False),
        State("Oregon", 6, 2, 0, False, False, False, False),
        State("Rhode Island", 2, 0, 0, False, False, False, False),
        State("Vermont", 1, 0, 0, True, False, False, False),  # R governor but voted Biden
        State("Washington", 10, 3, 0, False, False, False, False),
        State("Hawaii", 2, 0, 0, False, False, False, False),

        # DC (special case)
        State("District of Columbia", 0, 0, 0, False, False, False, False, is_state=False, is_dc=True),
    ]

    print(len(states))

    extra_violations = ["New Jersey"]

    for state in states:
        delegates = calculate_delegates(state, state.name in extra_violations)
        print(state.name, sum(delegates), delegates)


    '''
    Delegate Discrepancies:
    State observed instead of true
    Alaska 22 at-large instead of 23
    Florida 39 at-large instead of 38
    Georgia 16 at-large instead of 14
    Kansas 25 at-large instead of 24
    Kentucky 26 at-large instead of 24
    Louisiana 25 at-large instead of 26
    Montana 23 at large instead of 22
    West Virginia 22 at large instead of 23
    North Carolina 30 at large instead of 29
    Wisconsin 12 at large instead of 14
    Maine 12 at large instead of 11
    Michigan 11 at large instead of 13
    Nevada 13 at large instead of 11
    New Hampshire 12 at large instead of 13
    Virginia 10 at large instead of 12
    '''

if __name__ == "__main__":
    main()