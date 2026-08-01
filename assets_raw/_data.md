# Data

`~/data/`

### Attribution

**Author**: Steven LaGoy

**Created**: 01 August 2026

**Modified**: 01 August 2026

### Description

The `data/` directory contains most information used to create the map, characters, politics, and
other systems in *Mandate*. 

### Meta

- All information for one position, location, or body should be located within the same file. Do
not use state keys in data files, instead use the data file for that state.
- Keys should mostly be in camelCase. snake_case is acceptable when the only purpose of the key is
for uniqueness and it will never be parsed by key.
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

---
