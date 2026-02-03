import json
from typing import Any, Dict

def main() -> None:
    history: Dict[str, Any] = {}
    with open('src\\main\\resources\\conventions\\electoral_history.json', 'r') as data:
        contents = data.read()
        history = json.loads(contents)

    state_history: Dict[str, Dict[str, Any]] = {}

    for county_fips, data in history.items():
        state_fips = county_fips[:2]
        state_name = data['state']
        state_history.setdefault(state_fips, {'state': state_name, 'fips': state_fips, 'results': {}})
        state_results = state_history[state_fips]['results']
        for year, v in data['results'].items():
            # print(v['result'])
            state_results.setdefault(year, {'total_votes': 0, 'result': []})
            state_results[year]['total_votes'] += v['total_votes']
            # print(year, state_results[year])
            for result in v['result']:
                for r in state_results[year]['result']:
                    if r['candidate'] == result['candidate']:
                        r['votes'] += result['votes']
                        break
                else:
                    # print("Adding")
                    state_results[year]['result'].append({'party': result['party'], 'candidate': result['candidate'], 'votes': result['votes']})
                    # print(state_results[year]['result'])

    with open('src\\main\\resources\\conventions\\state_electoral_history.json', 'w') as out:
        out.write(json.dumps(state_history, indent=4, separators=(', ', ' : ')))


if __name__ == "__main__":
    main()