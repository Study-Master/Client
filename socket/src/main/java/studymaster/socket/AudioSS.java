package studymaster.socket;

import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import javax.sound.sampled.*;
import java.io.*;

public class AudioSS extends WebSocketServer {

    private static class defaultDelegate implements AudioEventHandler {
        public void onMessage( WebSocket conn, ByteBuffer message ) {
            System.out.println("[debug] (AudioSS.defaultDelegate onMessage): Receiving byte message, using default delegate.");
        }

        public void onMessage(WebSocket conn, String message) {
            System.out.println("[debug] (AudioSS.defaultDelegate onMessage): Receiving message, using default delegate.");
        }

        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("[debug] (AudioSS.defaultDelegate onOpen): Open new connection, using default delegate.");
        }

        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("[debug] (AudioSS.defaultDelegate onClose): Close connection, using default delegate.");
        }

        public void onError(WebSocket conn, Exception ex) {
            System.err.println("[err] (AudioSS.defaultDelegate onError): An error, using default delegate.");
        }
    }
    private static AudioSS instance = null;
    private static AudioEventHandler localDelegate = null;


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


    public static AudioSS getInstance() {
        if(instance == null) {
            if(localDelegate == null)
                localDelegate = new defaultDelegate();
            String localhost = "0.0.0.0";
            int port = 8089;
            InetSocketAddress address = new InetSocketAddress(localhost, port);
            instance = new AudioSS(address);
        }
        return instance;
    }

    private AudioSS(InetSocketAddress address) {
        super(address);
    }

    public static void setDelegate(AudioEventHandler delegate) {
        localDelegate = delegate;
    }

    public static void setByteArray(byte[] temp){
        byteArray = temp;
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

    private static AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        localDelegate.onOpen(conn, handshake);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        localDelegate.onClose(conn, code, reason, remote);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        localDelegate.onMessage(conn, message);
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        localDelegate.onMessage(conn, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        localDelegate.onError(conn, ex);
    }
}