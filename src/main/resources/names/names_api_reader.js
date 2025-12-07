import fs from 'node:fs';

const numberNames = 1000;

let data = await fetch(`https://api.census.gov/data/2010/surname.html?get=NAME,COUNT,PCTAIAN,PCTAPI,PCTBLACK,PCTHISPANIC,PCTWHITE&RANK=1:${numberNames}`);
data = await data.json();

const nationalPctAmericanIndianAlaskaNative = 0.0073;
const nationalPctAsianPacificIslander = 0.0484;
const nationalPctBlack = 0.1221;
const nationalPctHispanic = 0.1635;
const nationalPctWhite = 0.6375;

class Name {
    constructor(
        name, count,
        pctAIAN, pctAPI,
        pctBlack, pctHispanic,
        pctWhite, rank
    ) {
        this.name = name.charAt(0) + name.substring(1).toLowerCase();
        this.count = count;
        this.pctAmericanIndianAlaskaNative = pctAIAN / 100 / nationalPctAmericanIndianAlaskaNative;
        this.pctAsianPacificIslander = pctAPI / 100 / nationalPctAsianPacificIslander;
        this.pctBlack = pctBlack / 100 / nationalPctBlack;
        this.pctHispanic = pctHispanic / 100 / nationalPctHispanic;
        this.pctWhite = pctWhite / 100 / nationalPctWhite;
        this.rank = rank;
    }
};

const names = [];
for (let n of data) {
    if (n[0] === 'NAME') continue;
    console.log(n)
    names.push(new Name(n[0], n[1], n[2], n[3], n[4], n[5], n[6], n[7], n[8]));
    console.log(names[names.length - 1])
}

const americanIndianAlaskaNativeLastnames = {};
const asianPacificIslanderLastnames = {};
const blackLastnames = {};
const hispanicLastnames = {};
const whiteLastnames = {};
for (let n of names) {
    if (n.pctAmericanIndianAlaskaNative)
        americanIndianAlaskaNativeLastnames[n.name] = n.count * n.pctAmericanIndianAlaskaNative;
    if (n.pctAsianPacificIslander)
        asianPacificIslanderLastnames[n.name] = n.count * n.pctAsianPacificIslander;
    if (n.pctBlack)
        blackLastnames[n.name] = n.count * n.pctBlack;
    if (n.pctHispanic)
        hispanicLastnames[n.name] = n.count * n.pctHispanic;
    if (n.pctWhite)
        whiteLastnames[n.name] = n.count * n.pctWhite;
}

const result = {
    'American Indian Alaska Native' : americanIndianAlaskaNativeLastnames,
    'Asian Pacific Islander' : asianPacificIslanderLastnames,
    'Black' : blackLastnames,
    'Hispanic' : hispanicLastnames,
    'White' : whiteLastnames,
}

const ws = fs.createWriteStream('race_ethnicity_lastnames.json');
ws.write(`${JSON.stringify(result, null, '\t')}`);
ws.end();