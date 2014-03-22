package studymaster.examinee.ViewController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import studymaster.all.ViewController.TestViewController;
import javafx.fxml.FXML;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class TestView extends TestViewController {

// Copy from AudioView
    Thread stopper;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.AU;
    TargetDataLine line;

    private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    private ByteArrayOutputStream outputStream;
    
    private byte[] byteArray;
    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }
        
    @FXML @Override public void nextAction() {
        super.nextAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/testView2.fxml"));
    }
     
    @FXML @Override public void backAction() {
        super.backAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }

    @Override public void test() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) start audio test");
        startRecord();
    }
        
    @Override public void aftertest() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) finish audio test");
        stopRecord();
        playAudio();
	}
        
    private AudioFormat getAudioFormat() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) getAudioFormat");
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }

    public void startRecord(){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) start recording");
        try{
            outputStream = null;
            outputStream = new ByteArrayOutputStream();
        } catch(Exception e) {
            System.err.println("[err] Fail to start audio record");
        }
        stopper = new Thread(new Runnable() {
            public void run() {
                capture();
            }
        });
        stopper.start();
    }

    public void capture(){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) start capturing");
            try {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
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
                AudioSystem.write(ais, fileType, outputStream);
        } catch (LineUnavailableException ex) {
        	ex.printStackTrace();
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecord(){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) stop recording");
    	line.stop();
        line.close();
        System.out.println("Finished");
    	stopper.yield();
        byteArray = outputStream.toByteArray();
        System.out.println(byteArray);
    }

    public void playAudio(){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) play audio");
        ByteArrayInputStream baiut = new ByteArrayInputStream(byteArray);
        try {
            audioStream = AudioSystem.getAudioInputStream(baiut);
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
