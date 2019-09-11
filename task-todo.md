# Artificial Song Generator

## Implementiert:

1. a) 3 Akkordfolgen generien (I-VI), zufällige Tonart
1. b) Tempo Würfeln [80-160]
2. ) Melodie für jede Folge generieren, passend zur Tonart
3. ) Form von 8 Blöcken würfeln
4. ) Abspielen mit Midi-Instrumenten: Melodie, Akkorde
5. ) Formpart, Instrumente und Tempo zu Zeit in s annotieren (arff)
6. ) einfache drums backbeat grooves mit fills
7. ) config file ist auslesbar
8. ) unterschiedliche Instrumente pro Songpart (pro Buchstabe immer gleich)

## Todo:

[19/09/09]
* add Bass/Arpeggios/Drums zur Annotation
* auftauchen der Funktion mit Wahrscheinlichkeit (auch drums)
* Funktionen pro Segment ausdünnen (Additive Verteilung)
* memoize Instrument to Function
* Polyphoniegrad der Instrumente annotieren


## Make Script erstellen
1. JFugue herunterladen (keine Lib im eigenen Repo) [done]
2. jar file builden automatisch (Manifest-File)
3. hinweis auf fluid-synth und vlc-nox (mit option das direkt zu laden)

