package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.government.Government

private val allStates = setOf(
    "AL", "AK", "AZ", "AR", "CA", "CO",
       "CT", "DE", "DC", "FL", "GA",
    "HI", "ID", "IL", "IN", "IA", "KS",
       "KY", "LA", "ME", "MD", "MA",
    "MI", "MN", "MS", "MO", "MT", "NE",
       "NV", "NH", "NJ", "NM", "NY",
    "NC", "ND", "OH", "OK", "OR", "PA",
       "RI", "SC", "SD", "TN", "TX",
    "UT", "VT", "VA", "WV", "WI", "WY",
)

data class SubdivisionType(
    val label: String,
    val nameTemplate: String,
    val statesUsed: Set<String> = emptySet(),
    val hasGovernment: Boolean = true,
)

object SubdivisionTypes {
    /**
     * Census Subareas are a type of MCD used in Alaska which act similarly to CCDs and have
     * no government, being used only for statistical purposes.
     */
    val CENSUS_SUBAREA = SubdivisionType("Census Subarea", "%s Census Subarea", setOf("AK"), false)
    /**
     * Civil Townships are a type of MCD used in 17 states. In Wisconsin, civil townships may be
     * called "Towns." Civil Townships are unincorporated but have a town hall and variably manage
     * roads and infrastructure, with specific roles depending on state and county.
     */
    val CIVIL_TOWNSHIP = SubdivisionType("Civil Township", "%s Township", setOf("ND", "SD", "NE", "KS", "MN", "IA", "MO", "AR", "WI", "IL", "MI", "IN", "OH", "PA", "NJ", "NC"))
    /**
     * Charter Townships are a type of MCD used in Michigan. They act as more powerful or
     * municipal townships, able to establish civil services and having home rule. These are
     * most often found surrounding larger cities as their legal purpose is to protect
     * historical townships while providing adequate civil services to their larger populations.
     */
    val CHARTER_TOWNSHIP = SubdivisionType("Charter Township", "%s Township",  setOf("MI"))
    /**
     * New England Towns (officially just "Towns") are a type of MCD used in the New England
     * states. They function similarly to cities and villages seen in most states and are
     * legally incorporated (though not reflected as such on the Census) and have full
     * home-rule governments. Towns and their cities supplant county governments in the New
     * England states.
     */
    val NEW_ENGLAND_TOWN = SubdivisionType("Town", "Town of %s", setOf("CT", "MA", "ME", "NH", "NY", "RI", "VT"))
    /**
     * Magisterial Districts are a type of MCD used in Virginia and West Virginia. These county
     * divisions are responsible for conducting elections and recording land ownership, but have
     * no government or other powers.
     */
    val MAGISTERIAL_DISTRICT = SubdivisionType("Magisterial District", "%s Magisterial District", setOf("VA", "WV"), false)
    /**
     * Election Districts are a type of MCD used in Maryland. They conduct elections and censuses
     * but have no government or other powers.
     */
    val ELECTION_DISTRICT = SubdivisionType("Election District", "%s Election District", setOf("MD"), false)
    /**
     * Supervisors' Districts are a type of MCD used in Mississippi. They elect county
     * supervisors but have no local government.
     */
    val SUPERVISORS_DISTRICT = SubdivisionType("Supervisions' District", "%s Supervisors' District", setOf("MS"), false)
    /**
     * Parish Governing Authority Districts are a type of MCD used in Louisiana. They elect
     * county officials but have no local government.
     */
    val PARISH_GOVERNING_AUTHORITY_DISTRICT = SubdivisionType("Parish Governing Authority District", "%s Parish Governing Authority District", setOf("LA"), false)
    /**
     * County Commissioner Districts are a type of MCD used in Tennessee. They elect
     * county commissioners but have no local government.
     */
    val COUNTY_COMMISSIONER_DISTRICT = SubdivisionType("County Commissioner District", "%s County Commissioner District", setOf("TN"), false)
    /**
     * Election Precincts are a type of MCD used in some counties in Nebraska and Illinois.
     * They conduct elections but have no local government.
     */
    val ELECTION_PRECINCT = SubdivisionType("Election Precinct", "%s Election Precinct", setOf("NE", "IL"), false)
    /**
     * Unorganized Territores (UTs) are a type of MCD used in 8 states. They generally have
     * lower populations than other MCDs in those states and do not have local governments, and
     * are also used for the locations of military bases.
     */
    val UNORGANIZED_TERRITORY = SubdivisionType("Unorganized Territory", "Unorganized Territory of %s", setOf("ND", "SD", "MN", "IN", "NY", "ME", "NC"), false)
    /**
     * American Indian Reservations are a type of MCD used in Maine and New York (in other states,
     * reservations are not considered separate county subdivisions). There is no specific MCD
     * government, but there is a tribal government which fulfills the purposes of government for
     * these areas.
     */
    val AMERICAN_INDIAN_RESERVATION = SubdivisionType("American Indian Reservation", "%s American Indian Reservation", setOf("ME", "NY"), false)
    /**
     * Gores are a type of MCD found in Vermont and Maine. These are areas which were affected
     * by a survey error between two or more towns and have no local government.
     */
    val GORE = SubdivisionType("Gore", "%s Gore", setOf("VT", "ME"), false)
    /**
     * Grants are a type of MCD found in New Hampshire and Vermont. These areas were granted to
     * specific owners by the government and have no local government.
     */
    val GRANT = SubdivisionType("Grant", "%s Grant", setOf("VT", "NH"), false)
    /**
     * Purchases are a type of MCD found in New Hampshire. These are areas which were purchased
     * in full by a specific owner and have no local government.
     */
    val PURCHASE = SubdivisionType("Purchase", "%s Purchase", setOf("NH"), false)
    /**
     * Survey Townships are a type of MCD found in Coös County, New Hampshire. They are used
     * only for census purposes and have no local government.
     */
    val SURVEY_TOWNSHIP = SubdivisionType("Survey Township", "%s Township", setOf("NH"), false)
    /**
     * Locations are a type of MCD found in New Hampshire. These areas have no unique history
     * other than being unassigned to any other town or county subdivision. They are used only
     * for census purposes and have no local government.
     */
    val LOCATION = SubdivisionType("Location", "%s Location", setOf("NH"), false)
    /**
     * Plantations are a type of MCD used in Maine. They have an intermediate population
     * between Unorganized Territories and Towns and have their own limited government.
     */
    val PLANTATION = SubdivisionType("Plantation", "%s Plantation", setOf("ME"), false)
    /**
     * Unorganized Waters are areas of counties in open waters in the Great Lakes or Atlantic
     * Ocean. They have no population or government.
     */
    val UNORGANIZED_WATER = SubdivisionType("Unorganized Water", "%s Water", setOf("WI", "MI", "IL", "IN", "OH", "PA", "NY", "NH", "MA", "RI", "CN", "NJ"), false)
    /**
     * Pseudo-MCDs are a type of MCD defined by the census for counties where there are no
     * county subdivisions, specifically Cleveland County, NC and Arlington County, VA. These
     * counties abolished their subdivisions and are thus listed by the Census Bureau as a
     * non-governmental subdivision used only for census purposes.
     */
    val PSEUDO_MCD = SubdivisionType("Pseudo-MCD", "%s", setOf("NC", "VA"), false)
    /**
     * Barrios are a type of MCD used in Puerto Rico. Barrios have no local government
     * and are administered by their Municipio governments.
     */
    val BARRIO = SubdivisionType("Barrio", "Barrio %s", setOf("PR"), false)

