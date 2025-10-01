from datetime import date

class State:
    def __init__(self, name: str, representatives: int, d_vote_2016: int, d_vote_2020: int, d_vote_2024: int, primary_date: date, cluster: bool = False, violation: bool = False):
        self.name = name
        self.d_vote_2016 = d_vote_2016
        self.d_vote_2020 = d_vote_2020
        self.d_vote_2024 = d_vote_2024
        self.representatives = representatives
        self.electoral_votes = representatives + 2
        self.primary_date = primary_date
        self.cluster = cluster
        self.violation = violation

TERRITORY_VOTES_AT_LARGE = {
    "American Samoa" : 6,
    "Democrats Abroad" : 12,
    "Guam" : 3,
    "Northern Marianas" : 6,
    "Puerto Rico" : 44,
    "Virgin Islands" : 6,
}
TERRITORY_VOTES_PLEDGED = {"Democrats Abroad" : 1}
TERRITORY_VOTES_UNPLEDGED = {"Northern Marianas" : 5}

SUPERDELEGATES = {
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

def calculate_delegates(state: State) -> tuple[int, int, int, int]:

    # Compute state allocation factor and base votes

    state_dem_vote_history = (state.d_vote_2016 + state.d_vote_2020 + state.d_vote_2024) / 3
    nation_vote_history = 65853514 + 81283501 + 75017613

    SDV = state_dem_vote_history
    TDV = nation_vote_history
    SEV = state.electoral_votes
    total_electoral_votes = 538

    allocation_factor = 0.5 * ((SDV / TDV) + (SEV / total_electoral_votes))
    roll_call = 3200 # Fixed value set by the DNC

    base_votes = round(allocation_factor * roll_call)

    # Split into district and at-large

    at_large_delegates = round(base_votes * 0.25)
    district_delegates = base_votes - at_large_delegates
    assert at_large_delegates + district_delegates == base_votes

    # Compute pledged PLEOs
    PLEO_delegates = round(base_votes * 0.15)

    # Compute timing and cluster bonuses

    stage_i_start = date(2020, 3, 3)
    stage_ii_start = date(2020, 4, 1)
    stage_iii_start = date(2020, 5, 1)
    
    stage_i_bonus = 0
    stage_ii_bonus = 0.1
    stage_iii_bonus = 0.2
    clustering_bonus = 0.15 # 3 or more neighboring states begin on same date

    bonus = 0
    if state.primary_date > stage_iii_start:
        bonus = stage_iii_bonus * base_votes
    elif state.primary_date > stage_ii_start:
        bonus = stage_ii_bonus * base_votes
    elif state.primary_date > stage_i_start:
        bonus = stage_i_bonus * base_votes
    if state.cluster:
        bonus += clustering_bonus * base_votes
    bonus = round(bonus)

    # Apply bonus to district & at-large

    at_large_bonus = round(bonus * 0.25)
    at_large_bonus = 1 if bonus != 0 and at_large_bonus == 0 else 0
    district_bonus = bonus - at_large_bonus
    assert at_large_bonus + district_bonus == bonus

    total_at_large = at_large_delegates + at_large_bonus
    total_district = district_delegates + district_bonus

    # Add superdelegates (unpledged PLEOs)

    unpledged = SUPERDELEGATES[state.name]

    # Apply penalties

    if state.violation:
        total_at_large = round(total_at_large * 0.5)
        total_district = round(total_district * 0.5)
        PLEO_delegates = round(PLEO_delegates * 0.5)
        unpledged = 0

    # The district delegates are apportioned to the districts according to
    #     state rules, depending one one of four possible formulae criteria.
    #     This depends on each district's past democratic voting history and strength

    return (total_district, total_at_large, PLEO_delegates, unpledged)

    '''
    Total population, average of democratic vote in last two elections
    Vote for democratic vote in most recent presidential and gubernatorial elections
    Average vote for democratic candidates in last two elections and to Democratic Party registration
    Or 1/3 of each of the above

    75% of the base delegation is district
    25% is at-large pledged party officials (PLEO)



    Automatic Delegates / Superdelegates:
    The Democratic National Committee:
    Chairperson and highest ranking officer of another gender of
        each state and Guam, VI, Samoa, Northern Marianas
    200 others apportioned to states in 2-5(a) and 8-3/8-4, at least two
        per state
    National committeeman and national committeewoman from Guam, VI, Samoa,
        Northern Marianas
    Chairperson of Democratic Governors' Association and two additional
        governors one of whom is another gender than the Chairperson, selected
        by the Association
    Democratic leader in the enate and the house, and one member from each body
        of a different gender than the leader
    Chairperson, 5 Vice Chairpersons, National Finance Chair, Treasurer, and
        Secretary of the DNC
    Chairperson of the Democratic Mayors Association and two additional mayors
        one of whom is another gender slected by the Association
    Chairperson and two county officials from the National Democratic County Officials
        organization, representing two genders, selected by the org
    Chairperson and two state legislators of the Democratic Legislative Campaign
        Committee, 2 genders, selected by the Committee
    Chairperson and two municipal officials of the Democratic Municipal Officials,
        2 genders, selected by the Org
    President of the National Federation of Democratic Women, and two selected others
    President of the College Democrats of America and the Vice President who is another gender, elected annually
    Chairperson of the Democratic State Treasurers Association and the Vice Chair who is another gender, selected by the Association
    Chairperson of the Democratic Lieutenant Governors Association and Vice Chair other gender
    Chairperson and Vice Chair of Democratic Association of Secretaries of State
    Chairperson and anotehr attorney general of the Democratic Attorneys General Association
    Chairperson and one other member of another gender of the national Democratic Ethnic
        Coordinating Committee who is not otherwise a member of the DNC
    Chairperson and one other member of another gender of the National Democratic Seniors Coordinating Council
    Chairperson and Vice Chairperson of another gender of the High School Democrats of America
    Democrats Abroad: 1/2 vote for each: Chairman, highest ranking officer of another gender,
        3 National Committeeman, 3 National Committeewomen (4 votes shared total)
    75 or fewer at-large members. 22 by Asian American and Pacific Islander Caucus,
        Black Caucus, Disability Caucus, Hispanic Caucus, LGBTQ Caucus, Native American Caucus,
            regional caucuses, and the Youth Council as decided by the Chair of the National
            Committeee
    The immediate past Chair of the DNC
    
    Apportioned not by Party office:
        Apportioned to the states and Democrats Abroad
        Selected by each State, as equal as possible between committeemen and committeewomen
    
    Others:
    The President and Vice President if Democratic
    All Democratic members of the HoR and Senate
    All Democratic Governors
    All former Democratic Presidents, Vice Presidents, Leaders of the Senate,
        Speakers of the House, Congress Minority Leaders, and Chairs of the DNC

    NO ONE ELSE
    '''

def main() -> None:

    '''
    Sources:
    https://democrats.org/wp-content/uploads/2024/07/2024-Delegate-Selection-Rules.pdf
    https://democrats.org/wp-content/uploads/2025/09/DNC-Charter-Bylaws-08.27.2025.pdf
    https://www.thegreenpapers.com/P20/D-Alloc.phtml#Alph
    https://en.wikipedia.org/wiki/Results_of_the_2020_Democratic_Party_presidential_primaries
    '''

    states = [
        State("Florida", 28, 4504975, 5297045, 4683038, date(2024, 3, 19))
    ]

    for state in states:
        print(calculate_delegates(state))

if __name__ == "__main__":
    main()