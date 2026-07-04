package com.stevenlagoy.presidency.demographics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.Citizen
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.politics.Party

/**
 * Blocs are groups defined by membership in a demographic subset, whether by Race & Ethnicity,
 * Religion, Generation, or any other Demographic Category.
 *
 * @property name Name of this Bloc, which should be uniquely identifying.
 * @property category Demographic Category describing the membership criterion for this Bloc.
 * @property percentageMembership Nationwide percentage of citizens who are a member of this Bloc.
 * @property members [Character]s who are a member of this Bloc.
 * @property superBloc Parent or encompassing Bloc of this Bloc. All members of this Bloc are members of the [superBloc]. May be null for a root Bloc.
 * @property subBlocs Child or encompassed Blocs of this Bloc. All members of this Bloc are members of each of the [subBlocs].
 * @property ancestorBlocs Collection of ancestor Blocs, following [superBloc] references. Includes self, ordered from self to parent.
 * @property descendantBlocs Collection of descendant Blocs, following [subBlocs] references. Includes self. Ordered from self to children, but each layer is unordered.
 *
 * @author Steven LaGoy
 */
class Bloc (
    engine: Engine,
    name: String = "",
    category: DemographicCategory = DemographicCategory.NONE,
    percentageMembership: Double = 0.0,
    members: Collection<Citizen> = setOf(),
    superBloc: Bloc? = null,
    subBlocs: MutableList<Bloc> = mutableListOf(),
) : JSONSerializable<Bloc>, EngineBound(engine) {

    var name: String = name
        internal set

    var category: DemographicCategory = category
        internal set

    var percentageMembership: Double = percentageMembership
        internal set

    val ancestorBlocs: List<Bloc>
        get() = if (superBloc != null) listOf(superBloc!!) + superBloc!!.ancestorBlocs else listOf()

    var superBloc: Bloc? = superBloc
        internal set

    val subBlocs: MutableList<Bloc> = subBlocs.toMutableList()

    val descendantBlocs: List<Bloc>
        get() = subBlocs + subBlocs.flatMap { it.descendantBlocs }.toList()

    companion object {
        val CITIZENS = Bloc(Engine.getInstance(), "Citizens", DemographicCategory.NONE, 1.0)
        val UNAFFILIATED = Bloc(Engine.getInstance(), "Unaffiliated Voters", DemographicCategory.NONE, 1.0)
        val AFFILIATED = Bloc(Engine.getInstance(), "Party-Affiliated Voters", DemographicCategory.NONE, 1.0)

        fun resolvePartyAffiliation(party: Party): Bloc {
            return AFFILIATED
        }
    }

    val members: MutableSet<Citizen> = members.toMutableSet()

    /**
     * Collection of the names of ancestor Blocs. Includes self.
     * @see [ancestorBlocs]
     */
    val ancestorNames: Collection<String> by lazy(LazyThreadSafetyMode.NONE) { ancestorBlocs.map { it.name } }

    /**
     * Collection of the names of descendant Blocs. Includes self.
     * @see [descendantBlocs]
     */
    val descendantNames: Collection<String> by lazy(LazyThreadSafetyMode.NONE) { descendantBlocs.map { it.name } }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun toJson() = JSONObject(name, mapOf(
        "name" to name,
        "category" to category,
        "superBloc" to superBloc,
        "subBlocs" to subBlocs,
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        name = json.requireString("name")
        category = DemographicCategory.valueOf(json.requireString("category").uppercase().replace(Regex("[^A-Z]"), "_"))
        superBloc = engine.DEMOGRAPHICS_MANAGER.matchBloc(json.requireString("superBloc")).orElseThrow()
        subBlocs.clear()
        subBlocs.addAll(json.requireArray("subBlocs").map { engine.DEMOGRAPHICS_MANAGER.matchBloc(it as String) }.filter { it.isPresent }.map { it.get() })
    }

}
