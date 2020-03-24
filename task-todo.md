# TODOs

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

- overdriven guitar (mit SoX effect) ??

# instrument rework

- Instrumente.txt -> aufteilen in nehmen und nicht-nehmen, dann an Igor schicken

- SongGenerator bekommt **Synthesizer-Instrument-Keywords** plus Demo-Anweisung (Midi-Instrument)
- generiert für jedes Instrument einen Midifile (Omni-Kanal) plus Demo-File (mit Midi-Instrumenten)
- eventuell muss die Range jetzt mit einbezogen werden! (Programmieraufwand)
- OnsetAnnotator annotiert Events aller **Synthesizer-Instrument-Keywords**
- Annotator und Generator fusionieren für einheitliche Arff-Files und Instrumenten-Pool

- Sampling nutzt **Synthesizer-Instrument-Keywords** an Dateinamen um den korrekten Synthesizer zu nutzen\
	1. Entweder mehrere MidiPorts an Kontakt-Player A1-16,B1-16,C1-16)\
	2. Oder jedes Instrument einzeln (zu bevorzugen wegen Memory-CPU-Last)\
