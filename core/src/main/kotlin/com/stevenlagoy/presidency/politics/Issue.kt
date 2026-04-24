package com.stevenlagoy.presidency.politics

open class Issue(
    var title: String,
    var description: String,
    var positions: Set<IssuePosition>,
    var subissues: Set<Issue>? = null,
)
