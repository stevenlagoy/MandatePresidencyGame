# States

### Attribution

### Description

### Fields

- `FIPS` = The American National Standards Institute (ANSI) Federal Information Processing Series (FIPS) code, a unique 2-digit number (which may have a leading zero) identifying the state.
- `commonName` = The name used regularly for the state. In most cases, should not include qualifiers like "State of", "Commonwealth of", etc., but may use these if the most common form of the state's name does include these words (I.E. "District of Columbia").
- `fullName` = The official name of a state defined by the constitution of that state. May include qualifiers like "State of", "Commonwealth of", etc.
- `abbreviation` = The two-letter abbreviation used by the United States Postal Service to identify the state since 1970.
- `demonymicNoun` = Word or words describing a single person from the state.
- `demonymicPluralNoun` = Word or words describing two or more people from the state.
- `demonymicAdjective` = Word or words describing places and things from or pertaining to the state. These should be of the form "the [demonym] government", "a [demonym] road".
- `nicknames` = Array of traditional nicknames for the state, which may be officially declared through state legislation, semi-official without legislative codification, or unofficial used by residents and popular culture. These should be ordered by popularity, with the official or most common nickname listed first.
- `mottos` = Array of motto objects. These should be ordered by popularity, with the object contianing the most official motto listed first. The array may be empty if there is no state or territorial motto.
  - `motto` = Motto defined by the state legislature or found on the state seal or state flag.
  - `translation` = When the motto is not originally in English, the official translation of the motto into English.
  - `language` = When the motto is not originally in English, the language of the motto.
  - `adoptionDate` = Date on which the motto was adopted.
- `flagFileObverse` = A path from `assets/gfx/flags/` which points to the image file for the state's flag. The path should end with a filename including `.png` (preferred), `.jpg`, `.jpeg`, `.tif`, `.tiff`, `.bmp`, `.webp`, or `.avif`. The path should not include `./` or `../`. This field should not be present if either of `flagFileObverse` or `flagFileReverse` are present.
- `flagFileObverse` = A path from `assets/gfx/flags/` which points to the image file for the obverse of the state's flag. The path should end with a filename including `.png` (preferred), `.jpg`, `.jpeg`, `.tif`, `.tiff`, `.bmp`, `.webp`, or `.avif`. The path should not include `./` or `../`.
- `flagFileReverse` = A path from `assets/gfx/flags/` which points to the image file for the reverse of the state's flag. The path should end with a filename including `.png` (preferred), `.jpg`, `.jpeg`, `.tif`, `.tiff`, `.bmp`, `.webp`, or `.avif`. The path should not include `./` or `../`.
- `capital` = The full name, including county and state, of the capital city of the state. This city may not be located within the state, as is the case for the United States Minor Outlying Islands, which are administered from Washington, DC.
- `federalCourtCircuit` = Number of the appelate federal court circuit which hear appeals from the U.S. district courts within the state.
- `censusDivision` = Unique name of the Census Division the state is within.
- `descriptors` = Array of descriptors' unique names which apply to the entire state.
- `allowedSubdivisionTypes` = Array of allowed types for independent county subdivisions in the state. 
- `allowedPlaceTypes` = Array of allowed types for dependent places in the state.
- `government` = Object describing the government of the state.
  - `name` = Name of the government, like "Government of [state]"
  - `constitution` = Name of the constitution of the government, like "Constitution of the State of [state]"
  - `branches` = Object containing information about the branches of the government.
    - `executiveBranch` = Object containing information about the executive branch of the government.
    - `legislativeBranch` = Object containing information about the legislative branch of the government.
    - `judicialBranch` = Object containing information about the judicial branch of the government.

### Sources

- https://www.census.gov/library/reference/code-lists/ansi.html
- https://about.usps.com/who/profile/history/state-abbreviations.htm
- https://www.govinfo.gov/content/pkg/GPO-STYLEMANUAL-2016/pdf/GPO-STYLEMANUAL-2016.pdf
- https://en.wikipedia.org/wiki/List_of_U.S._state_and_territory_nicknames
- https://en.wikipedia.org/wiki/50_State_quarters
- https://en.wikipedia.org/wiki/List_of_U.S._state_and_territory_mottos
