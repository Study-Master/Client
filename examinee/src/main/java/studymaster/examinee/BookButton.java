package studymaster.examinee;

import javafx.scene.control.Button;

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