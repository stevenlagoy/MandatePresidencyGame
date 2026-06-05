contents = open("cities_table.txt").read()

abbreviation_to_name = {
    "AK": "Alaska",
    "AL": "Alabama",
    "AR": "Arkansas",
    "AZ": "Arizona",
    "CA": "California",
    "CO": "Colorado",
    "CT": "Connecticut",
    "DE": "Delaware",
    "FL": "Florida",
    "GA": "Georgia",
    "HI": "Hawaii",
    "IA": "Iowa",
    "ID": "Idaho",
    "IL": "Illinois",
    "IN": "Indiana",
    "KS": "Kansas",
    "KY": "Kentucky",
    "LA": "Louisiana",
    "MA": "Massachusetts",
    "MD": "Maryland",
    "ME": "Maine",
    "MI": "Michigan",
    "MN": "Minnesota",
    "MO": "Missouri",
    "MS": "Mississippi",
    "MT": "Montana",
    "NC": "North Carolina",
    "ND": "North Dakota",
    "NE": "Nebraska",
    "NH": "New Hampshire",
    "NJ": "New Jersey",
    "NM": "New Mexico",
    "NV": "Nevada",
    "NY": "New York",
    "OH": "Ohio",
    "OK": "Oklahoma",
    "OR": "Oregon",
    "PA": "Pennsylvania",
    "RI": "Rhode Island",
    "SC": "South Carolina",
    "SD": "South Dakota",
    "TN": "Tennessee",
    "TX": "Texas",
    "UT": "Utah",
    "VA": "Virginia",
    "VT": "Vermont",
    "WA": "Washington",
    "WI": "Wisconsin",
    "WV": "West Virginia",
    "WY": "Wyoming",
    "DC": "District of Columbia",
    "AS": "American Samoa",
    "GU": "Guam GU",
    "MP": "Northern Mariana Islands",
    "PR": "Puerto Rico PR",
    "VI": "U.S. Virgin Islands",
}
name_to_abbreviation = {v: k for k, v in abbreviation_to_name.items()}

class City:
  def __init__(self, name, state, pop2020, pop2023, growthRate, area, density, loc):
    self.name = name
    self.state_name = state if len(state) != 2 else abbreviation_to_name[state]
    self.state_abbr = state if len(state) == 2 else name_to_abbreviation[state]
    self.population2020 = pop2020
    self.population2023 = pop2023
    self.population2027 = 0
    self.growthRate = growthRate
    self.landArea = area
    self.density = density
    self.loc = loc

outputfilename = "src\\main\\resources\\cities.out"

contents = [line for line in contents.split("\n") if line]
start_index, end_index = 760, 8000
view_window = 20
contents = contents[start_index : end_index]

important_lines = [i for i,x in enumerate(contents) if x == "<tr>"]
cities = []
months_between = 39
for line in important_lines:
  #print(contents[line:line+view_window])
  try:
    name = contents[line+1].split("title=")[1].split(">")[1].split("<")[0]
    #print(name)
    state = contents[line+3].split("title=")[1].split(">")[1].split("<")[0]
    #print(state)
    pop2023 = int(contents[line+5].split(">")[1].replace(",",""))
    #print(pop2023)
    pop2020 = int(contents[line+7].split(">")[1].replace(",",""))
    #print(pop2020)
    growthRate = round(float(contents[line+9].split(">")[4].split("<")[0].replace("%","").replace("+","").replace("−","-").strip()) / months_between * 1000) / 10
    #print(growthRate)
    area = contents[line+11].split(">")[1]
    #print(area)
    density = contents[line+15].split(">")[1].replace(",","")
    #print(density)
    loc = tuple(contents[line+19].split("span class=\"geo\">")[1].split("<")[0].split("; "))
    #print(loc)
    cities.append(City(name, state, pop2020, pop2023, growthRate, area, density, loc))
  except IndexError:
    break

for city in cities:
  #print(city.name + ", " + city.state)
  #print("growth rate = " + str(city.growthRate*100) + "%")
  #print("population 2020 = " + str(city.population2020))
  #print("pop estimate 2023 = " + str(city.population2023))
  pop_estimate = round(city.population2020 * (101 + city.growthRate*81)/100)
  city.population2027 = pop_estimate
  #print("pop estimate 2027 = " + str(pop_estimate))
  #print("")


with open(outputfilename, "w") as file:
  file.write("{\n")
  for city in cities:
    file.write("\t\"" + city.name + ", " + city.state_abbr + "\" : {\n")
    file.write("\t\t\"name\" : \"" + city.name + "\",\n")
    file.write("\t\t\"state\" : \"" + city.state_name + "\",\n")
    file.write("\t\t\"population2020\" : " + str(city.population2020) + ",\n")
    file.write("\t\t\"population2023\" : " + str(city.population2023) + ",\n")
    file.write("\t\t\"population2027\" : " + str(city.population2027) + ",\n")
    file.write("\t\t\"popGrowthRate%\" : " + str(city.growthRate) + ",\n")
    file.write("\t\t\"landArea2020\" : " + str(city.landArea) + ",\n")
    file.write("\t\t\"density2020\" : " + str(city.density) + ",\n")
    file.write("\t\t\"location\" : [" + city.loc[0] + ", " + city.loc[1] + "]\n")
    file.write("\t},\n")
  file.write("}")

with open(outputfilename) as file:
  print(file.read())
