package test;

import java.util.Arrays;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Key;

import asglib.MidiDictionary;
import main.Config;
import parts.ArpeggioSequence;
import parts.BassLine;
import parts.ChordSequenceRanged;
import parts.MelodyBow;





/**
 * DEMO
 * This file shows how a melody ("Eq Ch. | Eq Ch. | Dq Eq Dq Cq") can be created as Voice 0.
 * And how a chord progression ("I IV V I") can be called back as whole notes ("$!w") simultaneously in Voice 1.
 * Melody is played by Flute, chords by Piano.
 * Player playback can easily be stored in a midi file (see MidiFileManagerDemo).
 *
 */

public class Test {
  public static void main(String[] args) {
//    Pattern p1 = new Pattern("E6i E6i C6h. | Es Es Es Es Ch. | Dq Eq Dq Cq").setVoice(0).setInstrument("Flute");
////    Pattern p2 = new Pattern("CmajW  | FmajW  | GmajQQQ  CmajQ").setVoice(1).setInstrument("Piano");
//    ChordProgression cp = new ChordProgression("I IV V I").setKey("C");
////    Pattern p3 = cp.allChordsAs("$0w $1w $2qqq $0q").getPattern().setVoice(1).setInstrument("Piano");
//    Pattern p3 = cp.eachChordAs("$!w").getPattern().setVoice(0).setInstrument("Piano");
//    Player player = new Player();
//    
//    Pattern overallPattern = new Pattern(p1, p3);
//    player.play(overallPattern);
    
//    StaccatoParser parser = new StaccatoParser();
//    DiagnosticParserListener dpl = new DiagnosticParserListener();
//    parser.addParserListener(dpl);
//    Pattern pattern = new Pattern("KEY:Cmaj B Bn Bb R  KEY:FMaj B Bn Bb");
//    parser.parse(pattern);
//    Player player = new Player();
//    player.play(pattern);
	  
//	        Rhythm rhythm = new Rhythm()
//	          .addLayer("O..oO...O..oOO..") // This is Layer 0
//	          .addLayer("..S...S...S...S.")
//	          .addLayer("````````````````")
//	          .addLayer("...............+") // This is Layer 3
//	          .addOneTimeAltLayer(3, 3, "...+...+...+...+") // Replace Layer 3 with this string on the 4th (count from 0) measure
//	          .setLength(4); // Set the length of the rhythm to 4 measures
//	        new Player().play(rhythm.getPattern().repeat(2)); // Play 2 instances of the 4-measure-long rhythm
	    
//	        ChordProgression cp = new ChordProgression("I IV V");
//	        Player player = new Player();
//	        player.play(cp.eachChordAs("$0q $1q $2q Rq"));
//	        player.play(cp.allChordsAs("$0q $0q $0q $0q $1q $1q $2q $0q"));
//	        player.play(cp.allChordsAs("$0 $0 $0 $0 $1 $1 $2 $0").eachChordAs("$0h"));
	  
//      Player player = new Player();
//      int length = 4;
//      Config.loadDefaults();
//      Key key = Config.GET.randomKey();
//      int tempo = Config.GET.randomTempo();
//      System.out.println(key.getKeySignature()+", "+tempo+"bpm");
//      ChordSequenceRanged chords = new ChordSequenceRanged(key, length);
//      Pattern melody = new MelodyBow(key, length, chords.getChords()).getPattern();
//      Rhythm drums = new Rhythm("O...O...");
//      BassLine bass = BassLine.newRandomBassLine(chords.getChords());
//      ArpeggioSequence arpSeq = ArpeggioSequence.newRandomArpeggio(chords.getChords());
//      Pattern song = new Pattern();
//      song.add(melody.setInstrument("Trumpet").setVoice(0));
//      song.add(chords.getPattern().setInstrument("Electric_Piano").setVoice(1));
//      song.add(drums.getPattern().repeat(length));
//      song.add(bass.getPattern().setInstrument("Electric_Bass_Finger").setVoice(2));
//      song.add(arpSeq.getPattern().setInstrument("Marimba").setVoice(3));
//      System.out.println(melody);
//      System.out.println(Arrays.toString(chords.getChords()));
//      player.play(song.setTempo(tempo));

  }
}

//import org.jfugue.player.Player;
//import org.jfugue.theory.ChordProgression;
//
//public class Test {
//  public static void main(String[] args) {
//    ChordProgression cp = new ChordProgression("I IV V");
//
//    Player player = new Player();
//    player.play(cp.eachChordAs("$0q $1q $2q Rq"));
//
//    player.play(cp.allChordsAs("$0q $0q $0q $0q $1q $1q $2q $0q"));
//
//    player.play(cp.allChordsAs("$0 $0 $0 $0 $1 $1 $2 $0").eachChordAs("V0 $0s $1s $2s Rs V1 $!q"));
//  }
//}