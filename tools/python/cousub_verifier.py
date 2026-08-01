import json
import csv
import random as rand
import re
from data.states_data import states_data

LSAD_types = {
    "00" : "Undefined",
    "03" : "City and Borough",
    "04" : "Borough",
    "05" : "Census Area",
    "06" : "County",
    "07" : "District",
    "10" : "Island",
    "12" : "Municipality",
    "13" : "Municipio",
    "15" : "Parish",
    "20" : "Barrio",
    "21" : "Borough",
    "22" : "Census County Division",
    "23" : "Census Subarea",
    "24" : "Census Subdistrict",
    "25" : "City",
    "26" : "County",
    "27" : "District",
    "28" : "District",
    "29" : "Precinct",
    "30" : "Precinct",
    "31" : "Gore",
    "32" : "Grant",
    "36" : "Location",
    "37" : "Municipality",
    "39" : "Plantation",
    "41" : "Barrio-pueblo",
    "42" : "Purchase",
    "43" : "Town",
    "44" : "Township",
    "45" : "Township",
    "46" : "Unorganized Territory",
    "47" : "Village",
    "49" : "Charter Township",
    "51" : "Subbarrio",
    "53" : "City and Borough",
    "55" : "Comunidad",
    "57" : "Census-Designated Place",
    "62" : "Zona Urbana",
    "68" : "Region",
    "69" : "Division",
    "70" : "Urban Growth Area",
    "71" : "Consolidated Metropolitan Statistical Area",
    "72" : "Metropolitan Statistical Area",
    "73" : "Primary Metropolitan Statistical Area",
    "74" : "New England County Metropolitan Area",
    "75" : "Urbanized Area",
    "76" : "Urban Cluster",
    "77" : "Alaska Native Regional Corporation",
    "78" : "Hawaiian Home Land",
    "79" : "Alaska Native Village Statistical Area",
    "80" : "Tribal Designated Statistical Area",
    "81" : "American Indian Area Colony",
    "82" : "American Indian Area Community",
    "83" : "American Indian Joint Use Area",
    "84" : "American Indian Area Pueblo",
    "85" : "American Indian Area Rancheria",
    "86" : "American Indian Area Reservation",
    "87" : "American Indian Area Reserve",
    "88" : "Oklahoma Tribal Statistical Area",
    "89" : "American Indian Area Trust Land",
    "90" : "Joint-Use Oklahoma Tribal Statistical Area",
    "91" : "American Indian Area Ranch",
    "92" : "State-designated Tribal Statistical Area",
    "93" : "Indian Village",
    "94" : "American Indian Area Village",
    "95" : "Indian Community",
    "96" : "Indian Reservation",
    "97" : "Indian Rancheria",
    "98" : "Indian Colony",
    "99" : "Pueblo de",
    "9C" : "Pueblo of",
    "9D" : "Ranch Reservation",
    "9E" : "Rancheria Reservation",
    "9F" : "American Indian Area Ranches",
    "B1" : "Balance of County",
    "B2" : "Balance of Parish",
    "B3" : "Balance of Borough",
    "B4" : "Balance of Census Area",
    "B5" : "Town Balance",
    "B6" : "Township Balance",
    "B7" : "Charter Township Balance",
    "B8" : "Balance of",
}

def get_data_for_index(data: list[tuple[str, ...]], index: int, field: str) -> float:
    for line in data:
        if line[0].lower() == field.lower():
            return float(line[index].replace(',',''))
    raise IndexError(f'Failed to find field {field}')

