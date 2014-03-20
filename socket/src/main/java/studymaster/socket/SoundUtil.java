package studymaster.socket;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;

public class SoundUtil  {
  private static Thread stopper;

  // format of audio file
  private static AudioFileFormat.Type fileType = AudioFileFormat.Type.AU;

  // the line from which audio data is captured
  private static TargetDataLine line;
  private static final int BUFFER_SIZE = 128000;
  private static AudioInputStream audioStream;
  private static AudioFormat audioFormat;
  private static SourceDataLine sourceLine;
  private static ByteArrayOutputStream outputStream;
  private static byte[] byteArray;

  private static AudioFormat getAudioFormat() {
    float sampleRate = 16000;
    int sampleSizeInBits = 8;
    int channels = 2;
    boolean signed = true;
    boolean bigEndian = true;
    AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    return format;
  }

  public static void startRecord(){
    try{
      outputStream = null;
      outputStream = new ByteArrayOutputStream();
    }catch(Exception e){

    }

    stopper = new Thread(new Runnable() {
      public void run() {
        capture();
      }
    });

    stopper.start();
  }



  public static void stopRecord(){
    line.stop();
    line.close();
    System.out.println("Finished");
    stopper.yield();
    byteArray = outputStream.toByteArray();
    System.out.println(byteArray);

  }


  public static void capture(){
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
      line.start(); // start capturing

      System.out.println("Start capturing...");

      AudioInputStream ais = new AudioInputStream(line);


      System.out.println("Start recording...");

      // start recording
      AudioSystem.write(ais, fileType, outputStream);

    } catch (LineUnavailableException ex) {
      ex.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void setByteArray(ByteBuffer temp){
    System.out.println("convert bytebuffer to temp");
    byteArray = temp.array();
    System.out.println("successfully converted");
  }

  public static void playAudio(){
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

  public static byte[] getByteArray(){
    return byteArray;
  }
}
