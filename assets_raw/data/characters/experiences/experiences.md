# Experiences

### Attribution

**Author**: Steven LaGoy

**Created**: 06 June 2026

**Modified**: 04 September 2026

### Description

`experiences.json` contains data about experiences which characters may have in their personal
history. This includes education, careers, government positions, military service, etc. Each entry
should describe a specific experience or a small category of similar experiences, and should
include data which is used to determine when a character might have the experience and for how
long, as well as the impacts this has on the character.

### Format

"id" : {<br>
&emsp;"name" : "Plain-English name of the experience",<br>
&emsp;"track" : "[academic | civic | labor | military | legal | legislative | executive | judicial | business | media ]",<br>
&emsp;"capacity" : [0.0, 1.0], (=1.0)<br>
&emsp;"tenure_years" : {"<br>
&emsp;&emsp;"min" : [0, ^max], (=0)<br>
&emsp;&emsp;"avg" : [^min, ^max], (=[min+max]/2)<br>
&emsp;&emsp;"max" : [^min, 120] (=120)<br>
&emsp;},<br>
&emsp;"repeatable" : true/false, (=true)<br>
&emsp;"prerequisites" : [<br>
&emsp;&emsp;^id,<br>
&emsp;&emsp;...<br>
&emsp;], (=[])<br>
&emsp;"prerequisite_logic" : "[any | all | 2+ | 3+]", (=any)<br>
&emsp;"base_chance" : [0, &infin;], (=1)<br>
&emsp;"min_age" : [18, ^max_age], (=18)<br>
&emsp;"max_age" : [^min_age, 120], (=120)<br>
&emsp;"yearly_skills" : {<br>
&emsp;&emsp;"legislative" : [0.0,&infin;],<br>
&emsp;&emsp;"executive" : [0.0,&infin;],<br>
&emsp;&emsp;"judicial" : [0.0,&infin;],<br>
&emsp;},<br>
&emsp;"description" : "Description of the experience.",<br>
&emsp;"connections" : {<br>
&emsp;&emsp;"^id" : [0.0, &infin;],<br>
&emsp;&emsp;...<br>
&emsp;}, (={})<br>
&emsp;"overlaps" : [<br>
&emsp;&emsp;"^id",<br>
&emsp;&emsp;...<br>
&emsp;] (=[])<br>
}

- track: One category which describes which "track" an experience is on. Characters generally
    progress down one or a few tracks during different stages of their life.
- capacity: A value between 0 and 1 specifying how much of a character's capacity to have
    experiences is occupied by this experience while active. At any time, all of a character's
    experiences will have capacities summing to <=1.
- repeatable: A boolean representing whether a character which has already completed this
    experience may begin it again.
- prerequisites: An array of experience IDs which determines which prior experiences are necessary
    for this experience to be valid. (I.E. being a Physician requires completing Medial School)
- prerequisite_logic: A string for the logic applied to prerequisites. Any = one or more of the
    prerequisites would be required for this experience to be valid. All = all the prerequisites
    would be required for this experience to be valid.
- base_chance: The relative liklihood that an experience is selected among all valid experiences
    for a character's history. By convention, this should be relative to a normal liklihood of 1.
    0 means that the experience will never be selected, even when valid, and 2 means that an
    experience is twice as likely to be selected than an experience with base_chance=1, assuming
    both are valid.
- min_age: Minimum age in years required to begin an experience.
- max_age: Maximum age in years (inclusive) allowed for characters having this experience. A
    character will not have this experience at any time when their age exceeds this value.
- yearly_skills: An object with three values specifying yearly change in skills while this
    experience is active.
- connections: An object with ID keys and values specifying modifiers to base_chances. A value of 1
    indicates no modifier, a value <1 indicates that experience are less likely following this
    experience, and a value >1 indicates that experience is more likely following this experience.
    These are independently applied to the base_chance of each connected experience, and one of
    those is selected (if valid) based on the modified chances.
- overlaps: An array of IDs specifying which experiences may overlap with this experience, after
    considering capacity.

### Data Sources

AI use disclosure: This file was made with the input of generative AI. AI-generated data is always
reviewed by a real human for quality and correctness. See the conversation here:
https://claude.ai/share/ff544a7b-a7a1-4499-a0a0-1013991eb59a