class CountySubdivision:
    def __init__(self, name: str, data: list[tuple[str, ...]], index: int):
        self._name = name + str(rand.randint(0, 1000000))
        self.totalPopulation = int(get_data_for_index(data, index, 'Total population'))
        self.male5under = int(get_data_for_index(data, index, 'Male population Under 5 years'))
        self.male5to9 = int(get_data_for_index(data, index, 'Male population 5 to 9 years'))
        self.male10to14 = int(get_data_for_index(data, index, 'Male population 10 to 14 years'))
        self.male15to19 = int(get_data_for_index(data, index, 'Male population 15 to 19 years'))
        self.male20to24 = int(get_data_for_index(data, index, 'Male population 20 to 24 years'))
        self.male25to29 = int(get_data_for_index(data, index, 'Male population 25 to 29 years'))
        self.male30to34 = int(get_data_for_index(data, index, 'Male population 30 to 34 years'))
        self.male35to39 = int(get_data_for_index(data, index, 'Male population 35 to 39 years'))
        self.male40to44 = int(get_data_for_index(data, index, 'Male population 40 to 44 years'))
        self.male45to49 = int(get_data_for_index(data, index, 'Male population 45 to 49 years'))
        self.male50to54 = int(get_data_for_index(data, index, 'Male population 50 to 54 years'))
        self.male55to59 = int(get_data_for_index(data, index, 'Male population 55 to 59 years'))
        self.male60to64 = int(get_data_for_index(data, index, 'Male population 60 to 64 years'))
        self.male65to69 = int(get_data_for_index(data, index, 'Male population 65 to 69 years'))
        self.male70to74 = int(get_data_for_index(data, index, 'Male population 70 to 74 years'))
        self.male75to79 = int(get_data_for_index(data, index, 'Male population 75 to 79 years'))
        self.male80to84 = int(get_data_for_index(data, index, 'Male population 80 to 84 years'))
        self.male85over = int(get_data_for_index(data, index, 'Male population 85 years and over'))
        self.female5under = int(get_data_for_index(data, index, 'Female population Under 5 years'))
        self.female5to9 = int(get_data_for_index(data, index, 'Female population 5 to 9 years'))
        self.female10to14 = int(get_data_for_index(data, index, 'Female population 10 to 14 years'))
        self.female15to19 = int(get_data_for_index(data, index, 'Female population 15 to 19 years'))
        self.female20to24 = int(get_data_for_index(data, index, 'Female population 20 to 24 years'))
        self.female25to29 = int(get_data_for_index(data, index, 'Female population 25 to 29 years'))
        self.female30to34 = int(get_data_for_index(data, index, 'Female population 30 to 34 years'))
        self.female35to39 = int(get_data_for_index(data, index, 'Female population 35 to 39 years'))
        self.female40to44 = int(get_data_for_index(data, index, 'Female population 40 to 44 years'))
        self.female45to49 = int(get_data_for_index(data, index, 'Female population 45 to 49 years'))
        self.female50to54 = int(get_data_for_index(data, index, 'Female population 50 to 54 years'))
        self.female55to59 = int(get_data_for_index(data, index, 'Female population 55 to 59 years'))
        self.female60to64 = int(get_data_for_index(data, index, 'Female population 60 to 64 years'))
        self.female65to69 = int(get_data_for_index(data, index, 'Female population 65 to 69 years'))
        self.female70to74 = int(get_data_for_index(data, index, 'Female population 70 to 74 years'))
        self.female75to79 = int(get_data_for_index(data, index, 'Female population 75 to 79 years'))
        self.female80to84 = int(get_data_for_index(data, index, 'Female population 80 to 84 years'))
        self.female85over = int(get_data_for_index(data, index, 'Female population 85 years and over'))
        self.populationWhiteAlone = int(get_data_for_index(data, index, 'One Race White'))
        self.populationBlackAlone = int(get_data_for_index(data, index, 'One Race Black or African American'))
        self.populationAmericanIndianAlone = int(get_data_for_index(data, index, 'One Race American Indian and Alaska Native'))
        self.populationAsianAlone = int(get_data_for_index(data, index, 'One Race Asian'))
        self.populationNativeHawaiianAlone = int(get_data_for_index(data, index, 'One Race Native Hawaiian and Other Pacific Islander'))
        self.populationOtherRaceAlone = int(get_data_for_index(data, index, 'One Race Some Other Race'))
        self.populationTwoOrMoreRaces = int(get_data_for_index(data, index, 'Two or More Races'))
        self.populationWhiteOrCombination = int(get_data_for_index(data, index, 'White alone or in combination with one or more other races'))
        self.populationBlackOrCombination = int(get_data_for_index(data, index, 'Black or African American alone or in combination with one or more other races'))
        self.populationAmericanIndianOrCombination = int(get_data_for_index(data, index, 'American Indian and Alaska Native alone or in combination with one or more other races'))
        self.populationAsianOrCombination = int(get_data_for_index(data, index, 'Asian alone or in combination with one or more other races'))
        self.populationNativeHawaiianOrCombination = int(get_data_for_index(data, index, 'Native Hawaiian and Other Pacific Islander alone or in combination with one or more other races'))
        self.populationOtherRaceOrCombination = int(get_data_for_index(data, index, 'Some Other Race alone or in combination with one or more other races'))
        self.populationHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino (of any race)'))
        self.populationNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino'))
        self.populationWhiteHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino White alone'))
        self.populationBlackHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino Black or African American alone'))
        self.populationAmericanIndianHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino American Indian and Alaska Native alone'))
        self.populationAsianHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino Asian alone'))
        self.populationNativeHawaiianHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino Native Hawaiian and Other Pacific Islander alone'))
        self.populationOtherRaceHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino Some Other Race alone'))
        self.populationTwoOrMoreRacesHispanic = int(get_data_for_index(data, index, 'Hispanic or Latino Two or More Races'))
        self.populationWhiteNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino White alone'))
        self.populationBlackNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino Black or African American alone'))
        self.populationAmericanIndianNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino American Indian and Alaska Native alone'))
        self.populationAsianNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino Asian alone'))
        self.populationNativeHawaiianNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino Native Hawaiian and Other Pacific Islander alone'))
        self.populationOtherRaceNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino Some Other Race alone'))
        self.populationTwoOrMoreRacesNotHispanic = int(get_data_for_index(data, index, 'Not Hispanic or Latino Two or More Races'))
        self.relationshipHouseholder = int(get_data_for_index(data, index, 'Householder'))
        self.livesWithOppositeSexSpouse = int(get_data_for_index(data, index, 'Opposite-sex spouse'))
        self.livesWithSameSexSpouse = int(get_data_for_index(data, index, 'Same-sex spouse'))
        self.livesWithOppositeSexUnmarriedPartner = int(get_data_for_index(data, index, 'Opposite-sex unmarried partner'))
        self.livesWithSameSexUnmarriedPartner = int(get_data_for_index(data, index, 'Same-sex unmarried partner'))
        self.childLivesWithHouseholder = int(get_data_for_index(data, index, 'Child [2]'))
        self.childUnder18LivesWithHouseholder = int(get_data_for_index(data, index, 'Under 18 years'))
        self.grandchildLivesWithHouseholder = int(get_data_for_index(data, index, 'Grandchild'))
        self.grandchildUnder18LivesWithHouseholder = int(get_data_for_index(data, index, 'Grandchild Under 18 years'))
        self.livesWithOtherRelatives = int(get_data_for_index(data, index, 'Other relatives'))
        self.livesWithNonrelatives = int(get_data_for_index(data, index, 'Nonrelatives'))
        self.institutionalizedMale = int(get_data_for_index(data, index, 'Institutionalized population Male'))
        self.institutionalizedFemale = int(get_data_for_index(data, index, 'Institutionalized population Female'))
        self.groupQuartersNoninstitutionalizedMale = int(get_data_for_index(data, index, 'Noninstitutionalized population Male'))
        self.groupQuartersNoninstitutionalizedFemale = int(get_data_for_index(data, index, 'Noninstitutionalized population Female'))
        self.householdsMarriedCouples = int(get_data_for_index(data, index, 'Married couple household'))
        self.householdsMarriedCouplesWithOwnChildrenUnder18 = int(get_data_for_index(data, index, 'Married couple household With own children under 18 [3]'))
        self.householdsCohabitingCouples = int(get_data_for_index(data, index, 'Cohabiting couple household'))
        self.householdsCohabitingCouplesWithChildrenUnder18 = int(get_data_for_index(data, index, 'Cohabiting couple household With own children under 18 [3]'))
        self.maleHouseholderNoSpouseOrPartner = int(get_data_for_index(data, index, 'Male householder, no spouse or partner present:'))
        self.maleHouseholderLivingAlone = int(get_data_for_index(data, index, 'Male householder, no spouse or partner present Living alone'))
        self.maleHouseholderLivingAloneOver65 = int(get_data_for_index(data, index, 'Male householder, no spouse or partner present Living alone 65 years and over'))
        self.maleHouseholderLivingAloneWithChildrenUnder18 = int(get_data_for_index(data, index, 'Male householder, no spouse or partner present With own children under 18 [3]'))
        self.femaleHouseholderNoSpouseOrPartner = int(get_data_for_index(data, index, 'Female householder, no spouse or partner present:'))
        self.femaleHouseholderLivingAlone = int(get_data_for_index(data, index, 'Female householder, no spouse or partner present Living alone'))
        self.femaleHouseholderLivingAloneOver65 = int(get_data_for_index(data, index, 'Female householder, no spouse or partner present Living alone 65 years and over'))
        self.femaleHouseholderLivingAloneWithChildrenUnder18 = int(get_data_for_index(data, index, 'Female householder, no spouse or partner present With own children under 18 [3]'))
        self.householdsWithIndividualsUnder18 = int(get_data_for_index(data, index, 'Households with individuals under 18 years'))
        self.householdsWithIndividuals65over = int(get_data_for_index(data, index, 'Households with individuals 65 years and over'))
        self.occupiedHousingUnits = int(get_data_for_index(data, index, 'Occupied housing units'))
        self.vacantHousingForRent = int(get_data_for_index(data, index, 'For rent'))
        self.vacantHousingRentedNotOccupied = int(get_data_for_index(data, index, 'Rented, not occupied'))
        self.vacantHousingForSaleOnly = int(get_data_for_index(data, index, 'For sale only'))
        self.vacantHousingSoldNotOccupied = int(get_data_for_index(data, index, 'Sold, not occupied'))
        self.vacantHousingSeasonalRecreational = int(get_data_for_index(data, index, 'For seasonal, recreational, or occasional use'))
        self.vacantHousingOtherVacancy = int(get_data_for_index(data, index, 'All other vacants'))
        self.homeownerVacancyRate = float(get_data_for_index(data, index, 'Homeowner vacancy rate (percent) [4]'))
        self.rentalVacancyRate = float(get_data_for_index(data, index, 'Rental vacancy rate (percent) [5]'))
        self.ownerOccupiedHousingUnits = int(get_data_for_index(data, index, 'Owner-occupied housing units'))
        self.renterOccupiedHousingUnits = int(get_data_for_index(data, index, 'Renter-occupied housing units'))

    def to_dict(self):
        return {
            'totalPopulation': self.totalPopulation,
            'male5under': self.male5under,
            'male5to9': self.male5to9,
            'male10to14': self.male10to14,
            'male15to19': self.male15to19,
            'male20to24': self.male20to24,
            'male25to29': self.male25to29,
            'male30to34': self.male30to34,
            'male35to39': self.male35to39,
            'male40to44': self.male40to44,
            'male45to49': self.male45to49,
            'male50to54': self.male50to54,
            'male55to59': self.male55to59,
            'male60to64': self.male60to64,
            'male65to69': self.male65to69,
            'male70to74': self.male70to74,
            'male75to79': self.male75to79,
            'male80to84': self.male80to84,
            'male85over': self.male85over,
            'female5under': self.female5under,
            'female5to9': self.female5to9,
            'female10to14': self.female10to14,
            'female15to19': self.female15to19,
            'female20to24': self.female20to24,
            'female25to29': self.female25to29,
            'female30to34': self.female30to34,
            'female35to39': self.female35to39,
            'female40to44': self.female40to44,
            'female45to49': self.female45to49,
            'female50to54': self.female50to54,
            'female55to59': self.female55to59,
            'female60to64': self.female60to64,
            'female65to69': self.female65to69,
            'female70to74': self.female70to74,
            'female75to79': self.female75to79,
            'female80to84': self.female80to84,
            'female85over': self.female85over,
            'populationWhiteAlone': self.populationWhiteAlone,
            'populationBlackAlone': self.populationBlackAlone,
            'populationAmericanIndianAlone': self.populationAmericanIndianAlone,
            'populationAsianAlone': self.populationAsianAlone,
            'populationNativeHawaiianAlone': self.populationNativeHawaiianAlone,
            'populationOtherRaceAlone': self.populationOtherRaceAlone,
            'populationTwoOrMoreRaces': self.populationTwoOrMoreRaces,
            'populationWhiteOrCombination': self.populationWhiteOrCombination,
            'populationBlackOrCombination': self.populationBlackOrCombination,
            'populationAmericanIndianOrCombination': self.populationAmericanIndianOrCombination,
            'populationAsianOrCombination': self.populationAsianOrCombination,
            'populationNativeHawaiianOrCombination': self.populationNativeHawaiianOrCombination,
            'populationOtherRaceOrCombination': self.populationOtherRaceOrCombination,
            'populationHispanic': self.populationHispanic,
            'populationNotHispanic': self.populationNotHispanic,
            'populationWhiteHispanic': self.populationWhiteHispanic,
            'populationBlackHispanic': self.populationBlackHispanic,
            'populationAmericanIndianHispanic': self.populationAmericanIndianHispanic,
            'populationAsianHispanic': self.populationAsianHispanic,
            'populationNativeHawaiianHispanic': self.populationNativeHawaiianHispanic,
            'populationOtherRaceHispanic': self.populationOtherRaceHispanic,
            'populationTwoOrMoreRacesHispanic': self.populationTwoOrMoreRacesHispanic,
            'populationWhiteNotHispanic': self.populationWhiteNotHispanic,
            'populationBlackNotHispanic': self.populationBlackNotHispanic,
            'populationAmericanIndianNotHispanic': self.populationAmericanIndianNotHispanic,
            'populationAsianNotHispanic': self.populationAsianNotHispanic,
            'populationNativeHawaiianNotHispanic': self.populationNativeHawaiianNotHispanic,
            'populationOtherRaceNotHispanic': self.populationOtherRaceNotHispanic,
            'populationTwoOrMoreRacesNotHispanic': self.populationTwoOrMoreRacesNotHispanic,
            'relationshipHouseholder': self.relationshipHouseholder,
            'livesWithOppositeSexSpouse': self.livesWithOppositeSexSpouse,
            'livesWithSameSexSpouse': self.livesWithSameSexSpouse,
            'livesWithOppositeSexUnmarriedPartner': self.livesWithOppositeSexUnmarriedPartner,
            'livesWithSameSexUnmarriedPartner': self.livesWithSameSexUnmarriedPartner,
            'childLivesWithHouseholder': self.childLivesWithHouseholder,
            'childUnder18LivesWithHouseholder': self.childUnder18LivesWithHouseholder,
            'grandchildLivesWithHouseholder': self.grandchildLivesWithHouseholder,
            'grandchildUnder18LivesWithHouseholder': self.grandchildUnder18LivesWithHouseholder,
            'livesWithOtherRelatives': self.livesWithOtherRelatives,
            'livesWithNonrelatives': self.livesWithNonrelatives,
            'institutionalizedMale': self.institutionalizedMale,
            'institutionalizedFemale': self.institutionalizedFemale,
            'groupQuartersNoninstitutionalizedMale': self.groupQuartersNoninstitutionalizedMale,
            'groupQuartersNoninstitutionalizedFemale': self.groupQuartersNoninstitutionalizedFemale,
            'householdsMarriedCouples': self.householdsMarriedCouples,
            'householdsMarriedCouplesWithOwnChildrenUnder18': self.householdsMarriedCouplesWithOwnChildrenUnder18,
            'householdsCohabitingCouples': self.householdsCohabitingCouples,
            'householdsCohabitingCouplesWithChildrenUnder18': self.householdsCohabitingCouplesWithChildrenUnder18,
            'maleHouseholderNoSpouseOrPartner': self.maleHouseholderNoSpouseOrPartner,
            'maleHouseholderLivingAlone': self.maleHouseholderLivingAlone,
            'maleHouseholderLivingAloneOver65': self.maleHouseholderLivingAloneOver65,
            'maleHouseholderLivingAloneWithChildrenUnder18': self.maleHouseholderLivingAloneWithChildrenUnder18,
            'femaleHouseholderNoSpouseOrPartner': self.femaleHouseholderNoSpouseOrPartner,
            'femaleHouseholderLivingAlone': self.femaleHouseholderLivingAlone,
            'femaleHouseholderLivingAloneOver65': self.femaleHouseholderLivingAloneOver65,
            'femaleHouseholderLivingAloneWithChildrenUnder18': self.femaleHouseholderLivingAloneWithChildrenUnder18,
            'householdsWithIndividualsUnder18': self.householdsWithIndividualsUnder18,
            'householdsWithIndividuals65over': self.householdsWithIndividuals65over,
            'occupiedHousingUnits': self.occupiedHousingUnits,
            'vacantHousingForRent': self.vacantHousingForRent,
            'vacantHousingRentedNotOccupied': self.vacantHousingRentedNotOccupied,
            'vacantHousingForSaleOnly': self.vacantHousingForSaleOnly,
            'vacantHousingSoldNotOccupied': self.vacantHousingSoldNotOccupied,
            'vacantHousingSeasonalRecreational': self.vacantHousingSeasonalRecreational,
            'vacantHousingOtherVacancy': self.vacantHousingOtherVacancy,
            'homeownerVacancyRate': self.homeownerVacancyRate,
            'rentalVacancyRate': self.rentalVacancyRate,
            'ownerOccupiedHousingUnits': self.ownerOccupiedHousingUnits,
            'renterOccupiedHousingUnits': self.renterOccupiedHousingUnits,
        }

