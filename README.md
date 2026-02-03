# Mandate: Race for the Presidency

Mandate: Race for the Presidency is a a real-time strategy/roleplaying game that simulates the complex journey of running for President of the United States.

## NOTE

This is a work-in-progress personal project. While running this code will not harm your computer, I cannot guarantee it will run on every machine, and even if it does run then there's likely little to be seen. The game is very much in pre-alpha and everything is liable to change with later development. So have no expectations for what this game will actually do. Thanks :)

## Overview

Mandate simulates a detailed political system in which you take on the role of a presidential candidate in the United States. Navigate the complex political landscape as you:

- Create and customize your candidate with unique traits, skills, and political opinions
- Balance resources including funds, influence, and worktime
- Appeal to diverse voting blocs and interest groups across the country
- Respond to real-time events that can make or break your campaign
- Manage relationships with party members, other candidates, and your campaign staff
- Win your party's nomination through primaries and caucuses
- Compete against an opponent to win in the Electoral College and become President

## Features

- **Deep Character Customization**: Create a unique candidate with customizable demographics, skills, experience, education, and political positions.
- **Geographic Detail**: Campaign across states, congressional districts, and counties, each with their own demographic compositions.
- **Diverse Voting Blocs**: Appeal to voters across various demographics including age, religion, education, ethnicity, and more.
- **Resource Management**: Balance campaign funds, personal influence, and supporter pledges.
- **Political Positioning**: Take stances on important issues that may appeal to some voters while alienating others.
- **Dynamic Events**: React to unexpected events that can change the course of your campaign.
- **AI Opponents**: Compete against computer-controlled candidates with their own strategies and personalities.
- **Multiple Difficulty Levels**: Choose your challenge, from running as an incumbent to facing steep opposition.

## Installation

### Requirements
- Java 17
- At least 8GB RAM recommended
- 500MB free disk space
- 1080p or better monitor

### Setup Instructions

1. Prerequisites:
    Check that you have Java JDK 17.
    ```
    java -version
    ```
    You should see a listed version of 17 or higher. If you have multiple Java versions installed, ensure JAVA_HOME points to JDK 17.
    <p>
    Also ensure that you have Git installed. An IDE is recommended, such as IntelliJ IDEA, Eclipse, or VS Code.


2. Clone the repository:
    ```
    git clone https://github.com/stevenlagoy/MandatePresidencyGame.git
    cd MandatePresidencyGame
    ```

3. Use the Gradle Wrapper
    This project uses the **Gradel Wrapper**, so you do not need to install Gradle manually.

    macOS/Linux:
    ```bash
    ./gradlew build
    ```
    Windows (CMD / Powershell):
    ```cmd
    gradlew.bat build
    ```
    ```powershell
    .\gradlew build
    ```
    This downloads dependencies and verifies the project builds.

    If you encounter `Unsupported class file major version`, you are using the wrong Java version-- switch to JDK 17.

4. Run the Desktop Version
    The desktop launcher is the primary and suggested way to run the game during development:

    macOS/Linux:
    ```bash
    ./gradlew lwjgl3:run
    ```
    Windows:
    ```cmd
    gradlew.bat lwjgl3:run
    ```

## How to Play

1. **Character Creation**: Start by customizing your candidate, choosing demographics, background, education, and political leanings.

2. **Early Campaign**: Begin by building name recognition, raising funds, and establishing your platform.

3. **Primary Season**: Compete against other candidates in your party through state primaries and caucuses.

4. **Party Convention**: Secure your party's nomination at the national convention.

5. **General Election**: Campaign across the country, participate in debates, and work to win the Electoral College.

6. **Election Day**: Watch the results come in state by state and see if your strategy paid off.

## Game Structure

The game is organized around several key phases:

- **Pre-Campaign Phase**: Build your candidate and initial support
- **Primary Phase**: Win your party's nomination
- **General Election Phase**: Campaign against the opposing party's candidate
- **Election Day**: The final results
- **Post-Election**: If you win, prepare for inauguration

## Credits

Developed solely by Steven LaGoy from 2022-2026
For attributions and other acknowledgements, see `ATTRIBUTIONS.md`

## Contact

For bug reports or feature requests, please open an issue on the GitHub repository, or contact Steven LaGoy at stevenlagoy@gmail.com

---

# MandatePresidencyGame

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
