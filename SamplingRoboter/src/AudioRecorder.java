import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

/*
 * Code model found on:
 * 
 * http://www.jsresources.org/
 * 
 * by Florian Bomers and Matthias Pfisterer
 */


public class AudioRecorder
{
	private static final SupportedFormat[]	SUPPORTED_FORMATS =
	{
		new SupportedFormat("s8",
				    AudioFormat.Encoding.PCM_SIGNED, 8, true),
		new SupportedFormat("u8",
				    AudioFormat.Encoding.PCM_UNSIGNED, 8, true),
		new SupportedFormat("s16_le",
				    AudioFormat.Encoding.PCM_SIGNED, 16, false),
		new SupportedFormat("s16_be",
				    AudioFormat.Encoding.PCM_SIGNED, 16, true),
		new SupportedFormat("u16_le",
				    AudioFormat.Encoding.PCM_UNSIGNED, 16, false),
		new SupportedFormat("u16_be",
				    AudioFormat.Encoding.PCM_UNSIGNED, 16, true),
		new SupportedFormat("s24_le",
				    AudioFormat.Encoding.PCM_SIGNED, 24, false),
		new SupportedFormat("s24_be",
				    AudioFormat.Encoding.PCM_SIGNED, 24, true),
		new SupportedFormat("u24_le",
				    AudioFormat.Encoding.PCM_UNSIGNED, 24, false),
		new SupportedFormat("u24_be",
				    AudioFormat.Encoding.PCM_UNSIGNED, 24, true),
		new SupportedFormat("s32_le",
				    AudioFormat.Encoding.PCM_SIGNED, 32, false),
		new SupportedFormat("s32_be",
				    AudioFormat.Encoding.PCM_SIGNED, 32, true),
		new SupportedFormat("u32_le",
				    AudioFormat.Encoding.PCM_UNSIGNED, 32, false),
		new SupportedFormat("u32_be",
				    AudioFormat.Encoding.PCM_UNSIGNED, 32, true),
	};

	/*
	 *  ###### Change Format Setting here: ######
	 */
	private static final int	DEFAULT_CHANNELS = 1; //Mono or Stereo
	private static final String	DEFAULT_FORMAT = "s16_le";
	private static final float	DEFAULT_RATE = 44100.0F;
	private static final AudioFileFormat.Type	DEFAULT_TARGET_TYPE = AudioFileFormat.Type.WAVE;

	private static boolean sm_bDebug = false;



