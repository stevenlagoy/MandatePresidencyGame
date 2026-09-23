# Hydrology

### Attribution

**Author**: Steven LaGoy

**Created**: 15 July 2026

**Modified**: 03 September 2026

### Dimensions

52224px width × 28672px length (1,497,366,528px square area)

### Color Channels

RGB: Hydrology

### Projection

ESRI:102004 - USA Contiguous Lambert Conformal Conic

### Description

Hydrological features of North America, including Oceans, lentic and lotic inland water systems,
wetlands, and drainage basins. Oceans are areas of deep saltwater in which the subaqueous soil
(seabed) is always naturally submerged. Lentic systems are standing bodies of freshwater of
moderate depth, including most lakes and larger ponds. Lotic systems are flowing bodies of
freshwater, including streams, rivers, creeks, canals, and springs. Wetlands are areas where
soils are saturated by shallow standing water from the surface, including marshes, swamps, bogs,
and mangroves. Tidal areas and sandy coastal structures are also included, including sandbars,
atolls, tidal beaches, playas, and foreshores. All land is colored in unique colors according to
river drainage basins, with areas in which water flows into the same river around the same area in
a unique color. The most notable systems described above are displayed in unique colors as well,
while other systems of those types may be displayed as shades on top of the river basin colors.

In some cases, significant portions of lakes and rivers which have their own name are displayed in
a separate color from the main body, especially in the Great Lakes. An example of this is Green Bay,
which is shown in a different color from Lake Michigan. In these cases, the smaller bodies should
still be considered part of the larger body, but also have a different name and shape.

Where multiple types of water body coincide, the following order of precedence is followed, with
earlier elements in the list displayed on top of later elements:
- Large rivers (unique colors)
- Large lakes (unique colors)
- Steam / River (shade)
- Lake / Pond (shade)
- Ice Mass
- Inundation Area
- Playa
- Reservoir
- Swamp / Marsh
- Canadian inland water bodies
- Swamp / Marsh
- Other Systems
- Unclassified non-ocean
- Ocean
- River Basins

Small water bodies are displayed by applying a shade to their underlying river basin color. The
colors below describe the initial shade used for each type of water body. These were applied with
an opacity value of 128/255 to the river basin colors. Note that these shades are only used for
bodies of water which are not assigned their own unique color. There may be some area in which
these colors (or their transparent shadings) appear alone rather than as a shade of a river basin.

- `#FFFFFF` = Canadian inland water bodies
- `#E6E6E6` = Canal / Ditch
- `#CDCDCD` = Ice Mass
- `#B3B3B3` = Inundation Area
- `#9A9A9A` = Lake / Pond
- `#808080` = Playa
- `#676767` = Reservoir
- `#4D4D4D` = Stream / River
- `#333333` = Swamp / Marsh
- `#191919` = Other systems (unused)

Oceans:
The Arcitc Ocean generally follows the 10 °C July isotherm, as well as Hudson Bay. The Atlantic
Ocean is counted as all ocean to the east of the continental divide south of the Arctic Ocean. The
Pacific Ocean is similarly counted as all ocean to the west of the continental divide south of the
Arctic Ocean. The colors below are chosen mostly randomly for contrast with other shading colors.

- `#000018` = Pacific Ocean
- `#001800` = Arctic Ocean
- `#181800` = Hudson Bay
- `#180000` = Atlantic Ocean
- `#180018` = Gulf of Mexico

Non-ocean areas which are not classified (outside the mainland US and Alaska, Canada, or Mexico) 
are colored white (`#FFFFFF`).

Water bodies of different kinds, including oceans, lentic systems, lotic systems, and wetlands,
are displayed in different shades of blue. Oceans are areas of deep saltwater in which the
subaqueous soil (seabed) is always naturally submerged. Lentic systems are standing bodies of
freshwater of moderate depth, including most lakes and larger ponds. Lotic systems are flowing
bodies of freshwater, including streams, rivers, creeks, and springs. Wetlands are areas where
soils and shallow standing water form the surface, including most marshes, swamps, bogs, and
mangroves, as well as sandy structures like sandbars, atolls, tidal beaches, and foreshores. Areas
which are not water bodies (in other words, dry land) are black (#000000). Where overlapping
water bodies are present (I.E. the mouth or inlet of a river which overlays the ocean or a lake),
the order of preference is: lotic systems, wetlands, lentic systems, oceans. Types earlier in this
list will be displayed on top of items later in the list. For consistency with other maps (like the
Topography/Bathymetry Map), the mouth of the St. Lawrence river is shown just south of the Île
d'Orléans and just north of Québec City, rather than near Tadoussac, as used by some definitions.
River width does not necessarily correspond to volume or speed, but rather to the actual area
coverage of the surface of the river at average flow. To maintain the connectedness of smaller
rivers and streams, all rivers are given a minimum width of 3px. No data is present for the
following areas: Russia, Greenland, Iceland, Caribbean Islands including Puerto Rico and the US
Virgin Islands, and Pacific Islands including Hawaii.

### Selected Shades

- `#FFFFFF` = Unclassified non-ocean
- `#000018` = Pacific Ocean
- `#001800` = Arctic Ocean
- `#180000` = Atlantic Ocean

### Data Sources

- Commission for Environmental Cooperation. “North American Lakes and Rivers, 2023,” https://www.cec.org/north-american-environmental-atlas/lakes-and-rivers-2023/, accessed 15 July 2026.
- Esri Data and Maps. “USA Detailed Water Bodies,” https://hub.arcgis.com/datasets/esri::usa-detailed-water-bodies/explore, accessed 15 July 2026.
- Government of Canada. “Lakes and Rivers (polygons) Boundary files - 2016 Census,” https://open.canada.ca/data/en/dataset/d0cdef71-9343-46c3-b2e7-c1ded5907686, accessed 15 July 2026.
- University of Manitoba. “Coastal Waters Boundary file - 2016 Census,” Centre for Earth Observation Science, https://canwinmap.ad.umanitoba.ca/layers/geoserver:geonode:lhy_000h16a_e, accessed 15 July 2026.
- https://en.wikipedia.org/wiki/Arctic_Ocean

*This map constitutes an independent creation which transforms the original factual data, including
unique styling, colors, and map projection, qualifying as a permissible use of public factual
information.*
