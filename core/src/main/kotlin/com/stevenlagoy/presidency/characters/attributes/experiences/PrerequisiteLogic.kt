package com.stevenlagoy.presidency.characters.attributes.experiences

enum class PrerequisiteLogic(
    val evaluate: (Collection<Experience>, Collection<Experience>) -> Boolean
) {
    ANY(fun(priors: Collection<Experience>, prerequisites: Collection<Experience>) = prerequisites.isEmpty() || prerequisites.find { it in priors } != null),
    ALL(fun(priors: Collection<Experience>, prerequisites: Collection<Experience>) = prerequisites.isEmpty() || prerequisites.find { it !in priors } == null),
}