class Color:
    def __init__(self, r: int | str, g: int | str | None = None, b: int | str | None = None):
        if isinstance(r, str) and g is None and b is None:
            hex_string = r.replace("#", "")
            self.r = int(hex_string[0:2], 16)
            self.g = int(hex_string[2:4], 16)
            self.b = int(hex_string[4:6], 16)
        else:
            self.r = int(r, 16) if type(r) is str else r
            self.g = int(g, 16) if type(g) is str else g
            self.b = int(b, 16) if type(b) is str else b

    def __getitem__(self, index):
        return [self.r, self.g, self.b][index]

    def __setitem__(self, index, value: int | str):
        if index == 0:
            self.r = int(value, 16) if type(value) is str else value
        elif index == 1:
            self.g = int(value, 16) if type(value) is str else value
        elif index == 2:
            self.b = int(value, 16) if type(value) is str else value
        else: raise IndexError(f'index {index} is out of range for Color')

    def __str__(self):
        return f'#{hex(self.r)[2:].rjust(2,'0')}{hex(self.g)[2:].rjust(2,'0')}{hex(self.b)[2:].rjust(2,'0')}'

    def __eq__(self, other):
        return self.r == other.r and self.g == other.g and self.b == other.b

    def __hash__(self):
        return hash((self.r, self.g, self.b))

