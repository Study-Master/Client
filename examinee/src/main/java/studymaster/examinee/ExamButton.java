package studymaster.examinee;

import javafx.scene.control.Button;

public class ExamButton extends Button {
    int row;
    String courseCode;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }    

    public ExamButton(String examStartTime, String courseCode, int row){
        this.row = row;
        this.courseCode = courseCode;
        setText("Exam");
        System.out.println("[Info] A ExamButton created!");
    }
}
