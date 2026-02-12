package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.presidency.politics.FederalLevel

enum class RoleKT(val level: FederalLevel?) {
    PARTY_LEADER(null),
    LOBBYIST(null),
    CONSULTANT(null),
    CANDIDATE(null),
    CAMPAIGN_MANAGER(null),
    CAMPAIGN_CABINET_MEMBER(null),
    UNION_LEADER(null),
    MEDIAPERSON(null),
    NONE(null);
}
