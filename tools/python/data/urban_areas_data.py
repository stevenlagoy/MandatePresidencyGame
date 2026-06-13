import csv
import json
import random
from io import StringIO

urban_areas_data = {
    "00037": {
        "name": "Abbeville",
        "state": "Abbeville LA",
        "population": 18078,
        "housing": 8521,
        "land_area": 28823510.0,
        "water_area": 340389.0
    },
    "00064": {
        "name": "Abbeville",
        "state": "Abbeville SC",
        "population": 4940,
        "housing": 2453,
        "land_area": 12815034.0,
        "water_area": 6144.0
    },
    "00145": {
        "name": "Aberdeen",
        "state": "Aberdeen SD",
        "population": 27982,
        "housing": 13246,
        "land_area": 35989073.0,
        "water_area": 183150.0
    },
    "00172": {
        "name": "Aberdeen",
        "state": "Aberdeen WA",
        "population": 26603,
        "housing": 11561,
        "land_area": 28416189.0,
        "water_area": 1802207.0
    },
    "00253": {
        "name": "Abilene",
        "state": "Abilene KS",
        "population": 6605,
        "housing": 3216,
        "land_area": 9426821.0,
        "water_area": 1877.0
    },
    "00280": {
        "name": "Abilene",
        "state": "Abilene TX",
        "population": 118138,
        "housing": 50514,
        "land_area": 160509868.0,
        "water_area": 1015710.0
    },
    "00388": {
        "name": "Ada",
        "state": "Ada OH",
        "population": 5343,
        "housing": 1984,
        "land_area": 5425263.0,
        "water_area": 0.0
    },
    "00415": {
        "name": "Ada",
        "state": "Ada OK",
        "population": 17264,
        "housing": 8654,
        "land_area": 36906552.0,
        "water_area": 32242.0
    },
    "00442": {
        "name": "Adairsville",
        "state": "Adairsville GA",
        "population": 5799,
        "housing": 2287,
        "land_area": 13860065.0,
        "water_area": 0.0
    },
    "00469": {
        "name": "Adel",
        "state": "Adel GA",
        "population": 7034,
        "housing": 2965,
        "land_area": 15827696.0,
        "water_area": 171671.0
    },
    "00496": {
        "name": "Adel",
        "state": "Adel IA",
        "population": 5674,
        "housing": 2250,
        "land_area": 7103711.0,
        "water_area": 16231.0
    },
    "00550": {
        "name": "Adjuntas",
        "state": "Adjuntas PR",
        "population": 8008,
        "housing": 3687,
        "land_area": 12594208.0,
        "water_area": 1242.0
    },
    "00577": {
        "name": "Adrian",
        "state": "Adrian MI",
        "population": 29206,
        "housing": 11726,
        "land_area": 34641303.0,
        "water_area": 384850.0
    },
    "00604": {
        "name": "Agat--Apra Harbor",
        "state": "Agat--Apra Harbor GU",
        "population": 8712,
        "housing": 2881,
        "land_area": 10357136.0,
        "water_area": 0.0
    },
    "00631": {
        "name": "Aguadilla--Isabela--San Sebasti\u00e1n",
        "state": "Aguadilla--Isabela--San Sebasti\u00e1n PR",
        "population": 232573,
        "housing": 114369,
        "land_area": 485158099.0,
        "water_area": 371255.0
    },
    "00658": {
        "name": "Ahoskie",
        "state": "Ahoskie NC",
        "population": 4861,
        "housing": 2308,
        "land_area": 8613935.0,
        "water_area": 0.0
    },
    "00660": {
        "name": "Aibonito",
        "state": "Aibonito PR",
        "population": 20255,
        "housing": 9140,
        "land_area": 34460553.0,
        "water_area": 63660.0
    },
    "00766": {
        "name": "Akron",
        "state": "Akron OH",
        "population": 541879,
        "housing": 251080,
        "land_area": 778645785.0,
        "water_area": 15073639.0
    },
    "00820": {
        "name": "Alamogordo",
        "state": "Alamogordo NM",
        "population": 30801,
        "housing": 15200,
        "land_area": 35564058.0,
        "water_area": 4382.0
    },
    "00847": {
        "name": "Alamosa",
        "state": "Alamosa CO",
        "population": 10965,
        "housing": 4656,
        "land_area": 19903192.0,
        "water_area": 38995.0
    },
    "00901": {
        "name": "Albany",
        "state": "Albany GA",
        "population": 85960,
        "housing": 39864,
        "land_area": 172252814.0,
        "water_area": 1905095.0
    },
    "00955": {
        "name": "Albany",
        "state": "Albany OR",
        "population": 62074,
        "housing": 25245,
        "land_area": 59695502.0,
        "water_area": 191140.0
    },
    "00970": {
        "name": "Albany--Schenectady",
        "state": "Albany--Schenectady NY",
        "population": 593142,
        "housing": 272369,
        "land_area": 702671342.0,
        "water_area": 14075226.0
    },
    "00982": {
        "name": "Albemarle",
        "state": "Albemarle NC",
        "population": 16988,
        "housing": 7840,
        "land_area": 43150510.0,
        "water_area": 47629.0
    },
    "01009": {
        "name": "Albert Lea",
        "state": "Albert Lea MN",
        "population": 17992,
        "housing": 8366,
        "land_area": 27963336.0,
        "water_area": 1396477.0
    },
    "01036": {
        "name": "Albertville",
        "state": "Albertville AL",
        "population": 38476,
        "housing": 15505,
        "land_area": 90243946.0,
        "water_area": 613266.0
    },
    "01090": {
        "name": "Albion",
        "state": "Albion MI",
        "population": 8133,
        "housing": 3472,
        "land_area": 12150862.0,
        "water_area": 136862.0
    },
    "01117": {
        "name": "Albion",
        "state": "Albion NY",
        "population": 7216,
        "housing": 2746,
        "land_area": 7626920.0,
        "water_area": 0.0
    },
    "01171": {
        "name": "Albuquerque",
        "state": "Albuquerque NM",
        "population": 769837,
        "housing": 335464,
        "land_area": 681352326.0,
        "water_area": 6964855.0
    },
    "01252": {
        "name": "Alexander City",
        "state": "Alexander City AL",
        "population": 8920,
        "housing": 4428,
        "land_area": 25354836.0,
        "water_area": 35945.0
    },
    "01273": {
        "name": "Alexandria",
        "state": "Alexandria IN",
        "population": 6140,
        "housing": 2955,
        "land_area": 9853044.0,
        "water_area": 11855.0
    },
    "01279": {
        "name": "Alexandria",
        "state": "Alexandria LA",
        "population": 78305,
        "housing": 35538,
        "land_area": 167600150.0,
        "water_area": 1851472.0
    },
    "01306": {
        "name": "Alexandria",
        "state": "Alexandria MN",
        "population": 18957,
        "housing": 9895,
        "land_area": 58462308.0,
        "water_area": 18928970.0
    },
    "01387": {
        "name": "Algona",
        "state": "Algona IA",
        "population": 5443,
        "housing": 2684,
        "land_area": 8681037.0,
        "water_area": 16085.0
    },
    "01414": {
        "name": "Alice",
        "state": "Alice TX",
        "population": 19413,
        "housing": 7966,
        "land_area": 25980592.0,
        "water_area": 0.0
    },
    "01441": {
        "name": "Allegan",
        "state": "Allegan MI",
        "population": 7247,
        "housing": 3137,
        "land_area": 17817488.0,
        "water_area": 2275950.0
    },
    "01450": {
        "name": "Allendale",
        "state": "Allendale MI",
        "population": 25094,
        "housing": 8006,
        "land_area": 27311451.0,
        "water_area": 158913.0
    },
    "01495": {
        "name": "Allentown--Bethlehem",
        "state": "Allentown--Bethlehem PA--NJ",
        "population": 621703,
        "housing": 251480,
        "land_area": 677400838.0,
        "water_area": 9302819.0
    },
    "01522": {
        "name": "Alliance",
        "state": "Alliance NE",
        "population": 8119,
        "housing": 3956,
        "land_area": 12704963.0,
        "water_area": 31691.0
    },
    "01549": {
        "name": "Alliance",
        "state": "Alliance OH",
        "population": 30944,
        "housing": 14282,
        "land_area": 44687294.0,
        "water_area": 230974.0
    },
    "01630": {
        "name": "Alma--St. Louis",
        "state": "Alma--St. Louis MI",
        "population": 17417,
        "housing": 5455,
        "land_area": 23400576.0,
        "water_area": 551837.0
    },
    "01684": {
        "name": "Alpena",
        "state": "Alpena MI",
        "population": 15425,
        "housing": 8062,
        "land_area": 32915693.0,
        "water_area": 1992202.0
    },
    "01701": {
        "name": "Alpine",
        "state": "Alpine CA",
        "population": 13307,
        "housing": 5022,
        "land_area": 25499890.0,
        "water_area": 1475.0
    },
    "01711": {
        "name": "Alpine",
        "state": "Alpine TX",
        "population": 6283,
        "housing": 3292,
        "land_area": 11103460.0,
        "water_area": 688.0
    },
    "01738": {
        "name": "Altavista",
        "state": "Altavista VA",
        "population": 4597,
        "housing": 2250,
        "land_area": 15387790.0,
        "water_area": 163736.0
    },
    "01765": {
        "name": "Alton",
        "state": "Alton IL",
        "population": 79260,
        "housing": 38052,
        "land_area": 154514205.0,
        "water_area": 2463025.0
    },
    "01792": {
        "name": "Altoona",
        "state": "Altoona PA",
        "population": 74426,
        "housing": 34467,
        "land_area": 95860663.0,
        "water_area": 160936.0
    },
    "01846": {
        "name": "Altus",
        "state": "Altus OK",
        "population": 18870,
        "housing": 9194,
        "land_area": 40550855.0,
        "water_area": 525871.0
    },
    "01873": {
        "name": "Alva",
        "state": "Alva OK",
        "population": 5145,
        "housing": 2637,
        "land_area": 9442972.0,
        "water_area": 0.0
    },
    "01900": {
        "name": "Alvarado",
        "state": "Alvarado TX",
        "population": 5034,
        "housing": 1869,
        "land_area": 7883213.0,
        "water_area": 39327.0
    },
    "01927": {
        "name": "Amarillo",
        "state": "Amarillo TX",
        "population": 205860,
        "housing": 87615,
        "land_area": 217541054.0,
        "water_area": 1780045.0
    },
    "02008": {
        "name": "Americus",
        "state": "Americus GA",
        "population": 17407,
        "housing": 7609,
        "land_area": 28436180.0,
        "water_area": 154905.0
    },
    "02062": {
        "name": "Ames",
        "state": "Ames IA",
        "population": 66342,
        "housing": 27846,
        "land_area": 57904564.0,
        "water_area": 209840.0
    },
    "02103": {
        "name": "Amherst Town--Northampton--Easthampton Town",
        "state": "Amherst Town--Northampton--Easthampton Town MA",
        "population": 90570,
        "housing": 35432,
        "land_area": 141549252.0,
        "water_area": 1731252.0
    },
    "02143": {
        "name": "Amory",
        "state": "Amory MS",
        "population": 5977,
        "housing": 2952,
        "land_area": 14519988.0,
        "water_area": 27224.0
    },
    "02170": {
        "name": "Amsterdam",
        "state": "Amsterdam NY",
        "population": 21753,
        "housing": 10808,
        "land_area": 26043582.0,
        "water_area": 841185.0
    },
    "02197": {
        "name": "Anaconda-Deer Lodge County",
        "state": "Anaconda-Deer Lodge County MT",
        "population": 6697,
        "housing": 3828,
        "land_area": 6759740.0,
        "water_area": 0.0
    },
    "02224": {
        "name": "Anacortes",
        "state": "Anacortes WA",
        "population": 18529,
        "housing": 8883,
        "land_area": 25533104.0,
        "water_area": 908946.0
    },
    "02251": {
        "name": "Anadarko",
        "state": "Anadarko OK",
        "population": 5005,
        "housing": 2252,
        "land_area": 5450708.0,
        "water_area": 0.0
    },
    "02278": {
        "name": "Anamosa",
        "state": "Anamosa IA",
        "population": 5411,
        "housing": 2093,
        "land_area": 6854912.0,
        "water_area": 8863.0
    },
    "02305": {
        "name": "Anchorage",
        "state": "Anchorage AK",
        "population": 249252,
        "housing": 101938,
        "land_area": 237473557.0,
        "water_area": 2775799.0
    },
    "02332": {
        "name": "Anchorage Northeast",
        "state": "Anchorage Northeast AK",
        "population": 29561,
        "housing": 11251,
        "land_area": 45798297.0,
        "water_area": 475350.0
    },
    "02359": {
        "name": "Andalusia",
        "state": "Andalusia AL",
        "population": 6391,
        "housing": 3351,
        "land_area": 17775439.0,
        "water_area": 103830.0
    },
    "02386": {
        "name": "Anderson",
        "state": "Anderson IN",
        "population": 79517,
        "housing": 36893,
        "land_area": 135120308.0,
        "water_area": 395557.0
    },
    "02420": {
        "name": "Anderson--Clemson",
        "state": "Anderson--Clemson SC",
        "population": 118369,
        "housing": 50811,
        "land_area": 248484739.0,
        "water_area": 7321967.0
    },
    "02467": {
        "name": "Andrews",
        "state": "Andrews TX",
        "population": 15201,
        "housing": 5797,
        "land_area": 24644900.0,
        "water_area": 30529.0
    },
    "02500": {
        "name": "Angleton",
        "state": "Angleton TX",
        "population": 19866,
        "housing": 8221,
        "land_area": 28355057.0,
        "water_area": 48854.0
    },
    "02548": {
        "name": "Angola",
        "state": "Angola IN",
        "population": 12686,
        "housing": 6728,
        "land_area": 32187898.0,
        "water_area": 5901426.0
    },
    "02575": {
        "name": "Anna",
        "state": "Anna IL",
        "population": 6068,
        "housing": 2749,
        "land_area": 11470852.0,
        "water_area": 57886.0
    },
    "02602": {
        "name": "Ann Arbor",
        "state": "Ann Arbor MI",
        "population": 317689,
        "housing": 137325,
        "land_area": 371546429.0,
        "water_area": 13342445.0
    },
    "02629": {
        "name": "Anniston--Oxford",
        "state": "Anniston--Oxford AL",
        "population": 78302,
        "housing": 35959,
        "land_area": 204340445.0,
        "water_area": 1108735.0
    },
    "02656": {
        "name": "Antigo",
        "state": "Antigo WI",
        "population": 8071,
        "housing": 4130,
        "land_area": 12555575.0,
        "water_area": 120062.0
    },
    "02683": {
        "name": "Antioch",
        "state": "Antioch CA",
        "population": 326205,
        "housing": 104264,
        "land_area": 189968288.0,
        "water_area": 2037833.0
    },
    "02764": {
        "name": "Appleton",
        "state": "Appleton WI",
        "population": 230967,
        "housing": 98811,
        "land_area": 279136276.0,
        "water_area": 10660971.0
    },
    "02791": {
        "name": "Arab",
        "state": "Arab AL",
        "population": 7849,
        "housing": 3494,
        "land_area": 22520970.0,
        "water_area": 279456.0
    },
    "02818": {
        "name": "Aransas Pass--Port Aransas--Ingleside",
        "state": "Aransas Pass--Port Aransas--Ingleside TX",
        "population": 21868,
        "housing": 13912,
        "land_area": 63826698.0,
        "water_area": 572244.0
    },
    "02912": {
        "name": "Arcadia",
        "state": "Arcadia FL",
        "population": 16128,
        "housing": 7287,
        "land_area": 25909522.0,
        "water_area": 15770.0
    },
    "02930": {
        "name": "Arcata",
        "state": "Arcata CA",
        "population": 19714,
        "housing": 8796,
        "land_area": 20532275.0,
        "water_area": 118564.0
    },
    "03007": {
        "name": "Ardmore",
        "state": "Ardmore OK",
        "population": 21403,
        "housing": 10020,
        "land_area": 38898730.0,
        "water_area": 360022.0
    },
    "03034": {
        "name": "Arecibo",
        "state": "Arecibo PR",
        "population": 123724,
        "housing": 59095,
        "land_area": 195525702.0,
        "water_area": 248325.0
    },
    "03088": {
        "name": "Arizona City",
        "state": "Arizona City AZ",
        "population": 9640,
        "housing": 4466,
        "land_area": 11238099.0,
        "water_area": 198208.0
    },
    "03115": {
        "name": "Arkadelphia",
        "state": "Arkadelphia AR",
        "population": 10086,
        "housing": 4205,
        "land_area": 16058897.0,
        "water_area": 1130.0
    },
    "03142": {
        "name": "Arkansas City",
        "state": "Arkansas City KS",
        "population": 11878,
        "housing": 5372,
        "land_area": 17694779.0,
        "water_area": 0.0
    },
    "03158": {
        "name": "Arlington",
        "state": "Arlington TN",
        "population": 14230,
        "housing": 4634,
        "land_area": 19877761.0,
        "water_area": 87911.0
    },
    "03196": {
        "name": "Arroyo Grande--Grover Beach--Pismo Beach",
        "state": "Arroyo Grande--Grover Beach--Pismo Beach CA",
        "population": 50885,
        "housing": 25235,
        "land_area": 44223390.0,
        "water_area": 158952.0
    },
    "03223": {
        "name": "Artesia",
        "state": "Artesia NM",
        "population": 14149,
        "housing": 5937,
        "land_area": 17739520.0,
        "water_area": 16866.0
    },
    "03250": {
        "name": "Arvin",
        "state": "Arvin CA",
        "population": 19385,
        "housing": 4870,
        "land_area": 6073680.0,
        "water_area": 0.0
    },
    "03262": {
        "name": "Asbury Lake--Middleburg",
        "state": "Asbury Lake--Middleburg FL",
        "population": 23649,
        "housing": 8746,
        "land_area": 59576581.0,
        "water_area": 700279.0
    },
    "03277": {
        "name": "Ashburn",
        "state": "Ashburn GA",
        "population": 4738,
        "housing": 2086,
        "land_area": 10999818.0,
        "water_area": 108350.0
    },
    "03331": {
        "name": "Asheboro",
        "state": "Asheboro NC",
        "population": 37523,
        "housing": 16252,
        "land_area": 71019749.0,
        "water_area": 315965.0
    },
    "03358": {
        "name": "Asheville",
        "state": "Asheville NC",
        "population": 285776,
        "housing": 138374,
        "land_area": 643832055.0,
        "water_area": 4932387.0
    },
    "03385": {
        "name": "Ashland",
        "state": "Ashland OH",
        "population": 19206,
        "housing": 8954,
        "land_area": 25515681.0,
        "water_area": 92994.0
    },
    "03395": {
        "name": "Ashland",
        "state": "Ashland PA",
        "population": 4249,
        "housing": 2530,
        "land_area": 3674775.0,
        "water_area": 0.0
    },
    "03412": {
        "name": "Ashland",
        "state": "Ashland WI",
        "population": 7225,
        "housing": 3543,
        "land_area": 10936251.0,
        "water_area": 2466.0
    },
    "03466": {
        "name": "Ashtabula",
        "state": "Ashtabula OH",
        "population": 27421,
        "housing": 14439,
        "land_area": 56175650.0,
        "water_area": 467129.0
    },
    "03493": {
        "name": "Ashville",
        "state": "Ashville OH",
        "population": 6670,
        "housing": 2715,
        "land_area": 9860526.0,
        "water_area": 0.0
    },
    "03520": {
        "name": "Aspen",
        "state": "Aspen CO",
        "population": 7674,
        "housing": 6597,
        "land_area": 13498106.0,
        "water_area": 0.0
    },
    "03547": {
        "name": "Astoria",
        "state": "Astoria OR",
        "population": 15825,
        "housing": 7690,
        "land_area": 25041475.0,
        "water_area": 335804.0
    },
    "03601": {
        "name": "Atchison",
        "state": "Atchison KS",
        "population": 10907,
        "housing": 4385,
        "land_area": 13947856.0,
        "water_area": 115035.0
    },
    "03655": {
        "name": "Athens",
        "state": "Athens AL",
        "population": 23204,
        "housing": 10492,
        "land_area": 52039316.0,
        "water_area": 299071.0
    },
    "03682": {
        "name": "Athens",
        "state": "Athens OH",
        "population": 27355,
        "housing": 10036,
        "land_area": 24482022.0,
        "water_area": 373562.0
    },
    "03709": {
        "name": "Athens",
        "state": "Athens TN",
        "population": 15724,
        "housing": 7179,
        "land_area": 40074162.0,
        "water_area": 297.0
    },
    "03736": {
        "name": "Athens",
        "state": "Athens TX",
        "population": 12050,
        "housing": 4960,
        "land_area": 24138742.0,
        "water_area": 64685.0
    },
    "03763": {
        "name": "Athens-Clarke County",
        "state": "Athens-Clarke County GA",
        "population": 143213,
        "housing": 60979,
        "land_area": 237882105.0,
        "water_area": 2426867.0
    },
    "03790": {
        "name": "Athol",
        "state": "Athol MA",
        "population": 13557,
        "housing": 6243,
        "land_area": 27482042.0,
        "water_area": 1195269.0
    },
    "03817": {
        "name": "Atlanta",
        "state": "Atlanta GA",
        "population": 5100112,
        "housing": 2035642,
        "land_area": 6612376890.0,
        "water_area": 99716580.0
    },
    "03844": {
        "name": "Atlanta",
        "state": "Atlanta TX",
        "population": 5531,
        "housing": 2659,
        "land_area": 17636485.0,
        "water_area": 99001.0
    },
    "03871": {
        "name": "Atlantic",
        "state": "Atlantic IA",
        "population": 6608,
        "housing": 3309,
        "land_area": 11633452.0,
        "water_area": 113854.0
    },
    "03904": {
        "name": "Atlantic City--Ocean City--Villas",
        "state": "Atlantic City--Ocean City--Villas NJ",
        "population": 294921,
        "housing": 201613,
        "land_area": 421843599.0,
        "water_area": 10633955.0
    },
    "03925": {
        "name": "Atmore",
        "state": "Atmore AL",
        "population": 6390,
        "housing": 3151,
        "land_area": 13219601.0,
        "water_area": 52468.0
    },
    "03957": {
        "name": "Atoka",
        "state": "Atoka TN",
        "population": 13056,
        "housing": 4834,
        "land_area": 25705273.0,
        "water_area": 72575.0
    },
    "04021": {
        "name": "Aubrey",
        "state": "Aubrey TX",
        "population": 5116,
        "housing": 1963,
        "land_area": 7097052.0,
        "water_area": 11230.0
    },
    "04033": {
        "name": "Auburn",
        "state": "Auburn AL",
        "population": 100842,
        "housing": 44840,
        "land_area": 158393499.0,
        "water_area": 1472748.0
    },
    "04060": {
        "name": "Auburn",
        "state": "Auburn IN",
        "population": 20346,
        "housing": 8813,
        "land_area": 25597810.0,
        "water_area": 0.0
    },
    "04114": {
        "name": "Auburn",
        "state": "Auburn NY",
        "population": 31433,
        "housing": 15338,
        "land_area": 34030890.0,
        "water_area": 211524.0
    },
    "04130": {
        "name": "Auburn",
        "state": "Auburn CA",
        "population": 31371,
        "housing": 13842,
        "land_area": 44648435.0,
        "water_area": 22143.0
    },
    "04146": {
        "name": "Au Gres",
        "state": "Au Gres MI",
        "population": 1869,
        "housing": 2201,
        "land_area": 10264539.0,
        "water_area": 168489.0
    },
    "04168": {
        "name": "Augusta",
        "state": "Augusta KS",
        "population": 9231,
        "housing": 4004,
        "land_area": 9624503.0,
        "water_area": 695128.0
    },
    "04195": {
        "name": "Augusta",
        "state": "Augusta ME",
        "population": 24005,
        "housing": 12627,
        "land_area": 56766242.0,
        "water_area": 1871378.0
    },
    "04222": {
        "name": "Augusta-Richmond County",
        "state": "Augusta-Richmond County GA--SC",
        "population": 431480,
        "housing": 184589,
        "land_area": 707822587.0,
        "water_area": 5760420.0
    },
    "04276": {
        "name": "Aurora",
        "state": "Aurora MO",
        "population": 7466,
        "housing": 3488,
        "land_area": 13670605.0,
        "water_area": 40800.0
    },
    "04357": {
        "name": "Austin",
        "state": "Austin MN",
        "population": 25479,
        "housing": 10764,
        "land_area": 26021766.0,
        "water_area": 254097.0
    },
    "04384": {
        "name": "Austin",
        "state": "Austin TX",
        "population": 1809888,
        "housing": 765527,
        "land_area": 1604809936.0,
        "water_area": 12678276.0
    },
    "04438": {
        "name": "Avalon",
        "state": "Avalon CA",
        "population": 3362,
        "housing": 2165,
        "land_area": 3080708.0,
        "water_area": 0.0
    },
    "04465": {
        "name": "Avenal",
        "state": "Avenal CA",
        "population": 13304,
        "housing": 2480,
        "land_area": 9397889.0,
        "water_area": 0.0
    },
    "04587": {
        "name": "Aztec",
        "state": "Aztec NM",
        "population": 7301,
        "housing": 3446,
        "land_area": 17566785.0,
        "water_area": 264792.0
    },
    "04627": {
        "name": "Bainbridge",
        "state": "Bainbridge GA",
        "population": 13857,
        "housing": 6070,
        "land_area": 27649585.0,
        "water_area": 614197.0
    },
    "04654": {
        "name": "Baker City",
        "state": "Baker City OR",
        "population": 9768,
        "housing": 4509,
        "land_area": 11601573.0,
        "water_area": 0.0
    },
    "04681": {
        "name": "Bakersfield",
        "state": "Bakersfield CA",
        "population": 570235,
        "housing": 186629,
        "land_area": 342190994.0,
        "water_area": 2545322.0
    },
    "04843": {
        "name": "Baltimore",
        "state": "Baltimore MD",
        "population": 2212038,
        "housing": 944161,
        "land_area": 1696273764.0,
        "water_area": 55784905.0
    },
    "04924": {
        "name": "Bandon",
        "state": "Bandon OR",
        "population": 4104,
        "housing": 2514,
        "land_area": 12605488.0,
        "water_area": 41944.0
    },
    "04951": {
        "name": "Bangor",
        "state": "Bangor ME",
        "population": 61539,
        "housing": 28723,
        "land_area": 132390040.0,
        "water_area": 4822906.0
    },
    "05005": {
        "name": "Baraboo",
        "state": "Baraboo WI",
        "population": 14201,
        "housing": 6569,
        "land_area": 17518061.0,
        "water_area": 214011.0
    },
    "05032": {
        "name": "Barbourville",
        "state": "Barbourville KY",
        "population": 5998,
        "housing": 2744,
        "land_area": 13718477.0,
        "water_area": 73476.0
    },
    "05040": {
        "name": "Barceloneta--Florida--Bajadero",
        "state": "Barceloneta--Florida--Bajadero PR",
        "population": 65070,
        "housing": 29534,
        "land_area": 108004676.0,
        "water_area": 73429.0
    },
    "05059": {
        "name": "Bardstown",
        "state": "Bardstown KY",
        "population": 17682,
        "housing": 7738,
        "land_area": 34627012.0,
        "water_area": 272635.0
    },
    "05113": {
        "name": "Barnesville",
        "state": "Barnesville GA",
        "population": 6825,
        "housing": 2796,
        "land_area": 15058636.0,
        "water_area": 109101.0
    },
    "05167": {
        "name": "Barnstable Town",
        "state": "Barnstable Town MA",
        "population": 303269,
        "housing": 195668,
        "land_area": 883909133.0,
        "water_area": 90287592.0
    },
    "05221": {
        "name": "Barre--Montpelier",
        "state": "Barre--Montpelier VT",
        "population": 20014,
        "housing": 10096,
        "land_area": 37270872.0,
        "water_area": 413559.0
    },
    "05302": {
        "name": "Barstow",
        "state": "Barstow CA",
        "population": 30522,
        "housing": 11453,
        "land_area": 32068959.0,
        "water_area": 0.0
    },
    "05329": {
        "name": "Bartlesville",
        "state": "Bartlesville OK",
        "population": 39479,
        "housing": 18237,
        "land_area": 51181227.0,
        "water_area": 32200.0
    },
    "05383": {
        "name": "Bartow",
        "state": "Bartow FL",
        "population": 16948,
        "housing": 7166,
        "land_area": 20496504.0,
        "water_area": 669388.0
    },
    "05410": {
        "name": "Basalt",
        "state": "Basalt CO",
        "population": 8127,
        "housing": 3458,
        "land_area": 9433311.0,
        "water_area": 68797.0
    },
    "05437": {
        "name": "Bastrop",
        "state": "Bastrop LA",
        "population": 12604,
        "housing": 5701,
        "land_area": 26749993.0,
        "water_area": 105190.0
    },
    "05464": {
        "name": "Bastrop",
        "state": "Bastrop TX",
        "population": 19384,
        "housing": 7798,
        "land_area": 46957839.0,
        "water_area": 620588.0
    },
    "05491": {
        "name": "Batavia",
        "state": "Batavia NY",
        "population": 17472,
        "housing": 8308,
        "land_area": 20898199.0,
        "water_area": 118102.0
    },
    "05518": {
        "name": "Batesburg-Leesville",
        "state": "Batesburg-Leesville SC",
        "population": 4989,
        "housing": 2342,
        "land_area": 14503633.0,
        "water_area": 163954.0
    },
    "05545": {
        "name": "Batesville",
        "state": "Batesville AR",
        "population": 10913,
        "housing": 4724,
        "land_area": 20312531.0,
        "water_area": 22032.0
    },
    "05572": {
        "name": "Batesville",
        "state": "Batesville IN",
        "population": 7941,
        "housing": 3285,
        "land_area": 14178627.0,
        "water_area": 175293.0
    },
    "05599": {
        "name": "Batesville",
        "state": "Batesville MS",
        "population": 6273,
        "housing": 2643,
        "land_area": 16264239.0,
        "water_area": 0.0
    },
    "05653": {
        "name": "Bath",
        "state": "Bath NY",
        "population": 6335,
        "housing": 3264,
        "land_area": 7522968.0,
        "water_area": 245799.0
    },
    "05680": {
        "name": "Baton Rouge",
        "state": "Baton Rouge LA",
        "population": 631326,
        "housing": 273965,
        "land_area": 1026517587.0,
        "water_area": 11310643.0
    },
    "05707": {
        "name": "Battle Creek",
        "state": "Battle Creek MI",
        "population": 75513,
        "housing": 34049,
        "land_area": 122588672.0,
        "water_area": 4148654.0
    },
    "05734": {
        "name": "Battlement Mesa",
        "state": "Battlement Mesa CO",
        "population": 6311,
        "housing": 2571,
        "land_area": 7936842.0,
        "water_area": 56226.0
    },
    "05788": {
        "name": "Baxley",
        "state": "Baxley GA",
        "population": 5354,
        "housing": 2482,
        "land_area": 16211372.0,
        "water_area": 13308.0
    },
    "05842": {
        "name": "Bayard",
        "state": "Bayard NM",
        "population": 4975,
        "housing": 2485,
        "land_area": 8065433.0,
        "water_area": 64219.0
    },
    "05869": {
        "name": "Bay City",
        "state": "Bay City MI",
        "population": 68472,
        "housing": 33037,
        "land_area": 103422422.0,
        "water_area": 2949655.0
    },
    "05896": {
        "name": "Bay City",
        "state": "Bay City TX",
        "population": 19311,
        "housing": 8683,
        "land_area": 26972308.0,
        "water_area": 41853.0
    },
    "05923": {
        "name": "Bay Minette",
        "state": "Bay Minette AL",
        "population": 7685,
        "housing": 3118,
        "land_area": 18014152.0,
        "water_area": 143683.0
    },
    "05935": {
        "name": "Bayside Gardens--Manzanita",
        "state": "Bayside Gardens--Manzanita OR",
        "population": 2849,
        "housing": 3052,
        "land_area": 7720067.0,
        "water_area": 3042.0
    },
    "05958": {
        "name": "Bealeton",
        "state": "Bealeton VA",
        "population": 6608,
        "housing": 2257,
        "land_area": 11316315.0,
        "water_area": 20856.0
    },
    "05977": {
        "name": "Beardstown",
        "state": "Beardstown IL",
        "population": 6262,
        "housing": 2505,
        "land_area": 5541099.0,
        "water_area": 96717.0
    },
    "06004": {
        "name": "Beatrice",
        "state": "Beatrice NE",
        "population": 12142,
        "housing": 6011,
        "land_area": 22510017.0,
        "water_area": 168444.0
    },
    "06031": {
        "name": "Beaufort--Port Royal",
        "state": "Beaufort--Port Royal SC",
        "population": 52515,
        "housing": 21456,
        "land_area": 113338543.0,
        "water_area": 1418819.0
    },
    "06058": {
        "name": "Beaumont",
        "state": "Beaumont TX",
        "population": 146649,
        "housing": 65409,
        "land_area": 248896009.0,
        "water_area": 3817936.0
    },
    "06085": {
        "name": "Beaver Dam",
        "state": "Beaver Dam KY",
        "population": 5658,
        "housing": 2566,
        "land_area": 8811959.0,
        "water_area": 24352.0
    },
    "06112": {
        "name": "Beaver Dam",
        "state": "Beaver Dam WI",
        "population": 18824,
        "housing": 8633,
        "land_area": 24305017.0,
        "water_area": 71584.0
    },
    "06139": {
        "name": "Beckley",
        "state": "Beckley WV",
        "population": 57468,
        "housing": 27981,
        "land_area": 138915493.0,
        "water_area": 431443.0
    },
    "06166": {
        "name": "Bedford",
        "state": "Bedford IN",
        "population": 14432,
        "housing": 6932,
        "land_area": 22034934.0,
        "water_area": 2314.0
    },
    "06193": {
        "name": "Bedford",
        "state": "Bedford PA",
        "population": 4392,
        "housing": 2426,
        "land_area": 9941199.0,
        "water_area": 270639.0
    },
    "06220": {
        "name": "Bedford",
        "state": "Bedford VA",
        "population": 7541,
        "housing": 3587,
        "land_area": 22211678.0,
        "water_area": 30240.0
    },
    "06247": {
        "name": "Beebe",
        "state": "Beebe AR",
        "population": 7216,
        "housing": 2969,
        "land_area": 11586697.0,
        "water_area": 0.0
    },
    "06274": {
        "name": "Beeville",
        "state": "Beeville TX",
        "population": 14230,
        "housing": 6110,
        "land_area": 16350545.0,
        "water_area": 0.0
    },
    "06290": {
        "name": "Bel Air--Aberdeen",
        "state": "Bel Air--Aberdeen MD",
        "population": 214647,
        "housing": 86017,
        "land_area": 277642000.0,
        "water_area": 3614984.0
    },
    "06301": {
        "name": "Belding",
        "state": "Belding MI",
        "population": 5611,
        "housing": 2285,
        "land_area": 7530588.0,
        "water_area": 154362.0
    },
    "06335": {
        "name": "Belfair",
        "state": "Belfair WA",
        "population": 5141,
        "housing": 3022,
        "land_area": 13854178.0,
        "water_area": 1964.0
    },
    "06355": {
        "name": "Belfast",
        "state": "Belfast ME",
        "population": 3754,
        "housing": 2484,
        "land_area": 9120701.0,
        "water_area": 45394.0
    },
    "06382": {
        "name": "Belgrade",
        "state": "Belgrade MT",
        "population": 18534,
        "housing": 7215,
        "land_area": 34572870.0,
        "water_area": 107241.0
    },
    "06436": {
        "name": "Bellefontaine",
        "state": "Bellefontaine OH",
        "population": 14024,
        "housing": 6358,
        "land_area": 16085825.0,
        "water_area": 0.0
    },
    "06463": {
        "name": "Bellefonte",
        "state": "Bellefonte PA",
        "population": 15588,
        "housing": 6424,
        "land_area": 20305137.0,
        "water_area": 0.0
    },
    "06490": {
        "name": "Belle Fourche",
        "state": "Belle Fourche SD",
        "population": 5089,
        "housing": 2375,
        "land_area": 7406382.0,
        "water_area": 0.0
    },
    "06517": {
        "name": "Belle Glade",
        "state": "Belle Glade FL",
        "population": 23009,
        "housing": 7996,
        "land_area": 18672943.0,
        "water_area": 0.0
    },
    "06571": {
        "name": "Belle Plaine",
        "state": "Belle Plaine MN",
        "population": 7061,
        "housing": 2629,
        "land_area": 9192802.0,
        "water_area": 0.0
    },
    "06625": {
        "name": "Bellevue",
        "state": "Bellevue OH",
        "population": 8400,
        "housing": 3759,
        "land_area": 11577760.0,
        "water_area": 181429.0
    },
    "06652": {
        "name": "Bellingham",
        "state": "Bellingham WA",
        "population": 128979,
        "housing": 56420,
        "land_area": 129840795.0,
        "water_area": 7550895.0
    },
    "06679": {
        "name": "Bellows Falls",
        "state": "Bellows Falls VT--NH",
        "population": 3978,
        "housing": 2072,
        "land_area": 6270081.0,
        "water_area": 119932.0
    },
    "06760": {
        "name": "Beloit",
        "state": "Beloit WI--IL",
        "population": 63073,
        "housing": 26188,
        "land_area": 82713169.0,
        "water_area": 2164278.0
    },
    "06770": {
        "name": "Belterra",
        "state": "Belterra TX",
        "population": 8075,
        "housing": 2807,
        "land_area": 8700398.0,
        "water_area": 0.0
    },
    "06779": {
        "name": "Belton",
        "state": "Belton SC",
        "population": 5301,
        "housing": 2518,
        "land_area": 11816372.0,
        "water_area": 28235.0
    },
    "06841": {
        "name": "Bemidji",
        "state": "Bemidji MN",
        "population": 14849,
        "housing": 6747,
        "land_area": 37447425.0,
        "water_area": 2705902.0
    },
    "06868": {
        "name": "Bend",
        "state": "Bend OR",
        "population": 106988,
        "housing": 47859,
        "land_area": 109834087.0,
        "water_area": 786737.0
    },
    "06895": {
        "name": "Bennettsville",
        "state": "Bennettsville SC",
        "population": 9075,
        "housing": 4618,
        "land_area": 19347948.0,
        "water_area": 1278568.0
    },
    "06922": {
        "name": "Bennington",
        "state": "Bennington VT",
        "population": 13759,
        "housing": 6140,
        "land_area": 30128576.0,
        "water_area": 301080.0
    },
    "06949": {
        "name": "Benson",
        "state": "Benson AZ",
        "population": 3830,
        "housing": 2342,
        "land_area": 7028637.0,
        "water_area": 4137.0
    },
    "07030": {
        "name": "Benton",
        "state": "Benton IL",
        "population": 7491,
        "housing": 3737,
        "land_area": 12902238.0,
        "water_area": 51328.0
    },
    "07057": {
        "name": "Benton",
        "state": "Benton KY",
        "population": 4691,
        "housing": 2114,
        "land_area": 10688616.0,
        "water_area": 141709.0
    },
    "07084": {
        "name": "Benton",
        "state": "Benton LA",
        "population": 5591,
        "housing": 2150,
        "land_area": 11591813.0,
        "water_area": 17419.0
    },
    "07138": {
        "name": "Benton Harbor--Lincoln--St. Joseph",
        "state": "Benton Harbor--Lincoln--St. Joseph MI",
        "population": 61888,
        "housing": 30730,
        "land_area": 136046516.0,
        "water_area": 3221569.0
    },
    "07165": {
        "name": "Berea",
        "state": "Berea KY",
        "population": 16158,
        "housing": 6631,
        "land_area": 29728875.0,
        "water_area": 133829.0
    },
    "07192": {
        "name": "Berlin",
        "state": "Berlin NH",
        "population": 9658,
        "housing": 5339,
        "land_area": 12806030.0,
        "water_area": 451316.0
    },
    "07219": {
        "name": "Berlin",
        "state": "Berlin WI",
        "population": 5289,
        "housing": 2495,
        "land_area": 8947266.0,
        "water_area": 33714.0
    },
    "07300": {
        "name": "Berryville",
        "state": "Berryville AR",
        "population": 5057,
        "housing": 2008,
        "land_area": 10832198.0,
        "water_area": 0.0
    },
    "07381": {
        "name": "Bethel",
        "state": "Bethel AK",
        "population": 5097,
        "housing": 1756,
        "land_area": 38849240.0,
        "water_area": 651455.0
    },
    "07472": {
        "name": "Beverly Hills--Homosassa Springs--Pine Ridge",
        "state": "Beverly Hills--Homosassa Springs--Pine Ridge FL",
        "population": 96729,
        "housing": 50309,
        "land_area": 307760295.0,
        "water_area": 9556843.0
    },
    "07508": {
        "name": "Big Bear",
        "state": "Big Bear CA",
        "population": 16498,
        "housing": 20795,
        "land_area": 41255479.0,
        "water_area": 552966.0
    },
    "07530": {
        "name": "Big Lake",
        "state": "Big Lake MN",
        "population": 11868,
        "housing": 4293,
        "land_area": 14048035.0,
        "water_area": 2127152.0
    },
    "07597": {
        "name": "Big Pine Key",
        "state": "Big Pine Key FL",
        "population": 8441,
        "housing": 6099,
        "land_area": 21918342.0,
        "water_area": 658787.0
    },
    "07624": {
        "name": "Big Rapids",
        "state": "Big Rapids MI",
        "population": 10136,
        "housing": 5122,
        "land_area": 19019305.0,
        "water_area": 151973.0
    },
    "07651": {
        "name": "Big Spring",
        "state": "Big Spring TX",
        "population": 28955,
        "housing": 11433,
        "land_area": 53464090.0,
        "water_area": 606612.0
    },
    "07678": {
        "name": "Big Stone Gap",
        "state": "Big Stone Gap VA",
        "population": 6915,
        "housing": 2830,
        "land_area": 17045424.0,
        "water_area": 230875.0
    },
    "07705": {
        "name": "Billings",
        "state": "Billings MT",
        "population": 128787,
        "housing": 57343,
        "land_area": 141671924.0,
        "water_area": 353601.0
    },
    "07732": {
        "name": "Binghamton",
        "state": "Binghamton NY",
        "population": 155942,
        "housing": 72333,
        "land_area": 180597279.0,
        "water_area": 7940048.0
    },
    "07759": {
        "name": "Birch Bay",
        "state": "Birch Bay WA",
        "population": 15833,
        "housing": 8619,
        "land_area": 33814662.0,
        "water_area": 393048.0
    },
    "07770": {
        "name": "Birdsboro--Amity",
        "state": "Birdsboro--Amity PA",
        "population": 16999,
        "housing": 6533,
        "land_area": 22176287.0,
        "water_area": 29128.0
    },
    "07786": {
        "name": "Birmingham",
        "state": "Birmingham AL",
        "population": 774956,
        "housing": 346732,
        "land_area": 1319007575.0,
        "water_area": 9300252.0
    },
    "07813": {
        "name": "Bisbee",
        "state": "Bisbee AZ",
        "population": 4637,
        "housing": 3033,
        "land_area": 8165838.0,
        "water_area": 0.0
    },
    "07840": {
        "name": "Bishop",
        "state": "Bishop CA",
        "population": 11013,
        "housing": 5104,
        "land_area": 14219124.0,
        "water_area": 17292.0
    },
    "07921": {
        "name": "Bismarck",
        "state": "Bismarck ND",
        "population": 98198,
        "housing": 45189,
        "land_area": 108402649.0,
        "water_area": 1047286.0
    },
    "07948": {
        "name": "Blackfoot",
        "state": "Blackfoot ID",
        "population": 14231,
        "housing": 5387,
        "land_area": 19458927.0,
        "water_area": 459187.0
    },
    "07975": {
        "name": "Black River Falls",
        "state": "Black River Falls WI",
        "population": 4415,
        "housing": 2155,
        "land_area": 9592872.0,
        "water_area": 8248.0
    },
    "08002": {
        "name": "Blacksburg--Christiansburg",
        "state": "Blacksburg--Christiansburg VA",
        "population": 72400,
        "housing": 29193,
        "land_area": 88004948.0,
        "water_area": 135579.0
    },
    "08083": {
        "name": "Blackwell",
        "state": "Blackwell OK",
        "population": 6017,
        "housing": 3186,
        "land_area": 9955610.0,
        "water_area": 17842.0
    },
    "08137": {
        "name": "Blair",
        "state": "Blair NE",
        "population": 8001,
        "housing": 3531,
        "land_area": 21189662.0,
        "water_area": 0.0
    },
    "08164": {
        "name": "Blairsville",
        "state": "Blairsville PA",
        "population": 6156,
        "housing": 3377,
        "land_area": 13521907.0,
        "water_area": 4980.0
    },
    "08353": {
        "name": "Bloomfield",
        "state": "Bloomfield NM",
        "population": 7841,
        "housing": 3151,
        "land_area": 16180989.0,
        "water_area": 87446.0
    },
    "08380": {
        "name": "Bloomington",
        "state": "Bloomington IN",
        "population": 110103,
        "housing": 50119,
        "land_area": 111461459.0,
        "water_area": 13178.0
    },
    "08407": {
        "name": "Bloomington--Normal",
        "state": "Bloomington--Normal IL",
        "population": 134100,
        "housing": 59416,
        "land_area": 130436714.0,
        "water_area": 571357.0
    },
    "08434": {
        "name": "Bloomsburg--Berwick",
        "state": "Bloomsburg--Berwick PA",
        "population": 39212,
        "housing": 17812,
        "land_area": 53729115.0,
        "water_area": 1385178.0
    },
    "08470": {
        "name": "Blowing Rock",
        "state": "Blowing Rock NC",
        "population": 1412,
        "housing": 2085,
        "land_area": 8284152.0,
        "water_area": 113630.0
    },
    "08515": {
        "name": "Bluefield",
        "state": "Bluefield WV--VA",
        "population": 40750,
        "housing": 20450,
        "land_area": 100230880.0,
        "water_area": 360007.0
    },
    "08542": {
        "name": "Bluffton",
        "state": "Bluffton IN",
        "population": 10346,
        "housing": 4758,
        "land_area": 16844842.0,
        "water_area": 198848.0
    },
    "08601": {
        "name": "Bluffton East--Hilton Head Island",
        "state": "Bluffton East--Hilton Head Island SC",
        "population": 71824,
        "housing": 43742,
        "land_area": 163794642.0,
        "water_area": 4715792.0
    },
    "08607": {
        "name": "Bluffton West",
        "state": "Bluffton West SC",
        "population": 31096,
        "housing": 15069,
        "land_area": 60360788.0,
        "water_area": 87316.0
    },
    "08623": {
        "name": "Blythe",
        "state": "Blythe CA--AZ",
        "population": 11780,
        "housing": 5054,
        "land_area": 16059355.0,
        "water_area": 110393.0
    },
    "08650": {
        "name": "Blytheville",
        "state": "Blytheville AR",
        "population": 15873,
        "housing": 7845,
        "land_area": 37636444.0,
        "water_area": 0.0
    },
    "08704": {
        "name": "Boerne",
        "state": "Boerne TX",
        "population": 18320,
        "housing": 7410,
        "land_area": 24995790.0,
        "water_area": 0.0
    },
    "08731": {
        "name": "Bogalusa",
        "state": "Bogalusa LA",
        "population": 11019,
        "housing": 5759,
        "land_area": 28238846.0,
        "water_area": 77121.0
    },
    "08785": {
        "name": "Boise City",
        "state": "Boise City ID",
        "population": 433180,
        "housing": 177221,
        "land_area": 360604917.0,
        "water_area": 5022231.0
    },
    "08812": {
        "name": "Bolivar",
        "state": "Bolivar MO",
        "population": 10324,
        "housing": 4442,
        "land_area": 15321678.0,
        "water_area": 21991.0
    },
    "08866": {
        "name": "Bolivar",
        "state": "Bolivar TN",
        "population": 5281,
        "housing": 2437,
        "land_area": 12278310.0,
        "water_area": 0.0
    },
    "08920": {
        "name": "Bonham",
        "state": "Bonham TX",
        "population": 7799,
        "housing": 3341,
        "land_area": 13023790.0,
        "water_area": 0.0
    },
    "08974": {
        "name": "Bonita Springs--Estero",
        "state": "Bonita Springs--Estero FL",
        "population": 425675,
        "housing": 280947,
        "land_area": 629261315.0,
        "water_area": 54438285.0
    },
    "09005": {
        "name": "Bonne Terre",
        "state": "Bonne Terre MO",
        "population": 6696,
        "housing": 1946,
        "land_area": 5703065.0,
        "water_area": 53277.0
    },
    "09028": {
        "name": "Boone",
        "state": "Boone IA",
        "population": 12357,
        "housing": 5905,
        "land_area": 19706645.0,
        "water_area": 3221.0
    },
    "09055": {
        "name": "Boone",
        "state": "Boone NC",
        "population": 26306,
        "housing": 10905,
        "land_area": 47104231.0,
        "water_area": 176287.0
    },
    "09109": {
        "name": "Booneville",
        "state": "Booneville MS",
        "population": 6438,
        "housing": 2626,
        "land_area": 16497866.0,
        "water_area": 27004.0
    },
    "09163": {
        "name": "Boonville",
        "state": "Boonville IN",
        "population": 6507,
        "housing": 2961,
        "land_area": 7326458.0,
        "water_area": 34048.0
    },
    "09190": {
        "name": "Boonville",
        "state": "Boonville MO",
        "population": 8034,
        "housing": 3364,
        "land_area": 15083778.0,
        "water_area": 58042.0
    },
    "09200": {
        "name": "Boothbay Harbor",
        "state": "Boothbay Harbor ME",
        "population": 3067,
        "housing": 3797,
        "land_area": 29076101.0,
        "water_area": 10829901.0
    },
    "09217": {
        "name": "Borger",
        "state": "Borger TX",
        "population": 12848,
        "housing": 6288,
        "land_area": 24389656.0,
        "water_area": 51733.0
    },
    "09271": {
        "name": "Boston",
        "state": "Boston MA--NH",
        "population": 4382009,
        "housing": 1792967,
        "land_area": 4288728505.0,
        "water_area": 185677268.0
    },
    "09298": {
        "name": "Boulder",
        "state": "Boulder CO",
        "population": 120828,
        "housing": 52204,
        "land_area": 65911869.0,
        "water_area": 655779.0
    },
    "09325": {
        "name": "Boulder City",
        "state": "Boulder City NV",
        "population": 14181,
        "housing": 7164,
        "land_area": 14826356.0,
        "water_area": 0.0
    },
    "09352": {
        "name": "Bowie",
        "state": "Bowie TX",
        "population": 5419,
        "housing": 2490,
        "land_area": 11176169.0,
        "water_area": 46255.0
    },
    "09379": {
        "name": "Bowling Green",
        "state": "Bowling Green KY",
        "population": 97814,
        "housing": 41874,
        "land_area": 135135616.0,
        "water_area": 760052.0
    },
    "09433": {
        "name": "Bowling Green",
        "state": "Bowling Green OH",
        "population": 30989,
        "housing": 12956,
        "land_area": 29366514.0,
        "water_area": 165541.0
    },
    "09460": {
        "name": "Box Elder",
        "state": "Box Elder SD",
        "population": 11386,
        "housing": 4162,
        "land_area": 15755001.0,
        "water_area": 0.0
    },
    "09487": {
        "name": "Boyne City",
        "state": "Boyne City MI",
        "population": 3990,
        "housing": 2708,
        "land_area": 13768973.0,
        "water_area": 172067.0
    },
    "09514": {
        "name": "Bozeman",
        "state": "Bozeman MT",
        "population": 59080,
        "housing": 26060,
        "land_area": 55406356.0,
        "water_area": 137703.0
    },
    "09536": {
        "name": "Bradenton--Sarasota--Venice",
        "state": "Bradenton--Sarasota--Venice FL",
        "population": 779075,
        "housing": 447842,
        "land_area": 1047084870.0,
        "water_area": 102433799.0
    },
    "09541": {
        "name": "Bradford",
        "state": "Bradford PA--NY",
        "population": 11182,
        "housing": 4980,
        "land_area": 14874372.0,
        "water_area": 289937.0
    },
    "09568": {
        "name": "Brady",
        "state": "Brady TX",
        "population": 4887,
        "housing": 2528,
        "land_area": 7577027.0,
        "water_area": 19525.0
    },
    "09622": {
        "name": "Brainerd",
        "state": "Brainerd MN",
        "population": 20687,
        "housing": 9316,
        "land_area": 41800334.0,
        "water_area": 2606175.0
    },
    "09649": {
        "name": "Brandon",
        "state": "Brandon SD",
        "population": 10959,
        "housing": 4021,
        "land_area": 12126940.0,
        "water_area": 6506.0
    },
    "09676": {
        "name": "Branson",
        "state": "Branson MO",
        "population": 28640,
        "housing": 16198,
        "land_area": 90219202.0,
        "water_area": 1146904.0
    },
    "09703": {
        "name": "Brattleboro",
        "state": "Brattleboro VT",
        "population": 10285,
        "housing": 5500,
        "land_area": 14831844.0,
        "water_area": 101337.0
    },
    "09730": {
        "name": "Brawley",
        "state": "Brawley CA",
        "population": 26270,
        "housing": 8559,
        "land_area": 12336787.0,
        "water_area": 0.0
    },
    "09757": {
        "name": "Brazil",
        "state": "Brazil IN",
        "population": 10587,
        "housing": 4722,
        "land_area": 17102535.0,
        "water_area": 49727.0
    },
    "09801": {
        "name": "Breaux Bridge",
        "state": "Breaux Bridge LA",
        "population": 17542,
        "housing": 7559,
        "land_area": 55075902.0,
        "water_area": 797786.0
    },
    "09811": {
        "name": "Breckenridge",
        "state": "Breckenridge CO",
        "population": 8725,
        "housing": 10276,
        "land_area": 28879151.0,
        "water_area": 0.0
    },
    "09838": {
        "name": "Breckenridge",
        "state": "Breckenridge TX",
        "population": 5455,
        "housing": 2639,
        "land_area": 12554020.0,
        "water_area": 9818.0
    },
    "09865": {
        "name": "Breese",
        "state": "Breese IL",
        "population": 4637,
        "housing": 2036,
        "land_area": 6068055.0,
        "water_area": 0.0
    },
    "09892": {
        "name": "Bremen",
        "state": "Bremen GA",
        "population": 7327,
        "housing": 2944,
        "land_area": 19104846.0,
        "water_area": 20518.0
    },
    "09946": {
        "name": "Bremerton",
        "state": "Bremerton WA",
        "population": 224449,
        "housing": 91973,
        "land_area": 380494577.0,
        "water_area": 82515494.0
    },
    "09973": {
        "name": "Brenham",
        "state": "Brenham TX",
        "population": 17395,
        "housing": 7340,
        "land_area": 26583191.0,
        "water_area": 79918.0
    },
    "10027": {
        "name": "Brevard",
        "state": "Brevard NC",
        "population": 13059,
        "housing": 6580,
        "land_area": 35797279.0,
        "water_area": 9489.0
    },
    "10081": {
        "name": "Brewton",
        "state": "Brewton AL",
        "population": 6371,
        "housing": 3060,
        "land_area": 16207531.0,
        "water_area": 52981.0
    },
    "10162": {
        "name": "Bridgeport--Stamford",
        "state": "Bridgeport--Stamford CT--NY",
        "population": 916408,
        "housing": 367076,
        "land_area": 1028982132.0,
        "water_area": 28182017.0
    },
    "10189": {
        "name": "Bridgeton",
        "state": "Bridgeton NJ",
        "population": 35666,
        "housing": 10832,
        "land_area": 31504765.0,
        "water_area": 480612.0
    },
    "10243": {
        "name": "Brigham City",
        "state": "Brigham City UT",
        "population": 25827,
        "housing": 8992,
        "land_area": 26251248.0,
        "water_area": 6306.0
    },
    "10351": {
        "name": "Bristol",
        "state": "Bristol TN--VA",
        "population": 70638,
        "housing": 34040,
        "land_area": 177897747.0,
        "water_area": 460219.0
    },
    "10405": {
        "name": "Broadway--Timberville",
        "state": "Broadway--Timberville VA",
        "population": 7188,
        "housing": 2960,
        "land_area": 9320100.0,
        "water_area": 73819.0
    },
    "10418": {
        "name": "Brockport",
        "state": "Brockport NY",
        "population": 13079,
        "housing": 5554,
        "land_area": 16638508.0,
        "water_area": 138614.0
    },
    "10567": {
        "name": "Brookfield",
        "state": "Brookfield MO",
        "population": 3869,
        "housing": 2034,
        "land_area": 8320578.0,
        "water_area": 23046.0
    },
    "10594": {
        "name": "Brookhaven",
        "state": "Brookhaven MS",
        "population": 10152,
        "housing": 4815,
        "land_area": 25989578.0,
        "water_area": 59304.0
    },
    "10621": {
        "name": "Brookings",
        "state": "Brookings OR",
        "population": 11294,
        "housing": 5996,
        "land_area": 18517726.0,
        "water_area": 329587.0
    },
    "10648": {
        "name": "Brookings",
        "state": "Brookings SD",
        "population": 23674,
        "housing": 10132,
        "land_area": 28744518.0,
        "water_area": 0.0
    },
    "10729": {
        "name": "Brooksville",
        "state": "Brooksville FL",
        "population": 12128,
        "housing": 6436,
        "land_area": 21627985.0,
        "water_area": 255566.0
    },
    "10783": {
        "name": "Brookville",
        "state": "Brookville OH",
        "population": 6372,
        "housing": 2960,
        "land_area": 10555766.0,
        "water_area": 0.0
    },
    "10810": {
        "name": "Brookville",
        "state": "Brookville PA",
        "population": 4644,
        "housing": 2228,
        "land_area": 9426440.0,
        "water_area": 211981.0
    },
    "10837": {
        "name": "Brownfield",
        "state": "Brownfield TX",
        "population": 8264,
        "housing": 3524,
        "land_area": 11284096.0,
        "water_area": 31039.0
    },
    "10891": {
        "name": "Browns Mills",
        "state": "Browns Mills NJ",
        "population": 27234,
        "housing": 9507,
        "land_area": 44879147.0,
        "water_area": 1472716.0
    },
    "10945": {
        "name": "Brownsville",
        "state": "Brownsville TN",
        "population": 9621,
        "housing": 4472,
        "land_area": 19132118.0,
        "water_area": 0.0
    },
    "10972": {
        "name": "Brownsville",
        "state": "Brownsville TX",
        "population": 216444,
        "housing": 73165,
        "land_area": 160844426.0,
        "water_area": 6262482.0
    },
    "10999": {
        "name": "Brownwood",
        "state": "Brownwood TX",
        "population": 21562,
        "housing": 9768,
        "land_area": 35064966.0,
        "water_area": 0.0
    },
    "11026": {
        "name": "Brunswick--St. Simons",
        "state": "Brunswick--St. Simons GA",
        "population": 68750,
        "housing": 34174,
        "land_area": 152826893.0,
        "water_area": 3458577.0
    },
    "11040": {
        "name": "Brunswick",
        "state": "Brunswick ME",
        "population": 31361,
        "housing": 15015,
        "land_area": 69300445.0,
        "water_area": 4009090.0
    },
    "11053": {
        "name": "Brunswick",
        "state": "Brunswick MD",
        "population": 8269,
        "housing": 3231,
        "land_area": 9426826.0,
        "water_area": 5156.0
    },
    "11080": {
        "name": "Brush",
        "state": "Brush CO",
        "population": 5568,
        "housing": 2242,
        "land_area": 6527711.0,
        "water_area": 0.0
    },
    "11107": {
        "name": "Bryan",
        "state": "Bryan OH",
        "population": 9238,
        "housing": 4406,
        "land_area": 13146751.0,
        "water_area": 18376.0
    },
    "11127": {
        "name": "Buchanan",
        "state": "Buchanan MI",
        "population": 5640,
        "housing": 2661,
        "land_area": 10749925.0,
        "water_area": 136037.0
    },
    "11134": {
        "name": "Buckeye",
        "state": "Buckeye AZ",
        "population": 23897,
        "housing": 7659,
        "land_area": 20154863.0,
        "water_area": 0.0
    },
    "11139": {
        "name": "Buckeye North",
        "state": "Buckeye North AZ",
        "population": 6796,
        "housing": 3928,
        "land_area": 7276729.0,
        "water_area": 0.0
    },
    "11161": {
        "name": "Buckhannon",
        "state": "Buckhannon WV",
        "population": 8547,
        "housing": 3964,
        "land_area": 16862809.0,
        "water_area": 0.0
    },
    "11215": {
        "name": "Bucyrus",
        "state": "Bucyrus OH",
        "population": 11772,
        "housing": 5827,
        "land_area": 17757877.0,
        "water_area": 1247.0
    },
    "11235": {
        "name": "Buellton",
        "state": "Buellton CA",
        "population": 5161,
        "housing": 2030,
        "land_area": 4125973.0,
        "water_area": 1684.0
    },
    "11242": {
        "name": "Buena Vista",
        "state": "Buena Vista CO",
        "population": 5038,
        "housing": 2075,
        "land_area": 13308287.0,
        "water_area": 88623.0
    },
    "11269": {
        "name": "Buena Vista",
        "state": "Buena Vista VA",
        "population": 6603,
        "housing": 2937,
        "land_area": 10478258.0,
        "water_area": 1846.0
    },
    "11296": {
        "name": "Buffalo",
        "state": "Buffalo MN",
        "population": 16439,
        "housing": 6385,
        "land_area": 21188068.0,
        "water_area": 3132787.0
    },
    "11350": {
        "name": "Buffalo",
        "state": "Buffalo NY",
        "population": 948864,
        "housing": 442770,
        "land_area": 881897337.0,
        "water_area": 7944757.0
    },
    "11377": {
        "name": "Buffalo",
        "state": "Buffalo WY",
        "population": 4516,
        "housing": 2316,
        "land_area": 8502865.0,
        "water_area": 1106.0
    },
    "11420": {
        "name": "Buies Creek--Lillington",
        "state": "Buies Creek--Lillington NC",
        "population": 7391,
        "housing": 2930,
        "land_area": 18495972.0,
        "water_area": 111755.0
    },
    "11431": {
        "name": "Bullhead City",
        "state": "Bullhead City AZ--NV",
        "population": 54396,
        "housing": 30618,
        "land_area": 91669615.0,
        "water_area": 1447784.0
    },
    "11593": {
        "name": "Burkburnett",
        "state": "Burkburnett TX",
        "population": 10449,
        "housing": 4632,
        "land_area": 16787734.0,
        "water_area": 0.0
    },
    "11620": {
        "name": "Burley",
        "state": "Burley ID",
        "population": 17741,
        "housing": 6631,
        "land_area": 24724005.0,
        "water_area": 1800922.0
    },
    "11674": {
        "name": "Burlington",
        "state": "Burlington IA--IL",
        "population": 28447,
        "housing": 14251,
        "land_area": 54690492.0,
        "water_area": 226333.0
    },
    "11728": {
        "name": "Burlington",
        "state": "Burlington NC",
        "population": 145311,
        "housing": 61970,
        "land_area": 238325399.0,
        "water_area": 2050931.0
    },
    "11755": {
        "name": "Burlington",
        "state": "Burlington VT",
        "population": 118032,
        "housing": 52015,
        "land_area": 160616181.0,
        "water_area": 2579671.0
    },
    "11782": {
        "name": "Burlington",
        "state": "Burlington WI",
        "population": 24086,
        "housing": 10643,
        "land_area": 32916138.0,
        "water_area": 3193675.0
    },
    "11809": {
        "name": "Burnet",
        "state": "Burnet TX",
        "population": 5001,
        "housing": 2223,
        "land_area": 9249642.0,
        "water_area": 53670.0
    },
    "11863": {
        "name": "Burns",
        "state": "Burns OR",
        "population": 4169,
        "housing": 2058,
        "land_area": 7820019.0,
        "water_area": 0.0
    },
    "11872": {
        "name": "Burnt Store Marina",
        "state": "Burnt Store Marina FL",
        "population": 4191,
        "housing": 3220,
        "land_area": 11047450.0,
        "water_area": 825900.0
    },
    "11890": {
        "name": "Bushnell",
        "state": "Bushnell FL",
        "population": 3664,
        "housing": 2061,
        "land_area": 7310388.0,
        "water_area": 0.0
    },
    "11998": {
        "name": "Butler",
        "state": "Butler PA",
        "population": 37954,
        "housing": 18787,
        "land_area": 73718593.0,
        "water_area": 382000.0
    },
    "12052": {
        "name": "Butte-Silver Bow",
        "state": "Butte-Silver Bow MT",
        "population": 30258,
        "housing": 15141,
        "land_area": 39593452.0,
        "water_area": 0.0
    },
    "12079": {
        "name": "Byron",
        "state": "Byron IL",
        "population": 5625,
        "housing": 2301,
        "land_area": 8606615.0,
        "water_area": 672655.0
    },
    "12106": {
        "name": "Byron",
        "state": "Byron MN",
        "population": 6341,
        "housing": 2398,
        "land_area": 7995614.0,
        "water_area": 0.0
    },
    "12160": {
        "name": "Cadillac",
        "state": "Cadillac MI",
        "population": 12208,
        "housing": 6140,
        "land_area": 28133532.0,
        "water_area": 5038611.0
    },
    "12241": {
        "name": "Cairo",
        "state": "Cairo GA",
        "population": 10346,
        "housing": 4422,
        "land_area": 20753105.0,
        "water_area": 230628.0
    },
    "12430": {
        "name": "Calexico",
        "state": "Calexico CA",
        "population": 38491,
        "housing": 10793,
        "land_area": 14011734.0,
        "water_area": 0.0
    },
    "12457": {
        "name": "Calhoun",
        "state": "Calhoun GA",
        "population": 23066,
        "housing": 9099,
        "land_area": 52812371.0,
        "water_area": 143355.0
    },
    "12538": {
        "name": "California--Brownsville",
        "state": "California--Brownsville PA",
        "population": 10185,
        "housing": 4994,
        "land_area": 15670451.0,
        "water_area": 1275094.0
    },
    "12646": {
        "name": "Calistoga",
        "state": "Calistoga CA",
        "population": 5173,
        "housing": 2376,
        "land_area": 5924115.0,
        "water_area": 46827.0
    },
    "12655": {
        "name": "Calumet",
        "state": "Calumet MI",
        "population": 5112,
        "housing": 2927,
        "land_area": 6433208.0,
        "water_area": 36824.0
    },
    "12754": {
        "name": "Camarillo",
        "state": "Camarillo CA",
        "population": 76338,
        "housing": 30143,
        "land_area": 58220217.0,
        "water_area": 42884.0
    },
    "12781": {
        "name": "Cambria",
        "state": "Cambria CA",
        "population": 5478,
        "housing": 3924,
        "land_area": 7082942.0,
        "water_area": 0.0
    },
    "12808": {
        "name": "Cambridge",
        "state": "Cambridge MD",
        "population": 14978,
        "housing": 7425,
        "land_area": 22406432.0,
        "water_area": 361745.0
    },
    "12835": {
        "name": "Cambridge",
        "state": "Cambridge MN",
        "population": 10128,
        "housing": 4196,
        "land_area": 15386802.0,
        "water_area": 590016.0
    },
    "12862": {
        "name": "Cambridge",
        "state": "Cambridge OH",
        "population": 14427,
        "housing": 7367,
        "land_area": 28426819.0,
        "water_area": 128898.0
    },
    "12916": {
        "name": "Camden",
        "state": "Camden AR",
        "population": 9873,
        "housing": 5192,
        "land_area": 28620569.0,
        "water_area": 0.0
    },
    "12943": {
        "name": "Camden",
        "state": "Camden ME",
        "population": 4660,
        "housing": 2959,
        "land_area": 10532475.0,
        "water_area": 307306.0
    },
    "13010": {
        "name": "Camden--Lugoff",
        "state": "Camden--Lugoff SC",
        "population": 30655,
        "housing": 13337,
        "land_area": 89711864.0,
        "water_area": 666897.0
    },
    "13024": {
        "name": "Camdenton",
        "state": "Camdenton MO",
        "population": 5849,
        "housing": 3481,
        "land_area": 22974321.0,
        "water_area": 0.0
    },
    "13051": {
        "name": "Cameron",
        "state": "Cameron MO",
        "population": 8450,
        "housing": 2882,
        "land_area": 11673085.0,
        "water_area": 84707.0
    },
    "13078": {
        "name": "Cameron",
        "state": "Cameron TX",
        "population": 5151,
        "housing": 2221,
        "land_area": 9070934.0,
        "water_area": 0.0
    },
    "13132": {
        "name": "Camilla",
        "state": "Camilla GA",
        "population": 5270,
        "housing": 2350,
        "land_area": 12286059.0,
        "water_area": 34608.0
    },
    "13159": {
        "name": "Campbellsville",
        "state": "Campbellsville KY",
        "population": 12789,
        "housing": 5547,
        "land_area": 21990766.0,
        "water_area": 308844.0
    },
    "13170": {
        "name": "Camp Verde",
        "state": "Camp Verde AZ",
        "population": 5759,
        "housing": 2649,
        "land_area": 13472196.0,
        "water_area": 0.0
    },
    "13186": {
        "name": "Canajoharie--Fort Plain",
        "state": "Canajoharie--Fort Plain NY",
        "population": 5278,
        "housing": 2706,
        "land_area": 10409101.0,
        "water_area": 374306.0
    },
    "13213": {
        "name": "Canandaigua",
        "state": "Canandaigua NY",
        "population": 18049,
        "housing": 9873,
        "land_area": 33853136.0,
        "water_area": 106257.0
    },
    "13217": {
        "name": "Canastota",
        "state": "Canastota NY",
        "population": 5616,
        "housing": 2550,
        "land_area": 9753228.0,
        "water_area": 13771.0
    },
    "13221": {
        "name": "Canby",
        "state": "Canby OR",
        "population": 19055,
        "housing": 7104,
        "land_area": 15275091.0,
        "water_area": 4632.0
    },
    "13267": {
        "name": "Ca\u00f1on City",
        "state": "Ca\u00f1on City CO",
        "population": 24737,
        "housing": 11117,
        "land_area": 41230887.0,
        "water_area": 31525.0
    },
    "13294": {
        "name": "Canton",
        "state": "Canton IL",
        "population": 13177,
        "housing": 5828,
        "land_area": 14045059.0,
        "water_area": 0.0
    },
    "13321": {
        "name": "Canton",
        "state": "Canton MS",
        "population": 26257,
        "housing": 10461,
        "land_area": 61401387.0,
        "water_area": 4007630.0
    },
    "13348": {
        "name": "Canton",
        "state": "Canton NY",
        "population": 6812,
        "housing": 1815,
        "land_area": 5744799.0,
        "water_area": 237068.0
    },
    "13361": {
        "name": "Canton",
        "state": "Canton NC",
        "population": 8812,
        "housing": 4109,
        "land_area": 22978340.0,
        "water_area": 16069.0
    },
    "13375": {
        "name": "Canton",
        "state": "Canton OH",
        "population": 295319,
        "housing": 132970,
        "land_area": 466699663.0,
        "water_area": 3240890.0
    },
    "13456": {
        "name": "Canyon",
        "state": "Canyon TX",
        "population": 16171,
        "housing": 6999,
        "land_area": 18970245.0,
        "water_area": 67565.0
    },
    "13483": {
        "name": "Canyon Lake",
        "state": "Canyon Lake TX",
        "population": 7918,
        "housing": 4615,
        "land_area": 23917432.0,
        "water_area": 39897.0
    },
    "13510": {
        "name": "Cape Coral",
        "state": "Cape Coral FL",
        "population": 599242,
        "housing": 316907,
        "land_area": 859359686.0,
        "water_area": 101715750.0
    },
    "13537": {
        "name": "Cape Girardeau",
        "state": "Cape Girardeau MO--IL",
        "population": 55546,
        "housing": 24822,
        "land_area": 80851552.0,
        "water_area": 121519.0
    },
    "13564": {
        "name": "Carbondale",
        "state": "Carbondale CO",
        "population": 7361,
        "housing": 2887,
        "land_area": 6832951.0,
        "water_area": 0.0
    },
    "13591": {
        "name": "Carbondale",
        "state": "Carbondale IL",
        "population": 31488,
        "housing": 17666,
        "land_area": 56111366.0,
        "water_area": 1285073.0
    },
    "13699": {
        "name": "Carlinville",
        "state": "Carlinville IL",
        "population": 5602,
        "housing": 2533,
        "land_area": 7737210.0,
        "water_area": 0.0
    },
    "13807": {
        "name": "Carlsbad",
        "state": "Carlsbad NM",
        "population": 34442,
        "housing": 14802,
        "land_area": 50439506.0,
        "water_area": 658851.0
    },
    "13861": {
        "name": "Carmi",
        "state": "Carmi IL",
        "population": 5067,
        "housing": 2539,
        "land_area": 6856255.0,
        "water_area": 52256.0
    },
    "13915": {
        "name": "Caro",
        "state": "Caro MI",
        "population": 5383,
        "housing": 2567,
        "land_area": 11767016.0,
        "water_area": 0.0
    },
    "13969": {
        "name": "Carrizo Springs",
        "state": "Carrizo Springs TX",
        "population": 5615,
        "housing": 2470,
        "land_area": 10973849.0,
        "water_area": 32947.0
    },
    "13996": {
        "name": "Carroll",
        "state": "Carroll IA",
        "population": 10150,
        "housing": 4769,
        "land_area": 12863981.0,
        "water_area": 0.0
    },
    "14023": {
        "name": "Carrollton",
        "state": "Carrollton GA",
        "population": 38385,
        "housing": 14836,
        "land_area": 89140581.0,
        "water_area": 1613880.0
    },
    "14050": {
        "name": "Carrollton",
        "state": "Carrollton KY",
        "population": 5471,
        "housing": 2398,
        "land_area": 9107568.0,
        "water_area": 61320.0
    },
    "14158": {
        "name": "Carson City",
        "state": "Carson City NV",
        "population": 61629,
        "housing": 26356,
        "land_area": 67818880.0,
        "water_area": 169053.0
    },
    "14185": {
        "name": "Cartersville",
        "state": "Cartersville GA",
        "population": 52351,
        "housing": 20867,
        "land_area": 115053727.0,
        "water_area": 91238.0
    },
    "14266": {
        "name": "Carthage",
        "state": "Carthage MO",
        "population": 16260,
        "housing": 6432,
        "land_area": 22231408.0,
        "water_area": 0.0
    },
    "14293": {
        "name": "Carthage",
        "state": "Carthage NY",
        "population": 5160,
        "housing": 2551,
        "land_area": 6419965.0,
        "water_area": 2451.0
    },
    "14347": {
        "name": "Carthage",
        "state": "Carthage TX",
        "population": 6328,
        "housing": 2841,
        "land_area": 19322282.0,
        "water_area": 15541.0
    },
    "14374": {
        "name": "Caruthersville",
        "state": "Caruthersville MO",
        "population": 5319,
        "housing": 2582,
        "land_area": 8416322.0,
        "water_area": 2773.0
    },
    "14401": {
        "name": "Casa Grande",
        "state": "Casa Grande AZ",
        "population": 50981,
        "housing": 22577,
        "land_area": 55593619.0,
        "water_area": 0.0
    },
    "14482": {
        "name": "Casper",
        "state": "Casper WY",
        "population": 67751,
        "housing": 31193,
        "land_area": 72204393.0,
        "water_area": 858175.0
    },
    "14563": {
        "name": "Castle Rock",
        "state": "Castle Rock CO",
        "population": 85350,
        "housing": 31345,
        "land_area": 92478222.0,
        "water_area": 15254.0
    },
    "14600": {
        "name": "Castroville--Prunedale",
        "state": "Castroville--Prunedale CA",
        "population": 12334,
        "housing": 3180,
        "land_area": 8754100.0,
        "water_area": 222990.0
    },
    "14644": {
        "name": "Catskill",
        "state": "Catskill NY",
        "population": 7012,
        "housing": 3733,
        "land_area": 16633988.0,
        "water_area": 225648.0
    },
    "14698": {
        "name": "Cedar City",
        "state": "Cedar City UT",
        "population": 40899,
        "housing": 14337,
        "land_area": 47391148.0,
        "water_area": 0.0
    },
    "14752": {
        "name": "Cedar Rapids",
        "state": "Cedar Rapids IA",
        "population": 192844,
        "housing": 86125,
        "land_area": 222816547.0,
        "water_area": 2436556.0
    },
    "14806": {
        "name": "Cedartown",
        "state": "Cedartown GA",
        "population": 12833,
        "housing": 5133,
        "land_area": 24785042.0,
        "water_area": 75977.0
    },
    "14860": {
        "name": "Celina",
        "state": "Celina OH",
        "population": 12035,
        "housing": 5519,
        "land_area": 15764453.0,
        "water_area": 181760.0
    },
    "14887": {
        "name": "Center",
        "state": "Center TX",
        "population": 5123,
        "housing": 2271,
        "land_area": 17564522.0,
        "water_area": 18192.0
    },
    "14914": {
        "name": "Centerville",
        "state": "Centerville IA",
        "population": 5269,
        "housing": 2724,
        "land_area": 9012668.0,
        "water_area": 69284.0
    },
    "14960": {
        "name": "Central City",
        "state": "Central City KY",
        "population": 5767,
        "housing": 2242,
        "land_area": 11082153.0,
        "water_area": 9017.0
    },
    "15022": {
        "name": "Centralia",
        "state": "Centralia IL",
        "population": 15301,
        "housing": 7386,
        "land_area": 25414515.0,
        "water_area": 60073.0
    },
    "15076": {
        "name": "Centralia",
        "state": "Centralia WA",
        "population": 42338,
        "housing": 16951,
        "land_area": 78706156.0,
        "water_area": 809598.0
    },
    "15130": {
        "name": "Chadron",
        "state": "Chadron NE",
        "population": 4930,
        "housing": 2213,
        "land_area": 5969800.0,
        "water_area": 0.0
    },
    "15184": {
        "name": "Chambersburg",
        "state": "Chambersburg PA",
        "population": 50094,
        "housing": 21787,
        "land_area": 89808865.0,
        "water_area": 24541.0
    },
    "15211": {
        "name": "Champaign",
        "state": "Champaign IL",
        "population": 147452,
        "housing": 68225,
        "land_area": 120497761.0,
        "water_area": 975880.0
    },
    "15265": {
        "name": "Chanute",
        "state": "Chanute KS",
        "population": 8710,
        "housing": 4112,
        "land_area": 15017907.0,
        "water_area": 27515.0
    },
    "15300": {
        "name": "Chapin",
        "state": "Chapin SC",
        "population": 5701,
        "housing": 2238,
        "land_area": 16377654.0,
        "water_area": 26954.0
    },
    "15319": {
        "name": "Chardon",
        "state": "Chardon OH",
        "population": 6454,
        "housing": 3062,
        "land_area": 13530825.0,
        "water_area": 49967.0
    },
    "15373": {
        "name": "Charles City",
        "state": "Charles City IA",
        "population": 7255,
        "housing": 3684,
        "land_area": 10575577.0,
        "water_area": 179033.0
    },
    "15400": {
        "name": "Charleston",
        "state": "Charleston IL",
        "population": 17415,
        "housing": 8399,
        "land_area": 20079870.0,
        "water_area": 735.0
    },
    "15481": {
        "name": "Charleston",
        "state": "Charleston WV",
        "population": 140958,
        "housing": 71602,
        "land_area": 240667742.0,
        "water_area": 20628176.0
    },
    "15508": {
        "name": "Charleston",
        "state": "Charleston SC",
        "population": 684773,
        "housing": 305541,
        "land_area": 878170419.0,
        "water_area": 18026428.0
    },
    "15535": {
        "name": "Charlestown",
        "state": "Charlestown IN",
        "population": 6696,
        "housing": 2881,
        "land_area": 10428023.0,
        "water_area": 37520.0
    },
    "15589": {
        "name": "Charlestown",
        "state": "Charlestown RI",
        "population": 4348,
        "housing": 3712,
        "land_area": 16371972.0,
        "water_area": 3289121.0
    },
    "15616": {
        "name": "Charlevoix",
        "state": "Charlevoix MI",
        "population": 3777,
        "housing": 3092,
        "land_area": 10479037.0,
        "water_area": 319731.0
    },
    "15643": {
        "name": "Charlotte",
        "state": "Charlotte MI",
        "population": 13026,
        "housing": 5569,
        "land_area": 21856825.0,
        "water_area": 443339.0
    },
    "15670": {
        "name": "Charlotte",
        "state": "Charlotte NC--SC",
        "population": 1379873,
        "housing": 576259,
        "land_area": 1703212422.0,
        "water_area": 26670624.0
    },
    "15697": {
        "name": "Charlotte Amalie",
        "state": "Charlotte Amalie VI",
        "population": 41534,
        "housing": 27775,
        "land_area": 60996780.0,
        "water_area": 436682.0
    },
    "15724": {
        "name": "Charlottesville",
        "state": "Charlottesville VA",
        "population": 104191,
        "housing": 45311,
        "land_area": 95500538.0,
        "water_area": 767733.0
    },
    "15778": {
        "name": "Chatsworth",
        "state": "Chatsworth GA",
        "population": 12808,
        "housing": 5030,
        "land_area": 41731470.0,
        "water_area": 24409.0
    },
    "15832": {
        "name": "Chattanooga",
        "state": "Chattanooga TN--GA",
        "population": 398569,
        "housing": 176961,
        "land_area": 755506593.0,
        "water_area": 5838212.0
    },
    "15859": {
        "name": "Cheboygan",
        "state": "Cheboygan MI",
        "population": 5142,
        "housing": 3100,
        "land_area": 15612124.0,
        "water_area": 194614.0
    },
    "15913": {
        "name": "Chelan",
        "state": "Chelan WA",
        "population": 6380,
        "housing": 4637,
        "land_area": 19534139.0,
        "water_area": 113252.0
    },
    "15940": {
        "name": "Chelsea",
        "state": "Chelsea MI",
        "population": 5851,
        "housing": 2661,
        "land_area": 9187596.0,
        "water_area": 133352.0
    },
    "15967": {
        "name": "Cheney",
        "state": "Cheney WA",
        "population": 13176,
        "housing": 5346,
        "land_area": 8540977.0,
        "water_area": 0.0
    },
    "15994": {
        "name": "Cheraw",
        "state": "Cheraw SC",
        "population": 7480,
        "housing": 3686,
        "land_area": 19040160.0,
        "water_area": 126066.0
    },
    "16021": {
        "name": "Cherokee",
        "state": "Cherokee IA",
        "population": 4705,
        "housing": 2288,
        "land_area": 8127606.0,
        "water_area": 0.0
    },
    "16075": {
        "name": "Cherryville",
        "state": "Cherryville NC",
        "population": 6747,
        "housing": 3027,
        "land_area": 15996520.0,
        "water_area": 32069.0
    },
    "16102": {
        "name": "Chesapeake Beach",
        "state": "Chesapeake Beach MD",
        "population": 16926,
        "housing": 7005,
        "land_area": 33904713.0,
        "water_area": 419304.0
    },
    "16156": {
        "name": "Chester",
        "state": "Chester IL",
        "population": 6338,
        "housing": 2026,
        "land_area": 10260912.0,
        "water_area": 31757.0
    },
    "16171": {
        "name": "Chester",
        "state": "Chester NY",
        "population": 5900,
        "housing": 2448,
        "land_area": 11842062.0,
        "water_area": 19894.0
    },
    "16183": {
        "name": "Chester",
        "state": "Chester SC",
        "population": 8611,
        "housing": 4093,
        "land_area": 16293135.0,
        "water_area": 26285.0
    },
    "16210": {
        "name": "Chestertown",
        "state": "Chestertown MD",
        "population": 7392,
        "housing": 3337,
        "land_area": 11448080.0,
        "water_area": 55789.0
    },
    "16237": {
        "name": "Cheyenne",
        "state": "Cheyenne WY",
        "population": 79250,
        "housing": 35732,
        "land_area": 87734808.0,
        "water_area": 226823.0
    },
    "16264": {
        "name": "Chicago",
        "state": "Chicago IL--IN",
        "population": 8671746,
        "housing": 3559615,
        "land_area": 6055094918.0,
        "water_area": 100880782.0
    },
    "16291": {
        "name": "Chickasha",
        "state": "Chickasha OK",
        "population": 15253,
        "housing": 7017,
        "land_area": 25251263.0,
        "water_area": 123501.0
    },
    "16318": {
        "name": "Chico",
        "state": "Chico CA",
        "population": 111411,
        "housing": 48438,
        "land_area": 87321741.0,
        "water_area": 145207.0
    },
    "16372": {
        "name": "Childress",
        "state": "Childress TX",
        "population": 4516,
        "housing": 2298,
        "land_area": 8345931.0,
        "water_area": 35605.0
    },
    "16426": {
        "name": "Chillicothe",
        "state": "Chillicothe MO",
        "population": 9122,
        "housing": 3910,
        "land_area": 15490725.0,
        "water_area": 68820.0
    },
    "16453": {
        "name": "Chillicothe",
        "state": "Chillicothe OH",
        "population": 31727,
        "housing": 12864,
        "land_area": 42460464.0,
        "water_area": 388799.0
    },
    "16507": {
        "name": "Chincoteague",
        "state": "Chincoteague VA",
        "population": 3223,
        "housing": 4092,
        "land_area": 10513333.0,
        "water_area": 330156.0
    },
    "16561": {
        "name": "Chino Valley",
        "state": "Chino Valley AZ",
        "population": 13317,
        "housing": 5875,
        "land_area": 35536452.0,
        "water_area": 0.0
    },
    "16615": {
        "name": "Chisholm",
        "state": "Chisholm MN",
        "population": 4586,
        "housing": 2296,
        "land_area": 5652033.0,
        "water_area": 69307.0
    },
    "16642": {
        "name": "Chittenango",
        "state": "Chittenango NY",
        "population": 5054,
        "housing": 2166,
        "land_area": 5601150.0,
        "water_area": 4283.0
    },
    "16696": {
        "name": "Chowchilla",
        "state": "Chowchilla CA",
        "population": 13196,
        "housing": 4417,
        "land_area": 12497893.0,
        "water_area": 119504.0
    },
    "16750": {
        "name": "Christiansted--Frederiksted",
        "state": "Christiansted--Frederiksted VI",
        "population": 38372,
        "housing": 23713,
        "land_area": 89028187.0,
        "water_area": 179432.0
    },
    "16804": {
        "name": "Ciales",
        "state": "Ciales PR",
        "population": 13098,
        "housing": 5797,
        "land_area": 29983903.0,
        "water_area": 34746.0
    },
    "16830": {
        "name": "Cienega Springs",
        "state": "Cienega Springs AZ",
        "population": 2041,
        "housing": 2934,
        "land_area": 8288459.0,
        "water_area": 0.0
    },
    "16885": {
        "name": "Cincinnati",
        "state": "Cincinnati OH--KY",
        "population": 1686744,
        "housing": 727550,
        "land_area": 1948383614.0,
        "water_area": 22192733.0
    },
    "16912": {
        "name": "Circleville",
        "state": "Circleville OH",
        "population": 15679,
        "housing": 6971,
        "land_area": 19103474.0,
        "water_area": 21920.0
    },
    "17047": {
        "name": "Clanton",
        "state": "Clanton AL",
        "population": 6423,
        "housing": 2847,
        "land_area": 23460292.0,
        "water_area": 141751.0
    },
    "17101": {
        "name": "Claremont",
        "state": "Claremont NH",
        "population": 9415,
        "housing": 4414,
        "land_area": 15494240.0,
        "water_area": 50767.0
    },
    "17128": {
        "name": "Claremore",
        "state": "Claremore OK",
        "population": 25415,
        "housing": 10532,
        "land_area": 39966157.0,
        "water_area": 2253.0
    },
    "17155": {
        "name": "Clarinda",
        "state": "Clarinda IA",
        "population": 5213,
        "housing": 2105,
        "land_area": 6107154.0,
        "water_area": 0.0
    },
    "17209": {
        "name": "Clarion",
        "state": "Clarion PA",
        "population": 5662,
        "housing": 2746,
        "land_area": 8735866.0,
        "water_area": 41518.0
    },
    "17236": {
        "name": "Clarksburg",
        "state": "Clarksburg WV",
        "population": 32882,
        "housing": 16027,
        "land_area": 54960546.0,
        "water_area": 180510.0
    },
    "17263": {
        "name": "Clarksdale",
        "state": "Clarksdale MS",
        "population": 14408,
        "housing": 6851,
        "land_area": 19203935.0,
        "water_area": 16607.0
    },
    "17290": {
        "name": "Clarksville",
        "state": "Clarksville AR",
        "population": 7816,
        "housing": 3324,
        "land_area": 20183782.0,
        "water_area": 117983.0
    },
    "17317": {
        "name": "Clarksville",
        "state": "Clarksville TN--KY",
        "population": 200947,
        "housing": 76824,
        "land_area": 292896076.0,
        "water_area": 154654.0
    },
    "17398": {
        "name": "Clay Center",
        "state": "Clay Center KS",
        "population": 4131,
        "housing": 2073,
        "land_area": 6516012.0,
        "water_area": 4528.0
    },
    "17424": {
        "name": "Clayton",
        "state": "Clayton NY",
        "population": 2092,
        "housing": 2089,
        "land_area": 7113748.0,
        "water_area": 1317860.0
    },
    "17426": {
        "name": "Clayton",
        "state": "Clayton NC",
        "population": 51898,
        "housing": 19895,
        "land_area": 93917530.0,
        "water_area": 415456.0
    },
    "17452": {
        "name": "Clearfield",
        "state": "Clearfield PA",
        "population": 10524,
        "housing": 5418,
        "land_area": 18320614.0,
        "water_area": 293357.0
    },
    "17479": {
        "name": "Clearlake",
        "state": "Clearlake CA",
        "population": 17351,
        "housing": 8262,
        "land_area": 21064685.0,
        "water_area": 175621.0
    },
    "17506": {
        "name": "Clear Lake",
        "state": "Clear Lake IA",
        "population": 8406,
        "housing": 5640,
        "land_area": 22335760.0,
        "water_area": 74704.0
    },
    "17514": {
        "name": "Clearlake Riviera",
        "state": "Clearlake Riviera CA",
        "population": 5461,
        "housing": 3439,
        "land_area": 17417930.0,
        "water_area": 106635.0
    },
    "17533": {
        "name": "Cleburne",
        "state": "Cleburne TX",
        "population": 43901,
        "housing": 16854,
        "land_area": 63482287.0,
        "water_area": 339995.0
    },
    "17560": {
        "name": "Cle Elum",
        "state": "Cle Elum WA",
        "population": 3846,
        "housing": 2369,
        "land_area": 11724827.0,
        "water_area": 0.0
    },
    "17641": {
        "name": "Cleveland",
        "state": "Cleveland MS",
        "population": 14346,
        "housing": 6405,
        "land_area": 21322849.0,
        "water_area": 0.0
    },
    "17668": {
        "name": "Cleveland",
        "state": "Cleveland OH",
        "population": 1712178,
        "housing": 808782,
        "land_area": 1848754208.0,
        "water_area": 11928286.0
    },
    "17722": {
        "name": "Cleveland",
        "state": "Cleveland TN",
        "population": 73918,
        "housing": 30584,
        "land_area": 142381752.0,
        "water_area": 113225.0
    },
    "17749": {
        "name": "Cleveland",
        "state": "Cleveland TX",
        "population": 7469,
        "housing": 3054,
        "land_area": 11761157.0,
        "water_area": 0.0
    },
    "17776": {
        "name": "Clewiston",
        "state": "Clewiston FL",
        "population": 12849,
        "housing": 4761,
        "land_area": 14371136.0,
        "water_area": 389366.0
    },
    "17857": {
        "name": "Clifton Forge",
        "state": "Clifton Forge VA",
        "population": 5127,
        "housing": 2759,
        "land_area": 11648596.0,
        "water_area": 229031.0
    },
    "17870": {
        "name": "Clifton Springs",
        "state": "Clifton Springs NY",
        "population": 6383,
        "housing": 2900,
        "land_area": 10873942.0,
        "water_area": 0.0
    },
    "17884": {
        "name": "Clinton",
        "state": "Clinton IL",
        "population": 7323,
        "housing": 3549,
        "land_area": 9441802.0,
        "water_area": 0.0
    },
    "17911": {
        "name": "Clinton",
        "state": "Clinton IN",
        "population": 6484,
        "housing": 3130,
        "land_area": 8117234.0,
        "water_area": 0.0
    },
    "17938": {
        "name": "Clinton--Fulton",
        "state": "Clinton--Fulton IA--IL",
        "population": 31126,
        "housing": 15074,
        "land_area": 49494357.0,
        "water_area": 3310977.0
    },
    "17965": {
        "name": "Clinton",
        "state": "Clinton MO",
        "population": 8866,
        "housing": 4370,
        "land_area": 15674034.0,
        "water_area": 186454.0
    },
    "17980": {
        "name": "Clinton",
        "state": "Clinton NJ",
        "population": 16136,
        "housing": 6161,
        "land_area": 27103941.0,
        "water_area": 253207.0
    },
    "17992": {
        "name": "Clinton",
        "state": "Clinton NC",
        "population": 9315,
        "housing": 4127,
        "land_area": 23542991.0,
        "water_area": 145681.0
    },
    "18019": {
        "name": "Clinton",
        "state": "Clinton OK",
        "population": 8022,
        "housing": 3571,
        "land_area": 11245863.0,
        "water_area": 0.0
    },
    "18046": {
        "name": "Clinton",
        "state": "Clinton SC",
        "population": 9143,
        "housing": 4046,
        "land_area": 21917816.0,
        "water_area": 40622.0
    },
    "18100": {
        "name": "Clintonville",
        "state": "Clintonville WI",
        "population": 4530,
        "housing": 2262,
        "land_area": 6919219.0,
        "water_area": 243569.0
    },
    "18127": {
        "name": "Cloquet",
        "state": "Cloquet MN",
        "population": 13213,
        "housing": 5704,
        "land_area": 24419597.0,
        "water_area": 504537.0
    },
    "18154": {
        "name": "Clover",
        "state": "Clover SC",
        "population": 7526,
        "housing": 2948,
        "land_area": 13432092.0,
        "water_area": 29347.0
    },
    "18181": {
        "name": "Cloverdale",
        "state": "Cloverdale CA",
        "population": 9451,
        "housing": 3718,
        "land_area": 8734589.0,
        "water_area": 0.0
    },
    "18208": {
        "name": "Clovis",
        "state": "Clovis NM",
        "population": 39314,
        "housing": 17413,
        "land_area": 41604534.0,
        "water_area": 173032.0
    },
    "18235": {
        "name": "Clyde",
        "state": "Clyde OH",
        "population": 6549,
        "housing": 2825,
        "land_area": 12420848.0,
        "water_area": 133708.0
    },
    "18270": {
        "name": "Coal City--Braidwood",
        "state": "Coal City--Braidwood IL",
        "population": 15837,
        "housing": 7007,
        "land_area": 26645355.0,
        "water_area": 500703.0
    },
    "18289": {
        "name": "Coalinga",
        "state": "Coalinga CA",
        "population": 13049,
        "housing": 4655,
        "land_area": 11039196.0,
        "water_area": 21696.0
    },
    "18316": {
        "name": "Coamo",
        "state": "Coamo PR",
        "population": 30344,
        "housing": 14143,
        "land_area": 42116009.0,
        "water_area": 28672.0
    },
    "18343": {
        "name": "Cobleskill",
        "state": "Cobleskill NY",
        "population": 5040,
        "housing": 2359,
        "land_area": 11215264.0,
        "water_area": 9387.0
    },
    "18370": {
        "name": "Cochran",
        "state": "Cochran GA",
        "population": 6159,
        "housing": 2408,
        "land_area": 14124158.0,
        "water_area": 293649.0
    },
    "18397": {
        "name": "Cody",
        "state": "Cody WY",
        "population": 9999,
        "housing": 5060,
        "land_area": 17893067.0,
        "water_area": 29888.0
    },
    "18451": {
        "name": "Coeur d'Alene",
        "state": "Coeur d'Alene ID",
        "population": 121831,
        "housing": 51420,
        "land_area": 121096977.0,
        "water_area": 2005943.0
    },
    "18478": {
        "name": "Coffeyville",
        "state": "Coffeyville KS--OK",
        "population": 9391,
        "housing": 4814,
        "land_area": 19269785.0,
        "water_area": 0.0
    },
    "18532": {
        "name": "Colby",
        "state": "Colby KS",
        "population": 5467,
        "housing": 2479,
        "land_area": 7676835.0,
        "water_area": 0.0
    },
    "18559": {
        "name": "Colchester",
        "state": "Colchester CT",
        "population": 5512,
        "housing": 2553,
        "land_area": 13053761.0,
        "water_area": 33988.0
    },
    "18586": {
        "name": "Cold Spring",
        "state": "Cold Spring MN",
        "population": 5099,
        "housing": 2211,
        "land_area": 8316484.0,
        "water_area": 0.0
    },
    "18640": {
        "name": "Cold Springs",
        "state": "Cold Springs NV",
        "population": 9686,
        "housing": 3510,
        "land_area": 8984732.0,
        "water_area": 0.0
    },
    "18667": {
        "name": "Coldwater",
        "state": "Coldwater MI",
        "population": 13721,
        "housing": 5659,
        "land_area": 27708953.0,
        "water_area": 772412.0
    },
    "18694": {
        "name": "Coleman",
        "state": "Coleman TX",
        "population": 3599,
        "housing": 2129,
        "land_area": 7221414.0,
        "water_area": 0.0
    },
    "18748": {
        "name": "College Station--Bryan",
        "state": "College Station--Bryan TX",
        "population": 206137,
        "housing": 86504,
        "land_area": 211410239.0,
        "water_area": 179498.0
    },
    "18755": {
        "name": "Collins",
        "state": "Collins NY",
        "population": 5448,
        "housing": 1680,
        "land_area": 6054287.0,
        "water_area": 41064.0
    },
    "18760": {
        "name": "Collinsville",
        "state": "Collinsville OK",
        "population": 4706,
        "housing": 2040,
        "land_area": 8891071.0,
        "water_area": 46681.0
    },
    "18775": {
        "name": "Colonial Beach",
        "state": "Colonial Beach VA",
        "population": 3975,
        "housing": 2539,
        "land_area": 4478990.0,
        "water_area": 28648.0
    },
    "18829": {
        "name": "Colorado City",
        "state": "Colorado City TX",
        "population": 5839,
        "housing": 1807,
        "land_area": 7212210.0,
        "water_area": 0.0
    },
    "18856": {
        "name": "Colorado Springs",
        "state": "Colorado Springs CO",
        "population": 632494,
        "housing": 254131,
        "land_area": 518938356.0,
        "water_area": 1049396.0
    },
    "18883": {
        "name": "Columbia",
        "state": "Columbia KY",
        "population": 5018,
        "housing": 2036,
        "land_area": 8760295.0,
        "water_area": 99489.0
    },
    "18910": {
        "name": "Columbia",
        "state": "Columbia MS",
        "population": 6236,
        "housing": 2826,
        "land_area": 16311106.0,
        "water_area": 62292.0
    },
    "18937": {
        "name": "Columbia",
        "state": "Columbia MO",
        "population": 141831,
        "housing": 62836,
        "land_area": 174042803.0,
        "water_area": 710720.0
    },
    "18964": {
        "name": "Columbia",
        "state": "Columbia SC",
        "population": 590407,
        "housing": 258608,
        "land_area": 951809584.0,
        "water_area": 18239231.0
    },
    "18991": {
        "name": "Columbia",
        "state": "Columbia TN",
        "population": 42423,
        "housing": 18828,
        "land_area": 78076538.0,
        "water_area": 25459.0
    },
    "19018": {
        "name": "Columbia City",
        "state": "Columbia City IN",
        "population": 10256,
        "housing": 4613,
        "land_area": 15631799.0,
        "water_area": 48844.0
    },
    "19045": {
        "name": "Columbia Falls",
        "state": "Columbia Falls MT",
        "population": 6589,
        "housing": 3158,
        "land_area": 11008998.0,
        "water_area": 2697.0
    },
    "19072": {
        "name": "Columbiana",
        "state": "Columbiana OH",
        "population": 9160,
        "housing": 4571,
        "land_area": 18081221.0,
        "water_area": 182403.0
    },
    "19099": {
        "name": "Columbus",
        "state": "Columbus GA--AL",
        "population": 267746,
        "housing": 117135,
        "land_area": 369997871.0,
        "water_area": 2247227.0
    },
    "19126": {
        "name": "Columbus",
        "state": "Columbus IN",
        "population": 60982,
        "housing": 26694,
        "land_area": 72160366.0,
        "water_area": 838584.0
    },
    "19180": {
        "name": "Columbus",
        "state": "Columbus MS",
        "population": 26895,
        "housing": 12698,
        "land_area": 52011870.0,
        "water_area": 723768.0
    },
    "19207": {
        "name": "Columbus",
        "state": "Columbus NE",
        "population": 24838,
        "housing": 10276,
        "land_area": 34920337.0,
        "water_area": 558476.0
    },
    "19234": {
        "name": "Columbus",
        "state": "Columbus OH",
        "population": 1567254,
        "housing": 672389,
        "land_area": 1336848856.0,
        "water_area": 20362512.0
    },
    "19288": {
        "name": "Columbus",
        "state": "Columbus WI",
        "population": 6977,
        "housing": 3056,
        "land_area": 10566473.0,
        "water_area": 92621.0
    },
    "19342": {
        "name": "Colusa",
        "state": "Colusa CA",
        "population": 6955,
        "housing": 2677,
        "land_area": 7679682.0,
        "water_area": 0.0
    },
    "19369": {
        "name": "Colville",
        "state": "Colville WA",
        "population": 5058,
        "housing": 2328,
        "land_area": 8356417.0,
        "water_area": 0.0
    },
    "19450": {
        "name": "Commerce",
        "state": "Commerce GA",
        "population": 7688,
        "housing": 3196,
        "land_area": 26764385.0,
        "water_area": 92059.0
    },
    "19477": {
        "name": "Commerce",
        "state": "Commerce TX",
        "population": 8320,
        "housing": 3217,
        "land_area": 8656431.0,
        "water_area": 56945.0
    },
    "19504": {
        "name": "Concord--Walnut Creek",
        "state": "Concord--Walnut Creek CA",
        "population": 538583,
        "housing": 211815,
        "land_area": 455224735.0,
        "water_area": 1690188.0
    },
    "19531": {
        "name": "Concord",
        "state": "Concord NH",
        "population": 42549,
        "housing": 18694,
        "land_area": 69017412.0,
        "water_area": 2021718.0
    },
    "19558": {
        "name": "Concord",
        "state": "Concord NC",
        "population": 278612,
        "housing": 111573,
        "land_area": 518146261.0,
        "water_area": 1327468.0
    },
    "19585": {
        "name": "Concordia",
        "state": "Concordia KS",
        "population": 5031,
        "housing": 2511,
        "land_area": 8093559.0,
        "water_area": 0.0
    },
    "19597": {
        "name": "Conesus Lake",
        "state": "Conesus Lake NY",
        "population": 4867,
        "housing": 3126,
        "land_area": 12397369.0,
        "water_area": 13079331.0
    },
    "19612": {
        "name": "Conneaut",
        "state": "Conneaut OH",
        "population": 12072,
        "housing": 5486,
        "land_area": 21440403.0,
        "water_area": 43079.0
    },
    "19639": {
        "name": "Conneaut Lakeshore",
        "state": "Conneaut Lakeshore PA",
        "population": 2846,
        "housing": 2522,
        "land_area": 9758926.0,
        "water_area": 3850135.0
    },
    "19666": {
        "name": "Connell",
        "state": "Connell WA",
        "population": 5437,
        "housing": 1021,
        "land_area": 5636806.0,
        "water_area": 0.0
    },
    "19686": {
        "name": "Connellsville",
        "state": "Connellsville PA",
        "population": 30777,
        "housing": 15316,
        "land_area": 53360867.0,
        "water_area": 174664.0
    },
    "19693": {
        "name": "Connersville",
        "state": "Connersville IN",
        "population": 14401,
        "housing": 7049,
        "land_area": 19300654.0,
        "water_area": 39566.0
    },
    "19801": {
        "name": "Conway",
        "state": "Conway AR",
        "population": 66619,
        "housing": 29045,
        "land_area": 85159076.0,
        "water_area": 719123.0
    },
    "19811": {
        "name": "Conway",
        "state": "Conway NH",
        "population": 5272,
        "housing": 3777,
        "land_area": 22685214.0,
        "water_area": 754532.0
    },
    "19828": {
        "name": "Cookeville",
        "state": "Cookeville TN",
        "population": 49089,
        "housing": 22181,
        "land_area": 117742211.0,
        "water_area": 244853.0
    },
    "19862": {
        "name": "Coolbaugh--Pocono Pines",
        "state": "Coolbaugh--Pocono Pines PA",
        "population": 24893,
        "housing": 13218,
        "land_area": 51130883.0,
        "water_area": 2208636.0
    },
    "19882": {
        "name": "Coolidge",
        "state": "Coolidge AZ",
        "population": 12008,
        "housing": 4486,
        "land_area": 10720219.0,
        "water_area": 19717.0
    },
    "19936": {
        "name": "Coos Bay",
        "state": "Coos Bay OR",
        "population": 31688,
        "housing": 14678,
        "land_area": 37884070.0,
        "water_area": 2389237.0
    },
    "19970": {
        "name": "Coqu\u00ed--Jobos",
        "state": "Coqu\u00ed--Jobos PR",
        "population": 11725,
        "housing": 5588,
        "land_area": 11507372.0,
        "water_area": 13840.0
    },
    "19990": {
        "name": "Coquille",
        "state": "Coquille OR",
        "population": 4373,
        "housing": 2030,
        "land_area": 8894922.0,
        "water_area": 296360.0
    },
    "20044": {
        "name": "Corcoran",
        "state": "Corcoran CA",
        "population": 22377,
        "housing": 4294,
        "land_area": 16284138.0,
        "water_area": 0.0
    },
    "20071": {
        "name": "Cordele",
        "state": "Cordele GA",
        "population": 10931,
        "housing": 5224,
        "land_area": 22269842.0,
        "water_area": 71998.0
    },
    "20098": {
        "name": "Corinth",
        "state": "Corinth MS",
        "population": 12464,
        "housing": 6085,
        "land_area": 33152371.0,
        "water_area": 38239.0
    },
    "20125": {
        "name": "Corinth",
        "state": "Corinth NY",
        "population": 3870,
        "housing": 2108,
        "land_area": 7666472.0,
        "water_area": 415864.0
    },
    "20152": {
        "name": "Cornelia--Baldwin",
        "state": "Cornelia--Baldwin GA",
        "population": 19489,
        "housing": 6859,
        "land_area": 50675336.0,
        "water_area": 246570.0
    },
    "20206": {
        "name": "Corning",
        "state": "Corning CA",
        "population": 8459,
        "housing": 3047,
        "land_area": 7891219.0,
        "water_area": 0.0
    },
    "20233": {
        "name": "Corning",
        "state": "Corning NY",
        "population": 19541,
        "housing": 9909,
        "land_area": 27919163.0,
        "water_area": 536596.0
    },
    "20247": {
        "name": "Corona de Tucson",
        "state": "Corona de Tucson AZ",
        "population": 7866,
        "housing": 2696,
        "land_area": 8889233.0,
        "water_area": 0.0
    },
    "20287": {
        "name": "Corpus Christi",
        "state": "Corpus Christi TX",
        "population": 339066,
        "housing": 143128,
        "land_area": 334817107.0,
        "water_area": 3706849.0
    },
    "20314": {
        "name": "Corry",
        "state": "Corry PA",
        "population": 6224,
        "housing": 2822,
        "land_area": 10778353.0,
        "water_area": 3552.0
    },
    "20341": {
        "name": "Corsicana",
        "state": "Corsicana TX",
        "population": 24380,
        "housing": 9346,
        "land_area": 40202339.0,
        "water_area": 346526.0
    },
    "20368": {
        "name": "Cortez",
        "state": "Cortez CO",
        "population": 8628,
        "housing": 3914,
        "land_area": 11001477.0,
        "water_area": 70393.0
    },
    "20395": {
        "name": "Cortland",
        "state": "Cortland NY",
        "population": 24866,
        "housing": 10897,
        "land_area": 26789698.0,
        "water_area": 121157.0
    },
    "20422": {
        "name": "Corvallis",
        "state": "Corvallis OR",
        "population": 66791,
        "housing": 28654,
        "land_area": 45203631.0,
        "water_area": 121295.0
    },
    "20442": {
        "name": "Corydon",
        "state": "Corydon IN",
        "population": 5696,
        "housing": 2509,
        "land_area": 9294847.0,
        "water_area": 0.0
    },
    "20476": {
        "name": "Coshocton",
        "state": "Coshocton OH",
        "population": 12334,
        "housing": 6012,
        "land_area": 20227058.0,
        "water_area": 155354.0
    },
    "20503": {
        "name": "Cottage Grove",
        "state": "Cottage Grove OR",
        "population": 11826,
        "housing": 4816,
        "land_area": 13593000.0,
        "water_area": 177162.0
    },
    "20595": {
        "name": "Cottonwood (Yavapai County)--Verde Village",
        "state": "Cottonwood (Yavapai County)--Verde Village AZ",
        "population": 29121,
        "housing": 13877,
        "land_area": 43787055.0,
        "water_area": 0.0
    },
    "20719": {
        "name": "Covington",
        "state": "Covington TN",
        "population": 7320,
        "housing": 3335,
        "land_area": 15533524.0,
        "water_area": 0.0
    },
    "20746": {
        "name": "Covington",
        "state": "Covington VA",
        "population": 7745,
        "housing": 4051,
        "land_area": 18976990.0,
        "water_area": 595459.0
    },
    "20773": {
        "name": "Coxsackie",
        "state": "Coxsackie NY",
        "population": 5384,
        "housing": 1685,
        "land_area": 9745358.0,
        "water_area": 37258.0
    },
    "20827": {
        "name": "Craig",
        "state": "Craig CO",
        "population": 9650,
        "housing": 4288,
        "land_area": 14247448.0,
        "water_area": 0.0
    },
    "20881": {
        "name": "Crawfordsville",
        "state": "Crawfordsville IN",
        "population": 17863,
        "housing": 8008,
        "land_area": 25306177.0,
        "water_area": 77556.0
    },
    "20890": {
        "name": "Crawfordville",
        "state": "Crawfordville FL",
        "population": 10124,
        "housing": 3912,
        "land_area": 25172054.0,
        "water_area": 0.0
    },
    "20899": {
        "name": "Creedmoor",
        "state": "Creedmoor NC",
        "population": 7482,
        "housing": 3022,
        "land_area": 17587508.0,
        "water_area": 84991.0
    },
    "20908": {
        "name": "Crescent City",
        "state": "Crescent City CA",
        "population": 15620,
        "housing": 6674,
        "land_area": 42311248.0,
        "water_area": 226006.0
    },
    "20980": {
        "name": "Cresson",
        "state": "Cresson PA",
        "population": 6512,
        "housing": 3002,
        "land_area": 10052866.0,
        "water_area": 0.0
    },
    "20985": {
        "name": "Crestline",
        "state": "Crestline OH",
        "population": 4597,
        "housing": 2213,
        "land_area": 8238992.0,
        "water_area": 13768.0
    },
    "20989": {
        "name": "Crestline--Lake Arrowhead",
        "state": "Crestline--Lake Arrowhead CA",
        "population": 22272,
        "housing": 17901,
        "land_area": 43643846.0,
        "water_area": 3478474.0
    },
    "21016": {
        "name": "Creston",
        "state": "Creston IA",
        "population": 7507,
        "housing": 3668,
        "land_area": 11693544.0,
        "water_area": 4916.0
    },
    "21043": {
        "name": "Crestview",
        "state": "Crestview FL",
        "population": 46816,
        "housing": 18409,
        "land_area": 103195003.0,
        "water_area": 1796281.0
    },
    "21070": {
        "name": "Creswell",
        "state": "Creswell OR",
        "population": 6137,
        "housing": 2358,
        "land_area": 6328496.0,
        "water_area": 42826.0
    },
    "21097": {
        "name": "Crete",
        "state": "Crete NE",
        "population": 6959,
        "housing": 2411,
        "land_area": 6741838.0,
        "water_area": 80171.0
    },
    "21151": {
        "name": "Crisfield",
        "state": "Crisfield MD",
        "population": 3509,
        "housing": 2036,
        "land_area": 6963110.0,
        "water_area": 396409.0
    },
    "21205": {
        "name": "Crockett",
        "state": "Crockett TX",
        "population": 5935,
        "housing": 2826,
        "land_area": 16989948.0,
        "water_area": 0.0
    },
    "21232": {
        "name": "Crookston",
        "state": "Crookston MN",
        "population": 7618,
        "housing": 3441,
        "land_area": 12039712.0,
        "water_area": 0.0
    },
    "21340": {
        "name": "Crossett",
        "state": "Crossett AR",
        "population": 7184,
        "housing": 3550,
        "land_area": 23335570.0,
        "water_area": 470878.0
    },
    "21394": {
        "name": "Crossville",
        "state": "Crossville TN",
        "population": 19949,
        "housing": 9868,
        "land_area": 69413715.0,
        "water_area": 3012365.0
    },
    "21421": {
        "name": "Crowley",
        "state": "Crowley LA",
        "population": 13168,
        "housing": 6429,
        "land_area": 18349470.0,
        "water_area": 17630.0
    },
    "21448": {
        "name": "Crozet",
        "state": "Crozet VA",
        "population": 9378,
        "housing": 3779,
        "land_area": 11622504.0,
        "water_area": 68900.0
    },
    "21475": {
        "name": "Cruz Bay",
        "state": "Cruz Bay VI",
        "population": 2964,
        "housing": 2681,
        "land_area": 8045975.0,
        "water_area": 45755.0
    },
    "21502": {
        "name": "Crystal City",
        "state": "Crystal City TX",
        "population": 6709,
        "housing": 2670,
        "land_area": 9172766.0,
        "water_area": 11147.0
    },
    "21529": {
        "name": "Crystal River",
        "state": "Crystal River FL",
        "population": 7834,
        "housing": 4847,
        "land_area": 36480222.0,
        "water_area": 2979925.0
    },
    "21556": {
        "name": "Crystal Springs",
        "state": "Crystal Springs MS",
        "population": 5057,
        "housing": 2151,
        "land_area": 10245447.0,
        "water_area": 25197.0
    },
    "21610": {
        "name": "Cuero",
        "state": "Cuero TX",
        "population": 7619,
        "housing": 3020,
        "land_area": 11304553.0,
        "water_area": 45653.0
    },
    "21637": {
        "name": "Cullman",
        "state": "Cullman AL",
        "population": 21165,
        "housing": 9371,
        "land_area": 55661380.0,
        "water_area": 455574.0
    },
    "21664": {
        "name": "Cullowhee",
        "state": "Cullowhee NC",
        "population": 9134,
        "housing": 3147,
        "land_area": 15791969.0,
        "water_area": 0.0
    },
    "21691": {
        "name": "Culpeper",
        "state": "Culpeper VA",
        "population": 22563,
        "housing": 8059,
        "land_area": 24269595.0,
        "water_area": 64317.0
    },
    "21745": {
        "name": "Cumberland",
        "state": "Cumberland MD--WV--PA",
        "population": 46296,
        "housing": 22834,
        "land_area": 82089280.0,
        "water_area": 680362.0
    },
    "21772": {
        "name": "Cushing",
        "state": "Cushing OK",
        "population": 6595,
        "housing": 3332,
        "land_area": 24117087.0,
        "water_area": 30090.0
    },
    "21853": {
        "name": "Cynthiana",
        "state": "Cynthiana KY",
        "population": 6393,
        "housing": 2952,
        "land_area": 7740229.0,
        "water_area": 143012.0
    },
    "21893": {
        "name": "Dade City",
        "state": "Dade City FL",
        "population": 20304,
        "housing": 7856,
        "land_area": 37288780.0,
        "water_area": 973310.0
    },
    "21934": {
        "name": "Dahlonega",
        "state": "Dahlonega GA",
        "population": 6508,
        "housing": 1403,
        "land_area": 8151821.0,
        "water_area": 249.0
    },
    "21988": {
        "name": "Dalhart",
        "state": "Dalhart TX",
        "population": 8352,
        "housing": 3489,
        "land_area": 9061417.0,
        "water_area": 4719.0
    },
    "22015": {
        "name": "Dallas",
        "state": "Dallas OR",
        "population": 17625,
        "housing": 7189,
        "land_area": 14233477.0,
        "water_area": 0.0
    },
    "22042": {
        "name": "Dallas--Fort Worth--Arlington",
        "state": "Dallas--Fort Worth--Arlington TX",
        "population": 5732354,
        "housing": 2243270,
        "land_area": 4524441010.0,
        "water_area": 64147875.0
    },
    "22069": {
        "name": "Dalton",
        "state": "Dalton GA",
        "population": 67830,
        "housing": 25333,
        "land_area": 148895536.0,
        "water_area": 200226.0
    },
    "22096": {
        "name": "Danbury",
        "state": "Danbury CT--NY",
        "population": 171680,
        "housing": 68643,
        "land_area": 306884173.0,
        "water_area": 15585535.0
    },
    "22177": {
        "name": "Dansville",
        "state": "Dansville NY",
        "population": 4806,
        "housing": 2321,
        "land_area": 8275503.0,
        "water_area": 0.0
    },
    "22204": {
        "name": "Danville",
        "state": "Danville IL",
        "population": 40044,
        "housing": 18786,
        "land_area": 71985501.0,
        "water_area": 2231770.0
    },
    "22231": {
        "name": "Danville",
        "state": "Danville KY",
        "population": 19814,
        "housing": 8752,
        "land_area": 35260301.0,
        "water_area": 135784.0
    },
    "22253": {
        "name": "Danville",
        "state": "Danville VA--NC",
        "population": 46683,
        "housing": 24055,
        "land_area": 94048065.0,
        "water_area": 1443948.0
    },
    "22260": {
        "name": "Danville--Mahoning",
        "state": "Danville--Mahoning PA",
        "population": 9771,
        "housing": 4698,
        "land_area": 13499941.0,
        "water_area": 44129.0
    },
    "22366": {
        "name": "Davenport",
        "state": "Davenport IA--IL",
        "population": 285211,
        "housing": 130167,
        "land_area": 349283232.0,
        "water_area": 19693324.0
    },
    "22420": {
        "name": "Davis",
        "state": "Davis CA",
        "population": 77034,
        "housing": 29345,
        "land_area": 31500260.0,
        "water_area": 90331.0
    },
    "22528": {
        "name": "Dayton",
        "state": "Dayton OH",
        "population": 674046,
        "housing": 308659,
        "land_area": 828517063.0,
        "water_area": 6799497.0
    },
    "22555": {
        "name": "Dayton",
        "state": "Dayton TN",
        "population": 9688,
        "housing": 4259,
        "land_area": 23619178.0,
        "water_area": 613490.0
    },
    "22582": {
        "name": "Dayton",
        "state": "Dayton TX",
        "population": 6879,
        "housing": 2908,
        "land_area": 14002995.0,
        "water_area": 0.0
    },
    "22612": {
        "name": "Daytona Beach--Palm Coast--Port Orange",
        "state": "Daytona Beach--Palm Coast--Port Orange FL",
        "population": 402126,
        "housing": 216962,
        "land_area": 550006605.0,
        "water_area": 31608090.0
    },
    "22616": {
        "name": "Dayton Northeast",
        "state": "Dayton Northeast NV",
        "population": 6248,
        "housing": 2375,
        "land_area": 9942625.0,
        "water_area": 0.0
    },
    "22622": {
        "name": "Dayton Southwest",
        "state": "Dayton Southwest NV",
        "population": 7547,
        "housing": 3184,
        "land_area": 11650600.0,
        "water_area": 3839.0
    },
    "22690": {
        "name": "Decatur",
        "state": "Decatur AL",
        "population": 60458,
        "housing": 26455,
        "land_area": 112586219.0,
        "water_area": 1051392.0
    },
    "22717": {
        "name": "Decatur",
        "state": "Decatur IL",
        "population": 86287,
        "housing": 42057,
        "land_area": 142726115.0,
        "water_area": 7596199.0
    },
    "22744": {
        "name": "Decatur",
        "state": "Decatur IN",
        "population": 10441,
        "housing": 4820,
        "land_area": 16986883.0,
        "water_area": 16644.0
    },
    "22771": {
        "name": "Decatur",
        "state": "Decatur TX",
        "population": 6486,
        "housing": 2608,
        "land_area": 16051644.0,
        "water_area": 0.0
    },
    "22798": {
        "name": "Decorah",
        "state": "Decorah IA",
        "population": 7993,
        "housing": 3384,
        "land_area": 13456481.0,
        "water_area": 111964.0
    },
    "22811": {
        "name": "Dededo--Apotgan--Tamuning",
        "state": "Dededo--Apotgan--Tamuning GU",
        "population": 128164,
        "housing": 43957,
        "land_area": 142866110.0,
        "water_area": 0.0
    },
    "22879": {
        "name": "Defiance",
        "state": "Defiance OH",
        "population": 17775,
        "housing": 7987,
        "land_area": 32034755.0,
        "water_area": 1386916.0
    },
    "22933": {
        "name": "DeFuniak Springs",
        "state": "DeFuniak Springs FL",
        "population": 6977,
        "housing": 3065,
        "land_area": 18569808.0,
        "water_area": 691086.0
    },
    "22960": {
        "name": "DeKalb",
        "state": "DeKalb IL",
        "population": 64736,
        "housing": 26985,
        "land_area": 66387396.0,
        "water_area": 626929.0
    },
    "22987": {
        "name": "Delano",
        "state": "Delano CA",
        "population": 44410,
        "housing": 11713,
        "land_area": 19076298.0,
        "water_area": 0.0
    },
    "22995": {
        "name": "Delano",
        "state": "Delano MN",
        "population": 6178,
        "housing": 2307,
        "land_area": 8010938.0,
        "water_area": 0.0
    },
    "23041": {
        "name": "Delavan",
        "state": "Delavan WI",
        "population": 12354,
        "housing": 6703,
        "land_area": 23555903.0,
        "water_area": 7135101.0
    },
    "23080": {
        "name": "Delhi",
        "state": "Delhi CA",
        "population": 10274,
        "housing": 2730,
        "land_area": 6061316.0,
        "water_area": 0.0
    },
    "23176": {
        "name": "Delphos",
        "state": "Delphos OH",
        "population": 7266,
        "housing": 3242,
        "land_area": 8901554.0,
        "water_area": 73578.0
    },
    "23203": {
        "name": "Del Rio",
        "state": "Del Rio TX",
        "population": 42680,
        "housing": 15789,
        "land_area": 48712813.0,
        "water_area": 215425.0
    },
    "23230": {
        "name": "Delta",
        "state": "Delta CO",
        "population": 8190,
        "housing": 3553,
        "land_area": 16126572.0,
        "water_area": 129608.0
    },
    "23311": {
        "name": "Deltona",
        "state": "Deltona FL",
        "population": 210712,
        "housing": 86104,
        "land_area": 282265175.0,
        "water_area": 12579177.0
    },
    "23338": {
        "name": "Deming",
        "state": "Deming NM",
        "population": 14913,
        "housing": 6492,
        "land_area": 21230088.0,
        "water_area": 0.0
    },
    "23365": {
        "name": "Demopolis",
        "state": "Demopolis AL",
        "population": 6227,
        "housing": 2986,
        "land_area": 11781694.0,
        "water_area": 82368.0
    },
    "23419": {
        "name": "Denison",
        "state": "Denison IA",
        "population": 8142,
        "housing": 3000,
        "land_area": 11637914.0,
        "water_area": 23959.0
    },
    "23473": {
        "name": "Denton",
        "state": "Denton MD",
        "population": 5009,
        "housing": 1941,
        "land_area": 8458496.0,
        "water_area": 92599.0
    },
    "23500": {
        "name": "Denton--Lewisville",
        "state": "Denton--Lewisville TX",
        "population": 429461,
        "housing": 166497,
        "land_area": 389741508.0,
        "water_area": 4912786.0
    },
    "23513": {
        "name": "Denton Southwest",
        "state": "Denton Southwest TX",
        "population": 14105,
        "housing": 5719,
        "land_area": 18291464.0,
        "water_area": 64122.0
    },
    "23527": {
        "name": "Denver--Aurora",
        "state": "Denver--Aurora CO",
        "population": 2686147,
        "housing": 1125043,
        "land_area": 1669352291.0,
        "water_area": 29334833.0
    },
    "23608": {
        "name": "De Queen",
        "state": "De Queen AR",
        "population": 5894,
        "housing": 2163,
        "land_area": 12462732.0,
        "water_area": 50899.0
    },
    "23662": {
        "name": "DeRidder",
        "state": "DeRidder LA",
        "population": 12126,
        "housing": 5563,
        "land_area": 37627641.0,
        "water_area": 224195.0
    },
    "23716": {
        "name": "Desert Hot Springs",
        "state": "Desert Hot Springs CA",
        "population": 45767,
        "housing": 18838,
        "land_area": 36465259.0,
        "water_area": 0.0
    },
    "23743": {
        "name": "Des Moines",
        "state": "Des Moines IA",
        "population": 542486,
        "housing": 232461,
        "land_area": 582080584.0,
        "water_area": 7226317.0
    },
    "23797": {
        "name": "De Soto",
        "state": "De Soto MO",
        "population": 7649,
        "housing": 3440,
        "land_area": 11394754.0,
        "water_area": 363295.0
    },
    "23824": {
        "name": "Detroit",
        "state": "Detroit MI",
        "population": 3776890,
        "housing": 1647476,
        "land_area": 3327686104.0,
        "water_area": 87241053.0
    },
    "23851": {
        "name": "Detroit Lakes",
        "state": "Detroit Lakes MN",
        "population": 10234,
        "housing": 5956,
        "land_area": 23834267.0,
        "water_area": 25717335.0
    },
    "23878": {
        "name": "Devils Lake",
        "state": "Devils Lake ND",
        "population": 7493,
        "housing": 3787,
        "land_area": 13136265.0,
        "water_area": 14898.0
    },
    "23986": {
        "name": "DeWitt",
        "state": "DeWitt IA",
        "population": 5162,
        "housing": 2250,
        "land_area": 10370231.0,
        "water_area": 0.0
    },
    "24008": {
        "name": "Dexter",
        "state": "Dexter MI",
        "population": 5285,
        "housing": 2155,
        "land_area": 6459898.0,
        "water_area": 3796.0
    },
    "24013": {
        "name": "Dexter",
        "state": "Dexter MO",
        "population": 9635,
        "housing": 4675,
        "land_area": 20056904.0,
        "water_area": 243643.0
    },
    "24040": {
        "name": "Diamondhead",
        "state": "Diamondhead MS",
        "population": 9044,
        "housing": 4380,
        "land_area": 13122331.0,
        "water_area": 224519.0
    },
    "24094": {
        "name": "Dickinson",
        "state": "Dickinson ND",
        "population": 25674,
        "housing": 11897,
        "land_area": 31747884.0,
        "water_area": 199866.0
    },
    "24121": {
        "name": "Dickson",
        "state": "Dickson TN",
        "population": 16543,
        "housing": 7239,
        "land_area": 42607380.0,
        "water_area": 163237.0
    },
    "24175": {
        "name": "Dillon",
        "state": "Dillon MT",
        "population": 4429,
        "housing": 2221,
        "land_area": 7115148.0,
        "water_area": 0.0
    },
    "24202": {
        "name": "Dillon",
        "state": "Dillon SC",
        "population": 8484,
        "housing": 3955,
        "land_area": 16395248.0,
        "water_area": 111569.0
    },
    "24256": {
        "name": "Discovery Bay",
        "state": "Discovery Bay CA",
        "population": 15939,
        "housing": 6271,
        "land_area": 10883779.0,
        "water_area": 2879325.0
    },
    "24283": {
        "name": "Dixon",
        "state": "Dixon CA",
        "population": 18876,
        "housing": 6524,
        "land_area": 11382169.0,
        "water_area": 6653.0
    },
    "24310": {
        "name": "Dixon",
        "state": "Dixon IL",
        "population": 15987,
        "housing": 6712,
        "land_area": 20246217.0,
        "water_area": 996920.0
    },
    "24337": {
        "name": "Dodge City",
        "state": "Dodge City KS",
        "population": 27702,
        "housing": 9853,
        "land_area": 28277230.0,
        "water_area": 332221.0
    },
    "24364": {
        "name": "Dodgeville",
        "state": "Dodgeville WI",
        "population": 4898,
        "housing": 2199,
        "land_area": 8430135.0,
        "water_area": 0.0
    },
    "24391": {
        "name": "Donaldsonville",
        "state": "Donaldsonville LA",
        "population": 12461,
        "housing": 5596,
        "land_area": 40529779.0,
        "water_area": 1506152.0
    },
    "24445": {
        "name": "Dos Palos",
        "state": "Dos Palos CA",
        "population": 7721,
        "housing": 2340,
        "land_area": 5938663.0,
        "water_area": 0.0
    },
    "24472": {
        "name": "Dothan",
        "state": "Dothan AL",
        "population": 72423,
        "housing": 33948,
        "land_area": 142674706.0,
        "water_area": 387556.0
    },
    "24499": {
        "name": "Douglas",
        "state": "Douglas AZ",
        "population": 16582,
        "housing": 6504,
        "land_area": 14195977.0,
        "water_area": 0.0
    },
    "24526": {
        "name": "Douglas",
        "state": "Douglas GA",
        "population": 14258,
        "housing": 6099,
        "land_area": 42977840.0,
        "water_area": 537942.0
    },
    "24539": {
        "name": "Douglas",
        "state": "Douglas MI",
        "population": 3259,
        "housing": 2651,
        "land_area": 10961033.0,
        "water_area": 1401554.0
    },
    "24553": {
        "name": "Douglas",
        "state": "Douglas WY",
        "population": 6498,
        "housing": 3120,
        "land_area": 8493607.0,
        "water_area": 0.0
    },
    "24580": {
        "name": "Dover",
        "state": "Dover DE",
        "population": 123101,
        "housing": 48756,
        "land_area": 187266900.0,
        "water_area": 2683061.0
    },
    "24607": {
        "name": "Dover--Rochester",
        "state": "Dover--Rochester NH--ME",
        "population": 72391,
        "housing": 33561,
        "land_area": 135487667.0,
        "water_area": 2690766.0
    },
    "24634": {
        "name": "Dowagiac",
        "state": "Dowagiac MI",
        "population": 5896,
        "housing": 2689,
        "land_area": 9212192.0,
        "water_area": 121550.0
    },
    "24715": {
        "name": "Dublin",
        "state": "Dublin GA",
        "population": 20842,
        "housing": 9337,
        "land_area": 52916176.0,
        "water_area": 425312.0
    },
    "24796": {
        "name": "DuBois",
        "state": "DuBois PA",
        "population": 11656,
        "housing": 5625,
        "land_area": 18542455.0,
        "water_area": 143022.0
    },
    "24823": {
        "name": "Dubuque",
        "state": "Dubuque IA--IL",
        "population": 70332,
        "housing": 31475,
        "land_area": 88492622.0,
        "water_area": 227949.0
    },
    "24850": {
        "name": "Duluth",
        "state": "Duluth MN--WI",
        "population": 119411,
        "housing": 55048,
        "land_area": 173182698.0,
        "water_area": 12812572.0
    },
    "24877": {
        "name": "Dumas",
        "state": "Dumas AR",
        "population": 4308,
        "housing": 2143,
        "land_area": 8046385.0,
        "water_area": 0.0
    },
    "24904": {
        "name": "Dumas",
        "state": "Dumas TX",
        "population": 14639,
        "housing": 5632,
        "land_area": 12787571.0,
        "water_area": 0.0
    },
    "24931": {
        "name": "Duncan",
        "state": "Duncan OK",
        "population": 20353,
        "housing": 10118,
        "land_area": 35251156.0,
        "water_area": 166641.0
    },
    "24958": {
        "name": "Dundee",
        "state": "Dundee MI",
        "population": 5252,
        "housing": 2314,
        "land_area": 10104534.0,
        "water_area": 95995.0
    },
    "25012": {
        "name": "Dunkirk--Fredonia",
        "state": "Dunkirk--Fredonia NY",
        "population": 23410,
        "housing": 10746,
        "land_area": 33031000.0,
        "water_area": 117554.0
    },
    "25039": {
        "name": "Dunn",
        "state": "Dunn NC",
        "population": 13707,
        "housing": 6504,
        "land_area": 28778425.0,
        "water_area": 136962.0
    },
    "25093": {
        "name": "Du Quoin",
        "state": "Du Quoin IL",
        "population": 5933,
        "housing": 2969,
        "land_area": 10516647.0,
        "water_area": 53516.0
    },
    "25120": {
        "name": "Durand",
        "state": "Durand MI",
        "population": 5056,
        "housing": 2354,
        "land_area": 9790978.0,
        "water_area": 123539.0
    },
    "25147": {
        "name": "Durango",
        "state": "Durango CO",
        "population": 19114,
        "housing": 9232,
        "land_area": 24477902.0,
        "water_area": 85392.0
    },
    "25201": {
        "name": "Durant",
        "state": "Durant OK",
        "population": 19324,
        "housing": 8259,
        "land_area": 31095467.0,
        "water_area": 41673.0
    },
    "25216": {
        "name": "Durham",
        "state": "Durham NH",
        "population": 12117,
        "housing": 2548,
        "land_area": 10453067.0,
        "water_area": 63609.0
    },
    "25228": {
        "name": "Durham",
        "state": "Durham NC",
        "population": 396118,
        "housing": 173410,
        "land_area": 474887900.0,
        "water_area": 3384320.0
    },
    "25237": {
        "name": "Duvall",
        "state": "Duvall WA",
        "population": 8165,
        "housing": 2840,
        "land_area": 6406787.0,
        "water_area": 3707.0
    },
    "25309": {
        "name": "Dyersburg",
        "state": "Dyersburg TN",
        "population": 16790,
        "housing": 7674,
        "land_area": 33607141.0,
        "water_area": 227443.0
    },
    "25390": {
        "name": "Eagle",
        "state": "Eagle CO",
        "population": 7419,
        "housing": 2640,
        "land_area": 8821345.0,
        "water_area": 45496.0
    },
    "25480": {
        "name": "Eagle Mountain",
        "state": "Eagle Mountain UT",
        "population": 10269,
        "housing": 2550,
        "land_area": 5234338.0,
        "water_area": 0.0
    },
    "25498": {
        "name": "Eagle Pass",
        "state": "Eagle Pass TX",
        "population": 54083,
        "housing": 18705,
        "land_area": 54765192.0,
        "water_area": 364285.0
    },
    "25579": {
        "name": "Earlimart",
        "state": "Earlimart CA",
        "population": 7470,
        "housing": 1883,
        "land_area": 3496392.0,
        "water_area": 0.0
    },
    "25598": {
        "name": "East Aurora",
        "state": "East Aurora NY",
        "population": 8765,
        "housing": 3997,
        "land_area": 15070063.0,
        "water_area": 43792.0
    },
    "25658": {
        "name": "East Hampton North--Springs--Northwest Harbor",
        "state": "East Hampton North--Springs--Northwest Harbor NY",
        "population": 21812,
        "housing": 15022,
        "land_area": 92856914.0,
        "water_area": 5870108.0
    },
    "25687": {
        "name": "East Liverpool",
        "state": "East Liverpool OH--WV--PA",
        "population": 21126,
        "housing": 10424,
        "land_area": 40213217.0,
        "water_area": 1629926.0
    },
    "25714": {
        "name": "Eastman",
        "state": "Eastman GA",
        "population": 6220,
        "housing": 2778,
        "land_area": 15441436.0,
        "water_area": 185369.0
    },
    "25741": {
        "name": "Easton",
        "state": "Easton MD",
        "population": 18033,
        "housing": 8357,
        "land_area": 28877915.0,
        "water_area": 343472.0
    },
    "25768": {
        "name": "East Palestine",
        "state": "East Palestine OH",
        "population": 4634,
        "housing": 2085,
        "land_area": 7528284.0,
        "water_area": 0.0
    },
    "25849": {
        "name": "East Stroudsburg--Stroudsburg",
        "state": "East Stroudsburg--Stroudsburg PA",
        "population": 47891,
        "housing": 19080,
        "land_area": 100846909.0,
        "water_area": 815695.0
    },
    "25876": {
        "name": "East Tawas",
        "state": "East Tawas MI",
        "population": 4844,
        "housing": 3004,
        "land_area": 12962372.0,
        "water_area": 0.0
    },
    "25903": {
        "name": "East Troy",
        "state": "East Troy WI",
        "population": 5309,
        "housing": 2453,
        "land_area": 10726094.0,
        "water_area": 1653078.0
    },
    "25930": {
        "name": "Eaton",
        "state": "Eaton CO",
        "population": 5823,
        "housing": 2174,
        "land_area": 5595173.0,
        "water_area": 0.0
    },
    "25957": {
        "name": "Eaton",
        "state": "Eaton OH",
        "population": 8067,
        "housing": 3860,
        "land_area": 11638626.0,
        "water_area": 1810.0
    },
    "25984": {
        "name": "Eaton Rapids",
        "state": "Eaton Rapids MI",
        "population": 5076,
        "housing": 2314,
        "land_area": 7263886.0,
        "water_area": 143061.0
    },
    "26038": {
        "name": "Eau Claire",
        "state": "Eau Claire WI",
        "population": 105475,
        "housing": 46055,
        "land_area": 161862928.0,
        "water_area": 10233969.0
    },
    "26065": {
        "name": "Ebensburg",
        "state": "Ebensburg PA",
        "population": 4880,
        "housing": 2350,
        "land_area": 6706721.0,
        "water_area": 31708.0
    },
    "26092": {
        "name": "Eden",
        "state": "Eden NC",
        "population": 16323,
        "housing": 8197,
        "land_area": 36869368.0,
        "water_area": 42294.0
    },
    "26119": {
        "name": "Edenton",
        "state": "Edenton NC",
        "population": 4329,
        "housing": 2361,
        "land_area": 9287029.0,
        "water_area": 178962.0
    },
    "26173": {
        "name": "Edgerton",
        "state": "Edgerton WI",
        "population": 8360,
        "housing": 4277,
        "land_area": 16032838.0,
        "water_area": 495146.0
    },
    "26227": {
        "name": "Edinboro",
        "state": "Edinboro PA",
        "population": 5730,
        "housing": 2627,
        "land_area": 6312077.0,
        "water_area": 194847.0
    },
    "26254": {
        "name": "Edna",
        "state": "Edna TX",
        "population": 5910,
        "housing": 2619,
        "land_area": 9569304.0,
        "water_area": 0.0
    },
    "26275": {
        "name": "Edwards--Avon",
        "state": "Edwards--Avon CO",
        "population": 16518,
        "housing": 11151,
        "land_area": 30052310.0,
        "water_area": 584367.0
    },
    "26308": {
        "name": "Effingham",
        "state": "Effingham IL",
        "population": 13990,
        "housing": 6588,
        "land_area": 35160390.0,
        "water_area": 75129.0
    },
    "26362": {
        "name": "Elberton",
        "state": "Elberton GA",
        "population": 5700,
        "housing": 2770,
        "land_area": 13004624.0,
        "water_area": 70348.0
    },
    "26375": {
        "name": "Elburn",
        "state": "Elburn IL",
        "population": 6395,
        "housing": 2334,
        "land_area": 9232591.0,
        "water_area": 0.0
    },
    "26389": {
        "name": "El Campo",
        "state": "El Campo TX",
        "population": 13286,
        "housing": 5171,
        "land_area": 20274190.0,
        "water_area": 19527.0
    },
    "26405": {
        "name": "El Centro",
        "state": "El Centro CA",
        "population": 74376,
        "housing": 24115,
        "land_area": 50120900.0,
        "water_area": 45518.0
    },
    "26422": {
        "name": "Eldersburg",
        "state": "Eldersburg MD",
        "population": 30486,
        "housing": 11346,
        "land_area": 47603413.0,
        "water_area": 474049.0
    },
    "26443": {
        "name": "Eldon",
        "state": "Eldon MO",
        "population": 4513,
        "housing": 2168,
        "land_area": 8645172.0,
        "water_area": 0.0
    },
    "26470": {
        "name": "El Dorado",
        "state": "El Dorado AR",
        "population": 18698,
        "housing": 8867,
        "land_area": 52478274.0,
        "water_area": 0.0
    },
    "26524": {
        "name": "El Dorado",
        "state": "El Dorado KS",
        "population": 12774,
        "housing": 5819,
        "land_area": 19285859.0,
        "water_area": 20061.0
    },
    "26659": {
        "name": "Elgin",
        "state": "Elgin TX",
        "population": 10779,
        "housing": 3823,
        "land_area": 12346592.0,
        "water_area": 0.0
    },
    "26686": {
        "name": "Elizabeth City",
        "state": "Elizabeth City NC",
        "population": 22834,
        "housing": 10393,
        "land_area": 42775666.0,
        "water_area": 23115.0
    },
    "26750": {
        "name": "Elizabethtown--Radcliff",
        "state": "Elizabethtown--Radcliff KY",
        "population": 76441,
        "housing": 32896,
        "land_area": 144587421.0,
        "water_area": 1299087.0
    },
    "26767": {
        "name": "Elk City",
        "state": "Elk City OK",
        "population": 11124,
        "housing": 5740,
        "land_area": 23342372.0,
        "water_area": 0.0
    },
    "26794": {
        "name": "Elkhart",
        "state": "Elkhart IN--MI",
        "population": 148199,
        "housing": 59094,
        "land_area": 245890129.0,
        "water_area": 8088284.0
    },
    "26821": {
        "name": "Elkhorn",
        "state": "Elkhorn WI",
        "population": 11876,
        "housing": 5034,
        "land_area": 17973247.0,
        "water_area": 144932.0
    },
    "26875": {
        "name": "Elkins",
        "state": "Elkins WV",
        "population": 11109,
        "housing": 5395,
        "land_area": 18442398.0,
        "water_area": 0.0
    },
    "26902": {
        "name": "Elko",
        "state": "Elko NV",
        "population": 21695,
        "housing": 9136,
        "land_area": 32529312.0,
        "water_area": 0.0
    },
    "26956": {
        "name": "Elkton",
        "state": "Elkton VA",
        "population": 5032,
        "housing": 2933,
        "land_area": 12446028.0,
        "water_area": 63976.0
    },
    "26983": {
        "name": "Ellensburg",
        "state": "Ellensburg WA",
        "population": 21518,
        "housing": 10092,
        "land_area": 22894467.0,
        "water_area": 55313.0
    },
    "27010": {
        "name": "Ellenville",
        "state": "Ellenville NY",
        "population": 7090,
        "housing": 2648,
        "land_area": 8555617.0,
        "water_area": 77769.0
    },
    "27037": {
        "name": "Ellijay",
        "state": "Ellijay GA",
        "population": 6738,
        "housing": 3738,
        "land_area": 34217672.0,
        "water_area": 291044.0
    },
    "27085": {
        "name": "Ellwood City",
        "state": "Ellwood City PA",
        "population": 13155,
        "housing": 6342,
        "land_area": 19061495.0,
        "water_area": 322979.0
    },
    "27118": {
        "name": "Elmira",
        "state": "Elmira NY",
        "population": 62468,
        "housing": 29533,
        "land_area": 82097640.0,
        "water_area": 1180585.0
    },
    "27253": {
        "name": "El Paso",
        "state": "El Paso TX--NM",
        "population": 854584,
        "housing": 315198,
        "land_area": 662734881.0,
        "water_area": 2555915.0
    },
    "27261": {
        "name": "El Paso de Robles (Paso Robles)--Atascadero",
        "state": "El Paso de Robles (Paso Robles)--Atascadero CA",
        "population": 67804,
        "housing": 27041,
        "land_area": 77603788.0,
        "water_area": 114779.0
    },
    "27280": {
        "name": "El Reno",
        "state": "El Reno OK",
        "population": 14346,
        "housing": 6252,
        "land_area": 22571973.0,
        "water_area": 12473.0
    },
    "27290": {
        "name": "Elsa",
        "state": "Elsa TX",
        "population": 12984,
        "housing": 4221,
        "land_area": 16201322.0,
        "water_area": 0.0
    },
    "27307": {
        "name": "Elwood",
        "state": "Elwood IN",
        "population": 9199,
        "housing": 4448,
        "land_area": 11996702.0,
        "water_area": 0.0
    },
    "27361": {
        "name": "Ely",
        "state": "Ely NV",
        "population": 4455,
        "housing": 2284,
        "land_area": 8018080.0,
        "water_area": 0.0
    },
    "27469": {
        "name": "Emmett",
        "state": "Emmett ID",
        "population": 10173,
        "housing": 4191,
        "land_area": 14841901.0,
        "water_area": 204462.0
    },
    "27523": {
        "name": "Emporia",
        "state": "Emporia KS",
        "population": 24082,
        "housing": 11224,
        "land_area": 28974487.0,
        "water_area": 210549.0
    },
    "27550": {
        "name": "Emporia",
        "state": "Emporia VA",
        "population": 6871,
        "housing": 3067,
        "land_area": 16944802.0,
        "water_area": 153998.0
    },
    "27631": {
        "name": "Enid",
        "state": "Enid OK",
        "population": 50194,
        "housing": 22482,
        "land_area": 76371567.0,
        "water_area": 37260.0
    },
    "27658": {
        "name": "Ennis",
        "state": "Ennis TX",
        "population": 19763,
        "housing": 7195,
        "land_area": 32161351.0,
        "water_area": 904179.0
    },
    "27685": {
        "name": "Enterprise",
        "state": "Enterprise AL",
        "population": 31258,
        "housing": 13725,
        "land_area": 61504880.0,
        "water_area": 151495.0
    },
    "27712": {
        "name": "Ephraim",
        "state": "Ephraim UT",
        "population": 5049,
        "housing": 1680,
        "land_area": 4521315.0,
        "water_area": 0.0
    },
    "27739": {
        "name": "Ephrata",
        "state": "Ephrata WA",
        "population": 8050,
        "housing": 3210,
        "land_area": 11064057.0,
        "water_area": 0.0
    },
    "27766": {
        "name": "Erie",
        "state": "Erie PA",
        "population": 187820,
        "housing": 85013,
        "land_area": 189948084.0,
        "water_area": 595822.0
    },
    "27793": {
        "name": "Erwin",
        "state": "Erwin TN",
        "population": 8678,
        "housing": 4077,
        "land_area": 15347733.0,
        "water_area": 12216.0
    },
    "27820": {
        "name": "Escalon",
        "state": "Escalon CA",
        "population": 7480,
        "housing": 2723,
        "land_area": 5148439.0,
        "water_area": 0.0
    },
    "27847": {
        "name": "Escanaba",
        "state": "Escanaba MI",
        "population": 21159,
        "housing": 10444,
        "land_area": 46782957.0,
        "water_area": 415095.0
    },
    "27874": {
        "name": "Espa\u00f1ola",
        "state": "Espa\u00f1ola NM",
        "population": 23931,
        "housing": 10382,
        "land_area": 52155628.0,
        "water_area": 422050.0
    },
    "27901": {
        "name": "Estacada",
        "state": "Estacada OR",
        "population": 5267,
        "housing": 1992,
        "land_area": 7746329.0,
        "water_area": 16758.0
    },
    "27928": {
        "name": "Estes Park",
        "state": "Estes Park CO",
        "population": 7907,
        "housing": 6112,
        "land_area": 31468729.0,
        "water_area": 701256.0
    },
    "27955": {
        "name": "Estherville",
        "state": "Estherville IA",
        "population": 5774,
        "housing": 2675,
        "land_area": 8936595.0,
        "water_area": 0.0
    },
    "28009": {
        "name": "Etowah",
        "state": "Etowah TN",
        "population": 4513,
        "housing": 2158,
        "land_area": 11668955.0,
        "water_area": 0.0
    },
    "28063": {
        "name": "Eudora",
        "state": "Eudora KS",
        "population": 6400,
        "housing": 2419,
        "land_area": 6785190.0,
        "water_area": 109922.0
    },
    "28090": {
        "name": "Eufaula",
        "state": "Eufaula AL--GA",
        "population": 9184,
        "housing": 4482,
        "land_area": 18010672.0,
        "water_area": 28095.0
    },
    "28117": {
        "name": "Eugene",
        "state": "Eugene OR",
        "population": 270179,
        "housing": 116321,
        "land_area": 190326261.0,
        "water_area": 164467.0
    },
    "28144": {
        "name": "Eunice",
        "state": "Eunice LA",
        "population": 10510,
        "housing": 4863,
        "land_area": 18473203.0,
        "water_area": 78619.0
    },
    "28198": {
        "name": "Eureka",
        "state": "Eureka CA",
        "population": 45951,
        "housing": 20603,
        "land_area": 48677864.0,
        "water_area": 3777810.0
    },
    "28225": {
        "name": "Eureka",
        "state": "Eureka IL",
        "population": 5401,
        "housing": 2216,
        "land_area": 6532024.0,
        "water_area": 124341.0
    },
    "28262": {
        "name": "Eureka",
        "state": "Eureka MO",
        "population": 14027,
        "housing": 4870,
        "land_area": 24177257.0,
        "water_area": 43957.0
    },
    "28306": {
        "name": "Evanston",
        "state": "Evanston WY",
        "population": 11416,
        "housing": 5057,
        "land_area": 19875338.0,
        "water_area": 114902.0
    },
    "28333": {
        "name": "Evansville",
        "state": "Evansville IN",
        "population": 206855,
        "housing": 94932,
        "land_area": 292028104.0,
        "water_area": 2911523.0
    },
    "28360": {
        "name": "Evansville",
        "state": "Evansville WI",
        "population": 6321,
        "housing": 2620,
        "land_area": 7260009.0,
        "water_area": 15.0
    },
    "28441": {
        "name": "Evergreen",
        "state": "Evergreen CO",
        "population": 10218,
        "housing": 4424,
        "land_area": 24502090.0,
        "water_area": 219473.0
    },
    "28495": {
        "name": "Excelsior Springs",
        "state": "Excelsior Springs MO",
        "population": 9840,
        "housing": 4435,
        "land_area": 14763042.0,
        "water_area": 0.0
    },
    "28506": {
        "name": "Exeter",
        "state": "Exeter CA",
        "population": 10973,
        "housing": 3909,
        "land_area": 7432151.0,
        "water_area": 0.0
    },
    "28511": {
        "name": "Exeter",
        "state": "Exeter NH",
        "population": 16165,
        "housing": 7629,
        "land_area": 30385804.0,
        "water_area": 317039.0
    },
    "28522": {
        "name": "Fabens",
        "state": "Fabens TX",
        "population": 7094,
        "housing": 2476,
        "land_area": 4379984.0,
        "water_area": 58739.0
    },
    "28549": {
        "name": "Fairbanks",
        "state": "Fairbanks AK",
        "population": 71396,
        "housing": 30180,
        "land_area": 192442666.0,
        "water_area": 6579721.0
    },
    "28603": {
        "name": "Fairbury",
        "state": "Fairbury NE",
        "population": 4011,
        "housing": 2022,
        "land_area": 6112076.0,
        "water_area": 10793.0
    },
    "28657": {
        "name": "Fairfield",
        "state": "Fairfield CA",
        "population": 150122,
        "housing": 50402,
        "land_area": 105694640.0,
        "water_area": 620103.0
    },
    "28684": {
        "name": "Fairfield",
        "state": "Fairfield IL",
        "population": 4766,
        "housing": 2540,
        "land_area": 8447586.0,
        "water_area": 63580.0
    },
    "28711": {
        "name": "Fairfield",
        "state": "Fairfield IA",
        "population": 9211,
        "housing": 4743,
        "land_area": 13943090.0,
        "water_area": 118241.0
    },
    "28765": {
        "name": "Fairfield Glade",
        "state": "Fairfield Glade TN",
        "population": 8212,
        "housing": 5118,
        "land_area": 24954953.0,
        "water_area": 1035173.0
    },
    "28766": {
        "name": "Fairhope--Daphne",
        "state": "Fairhope--Daphne AL",
        "population": 76807,
        "housing": 33719,
        "land_area": 152977898.0,
        "water_area": 674017.0
    },
    "28846": {
        "name": "Fairmont",
        "state": "Fairmont MN",
        "population": 8387,
        "housing": 4180,
        "land_area": 9423560.0,
        "water_area": 986276.0
    },
    "28873": {
        "name": "Fairmont",
        "state": "Fairmont WV",
        "population": 31694,
        "housing": 15332,
        "land_area": 52287598.0,
        "water_area": 1822186.0
    },
    "28981": {
        "name": "Fajardo",
        "state": "Fajardo PR",
        "population": 68587,
        "housing": 40103,
        "land_area": 88989581.0,
        "water_area": 573209.0
    },
    "28995": {
        "name": "Falcon",
        "state": "Falcon CO",
        "population": 21348,
        "housing": 6627,
        "land_area": 21952794.0,
        "water_area": 0.0
    },
    "29008": {
        "name": "Falfurrias",
        "state": "Falfurrias TX",
        "population": 4497,
        "housing": 2027,
        "land_area": 5662317.0,
        "water_area": 0.0
    },
    "29024": {
        "name": "Fallbrook",
        "state": "Fallbrook CA",
        "population": 41305,
        "housing": 14606,
        "land_area": 67408147.0,
        "water_area": 72463.0
    },
    "29035": {
        "name": "Fallon",
        "state": "Fallon NV",
        "population": 16753,
        "housing": 7153,
        "land_area": 36159767.0,
        "water_area": 18327.0
    },
    "29062": {
        "name": "Falls City",
        "state": "Falls City NE",
        "population": 4133,
        "housing": 2148,
        "land_area": 8683749.0,
        "water_area": 12085.0
    },
    "29089": {
        "name": "Fargo",
        "state": "Fargo ND--MN",
        "population": 216214,
        "housing": 98798,
        "land_area": 201323267.0,
        "water_area": 295424.0
    },
    "29116": {
        "name": "Faribault",
        "state": "Faribault MN",
        "population": 24013,
        "housing": 8902,
        "land_area": 26523666.0,
        "water_area": 614025.0
    },
    "29251": {
        "name": "Farmington",
        "state": "Farmington MO",
        "population": 32804,
        "housing": 13603,
        "land_area": 46756573.0,
        "water_area": 104259.0
    },
    "29278": {
        "name": "Farmington",
        "state": "Farmington NM",
        "population": 51763,
        "housing": 20575,
        "land_area": 84492772.0,
        "water_area": 35476.0
    },
    "29305": {
        "name": "Farmville",
        "state": "Farmville NC",
        "population": 4380,
        "housing": 2204,
        "land_area": 7636071.0,
        "water_area": 0.0
    },
    "29332": {
        "name": "Farmville",
        "state": "Farmville VA",
        "population": 7916,
        "housing": 3005,
        "land_area": 16997941.0,
        "water_area": 291443.0
    },
    "29440": {
        "name": "Fayetteville",
        "state": "Fayetteville NC",
        "population": 325008,
        "housing": 137211,
        "land_area": 507440035.0,
        "water_area": 5309514.0
    },
    "29467": {
        "name": "Fayetteville",
        "state": "Fayetteville TN",
        "population": 10120,
        "housing": 4817,
        "land_area": 32205658.0,
        "water_area": 0.0
    },
    "29494": {
        "name": "Fayetteville--Springdale--Rogers",
        "state": "Fayetteville--Springdale--Rogers AR--MO",
        "population": 373687,
        "housing": 150509,
        "land_area": 513516214.0,
        "water_area": 6271423.0
    },
    "29555": {
        "name": "Fenton",
        "state": "Fenton MI",
        "population": 38156,
        "housing": 16869,
        "land_area": 76831372.0,
        "water_area": 14410391.0
    },
    "29575": {
        "name": "Fergus Falls",
        "state": "Fergus Falls MN",
        "population": 13116,
        "housing": 6302,
        "land_area": 19777470.0,
        "water_area": 1563786.0
    },
    "29608": {
        "name": "Fernandina Beach--Yulee",
        "state": "Fernandina Beach--Yulee FL",
        "population": 50805,
        "housing": 26223,
        "land_area": 130935030.0,
        "water_area": 2945658.0
    },
    "29656": {
        "name": "Fernley",
        "state": "Fernley NV",
        "population": 19233,
        "housing": 7298,
        "land_area": 27445794.0,
        "water_area": 0.0
    },
    "29683": {
        "name": "Ferriday",
        "state": "Ferriday LA",
        "population": 5169,
        "housing": 2213,
        "land_area": 12525970.0,
        "water_area": 0.0
    },
    "29710": {
        "name": "Fillmore",
        "state": "Fillmore CA",
        "population": 16397,
        "housing": 4726,
        "land_area": 6819111.0,
        "water_area": 0.0
    },
    "29737": {
        "name": "Findlay",
        "state": "Findlay OH",
        "population": 48144,
        "housing": 22745,
        "land_area": 66381098.0,
        "water_area": 409535.0
    },
    "29764": {
        "name": "Firebaugh",
        "state": "Firebaugh CA",
        "population": 8117,
        "housing": 2246,
        "land_area": 6443572.0,
        "water_area": 143103.0
    },
    "29769": {
        "name": "Fire Island",
        "state": "Fire Island NY",
        "population": 998,
        "housing": 3990,
        "land_area": 7418767.0,
        "water_area": 133374.0
    },
    "29775": {
        "name": "Firestone--Frederick",
        "state": "Firestone--Frederick CO",
        "population": 35447,
        "housing": 12207,
        "land_area": 39409127.0,
        "water_area": 505251.0
    },
    "29791": {
        "name": "Fitzgerald",
        "state": "Fitzgerald GA",
        "population": 11281,
        "housing": 5354,
        "land_area": 22024209.0,
        "water_area": 525156.0
    },
    "29818": {
        "name": "Flagstaff",
        "state": "Flagstaff AZ",
        "population": 79842,
        "housing": 32500,
        "land_area": 75789980.0,
        "water_area": 78821.0
    },
    "29846": {
        "name": "Flemington--Raritan",
        "state": "Flemington--Raritan NJ",
        "population": 24401,
        "housing": 9571,
        "land_area": 47639244.0,
        "water_area": 60341.0
    },
    "29872": {
        "name": "Flint",
        "state": "Flint MI",
        "population": 298964,
        "housing": 139045,
        "land_area": 532132381.0,
        "water_area": 4508268.0
    },
    "29899": {
        "name": "Flora",
        "state": "Flora IL",
        "population": 4793,
        "housing": 2261,
        "land_area": 8250056.0,
        "water_area": 0.0
    },
    "29953": {
        "name": "Florence",
        "state": "Florence AL",
        "population": 78925,
        "housing": 38442,
        "land_area": 141482263.0,
        "water_area": 367280.0
    },
    "29980": {
        "name": "Florence East",
        "state": "Florence East AZ",
        "population": 14049,
        "housing": 2796,
        "land_area": 6876834.0,
        "water_area": 0.0
    },
    "30034": {
        "name": "Florence",
        "state": "Florence OR",
        "population": 11477,
        "housing": 6674,
        "land_area": 20668683.0,
        "water_area": 40058.0
    },
    "30061": {
        "name": "Florence",
        "state": "Florence SC",
        "population": 89436,
        "housing": 40455,
        "land_area": 176218906.0,
        "water_area": 1465145.0
    },
    "30073": {
        "name": "Florence West",
        "state": "Florence West AZ",
        "population": 11636,
        "housing": 5032,
        "land_area": 15214599.0,
        "water_area": 0.0
    },
    "30088": {
        "name": "Floresville",
        "state": "Floresville TX",
        "population": 6313,
        "housing": 2449,
        "land_area": 9978132.0,
        "water_area": 15265.0
    },
    "30171": {
        "name": "Foley--Gulf Shores",
        "state": "Foley--Gulf Shores AL",
        "population": 40920,
        "housing": 26434,
        "land_area": 127027183.0,
        "water_area": 1097944.0
    },
    "30223": {
        "name": "Fond du Lac",
        "state": "Fond du Lac WI",
        "population": 54731,
        "housing": 24532,
        "land_area": 63947869.0,
        "water_area": 809532.0
    },
    "30232": {
        "name": "Fontana-on-Geneva Lake",
        "state": "Fontana-on-Geneva Lake WI",
        "population": 10466,
        "housing": 7382,
        "land_area": 26554779.0,
        "water_area": 8888006.0
    },
    "30331": {
        "name": "Forest City--Spindale",
        "state": "Forest City--Spindale NC",
        "population": 20760,
        "housing": 9937,
        "land_area": 69648245.0,
        "water_area": 190521.0
    },
    "30358": {
        "name": "Forest Lake",
        "state": "Forest Lake MN",
        "population": 21882,
        "housing": 9111,
        "land_area": 36293421.0,
        "water_area": 11370060.0
    },
    "30439": {
        "name": "Forney",
        "state": "Forney TX",
        "population": 41112,
        "housing": 13983,
        "land_area": 50965415.0,
        "water_area": 183299.0
    },
    "30466": {
        "name": "Forrest City",
        "state": "Forrest City AR",
        "population": 8557,
        "housing": 4074,
        "land_area": 18165350.0,
        "water_area": 50824.0
    },
    "30493": {
        "name": "Forsyth",
        "state": "Forsyth GA",
        "population": 4852,
        "housing": 2158,
        "land_area": 13472380.0,
        "water_area": 31627.0
    },
    "30520": {
        "name": "Forsyth",
        "state": "Forsyth MO",
        "population": 7423,
        "housing": 3724,
        "land_area": 17087197.0,
        "water_area": 43136.0
    },
    "30547": {
        "name": "Fort Atkinson",
        "state": "Fort Atkinson WI",
        "population": 13852,
        "housing": 6067,
        "land_area": 17880313.0,
        "water_area": 291750.0
    },
    "30574": {
        "name": "Fort Bragg",
        "state": "Fort Bragg CA",
        "population": 10668,
        "housing": 5124,
        "land_area": 25403271.0,
        "water_area": 355247.0
    },
    "30628": {
        "name": "Fort Collins",
        "state": "Fort Collins CO",
        "population": 326332,
        "housing": 138125,
        "land_area": 305559749.0,
        "water_area": 18260733.0
    },
    "30682": {
        "name": "Fort Dodge",
        "state": "Fort Dodge IA",
        "population": 24699,
        "housing": 11246,
        "land_area": 35262225.0,
        "water_area": 515550.0
    },
    "30736": {
        "name": "Fort Irwin",
        "state": "Fort Irwin CA",
        "population": 8096,
        "housing": 2462,
        "land_area": 9367563.0,
        "water_area": 0.0
    },
    "30748": {
        "name": "Fort Leonard Wood--St. Robert--Waynesville",
        "state": "Fort Leonard Wood--St. Robert--Waynesville MO",
        "population": 31672,
        "housing": 9536,
        "land_area": 67797845.0,
        "water_area": 21373.0
    },
    "30755": {
        "name": "Fort Lupton",
        "state": "Fort Lupton CO",
        "population": 7856,
        "housing": 2749,
        "land_area": 6035607.0,
        "water_area": 0.0
    },
    "30763": {
        "name": "Fort Madison",
        "state": "Fort Madison IA--IL",
        "population": 10278,
        "housing": 5081,
        "land_area": 15440349.0,
        "water_area": 0.0
    },
    "30790": {
        "name": "Fort Meade",
        "state": "Fort Meade FL",
        "population": 4874,
        "housing": 2381,
        "land_area": 6007348.0,
        "water_area": 68544.0
    },
    "30817": {
        "name": "Fort Morgan",
        "state": "Fort Morgan CO",
        "population": 13473,
        "housing": 5011,
        "land_area": 14352958.0,
        "water_area": 0.0
    },
    "30844": {
        "name": "Fort Payne",
        "state": "Fort Payne AL",
        "population": 8380,
        "housing": 3335,
        "land_area": 22392683.0,
        "water_area": 30508.0
    },
    "30871": {
        "name": "Fort Polk South",
        "state": "Fort Polk South LA",
        "population": 9983,
        "housing": 3622,
        "land_area": 24493975.0,
        "water_area": 65357.0
    },
    "30885": {
        "name": "Fort Rucker--Daleville",
        "state": "Fort Rucker--Daleville AL",
        "population": 7157,
        "housing": 3126,
        "land_area": 20113274.0,
        "water_area": 0.0
    },
    "30898": {
        "name": "Fort Scott",
        "state": "Fort Scott KS",
        "population": 7439,
        "housing": 3603,
        "land_area": 13215963.0,
        "water_area": 14336.0
    },
    "30925": {
        "name": "Fort Smith",
        "state": "Fort Smith AR--OK",
        "population": 125811,
        "housing": 55567,
        "land_area": 191590332.0,
        "water_area": 838677.0
    },
    "30952": {
        "name": "Fort Stockton",
        "state": "Fort Stockton TX",
        "population": 8551,
        "housing": 3658,
        "land_area": 15502282.0,
        "water_area": 0.0
    },
    "30979": {
        "name": "Fortuna",
        "state": "Fortuna CA",
        "population": 12784,
        "housing": 5408,
        "land_area": 15077301.0,
        "water_area": 1091.0
    },
    "31033": {
        "name": "Fort Valley",
        "state": "Fort Valley GA",
        "population": 9704,
        "housing": 4195,
        "land_area": 15048253.0,
        "water_area": 32622.0
    },
    "31087": {
        "name": "Fort Wayne",
        "state": "Fort Wayne IN",
        "population": 335934,
        "housing": 144476,
        "land_area": 423710136.0,
        "water_area": 699749.0
    },
    "31114": {
        "name": "Fostoria",
        "state": "Fostoria OH",
        "population": 14295,
        "housing": 6652,
        "land_area": 21518921.0,
        "water_area": 213934.0
    },
    "31150": {
        "name": "Four Corners",
        "state": "Four Corners FL",
        "population": 92396,
        "housing": 50820,
        "land_area": 218809161.0,
        "water_area": 10504530.0
    },
    "31195": {
        "name": "Frankenmuth",
        "state": "Frankenmuth MI",
        "population": 5045,
        "housing": 2475,
        "land_area": 7571326.0,
        "water_area": 99073.0
    },
    "31222": {
        "name": "Frankfort",
        "state": "Frankfort IN",
        "population": 16775,
        "housing": 6650,
        "land_area": 19388846.0,
        "water_area": 0.0
    },
    "31249": {
        "name": "Frankfort",
        "state": "Frankfort KY",
        "population": 37844,
        "housing": 18234,
        "land_area": 57830360.0,
        "water_area": 1336195.0
    },
    "31262": {
        "name": "Frankfort",
        "state": "Frankfort MI",
        "population": 2603,
        "housing": 2627,
        "land_area": 10974513.0,
        "water_area": 132291.0
    },
    "31276": {
        "name": "Franklin",
        "state": "Franklin KY",
        "population": 11597,
        "housing": 4976,
        "land_area": 21591964.0,
        "water_area": 46538.0
    },
    "31303": {
        "name": "Franklin",
        "state": "Franklin LA",
        "population": 9491,
        "housing": 4516,
        "land_area": 16048914.0,
        "water_area": 158097.0
    },
    "31330": {
        "name": "Franklin",
        "state": "Franklin NH",
        "population": 6659,
        "housing": 3080,
        "land_area": 10943840.0,
        "water_area": 537804.0
    },
    "31384": {
        "name": "Franklin",
        "state": "Franklin NC",
        "population": 9358,
        "housing": 5011,
        "land_area": 36169299.0,
        "water_area": 823959.0
    },
    "31411": {
        "name": "Franklin (Venango County)",
        "state": "Franklin (Venango County) PA",
        "population": 8500,
        "housing": 4324,
        "land_area": 14564456.0,
        "water_area": 74075.0
    },
    "31438": {
        "name": "Franklin",
        "state": "Franklin VA",
        "population": 8749,
        "housing": 4228,
        "land_area": 16677506.0,
        "water_area": 112306.0
    },
    "31478": {
        "name": "Fraser",
        "state": "Fraser CO",
        "population": 3178,
        "housing": 5385,
        "land_area": 11535390.0,
        "water_area": 12925.0
    },
    "31519": {
        "name": "Frederick",
        "state": "Frederick MD",
        "population": 176456,
        "housing": 68467,
        "land_area": 207956090.0,
        "water_area": 1761196.0
    },
    "31573": {
        "name": "Fredericksburg",
        "state": "Fredericksburg TX",
        "population": 11641,
        "housing": 6225,
        "land_area": 20276772.0,
        "water_area": 14339.0
    },
    "31600": {
        "name": "Fredericksburg",
        "state": "Fredericksburg VA",
        "population": 167679,
        "housing": 64150,
        "land_area": 232112733.0,
        "water_area": 3161931.0
    },
    "31627": {
        "name": "Fredericktown",
        "state": "Fredericktown MO",
        "population": 4986,
        "housing": 2187,
        "land_area": 8465897.0,
        "water_area": 16415.0
    },
    "31660": {
        "name": "Freeland",
        "state": "Freeland MI",
        "population": 7412,
        "housing": 2282,
        "land_area": 21912258.0,
        "water_area": 16987.0
    },
    "31668": {
        "name": "Freeland",
        "state": "Freeland PA",
        "population": 5754,
        "housing": 2753,
        "land_area": 4047235.0,
        "water_area": 0.0
    },
    "31672": {
        "name": "Freeland",
        "state": "Freeland WA",
        "population": 7907,
        "housing": 5367,
        "land_area": 31280459.0,
        "water_area": 347869.0
    },
    "31681": {
        "name": "Freeport",
        "state": "Freeport IL",
        "population": 24135,
        "housing": 11988,
        "land_area": 27507093.0,
        "water_area": 95514.0
    },
    "31735": {
        "name": "Fremont",
        "state": "Fremont MI",
        "population": 5165,
        "housing": 2426,
        "land_area": 9923736.0,
        "water_area": 161803.0
    },
    "31762": {
        "name": "Fremont",
        "state": "Fremont NE",
        "population": 28292,
        "housing": 11998,
        "land_area": 35666800.0,
        "water_area": 843304.0
    },
    "31789": {
        "name": "Fremont",
        "state": "Fremont OH",
        "population": 22175,
        "housing": 10492,
        "land_area": 34691920.0,
        "water_area": 942607.0
    },
    "31843": {
        "name": "Fresno",
        "state": "Fresno CA",
        "population": 717589,
        "housing": 247152,
        "land_area": 412096133.0,
        "water_area": 3226657.0
    },
    "31852": {
        "name": "Friday Harbor",
        "state": "Friday Harbor WA",
        "population": 3542,
        "housing": 2139,
        "land_area": 11459412.0,
        "water_area": 1223349.0
    },
    "31889": {
        "name": "Frisco",
        "state": "Frisco CO",
        "population": 3463,
        "housing": 3654,
        "land_area": 5575410.0,
        "water_area": 0.0
    },
    "31924": {
        "name": "Front Royal",
        "state": "Front Royal VA",
        "population": 16193,
        "housing": 6641,
        "land_area": 27731240.0,
        "water_area": 55503.0
    },
    "31978": {
        "name": "Frostproof",
        "state": "Frostproof FL",
        "population": 8092,
        "housing": 3668,
        "land_area": 19406603.0,
        "water_area": 5010513.0
    },
    "32032": {
        "name": "Fulton",
        "state": "Fulton KY--TN",
        "population": 4256,
        "housing": 2224,
        "land_area": 8175253.0,
        "water_area": 46670.0
    },
    "32086": {
        "name": "Fulton",
        "state": "Fulton MO",
        "population": 12479,
        "housing": 4682,
        "land_area": 22457715.0,
        "water_area": 235047.0
    },
    "32092": {
        "name": "Fulton",
        "state": "Fulton NY",
        "population": 12788,
        "housing": 5989,
        "land_area": 14846940.0,
        "water_area": 750997.0
    },
    "32113": {
        "name": "Gadsden",
        "state": "Gadsden AL",
        "population": 57975,
        "housing": 27550,
        "land_area": 158530841.0,
        "water_area": 5255544.0
    },
    "32140": {
        "name": "Gaffney",
        "state": "Gaffney SC",
        "population": 19042,
        "housing": 8718,
        "land_area": 39956377.0,
        "water_area": 124411.0
    },
    "32167": {
        "name": "Gainesville",
        "state": "Gainesville FL",
        "population": 213748,
        "housing": 95632,
        "land_area": 227134311.0,
        "water_area": 3483914.0
    },
    "32194": {
        "name": "Gainesville",
        "state": "Gainesville GA",
        "population": 164365,
        "housing": 62897,
        "land_area": 386251091.0,
        "water_area": 24468801.0
    },
    "32221": {
        "name": "Gainesville",
        "state": "Gainesville TX",
        "population": 16544,
        "housing": 6734,
        "land_area": 24748391.0,
        "water_area": 26645.0
    },
    "32248": {
        "name": "Galax",
        "state": "Galax VA",
        "population": 6767,
        "housing": 3271,
        "land_area": 17147499.0,
        "water_area": 66597.0
    },
    "32329": {
        "name": "Galesburg",
        "state": "Galesburg IL",
        "population": 33847,
        "housing": 15669,
        "land_area": 56781538.0,
        "water_area": 141394.0
    },
    "32356": {
        "name": "Galion",
        "state": "Galion OH",
        "population": 11364,
        "housing": 5541,
        "land_area": 16611074.0,
        "water_area": 40302.0
    },
    "32383": {
        "name": "Galliano--Larose--Cut Off",
        "state": "Galliano--Larose--Cut Off LA",
        "population": 20056,
        "housing": 8765,
        "land_area": 48427017.0,
        "water_area": 949595.0
    },
    "32437": {
        "name": "Gallup",
        "state": "Gallup NM",
        "population": 24448,
        "housing": 9158,
        "land_area": 35426426.0,
        "water_area": 0.0
    },
    "32451": {
        "name": "Galt",
        "state": "Galt CA",
        "population": 26618,
        "housing": 8744,
        "land_area": 18355452.0,
        "water_area": 0.0
    },
    "32491": {
        "name": "Galveston--Texas City",
        "state": "Galveston--Texas City TX",
        "population": 191863,
        "housing": 92177,
        "land_area": 282260597.0,
        "water_area": 7938926.0
    },
    "32506": {
        "name": "Garapan",
        "state": "Garapan MP",
        "population": 36921,
        "housing": 14519,
        "land_area": 44668702.0,
        "water_area": 185857.0
    },
    "32518": {
        "name": "Garden City",
        "state": "Garden City KS",
        "population": 30976,
        "housing": 11478,
        "land_area": 33020228.0,
        "water_area": 67931.0
    },
    "32572": {
        "name": "Gardnerville",
        "state": "Gardnerville NV",
        "population": 21338,
        "housing": 9599,
        "land_area": 32837770.0,
        "water_area": 49380.0
    },
    "32653": {
        "name": "Gastonia",
        "state": "Gastonia NC",
        "population": 176897,
        "housing": 76009,
        "land_area": 322657931.0,
        "water_area": 3452598.0
    },
    "32707": {
        "name": "Gatesville",
        "state": "Gatesville TX",
        "population": 15565,
        "housing": 4000,
        "land_area": 26353164.0,
        "water_area": 31120.0
    },
    "32734": {
        "name": "Gaylord--Bagley",
        "state": "Gaylord--Bagley MI",
        "population": 8476,
        "housing": 4616,
        "land_area": 26574084.0,
        "water_area": 8583506.0
    },
    "32761": {
        "name": "Geneseo",
        "state": "Geneseo IL",
        "population": 6435,
        "housing": 3093,
        "land_area": 9729518.0,
        "water_area": 0.0
    },
    "32788": {
        "name": "Geneseo",
        "state": "Geneseo NY",
        "population": 8025,
        "housing": 2387,
        "land_area": 6243396.0,
        "water_area": 0.0
    },
    "32842": {
        "name": "Geneva",
        "state": "Geneva NY",
        "population": 29572,
        "housing": 14251,
        "land_area": 43404760.0,
        "water_area": 751961.0
    },
    "32850": {
        "name": "Geneva",
        "state": "Geneva OH",
        "population": 7355,
        "housing": 3480,
        "land_area": 12516480.0,
        "water_area": 0.0
    },
    "32869": {
        "name": "Genoa",
        "state": "Genoa IL",
        "population": 5484,
        "housing": 2058,
        "land_area": 5685910.0,
        "water_area": 128733.0
    },
    "32950": {
        "name": "Georgetown",
        "state": "Georgetown DE",
        "population": 9921,
        "housing": 2777,
        "land_area": 12775601.0,
        "water_area": 0.0
    },
    "32977": {
        "name": "Georgetown",
        "state": "Georgetown KY",
        "population": 38912,
        "housing": 15654,
        "land_area": 39007124.0,
        "water_area": 394485.0
    },
    "33031": {
        "name": "Georgetown",
        "state": "Georgetown SC",
        "population": 11364,
        "housing": 5404,
        "land_area": 23946739.0,
        "water_area": 1284591.0
    },
    "33091": {
        "name": "Germantown",
        "state": "Germantown OH",
        "population": 5577,
        "housing": 2311,
        "land_area": 7202679.0,
        "water_area": 0.0
    },
    "33139": {
        "name": "Gettysburg--Cumberland",
        "state": "Gettysburg--Cumberland PA",
        "population": 14733,
        "housing": 6074,
        "land_area": 21421738.0,
        "water_area": 619984.0
    },
    "33220": {
        "name": "Gillespie",
        "state": "Gillespie IL",
        "population": 5037,
        "housing": 2430,
        "land_area": 7354379.0,
        "water_area": 355.0
    },
    "33247": {
        "name": "Gillette",
        "state": "Gillette WY",
        "population": 34422,
        "housing": 14532,
        "land_area": 50898673.0,
        "water_area": 71361.0
    },
    "33301": {
        "name": "Gilmer",
        "state": "Gilmer TX",
        "population": 5084,
        "housing": 2208,
        "land_area": 10594732.0,
        "water_area": 32501.0
    },
    "33328": {
        "name": "Gilroy--Morgan Hill",
        "state": "Gilroy--Morgan Hill CA",
        "population": 114833,
        "housing": 36785,
        "land_area": 110178871.0,
        "water_area": 0.0
    },
    "33436": {
        "name": "Glasgow",
        "state": "Glasgow KY",
        "population": 14849,
        "housing": 6973,
        "land_area": 30556418.0,
        "water_area": 63731.0
    },
    "33490": {
        "name": "Glencoe",
        "state": "Glencoe MN",
        "population": 5738,
        "housing": 2478,
        "land_area": 8271828.0,
        "water_area": 17159.0
    },
    "33517": {
        "name": "Glendive",
        "state": "Glendive MT",
        "population": 6675,
        "housing": 3217,
        "land_area": 14052773.0,
        "water_area": 30895.0
    },
    "33598": {
        "name": "Glens Falls",
        "state": "Glens Falls NY",
        "population": 71191,
        "housing": 35410,
        "land_area": 133689107.0,
        "water_area": 7809635.0
    },
    "33625": {
        "name": "Glenwood",
        "state": "Glenwood IA",
        "population": 5009,
        "housing": 2078,
        "land_area": 6224280.0,
        "water_area": 29962.0
    },
    "33640": {
        "name": "Glenwood",
        "state": "Glenwood MN",
        "population": 4202,
        "housing": 2464,
        "land_area": 10985992.0,
        "water_area": 0.0
    },
    "33652": {
        "name": "Glenwood Springs",
        "state": "Glenwood Springs CO",
        "population": 10889,
        "housing": 4602,
        "land_area": 14143467.0,
        "water_area": 0.0
    },
    "33679": {
        "name": "Globe",
        "state": "Globe AZ",
        "population": 12620,
        "housing": 6333,
        "land_area": 21079174.0,
        "water_area": 634.0
    },
    "33733": {
        "name": "Gloversville",
        "state": "Gloversville NY",
        "population": 26286,
        "housing": 13325,
        "land_area": 40237923.0,
        "water_area": 27058.0
    },
    "33742": {
        "name": "Gold Canyon",
        "state": "Gold Canyon AZ",
        "population": 9590,
        "housing": 6814,
        "land_area": 20381511.0,
        "water_area": 0.0
    },
    "33814": {
        "name": "Goldsboro",
        "state": "Goldsboro NC",
        "population": 54456,
        "housing": 25498,
        "land_area": 137683817.0,
        "water_area": 406435.0
    },
    "33841": {
        "name": "Gonzales",
        "state": "Gonzales CA",
        "population": 8682,
        "housing": 2105,
        "land_area": 3993806.0,
        "water_area": 8236.0
    },
    "33868": {
        "name": "Gonzales",
        "state": "Gonzales TX",
        "population": 6953,
        "housing": 2902,
        "land_area": 11282946.0,
        "water_area": 0.0
    },
    "33922": {
        "name": "Goodland",
        "state": "Goodland KS",
        "population": 4439,
        "housing": 2214,
        "land_area": 7237207.0,
        "water_area": 0.0
    },
    "33960": {
        "name": "Goodyear South",
        "state": "Goodyear South AZ",
        "population": 16042,
        "housing": 6587,
        "land_area": 24857623.0,
        "water_area": 0.0
    },
    "34042": {
        "name": "Governors Club",
        "state": "Governors Club NC",
        "population": 4967,
        "housing": 2427,
        "land_area": 9381705.0,
        "water_area": 25693.0
    },
    "34111": {
        "name": "Grafton",
        "state": "Grafton WV",
        "population": 4824,
        "housing": 2380,
        "land_area": 9024558.0,
        "water_area": 339000.0
    },
    "34122": {
        "name": "Grafton--Port Washington--Cedarburg",
        "state": "Grafton--Port Washington--Cedarburg WI",
        "population": 44086,
        "housing": 19802,
        "land_area": 59842221.0,
        "water_area": 929707.0
    },
    "34138": {
        "name": "Graham",
        "state": "Graham TX",
        "population": 8585,
        "housing": 3825,
        "land_area": 12174354.0,
        "water_area": 806.0
    },
    "34165": {
        "name": "Granbury",
        "state": "Granbury TX",
        "population": 29706,
        "housing": 14236,
        "land_area": 56633516.0,
        "water_area": 4745502.0
    },
    "34219": {
        "name": "Grand Forks",
        "state": "Grand Forks ND--MN",
        "population": 68160,
        "housing": 31492,
        "land_area": 68592818.0,
        "water_area": 239589.0
    },
    "34246": {
        "name": "Grand Island",
        "state": "Grand Island NE",
        "population": 55099,
        "housing": 21892,
        "land_area": 76667722.0,
        "water_area": 692822.0
    },
    "34273": {
        "name": "Grand Junction",
        "state": "Grand Junction CO",
        "population": 135973,
        "housing": 58584,
        "land_area": 195824841.0,
        "water_area": 3556413.0
    },
    "34286": {
        "name": "Grand Lake",
        "state": "Grand Lake CO",
        "population": 1801,
        "housing": 3048,
        "land_area": 13928267.0,
        "water_area": 149149.0
    },
    "34300": {
        "name": "Grand Rapids",
        "state": "Grand Rapids MI",
        "population": 605666,
        "housing": 245031,
        "land_area": 710585662.0,
        "water_area": 15897425.0
    },
    "34327": {
        "name": "Grand Rapids",
        "state": "Grand Rapids MN",
        "population": 10348,
        "housing": 4826,
        "land_area": 27076209.0,
        "water_area": 2149701.0
    },
    "34381": {
        "name": "Grandview",
        "state": "Grandview WA",
        "population": 11187,
        "housing": 3353,
        "land_area": 10684232.0,
        "water_area": 0.0
    },
    "34470": {
        "name": "Granite Falls",
        "state": "Granite Falls WA",
        "population": 6349,
        "housing": 2326,
        "land_area": 8021347.0,
        "water_area": 226391.0
    },
    "34489": {
        "name": "Grants",
        "state": "Grants NM",
        "population": 9972,
        "housing": 4544,
        "land_area": 15916285.0,
        "water_area": 0.0
    },
    "34516": {
        "name": "Grants Pass",
        "state": "Grants Pass OR",
        "population": 55724,
        "housing": 24348,
        "land_area": 78779843.0,
        "water_area": 2084700.0
    },
    "34543": {
        "name": "Grantsville",
        "state": "Grantsville UT",
        "population": 9598,
        "housing": 2958,
        "land_area": 11380863.0,
        "water_area": 0.0
    },
    "34597": {
        "name": "Grass Valley",
        "state": "Grass Valley CA",
        "population": 36720,
        "housing": 17313,
        "land_area": 76798301.0,
        "water_area": 53117.0
    },
    "34651": {
        "name": "Grayson",
        "state": "Grayson KY",
        "population": 5418,
        "housing": 2397,
        "land_area": 13474116.0,
        "water_area": 226747.0
    },
    "34705": {
        "name": "Great Bend",
        "state": "Great Bend KS",
        "population": 14766,
        "housing": 7127,
        "land_area": 16163457.0,
        "water_area": 269393.0
    },
    "34759": {
        "name": "Great Falls",
        "state": "Great Falls MT",
        "population": 67097,
        "housing": 30776,
        "land_area": 78488614.0,
        "water_area": 1825334.0
    },
    "34786": {
        "name": "Greeley",
        "state": "Greeley CO",
        "population": 137222,
        "housing": 50941,
        "land_area": 96603258.0,
        "water_area": 369187.0
    },
    "34813": {
        "name": "Green Bay",
        "state": "Green Bay WI",
        "population": 224156,
        "housing": 95658,
        "land_area": 294373919.0,
        "water_area": 7376524.0
    },
    "34840": {
        "name": "Greencastle",
        "state": "Greencastle IN",
        "population": 10190,
        "housing": 4035,
        "land_area": 12323751.0,
        "water_area": 132764.0
    },
    "34876": {
        "name": "Greendale--Lawrenceburg--Hidden Valley",
        "state": "Greendale--Lawrenceburg--Hidden Valley IN--OH",
        "population": 20087,
        "housing": 8512,
        "land_area": 48617014.0,
        "water_area": 1188352.0
    },
    "34894": {
        "name": "Greeneville",
        "state": "Greeneville TN",
        "population": 22919,
        "housing": 10199,
        "land_area": 56902377.0,
        "water_area": 12959.0
    },
    "34921": {
        "name": "Greenfield",
        "state": "Greenfield CA",
        "population": 18858,
        "housing": 4170,
        "land_area": 5537088.0,
        "water_area": 0.0
    },
    "34975": {
        "name": "Greenfield",
        "state": "Greenfield MA",
        "population": 22294,
        "housing": 11083,
        "land_area": 36909783.0,
        "water_area": 252044.0
    },
    "35002": {
        "name": "Greenfield",
        "state": "Greenfield OH",
        "population": 4771,
        "housing": 2242,
        "land_area": 4409821.0,
        "water_area": 0.0
    },
    "35083": {
        "name": "Green River",
        "state": "Green River WY",
        "population": 11873,
        "housing": 5057,
        "land_area": 14397456.0,
        "water_area": 207245.0
    },
    "35164": {
        "name": "Greensboro",
        "state": "Greensboro NC",
        "population": 338928,
        "housing": 148331,
        "land_area": 438503913.0,
        "water_area": 5631045.0
    },
    "35191": {
        "name": "Greensburg",
        "state": "Greensburg IN",
        "population": 12529,
        "housing": 5556,
        "land_area": 22655381.0,
        "water_area": 90945.0
    },
    "35245": {
        "name": "Green Valley",
        "state": "Green Valley AZ",
        "population": 37315,
        "housing": 23803,
        "land_area": 52226785.0,
        "water_area": 39434.0
    },
    "35272": {
        "name": "Greenville",
        "state": "Greenville AL",
        "population": 5823,
        "housing": 2890,
        "land_area": 14521137.0,
        "water_area": 0.0
    },
    "35299": {
        "name": "Greenville",
        "state": "Greenville IL",
        "population": 6765,
        "housing": 2262,
        "land_area": 7734384.0,
        "water_area": 0.0
    },
    "35309": {
        "name": "Greenville",
        "state": "Greenville KY",
        "population": 5516,
        "housing": 2578,
        "land_area": 14140638.0,
        "water_area": 251381.0
    },
    "35326": {
        "name": "Greenville",
        "state": "Greenville MI",
        "population": 10265,
        "housing": 4403,
        "land_area": 17204683.0,
        "water_area": 314787.0
    },
    "35353": {
        "name": "Greenville",
        "state": "Greenville MS",
        "population": 29267,
        "housing": 13687,
        "land_area": 43476654.0,
        "water_area": 59169.0
    },
    "35380": {
        "name": "Greenville",
        "state": "Greenville NC",
        "population": 120150,
        "housing": 58789,
        "land_area": 171917996.0,
        "water_area": 909392.0
    },
    "35407": {
        "name": "Greenville",
        "state": "Greenville OH",
        "population": 12983,
        "housing": 6653,
        "land_area": 17229989.0,
        "water_area": 160158.0
    },
    "35434": {
        "name": "Greenville",
        "state": "Greenville PA",
        "population": 10553,
        "housing": 4866,
        "land_area": 20543559.0,
        "water_area": 29203.0
    },
    "35461": {
        "name": "Greenville",
        "state": "Greenville SC",
        "population": 387271,
        "housing": 171025,
        "land_area": 679012990.0,
        "water_area": 5775708.0
    },
    "35488": {
        "name": "Greenville",
        "state": "Greenville TX",
        "population": 27054,
        "housing": 11244,
        "land_area": 44813281.0,
        "water_area": 62711.0
    },
    "35542": {
        "name": "Greenwood",
        "state": "Greenwood AR",
        "population": 9077,
        "housing": 3663,
        "land_area": 17124390.0,
        "water_area": 370594.0
    },
    "35569": {
        "name": "Greenwood",
        "state": "Greenwood MS",
        "population": 19475,
        "housing": 8661,
        "land_area": 27653571.0,
        "water_area": 71918.0
    },
    "35596": {
        "name": "Greenwood",
        "state": "Greenwood SC",
        "population": 41998,
        "housing": 18897,
        "land_area": 91994818.0,
        "water_area": 668785.0
    },
    "35623": {
        "name": "Grenada",
        "state": "Grenada MS",
        "population": 10276,
        "housing": 5021,
        "land_area": 18444545.0,
        "water_area": 0.0
    },
    "35677": {
        "name": "Gridley",
        "state": "Gridley CA",
        "population": 8653,
        "housing": 3056,
        "land_area": 10308731.0,
        "water_area": 0.0
    },
    "35688": {
        "name": "Griffin",
        "state": "Griffin GA",
        "population": 38311,
        "housing": 15772,
        "land_area": 72296416.0,
        "water_area": 982671.0
    },
    "35704": {
        "name": "Grinnell",
        "state": "Grinnell IA",
        "population": 9486,
        "housing": 4011,
        "land_area": 11226086.0,
        "water_area": 106334.0
    },
    "35716": {
        "name": "Grissom AFB",
        "state": "Grissom AFB IN",
        "population": 6856,
        "housing": 1598,
        "land_area": 11151968.0,
        "water_area": 6730.0
    },
    "35785": {
        "name": "Grove",
        "state": "Grove OK",
        "population": 7934,
        "housing": 4342,
        "land_area": 26384917.0,
        "water_area": 9493.0
    },
    "35812": {
        "name": "Grove City",
        "state": "Grove City PA",
        "population": 9830,
        "housing": 3623,
        "land_area": 15739529.0,
        "water_area": 7109.0
    },
    "35839": {
        "name": "Guadalupe",
        "state": "Guadalupe CA",
        "population": 8046,
        "housing": 2118,
        "land_area": 2958576.0,
        "water_area": 9150.0
    },
    "35852": {
        "name": "Gu\u00e1nica",
        "state": "Gu\u00e1nica PR",
        "population": 6972,
        "housing": 4607,
        "land_area": 7513844.0,
        "water_area": 7867.0
    },
    "35866": {
        "name": "Guayama",
        "state": "Guayama PR",
        "population": 52290,
        "housing": 27128,
        "land_area": 56374286.0,
        "water_area": 347757.0
    },
    "35920": {
        "name": "Gulfport--Biloxi",
        "state": "Gulfport--Biloxi MS",
        "population": 236344,
        "housing": 106428,
        "land_area": 436768275.0,
        "water_area": 31092363.0
    },
    "35974": {
        "name": "Gun Barrel City",
        "state": "Gun Barrel City TX",
        "population": 18309,
        "housing": 10004,
        "land_area": 47670862.0,
        "water_area": 62287.0
    },
    "36001": {
        "name": "Gunnison",
        "state": "Gunnison CO",
        "population": 7228,
        "housing": 3285,
        "land_area": 9468678.0,
        "water_area": 11725.0
    },
    "36055": {
        "name": "Gustine",
        "state": "Gustine CA",
        "population": 6128,
        "housing": 2147,
        "land_area": 3015849.0,
        "water_area": 0.0
    },
    "36082": {
        "name": "Guthrie",
        "state": "Guthrie OK",
        "population": 9312,
        "housing": 4145,
        "land_area": 17702146.0,
        "water_area": 0.0
    },
    "36109": {
        "name": "Guymon",
        "state": "Guymon OK",
        "population": 12516,
        "housing": 4629,
        "land_area": 12471338.0,
        "water_area": 0.0
    },
    "36136": {
        "name": "Gypsum",
        "state": "Gypsum CO",
        "population": 8841,
        "housing": 2825,
        "land_area": 14962928.0,
        "water_area": 291101.0
    },
    "36190": {
        "name": "Hagerstown",
        "state": "Hagerstown MD--WV--PA--VA",
        "population": 197557,
        "housing": 81924,
        "land_area": 312789828.0,
        "water_area": 196300.0
    },
    "36217": {
        "name": "Hailey",
        "state": "Hailey ID",
        "population": 12035,
        "housing": 4877,
        "land_area": 13646439.0,
        "water_area": 13358.0
    },
    "36271": {
        "name": "Half Moon Bay",
        "state": "Half Moon Bay CA",
        "population": 21688,
        "housing": 8713,
        "land_area": 21619790.0,
        "water_area": 200.0
    },
    "36384": {
        "name": "Hamburg--Vernon--Highland Lakes",
        "state": "Hamburg--Vernon--Highland Lakes NJ",
        "population": 28250,
        "housing": 13840,
        "land_area": 56476586.0,
        "water_area": 2471909.0
    },
    "36406": {
        "name": "Hamilton",
        "state": "Hamilton MT",
        "population": 6870,
        "housing": 3490,
        "land_area": 10305114.0,
        "water_area": 9471.0
    },
    "36514": {
        "name": "Hammond",
        "state": "Hammond LA",
        "population": 72526,
        "housing": 31199,
        "land_area": 197927665.0,
        "water_area": 717662.0
    },
    "36541": {
        "name": "Hammonton",
        "state": "Hammonton NJ",
        "population": 12086,
        "housing": 4870,
        "land_area": 21400078.0,
        "water_area": 373780.0
    },
    "36568": {
        "name": "Hampshire",
        "state": "Hampshire IL",
        "population": 5699,
        "housing": 2104,
        "land_area": 7057711.0,
        "water_area": 0.0
    },
    "36585": {
        "name": "Hampstead",
        "state": "Hampstead NC",
        "population": 23340,
        "housing": 16255,
        "land_area": 81452893.0,
        "water_area": 577117.0
    },
    "36595": {
        "name": "Hampstead--Manchester",
        "state": "Hampstead--Manchester MD",
        "population": 14542,
        "housing": 5697,
        "land_area": 20420164.0,
        "water_area": 25976.0
    },
    "36703": {
        "name": "Hanford",
        "state": "Hanford CA",
        "population": 66638,
        "housing": 22595,
        "land_area": 47217676.0,
        "water_area": 0.0
    },
    "36730": {
        "name": "Hannibal",
        "state": "Hannibal MO",
        "population": 17672,
        "housing": 8097,
        "land_area": 27454914.0,
        "water_area": 45318.0
    },
    "36784": {
        "name": "Hanover",
        "state": "Hanover PA",
        "population": 56712,
        "housing": 24313,
        "land_area": 66588828.0,
        "water_area": 118907.0
    },
    "36838": {
        "name": "Harlan",
        "state": "Harlan IA",
        "population": 4713,
        "housing": 2284,
        "land_area": 6729756.0,
        "water_area": 0.0
    },
    "36865": {
        "name": "Harlan",
        "state": "Harlan KY",
        "population": 6147,
        "housing": 3073,
        "land_area": 12437244.0,
        "water_area": 598920.0
    },
    "36892": {
        "name": "Harlingen",
        "state": "Harlingen TX",
        "population": 118838,
        "housing": 46951,
        "land_area": 140267207.0,
        "water_area": 1882097.0
    },
    "36946": {
        "name": "Harriman--Kingston--Rockwood",
        "state": "Harriman--Kingston--Rockwood TN",
        "population": 22348,
        "housing": 10813,
        "land_area": 78973325.0,
        "water_area": 396963.0
    },
    "36973": {
        "name": "Harrington",
        "state": "Harrington DE",
        "population": 4943,
        "housing": 2152,
        "land_area": 8705129.0,
        "water_area": 18927.0
    },
    "37000": {
        "name": "Harrisburg",
        "state": "Harrisburg IL",
        "population": 8283,
        "housing": 4154,
        "land_area": 13704358.0,
        "water_area": 81560.0
    },
    "37081": {
        "name": "Harrisburg",
        "state": "Harrisburg PA",
        "population": 490859,
        "housing": 212463,
        "land_area": 648143710.0,
        "water_area": 29253771.0
    },
    "37091": {
        "name": "Harrisburg",
        "state": "Harrisburg SD",
        "population": 6663,
        "housing": 2369,
        "land_area": 6334693.0,
        "water_area": 0.0
    },
    "37108": {
        "name": "Harrison",
        "state": "Harrison AR",
        "population": 13950,
        "housing": 6799,
        "land_area": 32090921.0,
        "water_area": 74408.0
    },
    "37145": {
        "name": "Harrison",
        "state": "Harrison OH--IN",
        "population": 15007,
        "housing": 6162,
        "land_area": 19299515.0,
        "water_area": 74367.0
    },
    "37162": {
        "name": "Harrisonburg",
        "state": "Harrisonburg VA",
        "population": 73377,
        "housing": 27080,
        "land_area": 80557325.0,
        "water_area": 190942.0
    },
    "37189": {
        "name": "Harrisonville",
        "state": "Harrisonville MO",
        "population": 9423,
        "housing": 4034,
        "land_area": 17698345.0,
        "water_area": 91613.0
    },
    "37216": {
        "name": "Harrodsburg",
        "state": "Harrodsburg KY",
        "population": 9791,
        "housing": 4489,
        "land_area": 16905659.0,
        "water_area": 42574.0
    },
    "37243": {
        "name": "Hartford",
        "state": "Hartford CT",
        "population": 977158,
        "housing": 425056,
        "land_area": 1388045129.0,
        "water_area": 23641077.0
    },
    "37297": {
        "name": "Hartford",
        "state": "Hartford WI",
        "population": 23757,
        "housing": 10450,
        "land_area": 38523525.0,
        "water_area": 4011408.0
    },
    "37324": {
        "name": "Hartford City",
        "state": "Hartford City IN",
        "population": 6135,
        "housing": 3054,
        "land_area": 9120161.0,
        "water_area": 50358.0
    },
    "37351": {
        "name": "Hartselle",
        "state": "Hartselle AL",
        "population": 15596,
        "housing": 6619,
        "land_area": 36021661.0,
        "water_area": 138029.0
    },
    "37405": {
        "name": "Hartsville",
        "state": "Hartsville SC",
        "population": 13946,
        "housing": 6687,
        "land_area": 32844412.0,
        "water_area": 1173231.0
    },
    "37432": {
        "name": "Hartwell",
        "state": "Hartwell GA",
        "population": 5963,
        "housing": 2608,
        "land_area": 17543544.0,
        "water_area": 30670.0
    },
    "37459": {
        "name": "Harvard",
        "state": "Harvard IL",
        "population": 9376,
        "housing": 3278,
        "land_area": 11301822.0,
        "water_area": 0.0
    },
    "37513": {
        "name": "Hastings",
        "state": "Hastings MI",
        "population": 8041,
        "housing": 3558,
        "land_area": 12273779.0,
        "water_area": 918644.0
    },
    "37540": {
        "name": "Hastings",
        "state": "Hastings MN",
        "population": 21635,
        "housing": 9202,
        "land_area": 19070745.0,
        "water_area": 88223.0
    },
    "37567": {
        "name": "Hastings",
        "state": "Hastings NE",
        "population": 24807,
        "housing": 11175,
        "land_area": 33205915.0,
        "water_area": 372240.0
    },
    "37594": {
        "name": "Hattiesburg",
        "state": "Hattiesburg MS",
        "population": 80821,
        "housing": 35939,
        "land_area": 164772737.0,
        "water_area": 3865803.0
    },
    "37675": {
        "name": "Havelock",
        "state": "Havelock NC",
        "population": 17101,
        "housing": 6741,
        "land_area": 40744987.0,
        "water_area": 295668.0
    },
    "37702": {
        "name": "Havre",
        "state": "Havre MT",
        "population": 9826,
        "housing": 4702,
        "land_area": 10599176.0,
        "water_area": 1241.0
    },
    "37770": {
        "name": "Hayes",
        "state": "Hayes MI",
        "population": 3796,
        "housing": 2405,
        "land_area": 8415552.0,
        "water_area": 779727.0
    },
    "37783": {
        "name": "Hays",
        "state": "Hays KS",
        "population": 21880,
        "housing": 9934,
        "land_area": 25224957.0,
        "water_area": 0.0
    },
    "37837": {
        "name": "Hazard",
        "state": "Hazard KY",
        "population": 7808,
        "housing": 3664,
        "land_area": 20067659.0,
        "water_area": 776086.0
    },
    "37891": {
        "name": "Hazlehurst",
        "state": "Hazlehurst GA",
        "population": 4917,
        "housing": 2179,
        "land_area": 12808085.0,
        "water_area": 189963.0
    },
    "37945": {
        "name": "Hazleton",
        "state": "Hazleton PA",
        "population": 50860,
        "housing": 21110,
        "land_area": 49258949.0,
        "water_area": 60118.0
    },
    "37980": {
        "name": "Heartland",
        "state": "Heartland TX",
        "population": 9841,
        "housing": 3065,
        "land_area": 7165776.0,
        "water_area": 142320.0
    },
    "38053": {
        "name": "Heber",
        "state": "Heber UT",
        "population": 25059,
        "housing": 8634,
        "land_area": 34034230.0,
        "water_area": 0.0
    },
    "38060": {
        "name": "Heber-Overgaard",
        "state": "Heber-Overgaard AZ",
        "population": 3573,
        "housing": 4832,
        "land_area": 23887619.0,
        "water_area": 0.0
    },
    "38080": {
        "name": "Heber Springs",
        "state": "Heber Springs AR",
        "population": 6743,
        "housing": 3841,
        "land_area": 20132959.0,
        "water_area": 44302.0
    },
    "38161": {
        "name": "Helena",
        "state": "Helena MT",
        "population": 52380,
        "housing": 24037,
        "land_area": 82051726.0,
        "water_area": 181931.0
    },
    "38172": {
        "name": "Helena-West Helena",
        "state": "Helena-West Helena AR",
        "population": 8599,
        "housing": 4394,
        "land_area": 15827668.0,
        "water_area": 0.0
    },
    "38215": {
        "name": "Hemet",
        "state": "Hemet CA",
        "population": 173194,
        "housing": 61575,
        "land_area": 95979394.0,
        "water_area": 315049.0
    },
    "38242": {
        "name": "Hempstead",
        "state": "Hempstead TX",
        "population": 4890,
        "housing": 2095,
        "land_area": 7844261.0,
        "water_area": 2782.0
    },
    "38258": {
        "name": "Henderson",
        "state": "Henderson KY",
        "population": 28430,
        "housing": 13402,
        "land_area": 39523336.0,
        "water_area": 263905.0
    },
    "38269": {
        "name": "Henderson",
        "state": "Henderson NC",
        "population": 19894,
        "housing": 8880,
        "land_area": 42149864.0,
        "water_area": 75226.0
    },
    "38296": {
        "name": "Henderson",
        "state": "Henderson TN",
        "population": 5906,
        "housing": 2120,
        "land_area": 14008291.0,
        "water_area": 22914.0
    },
    "38323": {
        "name": "Henderson",
        "state": "Henderson TX",
        "population": 14924,
        "housing": 4608,
        "land_area": 27194749.0,
        "water_area": 142220.0
    },
    "38404": {
        "name": "Henryetta",
        "state": "Henryetta OK",
        "population": 6207,
        "housing": 2931,
        "land_area": 13518637.0,
        "water_area": 0.0
    },
    "38431": {
        "name": "Hereford",
        "state": "Hereford TX",
        "population": 15520,
        "housing": 5727,
        "land_area": 17280260.0,
        "water_area": 0.0
    },
    "38485": {
        "name": "Hermiston",
        "state": "Hermiston OR",
        "population": 28938,
        "housing": 9674,
        "land_area": 40090870.0,
        "water_area": 40332.0
    },
    "38620": {
        "name": "Hibbing",
        "state": "Hibbing MN",
        "population": 12035,
        "housing": 6319,
        "land_area": 17810946.0,
        "water_area": 28709.0
    },
    "38647": {
        "name": "Hickory",
        "state": "Hickory NC",
        "population": 201511,
        "housing": 89412,
        "land_area": 573260796.0,
        "water_area": 5389228.0
    },
    "38701": {
        "name": "Hidden Meadows",
        "state": "Hidden Meadows CA",
        "population": 4884,
        "housing": 2417,
        "land_area": 9724303.0,
        "water_area": 0.0
    },
    "38718": {
        "name": "Higgins Lake",
        "state": "Higgins Lake MI",
        "population": 2145,
        "housing": 3490,
        "land_area": 11719642.0,
        "water_area": 8374.0
    },
    "38728": {
        "name": "Higginsville",
        "state": "Higginsville MO",
        "population": 4551,
        "housing": 2081,
        "land_area": 5466690.0,
        "water_area": 37045.0
    },
    "38755": {
        "name": "Highland",
        "state": "Highland IL",
        "population": 10267,
        "housing": 4661,
        "land_area": 13754936.0,
        "water_area": 105545.0
    },
    "38809": {
        "name": "High Point",
        "state": "High Point NC",
        "population": 167830,
        "housing": 71478,
        "land_area": 261054371.0,
        "water_area": 4249361.0
    },
    "38863": {
        "name": "Hillsboro",
        "state": "Hillsboro IL",
        "population": 6880,
        "housing": 2390,
        "land_area": 10063611.0,
        "water_area": 125083.0
    },
    "38917": {
        "name": "Hillsboro",
        "state": "Hillsboro OH",
        "population": 6613,
        "housing": 3243,
        "land_area": 12503730.0,
        "water_area": 0.0
    },
    "38944": {
        "name": "Hillsboro",
        "state": "Hillsboro TX",
        "population": 8068,
        "housing": 3301,
        "land_area": 15403340.0,
        "water_area": 58410.0
    },
    "38978": {
        "name": "Hillsborough",
        "state": "Hillsborough NC",
        "population": 15800,
        "housing": 6802,
        "land_area": 28416453.0,
        "water_area": 355091.0
    },
    "38998": {
        "name": "Hillsdale",
        "state": "Hillsdale MI",
        "population": 10642,
        "housing": 4606,
        "land_area": 17278017.0,
        "water_area": 288400.0
    },
    "39052": {
        "name": "Hilo",
        "state": "Hilo HI",
        "population": 41410,
        "housing": 16878,
        "land_area": 60706834.0,
        "water_area": 217428.0
    },
    "39069": {
        "name": "Hilton",
        "state": "Hilton NY",
        "population": 8057,
        "housing": 3191,
        "land_area": 10435567.0,
        "water_area": 0.0
    },
    "39133": {
        "name": "Hinesville",
        "state": "Hinesville GA",
        "population": 53107,
        "housing": 21243,
        "land_area": 96427435.0,
        "water_area": 291852.0
    },
    "39214": {
        "name": "Hobbs",
        "state": "Hobbs NM",
        "population": 44157,
        "housing": 16787,
        "land_area": 67322683.0,
        "water_area": 131505.0
    },
    "39349": {
        "name": "Holden Beach",
        "state": "Holden Beach NC",
        "population": 8687,
        "housing": 9276,
        "land_area": 50887147.0,
        "water_area": 306474.0
    },
    "39376": {
        "name": "Holdenville",
        "state": "Holdenville OK",
        "population": 4264,
        "housing": 2029,
        "land_area": 6480702.0,
        "water_area": 20981.0
    },
    "39403": {
        "name": "Holdrege",
        "state": "Holdrege NE",
        "population": 5454,
        "housing": 2590,
        "land_area": 7607254.0,
        "water_area": 36417.0
    },
    "39430": {
        "name": "Holland",
        "state": "Holland MI",
        "population": 107034,
        "housing": 42839,
        "land_area": 167841136.0,
        "water_area": 8743597.0
    },
    "39511": {
        "name": "Hollister",
        "state": "Hollister CA",
        "population": 49611,
        "housing": 15163,
        "land_area": 34133557.0,
        "water_area": 28164.0
    },
    "39538": {
        "name": "Holly",
        "state": "Holly MI",
        "population": 8934,
        "housing": 3792,
        "land_area": 13175757.0,
        "water_area": 1040427.0
    },
    "39565": {
        "name": "Holly Springs",
        "state": "Holly Springs MS",
        "population": 5559,
        "housing": 2143,
        "land_area": 13772183.0,
        "water_area": 0.0
    },
    "39589": {
        "name": "Holts Summit",
        "state": "Holts Summit MO",
        "population": 5184,
        "housing": 2263,
        "land_area": 12272148.0,
        "water_area": 338350.0
    },
    "39619": {
        "name": "Holtville",
        "state": "Holtville CA",
        "population": 6230,
        "housing": 2009,
        "land_area": 4182641.0,
        "water_area": 11902.0
    },
    "39781": {
        "name": "Hondo",
        "state": "Hondo TX",
        "population": 6006,
        "housing": 2403,
        "land_area": 7863462.0,
        "water_area": 11836.0
    },
    "39835": {
        "name": "Honesdale",
        "state": "Honesdale PA",
        "population": 5404,
        "housing": 2960,
        "land_area": 10305903.0,
        "water_area": 546957.0
    },
    "39889": {
        "name": "Honolulu",
        "state": "Honolulu HI",
        "population": 853252,
        "housing": 315727,
        "land_area": 375480534.0,
        "water_area": 8571102.0
    },
    "39916": {
        "name": "Hood River",
        "state": "Hood River OR--WA",
        "population": 16171,
        "housing": 7297,
        "land_area": 24166848.0,
        "water_area": 15651.0
    },
    "39970": {
        "name": "Hoopeston",
        "state": "Hoopeston IL",
        "population": 4812,
        "housing": 2356,
        "land_area": 5851429.0,
        "water_area": 0.0
    },
    "40024": {
        "name": "Hope",
        "state": "Hope AR",
        "population": 8855,
        "housing": 4095,
        "land_area": 20376390.0,
        "water_area": 163144.0
    },
    "40078": {
        "name": "Hopkinsville",
        "state": "Hopkinsville KY",
        "population": 31696,
        "housing": 14731,
        "land_area": 59110368.0,
        "water_area": 285341.0
    },
    "40132": {
        "name": "Hornell",
        "state": "Hornell NY",
        "population": 10566,
        "housing": 5285,
        "land_area": 14960339.0,
        "water_area": 0.0
    },
    "40153": {
        "name": "Hornsby Bend",
        "state": "Hornsby Bend TX",
        "population": 11337,
        "housing": 3772,
        "land_area": 9835212.0,
        "water_area": 0.0
    },
    "40186": {
        "name": "Horse Cave",
        "state": "Horse Cave KY",
        "population": 4262,
        "housing": 2167,
        "land_area": 9567924.0,
        "water_area": 42693.0
    },
    "40189": {
        "name": "Horseshoe Bay",
        "state": "Horseshoe Bay TX",
        "population": 5583,
        "housing": 4331,
        "land_area": 21962211.0,
        "water_area": 259131.0
    },
    "40213": {
        "name": "Hot Springs",
        "state": "Hot Springs AR",
        "population": 59133,
        "housing": 31921,
        "land_area": 130215704.0,
        "water_area": 11993426.0
    },
    "40267": {
        "name": "Hot Springs Village",
        "state": "Hot Springs Village AR",
        "population": 12755,
        "housing": 7877,
        "land_area": 52135054.0,
        "water_area": 6156214.0
    },
    "40294": {
        "name": "Houghton--Hancock",
        "state": "Houghton--Hancock MI",
        "population": 15358,
        "housing": 5955,
        "land_area": 20579006.0,
        "water_area": 615840.0
    },
    "40321": {
        "name": "Houghton Lake",
        "state": "Houghton Lake MI",
        "population": 8521,
        "housing": 8859,
        "land_area": 28246270.0,
        "water_area": 835966.0
    },
    "40348": {
        "name": "Houlton",
        "state": "Houlton ME",
        "population": 4281,
        "housing": 2071,
        "land_area": 6486809.0,
        "water_area": 0.0
    },
    "40375": {
        "name": "Houma",
        "state": "Houma LA",
        "population": 145482,
        "housing": 61142,
        "land_area": 245761136.0,
        "water_area": 1621851.0
    },
    "40429": {
        "name": "Houston",
        "state": "Houston TX",
        "population": 5853575,
        "housing": 2232438,
        "land_area": 4539436477.0,
        "water_area": 66558913.0
    },
    "40537": {
        "name": "Hudson",
        "state": "Hudson NY",
        "population": 10610,
        "housing": 5886,
        "land_area": 16621164.0,
        "water_area": 143736.0
    },
    "40547": {
        "name": "Hudson",
        "state": "Hudson WI--MN",
        "population": 23743,
        "housing": 10292,
        "land_area": 35459028.0,
        "water_area": 1878352.0
    },
    "40564": {
        "name": "Hugo",
        "state": "Hugo OK",
        "population": 4992,
        "housing": 2508,
        "land_area": 10256320.0,
        "water_area": 25215.0
    },
    "40618": {
        "name": "Humboldt",
        "state": "Humboldt IA",
        "population": 5339,
        "housing": 2518,
        "land_area": 9491014.0,
        "water_area": 95051.0
    },
    "40645": {
        "name": "Humboldt",
        "state": "Humboldt TN",
        "population": 7160,
        "housing": 3661,
        "land_area": 13705610.0,
        "water_area": 0.0
    },
    "40672": {
        "name": "Huntingburg",
        "state": "Huntingburg IN",
        "population": 6117,
        "housing": 2541,
        "land_area": 9495275.0,
        "water_area": 74862.0
    },
    "40699": {
        "name": "Huntingdon",
        "state": "Huntingdon PA",
        "population": 11311,
        "housing": 3537,
        "land_area": 10669560.0,
        "water_area": 215459.0
    },
    "40726": {
        "name": "Huntington",
        "state": "Huntington IN",
        "population": 17555,
        "housing": 8013,
        "land_area": 25447591.0,
        "water_area": 444550.0
    },
    "40753": {
        "name": "Huntington",
        "state": "Huntington WV--KY--OH",
        "population": 200157,
        "housing": 94530,
        "land_area": 333783727.0,
        "water_area": 27113022.0
    },
    "40780": {
        "name": "Huntsville",
        "state": "Huntsville AL",
        "population": 329066,
        "housing": 145066,
        "land_area": 556245247.0,
        "water_area": 2626676.0
    },
    "40807": {
        "name": "Huntsville",
        "state": "Huntsville TX",
        "population": 43415,
        "housing": 15643,
        "land_area": 44021442.0,
        "water_area": 741871.0
    },
    "40813": {
        "name": "Huntsville Southeast",
        "state": "Huntsville Southeast AL",
        "population": 20165,
        "housing": 7538,
        "land_area": 27750195.0,
        "water_area": 98506.0
    },
    "40834": {
        "name": "Huron",
        "state": "Huron CA",
        "population": 6129,
        "housing": 1588,
        "land_area": 3351910.0,
        "water_area": 0.0
    },
    "40861": {
        "name": "Huron",
        "state": "Huron SD",
        "population": 14294,
        "housing": 6215,
        "land_area": 20437008.0,
        "water_area": 234275.0
    },
    "40888": {
        "name": "Hurricane",
        "state": "Hurricane UT",
        "population": 19370,
        "housing": 7645,
        "land_area": 26552392.0,
        "water_area": 0.0
    },
    "40915": {
        "name": "Hutchinson",
        "state": "Hutchinson KS",
        "population": 42475,
        "housing": 19892,
        "land_area": 63461156.0,
        "water_area": 375618.0
    },
    "40942": {
        "name": "Hutchinson",
        "state": "Hutchinson MN",
        "population": 14670,
        "housing": 6556,
        "land_area": 20935860.0,
        "water_area": 719906.0
    },
    "40969": {
        "name": "Idabel",
        "state": "Idabel OK",
        "population": 5523,
        "housing": 2508,
        "land_area": 10790237.0,
        "water_area": 37105.0
    },
    "40996": {
        "name": "Idaho Falls",
        "state": "Idaho Falls ID",
        "population": 105132,
        "housing": 38357,
        "land_area": 106858629.0,
        "water_area": 726738.0
    },
    "41023": {
        "name": "Ilion--Herkimer",
        "state": "Ilion--Herkimer NY",
        "population": 22267,
        "housing": 10680,
        "land_area": 21010105.0,
        "water_area": 79716.0
    },
    "41077": {
        "name": "Immokalee",
        "state": "Immokalee FL",
        "population": 23485,
        "housing": 6928,
        "land_area": 27454577.0,
        "water_area": 827081.0
    },
    "41100": {
        "name": "Incline Village",
        "state": "Incline Village NV--CA",
        "population": 19441,
        "housing": 19801,
        "land_area": 54645569.0,
        "water_area": 92291.0
    },
    "41131": {
        "name": "Independence",
        "state": "Independence IA",
        "population": 6057,
        "housing": 2833,
        "land_area": 12402431.0,
        "water_area": 185740.0
    },
    "41158": {
        "name": "Independence",
        "state": "Independence KS",
        "population": 8477,
        "housing": 4350,
        "land_area": 11917168.0,
        "water_area": 6752.0
    },
    "41185": {
        "name": "Indiana--White",
        "state": "Indiana--White PA",
        "population": 27693,
        "housing": 12704,
        "land_area": 35618451.0,
        "water_area": 100489.0
    },
    "41212": {
        "name": "Indianapolis",
        "state": "Indianapolis IN",
        "population": 1699881,
        "housing": 717732,
        "land_area": 1871391730.0,
        "water_area": 25868959.0
    },
    "41224": {
        "name": "Indian Head",
        "state": "Indian Head MD",
        "population": 5556,
        "housing": 2310,
        "land_area": 9641567.0,
        "water_area": 13287.0
    },
    "41239": {
        "name": "Indianola",
        "state": "Indianola IA",
        "population": 15344,
        "housing": 6226,
        "land_area": 22539488.0,
        "water_area": 0.0
    },
    "41266": {
        "name": "Indianola",
        "state": "Indianola MS",
        "population": 9341,
        "housing": 3810,
        "land_area": 13244435.0,
        "water_area": 225273.0
    },
    "41320": {
        "name": "Indiantown",
        "state": "Indiantown FL",
        "population": 5496,
        "housing": 1618,
        "land_area": 3863749.0,
        "water_area": 56559.0
    },
    "41347": {
        "name": "Indio--Palm Desert--Palm Springs",
        "state": "Indio--Palm Desert--Palm Springs CA",
        "population": 361075,
        "housing": 192446,
        "land_area": 393219428.0,
        "water_area": 3699330.0
    },
    "41365": {
        "name": "Inman",
        "state": "Inman SC",
        "population": 13269,
        "housing": 5440,
        "land_area": 35445694.0,
        "water_area": 66852.0
    },
    "41401": {
        "name": "International Falls",
        "state": "International Falls MN",
        "population": 6575,
        "housing": 3656,
        "land_area": 19133544.0,
        "water_area": 69464.0
    },
    "41482": {
        "name": "Iola",
        "state": "Iola KS",
        "population": 5845,
        "housing": 2907,
        "land_area": 10881654.0,
        "water_area": 120449.0
    },
    "41509": {
        "name": "Ione",
        "state": "Ione CA",
        "population": 4673,
        "housing": 2004,
        "land_area": 4380255.0,
        "water_area": 0.0
    },
    "41536": {
        "name": "Ionia",
        "state": "Ionia MI",
        "population": 15168,
        "housing": 3592,
        "land_area": 12730061.0,
        "water_area": 878.0
    },
    "41590": {
        "name": "Iowa City",
        "state": "Iowa City IA",
        "population": 126810,
        "housing": 55571,
        "land_area": 131659350.0,
        "water_area": 1350449.0
    },
    "41617": {
        "name": "Iowa Falls",
        "state": "Iowa Falls IA",
        "population": 5058,
        "housing": 2421,
        "land_area": 10534210.0,
        "water_area": 200898.0
    },
    "41644": {
        "name": "Iowa Park",
        "state": "Iowa Park TX",
        "population": 6454,
        "housing": 2785,
        "land_area": 7925550.0,
        "water_area": 187040.0
    },
    "41657": {
        "name": "Ipswich",
        "state": "Ipswich MA",
        "population": 9380,
        "housing": 4733,
        "land_area": 15740283.0,
        "water_area": 458954.0
    },
    "41671": {
        "name": "Iron Mountain--Kingsford",
        "state": "Iron Mountain--Kingsford MI--WI",
        "population": 18336,
        "housing": 9164,
        "land_area": 34140216.0,
        "water_area": 603248.0
    },
    "41752": {
        "name": "Ironwood",
        "state": "Ironwood MI--WI",
        "population": 7181,
        "housing": 4375,
        "land_area": 15645234.0,
        "water_area": 130062.0
    },
    "41806": {
        "name": "Irvine",
        "state": "Irvine KY",
        "population": 4029,
        "housing": 2021,
        "land_area": 7182930.0,
        "water_area": 0.0
    },
    "41820": {
        "name": "Isanti",
        "state": "Isanti MN",
        "population": 6621,
        "housing": 2454,
        "land_area": 8373906.0,
        "water_area": 78814.0
    },
    "41833": {
        "name": "Ishpeming",
        "state": "Ishpeming MI",
        "population": 11298,
        "housing": 5357,
        "land_area": 15125810.0,
        "water_area": 190940.0
    },
    "41914": {
        "name": "Ithaca",
        "state": "Ithaca NY",
        "population": 59102,
        "housing": 25031,
        "land_area": 63604468.0,
        "water_area": 546626.0
    },
    "42049": {
        "name": "Jackson",
        "state": "Jackson CA",
        "population": 7781,
        "housing": 3918,
        "land_area": 12491585.0,
        "water_area": 0.0
    },
    "42076": {
        "name": "Jackson",
        "state": "Jackson GA",
        "population": 5697,
        "housing": 2234,
        "land_area": 12215332.0,
        "water_area": 35351.0
    },
    "42157": {
        "name": "Jackson",
        "state": "Jackson MI",
        "population": 84307,
        "housing": 36028,
        "land_area": 134757083.0,
        "water_area": 3058216.0
    },
    "42211": {
        "name": "Jackson",
        "state": "Jackson MS",
        "population": 347693,
        "housing": 155654,
        "land_area": 614234278.0,
        "water_area": 6878645.0
    },
    "42238": {
        "name": "Jackson",
        "state": "Jackson OH",
        "population": 6749,
        "housing": 3307,
        "land_area": 10438219.0,
        "water_area": 0.0
    },
    "42265": {
        "name": "Jackson",
        "state": "Jackson TN",
        "population": 72809,
        "housing": 32121,
        "land_area": 123654940.0,
        "water_area": 22895.0
    },
    "42292": {
        "name": "Jackson",
        "state": "Jackson WI",
        "population": 7962,
        "housing": 3551,
        "land_area": 9334405.0,
        "water_area": 51899.0
    },
    "42319": {
        "name": "Jackson",
        "state": "Jackson WY",
        "population": 10760,
        "housing": 4930,
        "land_area": 7467850.0,
        "water_area": 77503.0
    },
    "42346": {
        "name": "Jacksonville",
        "state": "Jacksonville FL",
        "population": 1247374,
        "housing": 530649,
        "land_area": 1484772025.0,
        "water_area": 193597005.0
    },
    "42373": {
        "name": "Jacksonville",
        "state": "Jacksonville IL",
        "population": 21003,
        "housing": 9559,
        "land_area": 31658295.0,
        "water_area": 306226.0
    },
    "42400": {
        "name": "Jacksonville",
        "state": "Jacksonville NC",
        "population": 111224,
        "housing": 40962,
        "land_area": 195945428.0,
        "water_area": 1756988.0
    },
    "42427": {
        "name": "Jacksonville",
        "state": "Jacksonville TX",
        "population": 13881,
        "housing": 5546,
        "land_area": 27309380.0,
        "water_area": 0.0
    },
    "42481": {
        "name": "Jamestown",
        "state": "Jamestown NY",
        "population": 44424,
        "housing": 26313,
        "land_area": 68328429.0,
        "water_area": 404719.0
    },
    "42508": {
        "name": "Jamestown",
        "state": "Jamestown ND",
        "population": 15207,
        "housing": 7464,
        "land_area": 24356612.0,
        "water_area": 81976.0
    },
    "42562": {
        "name": "Janesville",
        "state": "Janesville WI",
        "population": 72285,
        "housing": 31455,
        "land_area": 94930540.0,
        "water_area": 1402703.0
    },
    "42616": {
        "name": "Jasper",
        "state": "Jasper AL",
        "population": 13274,
        "housing": 5781,
        "land_area": 43254699.0,
        "water_area": 84714.0
    },
    "42670": {
        "name": "Jasper",
        "state": "Jasper GA",
        "population": 6384,
        "housing": 2657,
        "land_area": 21266074.0,
        "water_area": 139907.0
    },
    "42697": {
        "name": "Jasper",
        "state": "Jasper IN",
        "population": 16749,
        "housing": 7203,
        "land_area": 29851597.0,
        "water_area": 161831.0
    },
    "42751": {
        "name": "Jasper",
        "state": "Jasper TX",
        "population": 7000,
        "housing": 3373,
        "land_area": 22111605.0,
        "water_area": 13407.0
    },
    "42778": {
        "name": "Jayuya",
        "state": "Jayuya PR",
        "population": 9987,
        "housing": 4338,
        "land_area": 22089392.0,
        "water_area": 378.0
    },
    "42805": {
        "name": "Jeanerette",
        "state": "Jeanerette LA",
        "population": 5325,
        "housing": 2484,
        "land_area": 7332330.0,
        "water_area": 155296.0
    },
    "42849": {
        "name": "Jefferson",
        "state": "Jefferson GA",
        "population": 11842,
        "housing": 4160,
        "land_area": 27633008.0,
        "water_area": 272435.0
    },
    "42952": {
        "name": "Jefferson",
        "state": "Jefferson WI",
        "population": 7566,
        "housing": 3329,
        "land_area": 11996404.0,
        "water_area": 676972.0
    },
    "42967": {
        "name": "Jefferson City",
        "state": "Jefferson City MO",
        "population": 50775,
        "housing": 23952,
        "land_area": 89244391.0,
        "water_area": 245212.0
    },
    "43021": {
        "name": "Jennings",
        "state": "Jennings LA",
        "population": 9378,
        "housing": 4081,
        "land_area": 19934482.0,
        "water_area": 46836.0
    },
    "43048": {
        "name": "Jerome",
        "state": "Jerome ID",
        "population": 12405,
        "housing": 4309,
        "land_area": 14147762.0,
        "water_area": 2037.0
    },
    "43064": {
        "name": "Jersey Shore",
        "state": "Jersey Shore PA",
        "population": 10009,
        "housing": 4469,
        "land_area": 18914545.0,
        "water_area": 155794.0
    },
    "43075": {
        "name": "Jerseyville",
        "state": "Jerseyville IL",
        "population": 8641,
        "housing": 3948,
        "land_area": 12352019.0,
        "water_area": 0.0
    },
    "43102": {
        "name": "Jesup",
        "state": "Jesup GA",
        "population": 12772,
        "housing": 4971,
        "land_area": 30827617.0,
        "water_area": 173492.0
    },
    "43115": {
        "name": "Jewett City",
        "state": "Jewett City CT",
        "population": 4706,
        "housing": 2210,
        "land_area": 6710431.0,
        "water_area": 618066.0
    },
    "43210": {
        "name": "Johnson City",
        "state": "Johnson City TN",
        "population": 128519,
        "housing": 60019,
        "land_area": 274613617.0,
        "water_area": 2659200.0
    },
    "43227": {
        "name": "Johnson Lane",
        "state": "Johnson Lane NV",
        "population": 5268,
        "housing": 2242,
        "land_area": 10734511.0,
        "water_area": 0.0
    },
    "43253": {
        "name": "Johnstown",
        "state": "Johnstown CO",
        "population": 19773,
        "housing": 6744,
        "land_area": 14867283.0,
        "water_area": 170858.0
    },
    "43264": {
        "name": "Johnstown",
        "state": "Johnstown OH",
        "population": 5449,
        "housing": 2317,
        "land_area": 7352250.0,
        "water_area": 58269.0
    },
    "43291": {
        "name": "Johnstown",
        "state": "Johnstown PA",
        "population": 61521,
        "housing": 32490,
        "land_area": 89862309.0,
        "water_area": 764050.0
    },
    "43345": {
        "name": "Jonesboro",
        "state": "Jonesboro AR",
        "population": 73781,
        "housing": 31507,
        "land_area": 120420945.0,
        "water_area": 367147.0
    },
    "43372": {
        "name": "Jonesboro",
        "state": "Jonesboro LA",
        "population": 5245,
        "housing": 2354,
        "land_area": 14770919.0,
        "water_area": 152071.0
    },
    "43399": {
        "name": "Joplin",
        "state": "Joplin MO",
        "population": 86679,
        "housing": 38706,
        "land_area": 156777192.0,
        "water_area": 286314.0
    },
    "43426": {
        "name": "Jordan",
        "state": "Jordan MN",
        "population": 6648,
        "housing": 2356,
        "land_area": 7738956.0,
        "water_area": 58600.0
    },
    "43439": {
        "name": "Joshua Tree",
        "state": "Joshua Tree CA",
        "population": 4370,
        "housing": 2525,
        "land_area": 9849596.0,
        "water_area": 0.0
    },
    "43453": {
        "name": "Juana D\u00edaz",
        "state": "Juana D\u00edaz PR",
        "population": 65023,
        "housing": 27385,
        "land_area": 80730047.0,
        "water_area": 3043938.0
    },
    "43507": {
        "name": "Junction City",
        "state": "Junction City KS",
        "population": 40723,
        "housing": 15632,
        "land_area": 62050414.0,
        "water_area": 582786.0
    },
    "43534": {
        "name": "Junction City",
        "state": "Junction City OR",
        "population": 7312,
        "housing": 2987,
        "land_area": 7315900.0,
        "water_area": 0.0
    },
    "43561": {
        "name": "Juneau",
        "state": "Juneau AK",
        "population": 24756,
        "housing": 10960,
        "land_area": 38274460.0,
        "water_area": 633165.0
    },
    "43615": {
        "name": "Kahului--Wailuku",
        "state": "Kahului--Wailuku HI",
        "population": 57905,
        "housing": 18348,
        "land_area": 35007714.0,
        "water_area": 110659.0
    },
    "43642": {
        "name": "Kailua (Hawaii County)",
        "state": "Kailua (Hawaii County) HI",
        "population": 33024,
        "housing": 15746,
        "land_area": 55267909.0,
        "water_area": 0.0
    },
    "43669": {
        "name": "Kailua (Honolulu County)--Kaneohe",
        "state": "Kailua (Honolulu County)--Kaneohe HI",
        "population": 118092,
        "housing": 40063,
        "land_area": 75845561.0,
        "water_area": 1376149.0
    },
    "43723": {
        "name": "Kalamazoo",
        "state": "Kalamazoo MI",
        "population": 204562,
        "housing": 90906,
        "land_area": 283500821.0,
        "water_area": 14697842.0
    },
    "43777": {
        "name": "Kalispell",
        "state": "Kalispell MT",
        "population": 36131,
        "housing": 15698,
        "land_area": 48500512.0,
        "water_area": 564840.0
    },
    "43885": {
        "name": "Kankakee",
        "state": "Kankakee IL",
        "population": 66530,
        "housing": 27634,
        "land_area": 81999622.0,
        "water_area": 1966681.0
    },
    "43912": {
        "name": "Kansas City",
        "state": "Kansas City MO--KS",
        "population": 1674218,
        "housing": 729472,
        "land_area": 1849506374.0,
        "water_area": 21421705.0
    },
    "43939": {
        "name": "Kapaa",
        "state": "Kapaa HI",
        "population": 18212,
        "housing": 7118,
        "land_area": 29178576.0,
        "water_area": 87811.0
    },
    "43993": {
        "name": "Kaplan",
        "state": "Kaplan LA",
        "population": 4656,
        "housing": 2343,
        "land_area": 6554940.0,
        "water_area": 0.0
    },
    "44047": {
        "name": "Kasson",
        "state": "Kasson MN",
        "population": 7649,
        "housing": 2996,
        "land_area": 8811107.0,
        "water_area": 703.0
    },
    "44074": {
        "name": "Kaufman",
        "state": "Kaufman TX",
        "population": 6127,
        "housing": 2290,
        "land_area": 7944797.0,
        "water_area": 4313.0
    },
    "44155": {
        "name": "Kearney",
        "state": "Kearney MO",
        "population": 10174,
        "housing": 3941,
        "land_area": 14541160.0,
        "water_area": 0.0
    },
    "44182": {
        "name": "Kearney",
        "state": "Kearney NE",
        "population": 34526,
        "housing": 14644,
        "land_area": 47053698.0,
        "water_area": 349578.0
    },
    "44209": {
        "name": "Keene",
        "state": "Keene NH",
        "population": 22687,
        "housing": 10393,
        "land_area": 37635959.0,
        "water_area": 436639.0
    },
    "44236": {
        "name": "Kekaha",
        "state": "Kekaha HI",
        "population": 5724,
        "housing": 2092,
        "land_area": 5273311.0,
        "water_area": 12257.0
    },
    "44344": {
        "name": "Kenai",
        "state": "Kenai AK",
        "population": 8642,
        "housing": 3805,
        "land_area": 33795257.0,
        "water_area": 126696.0
    },
    "44371": {
        "name": "Kendallville",
        "state": "Kendallville IN",
        "population": 10587,
        "housing": 4564,
        "land_area": 14827594.0,
        "water_area": 100837.0
    },
    "44452": {
        "name": "Kennett",
        "state": "Kennett MO",
        "population": 10560,
        "housing": 4832,
        "land_area": 16630965.0,
        "water_area": 2306.0
    },
    "44479": {
        "name": "Kennewick--Richland--Pasco",
        "state": "Kennewick--Richland--Pasco WA",
        "population": 255401,
        "housing": 93872,
        "land_area": 290635009.0,
        "water_area": 11717403.0
    },
    "44506": {
        "name": "Kenosha",
        "state": "Kenosha WI",
        "population": 125865,
        "housing": 53111,
        "land_area": 145481262.0,
        "water_area": 640672.0
    },
    "44533": {
        "name": "Kenton",
        "state": "Kenton OH",
        "population": 8033,
        "housing": 3835,
        "land_area": 11081409.0,
        "water_area": 0.0
    },
    "44560": {
        "name": "Keokuk",
        "state": "Keokuk IA--IL",
        "population": 12351,
        "housing": 5929,
        "land_area": 23394628.0,
        "water_area": 36795.0
    },
    "44587": {
        "name": "Kerman",
        "state": "Kerman CA",
        "population": 16002,
        "housing": 4509,
        "land_area": 7161033.0,
        "water_area": 0.0
    },
    "44614": {
        "name": "Kermit",
        "state": "Kermit TX",
        "population": 6381,
        "housing": 2643,
        "land_area": 7225148.0,
        "water_area": 0.0
    },
    "44641": {
        "name": "Kerrville",
        "state": "Kerrville TX",
        "population": 31844,
        "housing": 14956,
        "land_area": 56518765.0,
        "water_area": 435135.0
    },
    "44668": {
        "name": "Ketchikan",
        "state": "Ketchikan AK",
        "population": 11975,
        "housing": 5458,
        "land_area": 61324183.0,
        "water_area": 578935.0
    },
    "44695": {
        "name": "Ketchum",
        "state": "Ketchum ID",
        "population": 6346,
        "housing": 6698,
        "land_area": 20685351.0,
        "water_area": 118747.0
    },
    "44722": {
        "name": "Kewanee",
        "state": "Kewanee IL",
        "population": 12542,
        "housing": 5776,
        "land_area": 15419066.0,
        "water_area": 0.0
    },
    "44789": {
        "name": "Key Largo",
        "state": "Key Largo FL",
        "population": 21687,
        "housing": 16322,
        "land_area": 38839253.0,
        "water_area": 5136751.0
    },
    "44803": {
        "name": "Keyser",
        "state": "Keyser WV--MD",
        "population": 6328,
        "housing": 3035,
        "land_area": 8790849.0,
        "water_area": 0.0
    },
    "44830": {
        "name": "Keystone Heights",
        "state": "Keystone Heights FL",
        "population": 8218,
        "housing": 3760,
        "land_area": 26455041.0,
        "water_area": 3253286.0
    },
    "44857": {
        "name": "Key West",
        "state": "Key West FL",
        "population": 32146,
        "housing": 16779,
        "land_area": 17530643.0,
        "water_area": 3483217.0
    },
    "44911": {
        "name": "Kihei",
        "state": "Kihei HI",
        "population": 26878,
        "housing": 17408,
        "land_area": 21093761.0,
        "water_area": 14330.0
    },
    "44938": {
        "name": "Kilgore",
        "state": "Kilgore TX",
        "population": 16719,
        "housing": 6776,
        "land_area": 56365620.0,
        "water_area": 56215.0
    },
    "44965": {
        "name": "Kill Devil Hills--Nags Head",
        "state": "Kill Devil Hills--Nags Head NC",
        "population": 23851,
        "housing": 26763,
        "land_area": 86898932.0,
        "water_area": 2193885.0
    },
    "44992": {
        "name": "Killeen",
        "state": "Killeen TX",
        "population": 257222,
        "housing": 98214,
        "land_area": 260093903.0,
        "water_area": 715102.0
    },
    "45032": {
        "name": "Kimberling City",
        "state": "Kimberling City MO",
        "population": 4467,
        "housing": 3329,
        "land_area": 24228508.0,
        "water_area": 10819.0
    },
    "45073": {
        "name": "King City",
        "state": "King City CA",
        "population": 13760,
        "housing": 3603,
        "land_area": 7787360.0,
        "water_area": 11387.0
    },
    "45127": {
        "name": "Kingman",
        "state": "Kingman AZ",
        "population": 46953,
        "housing": 20797,
        "land_area": 58382216.0,
        "water_area": 0.0
    },
    "45157": {
        "name": "Kingsburg",
        "state": "Kingsburg CA",
        "population": 12602,
        "housing": 4506,
        "land_area": 10143228.0,
        "water_area": 0.0
    },
    "45181": {
        "name": "Kingsland",
        "state": "Kingsland TX",
        "population": 8093,
        "housing": 4878,
        "land_area": 26779704.0,
        "water_area": 158083.0
    },
    "45196": {
        "name": "Kingsland--St. Marys",
        "state": "Kingsland--St. Marys GA",
        "population": 38567,
        "housing": 15584,
        "land_area": 78819305.0,
        "water_area": 912545.0
    },
    "45208": {
        "name": "Kings Mountain",
        "state": "Kings Mountain NC",
        "population": 12619,
        "housing": 5513,
        "land_area": 31438562.0,
        "water_area": 33338.0
    },
    "45235": {
        "name": "Kingsport",
        "state": "Kingsport TN--VA",
        "population": 98411,
        "housing": 47217,
        "land_area": 243592494.0,
        "water_area": 2608549.0
    },
    "45262": {
        "name": "Kingston",
        "state": "Kingston NY",
        "population": 50254,
        "housing": 23955,
        "land_area": 80544615.0,
        "water_area": 2134641.0
    },
    "45289": {
        "name": "Kingstree",
        "state": "Kingstree SC",
        "population": 5250,
        "housing": 2590,
        "land_area": 13163941.0,
        "water_area": 0.0
    },
    "45316": {
        "name": "Kingsville",
        "state": "Kingsville TX",
        "population": 24945,
        "housing": 11291,
        "land_area": 32856175.0,
        "water_area": 6585.0
    },
    "45370": {
        "name": "Kinross",
        "state": "Kinross MI",
        "population": 5100,
        "housing": 951,
        "land_area": 8289893.0,
        "water_area": 0.0
    },
    "45397": {
        "name": "Kinston",
        "state": "Kinston NC",
        "population": 21050,
        "housing": 10990,
        "land_area": 46380199.0,
        "water_area": 25893.0
    },
    "45424": {
        "name": "Kirksville",
        "state": "Kirksville MO",
        "population": 16846,
        "housing": 7492,
        "land_area": 22260936.0,
        "water_area": 56697.0
    },
    "45436": {
        "name": "Kirtland",
        "state": "Kirtland NM",
        "population": 5737,
        "housing": 2044,
        "land_area": 15286850.0,
        "water_area": 0.0
    },
    "45443": {
        "name": "Kiryas Joel",
        "state": "Kiryas Joel NY",
        "population": 71582,
        "housing": 19817,
        "land_area": 74472197.0,
        "water_area": 2138073.0
    },
    "45451": {
        "name": "Kissimmee--St. Cloud",
        "state": "Kissimmee--St. Cloud FL",
        "population": 418404,
        "housing": 153652,
        "land_area": 418493199.0,
        "water_area": 10280685.0
    },
    "45478": {
        "name": "Kittanning--Ford City",
        "state": "Kittanning--Ford City PA",
        "population": 14605,
        "housing": 7455,
        "land_area": 26774833.0,
        "water_area": 1191660.0
    },
    "45505": {
        "name": "Klamath Falls--Altamont",
        "state": "Klamath Falls--Altamont OR",
        "population": 43208,
        "housing": 19117,
        "land_area": 61355348.0,
        "water_area": 1759501.0
    },
    "45613": {
        "name": "Knoxville",
        "state": "Knoxville IA",
        "population": 7561,
        "housing": 3486,
        "land_area": 9928177.0,
        "water_area": 0.0
    },
    "45640": {
        "name": "Knoxville",
        "state": "Knoxville TN",
        "population": 597257,
        "housing": 263977,
        "land_area": 1118633968.0,
        "water_area": 11689562.0
    },
    "45667": {
        "name": "Kodiak--Mill Bay",
        "state": "Kodiak--Mill Bay AK",
        "population": 9530,
        "housing": 3798,
        "land_area": 12907517.0,
        "water_area": 656531.0
    },
    "45694": {
        "name": "Kokomo",
        "state": "Kokomo IN",
        "population": 62576,
        "housing": 30777,
        "land_area": 84173198.0,
        "water_area": 0.0
    },
    "45721": {
        "name": "Kosciusko",
        "state": "Kosciusko MS",
        "population": 6716,
        "housing": 2870,
        "land_area": 14484759.0,
        "water_area": 5758.0
    },
    "45761": {
        "name": "Krum",
        "state": "Krum TX",
        "population": 5876,
        "housing": 2062,
        "land_area": 8470367.0,
        "water_area": 6508.0
    },
    "45775": {
        "name": "Kuna",
        "state": "Kuna ID",
        "population": 23565,
        "housing": 7813,
        "land_area": 16384896.0,
        "water_area": 46009.0
    },
    "45802": {
        "name": "Kutztown",
        "state": "Kutztown PA",
        "population": 8672,
        "housing": 2793,
        "land_area": 9702353.0,
        "water_area": 46826.0
    },
    "45829": {
        "name": "LaBelle",
        "state": "LaBelle FL",
        "population": 13053,
        "housing": 4759,
        "land_area": 21720337.0,
        "water_area": 33968.0
    },
    "45856": {
        "name": "Laconia",
        "state": "Laconia NH",
        "population": 27267,
        "housing": 17637,
        "land_area": 73510794.0,
        "water_area": 7562067.0
    },
    "45910": {
        "name": "La Crosse",
        "state": "La Crosse WI--MN",
        "population": 98872,
        "housing": 44018,
        "land_area": 109395269.0,
        "water_area": 6301358.0
    },
    "45991": {
        "name": "LaFayette",
        "state": "LaFayette GA",
        "population": 6772,
        "housing": 3100,
        "land_area": 16489322.0,
        "water_area": 0.0
    },
    "46018": {
        "name": "Lafayette",
        "state": "Lafayette IN",
        "population": 157100,
        "housing": 66557,
        "land_area": 178037526.0,
        "water_area": 2373868.0
    },
    "46045": {
        "name": "Lafayette",
        "state": "Lafayette LA",
        "population": 227316,
        "housing": 102033,
        "land_area": 417506929.0,
        "water_area": 532069.0
    },
    "46099": {
        "name": "Lafayette",
        "state": "Lafayette TN",
        "population": 6174,
        "housing": 2758,
        "land_area": 13775612.0,
        "water_area": 0.0
    },
    "46126": {
        "name": "Lafayette--Erie--Louisville",
        "state": "Lafayette--Erie--Louisville CO",
        "population": 96485,
        "housing": 37356,
        "land_area": 90623189.0,
        "water_area": 767469.0
    },
    "46153": {
        "name": "La Follette",
        "state": "La Follette TN",
        "population": 20114,
        "housing": 9387,
        "land_area": 61672595.0,
        "water_area": 892724.0
    },
    "46180": {
        "name": "Lago Vista (Travis County)",
        "state": "Lago Vista (Travis County) TX",
        "population": 8463,
        "housing": 4303,
        "land_area": 21690317.0,
        "water_area": 0.0
    },
    "46207": {
        "name": "La Grande",
        "state": "La Grande OR",
        "population": 14954,
        "housing": 6651,
        "land_area": 16022045.0,
        "water_area": 0.0
    },
    "46234": {
        "name": "LaGrange",
        "state": "LaGrange GA",
        "population": 35420,
        "housing": 14996,
        "land_area": 81480547.0,
        "water_area": 131662.0
    },
    "46288": {
        "name": "La Grange",
        "state": "La Grange KY",
        "population": 24556,
        "housing": 8138,
        "land_area": 52298532.0,
        "water_area": 868644.0
    },
    "46342": {
        "name": "La Grange",
        "state": "La Grange TX",
        "population": 5020,
        "housing": 2296,
        "land_area": 9892476.0,
        "water_area": 8895.0
    },
    "46396": {
        "name": "Lahaina--Napili-Honokowai",
        "state": "Lahaina--Napili-Honokowai HI",
        "population": 21398,
        "housing": 11642,
        "land_area": 13836433.0,
        "water_area": 18026.0
    },
    "46423": {
        "name": "Laie--Hauula",
        "state": "Laie--Hauula HI",
        "population": 12488,
        "housing": 3443,
        "land_area": 8830624.0,
        "water_area": 0.0
    },
    "46450": {
        "name": "La Junta",
        "state": "La Junta CO",
        "population": 7792,
        "housing": 3571,
        "land_area": 10049754.0,
        "water_area": 31115.0
    },
    "46489": {
        "name": "Lake Bryant",
        "state": "Lake Bryant FL",
        "population": 3632,
        "housing": 2123,
        "land_area": 7753525.0,
        "water_area": 605447.0
    },
    "46531": {
        "name": "Lake Charles",
        "state": "Lake Charles LA",
        "population": 162501,
        "housing": 71250,
        "land_area": 330850319.0,
        "water_area": 2314379.0
    },
    "46558": {
        "name": "Lake City",
        "state": "Lake City FL",
        "population": 25334,
        "housing": 11058,
        "land_area": 73963166.0,
        "water_area": 621903.0
    },
    "46585": {
        "name": "Lake City",
        "state": "Lake City MN",
        "population": 4912,
        "housing": 2634,
        "land_area": 7244466.0,
        "water_area": 22994.0
    },
    "46612": {
        "name": "Lake City",
        "state": "Lake City SC",
        "population": 6915,
        "housing": 3161,
        "land_area": 14286274.0,
        "water_area": 0.0
    },
    "46639": {
        "name": "Lake Conroe Eastshore",
        "state": "Lake Conroe Eastshore TX",
        "population": 12188,
        "housing": 5755,
        "land_area": 20059231.0,
        "water_area": 85746.0
    },
    "46666": {
        "name": "Lake Conroe Westshore",
        "state": "Lake Conroe Westshore TX",
        "population": 29322,
        "housing": 14134,
        "land_area": 49897496.0,
        "water_area": 6270961.0
    },
    "46684": {
        "name": "Lake Delton",
        "state": "Lake Delton WI",
        "population": 6546,
        "housing": 3722,
        "land_area": 24233947.0,
        "water_area": 1709219.0
    },
    "46687": {
        "name": "Lake Erie Beach",
        "state": "Lake Erie Beach NY",
        "population": 7643,
        "housing": 4254,
        "land_area": 20038257.0,
        "water_area": 0.0
    },
    "46693": {
        "name": "Lake Geneva",
        "state": "Lake Geneva WI",
        "population": 10955,
        "housing": 6168,
        "land_area": 16423707.0,
        "water_area": 80710.0
    },
    "46747": {
        "name": "Lake Havasu City",
        "state": "Lake Havasu City AZ",
        "population": 59017,
        "housing": 36876,
        "land_area": 87507009.0,
        "water_area": 118649.0
    },
    "46760": {
        "name": "Lake Holiday",
        "state": "Lake Holiday IL",
        "population": 7313,
        "housing": 3211,
        "land_area": 11138120.0,
        "water_area": 1250252.0
    },
    "46774": {
        "name": "Lake Isabella",
        "state": "Lake Isabella CA",
        "population": 3698,
        "housing": 2116,
        "land_area": 6374999.0,
        "water_area": 51642.0
    },
    "46801": {
        "name": "Lake Jackson",
        "state": "Lake Jackson TX",
        "population": 56054,
        "housing": 24765,
        "land_area": 88959801.0,
        "water_area": 5038143.0
    },
    "46828": {
        "name": "Lakeland",
        "state": "Lakeland FL",
        "population": 277915,
        "housing": 116354,
        "land_area": 377964243.0,
        "water_area": 30055450.0
    },
    "46882": {
        "name": "Lake Mills",
        "state": "Lake Mills WI",
        "population": 6857,
        "housing": 3262,
        "land_area": 10537706.0,
        "water_area": 45954.0
    },
    "46889": {
        "name": "Lake Mohawk",
        "state": "Lake Mohawk NJ",
        "population": 13164,
        "housing": 5460,
        "land_area": 21323528.0,
        "water_area": 3249090.0
    },
    "46909": {
        "name": "Lake Monticello",
        "state": "Lake Monticello VA",
        "population": 9825,
        "housing": 4320,
        "land_area": 16382395.0,
        "water_area": 1387162.0
    },
    "46963": {
        "name": "Lake of the Pines",
        "state": "Lake of the Pines CA",
        "population": 4261,
        "housing": 2030,
        "land_area": 6004040.0,
        "water_area": 908432.0
    },
    "46972": {
        "name": "Lake of the Woods",
        "state": "Lake of the Woods VA",
        "population": 10902,
        "housing": 4840,
        "land_area": 18201019.0,
        "water_area": 2084416.0
    },
    "47010": {
        "name": "Lake Placid",
        "state": "Lake Placid FL",
        "population": 17816,
        "housing": 10793,
        "land_area": 61035549.0,
        "water_area": 9253525.0
    },
    "47017": {
        "name": "Lake Placid",
        "state": "Lake Placid NY",
        "population": 3486,
        "housing": 2869,
        "land_area": 8965254.0,
        "water_area": 539622.0
    },
    "47044": {
        "name": "Lake Pocotopaug",
        "state": "Lake Pocotopaug CT",
        "population": 7622,
        "housing": 3617,
        "land_area": 19820463.0,
        "water_area": 2127744.0
    },
    "47071": {
        "name": "Lakeport",
        "state": "Lakeport CA",
        "population": 8994,
        "housing": 4352,
        "land_area": 16066764.0,
        "water_area": 116183.0
    },
    "47120": {
        "name": "Lake Royale",
        "state": "Lake Royale NC",
        "population": 2942,
        "housing": 2237,
        "land_area": 8313933.0,
        "water_area": 1391980.0
    },
    "47125": {
        "name": "Lakes of the Four Seasons",
        "state": "Lakes of the Four Seasons IN",
        "population": 13113,
        "housing": 4794,
        "land_area": 13985410.0,
        "water_area": 1249134.0
    },
    "47287": {
        "name": "Lamar",
        "state": "Lamar CO",
        "population": 7502,
        "housing": 3386,
        "land_area": 11031355.0,
        "water_area": 35165.0
    },
    "47368": {
        "name": "Lambertville",
        "state": "Lambertville NJ--PA",
        "population": 10167,
        "housing": 5234,
        "land_area": 14571870.0,
        "water_area": 121407.0
    },
    "47395": {
        "name": "Lamesa",
        "state": "Lamesa TX",
        "population": 8731,
        "housing": 3952,
        "land_area": 11251019.0,
        "water_area": 23217.0
    },
    "47412": {
        "name": "Lamont",
        "state": "Lamont CA",
        "population": 15271,
        "housing": 4112,
        "land_area": 7236735.0,
        "water_area": 0.0
    },
    "47422": {
        "name": "Lampasas",
        "state": "Lampasas TX",
        "population": 6674,
        "housing": 3004,
        "land_area": 10140685.0,
        "water_area": 24267.0
    },
    "47503": {
        "name": "Lancaster",
        "state": "Lancaster OH",
        "population": 43576,
        "housing": 19625,
        "land_area": 51932783.0,
        "water_area": 194695.0
    },
    "47530": {
        "name": "Lancaster--Manheim",
        "state": "Lancaster--Manheim PA",
        "population": 394530,
        "housing": 162561,
        "land_area": 470163952.0,
        "water_area": 3497706.0
    },
    "47557": {
        "name": "Lancaster",
        "state": "Lancaster SC",
        "population": 22709,
        "housing": 10207,
        "land_area": 57931076.0,
        "water_area": 77921.0
    },
    "47638": {
        "name": "Lander",
        "state": "Lander WY",
        "population": 6977,
        "housing": 3216,
        "land_area": 7750608.0,
        "water_area": 0.0
    },
    "47665": {
        "name": "Landrum--Tryon",
        "state": "Landrum--Tryon SC--NC",
        "population": 4518,
        "housing": 2594,
        "land_area": 13986584.0,
        "water_area": 362596.0
    },
    "47719": {
        "name": "Lansing",
        "state": "Lansing MI",
        "population": 318300,
        "housing": 143060,
        "land_area": 403591551.0,
        "water_area": 8683411.0
    },
    "47746": {
        "name": "Lapeer",
        "state": "Lapeer MI",
        "population": 12402,
        "housing": 5383,
        "land_area": 22878344.0,
        "water_area": 593809.0
    },
    "47800": {
        "name": "La Plata",
        "state": "La Plata MD",
        "population": 10536,
        "housing": 3920,
        "land_area": 16226989.0,
        "water_area": 12868.0
    },
    "47827": {
        "name": "Laramie",
        "state": "Laramie WY",
        "population": 32261,
        "housing": 15450,
        "land_area": 39320152.0,
        "water_area": 40221.0
    },
    "47854": {
        "name": "Laredo",
        "state": "Laredo TX",
        "population": 251462,
        "housing": 79974,
        "land_area": 166290032.0,
        "water_area": 1566734.0
    },
    "47865": {
        "name": "Lares",
        "state": "Lares PR",
        "population": 28615,
        "housing": 13312,
        "land_area": 77906786.0,
        "water_area": 2244.0
    },
    "47881": {
        "name": "Larned",
        "state": "Larned KS",
        "population": 3734,
        "housing": 2087,
        "land_area": 5396212.0,
        "water_area": 0.0
    },
    "47935": {
        "name": "Las Cruces",
        "state": "Las Cruces NM",
        "population": 139338,
        "housing": 60572,
        "land_area": 166979315.0,
        "water_area": 51532.0
    },
    "47989": {
        "name": "Las Vegas",
        "state": "Las Vegas NM",
        "population": 14530,
        "housing": 7337,
        "land_area": 18085067.0,
        "water_area": 0.0
    },
    "47995": {
        "name": "Las Vegas--Henderson--Paradise",
        "state": "Las Vegas--Henderson--Paradise NV",
        "population": 2196623,
        "housing": 884138,
        "land_area": 1127407757.0,
        "water_area": 1522870.0
    },
    "48016": {
        "name": "Laughlin",
        "state": "Laughlin NV",
        "population": 6579,
        "housing": 4162,
        "land_area": 3937232.0,
        "water_area": 0.0
    },
    "48043": {
        "name": "Laurel",
        "state": "Laurel MS",
        "population": 25201,
        "housing": 10428,
        "land_area": 52383699.0,
        "water_area": 250568.0
    },
    "48070": {
        "name": "Laurel",
        "state": "Laurel MT",
        "population": 8789,
        "housing": 3736,
        "land_area": 15370854.0,
        "water_area": 10723.0
    },
    "48151": {
        "name": "Laurens",
        "state": "Laurens SC",
        "population": 11331,
        "housing": 5255,
        "land_area": 25056420.0,
        "water_area": 0.0
    },
    "48178": {
        "name": "Laurinburg",
        "state": "Laurinburg NC",
        "population": 16225,
        "housing": 7385,
        "land_area": 34045431.0,
        "water_area": 384789.0
    },
    "48232": {
        "name": "Lawrence",
        "state": "Lawrence KS",
        "population": 94998,
        "housing": 43472,
        "land_area": 77505179.0,
        "water_area": 1076315.0
    },
    "48259": {
        "name": "Lawrenceburg",
        "state": "Lawrenceburg KY",
        "population": 13543,
        "housing": 5697,
        "land_area": 16978097.0,
        "water_area": 76658.0
    },
    "48286": {
        "name": "Lawrenceburg",
        "state": "Lawrenceburg TN",
        "population": 11679,
        "housing": 5267,
        "land_area": 25039645.0,
        "water_area": 0.0
    },
    "48340": {
        "name": "Lawrenceville",
        "state": "Lawrenceville IL",
        "population": 4632,
        "housing": 2317,
        "land_area": 6745548.0,
        "water_area": 0.0
    },
    "48394": {
        "name": "Lawton",
        "state": "Lawton OK",
        "population": 87464,
        "housing": 40042,
        "land_area": 121300500.0,
        "water_area": 136233.0
    },
    "48421": {
        "name": "Lead",
        "state": "Lead SD",
        "population": 4122,
        "housing": 2568,
        "land_area": 10060755.0,
        "water_area": 0.0
    },
    "48448": {
        "name": "Leadville",
        "state": "Leadville CO",
        "population": 4538,
        "housing": 2606,
        "land_area": 6461810.0,
        "water_area": 0.0
    },
    "48475": {
        "name": "Leavenworth",
        "state": "Leavenworth KS",
        "population": 47570,
        "housing": 17963,
        "land_area": 56139314.0,
        "water_area": 82727.0
    },
    "48502": {
        "name": "Lebanon",
        "state": "Lebanon IN",
        "population": 16466,
        "housing": 7511,
        "land_area": 21657107.0,
        "water_area": 0.0
    },
    "48529": {
        "name": "Lebanon",
        "state": "Lebanon KY",
        "population": 6209,
        "housing": 2783,
        "land_area": 12799893.0,
        "water_area": 32353.0
    },
    "48556": {
        "name": "Lebanon",
        "state": "Lebanon MO",
        "population": 14710,
        "housing": 6729,
        "land_area": 32119051.0,
        "water_area": 151005.0
    },
    "48583": {
        "name": "Lebanon",
        "state": "Lebanon NH--VT",
        "population": 30299,
        "housing": 13383,
        "land_area": 78122831.0,
        "water_area": 6858966.0
    },
    "48637": {
        "name": "Lebanon",
        "state": "Lebanon OR",
        "population": 22327,
        "housing": 9125,
        "land_area": 22130880.0,
        "water_area": 374893.0
    },
    "48664": {
        "name": "Lebanon",
        "state": "Lebanon PA",
        "population": 75485,
        "housing": 31685,
        "land_area": 84715873.0,
        "water_area": 383947.0
    },
    "48691": {
        "name": "Lebanon",
        "state": "Lebanon TN",
        "population": 36678,
        "housing": 15426,
        "land_area": 78454826.0,
        "water_area": 10591.0
    },
    "48745": {
        "name": "Lee",
        "state": "Lee MA",
        "population": 8119,
        "housing": 5415,
        "land_area": 32524986.0,
        "water_area": 425301.0
    },
    "48799": {
        "name": "Leesburg--Eustis--Tavares",
        "state": "Leesburg--Eustis--Tavares FL",
        "population": 151523,
        "housing": 75939,
        "land_area": 222984477.0,
        "water_area": 21383616.0
    },
    "48826": {
        "name": "Lee's Summit",
        "state": "Lee's Summit MO",
        "population": 91960,
        "housing": 36767,
        "land_area": 97369441.0,
        "water_area": 2772160.0
    },
    "48853": {
        "name": "Leesville",
        "state": "Leesville LA",
        "population": 8328,
        "housing": 4376,
        "land_area": 17823587.0,
        "water_area": 100153.0
    },
    "48890": {
        "name": "Lehighton--Palmerton--Jim Thorpe",
        "state": "Lehighton--Palmerton--Jim Thorpe PA",
        "population": 18503,
        "housing": 8782,
        "land_area": 25533788.0,
        "water_area": 522711.0
    },
    "48915": {
        "name": "Leisuretowne",
        "state": "Leisuretowne NJ",
        "population": 5294,
        "housing": 2953,
        "land_area": 8486323.0,
        "water_area": 301240.0
    },
    "48934": {
        "name": "Leitchfield",
        "state": "Leitchfield KY",
        "population": 6488,
        "housing": 2981,
        "land_area": 14137120.0,
        "water_area": 27181.0
    },
    "48988": {
        "name": "Le Mars",
        "state": "Le Mars IA",
        "population": 10138,
        "housing": 4347,
        "land_area": 14161819.0,
        "water_area": 0.0
    },
    "49015": {
        "name": "Lemoore",
        "state": "Lemoore CA",
        "population": 26957,
        "housing": 9514,
        "land_area": 17279362.0,
        "water_area": 0.0
    },
    "49042": {
        "name": "Lemoore Station",
        "state": "Lemoore Station CA",
        "population": 6568,
        "housing": 1799,
        "land_area": 13218009.0,
        "water_area": 0.0
    },
    "49096": {
        "name": "Leominster--Fitchburg",
        "state": "Leominster--Fitchburg MA",
        "population": 111790,
        "housing": 48267,
        "land_area": 137093774.0,
        "water_area": 5488840.0
    },
    "49109": {
        "name": "Leonardtown",
        "state": "Leonardtown MD",
        "population": 6092,
        "housing": 2397,
        "land_area": 16002129.0,
        "water_area": 156464.0
    },
    "49150": {
        "name": "Le Roy",
        "state": "Le Roy NY",
        "population": 4645,
        "housing": 2223,
        "land_area": 7737746.0,
        "water_area": 1482.0
    },
    "49204": {
        "name": "Levelland",
        "state": "Levelland TX",
        "population": 12601,
        "housing": 5391,
        "land_area": 16589128.0,
        "water_area": 5302.0
    },
    "49231": {
        "name": "Lewes--Rehoboth Beach",
        "state": "Lewes--Rehoboth Beach DE",
        "population": 39681,
        "housing": 33284,
        "land_area": 99205323.0,
        "water_area": 1791630.0
    },
    "49258": {
        "name": "Lewisburg",
        "state": "Lewisburg TN",
        "population": 11934,
        "housing": 5207,
        "land_area": 24303141.0,
        "water_area": 14335.0
    },
    "49285": {
        "name": "Lewisburg",
        "state": "Lewisburg WV",
        "population": 7227,
        "housing": 3856,
        "land_area": 18139226.0,
        "water_area": 295950.0
    },
    "49312": {
        "name": "Lewiston",
        "state": "Lewiston ID--WA",
        "population": 54798,
        "housing": 24031,
        "land_area": 72334135.0,
        "water_area": 1361261.0
    },
    "49339": {
        "name": "Lewiston",
        "state": "Lewiston ME",
        "population": 60743,
        "housing": 27496,
        "land_area": 81370404.0,
        "water_area": 2177771.0
    },
    "49366": {
        "name": "Lewistown",
        "state": "Lewistown MT",
        "population": 6024,
        "housing": 3182,
        "land_area": 7557338.0,
        "water_area": 0.0
    },
    "49393": {
        "name": "Lewistown",
        "state": "Lewistown PA",
        "population": 20999,
        "housing": 10313,
        "land_area": 31038012.0,
        "water_area": 372669.0
    },
    "49474": {
        "name": "Lexington",
        "state": "Lexington NE",
        "population": 10438,
        "housing": 3545,
        "land_area": 12200078.0,
        "water_area": 34285.0
    },
    "49528": {
        "name": "Lexington",
        "state": "Lexington TN",
        "population": 6357,
        "housing": 3086,
        "land_area": 17232674.0,
        "water_area": 6182.0
    },
    "49555": {
        "name": "Lexington",
        "state": "Lexington VA",
        "population": 9460,
        "housing": 3499,
        "land_area": 13682053.0,
        "water_area": 123531.0
    },
    "49582": {
        "name": "Lexington-Fayette",
        "state": "Lexington-Fayette KY",
        "population": 315631,
        "housing": 143074,
        "land_area": 217543130.0,
        "water_area": 2056356.0
    },
    "49594": {
        "name": "Lexington Park--California--Chesapeake Ranch Estates",
        "state": "Lexington Park--California--Chesapeake Ranch Estates MD",
        "population": 62352,
        "housing": 26209,
        "land_area": 120832052.0,
        "water_area": 5412448.0
    },
    "49609": {
        "name": "Libby",
        "state": "Libby MT",
        "population": 4341,
        "housing": 2194,
        "land_area": 9104840.0,
        "water_area": 169223.0
    },
    "49636": {
        "name": "Liberal",
        "state": "Liberal KS",
        "population": 19843,
        "housing": 7399,
        "land_area": 27496063.0,
        "water_area": 572999.0
    },
    "49663": {
        "name": "Liberty",
        "state": "Liberty NY",
        "population": 5284,
        "housing": 2472,
        "land_area": 7948305.0,
        "water_area": 0.0
    },
    "49690": {
        "name": "Liberty",
        "state": "Liberty TX",
        "population": 6387,
        "housing": 2607,
        "land_area": 13918964.0,
        "water_area": 0.0
    },
    "49771": {
        "name": "Lihue",
        "state": "Lihue HI",
        "population": 15885,
        "housing": 5538,
        "land_area": 18238396.0,
        "water_area": 78115.0
    },
    "49852": {
        "name": "Lima",
        "state": "Lima OH",
        "population": 68630,
        "housing": 31148,
        "land_area": 126026967.0,
        "water_area": 3780471.0
    },
    "49906": {
        "name": "Lincoln",
        "state": "Lincoln IL",
        "population": 13990,
        "housing": 6756,
        "land_area": 20982286.0,
        "water_area": 9593.0
    },
    "49933": {
        "name": "Lincoln",
        "state": "Lincoln NE",
        "population": 291217,
        "housing": 123888,
        "land_area": 243909888.0,
        "water_area": 2980698.0
    },
    "49945": {
        "name": "Lincoln",
        "state": "Lincoln NH",
        "population": 2005,
        "housing": 2751,
        "land_area": 12745379.0,
        "water_area": 461279.0
    },
    "49955": {
        "name": "Lincoln Beach",
        "state": "Lincoln Beach OR",
        "population": 3626,
        "housing": 3515,
        "land_area": 7556872.0,
        "water_area": 35293.0
    },
    "49960": {
        "name": "Lincoln City",
        "state": "Lincoln City OR",
        "population": 10494,
        "housing": 7831,
        "land_area": 17279816.0,
        "water_area": 2555757.0
    },
    "49987": {
        "name": "Lincolnton",
        "state": "Lincolnton NC",
        "population": 22657,
        "housing": 10130,
        "land_area": 73575382.0,
        "water_area": 350990.0
    },
    "50014": {
        "name": "Lindale--Hideaway",
        "state": "Lindale--Hideaway TX",
        "population": 11770,
        "housing": 5046,
        "land_area": 31849222.0,
        "water_area": 1346264.0
    },
    "50041": {
        "name": "Lindsay",
        "state": "Lindsay CA",
        "population": 13942,
        "housing": 3936,
        "land_area": 7472637.0,
        "water_area": 0.0
    },
    "50122": {
        "name": "Lindstrom--Chisago City",
        "state": "Lindstrom--Chisago City MN",
        "population": 9152,
        "housing": 4161,
        "land_area": 16117811.0,
        "water_area": 7232284.0
    },
    "50149": {
        "name": "Linton",
        "state": "Linton IN",
        "population": 5298,
        "housing": 2621,
        "land_area": 7716928.0,
        "water_area": 0.0
    },
    "50230": {
        "name": "Litchfield",
        "state": "Litchfield IL",
        "population": 6350,
        "housing": 3053,
        "land_area": 12635306.0,
        "water_area": 23169.0
    },
    "50257": {
        "name": "Litchfield",
        "state": "Litchfield MN",
        "population": 6569,
        "housing": 2863,
        "land_area": 10108192.0,
        "water_area": 42833.0
    },
    "50275": {
        "name": "Litchfield Beach",
        "state": "Litchfield Beach SC",
        "population": 15225,
        "housing": 10825,
        "land_area": 44068351.0,
        "water_area": 1071846.0
    },
    "50311": {
        "name": "Little Falls",
        "state": "Little Falls MN",
        "population": 9411,
        "housing": 4344,
        "land_area": 19191712.0,
        "water_area": 1117783.0
    },
    "50338": {
        "name": "Little Falls",
        "state": "Little Falls NY",
        "population": 4597,
        "housing": 2513,
        "land_area": 8750577.0,
        "water_area": 241434.0
    },
    "50365": {
        "name": "Littlefield",
        "state": "Littlefield TX",
        "population": 5626,
        "housing": 2573,
        "land_area": 7763061.0,
        "water_area": 0.0
    },
    "50392": {
        "name": "Little Rock",
        "state": "Little Rock AR",
        "population": 461864,
        "housing": 215096,
        "land_area": 693638192.0,
        "water_area": 8175879.0
    },
    "50419": {
        "name": "Littlestown",
        "state": "Littlestown PA",
        "population": 6442,
        "housing": 2746,
        "land_area": 8948225.0,
        "water_area": 9796.0
    },
    "50473": {
        "name": "Live Oak (Sutter County)",
        "state": "Live Oak (Sutter County) CA",
        "population": 9080,
        "housing": 2847,
        "land_area": 4312481.0,
        "water_area": 0.0
    },
    "50500": {
        "name": "Live Oak",
        "state": "Live Oak FL",
        "population": 6668,
        "housing": 2751,
        "land_area": 13725566.0,
        "water_area": 3873.0
    },
    "50533": {
        "name": "Livermore--Pleasanton--Dublin",
        "state": "Livermore--Pleasanton--Dublin CA",
        "population": 240381,
        "housing": 86809,
        "land_area": 169020821.0,
        "water_area": 3588211.0
    },
    "50581": {
        "name": "Livingston",
        "state": "Livingston CA",
        "population": 14255,
        "housing": 3734,
        "land_area": 6830950.0,
        "water_area": 0.0
    },
    "50608": {
        "name": "Livingston",
        "state": "Livingston MT",
        "population": 9350,
        "housing": 4752,
        "land_area": 11603714.0,
        "water_area": 79425.0
    },
    "50662": {
        "name": "Livingston",
        "state": "Livingston TX",
        "population": 5592,
        "housing": 2377,
        "land_area": 17852551.0,
        "water_area": 28894.0
    },
    "50743": {
        "name": "Lockhart",
        "state": "Lockhart TX",
        "population": 12886,
        "housing": 5022,
        "land_area": 21561023.0,
        "water_area": 16001.0
    },
    "50770": {
        "name": "Lock Haven",
        "state": "Lock Haven PA",
        "population": 14625,
        "housing": 6807,
        "land_area": 20455778.0,
        "water_area": 849922.0
    },
    "50797": {
        "name": "Lockport",
        "state": "Lockport NY",
        "population": 35958,
        "housing": 17025,
        "land_area": 45947910.0,
        "water_area": 101451.0
    },
    "50851": {
        "name": "Lodi",
        "state": "Lodi CA",
        "population": 73090,
        "housing": 27282,
        "land_area": 42320716.0,
        "water_area": 258077.0
    },
    "50932": {
        "name": "Logan",
        "state": "Logan OH",
        "population": 8209,
        "housing": 3671,
        "land_area": 11181249.0,
        "water_area": 236039.0
    },
    "50959": {
        "name": "Logan",
        "state": "Logan UT",
        "population": 113927,
        "housing": 37528,
        "land_area": 123606252.0,
        "water_area": 87264.0
    },
    "50986": {
        "name": "Logan",
        "state": "Logan WV",
        "population": 8821,
        "housing": 4389,
        "land_area": 16615622.0,
        "water_area": 1565300.0
    },
    "51013": {
        "name": "Logansport",
        "state": "Logansport IN",
        "population": 20374,
        "housing": 8712,
        "land_area": 27135232.0,
        "water_area": 682385.0
    },
    "51025": {
        "name": "Lo\u00edza--Vieques (Lo\u00edza Municipio)",
        "state": "Lo\u00edza--Vieques (Lo\u00edza Municipio) PR",
        "population": 15763,
        "housing": 8518,
        "land_area": 11373959.0,
        "water_area": 167572.0
    },
    "51040": {
        "name": "Lompoc",
        "state": "Lompoc CA",
        "population": 54287,
        "housing": 18212,
        "land_area": 25407607.0,
        "water_area": 73526.0
    },
    "51121": {
        "name": "London",
        "state": "London OH",
        "population": 10259,
        "housing": 4447,
        "land_area": 13153337.0,
        "water_area": 26037.0
    },
    "51131": {
        "name": "London--Corbin",
        "state": "London--Corbin KY",
        "population": 36861,
        "housing": 16540,
        "land_area": 124174916.0,
        "water_area": 615683.0
    },
    "51148": {
        "name": "Long Beach",
        "state": "Long Beach WA",
        "population": 3252,
        "housing": 2613,
        "land_area": 7578858.0,
        "water_area": 1747.0
    },
    "51175": {
        "name": "Longmont",
        "state": "Longmont CO",
        "population": 100776,
        "housing": 42509,
        "land_area": 62598110.0,
        "water_area": 708831.0
    },
    "51202": {
        "name": "Long Neck",
        "state": "Long Neck DE",
        "population": 20169,
        "housing": 14343,
        "land_area": 57027366.0,
        "water_area": 4211307.0
    },
    "51256": {
        "name": "Longview",
        "state": "Longview TX",
        "population": 107099,
        "housing": 45766,
        "land_area": 233762595.0,
        "water_area": 917375.0
    },
    "51283": {
        "name": "Longview",
        "state": "Longview WA--OR",
        "population": 69841,
        "housing": 29459,
        "land_area": 91145801.0,
        "water_area": 2779977.0
    },
    "51364": {
        "name": "Lorain--Elyria",
        "state": "Lorain--Elyria OH",
        "population": 199067,
        "housing": 91234,
        "land_area": 234690093.0,
        "water_area": 2292904.0
    },
    "51418": {
        "name": "Los Alamos",
        "state": "Los Alamos NM",
        "population": 13283,
        "housing": 6086,
        "land_area": 22069410.0,
        "water_area": 3480.0
    },
    "51445": {
        "name": "Los Angeles--Long Beach--Anaheim",
        "state": "Los Angeles--Long Beach--Anaheim CA",
        "population": 12237376,
        "housing": 4354341,
        "land_area": 4239359620.0,
        "water_area": 48266720.0
    },
    "51472": {
        "name": "Los Banos",
        "state": "Los Banos CA",
        "population": 45533,
        "housing": 13193,
        "land_area": 21772647.0,
        "water_area": 0.0
    },
    "51499": {
        "name": "Los Lunas",
        "state": "Los Lunas NM",
        "population": 53365,
        "housing": 21248,
        "land_area": 102095359.0,
        "water_area": 34322.0
    },
    "51520": {
        "name": "Los Osos",
        "state": "Los Osos CA",
        "population": 13978,
        "housing": 6341,
        "land_area": 9923800.0,
        "water_area": 14474.0
    },
    "51580": {
        "name": "Louisa",
        "state": "Louisa KY--WV",
        "population": 4582,
        "housing": 2114,
        "land_area": 9410544.0,
        "water_area": 215570.0
    },
    "51755": {
        "name": "Louisville/Jefferson County",
        "state": "Louisville/Jefferson County KY--IN",
        "population": 974397,
        "housing": 438588,
        "land_area": 1038222036.0,
        "water_area": 20274471.0
    },
    "51769": {
        "name": "Lovington",
        "state": "Lovington NM",
        "population": 11765,
        "housing": 4266,
        "land_area": 12444138.0,
        "water_area": 16175.0
    },
    "51796": {
        "name": "Lowell",
        "state": "Lowell IN",
        "population": 10747,
        "housing": 4239,
        "land_area": 13663167.0,
        "water_area": 68546.0
    },
    "51823": {
        "name": "Lowell",
        "state": "Lowell MI",
        "population": 7530,
        "housing": 3121,
        "land_area": 14388293.0,
        "water_area": 343358.0
    },
    "51877": {
        "name": "Lubbock",
        "state": "Lubbock TX",
        "population": 272280,
        "housing": 116963,
        "land_area": 275248143.0,
        "water_area": 2166424.0
    },
    "51958": {
        "name": "Ludington",
        "state": "Ludington MI",
        "population": 11883,
        "housing": 7235,
        "land_area": 32881873.0,
        "water_area": 126039.0
    },
    "51985": {
        "name": "Lufkin",
        "state": "Lufkin TX",
        "population": 41551,
        "housing": 17728,
        "land_area": 95156630.0,
        "water_area": 567033.0
    },
    "52039": {
        "name": "Luling",
        "state": "Luling TX",
        "population": 5391,
        "housing": 2218,
        "land_area": 7275021.0,
        "water_area": 0.0
    },
    "52066": {
        "name": "Lumberton",
        "state": "Lumberton NC",
        "population": 22256,
        "housing": 10432,
        "land_area": 52767724.0,
        "water_area": 192072.0
    },
    "52093": {
        "name": "Luray",
        "state": "Luray VA",
        "population": 4742,
        "housing": 2225,
        "land_area": 10615945.0,
        "water_area": 51669.0
    },
    "52147": {
        "name": "Luverne",
        "state": "Luverne MN",
        "population": 4808,
        "housing": 2199,
        "land_area": 5918735.0,
        "water_area": 10612.0
    },
    "52201": {
        "name": "Lynchburg",
        "state": "Lynchburg VA",
        "population": 125596,
        "housing": 54093,
        "land_area": 237236876.0,
        "water_area": 1885255.0
    },
    "52228": {
        "name": "Lynden",
        "state": "Lynden WA",
        "population": 15995,
        "housing": 6246,
        "land_area": 13762353.0,
        "water_area": 28897.0
    },
    "52363": {
        "name": "McAlester",
        "state": "McAlester OK",
        "population": 19542,
        "housing": 8932,
        "land_area": 40215352.0,
        "water_area": 94495.0
    },
    "52390": {
        "name": "McAllen",
        "state": "McAllen TX",
        "population": 779553,
        "housing": 269683,
        "land_area": 844899606.0,
        "water_area": 1681658.0
    },
    "52403": {
        "name": "McCall",
        "state": "McCall ID",
        "population": 3695,
        "housing": 4331,
        "land_area": 15645993.0,
        "water_area": 179097.0
    },
    "52417": {
        "name": "Macclenny",
        "state": "Macclenny FL",
        "population": 10881,
        "housing": 3897,
        "land_area": 21894994.0,
        "water_area": 0.0
    },
    "52471": {
        "name": "McComb",
        "state": "McComb MS",
        "population": 15327,
        "housing": 7396,
        "land_area": 33941567.0,
        "water_area": 154492.0
    },
    "52525": {
        "name": "McCook",
        "state": "McCook NE",
        "population": 7395,
        "housing": 3670,
        "land_area": 13580428.0,
        "water_area": 0.0
    },
    "52552": {
        "name": "McFarland",
        "state": "McFarland CA",
        "population": 14149,
        "housing": 3410,
        "land_area": 4822126.0,
        "water_area": 0.0
    },
    "52606": {
        "name": "McGregor",
        "state": "McGregor TX",
        "population": 5139,
        "housing": 1981,
        "land_area": 6001304.0,
        "water_area": 0.0
    },
    "52623": {
        "name": "McHenry Northwest--Wonder Lake",
        "state": "McHenry Northwest--Wonder Lake IL",
        "population": 5758,
        "housing": 2622,
        "land_area": 6077977.0,
        "water_area": 17689.0
    },
    "52633": {
        "name": "McKenzie",
        "state": "McKenzie TN",
        "population": 5360,
        "housing": 2305,
        "land_area": 11399228.0,
        "water_area": 0.0
    },
    "52672": {
        "name": "McKinleyville",
        "state": "McKinleyville CA",
        "population": 14981,
        "housing": 6655,
        "land_area": 20421720.0,
        "water_area": 29268.0
    },
    "52695": {
        "name": "McKinney--Frisco",
        "state": "McKinney--Frisco TX",
        "population": 504803,
        "housing": 181086,
        "land_area": 392749186.0,
        "water_area": 3806139.0
    },
    "52741": {
        "name": "McMinnville",
        "state": "McMinnville OR",
        "population": 41831,
        "housing": 15738,
        "land_area": 33163576.0,
        "water_area": 0.0
    },
    "52768": {
        "name": "McMinnville",
        "state": "McMinnville TN",
        "population": 15711,
        "housing": 7255,
        "land_area": 34242487.0,
        "water_area": 0.0
    },
    "52795": {
        "name": "Macomb",
        "state": "Macomb IL",
        "population": 15656,
        "housing": 8002,
        "land_area": 22648434.0,
        "water_area": 18498.0
    },
    "52822": {
        "name": "Macon-Bibb County",
        "state": "Macon-Bibb County GA",
        "population": 140111,
        "housing": 65107,
        "land_area": 257116553.0,
        "water_area": 2119150.0
    },
    "52876": {
        "name": "Macon",
        "state": "Macon MO",
        "population": 5359,
        "housing": 2639,
        "land_area": 13151154.0,
        "water_area": 44548.0
    },
    "52903": {
        "name": "McPherson",
        "state": "McPherson KS",
        "population": 14039,
        "housing": 6202,
        "land_area": 22379089.0,
        "water_area": 172060.0
    },
    "52984": {
        "name": "Madera",
        "state": "Madera CA",
        "population": 81635,
        "housing": 22331,
        "land_area": 54285495.0,
        "water_area": 0.0
    },
    "53065": {
        "name": "Madison",
        "state": "Madison GA",
        "population": 4709,
        "housing": 2088,
        "land_area": 12411816.0,
        "water_area": 80529.0
    },
    "53092": {
        "name": "Madison",
        "state": "Madison IN",
        "population": 17447,
        "housing": 7992,
        "land_area": 27894395.0,
        "water_area": 119361.0
    },
    "53146": {
        "name": "Madison",
        "state": "Madison SD",
        "population": 6169,
        "housing": 2931,
        "land_area": 11159434.0,
        "water_area": 0.0
    },
    "53200": {
        "name": "Madison",
        "state": "Madison WI",
        "population": 450305,
        "housing": 203337,
        "land_area": 387731534.0,
        "water_area": 3318120.0
    },
    "53227": {
        "name": "Madisonville",
        "state": "Madisonville KY",
        "population": 21328,
        "housing": 10394,
        "land_area": 41735701.0,
        "water_area": 306462.0
    },
    "53254": {
        "name": "Madisonville",
        "state": "Madisonville TN",
        "population": 6070,
        "housing": 2687,
        "land_area": 18568122.0,
        "water_area": 0.0
    },
    "53308": {
        "name": "Madras",
        "state": "Madras OR",
        "population": 8087,
        "housing": 3131,
        "land_area": 12115401.0,
        "water_area": 0.0
    },
    "53322": {
        "name": "Magalia",
        "state": "Magalia CA",
        "population": 6900,
        "housing": 3039,
        "land_area": 15223754.0,
        "water_area": 7128.0
    },
    "53362": {
        "name": "Magnolia",
        "state": "Magnolia AR",
        "population": 10403,
        "housing": 4483,
        "land_area": 22734546.0,
        "water_area": 89633.0
    },
    "53389": {
        "name": "Mahanoy City",
        "state": "Mahanoy City PA",
        "population": 3720,
        "housing": 2318,
        "land_area": 2148324.0,
        "water_area": 0.0
    },
    "53416": {
        "name": "Mahomet",
        "state": "Mahomet IL",
        "population": 11922,
        "housing": 4713,
        "land_area": 17802857.0,
        "water_area": 138788.0
    },
    "53524": {
        "name": "Malone",
        "state": "Malone NY",
        "population": 6885,
        "housing": 2992,
        "land_area": 8939354.0,
        "water_area": 170722.0
    },
    "53551": {
        "name": "Malvern",
        "state": "Malvern AR",
        "population": 8833,
        "housing": 4218,
        "land_area": 17348394.0,
        "water_area": 0.0
    },
    "53605": {
        "name": "Mammoth Lakes",
        "state": "Mammoth Lakes CA",
        "population": 7045,
        "housing": 9174,
        "land_area": 9738043.0,
        "water_area": 4195.0
    },
    "53686": {
        "name": "Manchester",
        "state": "Manchester IA",
        "population": 4913,
        "housing": 2313,
        "land_area": 9347969.0,
        "water_area": 0.0
    },
    "53740": {
        "name": "Manchester",
        "state": "Manchester NH",
        "population": 163289,
        "housing": 69559,
        "land_area": 208835588.0,
        "water_area": 4928131.0
    },
    "53767": {
        "name": "Manchester",
        "state": "Manchester TN",
        "population": 12953,
        "housing": 5512,
        "land_area": 27032477.0,
        "water_area": 15793.0
    },
    "53794": {
        "name": "Mandeville--Covington",
        "state": "Mandeville--Covington LA",
        "population": 113763,
        "housing": 47372,
        "land_area": 218412386.0,
        "water_area": 4456763.0
    },
    "53834": {
        "name": "Manhattan",
        "state": "Manhattan IL",
        "population": 7826,
        "housing": 2774,
        "land_area": 7602887.0,
        "water_area": 0.0
    },
    "53848": {
        "name": "Manhattan",
        "state": "Manhattan KS",
        "population": 60454,
        "housing": 27064,
        "land_area": 66921857.0,
        "water_area": 151419.0
    },
    "53902": {
        "name": "Manistee",
        "state": "Manistee MI",
        "population": 8093,
        "housing": 4590,
        "land_area": 23524743.0,
        "water_area": 401289.0
    },
    "53956": {
        "name": "Manitowoc",
        "state": "Manitowoc WI",
        "population": 46558,
        "housing": 22208,
        "land_area": 60932687.0,
        "water_area": 2550524.0
    },
    "53983": {
        "name": "Mankato",
        "state": "Mankato MN",
        "population": 60206,
        "housing": 25620,
        "land_area": 66384476.0,
        "water_area": 795096.0
    },
    "54037": {
        "name": "Manning",
        "state": "Manning SC",
        "population": 4522,
        "housing": 2132,
        "land_area": 10047105.0,
        "water_area": 0.0
    },
    "54050": {
        "name": "Manor",
        "state": "Manor TX",
        "population": 17006,
        "housing": 5733,
        "land_area": 13793390.0,
        "water_area": 0.0
    },
    "54064": {
        "name": "Mansfield",
        "state": "Mansfield LA",
        "population": 5602,
        "housing": 2620,
        "land_area": 10513660.0,
        "water_area": 67874.0
    },
    "54091": {
        "name": "Mansfield",
        "state": "Mansfield OH",
        "population": 73545,
        "housing": 33119,
        "land_area": 123501224.0,
        "water_area": 241289.0
    },
    "54145": {
        "name": "Manteca",
        "state": "Manteca CA",
        "population": 86674,
        "housing": 28725,
        "land_area": 53018651.0,
        "water_area": 815328.0
    },
    "54172": {
        "name": "Manteno",
        "state": "Manteno IL",
        "population": 10437,
        "housing": 4475,
        "land_area": 15563649.0,
        "water_area": 245231.0
    },
    "54199": {
        "name": "Manteo",
        "state": "Manteo NC",
        "population": 6070,
        "housing": 3300,
        "land_area": 13654693.0,
        "water_area": 159277.0
    },
    "54280": {
        "name": "Maquoketa",
        "state": "Maquoketa IA",
        "population": 6098,
        "housing": 2884,
        "land_area": 9788754.0,
        "water_area": 16593.0
    },
    "54295": {
        "name": "Marana",
        "state": "Marana AZ",
        "population": 10618,
        "housing": 3623,
        "land_area": 8432271.0,
        "water_area": 0.0
    },
    "54307": {
        "name": "Marathon",
        "state": "Marathon FL",
        "population": 9733,
        "housing": 6963,
        "land_area": 14362899.0,
        "water_area": 5189961.0
    },
    "54334": {
        "name": "Marble Falls",
        "state": "Marble Falls TX",
        "population": 7953,
        "housing": 3692,
        "land_area": 14286889.0,
        "water_area": 522664.0
    },
    "54388": {
        "name": "Marengo",
        "state": "Marengo IL",
        "population": 7509,
        "housing": 3066,
        "land_area": 9864690.0,
        "water_area": 0.0
    },
    "54442": {
        "name": "Marianna",
        "state": "Marianna FL",
        "population": 5560,
        "housing": 2724,
        "land_area": 11257335.0,
        "water_area": 19055.0
    },
    "54477": {
        "name": "Maricopa",
        "state": "Maricopa AZ",
        "population": 57771,
        "housing": 20897,
        "land_area": 34187863.0,
        "water_area": 182664.0
    },
    "54486": {
        "name": "Marietta",
        "state": "Marietta OH--WV",
        "population": 21723,
        "housing": 10388,
        "land_area": 35104150.0,
        "water_area": 1592877.0
    },
    "54496": {
        "name": "Marinette--Menominee",
        "state": "Marinette--Menominee WI--MI",
        "population": 23551,
        "housing": 12010,
        "land_area": 38302038.0,
        "water_area": 1895498.0
    },
    "54577": {
        "name": "Marion",
        "state": "Marion IN",
        "population": 40961,
        "housing": 19146,
        "land_area": 56623179.0,
        "water_area": 195380.0
    },
    "54631": {
        "name": "Marion",
        "state": "Marion NC",
        "population": 12031,
        "housing": 5734,
        "land_area": 35333959.0,
        "water_area": 128055.0
    },
    "54658": {
        "name": "Marion",
        "state": "Marion OH",
        "population": 42688,
        "housing": 17864,
        "land_area": 47637380.0,
        "water_area": 237368.0
    },
    "54685": {
        "name": "Marion",
        "state": "Marion SC",
        "population": 7009,
        "housing": 3307,
        "land_area": 14064797.0,
        "water_area": 0.0
    },
    "54712": {
        "name": "Marion",
        "state": "Marion VA",
        "population": 7281,
        "housing": 3707,
        "land_area": 18565599.0,
        "water_area": 74859.0
    },
    "54722": {
        "name": "Marion--Herrin",
        "state": "Marion--Herrin IL",
        "population": 39391,
        "housing": 19639,
        "land_area": 85398514.0,
        "water_area": 1149612.0
    },
    "54739": {
        "name": "Marion Oaks",
        "state": "Marion Oaks FL",
        "population": 19077,
        "housing": 7620,
        "land_area": 42289206.0,
        "water_area": 16200.0
    },
    "54793": {
        "name": "Marksville",
        "state": "Marksville LA",
        "population": 6682,
        "housing": 3058,
        "land_area": 16607947.0,
        "water_area": 0.0
    },
    "54820": {
        "name": "Marlin",
        "state": "Marlin TX",
        "population": 5396,
        "housing": 2430,
        "land_area": 10625487.0,
        "water_area": 114571.0
    },
    "54874": {
        "name": "Marquette",
        "state": "Marquette MI",
        "population": 24682,
        "housing": 11592,
        "land_area": 32869181.0,
        "water_area": 132763.0
    },
    "54901": {
        "name": "Marseilles",
        "state": "Marseilles IL",
        "population": 4660,
        "housing": 2212,
        "land_area": 6194521.0,
        "water_area": 0.0
    },
    "54955": {
        "name": "Marshall",
        "state": "Marshall MI",
        "population": 7471,
        "housing": 3633,
        "land_area": 11888158.0,
        "water_area": 76114.0
    },
    "54982": {
        "name": "Marshall",
        "state": "Marshall MN",
        "population": 13508,
        "housing": 5939,
        "land_area": 21477700.0,
        "water_area": 0.0
    },
    "55009": {
        "name": "Marshall",
        "state": "Marshall MO",
        "population": 13471,
        "housing": 5261,
        "land_area": 17649776.0,
        "water_area": 186755.0
    },
    "55036": {
        "name": "Marshall",
        "state": "Marshall TX",
        "population": 21387,
        "housing": 8991,
        "land_area": 41867103.0,
        "water_area": 54941.0
    },
    "55090": {
        "name": "Marshalltown",
        "state": "Marshalltown IA",
        "population": 27381,
        "housing": 11219,
        "land_area": 31333179.0,
        "water_area": 26069.0
    },
    "55117": {
        "name": "Marshfield",
        "state": "Marshfield MO",
        "population": 7537,
        "housing": 3204,
        "land_area": 13821165.0,
        "water_area": 8211.0
    },
    "55144": {
        "name": "Marshfield",
        "state": "Marshfield WI",
        "population": 19462,
        "housing": 9721,
        "land_area": 29188732.0,
        "water_area": 0.0
    },
    "55171": {
        "name": "Martin",
        "state": "Martin TN",
        "population": 10518,
        "housing": 4580,
        "land_area": 20815305.0,
        "water_area": 0.0
    },
    "55225": {
        "name": "Martinsville",
        "state": "Martinsville IN",
        "population": 12556,
        "housing": 5560,
        "land_area": 15990759.0,
        "water_area": 485132.0
    },
    "55252": {
        "name": "Martinsville",
        "state": "Martinsville VA",
        "population": 31273,
        "housing": 15866,
        "land_area": 103051287.0,
        "water_area": 220432.0
    },
    "55306": {
        "name": "Marysville",
        "state": "Marysville OH",
        "population": 25674,
        "housing": 9395,
        "land_area": 29932053.0,
        "water_area": 385525.0
    },
    "55333": {
        "name": "Marysville",
        "state": "Marysville WA",
        "population": 160440,
        "housing": 58939,
        "land_area": 175597979.0,
        "water_area": 4885337.0
    },
    "55360": {
        "name": "Maryville",
        "state": "Maryville MO",
        "population": 11212,
        "housing": 5175,
        "land_area": 14538667.0,
        "water_area": 14339.0
    },
    "55375": {
        "name": "Mascoutah",
        "state": "Mascoutah IL",
        "population": 8528,
        "housing": 3354,
        "land_area": 8316671.0,
        "water_area": 140230.0
    },
    "55414": {
        "name": "Mason City",
        "state": "Mason City IA",
        "population": 25954,
        "housing": 13118,
        "land_area": 37986222.0,
        "water_area": 709358.0
    },
    "55468": {
        "name": "Massena",
        "state": "Massena NY",
        "population": 10582,
        "housing": 5144,
        "land_area": 15975506.0,
        "water_area": 856771.0
    },
    "55495": {
        "name": "Mathis",
        "state": "Mathis TX",
        "population": 5253,
        "housing": 2306,
        "land_area": 8399920.0,
        "water_area": 0.0
    },
    "55534": {
        "name": "Mattawan",
        "state": "Mattawan MI",
        "population": 5721,
        "housing": 2197,
        "land_area": 16429467.0,
        "water_area": 166383.0
    },
    "55576": {
        "name": "Mattoon",
        "state": "Mattoon IL",
        "population": 17111,
        "housing": 8711,
        "land_area": 21610579.0,
        "water_area": 46655.0
    },
    "55603": {
        "name": "Mauldin--Simpsonville",
        "state": "Mauldin--Simpsonville SC",
        "population": 159506,
        "housing": 64676,
        "land_area": 262115104.0,
        "water_area": 2197725.0
    },
    "55738": {
        "name": "Mayag\u00fcez",
        "state": "Mayag\u00fcez PR",
        "population": 91583,
        "housing": 49990,
        "land_area": 129874694.0,
        "water_area": 88016.0
    },
    "55765": {
        "name": "Mayfield",
        "state": "Mayfield KY",
        "population": 12256,
        "housing": 5494,
        "land_area": 24373241.0,
        "water_area": 157581.0
    },
    "55792": {
        "name": "Mayodan",
        "state": "Mayodan NC",
        "population": 4627,
        "housing": 2554,
        "land_area": 11895807.0,
        "water_area": 20003.0
    },
    "55819": {
        "name": "Maysville",
        "state": "Maysville KY--OH",
        "population": 9799,
        "housing": 5071,
        "land_area": 23205815.0,
        "water_area": 3909454.0
    },
    "55832": {
        "name": "Maytown--Marietta",
        "state": "Maytown--Marietta PA",
        "population": 7737,
        "housing": 3179,
        "land_area": 8112523.0,
        "water_area": 17574.0
    },
    "55846": {
        "name": "Mayville",
        "state": "Mayville WI",
        "population": 5189,
        "housing": 2386,
        "land_area": 7103720.0,
        "water_area": 227057.0
    },
    "55900": {
        "name": "Meadville",
        "state": "Meadville PA",
        "population": 20652,
        "housing": 9614,
        "land_area": 40818923.0,
        "water_area": 16558.0
    },
    "55927": {
        "name": "Mecca",
        "state": "Mecca CA",
        "population": 6875,
        "housing": 1669,
        "land_area": 1621795.0,
        "water_area": 0.0
    },
    "55954": {
        "name": "Mechanicville",
        "state": "Mechanicville NY",
        "population": 11799,
        "housing": 5504,
        "land_area": 16949082.0,
        "water_area": 56036.0
    },
    "55981": {
        "name": "Medford",
        "state": "Medford OR",
        "population": 171640,
        "housing": 73280,
        "land_area": 163221156.0,
        "water_area": 392704.0
    },
    "56008": {
        "name": "Medford",
        "state": "Medford WI",
        "population": 4114,
        "housing": 2047,
        "land_area": 7851984.0,
        "water_area": 74100.0
    },
    "56035": {
        "name": "Medina",
        "state": "Medina NY",
        "population": 6279,
        "housing": 2930,
        "land_area": 7914029.0,
        "water_area": 187468.0
    },
    "56062": {
        "name": "Medina",
        "state": "Medina OH",
        "population": 46109,
        "housing": 19272,
        "land_area": 71948128.0,
        "water_area": 718382.0
    },
    "56074": {
        "name": "Melissa--Anna",
        "state": "Melissa--Anna TX",
        "population": 34516,
        "housing": 11551,
        "land_area": 43891925.0,
        "water_area": 188106.0
    },
    "56116": {
        "name": "Memphis",
        "state": "Memphis TN--MS--AR",
        "population": 1056190,
        "housing": 452043,
        "land_area": 1272376010.0,
        "water_area": 9237649.0
    },
    "56143": {
        "name": "Mena",
        "state": "Mena AR",
        "population": 5534,
        "housing": 2789,
        "land_area": 18052240.0,
        "water_area": 120057.0
    },
    "56170": {
        "name": "Mendota",
        "state": "Mendota CA",
        "population": 13382,
        "housing": 2875,
        "land_area": 11041564.0,
        "water_area": 8333.0
    },
    "56197": {
        "name": "Mendota",
        "state": "Mendota IL",
        "population": 6918,
        "housing": 3043,
        "land_area": 7384990.0,
        "water_area": 70458.0
    },
    "56224": {
        "name": "Menomonie",
        "state": "Menomonie WI",
        "population": 17022,
        "housing": 6665,
        "land_area": 27518962.0,
        "water_area": 4140556.0
    },
    "56251": {
        "name": "Merced",
        "state": "Merced CA",
        "population": 150052,
        "housing": 47917,
        "land_area": 111568397.0,
        "water_area": 30577.0
    },
    "56290": {
        "name": "Meredith",
        "state": "Meredith NH",
        "population": 3411,
        "housing": 2391,
        "land_area": 12691342.0,
        "water_area": 5352140.0
    },
    "56305": {
        "name": "Meridian",
        "state": "Meridian MS",
        "population": 33809,
        "housing": 16621,
        "land_area": 76345986.0,
        "water_area": 420299.0
    },
    "56359": {
        "name": "Merrill",
        "state": "Merrill WI",
        "population": 9519,
        "housing": 4573,
        "land_area": 19472053.0,
        "water_area": 1654604.0
    },
    "56413": {
        "name": "Mesquite",
        "state": "Mesquite NV--AZ",
        "population": 19206,
        "housing": 10498,
        "land_area": 25823275.0,
        "water_area": 0.0
    },
    "56467": {
        "name": "Metropolis",
        "state": "Metropolis IL",
        "population": 5925,
        "housing": 3004,
        "land_area": 7738971.0,
        "water_area": 6733.0
    },
    "56521": {
        "name": "Mexia",
        "state": "Mexia TX",
        "population": 6661,
        "housing": 2695,
        "land_area": 11631870.0,
        "water_area": 83451.0
    },
    "56548": {
        "name": "Mexico",
        "state": "Mexico MO",
        "population": 11351,
        "housing": 5198,
        "land_area": 17383043.0,
        "water_area": 155206.0
    },
    "56602": {
        "name": "Miami--Fort Lauderdale",
        "state": "Miami--Fort Lauderdale FL",
        "population": 6077522,
        "housing": 2622231,
        "land_area": 3222398623.0,
        "water_area": 227048874.0
    },
    "56629": {
        "name": "Miami",
        "state": "Miami OK",
        "population": 15348,
        "housing": 6901,
        "land_area": 23840055.0,
        "water_area": 27471.0
    },
    "56656": {
        "name": "Michigan City--La Porte",
        "state": "Michigan City--La Porte IN--MI",
        "population": 71367,
        "housing": 37009,
        "land_area": 127322729.0,
        "water_area": 4287181.0
    },
    "56737": {
        "name": "Middlebury",
        "state": "Middlebury VT",
        "population": 6154,
        "housing": 1939,
        "land_area": 11337138.0,
        "water_area": 266537.0
    },
    "56791": {
        "name": "Middleport",
        "state": "Middleport OH--WV",
        "population": 5814,
        "housing": 3047,
        "land_area": 14281906.0,
        "water_area": 1443599.0
    },
    "56818": {
        "name": "Middlesborough",
        "state": "Middlesborough KY--TN",
        "population": 13628,
        "housing": 6579,
        "land_area": 30537568.0,
        "water_area": 253480.0
    },
    "56829": {
        "name": "Middleton",
        "state": "Middleton ID",
        "population": 10265,
        "housing": 3418,
        "land_area": 11570866.0,
        "water_area": 56309.0
    },
    "56845": {
        "name": "Middletown",
        "state": "Middletown DE",
        "population": 41851,
        "housing": 14796,
        "land_area": 62573948.0,
        "water_area": 282510.0
    },
    "56899": {
        "name": "Middletown",
        "state": "Middletown NY",
        "population": 61516,
        "housing": 24531,
        "land_area": 67238774.0,
        "water_area": 347074.0
    },
    "56926": {
        "name": "Middletown",
        "state": "Middletown OH",
        "population": 93608,
        "housing": 37928,
        "land_area": 138863196.0,
        "water_area": 904432.0
    },
    "56980": {
        "name": "Midland",
        "state": "Midland MI",
        "population": 52340,
        "housing": 23885,
        "land_area": 98795707.0,
        "water_area": 985333.0
    },
    "57007": {
        "name": "Midland",
        "state": "Midland TX",
        "population": 141997,
        "housing": 59089,
        "land_area": 176864141.0,
        "water_area": 145403.0
    },
    "57034": {
        "name": "Midlothian",
        "state": "Midlothian TX",
        "population": 30908,
        "housing": 10511,
        "land_area": 64025479.0,
        "water_area": 530345.0
    },
    "57088": {
        "name": "Milan",
        "state": "Milan MI",
        "population": 7861,
        "housing": 2674,
        "land_area": 8789762.0,
        "water_area": 210930.0
    },
    "57115": {
        "name": "Milan",
        "state": "Milan TN",
        "population": 7578,
        "housing": 3420,
        "land_area": 15535969.0,
        "water_area": 0.0
    },
    "57169": {
        "name": "Miles City",
        "state": "Miles City MT",
        "population": 9227,
        "housing": 4518,
        "land_area": 12002319.0,
        "water_area": 6842.0
    },
    "57196": {
        "name": "Milford",
        "state": "Milford DE",
        "population": 17754,
        "housing": 7450,
        "land_area": 31005770.0,
        "water_area": 557134.0
    },
    "57223": {
        "name": "Milledgeville",
        "state": "Milledgeville GA",
        "population": 22441,
        "housing": 10341,
        "land_area": 51239783.0,
        "water_area": 46821.0
    },
    "57304": {
        "name": "Millersburg",
        "state": "Millersburg PA",
        "population": 4375,
        "housing": 2147,
        "land_area": 4850877.0,
        "water_area": 0.0
    },
    "57319": {
        "name": "Millington",
        "state": "Millington TN",
        "population": 12918,
        "housing": 5625,
        "land_area": 32863738.0,
        "water_area": 40648.0
    },
    "57331": {
        "name": "Millinocket",
        "state": "Millinocket ME",
        "population": 3812,
        "housing": 2360,
        "land_area": 8539488.0,
        "water_area": 133543.0
    },
    "57358": {
        "name": "Millsboro",
        "state": "Millsboro DE",
        "population": 9844,
        "housing": 4574,
        "land_area": 17182721.0,
        "water_area": 190757.0
    },
    "57402": {
        "name": "Milton",
        "state": "Milton VT",
        "population": 6417,
        "housing": 2701,
        "land_area": 14324910.0,
        "water_area": 219528.0
    },
    "57412": {
        "name": "Milton-Freewater",
        "state": "Milton-Freewater OR",
        "population": 8131,
        "housing": 3205,
        "land_area": 8639754.0,
        "water_area": 0.0
    },
    "57439": {
        "name": "Milton",
        "state": "Milton PA",
        "population": 28610,
        "housing": 12335,
        "land_area": 46394733.0,
        "water_area": 96619.0
    },
    "57466": {
        "name": "Milwaukee",
        "state": "Milwaukee WI",
        "population": 1306795,
        "housing": 582330,
        "land_area": 1200931774.0,
        "water_area": 26115240.0
    },
    "57493": {
        "name": "Minden",
        "state": "Minden LA",
        "population": 12659,
        "housing": 6136,
        "land_area": 33455136.0,
        "water_area": 233934.0
    },
    "57547": {
        "name": "Mineola",
        "state": "Mineola TX",
        "population": 5699,
        "housing": 2495,
        "land_area": 14647065.0,
        "water_area": 153653.0
    },
    "57574": {
        "name": "Mineral Wells",
        "state": "Mineral Wells TX",
        "population": 14211,
        "housing": 5987,
        "land_area": 22936185.0,
        "water_area": 67060.0
    },
    "57628": {
        "name": "Minneapolis--St. Paul",
        "state": "Minneapolis--St. Paul MN",
        "population": 2914866,
        "housing": 1198573,
        "land_area": 2628301524.0,
        "water_area": 173442910.0
    },
    "57655": {
        "name": "Minot",
        "state": "Minot ND",
        "population": 50925,
        "housing": 24508,
        "land_area": 69075358.0,
        "water_area": 88432.0
    },
    "57698": {
        "name": "Minster--New Bremen",
        "state": "Minster--New Bremen OH",
        "population": 6138,
        "housing": 2459,
        "land_area": 10823247.0,
        "water_area": 0.0
    },
    "57709": {
        "name": "Mission Viejo--Lake Forest--Laguna Niguel",
        "state": "Mission Viejo--Lake Forest--Laguna Niguel CA",
        "population": 646843,
        "housing": 261622,
        "land_area": 423806207.0,
        "water_area": 1661222.0
    },
    "57736": {
        "name": "Missoula",
        "state": "Missoula MT",
        "population": 88109,
        "housing": 41026,
        "land_area": 114715728.0,
        "water_area": 587170.0
    },
    "57817": {
        "name": "Mitchell",
        "state": "Mitchell SD",
        "population": 15690,
        "housing": 7698,
        "land_area": 23603242.0,
        "water_area": 2760651.0
    },
    "57844": {
        "name": "Moab",
        "state": "Moab UT",
        "population": 7933,
        "housing": 4167,
        "land_area": 17145883.0,
        "water_area": 0.0
    },
    "57898": {
        "name": "Moberly",
        "state": "Moberly MO",
        "population": 12163,
        "housing": 5797,
        "land_area": 29596426.0,
        "water_area": 25830.0
    },
    "57925": {
        "name": "Mobile",
        "state": "Mobile AL",
        "population": 321907,
        "housing": 145122,
        "land_area": 571733437.0,
        "water_area": 10111122.0
    },
    "57979": {
        "name": "Mocksville",
        "state": "Mocksville NC",
        "population": 5971,
        "housing": 2594,
        "land_area": 16582694.0,
        "water_area": 41754.0
    },
    "58006": {
        "name": "Modesto",
        "state": "Modesto CA",
        "population": 357301,
        "housing": 117353,
        "land_area": 182281708.0,
        "water_area": 938923.0
    },
    "58033": {
        "name": "Molalla",
        "state": "Molalla OR",
        "population": 10258,
        "housing": 3736,
        "land_area": 6451727.0,
        "water_area": 0.0
    },
    "58087": {
        "name": "Monahans",
        "state": "Monahans TX",
        "population": 9162,
        "housing": 4019,
        "land_area": 18911937.0,
        "water_area": 31467.0
    },
    "58162": {
        "name": "Monessen",
        "state": "Monessen PA",
        "population": 49962,
        "housing": 25911,
        "land_area": 85404826.0,
        "water_area": 5847820.0
    },
    "58195": {
        "name": "Monett",
        "state": "Monett MO",
        "population": 9391,
        "housing": 3860,
        "land_area": 14926723.0,
        "water_area": 38941.0
    },
    "58222": {
        "name": "Monmouth",
        "state": "Monmouth IL",
        "population": 9189,
        "housing": 3890,
        "land_area": 10760117.0,
        "water_area": 53160.0
    },
    "58249": {
        "name": "Monmouth--Independence",
        "state": "Monmouth--Independence OR",
        "population": 20912,
        "housing": 7434,
        "land_area": 12791770.0,
        "water_area": 207148.0
    },
    "58303": {
        "name": "Monroe",
        "state": "Monroe GA",
        "population": 16650,
        "housing": 6755,
        "land_area": 33880910.0,
        "water_area": 295616.0
    },
    "58330": {
        "name": "Monroe",
        "state": "Monroe LA",
        "population": 119964,
        "housing": 53099,
        "land_area": 235612605.0,
        "water_area": 5693775.0
    },
    "58357": {
        "name": "Monroe",
        "state": "Monroe MI",
        "population": 57260,
        "housing": 24843,
        "land_area": 91543157.0,
        "water_area": 1899420.0
    },
    "58369": {
        "name": "Monroe",
        "state": "Monroe WA",
        "population": 24635,
        "housing": 8004,
        "land_area": 29687874.0,
        "water_area": 156304.0
    },
    "58384": {
        "name": "Monroe",
        "state": "Monroe WI",
        "population": 10725,
        "housing": 5149,
        "land_area": 11327878.0,
        "water_area": 0.0
    },
    "58411": {
        "name": "Monroeville",
        "state": "Monroeville AL",
        "population": 4284,
        "housing": 2229,
        "land_area": 12581347.0,
        "water_area": 0.0
    },
    "58420": {
        "name": "Montauk",
        "state": "Montauk NY",
        "population": 3845,
        "housing": 3811,
        "land_area": 15350034.0,
        "water_area": 696073.0
    },
    "58429": {
        "name": "Mont Belvieu",
        "state": "Mont Belvieu TX",
        "population": 12180,
        "housing": 4061,
        "land_area": 27955192.0,
        "water_area": 157513.0
    },
    "58469": {
        "name": "Montesano--Elma",
        "state": "Montesano--Elma WA",
        "population": 12682,
        "housing": 5360,
        "land_area": 25946997.0,
        "water_area": 119784.0
    },
    "58492": {
        "name": "Montevallo",
        "state": "Montevallo AL",
        "population": 6438,
        "housing": 2579,
        "land_area": 13129334.0,
        "water_area": 64582.0
    },
    "58519": {
        "name": "Montevideo",
        "state": "Montevideo MN",
        "population": 5391,
        "housing": 2478,
        "land_area": 10575548.0,
        "water_area": 128550.0
    },
    "58600": {
        "name": "Montgomery",
        "state": "Montgomery AL",
        "population": 254348,
        "housing": 115435,
        "land_area": 375806552.0,
        "water_area": 2449268.0
    },
    "58708": {
        "name": "Monticello",
        "state": "Monticello AR",
        "population": 7974,
        "housing": 3906,
        "land_area": 20154056.0,
        "water_area": 69761.0
    },
    "58735": {
        "name": "Monticello",
        "state": "Monticello IL",
        "population": 5985,
        "housing": 2605,
        "land_area": 10153542.0,
        "water_area": 5128.0
    },
    "58762": {
        "name": "Monticello",
        "state": "Monticello IN",
        "population": 10635,
        "housing": 7338,
        "land_area": 33976761.0,
        "water_area": 168690.0
    },
    "58816": {
        "name": "Monticello",
        "state": "Monticello KY",
        "population": 6681,
        "housing": 2958,
        "land_area": 16714040.0,
        "water_area": 104567.0
    },
    "58830": {
        "name": "Monticello",
        "state": "Monticello MN",
        "population": 15760,
        "housing": 6112,
        "land_area": 22440549.0,
        "water_area": 1682990.0
    },
    "58843": {
        "name": "Monticello",
        "state": "Monticello NY",
        "population": 14328,
        "housing": 7407,
        "land_area": 25512157.0,
        "water_area": 1605299.0
    },
    "58951": {
        "name": "Montrose",
        "state": "Montrose CO",
        "population": 24513,
        "housing": 11114,
        "land_area": 50460848.0,
        "water_area": 0.0
    },
    "58961": {
        "name": "Montrose",
        "state": "Montrose MN",
        "population": 5539,
        "housing": 2102,
        "land_area": 7129331.0,
        "water_area": 1952729.0
    },
    "59167": {
        "name": "Morehead",
        "state": "Morehead KY",
        "population": 9375,
        "housing": 3651,
        "land_area": 21037256.0,
        "water_area": 315358.0
    },
    "59194": {
        "name": "Morehead City--Emerald Isle--Atlantic Beach",
        "state": "Morehead City--Emerald Isle--Atlantic Beach NC",
        "population": 44300,
        "housing": 37416,
        "land_area": 121522539.0,
        "water_area": 3429352.0
    },
    "59221": {
        "name": "Morgan City",
        "state": "Morgan City LA",
        "population": 30236,
        "housing": 13561,
        "land_area": 51105272.0,
        "water_area": 2337769.0
    },
    "59275": {
        "name": "Morgantown",
        "state": "Morgantown WV",
        "population": 77620,
        "housing": 37959,
        "land_area": 105409636.0,
        "water_area": 2707689.0
    },
    "59302": {
        "name": "Morrilton",
        "state": "Morrilton AR",
        "population": 6340,
        "housing": 3020,
        "land_area": 14817515.0,
        "water_area": 167130.0
    },
    "59329": {
        "name": "Morris",
        "state": "Morris IL",
        "population": 15740,
        "housing": 6956,
        "land_area": 22372306.0,
        "water_area": 330128.0
    },
    "59356": {
        "name": "Morris",
        "state": "Morris MN",
        "population": 5030,
        "housing": 2297,
        "land_area": 8709379.0,
        "water_area": 0.0
    },
    "59410": {
        "name": "Morristown",
        "state": "Morristown TN",
        "population": 66539,
        "housing": 28535,
        "land_area": 165513816.0,
        "water_area": 186099.0
    },
    "59430": {
        "name": "Morro Bay",
        "state": "Morro Bay CA",
        "population": 13163,
        "housing": 9002,
        "land_area": 13744567.0,
        "water_area": 2990.0
    },
    "59491": {
        "name": "Moscow",
        "state": "Moscow ID",
        "population": 25914,
        "housing": 11301,
        "land_area": 16588892.0,
        "water_area": 8987.0
    },
    "59518": {
        "name": "Moses Lake",
        "state": "Moses Lake WA",
        "population": 38751,
        "housing": 15228,
        "land_area": 71676230.0,
        "water_area": 9828253.0
    },
    "59572": {
        "name": "Moultrie",
        "state": "Moultrie GA",
        "population": 19217,
        "housing": 8379,
        "land_area": 49860266.0,
        "water_area": 639014.0
    },
    "59590": {
        "name": "Moundsville",
        "state": "Moundsville WV",
        "population": 11398,
        "housing": 5613,
        "land_area": 11528556.0,
        "water_area": 1220.0
    },
    "59653": {
        "name": "Mountain Grove",
        "state": "Mountain Grove MO",
        "population": 4219,
        "housing": 2207,
        "land_area": 8307169.0,
        "water_area": 80485.0
    },
    "59680": {
        "name": "Mountain Home",
        "state": "Mountain Home AR",
        "population": 17783,
        "housing": 9114,
        "land_area": 41778578.0,
        "water_area": 40689.0
    },
    "59707": {
        "name": "Mountain Home",
        "state": "Mountain Home ID",
        "population": 17799,
        "housing": 7378,
        "land_area": 16983899.0,
        "water_area": 12773.0
    },
    "59761": {
        "name": "Mountain Lake Park",
        "state": "Mountain Lake Park MD",
        "population": 4548,
        "housing": 2223,
        "land_area": 11327779.0,
        "water_area": 41325.0
    },
    "59779": {
        "name": "Mountain Top",
        "state": "Mountain Top PA",
        "population": 10520,
        "housing": 4210,
        "land_area": 26487565.0,
        "water_area": 0.0
    },
    "59815": {
        "name": "Mount Airy",
        "state": "Mount Airy NC",
        "population": 17354,
        "housing": 8544,
        "land_area": 52666071.0,
        "water_area": 439193.0
    },
    "59842": {
        "name": "Mount Carmel",
        "state": "Mount Carmel IL",
        "population": 6963,
        "housing": 3435,
        "land_area": 11126845.0,
        "water_area": 0.0
    },
    "59950": {
        "name": "Mount Horeb",
        "state": "Mount Horeb WI",
        "population": 7730,
        "housing": 3116,
        "land_area": 7731701.0,
        "water_area": 0.0
    },
    "60085": {
        "name": "Mount Pleasant",
        "state": "Mount Pleasant IA",
        "population": 9284,
        "housing": 3508,
        "land_area": 17746275.0,
        "water_area": 9043.0
    },
    "60112": {
        "name": "Mount Pleasant",
        "state": "Mount Pleasant MI",
        "population": 30738,
        "housing": 13793,
        "land_area": 38235935.0,
        "water_area": 465999.0
    },
    "60166": {
        "name": "Mount Pleasant",
        "state": "Mount Pleasant TX",
        "population": 15419,
        "housing": 5636,
        "land_area": 32981554.0,
        "water_area": 840959.0
    },
    "60184": {
        "name": "Mount Plymouth",
        "state": "Mount Plymouth FL",
        "population": 6165,
        "housing": 2378,
        "land_area": 10444399.0,
        "water_area": 407539.0
    },
    "60223": {
        "name": "Mount Shasta",
        "state": "Mount Shasta CA",
        "population": 5203,
        "housing": 3032,
        "land_area": 15615039.0,
        "water_area": 11777.0
    },
    "60274": {
        "name": "Mount Sterling",
        "state": "Mount Sterling KY",
        "population": 13920,
        "housing": 6211,
        "land_area": 30253190.0,
        "water_area": 132150.0
    },
    "60328": {
        "name": "Mount Vernon",
        "state": "Mount Vernon IL",
        "population": 15288,
        "housing": 7686,
        "land_area": 33183843.0,
        "water_area": 239155.0
    },
    "60355": {
        "name": "Mount Vernon",
        "state": "Mount Vernon IN",
        "population": 6715,
        "housing": 3164,
        "land_area": 12534922.0,
        "water_area": 61424.0
    },
    "60382": {
        "name": "Mount Vernon",
        "state": "Mount Vernon IA",
        "population": 6509,
        "housing": 2322,
        "land_area": 8695639.0,
        "water_area": 16175.0
    },
    "60463": {
        "name": "Mount Vernon",
        "state": "Mount Vernon OH",
        "population": 18993,
        "housing": 8460,
        "land_area": 24589685.0,
        "water_area": 424832.0
    },
    "60490": {
        "name": "Mount Vernon",
        "state": "Mount Vernon WA",
        "population": 66825,
        "housing": 25909,
        "land_area": 79418079.0,
        "water_area": 1442002.0
    },
    "60502": {
        "name": "Mount Washington",
        "state": "Mount Washington KY",
        "population": 21516,
        "housing": 8377,
        "land_area": 33680478.0,
        "water_area": 168692.0
    },
    "60517": {
        "name": "Mukwonago",
        "state": "Mukwonago WI",
        "population": 15287,
        "housing": 6230,
        "land_area": 41035485.0,
        "water_area": 2960922.0
    },
    "60571": {
        "name": "Muleshoe",
        "state": "Muleshoe TX",
        "population": 5159,
        "housing": 1944,
        "land_area": 6955733.0,
        "water_area": 0.0
    },
    "60598": {
        "name": "Mullins",
        "state": "Mullins SC",
        "population": 4924,
        "housing": 2532,
        "land_area": 10386047.0,
        "water_area": 0.0
    },
    "60625": {
        "name": "Muncie",
        "state": "Muncie IN",
        "population": 84382,
        "housing": 39372,
        "land_area": 124842172.0,
        "water_area": 650397.0
    },
    "60652": {
        "name": "Muncy",
        "state": "Muncy PA",
        "population": 7544,
        "housing": 3578,
        "land_area": 12917221.0,
        "water_area": 46190.0
    },
    "60663": {
        "name": "Munds Park",
        "state": "Munds Park AZ",
        "population": 773,
        "housing": 2140,
        "land_area": 3093852.0,
        "water_area": 0.0
    },
    "60733": {
        "name": "Murfreesboro",
        "state": "Murfreesboro TN",
        "population": 177313,
        "housing": 71867,
        "land_area": 223081412.0,
        "water_area": 343959.0
    },
    "60787": {
        "name": "Murray",
        "state": "Murray KY",
        "population": 18958,
        "housing": 8635,
        "land_area": 30659893.0,
        "water_area": 41062.0
    },
    "60814": {
        "name": "Muscatine",
        "state": "Muscatine IA",
        "population": 25144,
        "housing": 10949,
        "land_area": 41525970.0,
        "water_area": 103521.0
    },
    "60841": {
        "name": "Muskegon--Norton Shores",
        "state": "Muskegon--Norton Shores MI",
        "population": 166414,
        "housing": 72854,
        "land_area": 290242927.0,
        "water_area": 16990784.0
    },
    "60868": {
        "name": "Muskogee",
        "state": "Muskogee OK",
        "population": 35798,
        "housing": 16883,
        "land_area": 79354332.0,
        "water_area": 143679.0
    },
    "60895": {
        "name": "Myrtle Beach--North Myrtle Beach",
        "state": "Myrtle Beach--North Myrtle Beach SC--NC",
        "population": 298954,
        "housing": 193144,
        "land_area": 567047411.0,
        "water_area": 12448883.0
    },
    "60930": {
        "name": "Mystic Island--Little Egg Harbor",
        "state": "Mystic Island--Little Egg Harbor NJ",
        "population": 23074,
        "housing": 12466,
        "land_area": 33603836.0,
        "water_area": 2409781.0
    },
    "60949": {
        "name": "Nacogdoches",
        "state": "Nacogdoches TX",
        "population": 33732,
        "housing": 14961,
        "land_area": 64761902.0,
        "water_area": 197886.0
    },
    "60976": {
        "name": "Nampa",
        "state": "Nampa ID",
        "population": 177561,
        "housing": 62411,
        "land_area": 170587916.0,
        "water_area": 428457.0
    },
    "61003": {
        "name": "Nantucket",
        "state": "Nantucket MA",
        "population": 12011,
        "housing": 8520,
        "land_area": 30176195.0,
        "water_area": 600790.0
    },
    "61057": {
        "name": "Napa",
        "state": "Napa CA",
        "population": 84619,
        "housing": 33898,
        "land_area": 52699865.0,
        "water_area": 434371.0
    },
    "61084": {
        "name": "Napoleon",
        "state": "Napoleon OH",
        "population": 8868,
        "housing": 4104,
        "land_area": 16164517.0,
        "water_area": 534040.0
    },
    "61111": {
        "name": "Nappanee",
        "state": "Nappanee IN",
        "population": 7250,
        "housing": 3058,
        "land_area": 11190893.0,
        "water_area": 3011.0
    },
    "61165": {
        "name": "Nashua",
        "state": "Nashua NH--MA",
        "population": 242984,
        "housing": 99686,
        "land_area": 506500926.0,
        "water_area": 9287544.0
    },
    "61219": {
        "name": "Nashville",
        "state": "Nashville GA",
        "population": 4844,
        "housing": 2135,
        "land_area": 9393106.0,
        "water_area": 114650.0
    },
    "61273": {
        "name": "Nashville-Davidson",
        "state": "Nashville-Davidson TN",
        "population": 1158642,
        "housing": 496886,
        "land_area": 1515023996.0,
        "water_area": 13747388.0
    },
    "61327": {
        "name": "Natchez",
        "state": "Natchez MS--LA",
        "population": 25902,
        "housing": 13183,
        "land_area": 57990889.0,
        "water_area": 38388.0
    },
    "61354": {
        "name": "Natchitoches",
        "state": "Natchitoches LA",
        "population": 18935,
        "housing": 8777,
        "land_area": 28661363.0,
        "water_area": 1203849.0
    },
    "61372": {
        "name": "Navarre--Miramar Beach--Destin",
        "state": "Navarre--Miramar Beach--Destin FL",
        "population": 226213,
        "housing": 121681,
        "land_area": 309811016.0,
        "water_area": 21561480.0
    },
    "61381": {
        "name": "Navasota",
        "state": "Navasota TX",
        "population": 7458,
        "housing": 3021,
        "land_area": 18281173.0,
        "water_area": 104492.0
    },
    "61408": {
        "name": "Nebraska City",
        "state": "Nebraska City NE",
        "population": 7020,
        "housing": 3090,
        "land_area": 10855932.0,
        "water_area": 0.0
    },
    "61435": {
        "name": "Needles",
        "state": "Needles CA--AZ",
        "population": 6739,
        "housing": 3911,
        "land_area": 14377299.0,
        "water_area": 452818.0
    },
    "61489": {
        "name": "Nelsonville",
        "state": "Nelsonville OH",
        "population": 4709,
        "housing": 2536,
        "land_area": 8068784.0,
        "water_area": 11451.0
    },
    "61543": {
        "name": "Neosho",
        "state": "Neosho MO",
        "population": 12580,
        "housing": 5231,
        "land_area": 25274624.0,
        "water_area": 25849.0
    },
    "61570": {
        "name": "Nephi",
        "state": "Nephi UT",
        "population": 6330,
        "housing": 2117,
        "land_area": 9098316.0,
        "water_area": 0.0
    },
    "61597": {
        "name": "Nevada",
        "state": "Nevada IA",
        "population": 6881,
        "housing": 3111,
        "land_area": 9541096.0,
        "water_area": 28250.0
    },
    "61624": {
        "name": "Nevada",
        "state": "Nevada MO",
        "population": 8529,
        "housing": 4054,
        "land_area": 15836504.0,
        "water_area": 87486.0
    },
    "61651": {
        "name": "New Albany",
        "state": "New Albany MS",
        "population": 6763,
        "housing": 3134,
        "land_area": 21272280.0,
        "water_area": 102687.0
    },
    "61678": {
        "name": "Newark",
        "state": "Newark NY",
        "population": 13568,
        "housing": 6590,
        "land_area": 22865679.0,
        "water_area": 265163.0
    },
    "61705": {
        "name": "Newark",
        "state": "Newark OH",
        "population": 81223,
        "housing": 36427,
        "land_area": 114192298.0,
        "water_area": 8906643.0
    },
    "61786": {
        "name": "New Bedford",
        "state": "New Bedford MA",
        "population": 155491,
        "housing": 68020,
        "land_area": 153604036.0,
        "water_area": 12950793.0
    },
    "61813": {
        "name": "Newberg",
        "state": "Newberg OR",
        "population": 30893,
        "housing": 11645,
        "land_area": 27611058.0,
        "water_area": 41162.0
    },
    "61840": {
        "name": "New Bern",
        "state": "New Bern NC",
        "population": 47988,
        "housing": 23605,
        "land_area": 106923376.0,
        "water_area": 3456364.0
    },
    "61894": {
        "name": "Newberry",
        "state": "Newberry SC",
        "population": 12342,
        "housing": 5409,
        "land_area": 26982513.0,
        "water_area": 31045.0
    },
    "61921": {
        "name": "New Boston",
        "state": "New Boston TX",
        "population": 4502,
        "housing": 2138,
        "land_area": 9654465.0,
        "water_area": 6794.0
    },
    "61948": {
        "name": "New Braunfels",
        "state": "New Braunfels TX",
        "population": 100736,
        "housing": 41658,
        "land_area": 130603555.0,
        "water_area": 2143743.0
    },
    "62083": {
        "name": "New Carlisle",
        "state": "New Carlisle OH",
        "population": 5507,
        "housing": 2331,
        "land_area": 3948277.0,
        "water_area": 22708.0
    },
    "62110": {
        "name": "New Castle",
        "state": "New Castle CO",
        "population": 5844,
        "housing": 2228,
        "land_area": 5749512.0,
        "water_area": 71940.0
    },
    "62137": {
        "name": "New Castle",
        "state": "New Castle IN",
        "population": 18555,
        "housing": 9020,
        "land_area": 19867719.0,
        "water_area": 47622.0
    },
    "62164": {
        "name": "New Castle",
        "state": "New Castle PA",
        "population": 40243,
        "housing": 19179,
        "land_area": 74146683.0,
        "water_area": 732171.0
    },
    "62353": {
        "name": "New Freedom--Shrewsbury",
        "state": "New Freedom--Shrewsbury PA",
        "population": 12094,
        "housing": 4942,
        "land_area": 15849482.0,
        "water_area": 22309.0
    },
    "62407": {
        "name": "New Haven",
        "state": "New Haven CT",
        "population": 561456,
        "housing": 245569,
        "land_area": 771848509.0,
        "water_area": 25633001.0
    },
    "62434": {
        "name": "New Iberia",
        "state": "New Iberia LA",
        "population": 37897,
        "housing": 17163,
        "land_area": 64278368.0,
        "water_area": 701477.0
    },
    "62488": {
        "name": "New Lexington",
        "state": "New Lexington OH",
        "population": 4602,
        "housing": 2039,
        "land_area": 5622753.0,
        "water_area": 6111.0
    },
    "62515": {
        "name": "New London",
        "state": "New London WI",
        "population": 7804,
        "housing": 3535,
        "land_area": 11865621.0,
        "water_area": 212943.0
    },
    "62569": {
        "name": "Newman",
        "state": "Newman CA",
        "population": 12387,
        "housing": 3751,
        "land_area": 5399593.0,
        "water_area": 0.0
    },
    "62623": {
        "name": "New Martinsville",
        "state": "New Martinsville WV--OH",
        "population": 5608,
        "housing": 2872,
        "land_area": 9986688.0,
        "water_area": 161646.0
    },
    "62677": {
        "name": "New Orleans",
        "state": "New Orleans LA",
        "population": 963212,
        "housing": 441065,
        "land_area": 700004654.0,
        "water_area": 58024272.0
    },
    "62704": {
        "name": "New Paltz",
        "state": "New Paltz NY",
        "population": 9969,
        "housing": 3483,
        "land_area": 12658583.0,
        "water_area": 17799.0
    },
    "62731": {
        "name": "New Philadelphia--Dover",
        "state": "New Philadelphia--Dover OH",
        "population": 46776,
        "housing": 21186,
        "land_area": 61663477.0,
        "water_area": 928870.0
    },
    "62758": {
        "name": "Newport",
        "state": "Newport AR",
        "population": 5947,
        "housing": 2933,
        "land_area": 11859762.0,
        "water_area": 54671.0
    },
    "62839": {
        "name": "Newport",
        "state": "Newport OR",
        "population": 11731,
        "housing": 6668,
        "land_area": 24377706.0,
        "water_area": 716917.0
    },
    "62893": {
        "name": "Newport",
        "state": "Newport TN",
        "population": 11576,
        "housing": 5520,
        "land_area": 25647476.0,
        "water_area": 0.0
    },
    "62947": {
        "name": "New Prague",
        "state": "New Prague MN",
        "population": 8156,
        "housing": 3187,
        "land_area": 8439834.0,
        "water_area": 0.0
    },
    "62974": {
        "name": "New Richmond",
        "state": "New Richmond WI",
        "population": 9486,
        "housing": 4114,
        "land_area": 11969956.0,
        "water_area": 43075.0
    },
    "63001": {
        "name": "New Roads",
        "state": "New Roads LA",
        "population": 6794,
        "housing": 4010,
        "land_area": 18962526.0,
        "water_area": 12357556.0
    },
    "63028": {
        "name": "New Tazewell--Tazewell",
        "state": "New Tazewell--Tazewell TN",
        "population": 5374,
        "housing": 2509,
        "land_area": 17260234.0,
        "water_area": 0.0
    },
    "63082": {
        "name": "Newton",
        "state": "Newton IA",
        "population": 15943,
        "housing": 7446,
        "land_area": 18475406.0,
        "water_area": 0.0
    },
    "63109": {
        "name": "Newton",
        "state": "Newton KS",
        "population": 20378,
        "housing": 9015,
        "land_area": 29651283.0,
        "water_area": 19913.0
    },
    "63137": {
        "name": "Newton",
        "state": "Newton NJ",
        "population": 12813,
        "housing": 5530,
        "land_area": 20465957.0,
        "water_area": 1100653.0
    },
    "63152": {
        "name": "Newton Falls",
        "state": "Newton Falls OH",
        "population": 6604,
        "housing": 3344,
        "land_area": 11616337.0,
        "water_area": 250469.0
    },
    "63163": {
        "name": "New Ulm",
        "state": "New Ulm MN",
        "population": 13435,
        "housing": 6091,
        "land_area": 15069130.0,
        "water_area": 0.0
    },
    "63217": {
        "name": "New York--Jersey City--Newark",
        "state": "New York--Jersey City--Newark NY--NJ",
        "population": 19426449,
        "housing": 7657903,
        "land_area": 8412591091.0,
        "water_area": 548707462.0
    },
    "63232": {
        "name": "Nice",
        "state": "Nice CA",
        "population": 8555,
        "housing": 5133,
        "land_area": 9829386.0,
        "water_area": 489524.0
    },
    "63244": {
        "name": "Nicholasville",
        "state": "Nicholasville KY",
        "population": 31434,
        "housing": 12546,
        "land_area": 31018898.0,
        "water_area": 130082.0
    },
    "63282": {
        "name": "Nipomo",
        "state": "Nipomo CA",
        "population": 20303,
        "housing": 7868,
        "land_area": 26326053.0,
        "water_area": 0.0
    },
    "63325": {
        "name": "Nogales",
        "state": "Nogales AZ",
        "population": 19168,
        "housing": 7334,
        "land_area": 32871111.0,
        "water_area": 26000.0
    },
    "63406": {
        "name": "Norfolk",
        "state": "Norfolk NE",
        "population": 27407,
        "housing": 11803,
        "land_area": 33047498.0,
        "water_area": 227458.0
    },
    "63433": {
        "name": "Norman",
        "state": "Norman OK",
        "population": 120191,
        "housing": 52761,
        "land_area": 119039642.0,
        "water_area": 1395317.0
    },
    "63460": {
        "name": "North Adams",
        "state": "North Adams MA",
        "population": 25432,
        "housing": 12564,
        "land_area": 41767229.0,
        "water_area": 415784.0
    },
    "63514": {
        "name": "North Bend",
        "state": "North Bend WA",
        "population": 11762,
        "housing": 4626,
        "land_area": 14399867.0,
        "water_area": 195779.0
    },
    "63541": {
        "name": "North Branch",
        "state": "North Branch MN",
        "population": 6368,
        "housing": 2530,
        "land_area": 11250157.0,
        "water_area": 22247.0
    },
    "63622": {
        "name": "North East",
        "state": "North East PA",
        "population": 6513,
        "housing": 3010,
        "land_area": 9087274.0,
        "water_area": 1852.0
    },
    "63676": {
        "name": "Northfield",
        "state": "Northfield MN",
        "population": 22686,
        "housing": 7644,
        "land_area": 22981263.0,
        "water_area": 31608.0
    },
    "63784": {
        "name": "North Manchester",
        "state": "North Manchester IN",
        "population": 5188,
        "housing": 2388,
        "land_area": 7357413.0,
        "water_area": 151322.0
    },
    "63811": {
        "name": "North Platte",
        "state": "North Platte NE",
        "population": 23582,
        "housing": 11414,
        "land_area": 33580277.0,
        "water_area": 243722.0
    },
    "63865": {
        "name": "North Vernon",
        "state": "North Vernon IN",
        "population": 6936,
        "housing": 3113,
        "land_area": 13236302.0,
        "water_area": 26165.0
    },
    "63973": {
        "name": "North Windham",
        "state": "North Windham ME",
        "population": 10271,
        "housing": 5183,
        "land_area": 36266860.0,
        "water_area": 496436.0
    },
    "64054": {
        "name": "Norwalk",
        "state": "Norwalk OH",
        "population": 19269,
        "housing": 8722,
        "land_area": 25670538.0,
        "water_area": 20427.0
    },
    "64108": {
        "name": "Norwich",
        "state": "Norwich NY",
        "population": 7740,
        "housing": 3844,
        "land_area": 7786491.0,
        "water_area": 0.0
    },
    "64135": {
        "name": "Norwich--New London",
        "state": "Norwich--New London CT",
        "population": 167432,
        "housing": 77980,
        "land_area": 302300660.0,
        "water_area": 30755717.0
    },
    "64270": {
        "name": "Oakdale",
        "state": "Oakdale CA",
        "population": 25408,
        "housing": 9353,
        "land_area": 19042995.0,
        "water_area": 100699.0
    },
    "64297": {
        "name": "Oakdale",
        "state": "Oakdale LA",
        "population": 6700,
        "housing": 2428,
        "land_area": 12303470.0,
        "water_area": 57811.0
    },
    "64378": {
        "name": "Oak Harbor",
        "state": "Oak Harbor WA",
        "population": 37449,
        "housing": 15291,
        "land_area": 61255112.0,
        "water_area": 229376.0
    },
    "64459": {
        "name": "Oak Island",
        "state": "Oak Island NC",
        "population": 15592,
        "housing": 14603,
        "land_area": 39878815.0,
        "water_area": 662264.0
    },
    "64475": {
        "name": "Oakland",
        "state": "Oakland TN",
        "population": 9389,
        "housing": 3816,
        "land_area": 20496625.0,
        "water_area": 147928.0
    },
    "64498": {
        "name": "Oak Ridge",
        "state": "Oak Ridge NJ",
        "population": 8871,
        "housing": 3546,
        "land_area": 14007665.0,
        "water_area": 1115448.0
    },
    "64550": {
        "name": "Oberlin",
        "state": "Oberlin OH",
        "population": 8176,
        "housing": 2886,
        "land_area": 7610558.0,
        "water_area": 11781.0
    },
    "64567": {
        "name": "Ocala",
        "state": "Ocala FL",
        "population": 182647,
        "housing": 83908,
        "land_area": 323814545.0,
        "water_area": 575486.0
    },
    "64648": {
        "name": "Ocean Park",
        "state": "Ocean Park WA",
        "population": 5411,
        "housing": 5642,
        "land_area": 23279838.0,
        "water_area": 597409.0
    },
    "64660": {
        "name": "Ocean Pines--Ocean City",
        "state": "Ocean Pines--Ocean City MD--DE",
        "population": 37946,
        "housing": 53372,
        "land_area": 74670149.0,
        "water_area": 5473667.0
    },
    "64675": {
        "name": "Ocean Shores",
        "state": "Ocean Shores WA",
        "population": 6766,
        "housing": 5609,
        "land_area": 21673692.0,
        "water_area": 2296614.0
    },
    "64702": {
        "name": "Ocean View",
        "state": "Ocean View DE",
        "population": 18025,
        "housing": 22598,
        "land_area": 60911394.0,
        "water_area": 3478190.0
    },
    "64837": {
        "name": "Odessa",
        "state": "Odessa MO",
        "population": 5529,
        "housing": 2300,
        "land_area": 7257118.0,
        "water_area": 81201.0
    },
    "64864": {
        "name": "Odessa",
        "state": "Odessa TX",
        "population": 154818,
        "housing": 63127,
        "land_area": 222727634.0,
        "water_area": 1247556.0
    },
    "64891": {
        "name": "Oelwein",
        "state": "Oelwein IA",
        "population": 6585,
        "housing": 3342,
        "land_area": 12524607.0,
        "water_area": 221705.0
    },
    "64918": {
        "name": "Ogallala",
        "state": "Ogallala NE",
        "population": 4721,
        "housing": 2399,
        "land_area": 8458395.0,
        "water_area": 0.0
    },
    "64945": {
        "name": "Ogden--Layton",
        "state": "Ogden--Layton UT",
        "population": 608857,
        "housing": 203545,
        "land_area": 550625260.0,
        "water_area": 1380326.0
    },
    "64972": {
        "name": "Ogdensburg",
        "state": "Ogdensburg NY",
        "population": 10246,
        "housing": 4363,
        "land_area": 12749341.0,
        "water_area": 217766.0
    },
    "64999": {
        "name": "Oil City",
        "state": "Oil City PA",
        "population": 13666,
        "housing": 6955,
        "land_area": 22070561.0,
        "water_area": 653307.0
    },
    "65026": {
        "name": "Okeechobee--Taylor Creek",
        "state": "Okeechobee--Taylor Creek FL",
        "population": 26670,
        "housing": 14345,
        "land_area": 61855528.0,
        "water_area": 2295039.0
    },
    "65080": {
        "name": "Oklahoma City",
        "state": "Oklahoma City OK",
        "population": 982276,
        "housing": 426593,
        "land_area": 1092275518.0,
        "water_area": 19894479.0
    },
    "65107": {
        "name": "Okmulgee",
        "state": "Okmulgee OK",
        "population": 11016,
        "housing": 5118,
        "land_area": 19129473.0,
        "water_area": 0.0
    },
    "65161": {
        "name": "Olean",
        "state": "Olean NY",
        "population": 21144,
        "housing": 9929,
        "land_area": 34134440.0,
        "water_area": 675090.0
    },
    "65188": {
        "name": "Olney",
        "state": "Olney IL",
        "population": 8642,
        "housing": 4154,
        "land_area": 15897441.0,
        "water_area": 675.0
    },
    "65242": {
        "name": "Olympia--Lacey",
        "state": "Olympia--Lacey WA",
        "population": 208157,
        "housing": 87925,
        "land_area": 275070119.0,
        "water_area": 13519574.0
    },
    "65269": {
        "name": "Omaha",
        "state": "Omaha NE--IA",
        "population": 819508,
        "housing": 337786,
        "land_area": 701398536.0,
        "water_area": 12889694.0
    },
    "65296": {
        "name": "Omak",
        "state": "Omak WA",
        "population": 8165,
        "housing": 3682,
        "land_area": 15649413.0,
        "water_area": 301120.0
    },
    "65404": {
        "name": "Oneida",
        "state": "Oneida NY",
        "population": 12481,
        "housing": 6046,
        "land_area": 16399574.0,
        "water_area": 74930.0
    },
    "65485": {
        "name": "Oneonta",
        "state": "Oneonta AL",
        "population": 5624,
        "housing": 2285,
        "land_area": 11325973.0,
        "water_area": 1452.0
    },
    "65512": {
        "name": "Oneonta",
        "state": "Oneonta NY",
        "population": 16028,
        "housing": 6335,
        "land_area": 18495502.0,
        "water_area": 210890.0
    },
    "65539": {
        "name": "Ontario--Payette",
        "state": "Ontario--Payette OR--ID",
        "population": 27806,
        "housing": 10959,
        "land_area": 36101134.0,
        "water_area": 26474.0
    },
    "65593": {
        "name": "Opelousas",
        "state": "Opelousas LA",
        "population": 23498,
        "housing": 10611,
        "land_area": 48138665.0,
        "water_area": 70644.0
    },
    "65620": {
        "name": "Opp",
        "state": "Opp AL",
        "population": 5325,
        "housing": 2495,
        "land_area": 13468175.0,
        "water_area": 186253.0
    },
    "65647": {
        "name": "Orange",
        "state": "Orange TX",
        "population": 40796,
        "housing": 18518,
        "land_area": 95108876.0,
        "water_area": 4947934.0
    },
    "65674": {
        "name": "Orange",
        "state": "Orange VA",
        "population": 4802,
        "housing": 2149,
        "land_area": 7631237.0,
        "water_area": 42553.0
    },
    "65701": {
        "name": "Orangeburg",
        "state": "Orangeburg SC",
        "population": 29072,
        "housing": 13791,
        "land_area": 68826733.0,
        "water_area": 358163.0
    },
    "65728": {
        "name": "Orange City",
        "state": "Orange City IA",
        "population": 7093,
        "housing": 2585,
        "land_area": 10973234.0,
        "water_area": 0.0
    },
    "65755": {
        "name": "Orange Cove",
        "state": "Orange Cove CA",
        "population": 9716,
        "housing": 2502,
        "land_area": 4276643.0,
        "water_area": 0.0
    },
    "65767": {
        "name": "Orangetree",
        "state": "Orangetree FL",
        "population": 9791,
        "housing": 3432,
        "land_area": 24016879.0,
        "water_area": 2078940.0
    },
    "65809": {
        "name": "Oregon",
        "state": "Oregon WI",
        "population": 11551,
        "housing": 4598,
        "land_area": 11720169.0,
        "water_area": 0.0
    },
    "65836": {
        "name": "Orland",
        "state": "Orland CA",
        "population": 9422,
        "housing": 3394,
        "land_area": 11666397.0,
        "water_area": 0.0
    },
    "65863": {
        "name": "Orlando",
        "state": "Orlando FL",
        "population": 1853896,
        "housing": 746578,
        "land_area": 1669543451.0,
        "water_area": 168716795.0
    },
    "65901": {
        "name": "Oronoko--Berrien Springs",
        "state": "Oronoko--Berrien Springs MI",
        "population": 6725,
        "housing": 2710,
        "land_area": 9548430.0,
        "water_area": 61675.0
    },
    "65917": {
        "name": "Orosi",
        "state": "Orosi CA",
        "population": 12795,
        "housing": 3262,
        "land_area": 5159508.0,
        "water_area": 0.0
    },
    "65944": {
        "name": "Oroville",
        "state": "Oroville CA",
        "population": 40190,
        "housing": 15681,
        "land_area": 68447822.0,
        "water_area": 59656.0
    },
    "65971": {
        "name": "Orrville",
        "state": "Orrville OH",
        "population": 8703,
        "housing": 3866,
        "land_area": 14003079.0,
        "water_area": 36898.0
    },
    "66060": {
        "name": "Osage Beach",
        "state": "Osage Beach MO",
        "population": 6668,
        "housing": 7785,
        "land_area": 34375774.0,
        "water_area": 1694492.0
    },
    "66106": {
        "name": "Osceola",
        "state": "Osceola AR",
        "population": 6244,
        "housing": 2963,
        "land_area": 11484231.0,
        "water_area": 33532.0
    },
    "66133": {
        "name": "Osceola",
        "state": "Osceola IA",
        "population": 5283,
        "housing": 2297,
        "land_area": 9538268.0,
        "water_area": 0.0
    },
    "66148": {
        "name": "Oscoda--Au Sable",
        "state": "Oscoda--Au Sable MI",
        "population": 8558,
        "housing": 7258,
        "land_area": 48139571.0,
        "water_area": 4579145.0
    },
    "66160": {
        "name": "Oshkosh",
        "state": "Oshkosh WI",
        "population": 76190,
        "housing": 33084,
        "land_area": 85061092.0,
        "water_area": 4071638.0
    },
    "66187": {
        "name": "Oskaloosa",
        "state": "Oskaloosa IA",
        "population": 12541,
        "housing": 5642,
        "land_area": 21331624.0,
        "water_area": 0.0
    },
    "66241": {
        "name": "Oswego",
        "state": "Oswego NY",
        "population": 24421,
        "housing": 10237,
        "land_area": 29327695.0,
        "water_area": 1507853.0
    },
    "66268": {
        "name": "Othello",
        "state": "Othello WA",
        "population": 10865,
        "housing": 3219,
        "land_area": 11977465.0,
        "water_area": 0.0
    },
    "66285": {
        "name": "Otsego--Plainwell",
        "state": "Otsego--Plainwell MI",
        "population": 11819,
        "housing": 5043,
        "land_area": 21797426.0,
        "water_area": 932568.0
    },
    "66295": {
        "name": "Ottawa",
        "state": "Ottawa IL",
        "population": 20122,
        "housing": 9667,
        "land_area": 25874487.0,
        "water_area": 1411987.0
    },
    "66322": {
        "name": "Ottawa",
        "state": "Ottawa KS",
        "population": 12461,
        "housing": 5475,
        "land_area": 16323658.0,
        "water_area": 76147.0
    },
    "66349": {
        "name": "Ottawa",
        "state": "Ottawa OH",
        "population": 5418,
        "housing": 2357,
        "land_area": 12419073.0,
        "water_area": 141882.0
    },
    "66376": {
        "name": "Ottumwa",
        "state": "Ottumwa IA",
        "population": 25019,
        "housing": 11060,
        "land_area": 33179541.0,
        "water_area": 887434.0
    },
    "66430": {
        "name": "Owatonna",
        "state": "Owatonna MN",
        "population": 26278,
        "housing": 11106,
        "land_area": 32417909.0,
        "water_area": 0.0
    },
    "66457": {
        "name": "Owego",
        "state": "Owego NY",
        "population": 4365,
        "housing": 2213,
        "land_area": 8309243.0,
        "water_area": 579848.0
    },
    "66484": {
        "name": "Owensboro",
        "state": "Owensboro KY",
        "population": 76433,
        "housing": 33308,
        "land_area": 94271466.0,
        "water_area": 511837.0
    },
    "66538": {
        "name": "Owosso",
        "state": "Owosso MI",
        "population": 22329,
        "housing": 10344,
        "land_area": 35259297.0,
        "water_area": 468693.0
    },
    "66565": {
        "name": "Oxford",
        "state": "Oxford MS",
        "population": 33518,
        "housing": 17650,
        "land_area": 62962622.0,
        "water_area": 137188.0
    },
    "66592": {
        "name": "Oxford",
        "state": "Oxford NC",
        "population": 8925,
        "housing": 4062,
        "land_area": 17231818.0,
        "water_area": 0.0
    },
    "66619": {
        "name": "Oxford",
        "state": "Oxford OH",
        "population": 23221,
        "housing": 7262,
        "land_area": 18724952.0,
        "water_area": 8641.0
    },
    "66646": {
        "name": "Oxford",
        "state": "Oxford PA",
        "population": 9925,
        "housing": 3795,
        "land_area": 17028453.0,
        "water_area": 49397.0
    },
    "66673": {
        "name": "Oxnard--San Buenaventura (Ventura)",
        "state": "Oxnard--San Buenaventura (Ventura) CA",
        "population": 376117,
        "housing": 125620,
        "land_area": 198411033.0,
        "water_area": 4017268.0
    },
    "66690": {
        "name": "Ozark",
        "state": "Ozark AL",
        "population": 12218,
        "housing": 5829,
        "land_area": 43287085.0,
        "water_area": 178911.0
    },
    "66740": {
        "name": "Pacific",
        "state": "Pacific MO",
        "population": 8522,
        "housing": 3800,
        "land_area": 16028940.0,
        "water_area": 0.0
    },
    "66781": {
        "name": "Paducah",
        "state": "Paducah KY--IL",
        "population": 50833,
        "housing": 24494,
        "land_area": 107253440.0,
        "water_area": 693595.0
    },
    "66808": {
        "name": "Page",
        "state": "Page AZ",
        "population": 7022,
        "housing": 2799,
        "land_area": 18600590.0,
        "water_area": 0.0
    },
    "66889": {
        "name": "Pagosa Springs",
        "state": "Pagosa Springs CO",
        "population": 5632,
        "housing": 4096,
        "land_area": 16167759.0,
        "water_area": 848846.0
    },
    "66916": {
        "name": "Pahokee",
        "state": "Pahokee FL",
        "population": 6683,
        "housing": 2529,
        "land_area": 10503554.0,
        "water_area": 0.0
    },
    "66943": {
        "name": "Pahrump",
        "state": "Pahrump NV",
        "population": 37498,
        "housing": 18442,
        "land_area": 135724064.0,
        "water_area": 47519.0
    },
    "66997": {
        "name": "Paintsville",
        "state": "Paintsville KY",
        "population": 5717,
        "housing": 2801,
        "land_area": 11625366.0,
        "water_area": 274779.0
    },
    "67051": {
        "name": "Palatka",
        "state": "Palatka FL",
        "population": 20032,
        "housing": 8830,
        "land_area": 47389845.0,
        "water_area": 453017.0
    },
    "67078": {
        "name": "Palestine",
        "state": "Palestine TX",
        "population": 18615,
        "housing": 7789,
        "land_area": 39343056.0,
        "water_area": 185637.0
    },
    "67105": {
        "name": "Palm Bay--Melbourne",
        "state": "Palm Bay--Melbourne FL",
        "population": 510675,
        "housing": 240941,
        "land_area": 648909132.0,
        "water_area": 130732017.0
    },
    "67140": {
        "name": "Palmdale--Lancaster",
        "state": "Palmdale--Lancaster CA",
        "population": 359559,
        "housing": 111858,
        "land_area": 219588587.0,
        "water_area": 615595.0
    },
    "67223": {
        "name": "Palmyra",
        "state": "Palmyra NY",
        "population": 4477,
        "housing": 2099,
        "land_area": 6267932.0,
        "water_area": 0.0
    },
    "67230": {
        "name": "Palmyra",
        "state": "Palmyra PA",
        "population": 3772,
        "housing": 4385,
        "land_area": 21485108.0,
        "water_area": 333363.0
    },
    "67240": {
        "name": "Pampa",
        "state": "Pampa TX",
        "population": 16865,
        "housing": 8413,
        "land_area": 21374659.0,
        "water_area": 0.0
    },
    "67267": {
        "name": "Pana",
        "state": "Pana IL",
        "population": 5309,
        "housing": 2757,
        "land_area": 8747377.0,
        "water_area": 727429.0
    },
    "67305": {
        "name": "Panama City--Panama City Beach",
        "state": "Panama City--Panama City Beach FL",
        "population": 162060,
        "housing": 107507,
        "land_area": 309423147.0,
        "water_area": 18938978.0
    },
    "67313": {
        "name": "Panther Valley",
        "state": "Panther Valley NJ",
        "population": 4279,
        "housing": 2087,
        "land_area": 5730709.0,
        "water_area": 15159.0
    },
    "67321": {
        "name": "Paola",
        "state": "Paola KS",
        "population": 5553,
        "housing": 2538,
        "land_area": 8106375.0,
        "water_area": 29176.0
    },
    "67402": {
        "name": "Paragould",
        "state": "Paragould AR",
        "population": 25089,
        "housing": 10612,
        "land_area": 41925974.0,
        "water_area": 173084.0
    },
    "67483": {
        "name": "Paris",
        "state": "Paris IL",
        "population": 8316,
        "housing": 4218,
        "land_area": 12726845.0,
        "water_area": 29281.0
    },
    "67510": {
        "name": "Paris",
        "state": "Paris KY",
        "population": 11269,
        "housing": 5101,
        "land_area": 14575644.0,
        "water_area": 68666.0
    },
    "67537": {
        "name": "Paris",
        "state": "Paris TN",
        "population": 10303,
        "housing": 4999,
        "land_area": 23907671.0,
        "water_area": 51767.0
    },
    "67564": {
        "name": "Paris",
        "state": "Paris TX",
        "population": 26292,
        "housing": 12343,
        "land_area": 45395056.0,
        "water_area": 161369.0
    },
    "67620": {
        "name": "Park City--Snyderville",
        "state": "Park City--Snyderville UT",
        "population": 16168,
        "housing": 12233,
        "land_area": 38612790.0,
        "water_area": 0.0
    },
    "67645": {
        "name": "Parker",
        "state": "Parker AZ--CA",
        "population": 5329,
        "housing": 2841,
        "land_area": 11877771.0,
        "water_area": 74887.0
    },
    "67672": {
        "name": "Parkersburg",
        "state": "Parkersburg WV--OH",
        "population": 62500,
        "housing": 30935,
        "land_area": 103330327.0,
        "water_area": 4480549.0
    },
    "67753": {
        "name": "Parlier",
        "state": "Parlier CA",
        "population": 14522,
        "housing": 3841,
        "land_area": 4629860.0,
        "water_area": 0.0
    },
    "67780": {
        "name": "Parsons",
        "state": "Parsons KS",
        "population": 9433,
        "housing": 4715,
        "land_area": 16212019.0,
        "water_area": 58260.0
    },
    "67812": {
        "name": "Pascagoula--Gautier",
        "state": "Pascagoula--Gautier MS",
        "population": 51454,
        "housing": 24371,
        "land_area": 99989675.0,
        "water_area": 3251712.0
    },
    "67861": {
        "name": "Patterson",
        "state": "Patterson CA",
        "population": 23660,
        "housing": 6659,
        "land_area": 11411340.0,
        "water_area": 55612.0
    },
    "67942": {
        "name": "Pauls Valley",
        "state": "Pauls Valley OK",
        "population": 5608,
        "housing": 2806,
        "land_area": 8134657.0,
        "water_area": 54619.0
    },
    "68077": {
        "name": "Paw Paw",
        "state": "Paw Paw MI",
        "population": 5662,
        "housing": 2675,
        "land_area": 14599023.0,
        "water_area": 963771.0
    },
    "68106": {
        "name": "Paw Paw Lake",
        "state": "Paw Paw Lake MI",
        "population": 7526,
        "housing": 4106,
        "land_area": 17524363.0,
        "water_area": 3995885.0
    },
    "68131": {
        "name": "Paxton",
        "state": "Paxton IL",
        "population": 4528,
        "housing": 2036,
        "land_area": 5508604.0,
        "water_area": 0.0
    },
    "68158": {
        "name": "Payson",
        "state": "Payson AZ",
        "population": 17022,
        "housing": 9963,
        "land_area": 35999080.0,
        "water_area": 18536.0
    },
    "68170": {
        "name": "Payson--Santaquin",
        "state": "Payson--Santaquin UT",
        "population": 31132,
        "housing": 9024,
        "land_area": 24241007.0,
        "water_area": 29173.0
    },
    "68185": {
        "name": "Pea Ridge",
        "state": "Pea Ridge AR",
        "population": 6026,
        "housing": 2221,
        "land_area": 10047537.0,
        "water_area": 3002.0
    },
    "68212": {
        "name": "Pearsall",
        "state": "Pearsall TX",
        "population": 9063,
        "housing": 3016,
        "land_area": 14015152.0,
        "water_area": 0.0
    },
    "68239": {
        "name": "Pecan Plantation",
        "state": "Pecan Plantation TX",
        "population": 6831,
        "housing": 3229,
        "land_area": 21036038.0,
        "water_area": 46831.0
    },
    "68266": {
        "name": "Pecos",
        "state": "Pecos TX",
        "population": 13081,
        "housing": 4232,
        "land_area": 19995661.0,
        "water_area": 0.0
    },
    "68293": {
        "name": "Peculiar",
        "state": "Peculiar MO",
        "population": 5564,
        "housing": 2227,
        "land_area": 12396033.0,
        "water_area": 81851.0
    },
    "68347": {
        "name": "Pella",
        "state": "Pella IA",
        "population": 10160,
        "housing": 4169,
        "land_area": 18027947.0,
        "water_area": 4377.0
    },
    "68374": {
        "name": "Pell City",
        "state": "Pell City AL",
        "population": 11500,
        "housing": 5208,
        "land_area": 36222147.0,
        "water_area": 322539.0
    },
    "68401": {
        "name": "Pembroke",
        "state": "Pembroke NC",
        "population": 5694,
        "housing": 1682,
        "land_area": 9396972.0,
        "water_area": 0.0
    },
    "68428": {
        "name": "Pendleton",
        "state": "Pendleton OR",
        "population": 17488,
        "housing": 7120,
        "land_area": 26360364.0,
        "water_area": 0.0
    },
    "68439": {
        "name": "Pennsburg--Upper Hanover--East Greenville",
        "state": "Pennsburg--Upper Hanover--East Greenville PA",
        "population": 17239,
        "housing": 6857,
        "land_area": 22350195.0,
        "water_area": 22553.0
    },
    "68455": {
        "name": "Penn Yan",
        "state": "Penn Yan NY",
        "population": 8399,
        "housing": 5941,
        "land_area": 17651198.0,
        "water_area": 8431945.0
    },
    "68482": {
        "name": "Pensacola",
        "state": "Pensacola FL--AL",
        "population": 390172,
        "housing": 184298,
        "land_area": 679971435.0,
        "water_area": 21560293.0
    },
    "68499": {
        "name": "Peoria",
        "state": "Peoria AZ",
        "population": 19593,
        "housing": 8526,
        "land_area": 18768067.0,
        "water_area": 47510.0
    },
    "68509": {
        "name": "Peoria",
        "state": "Peoria IL",
        "population": 259781,
        "housing": 121278,
        "land_area": 377582973.0,
        "water_area": 11760373.0
    },
    "68563": {
        "name": "Pepperell",
        "state": "Pepperell MA",
        "population": 6103,
        "housing": 2579,
        "land_area": 13509844.0,
        "water_area": 210547.0
    },
    "68590": {
        "name": "Perry",
        "state": "Perry FL",
        "population": 6531,
        "housing": 2945,
        "land_area": 15344308.0,
        "water_area": 0.0
    },
    "68617": {
        "name": "Perry",
        "state": "Perry GA",
        "population": 14884,
        "housing": 6099,
        "land_area": 35854456.0,
        "water_area": 210397.0
    },
    "68644": {
        "name": "Perry",
        "state": "Perry IA",
        "population": 7628,
        "housing": 3137,
        "land_area": 7588326.0,
        "water_area": 0.0
    },
    "68698": {
        "name": "Perry",
        "state": "Perry NY",
        "population": 4066,
        "housing": 2385,
        "land_area": 7540258.0,
        "water_area": 0.0
    },
    "68725": {
        "name": "Perry",
        "state": "Perry OK",
        "population": 4445,
        "housing": 2258,
        "land_area": 7744014.0,
        "water_area": 20644.0
    },
    "68752": {
        "name": "Perryton",
        "state": "Perryton TX",
        "population": 8391,
        "housing": 3580,
        "land_area": 10305237.0,
        "water_area": 0.0
    },
    "68779": {
        "name": "Perryville",
        "state": "Perryville MO",
        "population": 8062,
        "housing": 3531,
        "land_area": 14805396.0,
        "water_area": 0.0
    },
    "68806": {
        "name": "Peru",
        "state": "Peru IN",
        "population": 12458,
        "housing": 6126,
        "land_area": 16766072.0,
        "water_area": 229059.0
    },
    "68833": {
        "name": "Peru--LaSalle",
        "state": "Peru--LaSalle IL",
        "population": 29763,
        "housing": 14250,
        "land_area": 55561608.0,
        "water_area": 108194.0
    },
    "68887": {
        "name": "Petaluma",
        "state": "Petaluma CA",
        "population": 65227,
        "housing": 26392,
        "land_area": 54295333.0,
        "water_area": 221181.0
    },
    "69022": {
        "name": "Petoskey",
        "state": "Petoskey MI",
        "population": 12233,
        "housing": 8601,
        "land_area": 39804164.0,
        "water_area": 510179.0
    },
    "69049": {
        "name": "Philadelphia",
        "state": "Philadelphia MS",
        "population": 7114,
        "housing": 3326,
        "land_area": 19043537.0,
        "water_area": 0.0
    },
    "69076": {
        "name": "Philadelphia",
        "state": "Philadelphia PA--NJ--DE--MD",
        "population": 5696125,
        "housing": 2377924,
        "land_area": 4916288579.0,
        "water_area": 134143760.0
    },
    "69130": {
        "name": "Philipsburg",
        "state": "Philipsburg PA",
        "population": 9379,
        "housing": 3886,
        "land_area": 13301451.0,
        "water_area": 20503.0
    },
    "69184": {
        "name": "Phoenix--Mesa--Scottsdale",
        "state": "Phoenix--Mesa--Scottsdale AZ",
        "population": 3976313,
        "housing": 1670745,
        "land_area": 2876131867.0,
        "water_area": 8942624.0
    },
    "69192": {
        "name": "Phoenix West--Goodyear--Avondale",
        "state": "Phoenix West--Goodyear--Avondale AZ",
        "population": 419946,
        "housing": 136070,
        "land_area": 330516491.0,
        "water_area": 1003179.0
    },
    "69211": {
        "name": "Picayune",
        "state": "Picayune MS",
        "population": 16301,
        "housing": 7396,
        "land_area": 40439956.0,
        "water_area": 1084788.0
    },
    "69222": {
        "name": "Pickens",
        "state": "Pickens SC",
        "population": 5068,
        "housing": 2213,
        "land_area": 12234796.0,
        "water_area": 37555.0
    },
    "69292": {
        "name": "Pierre",
        "state": "Pierre SD",
        "population": 14755,
        "housing": 7004,
        "land_area": 31118330.0,
        "water_area": 160028.0
    },
    "69373": {
        "name": "Pikeville",
        "state": "Pikeville KY",
        "population": 10710,
        "housing": 4853,
        "land_area": 26408247.0,
        "water_area": 444455.0
    },
    "69454": {
        "name": "Pine Bluff",
        "state": "Pine Bluff AR",
        "population": 46683,
        "housing": 21423,
        "land_area": 115372301.0,
        "water_area": 1018489.0
    },
    "69513": {
        "name": "Pinehurst (Montgomery County)--Magnolia",
        "state": "Pinehurst (Montgomery County)--Magnolia TX",
        "population": 9667,
        "housing": 3376,
        "land_area": 22516479.0,
        "water_area": 69482.0
    },
    "69517": {
        "name": "Pinehurst--Southern Pines",
        "state": "Pinehurst--Southern Pines NC",
        "population": 50319,
        "housing": 25063,
        "land_area": 123168161.0,
        "water_area": 3548455.0
    },
    "69562": {
        "name": "Pinetop-Lakeside",
        "state": "Pinetop-Lakeside AZ",
        "population": 9393,
        "housing": 9313,
        "land_area": 37701483.0,
        "water_area": 547975.0
    },
    "69616": {
        "name": "Piqua",
        "state": "Piqua OH",
        "population": 20890,
        "housing": 9451,
        "land_area": 23609617.0,
        "water_area": 456381.0
    },
    "69643": {
        "name": "Pittsburg",
        "state": "Pittsburg KS",
        "population": 23120,
        "housing": 10721,
        "land_area": 28896414.0,
        "water_area": 162569.0
    },
    "69697": {
        "name": "Pittsburgh",
        "state": "Pittsburgh PA",
        "population": 1745039,
        "housing": 831523,
        "land_area": 2348194653.0,
        "water_area": 42944812.0
    },
    "69778": {
        "name": "Pittsfield",
        "state": "Pittsfield MA",
        "population": 50720,
        "housing": 25125,
        "land_area": 78879613.0,
        "water_area": 4164821.0
    },
    "69832": {
        "name": "Placerville--Diamond Springs",
        "state": "Placerville--Diamond Springs CA",
        "population": 23291,
        "housing": 10402,
        "land_area": 48610405.0,
        "water_area": 142261.0
    },
    "69967": {
        "name": "Plainview",
        "state": "Plainview TX",
        "population": 22615,
        "housing": 9073,
        "land_area": 30525007.0,
        "water_area": 0.0
    },
    "70102": {
        "name": "Platte City",
        "state": "Platte City MO",
        "population": 10707,
        "housing": 4330,
        "land_area": 15413519.0,
        "water_area": 102079.0
    },
    "70129": {
        "name": "Platteville",
        "state": "Platteville WI",
        "population": 11838,
        "housing": 4424,
        "land_area": 11468529.0,
        "water_area": 0.0
    },
    "70156": {
        "name": "Plattsburgh",
        "state": "Plattsburgh NY",
        "population": 28958,
        "housing": 13647,
        "land_area": 62163664.0,
        "water_area": 847017.0
    },
    "70183": {
        "name": "Plattsmouth",
        "state": "Plattsmouth NE",
        "population": 6655,
        "housing": 2908,
        "land_area": 7757250.0,
        "water_area": 16249.0
    },
    "70210": {
        "name": "Pleasant Hill",
        "state": "Pleasant Hill MO",
        "population": 8737,
        "housing": 3423,
        "land_area": 12331538.0,
        "water_area": 202366.0
    },
    "70237": {
        "name": "Pleasanton",
        "state": "Pleasanton TX",
        "population": 13983,
        "housing": 5668,
        "land_area": 23462251.0,
        "water_area": 13126.0
    },
    "70280": {
        "name": "Plumas Lake",
        "state": "Plumas Lake CA",
        "population": 7337,
        "housing": 2226,
        "land_area": 5010833.0,
        "water_area": 0.0
    },
    "70291": {
        "name": "Plymouth",
        "state": "Plymouth IN",
        "population": 12279,
        "housing": 5236,
        "land_area": 24917397.0,
        "water_area": 514403.0
    },
    "70318": {
        "name": "Plymouth",
        "state": "Plymouth NH",
        "population": 6166,
        "housing": 2445,
        "land_area": 15123717.0,
        "water_area": 628129.0
    },
    "70372": {
        "name": "Plymouth",
        "state": "Plymouth WI",
        "population": 9011,
        "housing": 4281,
        "land_area": 13734311.0,
        "water_area": 216667.0
    },
    "70399": {
        "name": "Pocahontas",
        "state": "Pocahontas AR",
        "population": 7164,
        "housing": 3141,
        "land_area": 13018350.0,
        "water_area": 946.0
    },
    "70426": {
        "name": "Pocatello",
        "state": "Pocatello ID",
        "population": 72211,
        "housing": 29266,
        "land_area": 71130648.0,
        "water_area": 260757.0
    },
    "70480": {
        "name": "Poinciana",
        "state": "Poinciana FL",
        "population": 53267,
        "housing": 19372,
        "land_area": 59869472.0,
        "water_area": 502205.0
    },
    "70490": {
        "name": "Poinciana Southwest",
        "state": "Poinciana Southwest FL",
        "population": 16966,
        "housing": 6395,
        "land_area": 30536450.0,
        "water_area": 237802.0
    },
    "70507": {
        "name": "Point Pleasant--Gallipolis",
        "state": "Point Pleasant--Gallipolis WV--OH",
        "population": 10544,
        "housing": 5510,
        "land_area": 24471456.0,
        "water_area": 2392430.0
    },
    "70512": {
        "name": "Point Roberts",
        "state": "Point Roberts WA",
        "population": 1191,
        "housing": 2175,
        "land_area": 12652764.0,
        "water_area": 135344.0
    },
    "70539": {
        "name": "Pole Ojea",
        "state": "Pole Ojea PR",
        "population": 2521,
        "housing": 2626,
        "land_area": 7923273.0,
        "water_area": 20396.0
    },
    "70548": {
        "name": "Polk City",
        "state": "Polk City IA",
        "population": 5375,
        "housing": 1941,
        "land_area": 6017099.0,
        "water_area": 0.0
    },
    "70588": {
        "name": "Polson",
        "state": "Polson MT",
        "population": 5564,
        "housing": 3036,
        "land_area": 12222276.0,
        "water_area": 0.0
    },
    "70615": {
        "name": "Ponca City",
        "state": "Ponca City OK",
        "population": 24990,
        "housing": 12044,
        "land_area": 47120338.0,
        "water_area": 207607.0
    },
    "70642": {
        "name": "Ponce",
        "state": "Ponce PR",
        "population": 118345,
        "housing": 58865,
        "land_area": 82153947.0,
        "water_area": 480445.0
    },
    "70669": {
        "name": "Pontiac",
        "state": "Pontiac IL",
        "population": 11078,
        "housing": 4910,
        "land_area": 10868512.0,
        "water_area": 40803.0
    },
    "70750": {
        "name": "Poolesville",
        "state": "Poolesville MD",
        "population": 5685,
        "housing": 1910,
        "land_area": 7460750.0,
        "water_area": 42621.0
    },
    "70804": {
        "name": "Poplar Bluff",
        "state": "Poplar Bluff MO",
        "population": 20449,
        "housing": 9497,
        "land_area": 39997888.0,
        "water_area": 167841.0
    },
    "70831": {
        "name": "Portage",
        "state": "Portage PA",
        "population": 5661,
        "housing": 2721,
        "land_area": 9301059.0,
        "water_area": 0.0
    },
    "70858": {
        "name": "Portage",
        "state": "Portage WI",
        "population": 10555,
        "housing": 4681,
        "land_area": 13548551.0,
        "water_area": 446283.0
    },
    "70912": {
        "name": "Portales",
        "state": "Portales NM",
        "population": 12202,
        "housing": 5384,
        "land_area": 13601128.0,
        "water_area": 1691.0
    },
    "70939": {
        "name": "Port Angeles",
        "state": "Port Angeles WA",
        "population": 24445,
        "housing": 11778,
        "land_area": 43199325.0,
        "water_area": 72182.0
    },
    "70993": {
        "name": "Port Arthur",
        "state": "Port Arthur TX",
        "population": 116819,
        "housing": 47850,
        "land_area": 172141854.0,
        "water_area": 7867704.0
    },
    "71060": {
        "name": "Port Charlotte--North Port",
        "state": "Port Charlotte--North Port FL",
        "population": 199998,
        "housing": 105587,
        "land_area": 348853451.0,
        "water_area": 20369648.0
    },
    "71074": {
        "name": "Porterville",
        "state": "Porterville CA",
        "population": 69862,
        "housing": 20950,
        "land_area": 42345271.0,
        "water_area": 128843.0
    },
    "71128": {
        "name": "Port Hadlock-Irondale",
        "state": "Port Hadlock-Irondale WA",
        "population": 5372,
        "housing": 2851,
        "land_area": 15343670.0,
        "water_area": 81931.0
    },
    "71155": {
        "name": "Port Huron",
        "state": "Port Huron MI",
        "population": 82226,
        "housing": 37996,
        "land_area": 136004819.0,
        "water_area": 1531373.0
    },
    "71182": {
        "name": "Port Isabel--South Padre Island--Laguna Vista",
        "state": "Port Isabel--South Padre Island--Laguna Vista TX",
        "population": 12413,
        "housing": 11230,
        "land_area": 18973479.0,
        "water_area": 946849.0
    },
    "71209": {
        "name": "Port Jervis",
        "state": "Port Jervis NY--PA",
        "population": 16187,
        "housing": 7573,
        "land_area": 19649260.0,
        "water_area": 623366.0
    },
    "71236": {
        "name": "Portland",
        "state": "Portland IN",
        "population": 6364,
        "housing": 3039,
        "land_area": 10762820.0,
        "water_area": 22660.0
    },
    "71263": {
        "name": "Portland",
        "state": "Portland ME",
        "population": 205356,
        "housing": 101206,
        "land_area": 320839656.0,
        "water_area": 16779878.0
    },
    "71290": {
        "name": "Portland",
        "state": "Portland MI",
        "population": 5263,
        "housing": 2299,
        "land_area": 10846380.0,
        "water_area": 427064.0
    },
    "71317": {
        "name": "Portland",
        "state": "Portland OR--WA",
        "population": 2104238,
        "housing": 876555,
        "land_area": 1344980476.0,
        "water_area": 27747156.0
    },
    "71344": {
        "name": "Portland",
        "state": "Portland TN--KY",
        "population": 12285,
        "housing": 4779,
        "land_area": 28151130.0,
        "water_area": 38238.0
    },
    "71371": {
        "name": "Port Lavaca",
        "state": "Port Lavaca TX",
        "population": 12055,
        "housing": 5006,
        "land_area": 27154857.0,
        "water_area": 121453.0
    },
    "71479": {
        "name": "Port St. Lucie",
        "state": "Port St. Lucie FL",
        "population": 437745,
        "housing": 205720,
        "land_area": 580755409.0,
        "water_area": 113599199.0
    },
    "71506": {
        "name": "Portsmouth",
        "state": "Portsmouth NH--ME",
        "population": 95090,
        "housing": 58308,
        "land_area": 296156400.0,
        "water_area": 17722378.0
    },
    "71533": {
        "name": "Portsmouth",
        "state": "Portsmouth OH--KY",
        "population": 35346,
        "housing": 16550,
        "land_area": 55134167.0,
        "water_area": 1791632.0
    },
    "71560": {
        "name": "Port Townsend",
        "state": "Port Townsend WA",
        "population": 10042,
        "housing": 5633,
        "land_area": 15216009.0,
        "water_area": 437958.0
    },
    "71629": {
        "name": "Potala Pastillo",
        "state": "Potala Pastillo PR",
        "population": 5671,
        "housing": 2532,
        "land_area": 4126254.0,
        "water_area": 20210.0
    },
    "71641": {
        "name": "Poteau",
        "state": "Poteau OK",
        "population": 7826,
        "housing": 3293,
        "land_area": 16582901.0,
        "water_area": 22414.0
    },
    "71722": {
        "name": "Potsdam",
        "state": "Potsdam NY",
        "population": 8237,
        "housing": 2703,
        "land_area": 7532179.0,
        "water_area": 298647.0
    },
    "71776": {
        "name": "Pottsville",
        "state": "Pottsville PA",
        "population": 29600,
        "housing": 14976,
        "land_area": 33652714.0,
        "water_area": 0.0
    },
    "71803": {
        "name": "Poughkeepsie--Newburgh",
        "state": "Poughkeepsie--Newburgh NY",
        "population": 314766,
        "housing": 126555,
        "land_area": 543694476.0,
        "water_area": 8708997.0
    },
    "71830": {
        "name": "Powell",
        "state": "Powell WY",
        "population": 6485,
        "housing": 2885,
        "land_area": 6849811.0,
        "water_area": 0.0
    },
    "71857": {
        "name": "Prairie du Chien",
        "state": "Prairie du Chien WI--IA",
        "population": 6119,
        "housing": 3136,
        "land_area": 14434897.0,
        "water_area": 303373.0
    },
    "71884": {
        "name": "Prairie du Sac--Sauk City",
        "state": "Prairie du Sac--Sauk City WI",
        "population": 7846,
        "housing": 3428,
        "land_area": 7454488.0,
        "water_area": 323.0
    },
    "71911": {
        "name": "Prairie Grove",
        "state": "Prairie Grove AR",
        "population": 5496,
        "housing": 2260,
        "land_area": 6241057.0,
        "water_area": 22103.0
    },
    "71965": {
        "name": "Pratt",
        "state": "Pratt KS",
        "population": 6589,
        "housing": 3130,
        "land_area": 9493556.0,
        "water_area": 0.0
    },
    "72112": {
        "name": "Prescott--Prescott Valley",
        "state": "Prescott--Prescott Valley AZ",
        "population": 92427,
        "housing": 45998,
        "land_area": 126274121.0,
        "water_area": 70.0
    },
    "72122": {
        "name": "Prescott Valley East",
        "state": "Prescott Valley East AZ",
        "population": 7229,
        "housing": 4225,
        "land_area": 9457680.0,
        "water_area": 0.0
    },
    "72154": {
        "name": "Presque Isle",
        "state": "Presque Isle ME",
        "population": 5361,
        "housing": 2805,
        "land_area": 13832878.0,
        "water_area": 156831.0
    },
    "72208": {
        "name": "Prestonsburg",
        "state": "Prestonsburg KY",
        "population": 6271,
        "housing": 3104,
        "land_area": 16715959.0,
        "water_area": 1067343.0
    },
    "72235": {
        "name": "Price",
        "state": "Price UT",
        "population": 13346,
        "housing": 5968,
        "land_area": 26075486.0,
        "water_area": 0.0
    },
    "72262": {
        "name": "Princess Anne",
        "state": "Princess Anne MD",
        "population": 6406,
        "housing": 2711,
        "land_area": 7780748.0,
        "water_area": 44434.0
    },
    "72289": {
        "name": "Princeton",
        "state": "Princeton IL",
        "population": 7979,
        "housing": 3910,
        "land_area": 16056337.0,
        "water_area": 0.0
    },
    "72316": {
        "name": "Princeton",
        "state": "Princeton IN",
        "population": 8343,
        "housing": 3995,
        "land_area": 11002167.0,
        "water_area": 14066.0
    },
    "72343": {
        "name": "Princeton",
        "state": "Princeton KY",
        "population": 6058,
        "housing": 2985,
        "land_area": 15810502.0,
        "water_area": 35314.0
    },
    "72370": {
        "name": "Princeton",
        "state": "Princeton MN",
        "population": 4956,
        "housing": 2240,
        "land_area": 10744248.0,
        "water_area": 198994.0
    },
    "72397": {
        "name": "Princeton",
        "state": "Princeton TX",
        "population": 18184,
        "housing": 5936,
        "land_area": 21331024.0,
        "water_area": 156654.0
    },
    "72410": {
        "name": "Princeville",
        "state": "Princeville HI",
        "population": 2544,
        "housing": 2740,
        "land_area": 7542253.0,
        "water_area": 219610.0
    },
    "72424": {
        "name": "Prineville",
        "state": "Prineville OR",
        "population": 12407,
        "housing": 5298,
        "land_area": 21207569.0,
        "water_area": 0.0
    },
    "72451": {
        "name": "Prosser",
        "state": "Prosser WA",
        "population": 6589,
        "housing": 2525,
        "land_area": 12153573.0,
        "water_area": 419304.0
    },
    "72505": {
        "name": "Providence",
        "state": "Providence RI--MA",
        "population": 1285806,
        "housing": 554188,
        "land_area": 1409591947.0,
        "water_area": 86270593.0
    },
    "72532": {
        "name": "Provincetown",
        "state": "Provincetown MA",
        "population": 5698,
        "housing": 7432,
        "land_area": 31973241.0,
        "water_area": 324971.0
    },
    "72559": {
        "name": "Provo--Orem",
        "state": "Provo--Orem UT",
        "population": 588609,
        "housing": 172501,
        "land_area": 417268530.0,
        "water_area": 758864.0
    },
    "72586": {
        "name": "Pryor Creek",
        "state": "Pryor Creek OK",
        "population": 9436,
        "housing": 4243,
        "land_area": 18682896.0,
        "water_area": 67873.0
    },
    "72613": {
        "name": "Pueblo",
        "state": "Pueblo CO",
        "population": 120642,
        "housing": 52608,
        "land_area": 141680092.0,
        "water_area": 1902600.0
    },
    "72620": {
        "name": "Pueblo West",
        "state": "Pueblo West CO",
        "population": 25413,
        "housing": 9795,
        "land_area": 57144582.0,
        "water_area": 36776.0
    },
    "72640": {
        "name": "Pukalani--Haiku-Pauwela--Makawao",
        "state": "Pukalani--Haiku-Pauwela--Makawao HI",
        "population": 23305,
        "housing": 9001,
        "land_area": 41320425.0,
        "water_area": 108128.0
    },
    "72667": {
        "name": "Pulaski",
        "state": "Pulaski TN",
        "population": 8158,
        "housing": 3779,
        "land_area": 13076286.0,
        "water_area": 0.0
    },
    "72694": {
        "name": "Pulaski",
        "state": "Pulaski VA",
        "population": 16588,
        "housing": 7616,
        "land_area": 39909690.0,
        "water_area": 176687.0
    },
    "72748": {
        "name": "Pullman",
        "state": "Pullman WA",
        "population": 32691,
        "housing": 13588,
        "land_area": 21061238.0,
        "water_area": 0.0
    },
    "72775": {
        "name": "Punxsutawney",
        "state": "Punxsutawney PA",
        "population": 6199,
        "housing": 3359,
        "land_area": 9882435.0,
        "water_area": 127234.0
    },
    "72803": {
        "name": "Pupukea",
        "state": "Pupukea HI",
        "population": 15509,
        "housing": 6503,
        "land_area": 20217356.0,
        "water_area": 96763.0
    },
    "72829": {
        "name": "Purcell",
        "state": "Purcell OK",
        "population": 7327,
        "housing": 3111,
        "land_area": 12414312.0,
        "water_area": 28171.0
    },
    "72856": {
        "name": "Purcellville",
        "state": "Purcellville VA",
        "population": 16475,
        "housing": 5316,
        "land_area": 19465922.0,
        "water_area": 70834.0
    },
    "72868": {
        "name": "Putnam--Killingly",
        "state": "Putnam--Killingly CT",
        "population": 34582,
        "housing": 15702,
        "land_area": 80328871.0,
        "water_area": 1791135.0
    },
    "72937": {
        "name": "Quartzsite",
        "state": "Quartzsite AZ",
        "population": 2280,
        "housing": 2543,
        "land_area": 8473647.0,
        "water_area": 0.0
    },
    "73018": {
        "name": "Quincy",
        "state": "Quincy FL",
        "population": 8541,
        "housing": 3584,
        "land_area": 15939641.0,
        "water_area": 0.0
    },
    "73045": {
        "name": "Quincy",
        "state": "Quincy IL",
        "population": 43427,
        "housing": 20724,
        "land_area": 58852783.0,
        "water_area": 133107.0
    },
    "73072": {
        "name": "Quincy",
        "state": "Quincy WA",
        "population": 7846,
        "housing": 2526,
        "land_area": 13560316.0,
        "water_area": 0.0
    },
    "73153": {
        "name": "Racine",
        "state": "Racine WI",
        "population": 134877,
        "housing": 58182,
        "land_area": 135346466.0,
        "water_area": 1379581.0
    },
    "73207": {
        "name": "Radford",
        "state": "Radford VA",
        "population": 19521,
        "housing": 8268,
        "land_area": 28631229.0,
        "water_area": 460092.0
    },
    "73251": {
        "name": "Rainbow Springs",
        "state": "Rainbow Springs FL",
        "population": 4667,
        "housing": 2540,
        "land_area": 14797799.0,
        "water_area": 69853.0
    },
    "73261": {
        "name": "Raleigh",
        "state": "Raleigh NC",
        "population": 1106646,
        "housing": 455527,
        "land_area": 1436973664.0,
        "water_area": 13870659.0
    },
    "73288": {
        "name": "Ramona",
        "state": "Ramona CA",
        "population": 14837,
        "housing": 5076,
        "land_area": 17847252.0,
        "water_area": 0.0
    },
    "73315": {
        "name": "Rancho Calaveras",
        "state": "Rancho Calaveras CA",
        "population": 8164,
        "housing": 3212,
        "land_area": 18677334.0,
        "water_area": 8976.0
    },
    "73353": {
        "name": "Ranson--Charles Town",
        "state": "Ranson--Charles Town WV",
        "population": 21569,
        "housing": 8883,
        "land_area": 29499151.0,
        "water_area": 89975.0
    },
    "73369": {
        "name": "Rantoul",
        "state": "Rantoul IL",
        "population": 13654,
        "housing": 6328,
        "land_area": 23306939.0,
        "water_area": 269802.0
    },
    "73396": {
        "name": "Rapid City",
        "state": "Rapid City SD",
        "population": 85679,
        "housing": 38024,
        "land_area": 130350277.0,
        "water_area": 159937.0
    },
    "73423": {
        "name": "Rathdrum",
        "state": "Rathdrum ID",
        "population": 9241,
        "housing": 3485,
        "land_area": 10481784.0,
        "water_area": 0.0
    },
    "73450": {
        "name": "Raton",
        "state": "Raton NM",
        "population": 5629,
        "housing": 3204,
        "land_area": 12159757.0,
        "water_area": 0.0
    },
    "73477": {
        "name": "Ravena--Bethlehem",
        "state": "Ravena--Bethlehem NY",
        "population": 9346,
        "housing": 4435,
        "land_area": 30498326.0,
        "water_area": 33976.0
    },
    "73531": {
        "name": "Rawlins",
        "state": "Rawlins WY",
        "population": 7700,
        "housing": 4067,
        "land_area": 12424281.0,
        "water_area": 0.0
    },
    "73558": {
        "name": "Raymond",
        "state": "Raymond NH",
        "population": 5266,
        "housing": 2317,
        "land_area": 15724171.0,
        "water_area": 508800.0
    },
    "73612": {
        "name": "Raymondville",
        "state": "Raymondville TX",
        "population": 12986,
        "housing": 3991,
        "land_area": 14592800.0,
        "water_area": 151218.0
    },
    "73639": {
        "name": "Rayne",
        "state": "Rayne LA",
        "population": 8493,
        "housing": 3811,
        "land_area": 12315531.0,
        "water_area": 0.0
    },
    "73693": {
        "name": "Reading",
        "state": "Reading PA",
        "population": 276278,
        "housing": 110684,
        "land_area": 248950275.0,
        "water_area": 2113848.0
    },
    "73720": {
        "name": "Red Bluff",
        "state": "Red Bluff CA",
        "population": 19826,
        "housing": 8274,
        "land_area": 25898280.0,
        "water_area": 283729.0
    },
    "73774": {
        "name": "Redding",
        "state": "Redding CA",
        "population": 120602,
        "housing": 51389,
        "land_area": 173683290.0,
        "water_area": 3182367.0
    },
    "73882": {
        "name": "Redmond",
        "state": "Redmond OR",
        "population": 33293,
        "housing": 13308,
        "land_area": 37083874.0,
        "water_area": 1879.0
    },
    "73909": {
        "name": "Red Oak",
        "state": "Red Oak IA",
        "population": 5516,
        "housing": 2757,
        "land_area": 7450940.0,
        "water_area": 0.0
    },
    "73963": {
        "name": "Red Wing",
        "state": "Red Wing MN",
        "population": 14857,
        "housing": 6869,
        "land_area": 21274254.0,
        "water_area": 128505.0
    },
    "73990": {
        "name": "Redwood Falls",
        "state": "Redwood Falls MN",
        "population": 4608,
        "housing": 2247,
        "land_area": 8520508.0,
        "water_area": 13019.0
    },
    "74044": {
        "name": "Reedley--Dinuba",
        "state": "Reedley--Dinuba CA",
        "population": 49614,
        "housing": 14113,
        "land_area": 25055205.0,
        "water_area": 29051.0
    },
    "74071": {
        "name": "Reedsburg",
        "state": "Reedsburg WI",
        "population": 10067,
        "housing": 4481,
        "land_area": 14789410.0,
        "water_area": 72466.0
    },
    "74098": {
        "name": "Reedsport",
        "state": "Reedsport OR",
        "population": 4503,
        "housing": 2283,
        "land_area": 5444761.0,
        "water_area": 5291.0
    },
    "74152": {
        "name": "Reidsville",
        "state": "Reidsville NC",
        "population": 14653,
        "housing": 7315,
        "land_area": 33254305.0,
        "water_area": 104345.0
    },
    "74179": {
        "name": "Reno",
        "state": "Reno NV--CA",
        "population": 446529,
        "housing": 187560,
        "land_area": 428465811.0,
        "water_area": 2854935.0
    },
    "74206": {
        "name": "Rensselaer",
        "state": "Rensselaer IN",
        "population": 5509,
        "housing": 2510,
        "land_area": 8373941.0,
        "water_area": 12773.0
    },
    "74233": {
        "name": "Republic",
        "state": "Republic MO",
        "population": 18446,
        "housing": 7323,
        "land_area": 19381581.0,
        "water_area": 0.0
    },
    "74260": {
        "name": "Rexburg",
        "state": "Rexburg ID",
        "population": 41330,
        "housing": 10591,
        "land_area": 24295731.0,
        "water_area": 194766.0
    },
    "74341": {
        "name": "Rhinelander",
        "state": "Rhinelander WI",
        "population": 9738,
        "housing": 5016,
        "land_area": 25790121.0,
        "water_area": 3140525.0
    },
    "74368": {
        "name": "Rice Lake",
        "state": "Rice Lake WI",
        "population": 10156,
        "housing": 4991,
        "land_area": 21527061.0,
        "water_area": 0.0
    },
    "74395": {
        "name": "Richfield",
        "state": "Richfield UT",
        "population": 8393,
        "housing": 3020,
        "land_area": 12281703.0,
        "water_area": 0.0
    },
    "74557": {
        "name": "Richland Center",
        "state": "Richland Center WI",
        "population": 4924,
        "housing": 2452,
        "land_area": 7320556.0,
        "water_area": 218772.0
    },
    "74584": {
        "name": "Richlands",
        "state": "Richlands VA",
        "population": 8746,
        "housing": 4547,
        "land_area": 21463060.0,
        "water_area": 273397.0
    },
    "74638": {
        "name": "Richmond",
        "state": "Richmond IN--OH",
        "population": 43130,
        "housing": 20795,
        "land_area": 64574102.0,
        "water_area": 472957.0
    },
    "74665": {
        "name": "Richmond",
        "state": "Richmond KY",
        "population": 42999,
        "housing": 19014,
        "land_area": 61235224.0,
        "water_area": 310527.0
    },
    "74692": {
        "name": "Richmond",
        "state": "Richmond MI",
        "population": 6034,
        "housing": 2628,
        "land_area": 7657553.0,
        "water_area": 90906.0
    },
    "74719": {
        "name": "Richmond",
        "state": "Richmond MO",
        "population": 5857,
        "housing": 2738,
        "land_area": 11397452.0,
        "water_area": 32998.0
    },
    "74746": {
        "name": "Richmond",
        "state": "Richmond VA",
        "population": 1059150,
        "housing": 447842,
        "land_area": 1326968174.0,
        "water_area": 27170510.0
    },
    "74827": {
        "name": "Ridgecrest",
        "state": "Ridgecrest CA",
        "population": 29307,
        "housing": 13017,
        "land_area": 37046348.0,
        "water_area": 1077.0
    },
    "74837": {
        "name": "Ridgefield",
        "state": "Ridgefield CT",
        "population": 25683,
        "housing": 10075,
        "land_area": 74591328.0,
        "water_area": 1304823.0
    },
    "74840": {
        "name": "Ridgefield",
        "state": "Ridgefield WA",
        "population": 10356,
        "housing": 3714,
        "land_area": 16694471.0,
        "water_area": 27675.0
    },
    "74908": {
        "name": "Ridgway",
        "state": "Ridgway PA",
        "population": 4259,
        "housing": 2132,
        "land_area": 6495687.0,
        "water_area": 98944.0
    },
    "74935": {
        "name": "Rifle",
        "state": "Rifle CO",
        "population": 11469,
        "housing": 4082,
        "land_area": 10356978.0,
        "water_area": 0.0
    },
    "74962": {
        "name": "Rigby",
        "state": "Rigby ID",
        "population": 10283,
        "housing": 3361,
        "land_area": 19727352.0,
        "water_area": 57802.0
    },
    "74989": {
        "name": "Rincon",
        "state": "Rincon GA",
        "population": 14113,
        "housing": 5514,
        "land_area": 22781307.0,
        "water_area": 14197.0
    },
    "75072": {
        "name": "Rio Grande City--Roma",
        "state": "Rio Grande City--Roma TX",
        "population": 47070,
        "housing": 16204,
        "land_area": 50484921.0,
        "water_area": 356185.0
    },
    "75092": {
        "name": "Rio Verde",
        "state": "Rio Verde AZ",
        "population": 2765,
        "housing": 2088,
        "land_area": 7922293.0,
        "water_area": 14833.0
    },
    "75097": {
        "name": "Rio Vista",
        "state": "Rio Vista CA",
        "population": 9942,
        "housing": 5187,
        "land_area": 9346684.0,
        "water_area": 67292.0
    },
    "75151": {
        "name": "Ripley",
        "state": "Ripley TN",
        "population": 6922,
        "housing": 3154,
        "land_area": 19321604.0,
        "water_area": 22153.0
    },
    "75198": {
        "name": "Ripon",
        "state": "Ripon CA",
        "population": 15829,
        "housing": 5596,
        "land_area": 10906620.0,
        "water_area": 5479.0
    },
    "75205": {
        "name": "Ripon",
        "state": "Ripon WI",
        "population": 8059,
        "housing": 3581,
        "land_area": 11112566.0,
        "water_area": 132997.0
    },
    "75232": {
        "name": "Rising Sun",
        "state": "Rising Sun MD",
        "population": 5788,
        "housing": 2301,
        "land_area": 13414016.0,
        "water_area": 154880.0
    },
    "75286": {
        "name": "River Falls",
        "state": "River Falls WI",
        "population": 16344,
        "housing": 6005,
        "land_area": 16383858.0,
        "water_area": 203328.0
    },
    "75313": {
        "name": "Riverhead--Southold",
        "state": "Riverhead--Southold NY",
        "population": 51120,
        "housing": 26502,
        "land_area": 136742191.0,
        "water_area": 6069855.0
    },
    "75340": {
        "name": "Riverside--San Bernardino",
        "state": "Riverside--San Bernardino CA",
        "population": 2276703,
        "housing": 683675,
        "land_area": 1576174299.0,
        "water_area": 6671665.0
    },
    "75367": {
        "name": "Riverton",
        "state": "Riverton WY",
        "population": 11234,
        "housing": 5079,
        "land_area": 17852509.0,
        "water_area": 20785.0
    },
    "75421": {
        "name": "Roanoke",
        "state": "Roanoke VA",
        "population": 217312,
        "housing": 100135,
        "land_area": 325042587.0,
        "water_area": 2047241.0
    },
    "75448": {
        "name": "Roanoke Rapids",
        "state": "Roanoke Rapids NC",
        "population": 23400,
        "housing": 11171,
        "land_area": 40956545.0,
        "water_area": 87583.0
    },
    "75475": {
        "name": "Roaring Spring",
        "state": "Roaring Spring PA",
        "population": 6239,
        "housing": 2929,
        "land_area": 14524581.0,
        "water_area": 0.0
    },
    "75502": {
        "name": "Robertsdale",
        "state": "Robertsdale AL",
        "population": 7429,
        "housing": 3003,
        "land_area": 15229695.0,
        "water_area": 14294.0
    },
    "75529": {
        "name": "Robinson",
        "state": "Robinson IL",
        "population": 6134,
        "housing": 3202,
        "land_area": 11667402.0,
        "water_area": 29100.0
    },
    "75556": {
        "name": "Robstown",
        "state": "Robstown TX",
        "population": 10775,
        "housing": 4198,
        "land_area": 9491863.0,
        "water_area": 0.0
    },
    "75583": {
        "name": "Rochelle",
        "state": "Rochelle IL",
        "population": 11013,
        "housing": 4767,
        "land_area": 24779625.0,
        "water_area": 49946.0
    },
    "75610": {
        "name": "Rochester",
        "state": "Rochester IN",
        "population": 7333,
        "housing": 3706,
        "land_area": 13175227.0,
        "water_area": 2891414.0
    },
    "75637": {
        "name": "Rochester",
        "state": "Rochester MN",
        "population": 121587,
        "housing": 53319,
        "land_area": 133621364.0,
        "water_area": 657765.0
    },
    "75664": {
        "name": "Rochester",
        "state": "Rochester NY",
        "population": 704327,
        "housing": 314417,
        "land_area": 755834267.0,
        "water_area": 15847083.0
    },
    "75679": {
        "name": "Rockaway Beach",
        "state": "Rockaway Beach OR",
        "population": 1761,
        "housing": 2552,
        "land_area": 5320996.0,
        "water_area": 23095.0
    },
    "75691": {
        "name": "Rockdale",
        "state": "Rockdale TX",
        "population": 5464,
        "housing": 2537,
        "land_area": 8550770.0,
        "water_area": 0.0
    },
    "75718": {
        "name": "Rockford",
        "state": "Rockford IL",
        "population": 276443,
        "housing": 119742,
        "land_area": 346575428.0,
        "water_area": 5432789.0
    },
    "75745": {
        "name": "Rock Hill",
        "state": "Rock Hill SC",
        "population": 218443,
        "housing": 89706,
        "land_area": 375936502.0,
        "water_area": 6953518.0
    },
    "75772": {
        "name": "Rockingham",
        "state": "Rockingham NC",
        "population": 23833,
        "housing": 11098,
        "land_area": 59902877.0,
        "water_area": 126876.0
    },
    "75799": {
        "name": "Rockland",
        "state": "Rockland ME",
        "population": 9868,
        "housing": 5764,
        "land_area": 34253504.0,
        "water_area": 304429.0
    },
    "75826": {
        "name": "Rockmart",
        "state": "Rockmart GA",
        "population": 7743,
        "housing": 3265,
        "land_area": 21061016.0,
        "water_area": 317922.0
    },
    "75853": {
        "name": "Rockport",
        "state": "Rockport TX",
        "population": 16217,
        "housing": 10898,
        "land_area": 49427181.0,
        "water_area": 1561027.0
    },
    "75880": {
        "name": "Rock Springs",
        "state": "Rock Springs WY",
        "population": 25853,
        "housing": 11777,
        "land_area": 35364968.0,
        "water_area": 0.0
    },
    "75988": {
        "name": "Rocky Mount",
        "state": "Rocky Mount NC",
        "population": 63297,
        "housing": 30235,
        "land_area": 116432128.0,
        "water_area": 200938.0
    },
    "76015": {
        "name": "Rocky Mount",
        "state": "Rocky Mount VA",
        "population": 5411,
        "housing": 2585,
        "land_area": 17498784.0,
        "water_area": 29033.0
    },
    "76069": {
        "name": "Rogersville",
        "state": "Rogersville TN",
        "population": 6154,
        "housing": 2972,
        "land_area": 15832923.0,
        "water_area": 0.0
    },
    "76123": {
        "name": "Rolla",
        "state": "Rolla MO",
        "population": 20610,
        "housing": 9555,
        "land_area": 29966746.0,
        "water_area": 43306.0
    },
    "76204": {
        "name": "Rome",
        "state": "Rome GA",
        "population": 60403,
        "housing": 24813,
        "land_area": 116271186.0,
        "water_area": 2587480.0
    },
    "76231": {
        "name": "Rome",
        "state": "Rome NY",
        "population": 29222,
        "housing": 14264,
        "land_area": 44872844.0,
        "water_area": 63129.0
    },
    "76258": {
        "name": "Roosevelt",
        "state": "Roosevelt UT",
        "population": 6316,
        "housing": 2361,
        "land_area": 8644779.0,
        "water_area": 0.0
    },
    "76285": {
        "name": "Rosamond",
        "state": "Rosamond CA",
        "population": 17538,
        "housing": 6395,
        "land_area": 14540089.0,
        "water_area": 33333.0
    },
    "76339": {
        "name": "Roseburg",
        "state": "Roseburg OR",
        "population": 43484,
        "housing": 19020,
        "land_area": 53236303.0,
        "water_area": 1465424.0
    },
    "76447": {
        "name": "Roswell",
        "state": "Roswell NM",
        "population": 48831,
        "housing": 20562,
        "land_area": 65075175.0,
        "water_area": 143362.0
    },
    "76474": {
        "name": "Round Lake Beach--McHenry--Grayslake",
        "state": "Round Lake Beach--McHenry--Grayslake IL--WI",
        "population": 261835,
        "housing": 104091,
        "land_area": 330515051.0,
        "water_area": 25672566.0
    },
    "76501": {
        "name": "Roxboro",
        "state": "Roxboro NC",
        "population": 9500,
        "housing": 4590,
        "land_area": 20973653.0,
        "water_area": 12344.0
    },
    "76528": {
        "name": "Roxborough Park",
        "state": "Roxborough Park CO",
        "population": 9090,
        "housing": 3299,
        "land_area": 9914675.0,
        "water_area": 0.0
    },
    "76555": {
        "name": "Royse City",
        "state": "Royse City TX",
        "population": 13922,
        "housing": 4799,
        "land_area": 15887851.0,
        "water_area": 75670.0
    },
    "76636": {
        "name": "Ruidoso",
        "state": "Ruidoso NM",
        "population": 11042,
        "housing": 11251,
        "land_area": 43729529.0,
        "water_area": 0.0
    },
    "76690": {
        "name": "Rumford",
        "state": "Rumford ME",
        "population": 5585,
        "housing": 3064,
        "land_area": 8086950.0,
        "water_area": 245874.0
    },
    "76717": {
        "name": "Running Springs",
        "state": "Running Springs CA",
        "population": 5313,
        "housing": 3710,
        "land_area": 9435419.0,
        "water_area": 23184.0
    },
    "76744": {
        "name": "Rupert",
        "state": "Rupert ID",
        "population": 6534,
        "housing": 2532,
        "land_area": 7150532.0,
        "water_area": 26291.0
    },
    "76798": {
        "name": "Rushville",
        "state": "Rushville IN",
        "population": 6469,
        "housing": 3009,
        "land_area": 7478078.0,
        "water_area": 0.0
    },
    "76852": {
        "name": "Russell",
        "state": "Russell KS",
        "population": 4066,
        "housing": 2130,
        "land_area": 5903534.0,
        "water_area": 0.0
    },
    "76879": {
        "name": "Russells Point",
        "state": "Russells Point OH",
        "population": 6451,
        "housing": 6341,
        "land_area": 16178311.0,
        "water_area": 99691.0
    },
    "76906": {
        "name": "Russellville",
        "state": "Russellville AL",
        "population": 9939,
        "housing": 3847,
        "land_area": 17999410.0,
        "water_area": 34674.0
    },
    "76933": {
        "name": "Russellville",
        "state": "Russellville AR",
        "population": 31870,
        "housing": 13319,
        "land_area": 50396693.0,
        "water_area": 10932.0
    },
    "76960": {
        "name": "Russellville",
        "state": "Russellville KY",
        "population": 6641,
        "housing": 3252,
        "land_area": 17371567.0,
        "water_area": 35488.0
    },
    "76987": {
        "name": "Ruston",
        "state": "Ruston LA",
        "population": 28839,
        "housing": 11814,
        "land_area": 63306519.0,
        "water_area": 143866.0
    },
    "77014": {
        "name": "Rutland",
        "state": "Rutland VT",
        "population": 19550,
        "housing": 10162,
        "land_area": 35557929.0,
        "water_area": 367752.0
    },
    "77068": {
        "name": "Sacramento",
        "state": "Sacramento CA",
        "population": 1946618,
        "housing": 726246,
        "land_area": 1211031346.0,
        "water_area": 13333435.0
    },
    "77095": {
        "name": "Safford",
        "state": "Safford AZ",
        "population": 18331,
        "housing": 7461,
        "land_area": 27077817.0,
        "water_area": 7239.0
    },
    "77149": {
        "name": "Saginaw",
        "state": "Saginaw MI",
        "population": 116058,
        "housing": 54759,
        "land_area": 170238304.0,
        "water_area": 3242027.0
    },
    "77158": {
        "name": "Sahuarita",
        "state": "Sahuarita AZ",
        "population": 17276,
        "housing": 5718,
        "land_area": 12404696.0,
        "water_area": 0.0
    },
    "77176": {
        "name": "St. Albans",
        "state": "St. Albans VT",
        "population": 11368,
        "housing": 5232,
        "land_area": 20634347.0,
        "water_area": 12608.0
    },
    "77230": {
        "name": "St. Augustine",
        "state": "St. Augustine FL",
        "population": 91786,
        "housing": 48906,
        "land_area": 149639844.0,
        "water_area": 9012339.0
    },
    "77311": {
        "name": "St. Clair",
        "state": "St. Clair MO",
        "population": 6303,
        "housing": 2735,
        "land_area": 10146074.0,
        "water_area": 329722.0
    },
    "77338": {
        "name": "St. Cloud",
        "state": "St. Cloud MN",
        "population": 117638,
        "housing": 48944,
        "land_area": 139781775.0,
        "water_area": 4367553.0
    },
    "77365": {
        "name": "Ste. Genevieve",
        "state": "Ste. Genevieve MO",
        "population": 4988,
        "housing": 2097,
        "land_area": 8125643.0,
        "water_area": 5378.0
    },
    "77392": {
        "name": "St. Francis",
        "state": "St. Francis MN",
        "population": 6157,
        "housing": 2294,
        "land_area": 10135954.0,
        "water_area": 168653.0
    },
    "77446": {
        "name": "St. George",
        "state": "St. George UT",
        "population": 134109,
        "housing": 55868,
        "land_area": 158058286.0,
        "water_area": 21969.0
    },
    "77473": {
        "name": "St. Helen",
        "state": "St. Helen MI",
        "population": 2522,
        "housing": 2342,
        "land_area": 9038323.0,
        "water_area": 40658.0
    },
    "77500": {
        "name": "St. Helena",
        "state": "St. Helena CA",
        "population": 6086,
        "housing": 3050,
        "land_area": 11647885.0,
        "water_area": 120265.0
    },
    "77527": {
        "name": "St. Helens",
        "state": "St. Helens OR",
        "population": 19112,
        "housing": 7794,
        "land_area": 24547839.0,
        "water_area": 222774.0
    },
    "77554": {
        "name": "St. Ignace",
        "state": "St. Ignace MI",
        "population": 3457,
        "housing": 2336,
        "land_area": 14750707.0,
        "water_area": 77944.0
    },
    "77618": {
        "name": "St. James",
        "state": "St. James NC",
        "population": 7029,
        "housing": 4434,
        "land_area": 23235391.0,
        "water_area": 13259.0
    },
    "77625": {
        "name": "St. James City",
        "state": "St. James City FL",
        "population": 2055,
        "housing": 2000,
        "land_area": 5037962.0,
        "water_area": 1015263.0
    },
    "77662": {
        "name": "St. Johns",
        "state": "St. Johns MI",
        "population": 8370,
        "housing": 3827,
        "land_area": 12682997.0,
        "water_area": 47063.0
    },
    "77689": {
        "name": "St. Johnsbury",
        "state": "St. Johnsbury VT",
        "population": 4883,
        "housing": 2472,
        "land_area": 8328523.0,
        "water_area": 225342.0
    },
    "77743": {
        "name": "St. Joseph",
        "state": "St. Joseph MO--KS",
        "population": 77187,
        "housing": 35119,
        "land_area": 114859424.0,
        "water_area": 1124845.0
    },
    "77770": {
        "name": "St. Louis",
        "state": "St. Louis MO--IL",
        "population": 2156323,
        "housing": 975765,
        "land_area": 2357846658.0,
        "water_area": 32020389.0
    },
    "77824": {
        "name": "St. Martinville",
        "state": "St. Martinville LA",
        "population": 6399,
        "housing": 3077,
        "land_area": 10654790.0,
        "water_area": 149407.0
    },
    "77851": {
        "name": "St. Marys",
        "state": "St. Marys OH",
        "population": 9452,
        "housing": 4211,
        "land_area": 12457154.0,
        "water_area": 59290.0
    },
    "77878": {
        "name": "St. Marys",
        "state": "St. Marys PA",
        "population": 9402,
        "housing": 4552,
        "land_area": 18656568.0,
        "water_area": 874.0
    },
    "78013": {
        "name": "St. Peter",
        "state": "St. Peter MN",
        "population": 12145,
        "housing": 4119,
        "land_area": 12983113.0,
        "water_area": 0.0
    },
    "78067": {
        "name": "Salamanca",
        "state": "Salamanca NY",
        "population": 6375,
        "housing": 3131,
        "land_area": 12846613.0,
        "water_area": 477359.0
    },
    "78094": {
        "name": "Salem",
        "state": "Salem IL",
        "population": 7153,
        "housing": 3350,
        "land_area": 12683018.0,
        "water_area": 167909.0
    },
    "78121": {
        "name": "Salem",
        "state": "Salem IN",
        "population": 6617,
        "housing": 3016,
        "land_area": 10382766.0,
        "water_area": 7271.0
    },
    "78148": {
        "name": "Salem",
        "state": "Salem MO",
        "population": 4684,
        "housing": 2359,
        "land_area": 8167350.0,
        "water_area": 0.0
    },
    "78175": {
        "name": "Salem",
        "state": "Salem NJ",
        "population": 5927,
        "housing": 2861,
        "land_area": 7962412.0,
        "water_area": 177232.0
    },
    "78202": {
        "name": "Salem",
        "state": "Salem OH",
        "population": 15924,
        "housing": 7617,
        "land_area": 27496341.0,
        "water_area": 15590.0
    },
    "78229": {
        "name": "Salem",
        "state": "Salem OR",
        "population": 268331,
        "housing": 101688,
        "land_area": 188247416.0,
        "water_area": 176238.0
    },
    "78256": {
        "name": "Salida",
        "state": "Salida CO",
        "population": 5953,
        "housing": 3415,
        "land_area": 7380783.0,
        "water_area": 0.0
    },
    "78283": {
        "name": "Salina",
        "state": "Salina KS",
        "population": 46547,
        "housing": 20770,
        "land_area": 58340373.0,
        "water_area": 58589.0
    },
    "78310": {
        "name": "Salinas",
        "state": "Salinas CA",
        "population": 177532,
        "housing": 48914,
        "land_area": 77392699.0,
        "water_area": 101332.0
    },
    "78337": {
        "name": "Salinas--Coco",
        "state": "Salinas--Coco PR",
        "population": 13938,
        "housing": 7628,
        "land_area": 13362231.0,
        "water_area": 30944.0
    },
    "78364": {
        "name": "Salisbury",
        "state": "Salisbury MD--DE",
        "population": 78075,
        "housing": 32638,
        "land_area": 125341432.0,
        "water_area": 4258719.0
    },
    "78418": {
        "name": "Sallisaw",
        "state": "Sallisaw OK",
        "population": 7513,
        "housing": 3431,
        "land_area": 14075536.0,
        "water_area": 66774.0
    },
    "78499": {
        "name": "Salt Lake City",
        "state": "Salt Lake City UT",
        "population": 1178533,
        "housing": 424925,
        "land_area": 778073097.0,
        "water_area": 510795.0
    },
    "78553": {
        "name": "San Angelo",
        "state": "San Angelo TX",
        "population": 99982,
        "housing": 43410,
        "land_area": 127600935.0,
        "water_area": 2639628.0
    },
    "78580": {
        "name": "San Antonio",
        "state": "San Antonio TX",
        "population": 1992689,
        "housing": 789482,
        "land_area": 1588802530.0,
        "water_area": 8698357.0
    },
    "78634": {
        "name": "Sandersville",
        "state": "Sandersville GA",
        "population": 7097,
        "housing": 3344,
        "land_area": 21537788.0,
        "water_area": 115345.0
    },
    "78661": {
        "name": "San Diego",
        "state": "San Diego CA",
        "population": 3070300,
        "housing": 1149240,
        "land_area": 1747527134.0,
        "water_area": 37840003.0
    },
    "78699": {
        "name": "San Diego Country Estates",
        "state": "San Diego Country Estates CA",
        "population": 7002,
        "housing": 2450,
        "land_area": 8522759.0,
        "water_area": 0.0
    },
    "78715": {
        "name": "Sandpoint",
        "state": "Sandpoint ID",
        "population": 12824,
        "housing": 6419,
        "land_area": 23962522.0,
        "water_area": 372879.0
    },
    "78774": {
        "name": "Sandusky--Port Clinton",
        "state": "Sandusky--Port Clinton OH",
        "population": 61743,
        "housing": 39351,
        "land_area": 139336526.0,
        "water_area": 13775535.0
    },
    "78823": {
        "name": "Sandy",
        "state": "Sandy OR",
        "population": 13173,
        "housing": 4899,
        "land_area": 10900427.0,
        "water_area": 2984.0
    },
    "78850": {
        "name": "Sanford",
        "state": "Sanford ME",
        "population": 15067,
        "housing": 6885,
        "land_area": 15481402.0,
        "water_area": 350678.0
    },
    "78877": {
        "name": "Sanford",
        "state": "Sanford NC",
        "population": 36641,
        "housing": 15279,
        "land_area": 88886073.0,
        "water_area": 2215929.0
    },
    "78904": {
        "name": "San Francisco--Oakland",
        "state": "San Francisco--Oakland CA",
        "population": 3515933,
        "housing": 1391873,
        "land_area": 1330727047.0,
        "water_area": 31271476.0
    },
    "78931": {
        "name": "Sanger",
        "state": "Sanger CA",
        "population": 27325,
        "housing": 7986,
        "land_area": 12469370.0,
        "water_area": 0.0
    },
    "78958": {
        "name": "Sanger",
        "state": "Sanger TX",
        "population": 8279,
        "housing": 3144,
        "land_area": 11371906.0,
        "water_area": 57008.0
    },
    "78985": {
        "name": "San Germ\u00e1n--Cabo Rojo--Sabana Grande",
        "state": "San Germ\u00e1n--Cabo Rojo--Sabana Grande PR",
        "population": 97241,
        "housing": 51393,
        "land_area": 182823924.0,
        "water_area": 1496050.0
    },
    "79039": {
        "name": "San Jose",
        "state": "San Jose CA",
        "population": 1837446,
        "housing": 658649,
        "land_area": 739386477.0,
        "water_area": 1271373.0
    },
    "79093": {
        "name": "San Juan",
        "state": "San Juan PR",
        "population": 1844410,
        "housing": 888356,
        "land_area": 1977727163.0,
        "water_area": 23788757.0
    },
    "79120": {
        "name": "San Luis",
        "state": "San Luis AZ",
        "population": 24790,
        "housing": 6634,
        "land_area": 9567919.0,
        "water_area": 54603.0
    },
    "79147": {
        "name": "San Luis Obispo",
        "state": "San Luis Obispo CA",
        "population": 56904,
        "housing": 22210,
        "land_area": 36537326.0,
        "water_area": 55665.0
    },
    "79201": {
        "name": "San Marcos",
        "state": "San Marcos TX",
        "population": 70801,
        "housing": 30583,
        "land_area": 63296277.0,
        "water_area": 115426.0
    },
    "79282": {
        "name": "Santa Barbara",
        "state": "Santa Barbara CA",
        "population": 202197,
        "housing": 79353,
        "land_area": 141962041.0,
        "water_area": 2693338.0
    },
    "79309": {
        "name": "Santa Clarita",
        "state": "Santa Clarita CA",
        "population": 278031,
        "housing": 93011,
        "land_area": 201619528.0,
        "water_area": 1441456.0
    },
    "79336": {
        "name": "Santa Cruz",
        "state": "Santa Cruz CA",
        "population": 169038,
        "housing": 72855,
        "land_area": 156565432.0,
        "water_area": 509944.0
    },
    "79363": {
        "name": "Santa Fe",
        "state": "Santa Fe NM",
        "population": 94241,
        "housing": 47331,
        "land_area": 121214399.0,
        "water_area": 205406.0
    },
    "79390": {
        "name": "Santa Isabel",
        "state": "Santa Isabel PR",
        "population": 9742,
        "housing": 4866,
        "land_area": 7760927.0,
        "water_area": 11547.0
    },
    "79417": {
        "name": "Santa Maria",
        "state": "Santa Maria CA",
        "population": 143609,
        "housing": 42245,
        "land_area": 70087992.0,
        "water_area": 197276.0
    },
    "79444": {
        "name": "Santa Paula",
        "state": "Santa Paula CA",
        "population": 30675,
        "housing": 9189,
        "land_area": 12857643.0,
        "water_area": 10736.0
    },
    "79498": {
        "name": "Santa Rosa",
        "state": "Santa Rosa CA",
        "population": 297329,
        "housing": 116326,
        "land_area": 205625198.0,
        "water_area": 470825.0
    },
    "79579": {
        "name": "Saranac Lake",
        "state": "Saranac Lake NY",
        "population": 5163,
        "housing": 3084,
        "land_area": 8023763.0,
        "water_area": 668094.0
    },
    "79633": {
        "name": "Saratoga Springs",
        "state": "Saratoga Springs NY",
        "population": 75684,
        "housing": 37354,
        "land_area": 143950664.0,
        "water_area": 548657.0
    },
    "79687": {
        "name": "Sauk Centre",
        "state": "Sauk Centre MN",
        "population": 4849,
        "housing": 2256,
        "land_area": 12200881.0,
        "water_area": 669942.0
    },
    "79714": {
        "name": "Sault Ste. Marie",
        "state": "Sault Ste. Marie MI",
        "population": 12877,
        "housing": 6042,
        "land_area": 21188718.0,
        "water_area": 200900.0
    },
    "79768": {
        "name": "Savannah",
        "state": "Savannah GA",
        "population": 309466,
        "housing": 136572,
        "land_area": 533142236.0,
        "water_area": 8970297.0
    },
    "79795": {
        "name": "Savannah",
        "state": "Savannah MO",
        "population": 5253,
        "housing": 2301,
        "land_area": 8474446.0,
        "water_area": 22313.0
    },
    "79822": {
        "name": "Savannah",
        "state": "Savannah TN",
        "population": 8828,
        "housing": 4124,
        "land_area": 23021406.0,
        "water_area": 340.0
    },
    "79903": {
        "name": "Sayre--Waverly",
        "state": "Sayre--Waverly PA--NY",
        "population": 17262,
        "housing": 8295,
        "land_area": 20702672.0,
        "water_area": 102543.0
    },
    "79917": {
        "name": "Scappoose",
        "state": "Scappoose OR",
        "population": 9652,
        "housing": 4025,
        "land_area": 14996854.0,
        "water_area": 3907.0
    },
    "79957": {
        "name": "Schuyler",
        "state": "Schuyler NE",
        "population": 6522,
        "housing": 2031,
        "land_area": 6639609.0,
        "water_area": 153118.0
    },
    "79973": {
        "name": "Schuylkill Haven--Orwigsburg",
        "state": "Schuylkill Haven--Orwigsburg PA",
        "population": 14265,
        "housing": 6482,
        "land_area": 26583395.0,
        "water_area": 22563.0
    },
    "80038": {
        "name": "Scott City",
        "state": "Scott City MO",
        "population": 4949,
        "housing": 2238,
        "land_area": 12280907.0,
        "water_area": 2613.0
    },
    "80065": {
        "name": "Scottsbluff",
        "state": "Scottsbluff NE",
        "population": 25104,
        "housing": 11342,
        "land_area": 38028338.0,
        "water_area": 409822.0
    },
    "80092": {
        "name": "Scottsboro",
        "state": "Scottsboro AL",
        "population": 10791,
        "housing": 5239,
        "land_area": 31970898.0,
        "water_area": 0.0
    },
    "80119": {
        "name": "Scottsburg",
        "state": "Scottsburg IN",
        "population": 7578,
        "housing": 3510,
        "land_area": 12545467.0,
        "water_area": 62602.0
    },
    "80173": {
        "name": "Scottsville",
        "state": "Scottsville KY",
        "population": 4637,
        "housing": 2192,
        "land_area": 12362421.0,
        "water_area": 16054.0
    },
    "80227": {
        "name": "Scranton",
        "state": "Scranton PA",
        "population": 366713,
        "housing": 172990,
        "land_area": 419995196.0,
        "water_area": 8627792.0
    },
    "80241": {
        "name": "Seabrook Island",
        "state": "Seabrook Island SC",
        "population": 3371,
        "housing": 5286,
        "land_area": 21681111.0,
        "water_area": 756252.0
    },
    "80259": {
        "name": "Seaford--Laurel--Bridgeville",
        "state": "Seaford--Laurel--Bridgeville DE",
        "population": 29147,
        "housing": 11999,
        "land_area": 61081401.0,
        "water_area": 1485187.0
    },
    "80281": {
        "name": "Sealy",
        "state": "Sealy TX",
        "population": 6385,
        "housing": 2718,
        "land_area": 14718390.0,
        "water_area": 160229.0
    },
    "80308": {
        "name": "Searcy",
        "state": "Searcy AR",
        "population": 26652,
        "housing": 11658,
        "land_area": 55270493.0,
        "water_area": 109609.0
    },
    "80335": {
        "name": "Seaside",
        "state": "Seaside OR",
        "population": 9183,
        "housing": 6525,
        "land_area": 12945296.0,
        "water_area": 173105.0
    },
    "80362": {
        "name": "Seaside--Monterey--Pacific Grove",
        "state": "Seaside--Monterey--Pacific Grove CA",
        "population": 123495,
        "housing": 54906,
        "land_area": 106659553.0,
        "water_area": 551582.0
    },
    "80389": {
        "name": "Seattle--Tacoma",
        "state": "Seattle--Tacoma WA",
        "population": 3544011,
        "housing": 1468039,
        "land_area": 2544707994.0,
        "water_area": 150559818.0
    },
    "80405": {
        "name": "Sebastopol",
        "state": "Sebastopol CA",
        "population": 18734,
        "housing": 8245,
        "land_area": 39352738.0,
        "water_area": 0.0
    },
    "80416": {
        "name": "Sebring--Avon Park",
        "state": "Sebring--Avon Park FL",
        "population": 63297,
        "housing": 35215,
        "land_area": 115274385.0,
        "water_area": 10360203.0
    },
    "80443": {
        "name": "Sedalia",
        "state": "Sedalia MO",
        "population": 26043,
        "housing": 12068,
        "land_area": 44138673.0,
        "water_area": 188349.0
    },
    "80470": {
        "name": "Sedona",
        "state": "Sedona AZ",
        "population": 9190,
        "housing": 6317,
        "land_area": 32297893.0,
        "water_area": 60707.0
    },
    "80497": {
        "name": "Seguin",
        "state": "Seguin TX",
        "population": 28998,
        "housing": 12250,
        "land_area": 51794885.0,
        "water_area": 1963042.0
    },
    "80524": {
        "name": "Selma",
        "state": "Selma AL",
        "population": 21207,
        "housing": 10472,
        "land_area": 47365604.0,
        "water_area": 330985.0
    },
    "80551": {
        "name": "Selma",
        "state": "Selma CA",
        "population": 32546,
        "housing": 9737,
        "land_area": 23643888.0,
        "water_area": 0.0
    },
    "80605": {
        "name": "Seminole",
        "state": "Seminole OK",
        "population": 6283,
        "housing": 2870,
        "land_area": 13916132.0,
        "water_area": 0.0
    },
    "80632": {
        "name": "Seminole",
        "state": "Seminole TX",
        "population": 7068,
        "housing": 2807,
        "land_area": 8746908.0,
        "water_area": 0.0
    },
    "80659": {
        "name": "Senatobia",
        "state": "Senatobia MS",
        "population": 6817,
        "housing": 2275,
        "land_area": 10157776.0,
        "water_area": 120368.0
    },
    "80671": {
        "name": "Seneca",
        "state": "Seneca SC",
        "population": 23105,
        "housing": 11870,
        "land_area": 80399807.0,
        "water_area": 270088.0
    },
    "80686": {
        "name": "Sequim",
        "state": "Sequim WA",
        "population": 24864,
        "housing": 12889,
        "land_area": 82553227.0,
        "water_area": 1509.0
    },
    "80704": {
        "name": "Severance",
        "state": "Severance CO",
        "population": 6408,
        "housing": 2095,
        "land_area": 4943050.0,
        "water_area": 166186.0
    },
    "80713": {
        "name": "Sevierville",
        "state": "Sevierville TN",
        "population": 34032,
        "housing": 18818,
        "land_area": 120196084.0,
        "water_area": 0.0
    },
    "80767": {
        "name": "Seward",
        "state": "Seward NE",
        "population": 7473,
        "housing": 2959,
        "land_area": 8945137.0,
        "water_area": 27128.0
    },
    "80794": {
        "name": "Seymour",
        "state": "Seymour IN",
        "population": 24247,
        "housing": 9807,
        "land_area": 37221411.0,
        "water_area": 75722.0
    },
    "80805": {
        "name": "Seymour",
        "state": "Seymour TN",
        "population": 15219,
        "housing": 6297,
        "land_area": 35922050.0,
        "water_area": 23665.0
    },
    "80929": {
        "name": "Shafter",
        "state": "Shafter CA",
        "population": 19278,
        "housing": 5133,
        "land_area": 12426250.0,
        "water_area": 0.0
    },
    "80985": {
        "name": "Shamokin--Mount Carmel",
        "state": "Shamokin--Mount Carmel PA",
        "population": 28461,
        "housing": 14721,
        "land_area": 20341308.0,
        "water_area": 111840.0
    },
    "81020": {
        "name": "Sharon--Hermitage",
        "state": "Sharon--Hermitage PA--OH",
        "population": 42169,
        "housing": 21194,
        "land_area": 72936733.0,
        "water_area": 49661.0
    },
    "81064": {
        "name": "Shawano",
        "state": "Shawano WI",
        "population": 12229,
        "housing": 6869,
        "land_area": 27745399.0,
        "water_area": 1051137.0
    },
    "81091": {
        "name": "Shawnee",
        "state": "Shawnee OK",
        "population": 34245,
        "housing": 14979,
        "land_area": 48944793.0,
        "water_area": 56876.0
    },
    "81118": {
        "name": "Sheboygan",
        "state": "Sheboygan WI",
        "population": 74369,
        "housing": 33437,
        "land_area": 91450590.0,
        "water_area": 1093257.0
    },
    "81199": {
        "name": "Shelby",
        "state": "Shelby NC",
        "population": 25955,
        "housing": 12007,
        "land_area": 65976657.0,
        "water_area": 56266.0
    },
    "81226": {
        "name": "Shelby",
        "state": "Shelby OH",
        "population": 9317,
        "housing": 4348,
        "land_area": 12973814.0,
        "water_area": 159653.0
    },
    "81253": {
        "name": "Shelbyville",
        "state": "Shelbyville IL",
        "population": 4872,
        "housing": 2447,
        "land_area": 8036531.0,
        "water_area": 0.0
    },
    "81280": {
        "name": "Shelbyville",
        "state": "Shelbyville IN",
        "population": 21208,
        "housing": 9460,
        "land_area": 25028567.0,
        "water_area": 265011.0
    },
    "81307": {
        "name": "Shelbyville",
        "state": "Shelbyville KY",
        "population": 23143,
        "housing": 9241,
        "land_area": 29748681.0,
        "water_area": 375884.0
    },
    "81334": {
        "name": "Shelbyville",
        "state": "Shelbyville TN",
        "population": 22552,
        "housing": 8594,
        "land_area": 34096941.0,
        "water_area": 0.0
    },
    "81361": {
        "name": "Sheldon",
        "state": "Sheldon IA",
        "population": 5381,
        "housing": 2373,
        "land_area": 7302610.0,
        "water_area": 0.0
    },
    "81388": {
        "name": "Shelley",
        "state": "Shelley ID",
        "population": 5109,
        "housing": 1726,
        "land_area": 5622978.0,
        "water_area": 0.0
    },
    "81415": {
        "name": "Shelton",
        "state": "Shelton WA",
        "population": 14907,
        "housing": 5704,
        "land_area": 30265100.0,
        "water_area": 402341.0
    },
    "81442": {
        "name": "Shenandoah",
        "state": "Shenandoah IA",
        "population": 4872,
        "housing": 2511,
        "land_area": 7743086.0,
        "water_area": 0.0
    },
    "81472": {
        "name": "Shenandoah--Frackville",
        "state": "Shenandoah--Frackville PA",
        "population": 12025,
        "housing": 5638,
        "land_area": 7587731.0,
        "water_area": 168615.0
    },
    "81523": {
        "name": "Sheridan",
        "state": "Sheridan AR",
        "population": 4710,
        "housing": 2116,
        "land_area": 11669141.0,
        "water_area": 0.0
    },
    "81577": {
        "name": "Sheridan",
        "state": "Sheridan OR",
        "population": 6464,
        "housing": 1742,
        "land_area": 4494401.0,
        "water_area": 0.0
    },
    "81604": {
        "name": "Sheridan",
        "state": "Sheridan WY",
        "population": 19430,
        "housing": 9347,
        "land_area": 33639155.0,
        "water_area": 49823.0
    },
    "81631": {
        "name": "Sherman--Denison",
        "state": "Sherman--Denison TX",
        "population": 66691,
        "housing": 28718,
        "land_area": 99698210.0,
        "water_area": 309737.0
    },
    "81642": {
        "name": "Shinnston",
        "state": "Shinnston WV",
        "population": 4361,
        "housing": 2057,
        "land_area": 10248114.0,
        "water_area": 155972.0
    },
    "81658": {
        "name": "Shippensburg",
        "state": "Shippensburg PA",
        "population": 17014,
        "housing": 7339,
        "land_area": 25522165.0,
        "water_area": 85190.0
    },
    "81685": {
        "name": "Shiprock",
        "state": "Shiprock NM",
        "population": 6190,
        "housing": 1928,
        "land_area": 13529547.0,
        "water_area": 136249.0
    },
    "81712": {
        "name": "Show Low",
        "state": "Show Low AZ",
        "population": 12173,
        "housing": 8869,
        "land_area": 32635191.0,
        "water_area": 0.0
    },
    "81739": {
        "name": "Shreveport",
        "state": "Shreveport LA",
        "population": 288052,
        "housing": 133212,
        "land_area": 468381155.0,
        "water_area": 6661335.0
    },
    "81793": {
        "name": "Sidney",
        "state": "Sidney MT",
        "population": 6522,
        "housing": 3166,
        "land_area": 18219286.0,
        "water_area": 0.0
    },
    "81820": {
        "name": "Sidney",
        "state": "Sidney NE",
        "population": 6232,
        "housing": 3154,
        "land_area": 11493826.0,
        "water_area": 680.0
    },
    "81847": {
        "name": "Sidney",
        "state": "Sidney NY",
        "population": 4247,
        "housing": 2295,
        "land_area": 7290603.0,
        "water_area": 7950.0
    },
    "81874": {
        "name": "Sidney",
        "state": "Sidney OH",
        "population": 20734,
        "housing": 9280,
        "land_area": 27091529.0,
        "water_area": 274031.0
    },
    "81901": {
        "name": "Sierra Vista",
        "state": "Sierra Vista AZ",
        "population": 54274,
        "housing": 24495,
        "land_area": 73252360.0,
        "water_area": 35908.0
    },
    "81928": {
        "name": "Sikeston",
        "state": "Sikeston MO",
        "population": 17683,
        "housing": 7951,
        "land_area": 34053505.0,
        "water_area": 35951.0
    },
    "81955": {
        "name": "Siler City",
        "state": "Siler City NC",
        "population": 8616,
        "housing": 3228,
        "land_area": 13376176.0,
        "water_area": 37820.0
    },
    "81982": {
        "name": "Siloam Springs",
        "state": "Siloam Springs AR--OK",
        "population": 18027,
        "housing": 6734,
        "land_area": 24637357.0,
        "water_area": 189654.0
    },
    "82009": {
        "name": "Silsbee",
        "state": "Silsbee TX",
        "population": 9234,
        "housing": 4130,
        "land_area": 31325736.0,
        "water_area": 183333.0
    },
    "82036": {
        "name": "Silver City",
        "state": "Silver City NM",
        "population": 11817,
        "housing": 6002,
        "land_area": 27849226.0,
        "water_area": 57914.0
    },
    "82063": {
        "name": "Silver Creek",
        "state": "Silver Creek NY",
        "population": 3566,
        "housing": 2044,
        "land_area": 6375508.0,
        "water_area": 6643.0
    },
    "82076": {
        "name": "Silver Lakes",
        "state": "Silver Lakes CA",
        "population": 5908,
        "housing": 2649,
        "land_area": 5485415.0,
        "water_area": 1031337.0
    },
    "82090": {
        "name": "Silverthorne--Keystone",
        "state": "Silverthorne--Keystone CO",
        "population": 13867,
        "housing": 11960,
        "land_area": 28813972.0,
        "water_area": 258421.0
    },
    "82117": {
        "name": "Silverton",
        "state": "Silverton OR",
        "population": 10909,
        "housing": 4306,
        "land_area": 9500499.0,
        "water_area": 42776.0
    },
    "82144": {
        "name": "Simi Valley",
        "state": "Simi Valley CA",
        "population": 127364,
        "housing": 44405,
        "land_area": 81914688.0,
        "water_area": 1818681.0
    },
    "82171": {
        "name": "Sinton",
        "state": "Sinton TX",
        "population": 5661,
        "housing": 2203,
        "land_area": 6005422.0,
        "water_area": 0.0
    },
    "82198": {
        "name": "Sioux Center",
        "state": "Sioux Center IA",
        "population": 8222,
        "housing": 2664,
        "land_area": 14427717.0,
        "water_area": 0.0
    },
    "82225": {
        "name": "Sioux City",
        "state": "Sioux City IA--NE--SD",
        "population": 113066,
        "housing": 44463,
        "land_area": 142539898.0,
        "water_area": 2478312.0
    },
    "82252": {
        "name": "Sioux Falls",
        "state": "Sioux Falls SD",
        "population": 194283,
        "housing": 84183,
        "land_area": 175859060.0,
        "water_area": 1084142.0
    },
    "82306": {
        "name": "Sitka",
        "state": "Sitka AK",
        "population": 7668,
        "housing": 3663,
        "land_area": 17077399.0,
        "water_area": 3677390.0
    },
    "82360": {
        "name": "Skiatook",
        "state": "Skiatook OK",
        "population": 7342,
        "housing": 2998,
        "land_area": 10750011.0,
        "water_area": 89036.0
    },
    "82387": {
        "name": "Skowhegan",
        "state": "Skowhegan ME",
        "population": 4795,
        "housing": 2437,
        "land_area": 7583179.0,
        "water_area": 15925.0
    },
    "82401": {
        "name": "Slatington",
        "state": "Slatington PA",
        "population": 8362,
        "housing": 3751,
        "land_area": 10035771.0,
        "water_area": 48102.0
    },
    "82414": {
        "name": "Slaton",
        "state": "Slaton TX",
        "population": 5678,
        "housing": 2464,
        "land_area": 7125449.0,
        "water_area": 30586.0
    },
    "82468": {
        "name": "Slidell",
        "state": "Slidell LA",
        "population": 91587,
        "housing": 38048,
        "land_area": 136057723.0,
        "water_area": 8337825.0
    },
    "82495": {
        "name": "Slippery Rock",
        "state": "Slippery Rock PA",
        "population": 7226,
        "housing": 2905,
        "land_area": 11725511.0,
        "water_area": 13191.0
    },
    "82522": {
        "name": "Smithfield",
        "state": "Smithfield NC",
        "population": 21921,
        "housing": 9420,
        "land_area": 44054891.0,
        "water_area": 103547.0
    },
    "82549": {
        "name": "Smithfield",
        "state": "Smithfield VA",
        "population": 9725,
        "housing": 4086,
        "land_area": 18439296.0,
        "water_area": 219477.0
    },
    "82603": {
        "name": "Smithville",
        "state": "Smithville TN",
        "population": 4825,
        "housing": 2062,
        "land_area": 10351594.0,
        "water_area": 0.0
    },
    "82641": {
        "name": "Smithville",
        "state": "Smithville MO",
        "population": 9684,
        "housing": 3818,
        "land_area": 17724419.0,
        "water_area": 195667.0
    },
    "82675": {
        "name": "Snoqualmie",
        "state": "Snoqualmie WA",
        "population": 17070,
        "housing": 5841,
        "land_area": 17595050.0,
        "water_area": 196314.0
    },
    "82684": {
        "name": "Snowflake",
        "state": "Snowflake AZ",
        "population": 5342,
        "housing": 1929,
        "land_area": 9831498.0,
        "water_area": 0.0
    },
    "82701": {
        "name": "Snowmass Village",
        "state": "Snowmass Village CO",
        "population": 2392,
        "housing": 2048,
        "land_area": 5796956.0,
        "water_area": 0.0
    },
    "82711": {
        "name": "Snyder",
        "state": "Snyder TX",
        "population": 11547,
        "housing": 4783,
        "land_area": 18641089.0,
        "water_area": 58466.0
    },
    "82738": {
        "name": "Socorro",
        "state": "Socorro NM",
        "population": 8122,
        "housing": 3894,
        "land_area": 14035798.0,
        "water_area": 0.0
    },
    "82819": {
        "name": "Soldotna",
        "state": "Soldotna AK",
        "population": 4646,
        "housing": 2405,
        "land_area": 13119349.0,
        "water_area": 7710.0
    },
    "82846": {
        "name": "Soledad",
        "state": "Soledad CA",
        "population": 18946,
        "housing": 4492,
        "land_area": 6264829.0,
        "water_area": 5945.0
    },
    "82873": {
        "name": "Solvang--Santa Ynez",
        "state": "Solvang--Santa Ynez CA",
        "population": 10295,
        "housing": 4330,
        "land_area": 14194592.0,
        "water_area": 19164.0
    },
    "82900": {
        "name": "Somerset",
        "state": "Somerset KY",
        "population": 30832,
        "housing": 14671,
        "land_area": 100734842.0,
        "water_area": 1291365.0
    },
    "82927": {
        "name": "Somerset",
        "state": "Somerset PA",
        "population": 10098,
        "housing": 4655,
        "land_area": 17897695.0,
        "water_area": 0.0
    },
    "82981": {
        "name": "Somerton",
        "state": "Somerton AZ",
        "population": 13847,
        "housing": 4035,
        "land_area": 5246042.0,
        "water_area": 8514.0
    },
    "83008": {
        "name": "Sonoma",
        "state": "Sonoma CA",
        "population": 31479,
        "housing": 14704,
        "land_area": 36481225.0,
        "water_area": 0.0
    },
    "83073": {
        "name": "Sonora--Twain Harte",
        "state": "Sonora--Twain Harte CA",
        "population": 29013,
        "housing": 16017,
        "land_area": 75345224.0,
        "water_area": 541990.0
    },
    "83080": {
        "name": "Sonterra",
        "state": "Sonterra TX",
        "population": 9024,
        "housing": 3182,
        "land_area": 8032771.0,
        "water_area": 25107.0
    },
    "83116": {
        "name": "South Bend",
        "state": "South Bend IN--MI",
        "population": 278921,
        "housing": 121637,
        "land_area": 383126435.0,
        "water_area": 7982775.0
    },
    "83129": {
        "name": "South Berwick",
        "state": "South Berwick ME--NH",
        "population": 5584,
        "housing": 2319,
        "land_area": 10883580.0,
        "water_area": 442577.0
    },
    "83143": {
        "name": "South Boston",
        "state": "South Boston VA",
        "population": 7413,
        "housing": 3723,
        "land_area": 15997791.0,
        "water_area": 85513.0
    },
    "83160": {
        "name": "Southbridge Town",
        "state": "Southbridge Town MA",
        "population": 20789,
        "housing": 9359,
        "land_area": 29862528.0,
        "water_area": 947681.0
    },
    "83251": {
        "name": "South Haven",
        "state": "South Haven MI",
        "population": 6357,
        "housing": 5509,
        "land_area": 24302580.0,
        "water_area": 283534.0
    },
    "83278": {
        "name": "South Hill",
        "state": "South Hill VA",
        "population": 5076,
        "housing": 2469,
        "land_area": 16117341.0,
        "water_area": 69260.0
    },
    "83305": {
        "name": "South Lake Tahoe",
        "state": "South Lake Tahoe CA--NV",
        "population": 31363,
        "housing": 23573,
        "land_area": 50321995.0,
        "water_area": 1019628.0
    },
    "83332": {
        "name": "South Lyon--Hamburg--Genoa",
        "state": "South Lyon--Hamburg--Genoa MI",
        "population": 145963,
        "housing": 61107,
        "land_area": 309518775.0,
        "water_area": 25906977.0
    },
    "83368": {
        "name": "South Paris",
        "state": "South Paris ME",
        "population": 4371,
        "housing": 2198,
        "land_area": 10521543.0,
        "water_area": 220651.0
    },
    "83377": {
        "name": "South Pittsburg--Bridgeport",
        "state": "South Pittsburg--Bridgeport TN--AL",
        "population": 4687,
        "housing": 2354,
        "land_area": 14306706.0,
        "water_area": 0.0
    },
    "83480": {
        "name": "Sparta",
        "state": "Sparta MI",
        "population": 5630,
        "housing": 2382,
        "land_area": 11121921.0,
        "water_area": 25132.0
    },
    "83494": {
        "name": "Sparta",
        "state": "Sparta TN",
        "population": 5691,
        "housing": 2541,
        "land_area": 18510542.0,
        "water_area": 0.0
    },
    "83521": {
        "name": "Sparta",
        "state": "Sparta WI",
        "population": 10185,
        "housing": 4461,
        "land_area": 13477187.0,
        "water_area": 1191.0
    },
    "83548": {
        "name": "Spartanburg",
        "state": "Spartanburg SC",
        "population": 196943,
        "housing": 82772,
        "land_area": 468778791.0,
        "water_area": 3887516.0
    },
    "83575": {
        "name": "Spearfish",
        "state": "Spearfish SD",
        "population": 13206,
        "housing": 6442,
        "land_area": 20394569.0,
        "water_area": 0.0
    },
    "83629": {
        "name": "Spencer",
        "state": "Spencer IA",
        "population": 10967,
        "housing": 5442,
        "land_area": 15862131.0,
        "water_area": 135132.0
    },
    "83641": {
        "name": "Spencer",
        "state": "Spencer MA",
        "population": 8196,
        "housing": 4097,
        "land_area": 13412859.0,
        "water_area": 1395663.0
    },
    "83683": {
        "name": "Spicer--New London",
        "state": "Spicer--New London MN",
        "population": 3358,
        "housing": 2213,
        "land_area": 11102999.0,
        "water_area": 4773259.0
    },
    "83710": {
        "name": "Spirit Lake",
        "state": "Spirit Lake IA",
        "population": 12956,
        "housing": 10781,
        "land_area": 38471381.0,
        "water_area": 4300231.0
    },
    "83764": {
        "name": "Spokane",
        "state": "Spokane WA",
        "population": 447279,
        "housing": 187977,
        "land_area": 444630903.0,
        "water_area": 5699580.0
    },
    "83818": {
        "name": "Spout Springs",
        "state": "Spout Springs NC",
        "population": 18281,
        "housing": 6078,
        "land_area": 32803516.0,
        "water_area": 1271149.0
    },
    "83899": {
        "name": "Springfield",
        "state": "Springfield IL",
        "population": 159265,
        "housing": 77296,
        "land_area": 211702388.0,
        "water_area": 626358.0
    },
    "83926": {
        "name": "Springfield",
        "state": "Springfield MA--CT",
        "population": 442145,
        "housing": 186392,
        "land_area": 522574324.0,
        "water_area": 17004323.0
    },
    "83953": {
        "name": "Springfield",
        "state": "Springfield MO",
        "population": 282651,
        "housing": 129736,
        "land_area": 347924580.0,
        "water_area": 274823.0
    },
    "83980": {
        "name": "Springfield",
        "state": "Springfield OH",
        "population": 82369,
        "housing": 38075,
        "land_area": 116751735.0,
        "water_area": 171686.0
    },
    "83993": {
        "name": "Springfield",
        "state": "Springfield TN",
        "population": 18430,
        "housing": 7235,
        "land_area": 23208804.0,
        "water_area": 0.0
    },
    "84007": {
        "name": "Springfield",
        "state": "Springfield VT",
        "population": 5140,
        "housing": 2660,
        "land_area": 11267289.0,
        "water_area": 278180.0
    },
    "84024": {
        "name": "Spring Hill",
        "state": "Spring Hill FL",
        "population": 169050,
        "housing": 75458,
        "land_area": 329568649.0,
        "water_area": 12125585.0
    },
    "84034": {
        "name": "Spring Hill",
        "state": "Spring Hill KS",
        "population": 7344,
        "housing": 2691,
        "land_area": 7949165.0,
        "water_area": 23279.0
    },
    "84061": {
        "name": "Springhill",
        "state": "Springhill LA",
        "population": 5931,
        "housing": 3103,
        "land_area": 19120649.0,
        "water_area": 101724.0
    },
    "84088": {
        "name": "Spring Hill",
        "state": "Spring Hill TN",
        "population": 60309,
        "housing": 22018,
        "land_area": 60382092.0,
        "water_area": 44609.0
    },
    "84169": {
        "name": "Stafford Springs",
        "state": "Stafford Springs CT",
        "population": 5107,
        "housing": 2577,
        "land_area": 13399532.0,
        "water_area": 723752.0
    },
    "84304": {
        "name": "Stansbury Park",
        "state": "Stansbury Park UT",
        "population": 12804,
        "housing": 3586,
        "land_area": 8131796.0,
        "water_area": 490768.0
    },
    "84385": {
        "name": "Stanwood",
        "state": "Stanwood WA",
        "population": 7678,
        "housing": 2983,
        "land_area": 7122388.0,
        "water_area": 205.0
    },
    "84429": {
        "name": "Star",
        "state": "Star ID",
        "population": 10673,
        "housing": 3894,
        "land_area": 10041169.0,
        "water_area": 64209.0
    },
    "84439": {
        "name": "Starke",
        "state": "Starke FL",
        "population": 6486,
        "housing": 2690,
        "land_area": 15212254.0,
        "water_area": 0.0
    },
    "84466": {
        "name": "Starkville",
        "state": "Starkville MS",
        "population": 32812,
        "housing": 16188,
        "land_area": 50110444.0,
        "water_area": 293591.0
    },
    "84493": {
        "name": "State College",
        "state": "State College PA",
        "population": 83674,
        "housing": 33591,
        "land_area": 67129603.0,
        "water_area": 4169.0
    },
    "84547": {
        "name": "Statesboro",
        "state": "Statesboro GA",
        "population": 44488,
        "housing": 17978,
        "land_area": 73289398.0,
        "water_area": 1800268.0
    },
    "84574": {
        "name": "Statesville",
        "state": "Statesville NC",
        "population": 39829,
        "housing": 17252,
        "land_area": 97209848.0,
        "water_area": 336087.0
    },
    "84601": {
        "name": "Staunton",
        "state": "Staunton IL",
        "population": 4866,
        "housing": 2309,
        "land_area": 7942764.0,
        "water_area": 86504.0
    },
    "84630": {
        "name": "Staunton--Waynesboro",
        "state": "Staunton--Waynesboro VA",
        "population": 59065,
        "housing": 27498,
        "land_area": 101386449.0,
        "water_area": 456468.0
    },
    "84655": {
        "name": "Stayton",
        "state": "Stayton OR",
        "population": 11122,
        "housing": 4366,
        "land_area": 9928162.0,
        "water_area": 0.0
    },
    "84682": {
        "name": "Steamboat Springs",
        "state": "Steamboat Springs CO",
        "population": 14455,
        "housing": 10532,
        "land_area": 26183837.0,
        "water_area": 33083.0
    },
    "84763": {
        "name": "Stephenville",
        "state": "Stephenville TX",
        "population": 20852,
        "housing": 8447,
        "land_area": 24282134.0,
        "water_area": 11395.0
    },
    "84790": {
        "name": "Sterling",
        "state": "Sterling CO",
        "population": 12278,
        "housing": 5658,
        "land_area": 14029593.0,
        "water_area": 0.0
    },
    "84817": {
        "name": "Sterling",
        "state": "Sterling IL",
        "population": 27602,
        "housing": 13193,
        "land_area": 37845658.0,
        "water_area": 1611579.0
    },
    "84859": {
        "name": "Steubenville--Weirton",
        "state": "Steubenville--Weirton OH--WV--PA",
        "population": 64981,
        "housing": 31580,
        "land_area": 120028973.0,
        "water_area": 3205592.0
    },
    "84871": {
        "name": "Stevens Point",
        "state": "Stevens Point WI",
        "population": 44185,
        "housing": 19540,
        "land_area": 65075370.0,
        "water_area": 2747818.0
    },
    "84905": {
        "name": "Stevensville--Chester--Romancoke",
        "state": "Stevensville--Chester--Romancoke MD",
        "population": 18874,
        "housing": 8258,
        "land_area": 45473982.0,
        "water_area": 4158190.0
    },
    "84952": {
        "name": "Stewartville",
        "state": "Stewartville MN",
        "population": 6635,
        "housing": 2685,
        "land_area": 7030262.0,
        "water_area": 0.0
    },
    "85006": {
        "name": "Stillwater",
        "state": "Stillwater MN--WI",
        "population": 31474,
        "housing": 12975,
        "land_area": 41956816.0,
        "water_area": 1627875.0
    },
    "85033": {
        "name": "Stillwater",
        "state": "Stillwater OK",
        "population": 48237,
        "housing": 22072,
        "land_area": 64912514.0,
        "water_area": 1220194.0
    },
    "85087": {
        "name": "Stockton",
        "state": "Stockton CA",
        "population": 414847,
        "housing": 129251,
        "land_area": 239475820.0,
        "water_area": 5324117.0
    },
    "85114": {
        "name": "Storm Lake",
        "state": "Storm Lake IA",
        "population": 11860,
        "housing": 4117,
        "land_area": 10056589.0,
        "water_area": 0.0
    },
    "85141": {
        "name": "Storrs",
        "state": "Storrs CT",
        "population": 17747,
        "housing": 3510,
        "land_area": 20383065.0,
        "water_area": 68659.0
    },
    "85181": {
        "name": "Stoughton",
        "state": "Stoughton WI",
        "population": 15511,
        "housing": 7015,
        "land_area": 17467795.0,
        "water_area": 203482.0
    },
    "85195": {
        "name": "Strasburg",
        "state": "Strasburg VA",
        "population": 7572,
        "housing": 3294,
        "land_area": 10592876.0,
        "water_area": 101609.0
    },
    "85222": {
        "name": "Streator",
        "state": "Streator IL",
        "population": 16209,
        "housing": 7821,
        "land_area": 21040094.0,
        "water_area": 11681.0
    },
    "85276": {
        "name": "Sturgeon Bay",
        "state": "Sturgeon Bay WI",
        "population": 9429,
        "housing": 5580,
        "land_area": 17922965.0,
        "water_area": 4828995.0
    },
    "85303": {
        "name": "Sturgis",
        "state": "Sturgis MI",
        "population": 11943,
        "housing": 4927,
        "land_area": 16172004.0,
        "water_area": 182.0
    },
    "85330": {
        "name": "Sturgis",
        "state": "Sturgis SD",
        "population": 7076,
        "housing": 3431,
        "land_area": 10448272.0,
        "water_area": 0.0
    },
    "85357": {
        "name": "Stuttgart",
        "state": "Stuttgart AR",
        "population": 8132,
        "housing": 4049,
        "land_area": 15203971.0,
        "water_area": 0.0
    },
    "85384": {
        "name": "Suffolk",
        "state": "Suffolk VA",
        "population": 42480,
        "housing": 17157,
        "land_area": 61692056.0,
        "water_area": 1743260.0
    },
    "85390": {
        "name": "Sugarmill Woods",
        "state": "Sugarmill Woods FL",
        "population": 12948,
        "housing": 7100,
        "land_area": 40706988.0,
        "water_area": 34025.0
    },
    "85411": {
        "name": "Sullivan",
        "state": "Sullivan IL",
        "population": 4414,
        "housing": 2043,
        "land_area": 7177748.0,
        "water_area": 19468.0
    },
    "85438": {
        "name": "Sullivan",
        "state": "Sullivan IN",
        "population": 4874,
        "housing": 2446,
        "land_area": 7024747.0,
        "water_area": 0.0
    },
    "85465": {
        "name": "Sullivan",
        "state": "Sullivan MO",
        "population": 7227,
        "housing": 3384,
        "land_area": 15907125.0,
        "water_area": 0.0
    },
    "85519": {
        "name": "Sulphur",
        "state": "Sulphur OK",
        "population": 4847,
        "housing": 2174,
        "land_area": 8436695.0,
        "water_area": 123948.0
    },
    "85546": {
        "name": "Sulphur Springs",
        "state": "Sulphur Springs TX",
        "population": 14683,
        "housing": 6547,
        "land_area": 28051656.0,
        "water_area": 476384.0
    },
    "85573": {
        "name": "Sultan",
        "state": "Sultan WA",
        "population": 5665,
        "housing": 2105,
        "land_area": 7998106.0,
        "water_area": 14961.0
    },
    "85615": {
        "name": "Summerset",
        "state": "Summerset SD",
        "population": 5325,
        "housing": 2119,
        "land_area": 8813827.0,
        "water_area": 3748.0
    },
    "85654": {
        "name": "Summerville",
        "state": "Summerville GA",
        "population": 10227,
        "housing": 4185,
        "land_area": 21761110.0,
        "water_area": 18581.0
    },
    "85681": {
        "name": "Summit Park",
        "state": "Summit Park UT",
        "population": 7317,
        "housing": 2901,
        "land_area": 9357352.0,
        "water_area": 0.0
    },
    "85708": {
        "name": "Sumter",
        "state": "Sumter SC",
        "population": 68825,
        "housing": 30795,
        "land_area": 149322066.0,
        "water_area": 1220681.0
    },
    "85735": {
        "name": "Sunbury",
        "state": "Sunbury OH",
        "population": 7017,
        "housing": 2636,
        "land_area": 8649658.0,
        "water_area": 2983.0
    },
    "85762": {
        "name": "Sunbury",
        "state": "Sunbury PA",
        "population": 28249,
        "housing": 12642,
        "land_area": 35550407.0,
        "water_area": 196345.0
    },
    "85779": {
        "name": "Sunderland--South Deerfield",
        "state": "Sunderland--South Deerfield MA",
        "population": 5048,
        "housing": 2540,
        "land_area": 17841926.0,
        "water_area": 10620.0
    },
    "85789": {
        "name": "Sunnyside",
        "state": "Sunnyside WA",
        "population": 17140,
        "housing": 5081,
        "land_area": 14395164.0,
        "water_area": 0.0
    },
    "85870": {
        "name": "Susanville",
        "state": "Susanville CA",
        "population": 8995,
        "housing": 4233,
        "land_area": 8232620.0,
        "water_area": 21602.0
    },
    "85978": {
        "name": "Sutherlin",
        "state": "Sutherlin OR",
        "population": 9656,
        "housing": 4221,
        "land_area": 13424955.0,
        "water_area": 131818.0
    },
    "86005": {
        "name": "Swainsboro",
        "state": "Swainsboro GA",
        "population": 7251,
        "housing": 3111,
        "land_area": 20747654.0,
        "water_area": 623037.0
    },
    "86024": {
        "name": "Swansboro--Cedar Point",
        "state": "Swansboro--Cedar Point NC",
        "population": 20542,
        "housing": 10284,
        "land_area": 65972618.0,
        "water_area": 1303029.0
    },
    "86071": {
        "name": "Swatara",
        "state": "Swatara PA",
        "population": 6312,
        "housing": 2535,
        "land_area": 12881594.0,
        "water_area": 6918.0
    },
    "86113": {
        "name": "Sweet Home",
        "state": "Sweet Home OR",
        "population": 10088,
        "housing": 4128,
        "land_area": 13549732.0,
        "water_area": 383187.0
    },
    "86140": {
        "name": "Sweetwater",
        "state": "Sweetwater TN",
        "population": 6468,
        "housing": 2881,
        "land_area": 18345839.0,
        "water_area": 0.0
    },
    "86167": {
        "name": "Sweetwater",
        "state": "Sweetwater TX",
        "population": 10372,
        "housing": 5035,
        "land_area": 19549660.0,
        "water_area": 2421.0
    },
    "86194": {
        "name": "Sylacauga",
        "state": "Sylacauga AL",
        "population": 16980,
        "housing": 8382,
        "land_area": 49864638.0,
        "water_area": 288911.0
    },
    "86202": {
        "name": "Sylva",
        "state": "Sylva NC",
        "population": 5118,
        "housing": 2497,
        "land_area": 18685451.0,
        "water_area": 0.0
    },
    "86210": {
        "name": "Sullivan--Sylvan Beach",
        "state": "Sullivan--Sylvan Beach NY",
        "population": 3251,
        "housing": 2348,
        "land_area": 9981330.0,
        "water_area": 104053.0
    },
    "86248": {
        "name": "Sylvester",
        "state": "Sylvester GA",
        "population": 6146,
        "housing": 2726,
        "land_area": 15165202.0,
        "water_area": 69250.0
    },
    "86275": {
        "name": "Syracuse",
        "state": "Syracuse IN",
        "population": 7393,
        "housing": 4870,
        "land_area": 17368295.0,
        "water_area": 15347180.0
    },
    "86302": {
        "name": "Syracuse",
        "state": "Syracuse NY",
        "population": 413660,
        "housing": 183948,
        "land_area": 467581579.0,
        "water_area": 14852304.0
    },
    "86329": {
        "name": "Taft",
        "state": "Taft CA",
        "population": 15022,
        "housing": 5294,
        "land_area": 9271538.0,
        "water_area": 0.0
    },
    "86369": {
        "name": "Tafuna--Pago Pago",
        "state": "Tafuna--Pago Pago AS",
        "population": 37652,
        "housing": 8742,
        "land_area": 38957303.0,
        "water_area": 1628266.0
    },
    "86383": {
        "name": "Tahlequah",
        "state": "Tahlequah OK",
        "population": 17975,
        "housing": 8286,
        "land_area": 30607480.0,
        "water_area": 247940.0
    },
    "86437": {
        "name": "Talladega",
        "state": "Talladega AL",
        "population": 12609,
        "housing": 5687,
        "land_area": 26158998.0,
        "water_area": 9219.0
    },
    "86464": {
        "name": "Tallahassee",
        "state": "Tallahassee FL",
        "population": 252934,
        "housing": 116829,
        "land_area": 324937638.0,
        "water_area": 10615499.0
    },
    "86518": {
        "name": "Tallulah",
        "state": "Tallulah LA",
        "population": 6988,
        "housing": 3029,
        "land_area": 9703650.0,
        "water_area": 0.0
    },
    "86545": {
        "name": "Tama",
        "state": "Tama IA",
        "population": 5263,
        "housing": 2161,
        "land_area": 7428834.0,
        "water_area": 0.0
    },
    "86572": {
        "name": "Tamaqua",
        "state": "Tamaqua PA",
        "population": 15158,
        "housing": 7661,
        "land_area": 6535228.0,
        "water_area": 0.0
    },
    "86599": {
        "name": "Tampa--St. Petersburg",
        "state": "Tampa--St. Petersburg FL",
        "population": 2783045,
        "housing": 1286258,
        "land_area": 2509539028.0,
        "water_area": 208402499.0
    },
    "86626": {
        "name": "Taneytown",
        "state": "Taneytown MD",
        "population": 7158,
        "housing": 2813,
        "land_area": 5872084.0,
        "water_area": 22017.0
    },
    "86653": {
        "name": "Taos",
        "state": "Taos NM",
        "population": 15665,
        "housing": 8607,
        "land_area": 48837322.0,
        "water_area": 0.0
    },
    "86707": {
        "name": "Tarboro",
        "state": "Tarboro NC",
        "population": 12059,
        "housing": 5743,
        "land_area": 21890078.0,
        "water_area": 44404.0
    },
    "86734": {
        "name": "Taylor",
        "state": "Taylor TX",
        "population": 15147,
        "housing": 5969,
        "land_area": 18191471.0,
        "water_area": 167477.0
    },
    "86788": {
        "name": "Taylorville",
        "state": "Taylorville IL",
        "population": 11525,
        "housing": 5784,
        "land_area": 14140551.0,
        "water_area": 241377.0
    },
    "86815": {
        "name": "Tazewell",
        "state": "Tazewell VA",
        "population": 4666,
        "housing": 2295,
        "land_area": 13417929.0,
        "water_area": 62306.0
    },
    "86830": {
        "name": "Tea",
        "state": "Tea SD",
        "population": 5595,
        "housing": 1967,
        "land_area": 8446579.0,
        "water_area": 0.0
    },
    "86851": {
        "name": "Tecumseh",
        "state": "Tecumseh MI",
        "population": 13684,
        "housing": 5914,
        "land_area": 26229885.0,
        "water_area": 375598.0
    },
    "86869": {
        "name": "Tehachapi--Golden Hills",
        "state": "Tehachapi--Golden Hills CA",
        "population": 17298,
        "housing": 7041,
        "land_area": 20881984.0,
        "water_area": 51184.0
    },
    "86950": {
        "name": "Tell City",
        "state": "Tell City IN--KY",
        "population": 9541,
        "housing": 4754,
        "land_area": 13120762.0,
        "water_area": 2897.0
    },
    "86977": {
        "name": "Tellico Village",
        "state": "Tellico Village TN",
        "population": 7156,
        "housing": 4026,
        "land_area": 17839752.0,
        "water_area": 290322.0
    },
    "86984": {
        "name": "Telluride--Mountain Village",
        "state": "Telluride--Mountain Village CO",
        "population": 4587,
        "housing": 4347,
        "land_area": 17801696.0,
        "water_area": 0.0
    },
    "87004": {
        "name": "Temecula--Murrieta--Menifee",
        "state": "Temecula--Murrieta--Menifee CA",
        "population": 528991,
        "housing": 174148,
        "land_area": 389726572.0,
        "water_area": 2337140.0
    },
    "87058": {
        "name": "Temple",
        "state": "Temple TX",
        "population": 114632,
        "housing": 47995,
        "land_area": 151216886.0,
        "water_area": 542019.0
    },
    "87139": {
        "name": "Terre Haute",
        "state": "Terre Haute IN",
        "population": 79862,
        "housing": 35852,
        "land_area": 121301131.0,
        "water_area": 1009519.0
    },
    "87166": {
        "name": "Terrell",
        "state": "Terrell TX",
        "population": 16581,
        "housing": 6180,
        "land_area": 31864140.0,
        "water_area": 11575.0
    },
    "87193": {
        "name": "Texarkana",
        "state": "Texarkana TX--AR",
        "population": 78744,
        "housing": 35054,
        "land_area": 172594807.0,
        "water_area": 988669.0
    },
    "87263": {
        "name": "The Dalles",
        "state": "The Dalles OR",
        "population": 17398,
        "housing": 7216,
        "land_area": 19859547.0,
        "water_area": 4751.0
    },
    "87269": {
        "name": "The Pinery",
        "state": "The Pinery CO",
        "population": 14662,
        "housing": 5025,
        "land_area": 23873087.0,
        "water_area": 0.0
    },
    "87285": {
        "name": "The Villages--Lady Lake",
        "state": "The Villages--Lady Lake FL",
        "population": 161736,
        "housing": 98242,
        "land_area": 255167012.0,
        "water_area": 10004321.0
    },
    "87300": {
        "name": "The Woodlands--Conroe",
        "state": "The Woodlands--Conroe TX",
        "population": 402454,
        "housing": 153788,
        "land_area": 567571240.0,
        "water_area": 3890375.0
    },
    "87355": {
        "name": "Thief River Falls",
        "state": "Thief River Falls MN",
        "population": 8892,
        "housing": 4535,
        "land_area": 13610404.0,
        "water_area": 398494.0
    },
    "87382": {
        "name": "Thomaston",
        "state": "Thomaston GA",
        "population": 14765,
        "housing": 6679,
        "land_area": 26843428.0,
        "water_area": 92730.0
    },
    "87436": {
        "name": "Thomasville",
        "state": "Thomasville GA",
        "population": 25231,
        "housing": 11627,
        "land_area": 57726939.0,
        "water_area": 750892.0
    },
    "87463": {
        "name": "Thomson",
        "state": "Thomson GA",
        "population": 8788,
        "housing": 3892,
        "land_area": 21459144.0,
        "water_area": 307727.0
    },
    "87490": {
        "name": "Thousand Oaks",
        "state": "Thousand Oaks CA",
        "population": 213986,
        "housing": 79133,
        "land_area": 207708756.0,
        "water_area": 2666410.0
    },
    "87517": {
        "name": "Three Rivers",
        "state": "Three Rivers MI",
        "population": 10166,
        "housing": 4365,
        "land_area": 22468471.0,
        "water_area": 1748450.0
    },
    "87544": {
        "name": "Thurmont",
        "state": "Thurmont MD",
        "population": 6789,
        "housing": 2880,
        "land_area": 9829467.0,
        "water_area": 15610.0
    },
    "87598": {
        "name": "Tiffin",
        "state": "Tiffin OH",
        "population": 20284,
        "housing": 9154,
        "land_area": 23837894.0,
        "water_area": 357480.0
    },
    "87625": {
        "name": "Tifton",
        "state": "Tifton GA",
        "population": 24580,
        "housing": 10511,
        "land_area": 47556390.0,
        "water_area": 804652.0
    },
    "87652": {
        "name": "Tillamook",
        "state": "Tillamook OR",
        "population": 6166,
        "housing": 2712,
        "land_area": 6865964.0,
        "water_area": 0.0
    },
    "87659": {
        "name": "Tiltonsville--Brilliant",
        "state": "Tiltonsville--Brilliant OH",
        "population": 4115,
        "housing": 2163,
        "land_area": 8367924.0,
        "water_area": 51902.0
    },
    "87662": {
        "name": "Tippecanoe",
        "state": "Tippecanoe IN",
        "population": 3713,
        "housing": 3109,
        "land_area": 10708924.0,
        "water_area": 6118051.0
    },
    "87679": {
        "name": "Tipton",
        "state": "Tipton IN",
        "population": 5668,
        "housing": 2627,
        "land_area": 6274211.0,
        "water_area": 0.0
    },
    "87787": {
        "name": "Titusville",
        "state": "Titusville FL",
        "population": 62459,
        "housing": 29966,
        "land_area": 103484130.0,
        "water_area": 306880.0
    },
    "87814": {
        "name": "Titusville",
        "state": "Titusville PA",
        "population": 5219,
        "housing": 2569,
        "land_area": 5729929.0,
        "water_area": 0.0
    },
    "87841": {
        "name": "Toccoa",
        "state": "Toccoa GA",
        "population": 11807,
        "housing": 5219,
        "land_area": 32619202.0,
        "water_area": 83524.0
    },
    "87868": {
        "name": "Toledo",
        "state": "Toledo OH--MI",
        "population": 497952,
        "housing": 229911,
        "land_area": 623448818.0,
        "water_area": 20293135.0
    },
    "87922": {
        "name": "Tomah",
        "state": "Tomah WI",
        "population": 9818,
        "housing": 4560,
        "land_area": 18037350.0,
        "water_area": 53724.0
    },
    "88003": {
        "name": "Tonganoxie",
        "state": "Tonganoxie KS",
        "population": 5489,
        "housing": 2154,
        "land_area": 6996888.0,
        "water_area": 27094.0
    },
    "88057": {
        "name": "Tooele",
        "state": "Tooele UT",
        "population": 34892,
        "housing": 11507,
        "land_area": 29902374.0,
        "water_area": 0.0
    },
    "88084": {
        "name": "Topeka",
        "state": "Topeka KS",
        "population": 148956,
        "housing": 68882,
        "land_area": 218179861.0,
        "water_area": 3524378.0
    },
    "88111": {
        "name": "Toppenish",
        "state": "Toppenish WA",
        "population": 10057,
        "housing": 2823,
        "land_area": 7984671.0,
        "water_area": 0.0
    },
    "88192": {
        "name": "Torrington",
        "state": "Torrington CT",
        "population": 35212,
        "housing": 17184,
        "land_area": 56356547.0,
        "water_area": 1680940.0
    },
    "88219": {
        "name": "Torrington",
        "state": "Torrington WY",
        "population": 6436,
        "housing": 3187,
        "land_area": 9343047.0,
        "water_area": 570.0
    },
    "88246": {
        "name": "Towanda",
        "state": "Towanda PA",
        "population": 4029,
        "housing": 2069,
        "land_area": 7601686.0,
        "water_area": 53212.0
    },
    "88280": {
        "name": "Tracy--Mountain House",
        "state": "Tracy--Mountain House CA",
        "population": 120912,
        "housing": 36775,
        "land_area": 70071736.0,
        "water_area": 351998.0
    },
    "88300": {
        "name": "Traverse City--Garfield",
        "state": "Traverse City--Garfield MI",
        "population": 56890,
        "housing": 28936,
        "land_area": 135757514.0,
        "water_area": 7973273.0
    },
    "88327": {
        "name": "Treasure Lake",
        "state": "Treasure Lake PA",
        "population": 4677,
        "housing": 2735,
        "land_area": 8598496.0,
        "water_area": 1971539.0
    },
    "88354": {
        "name": "Tremonton",
        "state": "Tremonton UT",
        "population": 11898,
        "housing": 3904,
        "land_area": 17479565.0,
        "water_area": 0.0
    },
    "88435": {
        "name": "Trenton",
        "state": "Trenton MO",
        "population": 5426,
        "housing": 2859,
        "land_area": 11176057.0,
        "water_area": 676365.0
    },
    "88462": {
        "name": "Trenton",
        "state": "Trenton NJ",
        "population": 370422,
        "housing": 144898,
        "land_area": 344807881.0,
        "water_area": 6568437.0
    },
    "88504": {
        "name": "Tri-City--Myrtle Creek",
        "state": "Tri-City--Myrtle Creek OR",
        "population": 8656,
        "housing": 3769,
        "land_area": 12987435.0,
        "water_area": 516.0
    },
    "88516": {
        "name": "Trinidad",
        "state": "Trinidad CO",
        "population": 8323,
        "housing": 4362,
        "land_area": 12309618.0,
        "water_area": 50.0
    },
    "88543": {
        "name": "Troy",
        "state": "Troy AL",
        "population": 14466,
        "housing": 6857,
        "land_area": 25412899.0,
        "water_area": 14983.0
    },
    "88570": {
        "name": "Troy",
        "state": "Troy MO",
        "population": 16669,
        "housing": 6408,
        "land_area": 23310552.0,
        "water_area": 120944.0
    },
    "88612": {
        "name": "Troy",
        "state": "Troy OH",
        "population": 43259,
        "housing": 18944,
        "land_area": 55895927.0,
        "water_area": 629205.0
    },
    "88624": {
        "name": "Truckee",
        "state": "Truckee CA",
        "population": 12756,
        "housing": 11624,
        "land_area": 50131615.0,
        "water_area": 3380566.0
    },
    "88651": {
        "name": "Trumann",
        "state": "Trumann AR",
        "population": 7233,
        "housing": 3143,
        "land_area": 11073664.0,
        "water_area": 0.0
    },
    "88678": {
        "name": "Truth or Consequences",
        "state": "Truth or Consequences NM",
        "population": 7713,
        "housing": 5603,
        "land_area": 21340831.0,
        "water_area": 9874.0
    },
    "88705": {
        "name": "Tuba City",
        "state": "Tuba City AZ",
        "population": 7942,
        "housing": 2546,
        "land_area": 14399283.0,
        "water_area": 0.0
    },
    "88732": {
        "name": "Tucson",
        "state": "Tucson AZ",
        "population": 875441,
        "housing": 398383,
        "land_area": 925530003.0,
        "water_area": 1434827.0
    },
    "88813": {
        "name": "Tucumcari",
        "state": "Tucumcari NM",
        "population": 5217,
        "housing": 2872,
        "land_area": 9916255.0,
        "water_area": 0.0
    },
    "88840": {
        "name": "Tulare",
        "state": "Tulare CA",
        "population": 70628,
        "housing": 21714,
        "land_area": 45879904.0,
        "water_area": 0.0
    },
    "88921": {
        "name": "Tullahoma",
        "state": "Tullahoma TN",
        "population": 19297,
        "housing": 8558,
        "land_area": 38296270.0,
        "water_area": 0.0
    },
    "88948": {
        "name": "Tulsa",
        "state": "Tulsa OK",
        "population": 722810,
        "housing": 314048,
        "land_area": 876251014.0,
        "water_area": 14207524.0
    },
    "89029": {
        "name": "Tupelo",
        "state": "Tupelo MS",
        "population": 40233,
        "housing": 18370,
        "land_area": 112126988.0,
        "water_area": 369035.0
    },
    "89056": {
        "name": "Tupper Lake",
        "state": "Tupper Lake NY",
        "population": 3683,
        "housing": 2056,
        "land_area": 5972343.0,
        "water_area": 28436.0
    },
    "89083": {
        "name": "Turlock",
        "state": "Turlock CA",
        "population": 79203,
        "housing": 27325,
        "land_area": 43754342.0,
        "water_area": 69528.0
    },
    "89110": {
        "name": "Tuscaloosa",
        "state": "Tuscaloosa AL",
        "population": 156450,
        "housing": 71635,
        "land_area": 228331801.0,
        "water_area": 3907120.0
    },
    "89137": {
        "name": "Tuscola",
        "state": "Tuscola IL",
        "population": 4942,
        "housing": 2344,
        "land_area": 7823637.0,
        "water_area": 23450.0
    },
    "89164": {
        "name": "Tuskegee",
        "state": "Tuskegee AL",
        "population": 9003,
        "housing": 4332,
        "land_area": 21229049.0,
        "water_area": 419231.0
    },
    "89191": {
        "name": "Twentynine Palms",
        "state": "Twentynine Palms CA",
        "population": 12881,
        "housing": 6113,
        "land_area": 17659753.0,
        "water_area": 0.0
    },
    "89218": {
        "name": "Twentynine Palms North",
        "state": "Twentynine Palms North CA",
        "population": 11665,
        "housing": 1782,
        "land_area": 7183065.0,
        "water_area": 0.0
    },
    "89245": {
        "name": "Twin Falls",
        "state": "Twin Falls ID",
        "population": 58808,
        "housing": 22844,
        "land_area": 57151291.0,
        "water_area": 281269.0
    },
    "89261": {
        "name": "Twin Lakes",
        "state": "Twin Lakes WI--IL",
        "population": 12603,
        "housing": 6404,
        "land_area": 24487235.0,
        "water_area": 3547644.0
    },
    "89299": {
        "name": "Tybee Island",
        "state": "Tybee Island GA",
        "population": 3316,
        "housing": 3121,
        "land_area": 7261567.0,
        "water_area": 218061.0
    },
    "89326": {
        "name": "Tyler",
        "state": "Tyler TX",
        "population": 131028,
        "housing": 55860,
        "land_area": 211165770.0,
        "water_area": 2199378.0
    },
    "89353": {
        "name": "Tyrone",
        "state": "Tyrone PA",
        "population": 10442,
        "housing": 4719,
        "land_area": 15685979.0,
        "water_area": 9602.0
    },
    "89380": {
        "name": "Ukiah",
        "state": "Ukiah CA",
        "population": 28987,
        "housing": 11540,
        "land_area": 34186138.0,
        "water_area": 152631.0
    },
    "89407": {
        "name": "Ulysses",
        "state": "Ulysses KS",
        "population": 5865,
        "housing": 2314,
        "land_area": 7210802.0,
        "water_area": 4197.0
    },
    "89461": {
        "name": "Union",
        "state": "Union MO",
        "population": 12019,
        "housing": 4961,
        "land_area": 19440318.0,
        "water_area": 37.0
    },
    "89488": {
        "name": "Union",
        "state": "Union SC",
        "population": 9729,
        "housing": 4820,
        "land_area": 23725247.0,
        "water_area": 8766.0
    },
    "89515": {
        "name": "Union City",
        "state": "Union City IN--OH",
        "population": 5079,
        "housing": 2465,
        "land_area": 6694035.0,
        "water_area": 5940.0
    },
    "89569": {
        "name": "Union City",
        "state": "Union City TN",
        "population": 10605,
        "housing": 4878,
        "land_area": 21136333.0,
        "water_area": 0.0
    },
    "89596": {
        "name": "Union Grove",
        "state": "Union Grove WI",
        "population": 5899,
        "housing": 2110,
        "land_area": 9507194.0,
        "water_area": 6814.0
    },
    "89640": {
        "name": "Uniontown",
        "state": "Uniontown PA",
        "population": 32560,
        "housing": 15868,
        "land_area": 69830527.0,
        "water_area": 23721.0
    },
    "89731": {
        "name": "Upper Sandusky",
        "state": "Upper Sandusky OH",
        "population": 6628,
        "housing": 3139,
        "land_area": 12405348.0,
        "water_area": 0.0
    },
    "89742": {
        "name": "Urbana",
        "state": "Urbana MD",
        "population": 12966,
        "housing": 4093,
        "land_area": 9045464.0,
        "water_area": 32860.0
    },
    "89758": {
        "name": "Urbana",
        "state": "Urbana OH",
        "population": 11122,
        "housing": 5477,
        "land_area": 16751897.0,
        "water_area": 0.0
    },
    "89785": {
        "name": "Utica",
        "state": "Utica NY",
        "population": 119059,
        "housing": 52462,
        "land_area": 134794796.0,
        "water_area": 496051.0
    },
    "89812": {
        "name": "Utuado",
        "state": "Utuado PR",
        "population": 13008,
        "housing": 6234,
        "land_area": 29225059.0,
        "water_area": 162301.0
    },
    "89839": {
        "name": "Uvalde",
        "state": "Uvalde TX",
        "population": 15926,
        "housing": 6182,
        "land_area": 19037171.0,
        "water_area": 22341.0
    },
    "89866": {
        "name": "Vacaville",
        "state": "Vacaville CA",
        "population": 101027,
        "housing": 35582,
        "land_area": 55916790.0,
        "water_area": 88480.0
    },
    "89910": {
        "name": "Vail",
        "state": "Vail AZ",
        "population": 12835,
        "housing": 4690,
        "land_area": 14923233.0,
        "water_area": 0.0
    },
    "89920": {
        "name": "Vail",
        "state": "Vail CO",
        "population": 6080,
        "housing": 8070,
        "land_area": 13097377.0,
        "water_area": 170358.0
    },
    "89974": {
        "name": "Valdosta",
        "state": "Valdosta GA",
        "population": 76769,
        "housing": 32392,
        "land_area": 106937295.0,
        "water_area": 1522105.0
    },
    "90028": {
        "name": "Vallejo",
        "state": "Vallejo CA",
        "population": 175132,
        "housing": 63277,
        "land_area": 102667537.0,
        "water_area": 1619645.0
    },
    "90055": {
        "name": "Valley City",
        "state": "Valley City ND",
        "population": 6547,
        "housing": 3386,
        "land_area": 9638508.0,
        "water_area": 0.0
    },
    "90082": {
        "name": "Valley--Lanett",
        "state": "Valley--Lanett AL--GA",
        "population": 20466,
        "housing": 9528,
        "land_area": 46871400.0,
        "water_area": 362796.0
    },
    "90095": {
        "name": "Valparaiso--Shorewood Forest",
        "state": "Valparaiso--Shorewood Forest IN",
        "population": 51867,
        "housing": 22154,
        "land_area": 87117095.0,
        "water_area": 2303954.0
    },
    "90136": {
        "name": "Vandalia",
        "state": "Vandalia IL",
        "population": 8110,
        "housing": 2927,
        "land_area": 16761482.0,
        "water_area": 5741.0
    },
    "90217": {
        "name": "Van Wert",
        "state": "Van Wert OH",
        "population": 11069,
        "housing": 5072,
        "land_area": 15940790.0,
        "water_area": 75065.0
    },
    "90271": {
        "name": "Veneta",
        "state": "Veneta OR",
        "population": 6987,
        "housing": 2693,
        "land_area": 12046305.0,
        "water_area": 0.0
    },
    "90325": {
        "name": "Vermillion",
        "state": "Vermillion SD",
        "population": 11659,
        "housing": 4626,
        "land_area": 10327477.0,
        "water_area": 0.0
    },
    "90352": {
        "name": "Vernal",
        "state": "Vernal UT",
        "population": 19620,
        "housing": 7748,
        "land_area": 40552602.0,
        "water_area": 0.0
    },
    "90379": {
        "name": "Vernon",
        "state": "Vernon TX",
        "population": 9524,
        "housing": 4516,
        "land_area": 15002986.0,
        "water_area": 23262.0
    },
    "90406": {
        "name": "Vero Beach--Sebastian",
        "state": "Vero Beach--Sebastian FL",
        "population": 174292,
        "housing": 95595,
        "land_area": 274802450.0,
        "water_area": 30798825.0
    },
    "90433": {
        "name": "Versailles",
        "state": "Versailles KY",
        "population": 16855,
        "housing": 7132,
        "land_area": 20472977.0,
        "water_area": 101107.0
    },
    "90487": {
        "name": "Vicksburg",
        "state": "Vicksburg MS--LA",
        "population": 25888,
        "housing": 12760,
        "land_area": 62875577.0,
        "water_area": 642133.0
    },
    "90514": {
        "name": "Victoria",
        "state": "Victoria TX",
        "population": 65986,
        "housing": 28572,
        "land_area": 87023661.0,
        "water_area": 0.0
    },
    "90541": {
        "name": "Victorville--Hesperia--Apple Valley",
        "state": "Victorville--Hesperia--Apple Valley CA",
        "population": 355816,
        "housing": 110834,
        "land_area": 341294239.0,
        "water_area": 829804.0
    },
    "90568": {
        "name": "Vidalia",
        "state": "Vidalia GA",
        "population": 13709,
        "housing": 6238,
        "land_area": 39796102.0,
        "water_area": 788908.0
    },
    "90622": {
        "name": "Vieques (Vieques Municipio)",
        "state": "Vieques (Vieques Municipio) PR",
        "population": 6530,
        "housing": 4181,
        "land_area": 14364742.0,
        "water_area": 313.0
    },
    "90631": {
        "name": "Village of Four Seasons",
        "state": "Village of Four Seasons MO",
        "population": 7489,
        "housing": 9467,
        "land_area": 49484108.0,
        "water_area": 3740690.0
    },
    "90640": {
        "name": "Village of Oak Creek (Big Park)",
        "state": "Village of Oak Creek (Big Park) AZ",
        "population": 6128,
        "housing": 4354,
        "land_area": 13614844.0,
        "water_area": 0.0
    },
    "90649": {
        "name": "Villa Rica",
        "state": "Villa Rica GA",
        "population": 23202,
        "housing": 8744,
        "land_area": 49794820.0,
        "water_area": 481871.0
    },
    "90676": {
        "name": "Ville Platte",
        "state": "Ville Platte LA",
        "population": 8097,
        "housing": 3992,
        "land_area": 12710511.0,
        "water_area": 1674.0
    },
    "90703": {
        "name": "Vincennes",
        "state": "Vincennes IN",
        "population": 19800,
        "housing": 9176,
        "land_area": 30630789.0,
        "water_area": 549810.0
    },
    "90730": {
        "name": "Vineland",
        "state": "Vineland NJ",
        "population": 87226,
        "housing": 35033,
        "land_area": 147605174.0,
        "water_area": 796284.0
    },
    "90757": {
        "name": "Vineyard Haven--Edgartown--Oak Bluffs",
        "state": "Vineyard Haven--Edgartown--Oak Bluffs MA",
        "population": 14064,
        "housing": 11427,
        "land_area": 42868150.0,
        "water_area": 3589032.0
    },
    "90784": {
        "name": "Vinita",
        "state": "Vinita OK",
        "population": 5068,
        "housing": 2480,
        "land_area": 12241202.0,
        "water_area": 47476.0
    },
    "90811": {
        "name": "Vinton",
        "state": "Vinton IA",
        "population": 4780,
        "housing": 2187,
        "land_area": 7209751.0,
        "water_area": 0.0
    },
    "90865": {
        "name": "Virginia",
        "state": "Virginia MN",
        "population": 12724,
        "housing": 6916,
        "land_area": 17293746.0,
        "water_area": 362239.0
    },
    "90892": {
        "name": "Virginia Beach--Norfolk",
        "state": "Virginia Beach--Norfolk VA",
        "population": 1451578,
        "housing": 609066,
        "land_area": 1247532344.0,
        "water_area": 197576464.0
    },
    "90919": {
        "name": "Viroqua",
        "state": "Viroqua WI",
        "population": 3987,
        "housing": 2063,
        "land_area": 5171920.0,
        "water_area": 0.0
    },
    "90946": {
        "name": "Visalia",
        "state": "Visalia CA",
        "population": 160578,
        "housing": 53821,
        "land_area": 97653500.0,
        "water_area": 39512.0
    },
    "91000": {
        "name": "Wabash",
        "state": "Wabash IN",
        "population": 10254,
        "housing": 4965,
        "land_area": 15421992.0,
        "water_area": 146473.0
    },
    "91027": {
        "name": "Waco",
        "state": "Waco TX",
        "population": 192844,
        "housing": 79136,
        "land_area": 232786010.0,
        "water_area": 1417901.0
    },
    "91054": {
        "name": "Waconia",
        "state": "Waconia MN",
        "population": 13048,
        "housing": 4835,
        "land_area": 11854393.0,
        "water_area": 436643.0
    },
    "91081": {
        "name": "Wadena",
        "state": "Wadena MN",
        "population": 4110,
        "housing": 2043,
        "land_area": 6782651.0,
        "water_area": 7557.0
    },
    "91108": {
        "name": "Wadesboro",
        "state": "Wadesboro NC",
        "population": 4903,
        "housing": 2360,
        "land_area": 15464679.0,
        "water_area": 12444.0
    },
    "91135": {
        "name": "Wagoner",
        "state": "Wagoner OK",
        "population": 7470,
        "housing": 3325,
        "land_area": 14016097.0,
        "water_area": 0.0
    },
    "91162": {
        "name": "Wahoo",
        "state": "Wahoo NE",
        "population": 4782,
        "housing": 2000,
        "land_area": 5777357.0,
        "water_area": 2310.0
    },
    "91189": {
        "name": "Wahpeton",
        "state": "Wahpeton ND--MN",
        "population": 11290,
        "housing": 5193,
        "land_area": 18493073.0,
        "water_area": 0.0
    },
    "91216": {
        "name": "Waikoloa Village",
        "state": "Waikoloa Village HI",
        "population": 6824,
        "housing": 3222,
        "land_area": 15365371.0,
        "water_area": 0.0
    },
    "91252": {
        "name": "Walden",
        "state": "Walden NY",
        "population": 15784,
        "housing": 6127,
        "land_area": 29945842.0,
        "water_area": 237638.0
    },
    "91261": {
        "name": "Waldorf",
        "state": "Waldorf MD",
        "population": 118601,
        "housing": 42930,
        "land_area": 153719966.0,
        "water_area": 594805.0
    },
    "91270": {
        "name": "Waldport",
        "state": "Waldport OR",
        "population": 5394,
        "housing": 4224,
        "land_area": 14334951.0,
        "water_area": 97234.0
    },
    "91312": {
        "name": "Wales",
        "state": "Wales WI",
        "population": 5364,
        "housing": 2126,
        "land_area": 15560935.0,
        "water_area": 39145.0
    },
    "91324": {
        "name": "Walhalla",
        "state": "Walhalla SC",
        "population": 5392,
        "housing": 2415,
        "land_area": 14765149.0,
        "water_area": 161863.0
    },
    "91405": {
        "name": "Walla Walla",
        "state": "Walla Walla WA--OR",
        "population": 50013,
        "housing": 20109,
        "land_area": 61804396.0,
        "water_area": 86365.0
    },
    "91459": {
        "name": "Walnut Ridge",
        "state": "Walnut Ridge AR",
        "population": 6540,
        "housing": 2986,
        "land_area": 13385089.0,
        "water_area": 0.0
    },
    "91513": {
        "name": "Walterboro",
        "state": "Walterboro SC",
        "population": 9229,
        "housing": 4246,
        "land_area": 21077235.0,
        "water_area": 126488.0
    },
    "91648": {
        "name": "Wamego",
        "state": "Wamego KS",
        "population": 4899,
        "housing": 2079,
        "land_area": 6072775.0,
        "water_area": 5759.0
    },
    "91675": {
        "name": "Wapakoneta",
        "state": "Wapakoneta OH",
        "population": 10849,
        "housing": 4774,
        "land_area": 14431993.0,
        "water_area": 67642.0
    },
    "91702": {
        "name": "Wapato",
        "state": "Wapato WA",
        "population": 7071,
        "housing": 1995,
        "land_area": 6771998.0,
        "water_area": 0.0
    },
    "91756": {
        "name": "Ware",
        "state": "Ware MA",
        "population": 5662,
        "housing": 2828,
        "land_area": 8257526.0,
        "water_area": 0.0
    },
    "91783": {
        "name": "Warner Robins",
        "state": "Warner Robins GA",
        "population": 141132,
        "housing": 58015,
        "land_area": 228733512.0,
        "water_area": 1682762.0
    },
    "91810": {
        "name": "Warren",
        "state": "Warren AR",
        "population": 5278,
        "housing": 2590,
        "land_area": 16484178.0,
        "water_area": 64234.0
    },
    "91837": {
        "name": "Warren",
        "state": "Warren PA",
        "population": 14294,
        "housing": 7159,
        "land_area": 20193511.0,
        "water_area": 1123271.0
    },
    "91864": {
        "name": "Warrensburg",
        "state": "Warrensburg MO",
        "population": 19934,
        "housing": 8557,
        "land_area": 21954000.0,
        "water_area": 47219.0
    },
    "91918": {
        "name": "Warrenton",
        "state": "Warrenton MO",
        "population": 9398,
        "housing": 4018,
        "land_area": 15625477.0,
        "water_area": 212185.0
    },
    "91945": {
        "name": "Warrenton--New Baltimore",
        "state": "Warrenton--New Baltimore VA",
        "population": 24437,
        "housing": 8916,
        "land_area": 44509298.0,
        "water_area": 445049.0
    },
    "91999": {
        "name": "Warsaw",
        "state": "Warsaw IN",
        "population": 29904,
        "housing": 12541,
        "land_area": 61353313.0,
        "water_area": 5279867.0
    },
    "92134": {
        "name": "Warwick",
        "state": "Warwick NY",
        "population": 7084,
        "housing": 3394,
        "land_area": 7557110.0,
        "water_area": 11458.0
    },
    "92161": {
        "name": "Wasco",
        "state": "Wasco CA",
        "population": 22235,
        "housing": 6271,
        "land_area": 8930377.0,
        "water_area": 0.0
    },
    "92215": {
        "name": "Waseca",
        "state": "Waseca MN",
        "population": 9211,
        "housing": 3808,
        "land_area": 9847945.0,
        "water_area": 513017.0
    },
    "92242": {
        "name": "Washington--Arlington",
        "state": "Washington--Arlington DC--VA--MD",
        "population": 5174759,
        "housing": 2042623,
        "land_area": 3352762591.0,
        "water_area": 67538846.0
    },
    "92296": {
        "name": "Washington",
        "state": "Washington IN",
        "population": 12920,
        "housing": 5559,
        "land_area": 17574117.0,
        "water_area": 114004.0
    },
    "92323": {
        "name": "Washington",
        "state": "Washington IA",
        "population": 6846,
        "housing": 3035,
        "land_area": 8551196.0,
        "water_area": 0.0
    },
    "92350": {
        "name": "Washington",
        "state": "Washington MO",
        "population": 14616,
        "housing": 6620,
        "land_area": 20397190.0,
        "water_area": 81188.0
    },
    "92377": {
        "name": "Washington",
        "state": "Washington NJ",
        "population": 10138,
        "housing": 4308,
        "land_area": 10693396.0,
        "water_area": 15101.0
    },
    "92404": {
        "name": "Washington",
        "state": "Washington NC",
        "population": 16509,
        "housing": 8268,
        "land_area": 43234518.0,
        "water_area": 151299.0
    },
    "92431": {
        "name": "Washington Court House",
        "state": "Washington Court House OH",
        "population": 15029,
        "housing": 6920,
        "land_area": 19025939.0,
        "water_area": 154813.0
    },
    "92459": {
        "name": "Wasilla--Knik-Fairview--North Lakes",
        "state": "Wasilla--Knik-Fairview--North Lakes AK",
        "population": 53444,
        "housing": 20504,
        "land_area": 149262656.0,
        "water_area": 6247204.0
    },
    "92485": {
        "name": "Waterbury",
        "state": "Waterbury CT",
        "population": 199317,
        "housing": 83605,
        "land_area": 239413612.0,
        "water_area": 3918357.0
    },
    "92539": {
        "name": "Waterford",
        "state": "Waterford CA",
        "population": 9746,
        "housing": 2922,
        "land_area": 5090777.0,
        "water_area": 8527.0
    },
    "92566": {
        "name": "Waterloo",
        "state": "Waterloo IL",
        "population": 9933,
        "housing": 4305,
        "land_area": 14015212.0,
        "water_area": 84206.0
    },
    "92593": {
        "name": "Waterloo",
        "state": "Waterloo IA",
        "population": 114139,
        "housing": 51470,
        "land_area": 165006180.0,
        "water_area": 5752045.0
    },
    "92674": {
        "name": "Watertown",
        "state": "Watertown NY",
        "population": 51832,
        "housing": 23084,
        "land_area": 82847638.0,
        "water_area": 1388357.0
    },
    "92701": {
        "name": "Watertown",
        "state": "Watertown SD",
        "population": 20643,
        "housing": 9805,
        "land_area": 32048929.0,
        "water_area": 32904.0
    },
    "92728": {
        "name": "Watertown",
        "state": "Watertown WI",
        "population": 22712,
        "housing": 9767,
        "land_area": 27071935.0,
        "water_area": 1014965.0
    },
    "92782": {
        "name": "Waterville",
        "state": "Waterville ME",
        "population": 25529,
        "housing": 12264,
        "land_area": 43772874.0,
        "water_area": 3276778.0
    },
    "92797": {
        "name": "Watford City",
        "state": "Watford City ND",
        "population": 6687,
        "housing": 3796,
        "land_area": 21527946.0,
        "water_area": 206569.0
    },
    "92863": {
        "name": "Watseka",
        "state": "Watseka IL",
        "population": 4671,
        "housing": 2444,
        "land_area": 7423230.0,
        "water_area": 0.0
    },
    "92890": {
        "name": "Watsonville",
        "state": "Watsonville CA",
        "population": 68668,
        "housing": 19042,
        "land_area": 38071821.0,
        "water_area": 858576.0
    },
    "92917": {
        "name": "Wauchula",
        "state": "Wauchula FL",
        "population": 9790,
        "housing": 3931,
        "land_area": 16169748.0,
        "water_area": 0.0
    },
    "92971": {
        "name": "Waupaca",
        "state": "Waupaca WI",
        "population": 8293,
        "housing": 4379,
        "land_area": 22922760.0,
        "water_area": 3773001.0
    },
    "92998": {
        "name": "Waupun",
        "state": "Waupun WI",
        "population": 11673,
        "housing": 3889,
        "land_area": 11376418.0,
        "water_area": 167683.0
    },
    "93025": {
        "name": "Wausau",
        "state": "Wausau WI",
        "population": 77429,
        "housing": 34753,
        "land_area": 126229977.0,
        "water_area": 8674389.0
    },
    "93052": {
        "name": "Wauseon",
        "state": "Wauseon OH",
        "population": 7623,
        "housing": 3204,
        "land_area": 13129525.0,
        "water_area": 76844.0
    },
    "93079": {
        "name": "Waverly",
        "state": "Waverly IA",
        "population": 9159,
        "housing": 3661,
        "land_area": 14461915.0,
        "water_area": 576202.0
    },
    "93133": {
        "name": "Waverly",
        "state": "Waverly OH",
        "population": 4969,
        "housing": 2683,
        "land_area": 10448711.0,
        "water_area": 1460048.0
    },
    "93187": {
        "name": "Waycross",
        "state": "Waycross GA",
        "population": 24985,
        "housing": 11144,
        "land_area": 64613896.0,
        "water_area": 455626.0
    },
    "93214": {
        "name": "Wayland",
        "state": "Wayland MI",
        "population": 4957,
        "housing": 2007,
        "land_area": 8770321.0,
        "water_area": 117001.0
    },
    "93241": {
        "name": "Wayne",
        "state": "Wayne NE",
        "population": 5980,
        "housing": 2325,
        "land_area": 7998635.0,
        "water_area": 114652.0
    },
    "93268": {
        "name": "Waynesboro",
        "state": "Waynesboro GA",
        "population": 6103,
        "housing": 2701,
        "land_area": 15401708.0,
        "water_area": 147851.0
    },
    "93322": {
        "name": "Waynesboro",
        "state": "Waynesboro PA--MD",
        "population": 22267,
        "housing": 10184,
        "land_area": 28442165.0,
        "water_area": 105387.0
    },
    "93376": {
        "name": "Waynesburg",
        "state": "Waynesburg PA",
        "population": 8754,
        "housing": 3117,
        "land_area": 10471933.0,
        "water_area": 0.0
    },
    "93421": {
        "name": "Waynesville",
        "state": "Waynesville NC",
        "population": 24285,
        "housing": 14359,
        "land_area": 71486656.0,
        "water_area": 832750.0
    },
    "93457": {
        "name": "Weatherford",
        "state": "Weatherford OK",
        "population": 12076,
        "housing": 5519,
        "land_area": 16304077.0,
        "water_area": 4631.0
    },
    "93484": {
        "name": "Weatherford",
        "state": "Weatherford TX",
        "population": 48112,
        "housing": 19274,
        "land_area": 100200945.0,
        "water_area": 176674.0
    },
    "93511": {
        "name": "Webster City",
        "state": "Webster City IA",
        "population": 7606,
        "housing": 3642,
        "land_area": 11347270.0,
        "water_area": 5458.0
    },
    "93619": {
        "name": "Weiser",
        "state": "Weiser ID--OR",
        "population": 5599,
        "housing": 2332,
        "land_area": 6340217.0,
        "water_area": 12197.0
    },
    "93673": {
        "name": "Wellington",
        "state": "Wellington CO",
        "population": 11071,
        "housing": 3936,
        "land_area": 8411288.0,
        "water_area": 6659.0
    },
    "93700": {
        "name": "Wellington",
        "state": "Wellington KS",
        "population": 7398,
        "housing": 3565,
        "land_area": 11377256.0,
        "water_area": 176243.0
    },
    "93727": {
        "name": "Wellington",
        "state": "Wellington OH",
        "population": 4783,
        "housing": 2160,
        "land_area": 7297159.0,
        "water_area": 647142.0
    },
    "93781": {
        "name": "Wellston",
        "state": "Wellston OH",
        "population": 5655,
        "housing": 2604,
        "land_area": 10334852.0,
        "water_area": 45073.0
    },
    "93808": {
        "name": "Wellsville",
        "state": "Wellsville NY",
        "population": 5339,
        "housing": 2667,
        "land_area": 9268911.0,
        "water_area": 0.0
    },
    "93862": {
        "name": "Wenatchee",
        "state": "Wenatchee WA",
        "population": 78142,
        "housing": 30561,
        "land_area": 84442129.0,
        "water_area": 2579947.0
    },
    "93876": {
        "name": "Wendell",
        "state": "Wendell NC",
        "population": 8915,
        "housing": 3358,
        "land_area": 10824852.0,
        "water_area": 0.0
    },
    "93916": {
        "name": "West Bend",
        "state": "West Bend WI",
        "population": 34552,
        "housing": 15486,
        "land_area": 44229767.0,
        "water_area": 1168975.0
    },
    "93970": {
        "name": "West Columbia",
        "state": "West Columbia TX",
        "population": 5888,
        "housing": 2537,
        "land_area": 9625372.0,
        "water_area": 1285408.0
    },
    "93997": {
        "name": "Westerly",
        "state": "Westerly RI--CT",
        "population": 30955,
        "housing": 17606,
        "land_area": 66520991.0,
        "water_area": 1272093.0
    },
    "94051": {
        "name": "West Frankfort",
        "state": "West Frankfort IL",
        "population": 7935,
        "housing": 4110,
        "land_area": 12160156.0,
        "water_area": 20155.0
    },
    "94132": {
        "name": "West Jefferson--Lake Darby",
        "state": "West Jefferson--Lake Darby OH",
        "population": 8828,
        "housing": 3391,
        "land_area": 13121059.0,
        "water_area": 52370.0
    },
    "94273": {
        "name": "West Milford",
        "state": "West Milford NJ--NY",
        "population": 17659,
        "housing": 8193,
        "land_area": 36830201.0,
        "water_area": 8762006.0
    },
    "94282": {
        "name": "West Milton",
        "state": "West Milton OH",
        "population": 4646,
        "housing": 2117,
        "land_area": 5065830.0,
        "water_area": 0.0
    },
    "94285": {
        "name": "Westminster",
        "state": "Westminster MD",
        "population": 40040,
        "housing": 15792,
        "land_area": 68360243.0,
        "water_area": 167197.0
    },
    "94321": {
        "name": "Weston",
        "state": "Weston WV",
        "population": 4430,
        "housing": 2346,
        "land_area": 5304160.0,
        "water_area": 177832.0
    },
    "94375": {
        "name": "West Plains",
        "state": "West Plains MO",
        "population": 11852,
        "housing": 5579,
        "land_area": 26557142.0,
        "water_area": 3385.0
    },
    "94402": {
        "name": "West Point",
        "state": "West Point MS",
        "population": 8134,
        "housing": 3807,
        "land_area": 16949011.0,
        "water_area": 104005.0
    },
    "94442": {
        "name": "West Point--Highland Falls",
        "state": "West Point--Highland Falls NY",
        "population": 12156,
        "housing": 3256,
        "land_area": 10119267.0,
        "water_area": 178924.0
    },
    "94510": {
        "name": "West Salem",
        "state": "West Salem WI",
        "population": 5557,
        "housing": 2466,
        "land_area": 8202452.0,
        "water_area": 773.0
    },
    "94564": {
        "name": "Westville",
        "state": "Westville IN",
        "population": 5189,
        "housing": 1099,
        "land_area": 5449886.0,
        "water_area": 0.0
    },
    "94591": {
        "name": "West Wendover",
        "state": "West Wendover NV--UT",
        "population": 5238,
        "housing": 1957,
        "land_area": 19146400.0,
        "water_area": 0.0
    },
    "94618": {
        "name": "Wetumpka",
        "state": "Wetumpka AL",
        "population": 6488,
        "housing": 2510,
        "land_area": 11512073.0,
        "water_area": 76695.0
    },
    "94672": {
        "name": "Wharton",
        "state": "Wharton TX",
        "population": 8526,
        "housing": 3907,
        "land_area": 12064164.0,
        "water_area": 7360.0
    },
    "94726": {
        "name": "Wheeling",
        "state": "Wheeling WV--OH",
        "population": 57695,
        "housing": 30319,
        "land_area": 95338640.0,
        "water_area": 4582785.0
    },
    "94753": {
        "name": "Whitefish",
        "state": "Whitefish MT",
        "population": 7898,
        "housing": 4733,
        "land_area": 15958749.0,
        "water_area": 475913.0
    },
    "94807": {
        "name": "Whitehall",
        "state": "Whitehall MI",
        "population": 8678,
        "housing": 4196,
        "land_area": 22206994.0,
        "water_area": 481409.0
    },
    "94861": {
        "name": "White House",
        "state": "White House TN",
        "population": 15587,
        "housing": 6077,
        "land_area": 33334011.0,
        "water_area": 0.0
    },
    "94863": {
        "name": "Whitehouse",
        "state": "Whitehouse TX",
        "population": 9139,
        "housing": 3272,
        "land_area": 13499092.0,
        "water_area": 151177.0
    },
    "94870": {
        "name": "Whiteman AFB--Knob Noster",
        "state": "Whiteman AFB--Knob Noster MO",
        "population": 5577,
        "housing": 2264,
        "land_area": 18912283.0,
        "water_area": 22214.0
    },
    "94915": {
        "name": "White Rock",
        "state": "White Rock NM",
        "population": 5169,
        "housing": 2131,
        "land_area": 5360096.0,
        "water_area": 0.0
    },
    "94996": {
        "name": "Whiteville",
        "state": "Whiteville NC",
        "population": 5216,
        "housing": 2613,
        "land_area": 12799949.0,
        "water_area": 0.0
    },
    "95050": {
        "name": "Whitewater",
        "state": "Whitewater WI",
        "population": 14544,
        "housing": 5300,
        "land_area": 11885661.0,
        "water_area": 492077.0
    },
    "95077": {
        "name": "Wichita",
        "state": "Wichita KS",
        "population": 500231,
        "housing": 214740,
        "land_area": 587514018.0,
        "water_area": 13155100.0
    },
    "95104": {
        "name": "Wichita Falls",
        "state": "Wichita Falls TX",
        "population": 97039,
        "housing": 42923,
        "land_area": 131530216.0,
        "water_area": 0.0
    },
    "95158": {
        "name": "Wickenburg",
        "state": "Wickenburg AZ",
        "population": 4801,
        "housing": 2826,
        "land_area": 12194044.0,
        "water_area": 1952.0
    },
    "95266": {
        "name": "Wildwood",
        "state": "Wildwood FL",
        "population": 13899,
        "housing": 5717,
        "land_area": 33101786.0,
        "water_area": 425286.0
    },
    "95301": {
        "name": "Wilkesboro--North Wilkesboro",
        "state": "Wilkesboro--North Wilkesboro NC",
        "population": 19890,
        "housing": 9160,
        "land_area": 86321877.0,
        "water_area": 0.0
    },
    "95310": {
        "name": "Willard",
        "state": "Willard MO",
        "population": 6854,
        "housing": 2604,
        "land_area": 22443423.0,
        "water_area": 3423.0
    },
    "95320": {
        "name": "Willard",
        "state": "Willard OH",
        "population": 6666,
        "housing": 2966,
        "land_area": 9229079.0,
        "water_area": 109332.0
    },
    "95374": {
        "name": "Williams",
        "state": "Williams CA",
        "population": 5558,
        "housing": 1767,
        "land_area": 5279096.0,
        "water_area": 0.0
    },
    "95401": {
        "name": "Williamsburg",
        "state": "Williamsburg KY",
        "population": 6365,
        "housing": 2395,
        "land_area": 13519719.0,
        "water_area": 273676.0
    },
    "95411": {
        "name": "Williamsburg",
        "state": "Williamsburg VA",
        "population": 89585,
        "housing": 38974,
        "land_area": 180953914.0,
        "water_area": 2649056.0
    },
    "95455": {
        "name": "Williamsport",
        "state": "Williamsport PA",
        "population": 55344,
        "housing": 25810,
        "land_area": 71807159.0,
        "water_area": 3189772.0
    },
    "95482": {
        "name": "Williamston",
        "state": "Williamston MI",
        "population": 4850,
        "housing": 2181,
        "land_area": 8162020.0,
        "water_area": 258119.0
    },
    "95509": {
        "name": "Williamston",
        "state": "Williamston NC",
        "population": 5522,
        "housing": 2815,
        "land_area": 12313674.0,
        "water_area": 0.0
    },
    "95536": {
        "name": "Williamston",
        "state": "Williamston SC",
        "population": 10350,
        "housing": 4446,
        "land_area": 26430255.0,
        "water_area": 87771.0
    },
    "95590": {
        "name": "Willimantic",
        "state": "Willimantic CT",
        "population": 24332,
        "housing": 9723,
        "land_area": 33278465.0,
        "water_area": 986095.0
    },
    "95644": {
        "name": "Williston",
        "state": "Williston ND",
        "population": 29510,
        "housing": 14641,
        "land_area": 56610616.0,
        "water_area": 98418.0
    },
    "95671": {
        "name": "Willits",
        "state": "Willits CA",
        "population": 7552,
        "housing": 3162,
        "land_area": 11312926.0,
        "water_area": 883.0
    },
    "95698": {
        "name": "Willmar",
        "state": "Willmar MN",
        "population": 21586,
        "housing": 8853,
        "land_area": 34107315.0,
        "water_area": 8869226.0
    },
    "95725": {
        "name": "Willows",
        "state": "Willows CA",
        "population": 7578,
        "housing": 2960,
        "land_area": 6952158.0,
        "water_area": 77318.0
    },
    "95806": {
        "name": "Wilmington",
        "state": "Wilmington IL",
        "population": 6388,
        "housing": 2836,
        "land_area": 10229749.0,
        "water_area": 1319139.0
    },
    "95833": {
        "name": "Wilmington",
        "state": "Wilmington NC",
        "population": 255329,
        "housing": 126576,
        "land_area": 368418797.0,
        "water_area": 11624430.0
    },
    "95860": {
        "name": "Wilmington",
        "state": "Wilmington OH",
        "population": 12546,
        "housing": 5625,
        "land_area": 24560919.0,
        "water_area": 87387.0
    },
    "95887": {
        "name": "Wilmore",
        "state": "Wilmore KY",
        "population": 5727,
        "housing": 1861,
        "land_area": 4542770.0,
        "water_area": 17330.0
    },
    "95914": {
        "name": "Wilson",
        "state": "Wilson NC",
        "population": 48326,
        "housing": 22724,
        "land_area": 71740856.0,
        "water_area": 314022.0
    },
    "95995": {
        "name": "Winchendon",
        "state": "Winchendon MA",
        "population": 4866,
        "housing": 2122,
        "land_area": 5885043.0,
        "water_area": 430643.0
    },
    "96022": {
        "name": "Winchester",
        "state": "Winchester IN",
        "population": 4797,
        "housing": 2348,
        "land_area": 7453035.0,
        "water_area": 24624.0
    },
    "96049": {
        "name": "Winchester",
        "state": "Winchester KY",
        "population": 26253,
        "housing": 11608,
        "land_area": 37349480.0,
        "water_area": 143732.0
    },
    "96076": {
        "name": "Winchester",
        "state": "Winchester TN",
        "population": 12702,
        "housing": 6016,
        "land_area": 33322256.0,
        "water_area": 692130.0
    },
    "96103": {
        "name": "Winchester",
        "state": "Winchester VA",
        "population": 83377,
        "housing": 33248,
        "land_area": 109352896.0,
        "water_area": 686932.0
    },
    "96130": {
        "name": "Winder",
        "state": "Winder GA",
        "population": 50189,
        "housing": 17820,
        "land_area": 134031585.0,
        "water_area": 1035901.0
    },
    "96184": {
        "name": "Wind Lake",
        "state": "Wind Lake WI",
        "population": 4856,
        "housing": 2070,
        "land_area": 9039511.0,
        "water_area": 4383400.0
    },
    "96319": {
        "name": "Winfield",
        "state": "Winfield KS",
        "population": 11617,
        "housing": 5173,
        "land_area": 18108038.0,
        "water_area": 21717.0
    },
    "96373": {
        "name": "Winnemucca",
        "state": "Winnemucca NV",
        "population": 10546,
        "housing": 4664,
        "land_area": 18711975.0,
        "water_area": 0.0
    },
    "96427": {
        "name": "Winnfield",
        "state": "Winnfield LA",
        "population": 4671,
        "housing": 2341,
        "land_area": 11536282.0,
        "water_area": 0.0
    },
    "96481": {
        "name": "Winnsboro",
        "state": "Winnsboro LA",
        "population": 5142,
        "housing": 2195,
        "land_area": 8358327.0,
        "water_area": 0.0
    },
    "96508": {
        "name": "Winnsboro",
        "state": "Winnsboro SC",
        "population": 4710,
        "housing": 2399,
        "land_area": 10017246.0,
        "water_area": 0.0
    },
    "96562": {
        "name": "Winona",
        "state": "Winona MN",
        "population": 29633,
        "housing": 13461,
        "land_area": 34406997.0,
        "water_area": 1205362.0
    },
    "96616": {
        "name": "Winslow",
        "state": "Winslow AZ",
        "population": 7667,
        "housing": 3320,
        "land_area": 9420762.0,
        "water_area": 0.0
    },
    "96643": {
        "name": "Winsted",
        "state": "Winsted CT",
        "population": 7804,
        "housing": 4289,
        "land_area": 15859370.0,
        "water_area": 1816621.0
    },
    "96670": {
        "name": "Winston-Salem",
        "state": "Winston-Salem NC",
        "population": 420924,
        "housing": 187144,
        "land_area": 804943620.0,
        "water_area": 5472404.0
    },
    "96697": {
        "name": "Winter Haven",
        "state": "Winter Haven FL",
        "population": 253251,
        "housing": 112523,
        "land_area": 369641486.0,
        "water_area": 74104913.0
    },
    "96724": {
        "name": "Winters",
        "state": "Winters CA",
        "population": 7073,
        "housing": 2528,
        "land_area": 4242386.0,
        "water_area": 64542.0
    },
    "96778": {
        "name": "Winterset",
        "state": "Winterset IA",
        "population": 5077,
        "housing": 2359,
        "land_area": 5865488.0,
        "water_area": 0.0
    },
    "96832": {
        "name": "Wisconsin Rapids",
        "state": "Wisconsin Rapids WI",
        "population": 29550,
        "housing": 13972,
        "land_area": 56514821.0,
        "water_area": 578246.0
    },
    "96843": {
        "name": "Wise--Norton",
        "state": "Wise--Norton VA",
        "population": 8913,
        "housing": 4452,
        "land_area": 28350106.0,
        "water_area": 95639.0
    },
    "96913": {
        "name": "Woodburn",
        "state": "Woodburn OR",
        "population": 27577,
        "housing": 8921,
        "land_area": 19569313.0,
        "water_area": 35004.0
    },
    "96967": {
        "name": "Woodlake",
        "state": "Woodlake CA",
        "population": 7514,
        "housing": 2263,
        "land_area": 4889571.0,
        "water_area": 0.0
    },
    "96994": {
        "name": "Woodland",
        "state": "Woodland CA",
        "population": 61133,
        "housing": 21666,
        "land_area": 33062970.0,
        "water_area": 0.0
    },
    "97021": {
        "name": "Woodland",
        "state": "Woodland WA",
        "population": 7217,
        "housing": 2593,
        "land_area": 11365577.0,
        "water_area": 337033.0
    },
    "97048": {
        "name": "Woodland Park",
        "state": "Woodland Park CO",
        "population": 11548,
        "housing": 5647,
        "land_area": 24042059.0,
        "water_area": 6086.0
    },
    "97061": {
        "name": "Woodmont",
        "state": "Woodmont GA",
        "population": 6673,
        "housing": 2281,
        "land_area": 13350263.0,
        "water_area": 86781.0
    },
    "97129": {
        "name": "Woodstock",
        "state": "Woodstock IL",
        "population": 25298,
        "housing": 10243,
        "land_area": 24100107.0,
        "water_area": 0.0
    },
    "97156": {
        "name": "Woodstock",
        "state": "Woodstock VA",
        "population": 5852,
        "housing": 2572,
        "land_area": 10055007.0,
        "water_area": 16356.0
    },
    "97237": {
        "name": "Woodward",
        "state": "Woodward OK",
        "population": 11458,
        "housing": 5737,
        "land_area": 23525040.0,
        "water_area": 43636.0
    },
    "97264": {
        "name": "Wooster",
        "state": "Wooster OH",
        "population": 32449,
        "housing": 14287,
        "land_area": 56182236.0,
        "water_area": 171018.0
    },
    "97291": {
        "name": "Worcester",
        "state": "Worcester MA--CT",
        "population": 482085,
        "housing": 196132,
        "land_area": 674150071.0,
        "water_area": 30788965.0
    },
    "97318": {
        "name": "Worland",
        "state": "Worland WY",
        "population": 4889,
        "housing": 2525,
        "land_area": 7700989.0,
        "water_area": 62312.0
    },
    "97331": {
        "name": "World Golf Village",
        "state": "World Golf Village FL",
        "population": 19679,
        "housing": 7492,
        "land_area": 35932846.0,
        "water_area": 691598.0
    },
    "97345": {
        "name": "Worthington",
        "state": "Worthington MN",
        "population": 13800,
        "housing": 4710,
        "land_area": 14193160.0,
        "water_area": 3097152.0
    },
    "97349": {
        "name": "Worth--Lexington",
        "state": "Worth--Lexington MI",
        "population": 3310,
        "housing": 3668,
        "land_area": 10812098.0,
        "water_area": 96363.0
    },
    "97372": {
        "name": "Wrightwood",
        "state": "Wrightwood CA",
        "population": 3927,
        "housing": 2208,
        "land_area": 3586964.0,
        "water_area": 8669.0
    },
    "97426": {
        "name": "Wynne",
        "state": "Wynne AR",
        "population": 7564,
        "housing": 3383,
        "land_area": 14118046.0,
        "water_area": 28415.0
    },
    "97453": {
        "name": "Wytheville",
        "state": "Wytheville VA",
        "population": 7154,
        "housing": 3784,
        "land_area": 15489626.0,
        "water_area": 29963.0
    },
    "97470": {
        "name": "Xenia",
        "state": "Xenia OH",
        "population": 26614,
        "housing": 11923,
        "land_area": 29511809.0,
        "water_area": 24735.0
    },
    "97507": {
        "name": "Yakima",
        "state": "Yakima WA",
        "population": 133145,
        "housing": 51147,
        "land_area": 144445992.0,
        "water_area": 820952.0
    },
    "97534": {
        "name": "Yankton",
        "state": "Yankton SD",
        "population": 16022,
        "housing": 7072,
        "land_area": 22049332.0,
        "water_area": 0.0
    },
    "97561": {
        "name": "Yauco",
        "state": "Yauco PR",
        "population": 63885,
        "housing": 30548,
        "land_area": 90408094.0,
        "water_area": 62608.0
    },
    "97588": {
        "name": "Yazoo City",
        "state": "Yazoo City MS",
        "population": 15060,
        "housing": 4931,
        "land_area": 23950631.0,
        "water_area": 284814.0
    },
    "97642": {
        "name": "Yelm",
        "state": "Yelm WA",
        "population": 14924,
        "housing": 5099,
        "land_area": 19973944.0,
        "water_area": 72039.0
    },
    "97696": {
        "name": "Yoakum",
        "state": "Yoakum TX",
        "population": 5598,
        "housing": 2473,
        "land_area": 8161955.0,
        "water_area": 0.0
    },
    "97723": {
        "name": "York",
        "state": "York NE",
        "population": 7968,
        "housing": 3735,
        "land_area": 12242381.0,
        "water_area": 33827.0
    },
    "97750": {
        "name": "York",
        "state": "York PA",
        "population": 238549,
        "housing": 97643,
        "land_area": 292871617.0,
        "water_area": 431678.0
    },
    "97777": {
        "name": "York",
        "state": "York SC",
        "population": 8631,
        "housing": 3573,
        "land_area": 16859608.0,
        "water_area": 199655.0
    },
    "97831": {
        "name": "Youngstown",
        "state": "Youngstown OH",
        "population": 320901,
        "housing": 153376,
        "land_area": 507544101.0,
        "water_area": 4156956.0
    },
    "97912": {
        "name": "Yreka",
        "state": "Yreka CA",
        "population": 7617,
        "housing": 3591,
        "land_area": 13738918.0,
        "water_area": 77452.0
    },
    "97939": {
        "name": "Yuba City",
        "state": "Yuba City CA",
        "population": 125706,
        "housing": 42911,
        "land_area": 77697453.0,
        "water_area": 211908.0
    },
    "97966": {
        "name": "Yucca Valley",
        "state": "Yucca Valley CA",
        "population": 18293,
        "housing": 8224,
        "land_area": 29355757.0,
        "water_area": 0.0
    },
    "98020": {
        "name": "Yuma",
        "state": "Yuma AZ--CA",
        "population": 135717,
        "housing": 70358,
        "land_area": 137158796.0,
        "water_area": 292400.0
    },
    "98061": {
        "name": "Zachary",
        "state": "Zachary LA",
        "population": 16600,
        "housing": 6388,
        "land_area": 29444680.0,
        "water_area": 0.0
    },
    "98074": {
        "name": "Zanesville",
        "state": "Zanesville OH",
        "population": 42301,
        "housing": 20014,
        "land_area": 73304717.0,
        "water_area": 745596.0
    },
    "98101": {
        "name": "Zapata--Medina",
        "state": "Zapata--Medina TX",
        "population": 10942,
        "housing": 4642,
        "land_area": 12847239.0,
        "water_area": 0.0
    },
    "98129": {
        "name": "Zebulon",
        "state": "Zebulon NC",
        "population": 8158,
        "housing": 3149,
        "land_area": 15830724.0,
        "water_area": 33264.0
    },
    "98182": {
        "name": "Zephyrhills",
        "state": "Zephyrhills FL",
        "population": 55133,
        "housing": 32009,
        "land_area": 88329081.0,
        "water_area": 385518.0
    },
    "98209": {
        "name": "Zimmerman",
        "state": "Zimmerman MN",
        "population": 6360,
        "housing": 2345,
        "land_area": 8431785.0,
        "water_area": 1986455.0
    }
}

def color_closeness(color1: str, color2: str) -> float:
    r_closeness = 1 - abs(int(color1[1:3], 16) - int(color2[1:3], 16))/256
    g_closeness = 1 - abs(int(color1[3:5], 16) - int(color2[3:5], 16))/256
    b_closeness = 1 - abs(int(color1[5:7], 16) - int(color2[5:7], 16))/256
    return ((r_closeness + g_closeness + b_closeness) / 3) ** 2

def saturation(r_channel, g_channel, b_channel) -> float:
    max_sat = max(r_channel, g_channel, b_channel)
    min_sat = min(r_channel, g_channel, b_channel)
    delta = max_sat - min_sat
    saturation = 0
    if max_sat != 0:
        saturation = delta / max_sat
    return saturation

def brightness(r_channel, g_channel, b_channel) -> float:
    return ((r_channel/255) + (g_channel/255) + (b_channel/255))/3

def generate_color_channel() -> int:
    return random.randint(0, 255)

used_colors = set()
def generate_color(targ_saturation: float = -1.0, targ_brightness: float = 0.0) -> str:
    r_channel = generate_color_channel()
    g_channel = generate_color_channel()
    b_channel = generate_color_channel()
    sat = saturation(r_channel, g_channel, b_channel)
    bright = brightness(r_channel, g_channel, b_channel)
    if sat < targ_saturation or bright < targ_brightness: return generate_color(targ_saturation, targ_brightness)

    def code(channel: int) -> str: return hex(channel).split('x')[1].rjust(2, '0')
    color = f'#{code(r_channel)}{code(g_channel)}{code(b_channel)}'

    if color in used_colors: return generate_color(targ_saturation, targ_brightness)
    used_colors.add(color)

    return color

def main():
    print(f'UACE,color')
    for UACE, urban_area in urban_areas_data.items():
        color = generate_color(0.8, 0.5)
        print(f'{UACE},{color}')

if __name__ == '__main__':
    main()
