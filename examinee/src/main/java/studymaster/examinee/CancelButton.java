package studymaster.examinee;

import javafx.scene.control.Button;

public class CancelButton extends Button {
    int row;
    String courseCode;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }    

    public CancelButton(String examStartTime, String courseCode, int row){
        setText("Cancel");
        this.row = row;
        this.courseCode = courseCode;
        System.out.println("[Info] A CancelButton created!");
    }
}