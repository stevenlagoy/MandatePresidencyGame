package com.stevenlagoy.presidency.politics.voting

sealed class VotingMethod {

    class Primary : VotingMethod()

    class Caucus: VotingMethod()
}