    fun fromString(str: String) = mapOf(
        "census subarea" to CENSUS_SUBAREA,
        "civil township" to CIVIL_TOWNSHIP,
        "charter township" to CHARTER_TOWNSHIP,
        "new england town" to NEW_ENGLAND_TOWN,
        "magisterial district" to MAGISTERIAL_DISTRICT,
        "election district" to ELECTION_DISTRICT,
        "supervisors district" to SUPERVISORS_DISTRICT,
        "parish governing authority district" to PARISH_GOVERNING_AUTHORITY_DISTRICT,
        "county commissioner district" to COUNTY_COMMISSIONER_DISTRICT,
        "election precinct" to ELECTION_PRECINCT,
        "unorganized territory" to UNORGANIZED_TERRITORY,
        "american indian reservation" to AMERICAN_INDIAN_RESERVATION,
        "gore" to GORE,
        "grant" to GRANT,
        "purchase" to PURCHASE,
        "survey township" to SURVEY_TOWNSHIP,
        "location" to LOCATION,
        "plantation" to PLANTATION,
        "unorganized water" to UNORGANIZED_WATER,
        "pseudo mcd" to PSEUDO_MCD,
        "barrio" to BARRIO,
    )[str.lowercase().replace(Regex("[^a-z_]"), "").replace("_", " ")]
}

