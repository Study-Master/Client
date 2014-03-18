package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;
import javax.sound.sampled.*;
import java.io.*;

public class AudioCl extends WebSocketClient {
    private static AudioCl instance = null;
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default AudioCl";
    // private static ImageView imgView = null;
    // private static Webcam webcam;
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

    
    private AudioCl(URI serverURI) {
        super(serverURI);
    }

    public static AudioCl getInstance() {
        if(localServer == null && byteArray == null)
            return null;
        else {
            if(instance == null) {
                try {
                    instance = new AudioCl(new URI(localServer));
                } catch(Exception e) {
                    instance = null;
                }
                return instance;
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public static void setSender(String sender) {
        localSender = sender;
    }

    public static void setEndpoint(String endpoint) {
        localEndpoint = endpoint;
    }

    public static void submit(){
        System.out.println("submitting");
        AudioCl audioClient = AudioCl.getInstance();
        audioClient.send(byteArray);
        System.out.println("sent!");
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        request();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onMessage(String message) {
        System.out.println("[info] (AudioCl onMessage) Receive message: " + message);
        JSONObject msg = new JSONObject(message);

        String event = msg.getString("event");
        String endpoint = msg.getString("endpoint");
        JSONObject content = msg.getJSONObject("content");

        if(event.equals("register")) {
            String status = content.getString("status");
            
        }
    }

    @Override
    public void onError(Exception ex) {}

    private void request() {
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();
        msg.put("event", "register");
        msg.put("endpoint", localEndpoint);
        msg.put("content", content);
        super.send(msg.toString());
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
}
