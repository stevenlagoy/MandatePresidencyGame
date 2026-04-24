package com.stevenlagoy.presidency.demographics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.Citizen
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
 * @property ancestors Collection of ancestor Blocs, following [superBloc] references. Includes self, ordered from self to parent.
 * @property descendants Collection of descendant Blocs, following [subBlocs] references. Includes self. Ordered from self to children, but each layer is unordered.
 *
 * @author Steven LaGoy
 */
class Bloc (
    val name: String,
    val category: DemographicCategory,
    val percentageMembership: Double,
    members: Collection<Citizen> = setOf(),
    private var _superBloc: Bloc? = null,
    private val _subBlocs: MutableList<Bloc> = mutableListOf(),
) : Jsonic<Bloc>
{

    companion object {
        val CITIZENS = Bloc("Citizens", DemographicCategory.NONE, 1.0)
        val UNAFFILIATED = Bloc("Unaffiliated Voters", DemographicCategory.NONE, 1.0)
        val AFFILIATED = Bloc("Party-Affiliated Voters", DemographicCategory.NONE, 1.0)

        fun resolvePartyAffiliation(party: Party): Bloc {
            return AFFILIATED
        }
    }

    val members: MutableSet<Citizen> = members.toMutableSet()

    val superBloc: Bloc? get() = _superBloc

    val subBlocs: List<Bloc> get() = _subBlocs

    fun addSubBloc(bloc: Bloc) {
        _subBlocs.add(bloc)
        bloc._superBloc = this
    }

    val ancestors: Collection<Bloc> by lazy(LazyThreadSafetyMode.NONE) {
        _superBloc?.let { listOf(this) + it.ancestors } ?: listOf(this)
    }

    /**
     * Collection of the names of ancestor Blocs. Includes self.
     * @see [ancestors]
     */
    val ancestorNames: Collection<String> by lazy(LazyThreadSafetyMode.NONE) { ancestors.map { it.name } }

    val descendants: Collection<Bloc> by lazy(LazyThreadSafetyMode.NONE) {
        buildList { add(this@Bloc); _subBlocs.forEach { addAll(it.descendants) } }
    }

    /**
     * Collection of the names of descendant Blocs. Includes self.
     * @see [descendants]
     */
    val descendantNames: Collection<String> by lazy(LazyThreadSafetyMode.NONE) { descendants.map { it.name } }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun toString() = """[
        name: $name,
        category: $category,
        superBloc: $superBloc,
        subBlocs: $subBlocs,
    ]""".trimIndent()

    override fun toJson() = JSONObject(name, mapOf(
        "name" to name,
        "category" to category,
        "superBloc" to superBloc,
        "subBlocs" to subBlocs,
    ))

    override fun fromJson(json: JSONObject) = this.apply {

    }

}
