package studymaster.socket;

public interface AudioEventHandler {

    public void onAudioMessage(String name, byte[] receive);

}
