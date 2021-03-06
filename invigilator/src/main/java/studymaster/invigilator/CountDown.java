package studymaster.invigilator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import studymaster.invigilator.ViewController.TaskView;

public class CountDown extends Label {
    int row;
    String courseCode;
    String examStartTime;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getStartTime() {
        return examStartTime;
    }

    public CountDown(String courseCode, String examStartTime, int row) {
        this.row = row;
        this.courseCode = courseCode;
        this.examStartTime = examStartTime;
        bindToTime(examStartTime, courseCode, row);
        System.out.println("[Info] A CountDown Label created!");
    }

    private void bindToTime(final String examStartTime, final String courseCode, final int row) {
        EventHandler<ActionEvent> actionEvent = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    Date startTime = dateFormat.parse(examStartTime);
                    long diff = startTime.getTime() - currentTime.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffSeconds = diff / 1000 % 60;
                    String remainingTime = "";
                    if (diffDays>0) {
                        remainingTime = Long.toString(diffDays) + " Days" ;
                    }
                    else {
                        if (diffHours>9) {
                            remainingTime += Long.toString(diffHours);
                        }
                        else {
                            remainingTime += "0" + Long.toString(diffHours);
                        }
                        if (diffMinutes>9) {
                            remainingTime += ":" + Long.toString(diffMinutes);
                        }
                        else {
                            remainingTime += ":0" + Long.toString(diffMinutes);
                        }
                        if (diffSeconds>9) {
                            remainingTime += ":" + Long.toString(diffSeconds);
                        }
                        else {
                            remainingTime += ":0" + Long.toString(diffSeconds);
                        }
                    }
                    setText(remainingTime+ "       ");
                    if (remainingTime.contains(":")) {
                        setStyle("-fx-text-fill: red;");
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CountDown.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),actionEvent),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
