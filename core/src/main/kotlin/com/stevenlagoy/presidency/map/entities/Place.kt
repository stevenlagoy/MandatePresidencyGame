package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.map.Descriptor
import com.stevenlagoy.presidency.map.HasFIPS
import com.stevenlagoy.presidency.map.MapRegion
import com.stevenlagoy.presidency.politics.government.Government

/**
 * Places are defined by the Census as locations with a concentration of population, a name,
 * local recognition, and independence from other places. Many places are almost all
 * incorporated, meaning they have their own government and powers.
 */
open class Place(
    engine: Engine,
    name: String = "",
    val type: PlaceType = PlaceTypes.CITY,
    _container: PlaceContainer? = null,
    val government: Government? = null,
    FIPS: String = "",
    UACE: String? = null,
    color: Int? = null,
    // MapEntity
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: MapRegion? = null,
) : MapEntity(engine, name, squareMileage, population, demographics, descriptors, region), HasFIPS, PlaceContainer {

    override val censusRegion: CensusRegion? = countyEquivalent.censusRegion
    override val censusDivision: CensusDivision? = countyEquivalent.censusDivision

    lateinit var container: PlaceContainer
        internal set

    private val _additionalCounties: MutableSet<County> = mutableSetOf()
    val additionalCounties: Set<County>
        get() = _additionalCounties

    val countyEquivalents: Set<CountyEquivalent> get() = _additionalCounties + container.countyEquivalent

    val countySubdivisions: Set<CountySubdivision> = mutableSetOf()

    val commonName: String = name

    val fullName: String = String.format(type.nameTemplate(container.countyEquivalent.stateEquivalent.abbreviation), name)

    open val qualifiedName: String get() = "$name, ${container.countyEquivalent.qualifiedName}"

    override var FIPS: String = FIPS
        internal set

    var color: Int? = color
        internal set

    override val countyEquivalent: CountyEquivalent get() = container.countyEquivalent

    val incorporationStatus: IncorporationStatus =
        if (type.alwaysIncorporated) IncorporationStatus.INCORPORATED else IncorporationStatus.UNINCORPORATED

    val dependencyStatus: DependencyStatus
        get() = if (container is CountySubdivision) DependencyStatus.DEPENDENT else DependencyStatus.INDEPENDENT

    init {
        if (_container != null) {
            container = _container
            validateDependency()
        }
        // else: the constructing subclass sets `container` and calls `validateDependency()` afterwards
    }

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    protected fun validateDependency() {
        require(!(dependencyStatus == DependencyStatus.INDEPENDENT && !type.allowsIndependent)) { "${type.label} cannot be independent" }
        require(!(dependencyStatus == DependencyStatus.DEPENDENT && !type.allowsDependent)) { "${type.label} cannot be dependent" }
        require(!(countyEquivalent.stateEquivalent.subdivisionScheme == StateEquivalent.SubdivisionScheme.CCD && dependencyStatus == DependencyStatus.INDEPENDENT)) {
            "Independent places aren't valid in CCD states (${countyEquivalent.stateEquivalent.name})"
        }
    }

    internal fun addAdditionalCounty(county: County) {
        _additionalCounties += county
    }

    override fun toJson() = super.toJson().merge(

    ).apply { key = qualifiedName }

    override fun fromJson(json: JSONObject) = apply {
        super.fromJson(json)
    }

    enum class IncorporationStatus { INCORPORATED, UNINCORPORATED }

    enum class DependencyStatus { DEPENDENT, INDEPENDENT }
}
