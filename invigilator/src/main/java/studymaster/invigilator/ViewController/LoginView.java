package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.App;
import org.json.JSONObject;

public class LoginView extends LoginViewController {

    @Override
    public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
        try {
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            String endpoint = msg.getString("endpoint");
            final JSONObject content = msg.getJSONObject("content");

            if(event.equals("login")) {
                String status = content.getString("status");

                if(status.equals("success")) {
                    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login successfully.");
                    director.pushStageWithFXML(getClass().getResource("/fxml/invigilateView.fxml"));
                }

                else if(status.equals("failed")) {
                    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login failed.");
                    loginFailed(content.getString("reason"));                }

                else {
                    System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Unexpected JSON response string.");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    public void onEnter() {
        loginAction();
    }

    @Override
    public void login(String account, String password) {
        Connector.setSender(account);
        try {
            boolean connected = true;
            if(!connector.isOpen())
                connected = connector.connectBlocking();
            if(connected) {
                JSONObject content = new JSONObject();
                content.put("account", account);
                content.put("password", password);
                content.put("time", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                connector.setAndSendMessageContainer("login", content);
            }
        } catch(Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" login) An error is caught, no connection.");
            e.printStackTrace();
            loginFailed("Error, please check your Internet connection or restart the program.");
        }
    }

}
