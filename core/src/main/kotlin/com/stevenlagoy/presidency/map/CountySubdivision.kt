package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.politics.Government

/**
 * County Subdivisions are subdivisions of the 2nd-degree subdivisions (county-equivalents) of the
 * United States. All counties are divided this way, and the census uses these county subdivisions
 * for collection and statistical purposes.
 */
sealed class CountySubdivision(
    var name: String,
    var county: County?,
    var population: Int,
    var government: Government?
) {

    /**
     * Minor Civil Divisions (MCDs) are the only county divisions used in the states in which
     * they are located (a state may not have both CCDs and MCDs). These areas generally have
     * sufficient local populations to form a government.
     */
    sealed class MinorCivilDivision(
        name: String,
        county: County,
        population: Int,
        government: Government?
    ) : CountySubdivision(name, county, population, government) {

        /**
         * Census Subareas are a type of MCD used in Alaska which act similarly to CCDs and have
         * no government, being used only for statistical purposes.
         */
        class CensusSubarea(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Civil Townships are a type of MCD used in 17 states. In Wisconsin and New York, civil
         * townships are called "Towns." Civil Townships are unincorporated but have a town hall
         * and variably manage roads and infrastructure.
         */
        class CivilTownship(
            name: String,
            county: County,
            population: Int,
            government: Government?
        ) : MinorCivilDivision(name, county, population, government)

        /**
         * Charter Townships are a type of MCD used in Michigan. They act as more powerful or
         * municipal townships, able to establish civil services and having home rule. These are
         * most often found surrounding larger cities as their legal purpose is to protect
         * historical townships while providing adequate civil services to their larger populations.
         */
        class CharterTownship(
            name: String,
            county: County,
            population: Int,
            government: Government?
        ) : MinorCivilDivision(name, county, population, government) {
            init {
                require(population > 2000) { "population must be greater than 2000" }
            }
        }

        /**
         * New England Towns (officially just "Towns") are a type of MCD used in the New England
         * states. They function similarly to cities and villages seen in most states and are
         * legally incorporated (though not reflected as such on the Census) and have full
         * home-rule governments. Towns and their cities supplant county governments in the New
         * England states.
         */
        class NewEnglandTown(
            name: String,
            county: County,
            population: Int,
            government: Government
        ) : MinorCivilDivision(name, county, population, government)

        /**
         * Magisterial Districts are a type of MCD used in Virginia and West Virginia. These county
         * divisions are responsible for conducting elections and recording land ownership, but have
         * no government or other powers.
         */
        class MagisterialDistrict(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Election Districts are a type of MCD used in Maryland. They conduct elections and
         * censuses but have no government or other powers.
         */
        class ElectionDistrict(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Supervisors' Districts are a type of MCD used in Mississippi. They elect county
         * supervisors but have no local government.
         */
        class SupervisorsDistrict(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Parish Governing Authority Districts are a type of MCD used in Louisiana. They elect
         * county officials but have no local government.
         */
        class ParishGoverningAuthorityDistrict(
            name: String,
            parish: County,
            population: Int,
        ) : MinorCivilDivision(name, parish, population, null)

        /**
         * County Commissioner Districts are a type of MCD used in Tennessee. They elect
         * county commissioners but have no local government.
         */
        class CountyCommissionerDistrict(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Election Precincts are a type of MCD used in some counties in Nebraska and Illinois.
         * They conduct elections but have no local government.
         */
        class ElectionPrecinct(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Unorganized Territores (UTs) are a type of MCD used in 8 states. They generally have
         * lower populations than other MCDs in those states and do not have local governments, and
         * are also used for the locations of military bases.
         */
        class UnorganizedTerritory(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * American Indian Reservations are a type of MCD used in Maine and New York (in other states,
         * reservations are not considered separate county subdivisions). There is no specific MCD
         * government, but there is a tribal government which fulfills the purposes of government for
         * these areas.
         */
        class AmericanIndianReservation(
            name: String,
            county: County,
            population: Int,
            tribalGovernment: Government,
        ) : MinorCivilDivision(name, county, population, tribalGovernment)

        /**
         * Gores are a type of MCD found in Vermont and Maine. These are areas which were affected
         * by a survey error between two or more towns and have no local government.
         */
        class Gore(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Grants are a type of MCD found in New Hampshire and Vermont. These areas were granted to
         * specific owners by the government and have no local government.
         */
        class Grant(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Purchases are a type of MCD found in New Hampshire. These are areas which were purchased
         * in full by a specific owner and have no local government.
         */
        class Purchase(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Survey Townships are a type of MCD found in Coös County, New Hampshire. They are used
         * only for census purposes and have no local government.
         */
        class SurveyTownship(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Locations are a type of MCD found in New Hampshire. These areas have no unique history
         * other than being unassigned to any other town or county subdivision. They are used only
         * for census purposes and have no local government.
         */
        class Location(
            name: String,
            county: County,
            population: Int,
        ) : MinorCivilDivision(name, county, population, null)

        /**
         * Plantations are a type of MCD used in Maine. They have an intermediate population
         * between Unorganized Territories and Towns and have their own limited government.
         */
        class Plantation(
            name: String,
            county: County,
            population: Int,
            government: Government
        ) : MinorCivilDivision(name, county, population, government)

        /**
         * Unorganized Waters are areas of counties in open waters in the Great Lakes or Atlantic
         * Ocean. They have no population or government.
         */
        class UnorganizedWater(
            name: String,
            county: County,
        ) : MinorCivilDivision(name, county, 0, null)

        /**
         * Pseudo-MCDs are a type of MCD defined by the census for counties where there are no
         * county subdivisions, specifically Cleveland County, NC and Arlington County, VA. These
         * counties abolished their subdivisions and are thus listed by the Census Bureau as a
         * non-governmental subdivision used only for census purposes.
         */
        class PseudoMCD(
            county: County,
        ) : MinorCivilDivision(county.name, county, county.population, null)

        /**
         * Barrios are a type of MCD used in Puerto Rico. Barrios have no local government
         * and are administered by their Municipio governments.
         */
        class Barrio(
            name: String,
            municipio: County,
            population: Int,
        ) : MinorCivilDivision(name, municipio, population, null)

    }

    /**
     * Census County Divisions (CCDs) are the only county divisions used in the states in which
     * they are located (a state may not have both CCDs and MCDs). These are found mostly in states
     * with small populations in rural areas, meaning the census divisions have no government and
     * are administered by counties.
     */
    class CensusCountyDivision(
        name: String,
        county: County,
        population: Int,
    ) : CountySubdivision(name, county, population, null)


    /**
     * Places are defined by the Census as locations with a concentration of population, a name,
     * local recognition, and independence from other places. These places are almost all
     * incorporated, meaning they have their own government and powers.
     */
    sealed class Place(
        name: String,
        county: County?,
        population: Int,
        government: Government?,
        dependencyStatus: DependencyStatus,
    ) : CountySubdivision(name, county, population, government) {

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
        class City(
            name: String,
            county: County,
            population: Int,
            government: Government,
            dependencyStatus: DependencyStatus,
        ) : Place(name, county, population, government, dependencyStatus) {

            init {
                // if (county.state uses CCDs) require(dependencyStatus == DEPENDENT);
            }

        }

        /**
         * Consolidated City-Counties (or "City-counties") are places where city and county
         * governments are merged into one unified juristictive body. In these cases, city councils
         * also act as county boards, and mayors act as county chiefs. The city and county still
         * exist, but are merged into one unit. In CCD states, the city-counites are still
         * dependent on their underlying county subdivisions, despite taking up the entire county.
         * In Alaska, these are called Consolidated City-Boroughs. In Georgia, these are called
         * Unified Governments.
         */
        class ConsolidatedCityCounty(
            name: String,
            county: County,
            population: Int,
            dependencyStatus: DependencyStatus,
        ) : Place(name, county, population, county.government, dependencyStatus)

        /**
         * County Balances are places similar to Consolidated City-Counties and have unified
         * governments between their city and county. Unlike City-counties, however, County
         * Balances do not fully encompass their county and some cities and towns also exist
         * separately within the county with some municipal powers.
         */
        class CountyBalance(
            name: String,
            county: County,
            population: Int,
            dependencyStatus: DependencyStatus,
        ) : Place(name, county, population, county.government, dependencyStatus)

        /**
         * Independent Cities are places where a city government has full jurisdiction without
         * an encompassing county-- that is, the county does not exist in any form. The city itself
         * acts as a county equivalent and a county subdivision. In CCD states, Independent Cities
         * are still dependent upon a single CCD (this applies only to Carson City, NV, which is
         * dependent upon the Carson City CCD, despite there being no county).
         */
        class IndependentCity(
            name: String,
            state: State,
            population: Int,
            government: Government,
            dependencyStatus: DependencyStatus,
        ) : Place(name, null, population, government, dependencyStatus)

        /**
         * Towns are a type of place found in 31 state, and vary in definition between states.
         * Where they are present, they are always incorporated places with a lesser population
         * requirement than cities. Towns have their own municipal governments and vary in their
         * dependency status.
         */
        class Town(
            name: String,
            county: County,
            population: Int,
            government: Government,
            dependencyStatus: DependencyStatus,
        )

        /**
         * Villages are a type of place found in 16 states, and very in definition between states.
         * Where they are present, they are always incorporated places with a lesser population
         * requirement than cities or towns. Villages have their own municipal governments and very
         * in their dependency status.
         */
        class Village(
            name: String,
            county: County,
            population: Int,
            government: Government,
            dependencyStatus: DependencyStatus,
        )

        /**
         * Boroughs (not to be confused with Alaska's county equivalent also called "Boroughs") are
         * a type of place found in 4 states. Boroughs vary in definition between states. They have
         * their own municipal government and have a lower population requirement than cities.
         * In New York City, the five boroughs are each coextensive with their five counties. The
         * single City of New York is a dependent city on the five boroughs / counties, while the
         * boroughs are non-governmental MCDs dependent on their counties (the counties have no
         * governments and are powerless).
         */
        class Borough(
            name: String,
            county: County,
            population: Int,
            government: Government,
            dependencyStatus: DependencyStatus,
        )

        /**
         * Home-Rule Municipalities are a type of place found in Pennsylvania which are always
         * independent, acting as their own county subdivision. These have a unique charter
         * granting home rule and additional powers. There are 6 of these in Pennsylvania found
         * surrounding Pittsburgh, PA.
         */
        class HomeRuleMunicipality(
            name: String,
            county: County,
            population: Int,
            government: Government,
        ) : Place(name, county, population, government, DependencyStatus.INDEPENDENT)

        /**
         * There is one Corporation in the United States: Ranson, WV. This place was founded by a
         * corporation and acts identically to any other city in West Virginia.
         */
        class Corporation(
            name: String,
            county: County,
            population: Int,
            government: Government,
        ) : Place(name, county, population, government, DependencyStatus.DEPENDENT)

        /**
         * Census-Designated Places (CDPs) are found in every state and do not act as a county
         * subdivision, used only as a dependent place for the Census. They are the only places
         * occupying unincorporated areas and are usually loosely defined with a significant enoguh
         * population and locally-knwon name to warrant their reflection on the Census. They have no
         * local government or powers and are used only for statistical purposes.
         * In Hawaii, all places are CDPs, since the state government of Hawaii determined that the
         * county governments were sufficient for the entire population.
         */
        class CensusDesignatedPlace(
            name: String,
            county: County,
            population: Int,
        ) : Place(name, county, population, null, DependencyStatus.DEPENDENT)

        enum class DependencyStatus {
            /**
             * Dependent Places are found in all CCD states and some MCD states, in which the place
             * is dependent upon (sits "on top" of) other county divisions.
             */
            DEPENDENT,
            /**
             * Independent Places are found in some MCD states, in which the place is not dependent on
             * any other county division and represents its own division of its county.
             */
            INDEPENDENT,
        }

    }

}
