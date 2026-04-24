package com.stevenlagoy.presidency.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public final class FilePaths {

    public static final Path CORE                       = FileSystems.getDefault().getPath(".");
    public static final Path SRC                        = CORE.resolve("src");
    public static final Path MAIN                       = SRC.resolve("main");
    public static final Path PROJECT                    = MAIN.resolve("java").resolve("com").resolve("stevenlagoy").resolve("presidency");
    public static final Path RESOURCES                  = MAIN.resolve("resources");

    public static final Path LOGS_DIR                   = CORE.resolve("logs");
    public static final Path LOG_FILE                   = LOGS_DIR.resolve("log.txt");
    public static final Path ERROR_FILE                 = LOGS_DIR.resolve("error.txt");
    public static final Path OUTPUT_FILE                = LOGS_DIR.resolve("output.txt");
    public static final Path SAVES_DIR                  = CORE.resolve("saves");

    public static final Path CHARACTERS_RESOURCES       = RESOURCES.resolve("characters");

    public static final Path CONVENTIONS_RESOURCES      = RESOURCES.resolve("conventions");

    public static final Path DATES_RESOURCES            = RESOURCES.resolve("dates");
    public static final Path BIRTHDATE_POPULARITIES     = DATES_RESOURCES.resolve("birthdate_popularities.json");
    public static final Path BIRTHYEAR_PERCENTAGES      = DATES_RESOURCES.resolve("birthyear_percentages.json");
    public static final Path HOLIDAYS                   = DATES_RESOURCES.resolve("holidays.json");

    public static final Path DEMOGRAPHICS_RESOURCES     = RESOURCES.resolve("demographics");
    public static final Path BLOCS                      = DEMOGRAPHICS_RESOURCES.resolve("blocs.json");

    public static final Path LOCALIZATION_RESOURCES     = RESOURCES.resolve("localization");
    public static final String SYSTEM_TEXT_LOC          = "_system_text.json";
    public static final String DESCRIPTIONS_LOC         = "_descriptions.json";

    public static final Path MAP_RESOURCES              = RESOURCES.resolve("map");
    public static final Path NATION                     = MAP_RESOURCES.resolve("nation.json");
    public static final Path STATES_DIR                 = MAP_RESOURCES.resolve("states");

    public static final Path AIRCRAFT_TYPES             = MAP_RESOURCES.resolve("aircraft_types.json");
    public static final Path AIRPORTS                   = MAP_RESOURCES.resolve("airports.json");
    public static final Path DESCRIPTORS                = MAP_RESOURCES.resolve("descriptors.json");
    public static final Path RAILWAYS                   = MAP_RESOURCES.resolve("railways.json");
    public static final Path ROADWAY_TYPES              = MAP_RESOURCES.resolve("roadway_types.json");
    public static final Path ROADWAYS                   = MAP_RESOURCES.resolve("roadways");
    public static final Path SEAPORTS                   = MAP_RESOURCES.resolve("seaports");

    public static final Path NAMES_RESOURCES            = RESOURCES.resolve("names");
    public static final Path GIVEN_NAMES                = NAMES_RESOURCES.resolve("given_names.json");
    public static final Path FAMILY_NAMES               = NAMES_RESOURCES.resolve("family_names.json");
    public static final Path GENERATION_NAMES           = NAMES_RESOURCES.resolve("generation_names.json");
    public static final Path NICKNAMES                  = NAMES_RESOURCES.resolve("nicknames.json");

    public static final Path POLITICS_RESOURCES         = RESOURCES.resolve("politics");

    public static final Path SYSTEM_RESOURCES           = RESOURCES.resolve("system");





    public static final List<Path> IGNORED_PATHS = List.of(
        RESOURCES.resolve("_resources"),
        RESOURCES.resolve("_tools")
    );

}
