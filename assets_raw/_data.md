# Data

`~/data/`

### Attribution

**Author**: Steven LaGoy

**Created**: 01 August 2026

**Modified**: 04 September 2026

### Description

The `data/` directory contains most information used to create the map, characters, politics, and
other systems in *Mandate*. 

### Map Projection

The map projection for these datafiles was selected as ESRI:102004 - USA Contiguous Lambert
Conformal Conic. This was selected to minimize distortion of the continental United States, which
is the primary focus area of Mandate: Race for the Presidency.

### Map Extents

The edges of the map were selected based on a rectangle including all United States territory as of
2026 given a Lambert Conformal Conic projection, plus some padding on the edges. The corners of the
map are:
- NW corner just west of the Mariana Islands.
- SW corner just south of American Samoa.
- NE corner in the Greenland Sea north of Iceland.
- SE corner in the Caribbean Sea south of Puerto Rico and the US Virgin Islands.

### Color Channels

Alpha, Red, Green, and Blue are available as color channels in map data files. Value can be used
for a monochromatic scale on one or more channels. It is not suggested to use hue as a channel. In
general, ordinal / numerical data should be communicated using one channel when possible.
Colors are read by the game engine in argb8888 format, meaning that color channel values can range
from 0 to 255. This allows for 16,777,216 unique colors with the RGB channels, or 4,294,967,296
unique colors with the ARGB channels.

### Tools

- JetBrains IntelliJ IDEA - https://www.jetbrains.com/idea/
- Paint.NET 5 (latest) - https://paint.net/
    - BoltBait GPU Accelerated Plugin Pack - https://forums.paint.net/BoltBaitPluginPack
    - Kris Vandermotten's Paint.NET Effects - https://vandermotten.be/paintdotnet
- Inkscape
- QGIS Desktop 3.34 - https://qgis.org/
    - Color to Attribute - https://plugins.qgis.org/plugins/color_attribute/
- Notepad++ - https://notepad-plus-plus.org/
- GitHub - https://github.com/

### Meta

- All information for one position, location, or body should be located within the same file. Do
not use state keys in data files, instead use the data file for that state.
- Keys should mostly be in camelCase. snake_case is acceptable when the only purpose of the key is
for uniqueness, and it will never be parsed specifically by key. JSON reading code should make an
effort to accept any valid casing of keys.
- Consider edge cases early. Aim for accuracy, as the need for more parsing is considered
preferable to ambiguity for the sake of conciseness.
- Favor upwards references. Instead of having a large list of the counties or county FIPS of a
state, have the data files for the counties reference the state. Downwards reference is acceptable
when there are a small number of references (<3 is ideal).
- Create Markdown metadata files to explain the structure of files, with expected keys and 
acceptable value types.
  - Markdown files for data files (like JSON) should be in the same directory and should have the
  same or a very similar name as the file they describe (I.E. holidays.md describes holidays.json
  and is in the same directory).
  - Markdown files for directories should be located in that directory's parent directory and have
  the same or a very similar name as the directory they describe, with the addition of a leading
  underscore (I.E. _states.md describes states/ and is in the same parent directory).
  - Markdown files should begin with a `# Title` which matches the filename, in title case and
  possibly with added punctuation.
  - After the title, write the location of the described file or directory (which should be the
  same) as the Markdown file itself. These should be in block text and begin with `~/`, which
  refers to the `assets` directory. Directories should always be followed by a forward slash `/`
  and filenames should end with an extension `.type`.
  - After the title and location, Markdown files should have an `### Attribution` subsection which
  contains `**Author**: Author Name`, `**Created**: Date`, and `**Modified**: Date`.
  - Most Markdown files should have a `### Description` subsection which describes the file or directory.
  - Most Markdown files should end with a `### Data Sources` subsection which lists the sources
  used for the file, or for the directory when one source was used for everything in that
  directory. When a Markdown file is within a directory which is described by a Markdown file which
  itself includes sources, all those sources also apply to the first file.
  - Markdown files should have a single horizontal line `---` at the bottom, after all content and
  a blank line. Horizontal lines should not be used anywhere else in a Markdown file.
  - Lines in Markdown files should aim not to contain more than 100 characters, and never more than 120.
  Lines with more than 100 characters which cannot be split (like hyperlinks) can be more than 100
  characters in length, but no other text should be present on that line. Avoid lines with fewer
  than 10 characters, even if it means exceeding 100 characters on another line. It is suggested
  that authors write the complete file before splitting any lines to avoid later edits and changes
  requiring respacing throughout a paragraph or section.

### Generative AI use policy

Generative AI (including LLMs like ChatGPT, Claude, and Gemini) are non-expert tools which are
acceptable for informed, intentional use by human developers. Generative AI may be used for tasks
including: sanity checks, reviewing written data, suggesting general improvements, using a
human-devised format to fill in data files, writing code suggestions, and suggesting primary
sources. Generative AI is not appropriate for: creating or editing art or visual assets, creating
or editing audio assets including music and game sounds, working with real user data in any form,
or directly editing any code or data file without intensive review by a human developer. It is
suggested that agentic models never be given access to the entire project, nor should they be
granted the ability to edit more than one file at a time. As a guideline, it is best to fully
re-type AI generated code or data text into the edited file to ensure that every character is
reviewed. The primary source for data should never be the output of an LLM, and should always be
verified through obvious human intuition or by an expert source.

It is the expectation of players that this game is a curated, human-developed experience. As such,
the suggestions or outputs of generative AI models should be at least three "degrees of separation"
from player's game interactions, examples of which would be: a draft of a data file may be written
by an AI model, reviewed and retyped by a human developer, and then read and used in-game by the
game engine; an AI model's architecture suggestions for a game system is reviewed by a human, and
then code is written by a human developer to implement the human-reviewed architecture. Examples
of a violation of this rule would be: AI-generated art is used in the UI; in-game descriptions are
displayed from a datafile written entirely by an AI model; the architecture for a game system is
implemented exactly as specified by an AI model with no further thought by a human developer.

When using generative AI, it is the responsibility of the developer to limit harm to themselves,
to others, and to the environment. Researching the social and environmental impacts of different
AI models is encouraged. Developers should avoid using AI for certain tasks which have greater
impact, like image, audio, and (especially) video generation, or deep agentic research tasks.

In code and datafiles which were written or edited by generative AI, human reviewers MUST include
an AI use disclosure in code comments or a metadata file (under the ### Sources section or
similar). This should include an explanation of what the AI model was used for, and how the data
was verified by the human reviewer. Whenever possible, a link to the original conversation(s)
should be included to allow later review.

Usage of AI models for developer tasks does not shift the burden of care away from the developer
using the model. Any outcomes of AI-generated code or data, whether positive or negative, belong
to the developer who reviewed or implemented that code or data.

Violating this generative AI use policy is considered a serious quality and ethics violation, and
may result in loss of the privledges to contribute, review, or otherwise impact the game direction
in the future. In serious cases, violation of this policy may result in expulsion from the team and
reworking of all previous contributions by other members of the development team, and with omission
from receiving credit on those reworked systems.
