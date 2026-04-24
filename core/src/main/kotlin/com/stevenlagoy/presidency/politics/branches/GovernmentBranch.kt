package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.map.HasPolitics

abstract class GovernmentBranch(

) : HasPolitics, Jsonic<GovernmentBranch> {
}
