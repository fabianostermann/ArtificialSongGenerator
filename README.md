# ArtificialSongGenerator Project

The project consists of two subprograms that allow generation of a artificial pop song database with realistic samples

### Artificial Song Generator

Creates music pieces (in midi file format) based on intelligent random choices and segmentation in different song parts with various characteristics.
Annotates segments information and midi note events of the generated artificial song.

### Sampling Roboter

Playes back an artificial songs midifile trackwise to be able to record the different instrument parts via a loopback audiocable.

## Compile (via Ant) and easy use (mainly bash-scripts)

The main directory holds an ant build file: Use *pack* to generate an executable jar (including downloading JFugue lib).
The main build file will call the three build files of the subprojects 'ArtificialSongGenerator/' and 'SamplingRoboter/'.\
Then use bash script 'Sampling/generate-songs' under Linux to create a database based on the *ArtificialSongGenerator* routine.\
This script is currently capable of automatically creating wavs and mp3s from midis using cvlc (VLC for console) and fluidsynth plugin.\
*NOTE: VLC synth support is to be dropped in this version*\
\
For more usage information:\
**All jars and scripts have usage and help files included via '--help' option**\
