# ArtificialSongGenerator Project

## Compile (via Ant) and easy use (mainly bash-scripts)

The main directory provides an ant build file (`build.xml`): Use `ant pack` to generate an executable jar (including downloading the JFugue library).
The main build file will call the build file of the subproject `ArtificialSongGenerator/`.\
Then use bash script `Sampling/generate-songs` under Linux to create a database based on the *ArtificialSongGenerator* routine.\
Using Ardour or Mixbus one can then create audio using the Lua script `Sampling/mixbus_roboter.lua` therein.\
\
For more usage information:\
**All jars and scripts have usage and help files included via '--help' option**\
