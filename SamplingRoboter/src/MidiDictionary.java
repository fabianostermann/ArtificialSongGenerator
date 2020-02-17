/*
 * Borrowed from package org.jfugue.midi;
 * 
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2014 David Koelle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//

import java.util.HashMap;
import java.util.Map;

public class MidiDictionary {
	public static Map<Byte, String> INSTRUMENT_BYTE_TO_STRING = new HashMap<Byte, String>() {{
		put((byte)0, "Piano");
		put((byte)1, "Bright_Acoustic");
		put((byte)2, "Electric_Grand");
		put((byte)3, "Honkey_Tonk");
		put((byte)4, "Electric_Piano");
		put((byte)5, "Electric_Piano_2");
		put((byte)6, "Harpsichord");
		put((byte)7, "Clavinet");
		put((byte)8, "Celesta");
		put((byte)9, "Glockenspiel");

		put((byte)10, "Music_Box");
		put((byte)11, "Vibraphone");
		put((byte)12, "Marimba");
		put((byte)13, "Xylophone");
		put((byte)14, "Tubular_Bells");
		put((byte)15, "Dulcimer");
		put((byte)16, "Drawbar_Organ");
		put((byte)17, "Percussive_Organ");
		put((byte)18, "Rock_Organ");
		put((byte)19, "Church_Organ");

		put((byte)20, "Reed_Organ");
		put((byte)21, "Accordian");
		put((byte)22, "Harmonica");
		put((byte)23, "Tango_Accordian");
		put((byte)24, "Guitar");
		put((byte)25, "Steel_String_Guitar");
		put((byte)26, "Electric_Jazz_Guitar");
		put((byte)27, "Electric_Clean_Guitar");
		put((byte)28, "Electric_Muted_Guitar");
		put((byte)29, "Overdriven_Guitar");
		    	        
		put((byte)30, "Distortion_Guitar");
		put((byte)31, "Guitar_Harmonics");
		put((byte)32, "Acoustic_Bass");
		put((byte)33, "Electric_Bass_Finger");
		put((byte)34, "Electric_Bass_Pick");
		put((byte)35, "Fretless_Bass");
		put((byte)36, "Slap_Bass_1");
		put((byte)37, "Slap_Bass_2");
		put((byte)38, "Synth_Bass_1");
		put((byte)39, "Synth_Bass_2");

		put((byte)40, "Violin");
		put((byte)41, "Viola");
		put((byte)42, "Cello");
		put((byte)43, "Contrabass");
		put((byte)44, "Tremolo_Strings");
		put((byte)45, "Pizzicato_Strings");
		put((byte)46, "Orchestral_Strings");
		put((byte)47, "Timpani");
		put((byte)48, "String_Ensemble_1");
		put((byte)49, "String_Ensemble_2");

		put((byte)50, "Synth_Strings_1");
		put((byte)51, "Synth_Strings_2");
		put((byte)52, "Choir_Aahs");
		put((byte)53, "Voice_Oohs");
		put((byte)54, "Synth_Voice");
		put((byte)55, "Orchestra_Hit");
		put((byte)56, "Trumpet");
		put((byte)57, "Trombone");
		put((byte)58, "Tuba");
		put((byte)59, "Muted_Trumpet");

		put((byte)60, "French_Horn");
		put((byte)61, "Brass_Section");
		put((byte)62, "Synth_Brass_1");
		put((byte)63, "Synth_Brass_2");
		put((byte)64, "Soprano_Sax");
		put((byte)65, "Alto_Sax");
		put((byte)66, "Tenor_Sax");
		put((byte)67, "Baritone_Sax");
		put((byte)68, "Oboe");
		put((byte)69, "English_Horn");

		put((byte)70, "Bassoon");
		put((byte)71, "Clarinet");
		put((byte)72, "Piccolo");
		put((byte)73, "Flute");
		put((byte)74, "Recorder");
		put((byte)75, "Pan_Flute");
		put((byte)76, "Blown_Bottle");
		put((byte)77, "Skakuhachi");
		put((byte)78, "Whistle");
		put((byte)79, "Ocarina");

		put((byte)80, "Square");
		put((byte)81, "Sawtooth");
		put((byte)82, "Calliope");
		put((byte)83, "Chiff");
		put((byte)84, "Charang");
		put((byte)85, "Voice");
		put((byte)86, "Fifths");
		put((byte)87, "Bass_Lead");
		put((byte)88, "New_Age");
		put((byte)89, "Warm");

		put((byte)90, "Poly_Synth");
		put((byte)91, "Choir");
		put((byte)92, "Bowed");
		put((byte)93, "Metallic");
		put((byte)94, "Halo");
		put((byte)95, "Sweep");
		put((byte)96, "Rain");
		put((byte)97, "Soundtrack");
		put((byte)98, "Crystal");
		put((byte)99, "Atmosphere");

		put((byte)100, "Brightness");
		put((byte)101, "Goblins");
		put((byte)102, "Echoes");
		put((byte)103, "Sci_Fi");
		put((byte)104, "Sitar");
		put((byte)105, "Banjo");
		put((byte)106, "Shamisen");
		put((byte)107, "Koto");
		put((byte)108, "Kalimba");
		put((byte)109, "Bagpipe");

		put((byte)110, "Fiddle");
		put((byte)111, "Shanai");
		put((byte)112, "Tinkle_Bell");
		put((byte)113, "Agogo");
		put((byte)114, "Steel_Drums");
		put((byte)115, "Woodblock");
		put((byte)116, "Taiko_Drum");
		put((byte)117, "Melodic_Tom");
		put((byte)118, "Synth_Drum");
		put((byte)119, "Reverse_Cymbal");

		put((byte)120, "Guitar_Fret_Noise");
		put((byte)121, "Breath_Noise");
		put((byte)122, "Seashore");
		put((byte)123, "Bird_Tweet");
		put((byte)124, "Telephone_Ring");
		put((byte)125, "Helicopter");
		put((byte)126, "Applause");
		put((byte)127, "Gunshot");	
	}};
	
	public static Map<String, Byte> INSTRUMENT_STRING_TO_BYTE = new HashMap<String, Byte>() {{
		put("PIANO", (byte)0);
		put("BRIGHT_ACOUSTIC", (byte)1);
		put("ELECTRIC_GRAND", (byte)2);
		put("HONKEY_TONK", (byte)3);
		put("ELECTRIC_PIANO", (byte)4);
		put("ELECTRIC_PIANO_2", (byte)5);
		put("HARPSICHORD", (byte)6);
		put("CLAVINET", (byte)7);
		put("CELESTA", (byte)8);
		put("GLOCKENSPIEL", (byte)9);

		put("MUSIC_BOX", (byte)10);
		put("VIBRAPHONE", (byte)11);
		put("MARIMBA", (byte)12);
		put("XYLOPHONE", (byte)13);
		put("TUBULAR_BELLS", (byte)14);
		put("DULCIMER", (byte)15);
		put("DRAWBAR_ORGAN", (byte)16);
		put("PERCUSSIVE_ORGAN", (byte)17);
		put("ROCK_ORGAN", (byte)18);
		put("CHURCH_ORGAN", (byte)19);

		put("REED_ORGAN", (byte)20);
		put("ACCORDIAN", (byte)21);
		put("HARMONICA", (byte)22);
		put("TANGO_ACCORDIAN", (byte)23);
		put("GUITAR", (byte)24);
		put("STEEL_STRING_GUITAR", (byte)25);
		put("ELECTRIC_JAZZ_GUITAR", (byte)26);
		put("ELECTRIC_CLEAN_GUITAR", (byte)27);
		put("ELECTRIC_MUTED_GUITAR", (byte)28);
		put("OVERDRIVEN_GUITAR", (byte)29);
		    	        
		put("DISTORTION_GUITAR", (byte)30);
		put("GUITAR_HARMONICS", (byte)31);
		put("ACOUSTIC_BASS", (byte)32);
		put("ELECTRIC_BASS_FINGER", (byte)33);
		put("ELECTRIC_BASS_PICK", (byte)34);
		put("FRETLESS_BASS", (byte)35);
		put("SLAP_BASS_1", (byte)36);
		put("SLAP_BASS_2", (byte)37);
		put("SYNTH_BASS_1", (byte)38);
		put("SYNTH_BASS_2", (byte)39);

		put("VIOLIN", (byte)40);
		put("VIOLA", (byte)41);
		put("CELLO", (byte)42);
		put("CONTRABASS", (byte)43);
		put("TREMOLO_STRINGS", (byte)44);
		put("PIZZICATO_STRINGS", (byte)45);
		put("ORCHESTRAL_STRINGS", (byte)46);
		put("TIMPANI", (byte)47);
		put("STRING_ENSEMBLE_1", (byte)48);
		put("STRING_ENSEMBLE_2", (byte)49);

		put("SYNTH_STRINGS_1", (byte)50);
		put("SYNTH_STRINGS_2", (byte)51);
		put("CHOIR_AAHS", (byte)52);
		put("VOICE_OOHS", (byte)53);
		put("SYNTH_VOICE", (byte)54);
		put("ORCHESTRA_HIT", (byte)55);
		put("TRUMPET", (byte)56);
		put("TROMBONE", (byte)57);
		put("TUBA", (byte)58);
		put("MUTED_TRUMPET", (byte)59);

		put("FRENCH_HORN", (byte)60);
		put("BRASS_SECTION", (byte)61);
		put("SYNTH_BRASS_1", (byte)62);
		put("SYNTH_BRASS_2", (byte)63);
		put("SOPRANO_SAX", (byte)64);
		put("ALTO_SAX", (byte)65);
		put("TENOR_SAX", (byte)66);
		put("BARITONE_SAX", (byte)67);
		put("OBOE", (byte)68);
		put("ENGLISH_HORN", (byte)69);

		put("BASSOON", (byte)70);
		put("CLARINET", (byte)71);
		put("PICCOLO", (byte)72);
		put("FLUTE", (byte)73);
		put("RECORDER", (byte)74);
		put("PAN_FLUTE", (byte)75);
		put("BLOWN_BOTTLE", (byte)76);
		put("SKAKUHACHI", (byte)77);
		put("WHISTLE", (byte)78);
		put("OCARINA", (byte)79);

		put("SQUARE", (byte)80);
		put("SAWTOOTH", (byte)81);
		put("CALLIOPE", (byte)82);
		put("CHIFF", (byte)83);
		put("CHARANG", (byte)84);
		put("VOICE", (byte)85);
		put("FIFTHS", (byte)86);
		put("BASS_LEAD", (byte)87);
		put("NEW_AGE", (byte)88);
		put("WARM", (byte)89);

		put("POLY_SYNTH", (byte)90);
		put("CHOIR", (byte)91);
		put("BOWED", (byte)92);
		put("METALLIC", (byte)93);
		put("HALO", (byte)94);
		put("SWEEP", (byte)95);
		put("RAIN", (byte)96);
		put("SOUNDTRACK", (byte)97);
		put("CRYSTAL", (byte)98);
		put("ATMOSPHERE", (byte)99);

		put("BRIGHTNESS", (byte)100);
		put("GOBLINS", (byte)101);
		put("ECHOES", (byte)102);
		put("SCI_FI", (byte)103);
		put("SITAR", (byte)104);
		put("BANJO", (byte)105);
		put("SHAMISEN", (byte)106);
		put("KOTO", (byte)107);
		put("KALIMBA", (byte)108);
		put("BAGPIPE", (byte)109);

		put("FIDDLE", (byte)110);
		put("SHANAI", (byte)111);
		put("TINKLE_BELL", (byte)112);
		put("AGOGO", (byte)113);
		put("STEEL_DRUMS", (byte)114);
		put("WOODBLOCK", (byte)115);
		put("TAIKO_DRUM", (byte)116);
		put("MELODIC_TOM", (byte)117);
		put("SYNTH_DRUM", (byte)118);
		put("REVERSE_CYMBAL", (byte)119);

		put("GUITAR_FRET_NOISE", (byte)120);
		put("BREATH_NOISE", (byte)121);
		put("SEASHORE", (byte)122);
		put("BIRD_TWEET", (byte)123);
		put("TELEPHONE_RING", (byte)124);
		put("HELICOPTER", (byte)125);
		put("APPLAUSE", (byte)126);
		put("GUNSHOT", (byte)127);
		
	}};

	public static Map<Integer, String> TEMPO_INT_TO_STRING = new HashMap<Integer, String>() {{
		put(40, "GRAVE");
		put(45, "LARGO");
		put(50, "LARGHETTO");
		put(55, "LENTO");
		put(60, "ADAGIO");
		put(65, "ADAGIETTO");
		put(70, "ANDANTE");
		put(80, "ANDANTINO");
		put(95, "MODERATO");
		put(110, "ALLEGRETTO");
		put(120, "ALLEGRO");
		put(145, "VIVACE");
		put(180, "PRESTO");
		put(220, "PRETISSIMO");
	}};

	public static Map<String, Integer> TEMPO_STRING_TO_INT = new HashMap<String, Integer>() {{
		put("GRAVE", 40);
		put("LARGO", 45);
		put("LARGHETTO", 50);
		put("LENTO", 55);
		put("ADAGIO", 60);
		put("ADAGIETTO", 65);
		put("ANDANTE", 70);
		put("ANDANTINO", 80);
		put("MODERATO", 95);
		put("ALLEGRETTO", 110);
		put("ALLEGRO", 120);
		put("VIVACE", 145);
		put("PRESTO", 180);
		put("PRETISSIMO", 220);
	}};
	
	public static Map<Integer, String> CONTROLLER_INT_TO_STRING = new HashMap<Integer, String>() {{
	    put(0, "BANK_SELECT_COARSE");
	    put(1, "MOD_WHEEL_COARSE");
	    put(2, "BREATH_COARSE");
	    put(4, "FOOT_PEDAL_COARSE");
	    put(5, "PORTAMENTO_TIME_COARSE");
	    put(6, "DATA_ENTRY_COARSE");
	    put(7, "VOLUME_COARSE");
	    put(8, "BALANCE_COARSE");
	    put(10, "PAN_POSITION_COARSE");

	    put(11, "EXPRESSION_COARSE");
	    put(12, "EFFECT_CONTROL_1_COARSE");
	    put(13, "EFFECT_CONTROL_2_COARSE");
	    put(16, "SLIDER_1");
	    put(17, "SLIDER_2");
	    put(18, "SLIDER_3");
	    put(19, "SLIDER_4");

	    put(32, "BANK_SELECT_FINE");
	    put(33, "MOD_WHEEL_FINE");
	    put(34, "BREATH_FINE");
	    put(36, "FOOT_PEDAL_FINE");
	    put(37, "PORTAMENTO_TIME_FINE");
	    put(38, "DATA_ENTRY_FINE");
	    put(39, "VOLUME_FINE");
	    put(40, "BALANCE_FINE");
	    put(42, "PAN_POSITION_FINE");
	    put(43, "EXPRESSION_FINE");
	    put(44, "EFFECT_CONTROL_1_FINE");
	    put(45, "EFFECT_CONTROL_2_FINE");

	    put(64, "HOLD_PEDAL");
	    put(65, "PORTAMENTO");
	    put(66, "SUSTENUTO_PEDAL");
	    put(67, "SOFT_PEDAL");
	    put(68, "LEGATO_PEDAL");
	    put(69, "HOLD_2_PEDAL");

	    put(70, "SOUND_VARIATION");
	    put(71, "SOUND_TIMBRE");
	    put(72, "SOUND_RELEASE_TIME");
	    put(73, "SOUND_ATTACK_TIME");
	    put(74, "SOUND_BRIGHTNESS");
	    put(75, "SOUND_CONTROL_6");
	    put(76, "SOUND_CONTROL_7");
	    put(77, "SOUND_CONTROL_8");
	    put(78, "SOUND_CONTROL_9");
	    put(79, "SOUND_CONTROL_10");

	    put(80, "GENERAL_PURPOSE_BUTTON_1");
	    put(81, "GENERAL_PURPOSE_BUTTON_2");
	    put(82, "GENERAL_PURPOSE_BUTTON_3");
	    put(83, "GENERAL_PURPOSE_BUTTON_4");

	    put(91, "EFFECTS_LEVEL");
	    put(92, "TREMULO_LEVEL");
	    put(93, "CHORUS_LEVEL");
	    put(94, "CELESTE_LEVEL");
	    put(95, "PHASER_LEVEL");

	    put(96, "DATA_BUTTON_INCREMENT");
	    put(97, "DATA_BUTTON_DECREMENT");

	    put(98, "NON_REGISTERED_COARSE");
	    put(99, "NON_REGISTERED_FINE");
	    put(100, "REGISTERED_COARSE");
	    put(101, "REGISTERED_FINE");

	    put(120, "ALL_SOUND_OFF");
	    put(121, "ALL_CONTROLLERS_OFF");
	    put(122, "LOCAL_KEYBOARD");
	    put(123, "ALL_NOTES_OFF");
	    put(124, "OMNI_MODE_OFF");
	    put(125, "OMNI_MODE_ON");
	    put(126, "MONO_OPERATION");
	    put(127, "POLY_OPERATION");

	    //
	    // Combined Controller names
	    // (index = coarse_controller_index * 128 + fine_controller_index)
	    //
	    put(16383, "BANK_SELECT");
	    put(161, "MOD_WHEEL");
	    put(290, "BREATH");
	    put(548, "FOOT_PEDAL");
	    put(677, "PORTAMENTO_TIME");
	    put(806, "DATA_ENTRY");
	    put(935, "VOLUME");
	    put(1064, "BALANCE");
	    put(1322, "PAN_POSITION");
	    put(1451, "EXPRESSION");
	    put(1580, "EFFECT_CONTROL_1");
	    put(1709, "EFFECT_CONTROL_2");
	    put(12770, "NON_REGISTERED");
	    put(13028, "REGISTERED");

	    //
	    // Values for controllers
	    //
	    put(127, "ON");
	    put(0, "OFF");
	    put(64, "DEFAULT");	    
	}};
	
	public static Map<String, Integer> CONTROLLER_STRING_TO_INT = new HashMap<String, Integer>() {{
	    put("BANK_SELECT_COARSE", 0);
	    put("MOD_WHEEL_COARSE", 1);
	    put("BREATH_COARSE", 2);
	    put("FOOT_PEDAL_COARSE", 4);
	    put("PORTAMENTO_TIME_COARSE", 5);
	    put("DATA_ENTRY_COARSE", 6);
	    put("VOLUME_COARSE", 7);
	    put("BALANCE_COARSE", 8);
	    put("PAN_POSITION_COARSE", 10);

	    put("EXPRESSION_COARSE", 11);
	    put("EFFECT_CONTROL_1_COARSE", 12);
	    put("EFFECT_CONTROL_2_COARSE", 13);
	    put("SLIDER_1", 16);
	    put("SLIDER_2", 17);
	    put("SLIDER_3", 18);
	    put("SLIDER_4", 19);

	    put("BANK_SELECT_FINE", 32);
	    put("MOD_WHEEL_FINE", 33);
	    put("BREATH_FINE", 34);
	    put("FOOT_PEDAL_FINE", 36);
	    put("PORTAMENTO_TIME_FINE", 37);
	    put("DATA_ENTRY_FINE", 38);
	    put("VOLUME_FINE", 39);
	    put("BALANCE_FINE", 40);
	    put("PAN_POSITION_FINE", 42);
	    put("EXPRESSION_FINE", 43);
	    put("EFFECT_CONTROL_1_FINE", 44);
	    put("EFFECT_CONTROL_2_FINE", 45);

	    put("HOLD_PEDAL", 64);
	    put("PORTAMENTO", 65);
	    put("SUSTENUTO_PEDAL", 66);
	    put("SOFT_PEDAL", 67);
	    put("LEGATO_PEDAL", 68);
	    put("HOLD_2_PEDAL", 69);

	    put("SOUND_VARIATION", 70);
	    put("SOUND_TIMBRE", 71);
	    put("SOUND_RELEASE_TIME", 72);
	    put("SOUND_ATTACK_TIME", 73);
	    put("SOUND_BRIGHTNESS", 74);
	    put("SOUND_CONTROL_6", 75);
	    put("SOUND_CONTROL_7", 76);
	    put("SOUND_CONTROL_8", 77);
	    put("SOUND_CONTROL_9", 78);
	    put("SOUND_CONTROL_10", 79);

	    put("GENERAL_PURPOSE_BUTTON_1", 80);
	    put("GENERAL_PURPOSE_BUTTON_2", 81);
	    put("GENERAL_PURPOSE_BUTTON_3", 82);
	    put("GENERAL_PURPOSE_BUTTON_4", 83);

	    put("EFFECTS_LEVEL", 91);
	    put("TREMULO_LEVEL", 92);
	    put("CHORUS_LEVEL", 93);
	    put("CELESTE_LEVEL", 94);
	    put("PHASER_LEVEL", 95);

	    put("DATA_BUTTON_INCREMENT", 96);
	    put("DATA_BUTTON_DECREMENT", 97);

	    put("NON_REGISTERED_COARSE", 98);
	    put("NON_REGISTERED_FINE", 99);
	    put("REGISTERED_COARSE", 100);
	    put("REGISTERED_FINE", 101);

	    put("ALL_SOUND_OFF", 120);
	    put("ALL_CONTROLLERS_OFF", 121);
	    put("LOCAL_KEYBOARD", 122);
	    put("ALL_NOTES_OFF", 123);
	    put("OMNI_MODE_OFF", 124);
	    put("OMNI_MODE_ON", 125);
	    put("MONO_OPERATION", 126);
	    put("POLY_OPERATION", 127);

	    //
	    // Combined Controller names
	    // (index = coarse_controller_index * 128 + fine_controller_index)
	    //
	    put("BANK_SELECT", 16383);
	    put("MOD_WHEEL", 161);
	    put("BREATH", 290);
	    put("FOOT_PEDAL", 548);
	    put("PORTAMENTO_TIME", 677);
	    put("DATA_ENTRY", 806);
	    put("VOLUME", 935);
	    put("BALANCE", 1064);
	    put("PAN_POSITION", 1322);
	    put("EXPRESSION", 1451);
	    put("EFFECT_CONTROL_1", 1580);
	    put("EFFECT_CONTROL_2", 1709);
	    put("NON_REGISTERED", 12770);
	    put("REGISTERED", 13028);

	    //
	    // Values for controllers
	    //
	    put("ON", 127);
	    put("OFF", 0);
	    put("DEFAULT", 64);
	}};
	
}