data class PlaceType(
    val label: String,
    val nameTemplate: (String) -> String, // Takes abbreviation of state and returns a template including `%s`
    val statesUsed: Set<String> = allStates,
    val alwaysIncorporated: Boolean = true,
    val allowsDependent: Boolean = true,
    val allowsIndependent: Boolean = true,
)

object PlaceTypes {
    /**
     * Cities are defined differently in different states, but always represent populace places
     * with governments. In MCD states, cities are mostly independent, while in CCD states,
     * cities are always dependent. 49 states and DC have cities (Hawaii is the only state without).
     * The District of Columbia acts as a statistical state-equivalent, a single
     * county-equivalent, and a county subdivision, simoultaneously. The City of Washington is
     * not a consolidated city-county but acts as all three levels at once, coterminous with
     * the District. The city government has home rule, but their laws can be overturned and
     * the local government abolished at any time.
     */
    val CITY = PlaceType("City", { "City of %s" }, allStates - "HI")
    /**
     * Consolidated City-Counties (or "City-counties") are places where city and county
     * governments are merged into one unified juristictive body. In these cases, city councils
     * also act as county boards, and mayors act as county chiefs. The city and county still
     * exist, but are merged into one unit. In CCD states, the city-counites are still
     * dependent on their underlying county subdivisions, despite taking up the entire county.
     * In Alaska, these are called Consolidated City-Boroughs. In Georgia, these are called
     * Unified Governments.
     */
    val CONSOLIDATED_CITY_COUNTY = PlaceType("Consolidated City-County", { when (it) { "GA" -> "%s Unified Government" "AK" -> "City and Borough of %s" else -> "City and County of %s" } }, setOf("KY", "GA", "MT", "CA", "AK"))
    /**
     * County Balances are places similar to Consolidated City-Counties and have unified
     * governments between their city and county. Unlike City-counties, however, County
     * Balances do not fully encompass their county and some cities and towns also exist
     * separately within the county with some municipal powers.
     */
    val COUNTY_BALANCE = PlaceType("", { "%s County" }, setOf("MT", "KS", "IN", "KY", "TN", "GA", "CT"), alwaysIncorporated = false)
    /**
     * Towns are a type of place found in 31 states, and vary in definition between states.
     * Where they are present, they are always incorporated places with a lesser population
     * requirement than cities. Towns have their own municipal governments and vary in their
     * dependency status.
     */
    val TOWN = PlaceType("Town", { "Town of %s" }, setOf("WA", "OR", "CA", "AZ", "UT", "CO", "WY", "NM", "TX", "MT", "OK", "LA", "AR", "SD", "MO", "IL", "IN", "TN", "MS", "AL", "GA", "FL", "SC", "NC", "VA", "WV", "MD", "DE", "PA", "NJ"), alwaysIncorporated = false)
    /**
     * Villages are a type of place found in 16 states, and very in definition between states.
     * Where they are present, they are always incorporated places with a lesser population
     * requirement than cities or towns. Villages have their own municipal governments and very
     * in their dependency status.
     */
    val VILLAGE = PlaceType("Village", { "Village of %s" }, setOf("WI", "MI", "OH", "IL", "MO", "NE", "NM", "TX", "LA", "MS", "NC", "MD", "DE", "NJ", "NY", "VT"), alwaysIncorporated = false)
    /**
     * Boroughs (not to be confused with Alaska's county equivalent also called "Boroughs") are
     * a type of place found in 4 states. Boroughs vary in definition between states. They have
     * their own municipal government and have a lower population requirement than cities.
     * In New York City, the five boroughs are each coextensive with their five counties. The
     * single City of New York is a dependent city on the five boroughs / counties, while the
     * boroughs are non-governmental MCDs dependent on their counties (the counties have no
     * governments and are powerless).
     */
    val BOROUGH = PlaceType("Borough", { "Borough of %s" }, setOf("NY", "NJ", "CT", "PA"))
    /**
     * Home-Rule Municipalities are a type of place found in Pennsylvania which are always
     * independent, acting as their own county subdivision. These have a unique charter
     * granting home rule and additional powers. There are 6 of these in Pennsylvania found
     * surrounding Pittsburgh, PA.
     */
    val HOME_RULE_MUNICIPALITY = PlaceType("Home-Rule Municipality", { "Borough of %s" }, setOf("PA"), allowsDependent = false)
    /**
     * There is one Corporation in the United States: Ranson, WV. This place was founded by a
     * corporation and acts identically to any other city in West Virginia.
     */
    val CORPORATION = PlaceType("Corporation", { "Corporation of %s" }, setOf("WV"))
    /**
     * Census-Designated Places (CDPs) are found in every state and do not act as a county
     * subdivision, used only as a dependent place for the Census. They are the only places
     * occupying unincorporated areas and are usually loosely defined with a significant enoguh
     * population and locally-knwon name to warrant their reflection on the Census. They have no
     * local government or powers and are used only for statistical purposes.
     * In Hawaii, all places are CDPs, since the state government of Hawaii determined that the
     * county governments were sufficient for the entire population.
     */
    val CENSUS_DESIGNATED_PLACE = PlaceType("Census-Designated Place", { "%s CDP" }, alwaysIncorporated = false, allowsIndependent = false)
}

