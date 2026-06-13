
const center = [40, -96], defaultZoom = 5;
const map = L.map('map').setView(center, defaultZoom);

function resetView() {
    map.setView(center, defaultZoom);
}

let geoJSONNation = null;
let nation = null;
let geoJSONStates = null;
let states = null;
let geoJSONCounties = null;
let counties = null;

function style(feature) {
    return {
        fillColor: feature.color,
        weight: 1.0,
        opacity: 1,
        color: "#333",
        fillOpacity: 1.0,
        interactive: true
    };
}

async function loadMapData() {
    await fetch('us-states.json').then(res => res.json()).then(topoData => {
       nation   = topojson.feature(topoData, topoData.objects.nation);
       states   = topojson.feature(topoData, topoData.objects.states);
       counties = topojson.feature(topoData, topoData.objects.counties);

       return fetch('colors.json').then(res => res.json()).then(colors => {

           nation.features.forEach(f => {
           });
           states.features.forEach(f => {
               const stateColor = colors[f.id].state_color;
               if (!stateColor) {
                   console.warn(`No state color found for FIPS: ${f.id}.`);
               }
               f.color = stateColor;
           });
           countiesParsedPerState = {};
           counties.features.forEach(f => {
               const stateId = f.id.slice(0, 2);
               const stateColors = colors[stateId].county_colors;
               let countyIndex = countiesParsedPerState[stateId];
               if (!countyIndex) {
                   countiesParsedPerState[stateId] = 0;
                   countyIndex = 0;
               }
               countiesParsedPerState[stateId] += 1;
               const countyColor = stateColors[countyIndex];
               if (!countyColor) {
                   console.warn(`No county color found for FIPS: ${f.id}.`);
               }
               f.color = countyColor;
           });

           geoJSONNation   = L.geoJSON(nation, {style});
           geoJSONStates   = L.geoJSON(states, {style});
           geoJSONCounties = L.geoJSON(counties, {style});
       });
    });
}

function mapCSV() {
    let res = '';
    res += 'GEOID,color\n';
    counties.features.forEach(f => {
       res += `${f.id},${f.color}\n`
    });
    console.log(res);
    return res;
}

document.addEventListener('DOMContentLoaded', async () => {

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 10,
    }).addTo(map);

    window.addEventListener('resize', () => map.invalidateSize());

    await loadMapData();

    map.addLayer(geoJSONStates);
    map.addLayer(geoJSONCounties);
    //
    mapCSV();
});
