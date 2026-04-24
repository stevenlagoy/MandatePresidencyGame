package com.stevenlagoy.presidency.politics

class IssuePosition (
    var issue: Issue,
    var title: String,
    var description: String = title,
    var alignment: IntArray = IntArray(2),
)
