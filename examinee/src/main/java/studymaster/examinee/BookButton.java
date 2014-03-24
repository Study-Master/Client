package studymaster.examinee;


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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import studymaster.examinee.ViewController.CourseView;

public class BookButton extends Button {
    int row;
    String courseCode;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }    

    public BookButton(String examStartTime, String courseCode, int row){
        setText("Book");
        this.row = row;
        this.courseCode = courseCode;
        System.out.println("[Info] A BookButton created!");
    }
}