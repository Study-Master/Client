package studymaster.examinee;

import org.json.JSONObject;
import java.util.ArrayList;


public final class QuestionDatabase {

	private static QuestionDatabase instance;
	private static int index = 0;
    private static String course_code = null;
    private static int exam_pk = 0;
	private ArrayList<JSONObject> question_set = new ArrayList<JSONObject>();

	public static QuestionDatabase getInstance() {
        if(instance == null) {
            instance = new QuestionDatabase();
        }
        return instance;
    }

    public void setExamPk(int examPk) {
        exam_pk = examPk;
    }

    public int getExamPk() {
        return exam_pk;
    }

    public void setCourseCode(String courseCode) {
        course_code = courseCode;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void addQuestion(JSONObject question) {
        question_set.add(question);
    }

    public int getQuestionSetSize() {
        return question_set.size();
    }

    public void setAnswer(String answer) {
        getCurrentQuestion().getJSONObject("question_content").put("answer", answer);
    }

    public String getAnswer() {
        return getCurrentQuestion().getJSONObject("question_content").getString("answer");
    }

    public int getQuestionNumber() {
        return (index+1);
    }

    public String getQuestionDescription() {
        return getCurrentQuestion().getJSONObject("question_content").getString("description");
    }

    public JSONObject getCurrentQuestion() {
    	return question_set.get(index);
    }

    public void getPreviousQuestion() {
    	index--;
    	if (index < 0) {
    		index++;
    	}
    }

    public void getNextQuestion() {
    	index++;
    	if (index > (question_set.size()-1)) {
    		index--;
    	}
    }

    public void getFirstQuestion() {
        index = 0;
    }

    public void getLastQuestion() {
        index = question_set.size()-1;
    }

    public int getNumberOfQuestionsAnswered() {
        int number=getTotalNumberOfQuestions();
        for (int i=0; i<question_set.size() ;i++ ) {
            if (question_set.get(i).getJSONObject("question_content").getString("answer").equals("")) {
                number--;
            }
        }
        return number;
    }

    public int getTotalNumberOfQuestions() {
        return question_set.size();
    }

    public void emptyDatabase() {
        index = 0;
        course_code = null;
        exam_pk = 0;
        question_set.clear();
    }
}