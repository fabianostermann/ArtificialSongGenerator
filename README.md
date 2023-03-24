# ArtificialSongGenerator Project

The ArtificialSongGenerator tool was used to compile the open research dataset [Artificial Audio Multitracks (AAM)](https://zenodo.org/record/5794629), that currently consists of 3000 artificial songs with rich annotations.

Please find more details on the project in the accompying [paper](https://doi.org/10.1186/s13636-023-00278-7), that was published open-access in [EURASIP Journal on Audio, Speech, and Music Processing](https://doi.org/10.1186/s13636-023-00278-7).

## Create MIDIs with the JAR-executable

For creating MIDI, download the latest [release](TODO) and run:\
`java -jar AtificialSongGenerator.jar`\
The tool will generate a bunch of midi files based on the [standard configuration](https://github.com/fabianostermann/ArtificialSongGenerator/blob/master/ArtificialSongGenerator/src/main/Config.java).

You can specify your own settings by running:\
`java -jar ArtificialSongGenerator.jar --create-dummy`\
The config dummy file `dummy.conf` will then be created, which you can modify and feed back in to the generator by running:\
`java -jar ArtificialSongGenerator.jar --config=dummy.conf`

All options and full usage info is provided by:\
`java -jar ArtificialSongGenerator.jar --help`

## Compile from source with Ant

The main directory provides an ant build file (`build.xml`).\
Use `ant pack` to generate an executable jar (including downloading the JFugue library).\
The main build file will call the build file of the subproject `ArtificialSongGenerator/`.\
Then use bash script `Sampling/generate-songs` under Linux to create a whole dataset of songs based on the *ArtificialSongGenerator* routine.

## Generate audio

Using Ardour or Mixbus one can then create audio using the Lua script `Sampling/mixbus_roboter.lua` therein.\
Currently, the routine lacks full automization. This is planned to be accomplished next.  

## General information and help

For more usage information:\
**All jars and scripts have usage and help files included via '--help' option**\

In case you need any more help, find a bug or want to raise an issue, don't hesitate to contact me!
