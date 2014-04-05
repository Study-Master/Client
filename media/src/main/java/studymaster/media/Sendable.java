package studymaster.media;

public interface Sendable {

    public boolean isConnected();

    public void sendMedia(byte[] media, String typeFlag);

}