def convert_color(obj):
    if isinstance(obj, Color):
        return str(obj)
    raise TypeError(f'Object of type {type(obj)} is not JSON serializable')

def sq_meters_to_sq_miles(sq_meters: float) -> float:
    sq_kilometers = sq_meters / 1_000_000
    sq_miles = sq_kilometers / 2.59
    return sq_miles

def merge_colors(color1: Color, color2: Color, alpha255: int = 255) -> Color:
    channel_result = lambda i: (alpha255 * color1[i] + (255 - alpha255) * color2[i]) // 255
    return Color(channel_result(0), channel_result(1), channel_result(2))

census_data_files = {
    '01': 'decennial_alabama_cousub',
    '02': 'decennial_alaska_cousub',
    '04': 'decennial_arizona_cousub',
    '05': 'decennial_arkansas_cousub',
    '06': 'decennial_california_cousub',
    '08': 'decennial_colorado_cousub',
    '09': 'decennial_connecticut_cousub',
    '10': 'decennial_delaware_cousub',
    '11': 'decennial_district_of_columbia_cousub',
    '12': 'decennial_florida_cousub',
    '13': 'decennial_georgia_cousub',
    '15': 'decennial_hawaii_cousub',
    '16': 'decennial_idaho_cousub',
    '17': 'decennial_illinois_cousub',
    '18': 'decennial_indiana_cousub',
    '19': 'decennial_iowa_cousub',
    '20': 'decennial_kansas_cousub',
    '21': 'decennial_kentucky_cousub',
    '22': 'decennial_louisiana_cousub',
    '23': 'decennial_maine_cousub',
    '24': 'decennial_maryland_cousub',
    '25': 'decennial_massachusetts_cousub',
    '26': 'decennial_michigan_cousub',
    '27': 'decennial_minnesota_cousub',
    '28': 'decennial_mississippi_cousub',
    '29': 'decennial_missouri_cousub',
    '30': 'decennial_montana_cousub',
    '31': 'decennial_nebraska_cousub',
    '32': 'decennial_nevada_cousub',
    '33': 'decennial_new_hampshire_cousub',
    '34': 'decennial_new_jersey_cousub',
    '35': 'decennial_new_mexico_cousub',
    '36': 'decennial_new_york_cousub',
    '37': 'decennial_north_carolina_cousub',
    '38': 'decennial_north_dakota_cousub',
    '39': 'decennial_ohio_cousub',
    '40': 'decennial_oklahoma_cousub',
    '41': 'decennial_oregon_cousub',
    '42': 'decennial_pennsylvania_cousub',
    '44': 'decennial_rhode_island_cousub',
    '45': 'decennial_south_carolina_cousub',
    '46': 'decennial_south_dakota_cousub',
    '47': 'decennial_tennessee_cousub',
    '48': 'decennial_texas_cousub',
    '49': 'decennial_utah_cousub',
    '50': 'decennial_vermont_cousub',
    '51': 'decennial_virginia_cousub',
    '53': 'decennial_washington_cousub',
    '54': 'decennial_west_virginia_cousub',
    '55': 'decennial_wisconsin_cousub',
    '56': 'decennial_wyoming_cousub',
    '72': 'decennial_puerto_rico_cousub',
}

