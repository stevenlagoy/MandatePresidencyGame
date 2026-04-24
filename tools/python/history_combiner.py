import json

def main() -> None:
    
    # Read
    with open('src\\main\\resources\\conventions\\county_electoral_history.json', 'r') as data:
        contents = data.read()
        county_history = json.loads(contents)
    with open('src\\main\\resources\\conventions\\state_electoral_history.json', 'r') as data:
        contents = data.read()
        state_history = json.loads(contents)

    # Combine
    combined_history = county_history | state_history

    # Sort
    combined_history = dict(sorted(combined_history.items()))

    # Write
    with open('src\\main\\resources\\conventions\\electoral_history.json', 'w') as out:
        out.write(json.dumps(combined_history, indent=4, separators=(', ', ' : ')))

if __name__ == "__main__":
    main()