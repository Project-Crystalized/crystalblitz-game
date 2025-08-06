# Crystalized: Crystal Blitz
Recreation of TubNet Crystal Rush

# How to use
Crystal Blitz is currently built for 1.21.6 Paper, This plugin may or may not work on newer or older versions, use incompatible versions at your own risk.

Dependencies: <br>
[Crystalized Essentials](https://github.com/Project-Crystalized/crystalized-essentials) <br>
[Crystalized Lobby plugin](https://github.com/Project-Crystalized/lobby_plugin) <br>
Floodgate <br>

Commands: <br>
`/crystalblitz start (type)` - manually start the game, arguments will be tab complete, argument not needed for custom teams. <br>
`/crystalblitz end` - manually end the game <br>

## Building Instructions
Make sure you've installed JDK 21 from [here](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html). <br>

Configuring Lobby Plugin for Crystal Blitz:
1. Crystal Blitz Depends on the Lobby Plugin (linked above), Get the Lobby Plugin's source and open a terminal/cmd in the folder. <br>
2. type `./gradlew publishToMavenLocal` (Windows) or `gradlew publishToMavenLocal` (Linux) and wait for that to finish.
3. You are ready to start building Crystal Blitz

Building Knockoff:
1. Open a Terminal or Command Prompt in the root directory of the Crystal Blitz repo
2. Type `./gradlew build` (Windows) or `gradlew build` (Linux)
3. Jar file will be in `build/libs/`

Notes:
1. You will need to enable passive mode in the lobby plugin's config
2. You may need a Crystal Blitz compatible map for the plugin to start and work properly.
