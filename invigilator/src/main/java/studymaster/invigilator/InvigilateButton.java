package studymaster.invigilator;

import javafx.scene.control.Button;

public class InvigilateButton extends Button {
    int row;
    String courseCode;
    String examStartTime;

    public int getRow() {
        return row;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getExamStartTime(){
        return examStartTime;
    }

    public InvigilateButton(String courseCode, String examStartTime, int row){
        this.row = row;
        this.courseCode = courseCode;
        this.examStartTime = examStartTime;
        setText("Invigilate");
        System.out.println("[Info] A ExamButton created!");
    }
}
