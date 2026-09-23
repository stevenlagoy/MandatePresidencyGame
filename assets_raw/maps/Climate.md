# Precipitation

### Attribution

**Author**: Steven LaGoy

**Created**: 09 July 2026

**Modified**: 09 September 2026

### Dimensions

52224px width × 28672px length (1,497,366,528px square area)

### Color Channels

A : Annual Average Precipitation
R : Normal Mean Summer Temperature
G : Tree Cover / Vegetation
B : Normal Mean Winter Temperature

### Projection

ESRI:102004 - USA Contiguous Lambert Conformal Conic

### Description

Areas of North America are shaded on four channels based on climate factors. The alpha channel
encodes annual average precipitation, with seven discrete steps from 0" average annual rainfall
to 80+" average annual rainfall. The red channel encodes normal mean temperature in summer (July)
between 1991 and 2020. The green channel encodes tree cover / vegetation as a percentage of land
covered by trees, with eight discrete steps between no vegetation and heavy vegetation. The blue
channel is similar to the red channel but encodes normal mean temperature in winter (January) for
the same years. The climate of an area, as determined by precipitation, vegetation, and average
summer and winter temperature, can be determined by the values of the four channels.

The alpha channel ranges from 240 to 255 with 7 discrete steps, with lower values corresponding to
lower annual precipitation.
The red and blue channels are smooth shaded between 0 = -20°C and 255 = 40°C. Lower values
on these channels indicate lower normal mean temperatures for their respective seasons. Both
channels are scaled using the same minimum and maximum values (IE a value of 128 means the same
temperature on both the red and blue channel, 10°C).
The green channel uses 8 discrete steps between 31 and 255, with lower values corresponding to
areas with less tree coverage. The steps are each 32 apart, but the percentages of tree cover
encoded by each shade are not linear.

Areas which are not assigned a climate (ocean) are colored black (#00000000). The Great Lakes are
also not assigned a climate.

The temperature maps contain data for the continental United States. The tree cover map contains
data for mainland North America, Canadian arctic islands, Greenland, Iceland, a portion of Siberia,
and the Carribbean: only Hawaii and the Pacific US territories are not included. The precipitation
map contains data for all visible parts of the map.

### Meta

This file was made by combining four maps, one for each of the climate factors / channels.
The summer temperature map was made by applying a smooth shading between black and white for the
temperature gradient, and then discarding the channels other than the desired red channel.
The winter temperature map was similarly made by applying the same smooth grayscale shading as the
summer temperature map, and discarding the channels other than the desired blue channel.
The tree cover map was made by geoprojecting a raster map, applying gaussian blend to reduce
pixelization, then posterizing to create a number of discrete buckets not present in the original
map. These buckets were then assigned equidistant new colors on the green channel for visual
differentiation. This process resulted in some erroneous artifacts, but these are considered minor
and will not affect gameplay.
The precipitaiton map was made by tracing a geoprojected raster which already used discrete steps
for fixed precipitation amounts, which remained unchanged for the current map version. The steps
were assigned new grayscale values. This was then converted to an alpha mask.
To combine the files, the temperature maps and tree cover map were layered in additive mode and
merged. The alpha mask from the precipitaiton map was then applied to the merged map to create the
final four-channel climate map.

### Data Sources

- Global Forest Watch Canada. www.globalforestwatch.ca
- Encyclopædia Britannica. “Average annual precipitation in North America,” https://kids.britannica.com/students/assembly/view/151954.
- Encyclopædia Britannica. “North America,” https://www.britannica.com/place/North-America.
- PRISM/NACSE, Oregon State University. “PRISM 30-Year Normals,” https://prism.oregonstate.edu/normals/.

*This map constitutes an independent creation which transforms the original factual data, including
unique styling, colors, and map projection, qualifying as a permissible use of public factual
information.*
