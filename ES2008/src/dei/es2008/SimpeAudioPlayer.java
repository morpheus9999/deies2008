package dei.es2008;

/*
 *	SimpleAudioPlayer.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


/**	<titleabbrev>SimpleAudioPlayer</titleabbrev>
	<title>Playing an audio file (easy)</title>

	<formalpara><title>Purpose</title>
	<para>Plays a single audio file.</para></formalpara>

	<formalpara><title>Usage</title>
	<cmdsynopsis>
	<command>java SimpleAudioPlayer</command>
	<replaceable class="parameter">audiofile</replaceable>
	</cmdsynopsis>
	</formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><option><replaceable class="parameter">audiofile</replaceable></option></term>
	<listitem><para>the name of the
	audio file that should be played</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>

	<para>Only PCM encoded files are supported. A-law, &mu;-law,
	ADPCM, ogg vorbis, mp3 and other compressed data formats are not
	supported. For playing these, see <olink targetdoc="AudioPlayer"
	targetptr="AudioPlayer">AudioPlayer</olink>.</para>

	</formalpara>
		
	<formalpara><title>Source code</title>
	<para>
	<ulink url="SimpleAudioPlayer.java.html">SimpleAudioPlayer.java</ulink>
	</para>
	</formalpara>

*/
public class SimpeAudioPlayer
{
	private static final int	EXTERNAL_BUFFER_SIZE = 128000;



	public static void main(String[] args)
	{
		/*
		  We check that there is exactely one command-line
		  argument.
		  If not, we display the usage message and exit.
		*/
		

		/*
		  Now, that we're shure there is an argument, we
		  take it as the filename of the soundfile
		  we want to play.
		*/
        String filename = "/Users/jojo/Desktop/Sons ES/musica.wav";
		File	soundFile = new File(filename);
	
		/*
		  We have to read in the sound file.
		*/
		AudioInputStream	audioInputStream = null;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}
		catch (Exception e)
		{
			/*
			  In case of an exception, we dump the exception
			  including the stack trace to the console output.
			  Then, we exit the program.
			*/
			e.printStackTrace();
			System.exit(1);
		}

		/*
		  From the AudioInputStream, i.e. from the sound file,
		  we fetch information about the format of the
		  audio data.
		  These information include the sampling frequency,
		  the number of
		  channels and the size of the samples.
		  These information
		  are needed to ask Java Sound for a suitable output line
		  for this audio file.
		*/
		AudioFormat	audioFormat = audioInputStream.getFormat();

		/*
		  Asking for a line is a rather tricky thing.
		  We have to construct an Info object that specifies
		  the desired properties for the line.
		  First, we have to say which kind of line we want. The
		  possibilities are: SourceDataLine (for playback), Clip
		  (for repeated playback)	and TargetDataLine (for
		  recording).
		  Here, we want to do normal playback, so we ask for
		  a SourceDataLine.
		  Then, we have to pass an AudioFormat object, so that
		  the Line knows which format the data passed to it
		  will have.
		  Furthermore, we can give Java Sound a hint about how
		  big the internal buffer for the line should be. This
		  isn't used here, signaling that we
		  don't care about the exact size. Java Sound will use
		  some default value for the buffer size.
		*/
		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(SourceDataLine.class,
												 audioFormat);
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);

			/*
			  The line is there, but it is not yet ready to
			  receive audio data. We have to open the line.
			*/
			line.open(audioFormat);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		/*
		  Still not enough. The line now can receive data,
		  but will not pass them on to the audio output device
		  (which means to your sound card). This has to be
		  activated.
		*/
		line.start();

		/*
		  Ok, finally the line is prepared. Now comes the real
		  job: we have to write data to the line. We do this
		  in a loop. First, we read data from the
		  AudioInputStream to a buffer. Then, we write from
		  this buffer to the Line. This is done until the end
		  of the file is reached, which is detected by a
		  return value of -1 from the read method of the
		  AudioInputStream.
		*/
		int	nBytesRead = 0;
		byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
		while (nBytesRead != -1)
		{
			try
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (nBytesRead >= 0)
			{
				int	nBytesWritten = line.write(abData, 0, nBytesRead);
			}
		}

		/*
		  Wait until all data are played.
		  This is only necessary because of the bug noted below.
		  (If we do not wait, we would interrupt the playback by
		  prematurely closing the line and exiting the VM.)
		 
		  Thanks to Margie Fitch for bringing me on the right
		  path to this solution.
		*/
		line.drain();

		/*
		  All data are played. We can close the shop.
		*/
		line.close();

		/*
		  There is a bug in the jdk1.3/1.4.
		  It prevents correct termination of the VM.
		  So we have to exit ourselves.
		*/
		System.exit(0);
	}


	private static void printUsageAndExit()
	{
		out("SimpleAudioPlayer: usage:");
		out("\tjava SimpleAudioPlayer <soundfile>");
		System.exit(1);
	}


	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}
}



/*** SimpleAudioPlayer.java ***/