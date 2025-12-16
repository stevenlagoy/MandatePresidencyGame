from typing import Any, List, Dict, Tuple, Set
import itertools
import json
import random

DIR = "src\\main\\resources\\names\\"
FIRSTNAMES_FILE = DIR + "given_names.json"
MIDDLENAMES_FILE = DIR + "given_names.json"
LASTNAMES_FILE = DIR + "family_names.json"

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


def most_specific_paths(tree: Dict, targets: List[str] | Set[str]) -> List[List[str]]:
    '''
    Docstring for most_specific_paths
    
    :param tree: A tree-shaped dictionary (nested `dict[str, dict]`)
    :type tree: Dict
    :param targets: Target key strings in the tree
    :type targets: List[str]
    :return: The most specific paths (ordered list of keys) which cover all target keys
    :rtype: List[List[str]]
    '''
    targets = set(targets)

    # Collect all root-to-node paths
    all_paths: List[List[str]] = []

    def dfs(node: Dict, path: List[str] | None = None):
        nonlocal all_paths
        if path is None: path = []
        for k, v in node.items():
            if isinstance(v, dict):
                new_path = path + [k]
                all_paths.append(new_path)
                dfs(v, new_path)
    dfs(tree)

    # For each path, compute covered targets
    path_covers: List[Tuple[List[str], Set[str]]] = [ # Much data structures very wow
        (p, set(p) & targets)
        for p in all_paths
        if set(p) & targets
    ]

    best_solution = None
    best_score = None

    # Backtrack set cover with pruning
    def backtrack(remaining: Set[str], chosen: List[List[str]] | None = None):
        if chosen is None: chosen = []
        nonlocal best_solution, best_score

        score = (len(targets) - len(remaining), -len(chosen), -sum(len(p) for p in chosen))
        if best_score is None or score > best_score:
            best_score = score
            best_solution = chosen[:] # shallow copy

        # Prune if current sol'n cannot improve best score
        if best_score and (len(targets) - len(remaining) + len(chosen) > best_score[0]):
            return
        
        for path, covers in path_covers:
            if covers & remaining:
                backtrack(
                    remaining - covers,
                    chosen + [path]
                )
    backtrack(targets)

    return best_solution or []

def get_names(tree: Dict, paths: List[List[str]]) -> List[str]:
    res: List[str] = []
    for path in paths:
        current = tree
        for key in path:
            current = current[key] # May throw KeyError
        for name in current:
            res.append(name)
    return res

def choose_name(names: List[str]) -> str | None:
    if len(names) == 0: return None
    return random.choice(names)

def main() -> None:
    (firstnames, middlenames, lastnames) = load_resources()

    keys = ["Man", "White", "Anglo", "Christian", "Baby Boomer"]
    given_keys = most_specific_paths(firstnames, keys)
    family_keys = most_specific_paths(lastnames, keys)
    first_names = get_names(firstnames, given_keys)
    middle_names = get_names(middlenames, given_keys)
    last_names = get_names(lastnames, family_keys)

    for _ in range(100):
        firstname = choose_name(first_names)
        middlename = choose_name(middle_names)
        lastname = choose_name(last_names)

        print(f"{firstname} {middlename} {lastname}")

if __name__ == "__main__":
    main()