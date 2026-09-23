package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.map.entities.County
import com.stevenlagoy.presidency.map.entities.PlaceTypes
import com.stevenlagoy.presidency.map.entities.SubdivisionType
import com.stevenlagoy.presidency.map.entities.SubdivisionTypes

/**
 * Legal / Statistical Area Description (LSAD) codes describe the particular typology for each
 * geographic entity. For legal entities, the LSAD reflects the term that appears in legal
 * documentation pertaining to the entity, such as a treaty, charter, legislation, resolution, or
 * ordinance. For statistical entities, the LSAD is the term assigned by the Census Bureau or other
 * agency defining the entity. The LSAD code is a two-character field that corresponds to a
 * description of the legal or statistical type of entity and identifies whether the LSAD term
 * should be capitalized and should precede or follow the name of the geographic entity. Note that
 * the same LSAD code is assigned to entities at different levels of the geographic hierarchy when
 * they share the same LSAD. For example, the Census Bureau assigns the same LSAD code ("21") to
 * boroughs in New York and Connecticut, although they are county subdivisions in the former and
 * incorporated places in the latter.
 */
enum class LegalStatisticalAreaDescriptionType(val LSAD: String, val description: String, val associatedGeorgraphicEntities: List<String>) {
    UNKNOWN_00                              ("00", "", listOf("")),
    CITY_AND_BOROUGH_03                     ("03", "City and Borough (suffix)", listOf("")),
    BOROUGH_04                              ("04", "Borough (suffix)", listOf("")),
    CENSUS_AREA_05                          ("05", "Census Area (suffix)", listOf("")),
    BALANCE_OF_COUNTY_06                    ("06", "Balance of County EC Place", listOf("")),
    COUNTY_06                               ("06", "County (suffix)", listOf("")),
    DISTRICT_07                             ("07", "Distrit (suffix)", listOf("")),
    ISLAND_10                               ("10", "Island (suffix)", listOf("")),
    MUNICIPALITY_12                         ("12", "Municipality (suffix)", listOf("")),
    MUNICIPIO_13                            ("13", "Municipio (suffix)", listOf("")),
    PARISH_15                               ("15", "Parish (suffix)", listOf("")),
    BARRIO_20                               ("20", "barrio (suffix)", listOf("")),
    BOROUGH_21                              ("21", "borough (suffix)", listOf("")),
    CENSUS_COUNTY_DIVISION_22               ("22", "CCD (suffix)", listOf("")),
    CENSUS_SUBAREA_23                       ("23", "census subarea (suffix)", listOf("")),
    CENSUS_SUBDISTRICT_24                   ("24", "census subdistrict (suffix)", listOf("")),
    CITY_25                                 ("25", "city (suffix)", listOf("")),
    COUNTY_26                               ("26", "county (suffix)", listOf("")),
    DISTRICT_27                             ("27", "district (suffix)", listOf("")),
    DISTRICT_28                             ("28", "District (prefix)", listOf("")),
    PRECINCT_29                             ("29", "precinct (suffix)", listOf("")),
    PRECINCT_30                             ("30", "Precinct (prefix)", listOf("")),
    GORE_31                                 ("31", "gore (suffix)", listOf("")),
    GRANT_32                                ("32", "location (suffix)", listOf("")),
    LOCATION_36                             ("36", "location (suffix)", listOf("")),
    MUNICIPALITY_37                         ("37", "municipality (suffix)", listOf("")),
    PLANTATION_39                           ("39", "plantation (suffix)", listOf("")),
    BARRIO_PUEBLO_41                        ("41", "barrio-pueblo (suffix)", listOf("")),
    PURCHASE_42                             ("42", "purchase (suffix)", listOf("")),
    TOWN_43                                 ("43", "town (suffix)", listOf("")),
    TOWNSHIP_44                             ("44", "township (suffix)", listOf("")),
    TOWNSHIP_45                             ("45", "Township (prefix)", listOf("")),
    UNORGANIZED_TERRITORY_46                ("46", "UT (suffix)", listOf("")),
    VILLAGE_47                              ("47", "village (suffix)", listOf("")),
    CHARTER_TOWNSHIP_49                     ("49", "charter township (suffix)", listOf("")),
    SUBBARRIO_51                            ("51", "subbario (suffix)", listOf("")),
    CITY_AND_BOROUGH_53                     ("53", "", listOf("")),
    COMUNIDAD_55                            ("55", "", listOf("")),
    CENSUS_DESIGNATED_PLACE_57              ("57", "", listOf("")),
    ZONA_URBANA_62                          ("62", "", listOf("")),
    REGION_68                               ("68", "", listOf("")),
    DIVISION_69                             ("69", "", listOf("")),
    URBAN_GROWTH_AREA_70                    ("70", "", listOf("")),
    CMSA_71                                 ("71", "", listOf("")),
    MSA_72                                  ("72", "", listOf("")),
    PRIMARY_METROPOLITAN_STATISTICAL_AREA_73("73", "", listOf("")),
    NEW_ENGLAND_COUNTY_METROPOLITAN_AREA_74 ("74", "", listOf("")),
    URBANIZED_AREA_75                       ("75", "", listOf("")),
    URBAN_CLUSTER_76                        ("76", "", listOf("")),
    ALASKA_NATIVE_REGIONAL_CORPORATION_77   ("77", "", listOf("")),
    HAWAIIAN_HOME_LAND_78                   ("78", "", listOf("")),
    ANVSA_79                                ("79", "", listOf("")),
    TDSA_80                                 ("80", "", listOf("")),
    COLONY_81                               ("81", "", listOf("")),
    COMMUNITY_82                            ("82", "", listOf("")),
    JOINT_USE_AREA_83                       ("83", "", listOf("")),
    PUEBLO_84                               ("84", "", listOf("")),
    RANCHERIA_85                            ("85", "", listOf("")),
    RESERVATION_86                          ("86", "", listOf("")),
    RESERVE_87                              ("87", "", listOf("")),
    OTSA_88                                 ("88", "", listOf("")),
    TRUST_LAND_89                           ("89", "", listOf("")),
    JOINT_USE_OTSA_90                       ("90", "", listOf("")),
    RANCH_91                                ("91", "", listOf("")),
    SDTSA_92                                ("92", "", listOf("")),
    INDIAN_VILLAGE_93                       ("93", "", listOf("")),
    VILLAGE_94                              ("94", "", listOf("")),
    INDIAN_COMMUNITY_95                     ("95", "", listOf("")),
    INDIAN_RESERVATION_96                   ("96", "", listOf("")),
    INDIAN_RANCHERIA_97                     ("97", "", listOf("")),
    INDIAN_COLONY_98                        ("98", "", listOf("")),
    PUEBLO_DE_99                            ("99", "", listOf("")),
    PUEBLO_OF_9C                            ("9C", "", listOf("")),
    RANCH_RESERVATION_9D                    ("9D", "", listOf("")),
    RANCHERIA_RESERVATION_9E                ("9E", "", listOf("")),
    RANCHES_9F                              ("9F", "", listOf("")),
    BALANCE_OF_COUNTY_B1                    ("B1", "", listOf("")),
    BALANCE_OF_PARISH_B2                    ("B2", "", listOf("")),
    BALANCE_OF_BOROUGH_B3                   ("B3", "", listOf("")),
    BALANCE_OF_CENSUS_AREA_B4               ("B4", "", listOf("")),
    TOWN_BALANCE_B5                         ("B5", "", listOf("")),
    TOWNSHIP_BALANCE_B6                     ("B6", "", listOf("")),
    CHARTER_TOWNSHIP_BALANCE_B7             ("B7", "", listOf("")),
    BALANCE_OF_B8                           ("B8", "", listOf("")),
    BLOCK_GROUP_BG                          ("BG", "", listOf("")),
    BALANCE_OF_ISLAND_BI                    ("BI", "", listOf("")),
    BLOCK_BK                                ("BK", "", listOf("")),
    BALANCE_BL                              ("BL", "", listOf("")),
    CONGRESSIONAL_DISTRICT_AT_LARGE_C1      ("C1", "", listOf("")),
    CONGRESSIONAL_DISTRICT_C2               ("C2", "", listOf("")),
    RESIDENT_COMMISSIONER_DISTRICT_C3       ("C3", "", listOf("")),
    DELEGATE_DISTRICT_C4                    ("C4", "", listOf("")),
    NO_REPRESENTATIVE_C5                    ("C5", "", listOf("")),
    CONSOLIDATED_GOVERNMENT_BALANCE_CB      ("CB", "", listOf("")),
    CONSOLIDATED_GOVERNMENT_CG              ("CG", "", listOf("")),
    CORPORTATION_CN                         ("CN", "", listOf("")),
    COMMERCIAL_REGION_CR                    ("CR", "", listOf("")),
    CENSUS_TRACT_CT                         ("CT", "", listOf("")),
    TRIBAL_BLOCK_GROUP_IB                   ("IB", "", listOf("")),
    TRIBAL_CENSUS_TRACT_IT                  ("IT", "", listOf("")),
    WARD_L1                                 ("L1", "", listOf("")),
    SENATORIAL_DISTRICT_L2                  ("L2", "", listOf("")),
    ASSEMBLY_DISTRICT_L3                    ("L3", "", listOf("")),
    GENERAL_ASSEMBLY_DISTRICT_L4            ("L4", "", listOf("")),
    STATE_LEGISLATIVE_DISTRICT_L5           ("L5", "", listOf("")),
    STATE_LEGISLATIVE_SUBDISTRICT_L6        ("L6", "", listOf("")),
    DISTRICT_L7                             ("L7", "", listOf("")),
    STATE_HOUSE_DISTRICT_LL                 ("LL", "", listOf("")),
    STATE_SENATE_DISTRICT_LU                ("LU", "", listOf("")),
    COMBINED_STATISTICAL_AREA_M0            ("M0", "", listOf("")),
    METRO_AREA_M1                           ("M1", "", listOf("")),
    MICRO_AREA_M2                           ("M2", "", listOf("")),
    METRO_DIVISION_M3                       ("M3", "", listOf("")),
    COMBINED_NECTA_M4                       ("M4", "", listOf("")),
    METROPOLITAN_NECTA_M5                   ("M5", "", listOf("")),
    MICROPOLITAN_NECTA_M6                   ("M6", "", listOf("")),
    NECTA_DIVISION_M7                       ("M7", "", listOf("")),
    METROPOLITAN_GOVERNMENT_BALANCE_MB      ("MB", "", listOf("")),
    METROPOLITAN_GOVERNMENT_MG              ("MG", "", listOf("")),
    METRO_GOVERNMENT_MT                     ("MT", "", listOf("")),
    SUPER_PUMA_P1                           ("P1", "", listOf("")),
    PUMA_P5                                 ("P5", "", listOf("")),
    AREA_T1                                 ("T1", "", listOf("")),
    CHAPTER_T2                              ("T2", "", listOf("")),
    SEGMENT_T3                              ("T3", "", listOf("")),
    ADMINISTRATIVE_AREA_TA                  ("TA", "", listOf("")),
    ADDITION_TB                             ("TB", "", listOf("")),
    COUNTY_DISTRICT_TC                      ("TC", "", listOf("")),
    TRAFFIC_ANALYSIS_ZONE_TZ                ("TZ", "", listOf("")),
    UNIFIED_GOVERNMENT_BALANCE_UB           ("UB", "", listOf("")),
    URBAN_COUNTY_UC                         ("UC", "", listOf("")),
    UNIFIED_GOVERNMENT_UG                   ("UG", "", listOf("")),
    VOTING_DISTRICT_V1                      ("V1", "", listOf("")),
    VOTING_DISTRICT_V2                      ("V2", "", listOf("")),
    ZCTA3_Z3                                ("Z3", "", listOf("")),
    ZCTA5_Z5                                ("Z5", "", listOf(""));

    companion object {
        fun fromString(str: String): LegalStatisticalAreaDescriptionType {
            if (str.matches(Regex(".+ ([0-9A-Z]{2})"))) {
                val LSAD = str.split("(").last().split(")").first().uppercase()
                return LegalStatisticalAreaDescriptionType.entries.find { it.LSAD == LSAD } ?: LegalStatisticalAreaDescriptionType.UNKNOWN_00
            }
            return LegalStatisticalAreaDescriptionType.entries.find { it.description == str } ?: LegalStatisticalAreaDescriptionType.UNKNOWN_00
        }
    }
}