/**
 * County Subdivisions are the thrid-degree subdivisions of the United States. All
 * county-equivalent areas are divided this way, and the census uses these county subdivisions for
 * collection and statistical purposes. The powers of county subdivisions vary by state, with some
 * having full home-rule governments and others being little more than sections of a county.
 */
sealed class CountySubdivision(
    engine: Engine,
    name: String,
    override val countyEquivalent: CountyEquivalent,
) : MapEntity(engine, name), PlaceContainer {

    override val censusDivision = countyEquivalent.censusDivision
    override val censusRegion = countyEquivalent.censusRegion

    val stateEquivalent = countyEquivalent.stateEquivalent

    val places: Set<Place> = mutableSetOf()

    /**
     * Minor Civil Divisions (MCDs) are the only county divisions used in the states in which
     * they are located (a state may not have both CCDs and MCDs). These areas generally have
     * sufficient local populations to form a government.
     */
    class MinorCivilDivision(
        engine: Engine,
        name: String = "",
        countyEquivalent: CountyEquivalent,
        val type: SubdivisionType = SubdivisionTypes.CIVIL_TOWNSHIP,
        val government: Government? = null,
    ) : CountySubdivision(engine, name, countyEquivalent) {
        init {
            require((government != null) == type.hasGovernment) {
                "${type.label} government-presence does not match type defintion for $name, ${countyEquivalent.name}"
            }
        }
    }

    /**
     * Census County Divisions (CCDs) are the only county divisions used in the states in which
     * they are located (a state may not have both CCDs and MCDs). These are found mostly in states
     * with small populations in rural areas, meaning the census divisions have no government and
     * are administered by counties.
     */
    class CensusCountyDivision(
        engine: Engine,
        name: String,
        countyEquivalent: CountyEquivalent,
    ) : CountySubdivision(engine, name, countyEquivalent)
}
