from typing import Any, List, Dict, Tuple, Set
import itertools
import json

DIR = "src\\main\\resources\\names\\"
FIRSTNAMES_FILE = DIR + "test_firstnames.json"
MIDDLENAMES_FILE = DIR + "middlename_distribution.json"
LASTNAMES_FILE = DIR + "lastname_distribution.json"

def load_resources() -> Tuple[Dict[str, Dict[str, Any]], Dict[str, Dict[str, Any]], Dict[str, Any]]:
    firstnames: Dict[str, Dict[str, Any]] = {}
    middlenames: Dict[str, Dict[str, Any]] = {}
    lastnames: Dict[str, Any] = {}
    with open(FIRSTNAMES_FILE, 'r', encoding='utf-8') as fn:
        firstnames = json.loads(fn.read())
    with open(MIDDLENAMES_FILE, 'r', encoding='utf-8') as mn:
        middlenames = json.loads(mn.read())
    with open(LASTNAMES_FILE, 'r', encoding='utf-8') as ln:
        lastnames = json.loads(ln.read())

    return (firstnames, middlenames, lastnames)


def get_possible_names(namespace: Dict[str, Any], *keys: str) -> Set[str]:
    '''
    get_possible_names() searches a namespace based on a list of keys and finds the most specific possible set of names which fulfil all those keys.
    Recursive function.
    
    :param namespace: Nested map of string bloc to name:popularity pairs or to same-type submaps.
    :type namespace: Dict[str, Any]
    :param keys: Strings to fulfill as keys in the namespace. All keys will be fulfilled with greatest possible specificity in the namespace.
    :type keys: str
    :return: List of names based on the bloc keys.
    :rtype: List[str]
    '''
    res: Set[str] = set()
    
    # Find fewest number of lists which fulfill all the keys
    # (Man + White + Christian) & (Man + White + Boomer) fulfills [Man, White, Christian, Boomer]

    # Generate key permutations. Each is a reordering of the keys
    combinations: List[Tuple[str, ...]] = []
    for r in range(len(keys) + 1):
        for combo in itertools.combinations(keys, r):
            combinations.append(combo)

    working_combos: List[Tuple[str, ...]] = []
    for combo in combinations:
        works = True
        it = namespace
        for key in combo:
            if key not in it:
                works = False
            if not works:
                continue
            it = it[key]
        if works:
            working_combos.append(combo)

    # Find the most specific combination of working combinations which fulfills all the keys
    working_combo_combos: List[Tuple[Tuple[str, ...], ...]] = []
    for r in range(len(working_combos) + 1):
        for combo in itertools.combinations(working_combos, r):
            working_combo_combos.append(combo)
    
    working_working_combo_combos = []
    for working_combo_combo in working_combo_combos:
        contained: List[str] = []
        for working_combo in working_combo_combo:
            for key in working_combo:
                contained.append(key)
        if all([key in contained for key in keys]):
            # found one
            working_working_combo_combos.append(working_combo_combo)

    if len(working_working_combo_combos) < 1:
        return res
    # Find the shortest
    shortest_idxs: List[int] = [0]
    shortest_len = len(working_working_combo_combos[shortest_idxs[0]])
    for i in range(len(working_working_combo_combos)):
        if len(working_working_combo_combos[i]) < shortest_len:
            shortest_idxs = [i]
            shortest_len = len(working_working_combo_combos[i])
        elif len(working_working_combo_combos[i]) == shortest_len:
            shortest_idxs.append(i)

    bests = [working_working_combo_combos[shortest_idxs[i]] for i in shortest_idxs]
    print(bests)

    return res

def main() -> None:
    (firstnames, middlenames, lastnames) = load_resources()

    keys = ["Man", "White", "Christian", "Boomer"]
    keys = get_possible_names(firstnames, *keys)

    print(keys)

if __name__ == "__main__":
    main()