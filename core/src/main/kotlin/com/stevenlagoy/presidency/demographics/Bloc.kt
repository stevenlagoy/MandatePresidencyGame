package com.stevenlagoy.presidency.demographics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.Character
import com.stevenlagoy.presidency.data.Repr

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
    val category: DemographicCategoryKT,
    val percentageMembership: Double,
    members: Collection<Character> = setOf(),
    private var _superBloc: Bloc? = null,
    private val _subBlocs: MutableList<Bloc> = mutableListOf(),
) : Repr<Bloc>, Jsonic<Bloc>
{
    val members: MutableSet<Character> = members.toMutableSet();

    val superBloc: Bloc? get() = _superBloc

    val subBlocs: List<Bloc> get() = _subBlocs

    fun addSubBloc(bloc: Bloc) {
        _subBlocs.add(bloc);
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

    override fun toRepr(): String? {
        TODO("Not yet implemented")
    }

    override fun fromRepr(repr: String?): Bloc? {
        TODO("Not yet implemented")
    }

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): Bloc? {
        TODO("Not yet implemented")
    }

}
