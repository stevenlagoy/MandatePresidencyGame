package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.Citizen
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class Family(
    engine: Engine,
    var mother: Citizen? = null,
    var father: Citizen? = null,
    var spouse: Citizen? = null,
    val children: MutableSet<Citizen> = mutableSetOf(),
) : Jsonic<Family>, EngineBound(engine) {

    val parents = setOf(mother, father).filterNotNull()

    val grandparents: Set<Citizen> get() = parents.flatMap { it.family.parents }.toSet()

    val siblings: Set<Citizen> get() = parents.flatMap { it.family.children }.toSet()

    val piblings: Set<Citizen> get() = parents.flatMap { it.family.siblings }.toSet()

    val cousins: Set<Citizen> get() = piblings.flatMap { it.family.children }.toSet()

    val niblings: Set<Citizen> get() = cousins.flatMap { it.family.children }.toSet()

    val grandchildren: Set<Citizen> get() = children.flatMap { it.family.children }.toSet()

    init {
        require(mother?.sex == Sex.FEMALE || mother?.sex == Sex.INTERSEX) { "Mother's sex must be female or intersex" }
        require(father?.sex == Sex.MALE || father?.sex == Sex.INTERSEX) { "Father's sex must be male or intersex" }
    }

    fun copy(other: Family) {
        this.mother = other.mother
        this.father = other.father
        this.spouse = other.spouse
        this.children.apply {
            clear()
            addAll(other.children)
        }
    }

    override fun fromJson(json: JSONObject) = this

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("parents", parents.map { it.id }),
        JSONObject("spouse", spouse?.id),
        JSONObject("children", children.map { it.id }),
    ))

    fun getSideSize(): Int {
        return (mother?.family?.getSideSize() ?: 0) + (father?.family?.getSideSize() ?: 0) + (spouse?.family?.getSideSize() ?: 0) + children.sumOf { it.family.getSideSize() }
    }

    fun getFamilyPlan()= FamilyManager.FamilyPlan(
        mother != null,
        mother?.family?.getSideSize() ?: 0,
        father != null,
        father?.family?.getSideSize() ?: 0,
        siblings.size,
        siblings.sumOf { it.family.getSideSize() },
        spouse != null,
        spouse?.family?.getSideSize() ?: 0,
        children.size,
        children.sumOf { it.family.getSideSize() },
    )
}
