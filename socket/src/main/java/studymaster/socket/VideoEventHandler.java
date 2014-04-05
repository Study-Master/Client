package studymaster.socket;

public interface VideoEventHandler {

    public void onVideoClientClose(int code, String reason, boolean remote);

}
