package com.stevenlagoy.presidency.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static com.stevenlagoy.presidency.util.IOUtils.FileExtension.*;

public enum FilePath {
    _ROOT                       ("."),
        _ASSETS                     (_ROOT, "assets"),
            _DATA                       (_ASSETS, "data"),
                _CHARACTERS                 (_DATA, "characters"),
                    _EXPERIENCES                (_CHARACTERS, "experiences"),
                        EXPERINECES                 (_EXPERIENCES, "experiences", JSON),
                    _NAMES                      (_CHARACTERS, "names"),
                        DECADE_NAMES                (_NAMES, "decade_names", JSON),
                        FAMILY_NAMES                (_NAMES, "family_names", JSON),
                        GIVEN_NAMES                 (_NAMES, "given_names", JSON),
                        NICKNAMES                   (_NAMES, "nicknames", JSON),
                    HISTORICAL_CHARACTERS       (_CHARACTERS, "historical_characters", JSON),
                    PAST_PRESIDENTS             (_CHARACTERS, "past_presidents", JSON),
                    TRAITS                      (_CHARACTERS, "traits", JSON),
                _DATES                      (_DATA, "dates"),
                    BIRTHDATE_POPULARITIES      (_DATES, "birthdate_popularities", JSON),
                    BIRTHYEAR_PERCENTAGES       (_DATES, "birthyear_percentages", JSON),
                    HOLIDAYS                    (_DATES, "holidays", JSON),
                _DEMOGRAPHICS               (_DATA, "demographics"),
                    BLOCS                       (_DEMOGRAPHICS, "blocs", JSON),
                    POPULATION_PYRAMID          (_DEMOGRAPHICS, "pop_pyramid_everyone", JSON),
                    STATE_DEMOGRAPHICS          (_DEMOGRAPHICS, "state_demographics", JSON),
                _GFX                        (_DATA, "gfx"),
                    _FLAGS                      (_GFX, "flags"),
                    _SHADERS                    (_GFX, "shaders"),
                _LOCALIZATION               (_DATA, "localization"),
                    _EN_LOCALIZATION            (_LOCALIZATION, "EN"),
                        EN_DESCRIPTIONS             (_EN_LOCALIZATION, "EN_descriptions", JSON),
                        EN_SYSTEM_TEXT              (_EN_LOCALIZATION, "EN_system_text", JSON),
                    LANGUAGES                   (_LOCALIZATION, "languages", JSON),
                _MAP                        (_DATA, "map"),
                    _STATES                     (_MAP, "states"),
                    _TRAVEL                     (_MAP, "travel"),
                        AIRCRAFT_TYPES              (_TRAVEL, "aircraft_types", JSON),
                        AIRPORTS                    (_TRAVEL, "airports", JSON),
                        RAILWAYS                    (_TRAVEL, "railways", JSON),
                        ROADWAY_DESIGNATIONS        (_TRAVEL, "roadway_designations", JSON),
                        ROADWAYS                    (_TRAVEL, "roadways", JSON),
                        SEAPORTS                    (_TRAVEL, "seaports", JSON),
                    CENSUS_REGIONS_DIVISIONS    (_MAP, "census_regions_divisions", JSON),
                    DESCRIPTORS                 (_MAP, "descriptors", JSON),
                    NATION                      (_MAP, "nation", JSON),
                    TIME_ZONES                  (_MAP, "timezones", JSON),
                _POLITICS                   (_DATA, "politics"),
                    _CANDIDATES                 (_POLITICS, "candidates"),
                    _CONVENTIONS                (_POLITICS, "conventions"),
                    _ELECTIONS                  (_POLITICS, "elections"),
                    _PARTIES                    (_POLITICS, "parties"),
                        _PARTY_SYMBOLS              (_PARTIES, "symbols"),
                        PARTIES                     (_PARTIES, "parties", JSON),
                    IDEOLOGIES                  (_POLITICS, "ideologies", JSON),
                    MAIL_IN_RULES               (_POLITICS, "mail_in_rules", JSON),
                _SYSTEM                     (_DATA, "system"),
                    SETTINGS                    (_SYSTEM, "settings", JSON),
            _MAPS                       (_ASSETS, "maps"),
                CLIMATE_MAP                 (_MAPS, "climate", BINARY),
                CONGRESSIONAL_DISTRICTS_MAP (_MAPS, "congressional_districts", BINARY),
                COUNTY_SUBDIVISIONS_MAP     (_MAPS, "county_subdivisions", BINARY),
                HISTORICAL_PROVINCES_MAP    (_MAPS, "historical_provinces", BINARY),
                HYDROLOGY_MAP               (_MAPS, "hydrology", BINARY),
                PHYSIOGRAPHY_MAP            (_MAPS, "physiography", BINARY),
                PLACES_MAP                  (_MAPS, "places", BINARY),
                TIMEZONES_MAP               (_MAPS, "timezones", BINARY),
                TOPOGRAPHY_BATHYMETRY_MAP   (_MAPS, "topography_bathymetry", BINARY),
            _MODELS                     (_ASSETS, "models"),
            _TEXTURES                   (_ASSETS, "textures"),
                _BACKGROUNDS                (_TEXTURES, "backgrounds"),
                _LOGOS                      (_TEXTURES, "logos"),
            _UI                         (_ASSETS, "ui"),
                _BUTTONS                    (_UI, "buttons"),
                _FONTS                      (_UI, "fonts"),
                _ICONS                      (_UI, "icons"),
                _PANELS                     (_UI, "panels"),
        _LOGS                       (_ROOT, "logs"),
            ERROR_LOG                   (_LOGS, "error", TEXT),
            LOG                         (_LOGS, "log", TEXT),
            OUTPUT                      (_LOGS, "output", TEXT),
        _SAVES                      (_ROOT, "saves"),
    ;

    public final @NotNull Path path;
    FilePath(@NotNull String resolution) {
        path = FileSystems.getDefault().getPath(resolution);
    }
    FilePath(@NotNull FilePath dir, @NotNull String resolution) {
        path = dir.path.resolve(resolution);
    }
    FilePath(@NotNull FilePath dir, @NotNull String resolution, @NotNull IOUtils.FileExtension extension) {
        path = dir.path.resolve(resolution + extension.extension);
    }
    public @NotNull Path resolve(@NotNull String resolution) throws InvalidPathException {
        return path.resolve(resolution);
    }
    public @NotNull File toFile() {
        return path.toFile();
    }
    @Override
    public @NotNull String toString() {
        return path.toString();
    }
}
