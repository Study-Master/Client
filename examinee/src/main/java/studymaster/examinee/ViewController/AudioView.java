 package studymaster.examinee.ViewController;

 import studymaster.all.ViewController.ViewController;
 import studymaster.examinee.App;
 import studymaster.socket.Connector;
 import studymaster.socket.AudioCl;
 import javax.sound.sampled.*;
 import java.io.*;







 public class AudioView extends ViewController {




 	@Override
     public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
         super.initialize(location, resources);
         connector.auth();
     }

     @Override
     public void onMessage(String message) {
         System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
         AudioCl.setServer("ws://localhost:8089");
         // VideoCl.setImageView(imgView);
         AudioCl audioClient = AudioCl.getInstance();
         audioClient.connect();
     }



 	public void startRecord(){
         AudioCl.startRecord();
     }

     public void stopRecord(){
         AudioCl.stopRecord();
     }

     public void playAudio(){
         AudioCl.playAudio();
     }

     public void send(){
         AudioCl.submit();
     }
 }

