package com.stevenlagoy.presidency.politics.news

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.map.entities.Place
import com.stevenlagoy.presidency.politics.PoliticalAlignment

class NewsPublisher(
    engine: Engine,
    _name: String? = null,
    _abbreviation: String? = null,
    _preferAbbreviation: Boolean? = null,
) : EngineBound(engine) {

    lateinit var name: String
        internal set

    lateinit var abbreviation: String
        internal set

    var preferAbbreviation: Boolean = false
        internal set

    val focuses: MutableSet<NewsFocus> = mutableSetOf()

    /** How often articles are published. */
    val schedule: Int = 0

    /** Number of articles in each publication. */
    val publicationSize: Int = 0

    val headquarters: Place? = null

    val alignment: PoliticalAlignment = PoliticalAlignment()

    val readerbase: MutableMap<Bloc, Int> = mutableMapOf()

    /** The mediums(s) of publication: print, digitial, television, radio. */
    val mediums: MutableSet<NewsMedium> = mutableSetOf()

    val articles: MutableSet<NewsArticle> = HashSet()

    /** List of stories by prominence and recency which will be selected for publication. */
    val backlog: Int = 0

    init {
        if (_name != null) name = _name
        if (_abbreviation != null) abbreviation = _abbreviation
        if (_preferAbbreviation != null) preferAbbreviation = _preferAbbreviation
    }

    enum class NewsFocus {
        POLITICS,
        POLICIES,
        PROFILES,
        CULTURE,
        SCIENCE,
        BUSINESS,
        SPORTS,
        OPINIONS,
        INVESTIGATIONS,
    }

    enum class NewsMedium {
        NEWSPAPER,
        MAGAZINE,
        TABLOID,
        WEBSITE,
        RADIO,
        TELEVISION,
    }

}