	static Recorder recorder = null;
	
	
	/**
	 */
	public static void initFile(File audiofile)
	{
		/*
		 *	Parsing of command-line options takes place...
		 */
		String	strMixerName = null;
		int	nInternalBufferSize = AudioSystem.NOT_SPECIFIED;
		String	strFormat = DEFAULT_FORMAT;
		int	nChannels = DEFAULT_CHANNELS;
		float	fRate = DEFAULT_RATE;
		String	strExtension = null;
		boolean	bDirectRecording = true;

		String strFilename = audiofile.getName();
		
		if (sm_bDebug) { out("AudioRecorder.main(): output filename: " + strFilename); }
		if (strFilename == null)
		{
			printUsageAndExit();
		}

		File	outputFile = audiofile;

		/* For convenience, we have some shortcuts to set the
		   properties needed for constructing an AudioFormat.
		*/
		if (strFormat.equals("phone"))
		{
			// 8 kHz, 8 bit unsigned, mono
			fRate = 8000.0F;
			strFormat = "u8";
			nChannels = 1;
		}
		else if (strFormat.equals("radio"))
		{
			// 22.05 kHz, 16 bit signed, mono
			fRate = 22050.0F;
			strFormat = "s16_le";
			nChannels = 1;
		}
		else if (strFormat.equals("cd"))
		{
			// 44.1 kHz, 16 bit signed, stereo, little-endian
			fRate = 44100.0F;
			strFormat = "s16_le";
			nChannels = 2;
		}
		else if (strFormat.equals("dat"))
		{
			// 48 kHz, 16 bit signed, stereo, little-endian
			fRate = 48000.0F;
			strFormat = "s16_le";
			nChannels = 2;
		}

		/* Here, we are constructing the AudioFormat to use for the
		   recording. Sample rate (fRate) and number of channels
		   (nChannels) are already set safely, since they have
		   default values set at the very top. The other properties
		   needed for AudioFormat are derived from the 'format'
		   specification (strFormat).
		*/
		int	nOutputFormatIndex = -1;
		for (int i = 0; i < SUPPORTED_FORMATS.length; i++)
		{
			if (SUPPORTED_FORMATS[i].getName().equals(strFormat))
			{
				nOutputFormatIndex = i;
				break;
			}
		}
		/* If we haven't found the format (string) requested by the
		   user, we switch to a default format.
		*/
		if (nOutputFormatIndex == -1)
		{
			out("warning: output format '" + strFormat + "' not supported; using default output format '" + DEFAULT_FORMAT + "'");
			/* This is the index of "s16_le". Yes, it's
			   a bit quick & dirty to hardcode the index here.
			*/
			nOutputFormatIndex = 2;
		}
		AudioFormat.Encoding	encoding = SUPPORTED_FORMATS[nOutputFormatIndex].getEncoding();;
		int	nBitsPerSample = SUPPORTED_FORMATS[nOutputFormatIndex].getSampleSize();
		boolean	bBigEndian = SUPPORTED_FORMATS[nOutputFormatIndex].getBigEndian();
		int	nFrameSize = (nBitsPerSample / 8) * nChannels;
		AudioFormat	audioFormat = new AudioFormat(encoding, fRate, nBitsPerSample, nChannels, nFrameSize, fRate, bBigEndian);
		if (sm_bDebug) { out("AudioRecorder.main(): target audio format: " + audioFormat); }

		// extension
		//


		AudioFileFormat.Type	targetType = null;
		if (strExtension == null)
		{
			/* The user chose not to specify a target audio
			   file type explicitely. We are trying to guess
			   the type from the target file name extension.
			*/
			int	nDotPosition = strFilename.lastIndexOf('.');
			if (nDotPosition != -1)
			{
				strExtension = strFilename.substring(nDotPosition + 1);
			}
		}
		if (strExtension != null)
		{
			targetType = AudioCommon.findTargetType(strExtension);
			if (targetType == null)
			{
				out("target type '" + strExtension + "' is not supported.");
				out("using default type '" + DEFAULT_TARGET_TYPE.getExtension() + "'");
				targetType = DEFAULT_TARGET_TYPE;
			}
		}
		else
		{
			out("target type is neither specified nor can be guessed from the target file name.");
			out("using default type '" + DEFAULT_TARGET_TYPE.getExtension() + "'");
			targetType = DEFAULT_TARGET_TYPE;
		}
		if (sm_bDebug) { out("AudioRecorder.main(): target audio file format type: " + targetType); }

		TargetDataLine	targetDataLine = null;
		targetDataLine = AudioCommon.getTargetDataLine(
			strMixerName, audioFormat, nInternalBufferSize);
		if (targetDataLine == null)
		{
			out("can't get TargetDataLine, exiting.");
			System.exit(1);
		}

		if (bDirectRecording)
		{
			recorder = new DirectRecorder(
				targetDataLine,
				targetType,
				outputFile);
		}
		else
		{
			recorder = new BufferingRecorder(
				targetDataLine,
				targetType,
				outputFile);
		}
		if (sm_bDebug) { out("AudioRecorder.main(): Recorder: " + recorder); }

	}
	
	public static void startRecording() {
		recorder.start();
		//out("Recording...");
	}
	
	public static void stopRecording() {
		recorder.stopRecording();
		//out("Recording stopped.");
	}



	private static void printUsageAndExit()
	{
		out("AudioRecorder: usage:");
		out("\tjava AudioRecorder -l");
		out("\tjava AudioRecorder -L");
		out("\tjava AudioRecorder [-f <format>] [-c <numchannels>] [-r <samplingrate>] [-t <targettype>] [-M <mixername>] <soundfile>");
		System.exit(0);
	}



	/**
	 */
	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}