def process_subcounty_census_data() -> dict[str, list[CountySubdivision]]:
    results = {}
    for state_fips, data_filename in census_data_files.items():
        print(f"Processing {state_fips}")
        with open(f'../raw/census/{data_filename}.csv', 'r', encoding='UTF-8') as f:
            reader = csv.reader(f)
            header = next(reader)

            contents = []
            for row in reader:
                contents.append(tuple(row))

            result = []
            offset = 1
            for index, cousub in enumerate(header[offset:]):
                if cousub.endswith('!!Percent'):
                    continue
                try:
                    result.append(CountySubdivision(cousub, contents, index + offset))
                except IndexError as e:
                    print(f"{e}\nEncountered for {cousub}")
                    raise e

            results[state_fips] = result
    return results

def main() -> None:

    print("Reading county subarea base data...")
    with open("../../assets_raw/data/map/cousub.csv") as f:
        lines = f.readlines()
    rows = [line.split(",") for line in lines[1:]]
    lines = []
    for row in rows:
        lines.append([value.strip('"') for value in row])

    new_cousub_colors = {}

    for line in lines:
        state_fips = line[0]
        county_fips = f'{line[0]}{line[1]}'
        cousub_fips = f'{line[0]}-{line[2]}'
        county_datafile_name = f'../../assets_raw/data/map/states/{state_fips}-{states_data[state_fips]['abbreviation']}/{state_fips}_counties.json'

        with open(county_datafile_name, 'r', encoding='UTF-8') as f:
            county_data = json.load(f)
            county_color = Color(county_data[county_fips]['color'])
        cousub_color = Color(line[16])
        new_color = merge_colors(county_color, cousub_color, 176)
        new_cousub_colors[f'{cousub_fips}'] = new_color

    print("Adjusting colors...")
    seen_colors = set()
    for cousub, new_color in new_cousub_colors.items():
        if new_color in seen_colors:
            corrected_color = Color(new_color[0], new_color[1], new_color[2])
            while corrected_color in seen_colors:
                corrected_color = Color(corrected_color[0], corrected_color[1], corrected_color[2]+1)
            new_cousub_colors[cousub] = corrected_color
            seen_colors.add(corrected_color)
        seen_colors.add(new_color)

    print("Processing subcounty census data...")
    census_data = process_subcounty_census_data()
    used_census_data = {}

    print("Preparing area data...")
    results = {}
    for count, line in enumerate(lines):
        if count % 1000 == 0:
            # print(f"{round(count*100/len(lines), 1)}%")
            pass
        state = line[0]
        results.setdefault(state, {})
        cousub_fips = f'{line[0]}-{line[2]}'
        data = {
            "FIPS" : f"{line[0]}-{line[2]}",
            "fullName" : line[7],
            "commonName" : line[6],
            "county" : f"{line[0]}{line[1]}",
            "type" : f"{LSAD_types[line[8]]} ({line[8]})",
            "color" : new_cousub_colors[cousub_fips],
            "squareMileage" : round(sq_meters_to_sq_miles(int(line[12]) + int(line[13])), 3),
        }
        for state_fips, cousubs_data in census_data.items():
            used_census_data.setdefault(state_fips, [])
            found = False
            if state != state_fips: continue
            for cousub_data in cousubs_data:
                if state == state_fips and re.sub('[^a-zA-Z0-9]', '', data['fullName']).lower() in re.sub('[^a-zA-Z0-9]', '', cousub_data._name).lower() and cousub_data._name not in used_census_data[state_fips]:
                    data = data | cousub_data.to_dict()
                    used_census_data[state_fips].append(cousub_data._name)
                    found = True
                    break
            if found: break
        else:
            print(f"Could not find Census data for ({cousub_fips}) {line[7]}")

        results[state][data['FIPS']] = data

    # Check for unused census data
    for state_fips, cousubs_data in census_data.items():
        for cousub_data in cousubs_data:
            if cousub_data._name not in used_census_data[state_fips]:
                print(f"{cousub_data._name} was not used for state {state_fips}")

    print("Writing results...")
    for state, result in results.items():
        print(f'Writing {state}')
        with open(f"../../assets_raw/data/map/states/{state}-{states_data[state]['abbreviation']}/{state}_county_subdivisions.json", "w") as f:
            json.dump(result, f, indent=4, separators=(","," : "), default=convert_color)

    print("Done!")
    return

if __name__ == "__main__":
    main()
