# TODOs

*TODOs AUFRÄUMEN!*

## Make Script erstellen
- Hinweis auf fluid-synth und vlc-nox (mit option das direkt zu laden)

# Artificial Song Generator

## Ebeling Besprechung 2020-02-18

- **DONE** Tonart zur Annotation hinzufügen
- **DONE** memoize-keys/tempo in relation (Abstufung Quinte-Quart-Terz-zufall, halbe-doppelt-triole-zufall)
- **DONE** Zeitliche Länge maßgebend (anstatt Anzahl Formteile) -> alle Songs 2:30--3:00min
- **DONE** Schlusston Relation (eher Grundton, eher Halbtonannäherung, Intervalrahmen (Anfang-Ende))
- **DONE** *IMPROVABLE* Harmoniefolge Muster einbeziehen (Tonart: letzter Akkord)

- **LATER** Formenlehre! (Modulations-Dominanten, ...)
- **LATER** Segmentgrenzen: Melodie und Akkorde zusammen generieren
- **LATER** Akzente und Betonungen!

- **LATER** 7te Stufe fundiert ausschließen
- **LATER** Variationen der Songparts (A', A'', B', ...)

# Onset Annotator

-

# Sampling Roboter2

-

# instrument rework

- Instrumente.txt -> aufteilen in nehmen und nicht-nehmen, dann an Igor schicken, dann einbauen

- **DONE**  SongGenerator bekommt **Synthesizer-Instrument-Keywords** plus Demo-Anweisung (Midi-Instrument)
- **DONE** generiert für jedes Instrument einen Midifile (Omni-Kanal) plus Demo-File (mit Midi-Instrumenten)
- **DONE** Annotator und Generator fusionieren für einheitliche Arff-Files und Instrumenten-Pool
- **DONE** OnsetAnnotator annotiert Events aller **Synthesizer-Instrument-Keywords**
- **DONE** eventuell muss die Range jetzt mit einbezogen werden! (Programmieraufwand, erstmal nur check einbauen in onsetannotator)
- *TODO* Range for BassLine

- **DONE** SamplingRoboter nutzt **Synthesizer-Instrument-Keywords** an Dateinamen um den korrekten Synthesizer zu nutzen\
	Jedes Instrument einzeln (zu bevorzugen wegen Memory-CPU-Last)\
- *TODO* padding to same length with sox/soxi after sampling with roboter


# future ideas to get greater variation in the generated music

- write a bunch of music generators (SongPartElements) with different dependencies (one need chords, one need melody, one need both, one need none, etc.)
- then generate music elements using random generator that suits the dependency tree of elements already created.
- in that manor build multiple songparts
- finally order the songparts by analysing the music of each
 



