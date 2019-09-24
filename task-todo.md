# Artificial Song Generator

## Implementiert:

1. ) 3 Akkordfolgen generien (I-VI), zufällige Tonart
1. ) Tempo Würfeln [80-160]
1. ) Melodie für jede Folge generieren, passend zur Tonart
1. ) Form von 8 Blöcken würfeln
1. ) Abspielen mit Midi-Instrumenten: Melodie, Akkorde
1. ) Formpart, Instrumente und Tempo zu Zeit in s annotieren (arff)
1. ) einfache drums backbeat grooves mit fills
1. ) config file ist auslesbar
1. ) unterschiedliche Instrumente pro Songpart (pro Buchstabe immer gleich)
1. ) Bass/Arpeggios/Drums in Annotation
1. ) Polyphoniegrad der Instrumente annotieren

## Todo:

[19/09/09]
* auftauchen der Funktion mit Wahrscheinlichkeit (auch drums)
* Funktionen pro Segment ausdünnen (Additive Verteilung)
* memoize Instrument to Function (zerstört 9.) (BUG!!)


## Make Script erstellen
1. JFugue herunterladen (keine Lib im eigenen Repo) [done]
2. jar file builden automatisch (Manifest-File)
3. hinweis auf fluid-synth und vlc-nox (mit option das direkt zu laden)

