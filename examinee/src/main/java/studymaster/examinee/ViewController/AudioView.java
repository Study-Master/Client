package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import javax.sound.sampled.*;
import java.io.*;
import javafx.scene.media.Media;
import javafx.scene.media.AudioClip;



public class AudioView extends ViewController {

	Thread stopper;

    // path of the wav file
	File wavFile = new File("/Users/zhanghanlin/Desktop/RecordAudio.wav");
	File soundFile = new File("/Users/zhanghanlin/Desktop/RecordAudio.wav");

    // format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
	TargetDataLine line;

	private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;



	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.auth();
	}

	@Override
	public void onMessage(String message){

	}
	private AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
			channels, signed, bigEndian);
		return format;
	}

	public void startRecord(){
		stopper = new Thread(new Runnable() {
			public void run() {
				capture();
			}
		});

		stopper.start();
	}

	public void capture(){
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
        	ex.printStackTrace();
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
    }

    public void stopRecord(){
    	line.stop();
		line.close();
		System.out.println("Finished");
    	stopper.yield();
    	
    }

    public void playAudio(){
		
		
		

		try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
}