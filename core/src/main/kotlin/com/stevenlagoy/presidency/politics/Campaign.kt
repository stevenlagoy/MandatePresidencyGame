package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.government.GovernmentPosition
import kotlin.jvm.optionals.getOrNull

class Campaign(
    engine: Engine,
    /**
     * Every [com.stevenlagoy.presidency.politics.government.GovernmentPosition] this campaign
     * fills if it wins, mapped to who would hold it. Ordinary races has one entry, and a joint
     * ticket (like for the President and Vice President) has several.
     */
    var candidates: Map<GovernmentPosition, PoliticalActor> = emptyMap(),
    var affiliatedParty: Party? = null
) : Issue(
    engine,
    "${candidates.values.joinToString(", ") { it.name.commonName }} ticket",
    "...",
    emptySet()
) {

    val campaigners: Collection<PoliticalActor>
        get() = candidates.values

    /**
     * Single votable position representing a vote for this campaign.
     */
    var candidacy: IssuePosition = IssuePosition(engine, this, title = "Vote ${candidates.values.joinToString(", ") { it.name.preferredFamily }}")
        internal set

    init {
        positions = setOf(candidacy)
    }

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("candidates", candidates.map { (targetPosition, candidate) -> JSONObject(targetPosition.title, candidate.name.indexedName) }),
        JSONObject("affiliatedParty", affiliatedParty?.name)
    )

    override fun fromJson(json: JSONObject) = apply {
        candidates = json.requireJson("campaigners").associate {
            engine.POLITICS_MANAGER.GOVERNMENT_MANAGER.matchGovernmentPosition((it as JSONObject).key).get() to engine.CHARACTER_MANAGER.matchCitizenByIndexedName(it.requireString()).get() as PoliticalActor
        }
        affiliatedParty = engine.POLITICS_MANAGER.PARTY_MANAGER.matchParty(json.requireString("affiliatedParty")).getOrNull()
        candidacy = IssuePosition(engine, this, title = "Vote ${candidates.values.joinToString(", ") { it.name.preferredFamily }}")
        positions = setOf(candidacy)
    }
}
