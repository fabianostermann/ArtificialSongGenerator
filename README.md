# ArtificialSongGenerator Project

The project consists of three subprograms that allow generation of a artificial pop song database with realistic samples

## (1) Artificial Song Generator

Creates music pieces (in midi file format) based on intelligent random choices and segmentation in different song parts with various characteristics.

## (2) Onset Annotator

Annotates the midi note events of a generated artificial song (in midi file format).

## (3) Sampling Roboter

Playes back an artificial songs midifile trackwise to be able to record the different instrument parts via a loopback audiocable.

# Compile (via Ant) and easy use (mainly bash-scripts)

The main directory holds an ant build file: Use *pack* to generate an executable jar (including downloading JFugue lib).
The main build file will call the three build files of the subprojects 'ArtificialSongGenerator/', 'OnsetAnnotator/' and 'SamplingRoboter/'.
Then use bash script 'Sampling/generate-songs' under Linux to create a database based on the *ArtificialSongGenerator* routine.
This script is currently capable of automatically creating wavs and mp3s from midis using cvlc (VLC for console) and fluidsynth plugin.
*NOTE: VLC synth support will be dropped in the next version*
Use bash script 'Sampling/annotate-onsets' under Linux to create onset annotation files for a database folder.

For more usage information:
**All jars and scripts have usage and help files included via '--help' option**
