package com.stevenlagoy.presidency.map2

enum class StateKind { STATE, COMMONWEALTH, DISTRICT, TERRITORY }
enum class SubdivisionScheme { CCD, MCD }
enum class Incorporation { INCORPORATED, UNINCORPORATED }
enum class Dependency { DEPENDENT, INDEPENDENT }

class StateEquivalent(
    val name: String,
    val kind: StateKind,
    val subdivisionScheme: SubdivisionScheme,
)

sealed interface PlaceContainer {
    val county: County2
}

class County2(
    val name: String,
    val state: StateEquivalent,
) : PlaceContainer {

    override val county: County2 get() = this

    private val _subdivisions = mutableListOf<CountySubdivision2>()
    val subdivisions: List<CountySubdivision2> get() = _subdivisions

    fun addSubdivision(subdivision: CountySubdivision2) {
        val ok = when (state.subdivisionScheme) {
            SubdivisionScheme.CCD -> subdivision is CensusCountyDivision
            SubdivisionScheme.MCD -> subdivision is MinorCivilDivision
        }
        require(ok) {
            "${subdivision::class.simpleName} is not valid in a ${state.subdivisionScheme} state (${state.name}"
        }
        _subdivisions += subdivision
    }
}

sealed class CountySubdivision2(
    val name: String,
    override val county: County2,
) : PlaceContainer

class CensusCountyDivision(
    name: String,
    county: County2,
) : CountySubdivision2(name, county)

class MinorCivilDivision(
    name: String,
    county: County2,
    val legalDesignation: String,
) : CountySubdivision2(name, county)

class Place(
    val name: String,
    val incorporation: Incorporation,
    val container: PlaceContainer,
) {
    val county: County2 get() = container.county

    val dependency: Dependency
        get() = if (container is CountySubdivision2) Dependency.DEPENDENT else Dependency.INDEPENDENT

    init {
        require(!(county.state.subdivisionScheme == SubdivisionScheme.CCD && dependency == Dependency.INDEPENDENT)) {
            "Independent places aren't valid in CCD states (${county.state.name})"
        }
    }
}
