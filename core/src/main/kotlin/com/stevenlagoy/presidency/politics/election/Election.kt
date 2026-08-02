package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.politics.Campaign
import com.stevenlagoy.presidency.politics.Issue
import com.stevenlagoy.presidency.politics.government.GovernmentPosition

class Election(
    engine: Engine,
    targetPosition: GovernmentPosition = GovernmentPosition(engine),
    val campaigns: MutableSet<Campaign> = mutableSetOf(),
    isPartisan: Boolean = false,
    var incumbent: PoliticalActor? = null,
) : Contest(engine) {

    val name get() = "${pollsOpenDate.year} ${constituency.name} ${targetPosition.title} election"

    override var constituency: MapEntity = targetPosition.constituency

    var targetPosition: GovernmentPosition = targetPosition
        internal set

    var isPartisan: Boolean = isPartisan
        internal set

    var resultsByArea: Map<MapEntity, ElectionResult> = emptyMap()
        internal set

    override var results: Map<Ballot, Int>
        get() = resultsByArea[constituency]?.tally ?: emptyMap()
        set(_) {}

    /**
     * Every campaign whose [Campaign] is among the winning positions, including every winner in a
     * multi-seat race.
     */
    val winningCampaigns: List<Campaign>
        get() = getWinningPositions().mapNotNull { it.issue as? Campaign }

    val winnersByPosition: Map<GovernmentPosition, PoliticalActor>
        get() = winningCampaigns.flatMap { it.candidates.entries }.associate { it.key to it.value }

    override var issues: Set<Issue> = campaigns

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("targetPosition", targetPosition.title),
        JSONObject("campaigns", campaigns.map { it.toJson() }),
        JSONObject("isPartisan", isPartisan),
        JSONObject("incumbent", incumbent?.toJson()),
    ).apply { key = name }

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
        campaigns.clear()
        campaigns.addAll(json.requireJson("campaigns").requireArray().map { Campaign(engine, it as JSONObject) })
        issues = campaigns
    }
}