///////////// inner classes ////////////////////


	/**
	 */
	private static class SupportedFormat
	{
		/** The name of the format.
		 */
		private String			m_strName;

		/** The encoding of the format.
		 */
		private AudioFormat.Encoding	m_encoding;

		/** The sample size of the format.
		    This value is in bits for a single sample
		    (not for a frame).
		 */
		private int			m_nSampleSize;

		/** The endianess of the format.
		 */
		private boolean			m_bBigEndian;

		// sample size is in bits
		/** Construct a new supported format.
		    @param strName the name of the format.
		    @param encoding the encoding of the format.
		    @param nSampleSize the sample size of the format, in bits.
		    @param bBigEndian the endianess of the format.
		*/
		public SupportedFormat(String strName,
				       AudioFormat.Encoding encoding,
				       int nSampleSize,
				       boolean bBigEndian)
		{
			m_strName = strName;
			m_encoding = encoding;
			m_nSampleSize = nSampleSize;
		}

		/** Returns the name of the format.
		 */
		public String getName()
		{
			return m_strName;
		}

		/** Returns the encoding of the format.
		 */
		public AudioFormat.Encoding getEncoding()
		{
			return m_encoding;
		}

		/** Returns the sample size of the format.
		    This value is in bits.
		*/
		public int getSampleSize()
		{
			return m_nSampleSize;
		}

		/** Returns the endianess of the format.
		 */
		public boolean getBigEndian()
		{
			return m_bBigEndian;
		}
	}


	///////////////////////////////////////////////


	public static interface Recorder
	{
		public void start();

		public void stopRecording();
	}



	public static class AbstractRecorder
	extends Thread
	implements Recorder
	{
		protected TargetDataLine	m_line;
		protected AudioFileFormat.Type	m_targetType;
		protected File			m_file;
		protected boolean		m_bRecording;



		public AbstractRecorder(TargetDataLine line,
					AudioFileFormat.Type targetType,
					File file)
		{
			m_line = line;
			m_targetType = targetType;
			m_file = file;
		}



		/**	Starts the recording.
		 *	To accomplish this, (i) the line is started and (ii) the
		 *	thread is started.
		 */
		public void start()
		{
			m_line.start();
			super.start();
		}



		public void stopRecording()
		{
			// for recording, the line needs to be stopped
			// before draining (especially if you're still
			// reading from it)
			m_line.stop();
			m_line.drain();
			m_line.close();
			m_bRecording = false;
		}
	}



	public static class DirectRecorder
	extends AbstractRecorder
	{
		private AudioInputStream	m_audioInputStream;



		public DirectRecorder(TargetDataLine line,
				      AudioFileFormat.Type targetType,
				      File file)
		{
			super(line, targetType, file);
			m_audioInputStream = new AudioInputStream(line);
		}



		public void run()
		{
			try
			{
				if (sm_bDebug) { out("before AudioSystem.write"); }
				AudioSystem.write(
					m_audioInputStream,
					m_targetType,
					m_file);
				if (sm_bDebug) { out("after AudioSystem.write"); }
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}



	}



	public static class BufferingRecorder
	extends AbstractRecorder
	{
		public BufferingRecorder(TargetDataLine line,
					     AudioFileFormat.Type targetType,
					     File file)
		{
			super(line, targetType, file);
		}



		public void run()
		{
			ByteArrayOutputStream	byteArrayOutputStream = new ByteArrayOutputStream();
			OutputStream		outputStream = byteArrayOutputStream;
			// TODO: intelligent size
			byte[]	abBuffer = new byte[65536];
			AudioFormat	format = m_line.getFormat();
			int	nFrameSize = format.getFrameSize();
			int	nBufferFrames = abBuffer.length / nFrameSize;
			m_bRecording = true;
			while (m_bRecording)
			{
				if (sm_bDebug) { out("BufferingRecorder.run(): trying to read: " + nBufferFrames); }
				int	nFramesRead = m_line.read(abBuffer, 0, nBufferFrames);
				if (sm_bDebug) { out("BufferingRecorder.run(): read: " + nFramesRead); }
				int	nBytesToWrite = nFramesRead * nFrameSize;
				try
				{
					outputStream.write(abBuffer, 0, nBytesToWrite);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			/* We close the ByteArrayOutputStream.
			 */
			try
			{
				byteArrayOutputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}


			byte[]	abData = byteArrayOutputStream.toByteArray();
			ByteArrayInputStream	byteArrayInputStream = new ByteArrayInputStream(abData);

			AudioInputStream	audioInputStream = new AudioInputStream(byteArrayInputStream, format, abData.length / format.getFrameSize());
			try
			{
				AudioSystem.write(audioInputStream,  m_targetType, m_file);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}


