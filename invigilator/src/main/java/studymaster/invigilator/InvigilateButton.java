package studymaster.invigilator;

import javafx.scene.control.Button;

public class InvigilateButton extends Button {
    int row;
    String courseCode;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public InvigilateButton(String courseCode, int row){
        this.row = row;
        this.courseCode = courseCode;
        setText("Invigilate");
        System.out.println("[Info] A ExamButton created!");
    }
}
