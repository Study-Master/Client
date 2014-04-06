package studymaster.examinee.ViewController;

import studymaster.examinee.QuestionDatabase;
import studymaster.socket.Connector;
import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.Director;
import studymaster.all.ViewController.AlertAction;

import studymaster.examinee.App;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import studymaster.media.Webcamera;

public class ExamView extends ViewController {
	@FXML protected Label titleLabel;
	@FXML protected Label questionDescription;
	@FXML protected Label timer;
    @FXML protected Label numberOfQuestionsAnswered;
	@FXML protected RadioButton choiceA;
	@FXML protected RadioButton choiceB;
	@FXML protected RadioButton choiceC;
	@FXML protected RadioButton choiceD;
	@FXML protected Button firstQuestion;
	@FXML protected Button previousQuestion;
	@FXML protected Button nextQuestion;
	@FXML protected Button lastQuestion;
	@FXML protected Button submit;
	@FXML protected GridPane gridPane;
	@FXML protected AnchorPane msgArea;
    @FXML protected TextArea receiveTextArea;
    @FXML protected TextArea sendTextArea;
    @FXML protected Button sendTextButton;
    private boolean created = false;
    private boolean status = false;
	private Integer duration = 7200;//time duration of the exam in seconds
	private Timeline timeline;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("[info] (" + getClass().getSimpleName() + " initializing page \n\n");
        super.initialize(location, resources);

        setAttribute();
        if (created == false) {
            msgArea.setVisible(false);
            created = true;
        }

        QuestionDatabase database = QuestionDatabase.getInstance();
        titleLabel.setText(database.getCourseCode() + " Online Exam");//course code is stored at position 0!!!
        questionDescription.setText(database.getQuestionNumber() + ". " + database.getQuestionDescription());
        questionDescription.setTextOverrun(OverrunStyle.CLIP);
        numberOfQuestionsAnswered.setText(database.getNumberOfQuestionsAnswered() + " of " + database.getTotalNumberOfQuestions() + " answered");

        ToggleGroup group = new ToggleGroup();
        choiceA.setToggleGroup(group);
        choiceB.setToggleGroup(group);
        choiceC.setToggleGroup(group);
        choiceD.setToggleGroup(group);
        choiceA.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("a"));
        choiceA.setTextOverrun(OverrunStyle.CLIP);
        choiceB.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("b"));
        choiceB.setTextOverrun(OverrunStyle.CLIP);
        choiceC.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("c"));
        choiceC.setTextOverrun(OverrunStyle.CLIP);
        choiceD.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("d"));
        choiceD.setTextOverrun(OverrunStyle.CLIP);

        //code below is responsible for the countdown function
        formatCountdown(duration);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                                    new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent event) {     
                                        duration--;
                                        if (duration <= 600) {
                                            timer.setTextFill(Color.RED);
                                        }
                                        if (duration == 0) {
                                            timeline.stop();
                                            timer.setText("Time is up!");
                                            AlertAction action = new AlertAction() {
                                                @Override public void ok(Stage stage) {
                                                    submitAnswer();
                                                    stage.close();
                                                }
                                            };
                                            director.invokeOneButtonAlert("Time is up!", "Your answers will be submitted automatically.", action);
                                        }
                                        else {
                                            formatCountdown(duration);
                                        }
                                    }
                                }));
        timeline.playFromStart();

        choiceA.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.setAnswer("a");
                numberOfQuestionsAnswered.setText(database.getNumberOfQuestionsAnswered() + " of " + database.getTotalNumberOfQuestions() + " answered");
            }
        });
        choiceB.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.setAnswer("b");
                numberOfQuestionsAnswered.setText(database.getNumberOfQuestionsAnswered() + " of " + database.getTotalNumberOfQuestions() + " answered");
            }
        });
        choiceC.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.setAnswer("c");
                numberOfQuestionsAnswered.setText(database.getNumberOfQuestionsAnswered() + " of " + database.getTotalNumberOfQuestions() + " answered");
            }
        });
        choiceD.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.setAnswer("d");
                numberOfQuestionsAnswered.setText(database.getNumberOfQuestionsAnswered() + " of " + database.getTotalNumberOfQuestions() + " answered");
            }
        });

        //pre-select the answer that examinee chooses already
        if (database.getAnswer() == "a") {
            choiceA.setSelected(true);
        } else if (database.getAnswer() == "b") {
                choiceB.setSelected(true);
            } else if (database.getAnswer() == "c") {
                    choiceC.setSelected(true);
                } else if (database.getAnswer() == "d") {
                        choiceD.setSelected(true);
                    } else {
                        }

        firstQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.getFirstQuestion();
                updataStage();
            }
        });
        previousQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.getPreviousQuestion();
                updataStage();
            }
        });
        nextQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.getNextQuestion();
                updataStage();
            }
        });
        lastQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                QuestionDatabase database = QuestionDatabase.getInstance();
                database.getLastQuestion();
                updataStage();
            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                AlertAction action = new AlertAction() {
                    @Override public void ok(Stage stage) {
                        submitAnswer();
                        stage.close();
                    }
                };
                director.invokeTwoButtonAlert("Submit?", "Are you sure that you want to submit?", action);
            }
        });
    }

  	@Override public void onMessage(String message) {
	    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message + "\n\n");

    	JSONObject msg = new JSONObject(message);
    	String event = msg.getString("event");
        JSONObject content = msg.getJSONObject("content");

    	//if submission is successful, pop up a window, then jump to course view
    	if (event.equals("submission_successful")) {
			AlertAction action = new AlertAction() {
            	@Override public void ok(Stage stage) {
                    Webcamera.renew();
                	director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
                	stage.close();
            	}
        	};
        	director.invokeOneButtonAlert("Successful", "Your submission is successful!", action);
    	}
        else if (event.equals("exam_chat")) {
            String invigilatorMessage = content.getString("msg");
            receiveTextAction(invigilatorMessage);
        }
        else if (event.equals("terminate")) {
            AlertAction action = new AlertAction() {
                @Override public void ok(Stage stage) {
                    Webcamera.renew();
                    director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
                    stage.close();
                }
            };
            director.invokeOneButtonAlert("Exam terminated!", "Message from invigilator: \"" + content.getString("reason") + "\"", action);
        }
    }

    @FXML protected void textAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " textAction): text chat with invigilator...");
        if (status == true) {
            msgArea.setVisible(false);       
            status = false;
        }
        else {
            msgArea.setVisible(true);
            status = true;
        }
    }

	private void updataStage() {
		System.out.println("[info] (" + getClass().getSimpleName() + " updataStage) reload content \n");
		choiceA.setSelected(false);
		choiceB.setSelected(false);
		choiceC.setSelected(false);
		choiceD.setSelected(false);
		QuestionDatabase database = QuestionDatabase.getInstance();
		questionDescription.setText(database.getQuestionNumber() + ". " + database.getQuestionDescription());
		choiceA.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("a"));
		choiceB.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("b"));
		choiceC.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("c"));
		choiceD.setText(database.getCurrentQuestion().getJSONObject("question_content").getJSONObject("choices").getString("d"));

		if (database.getAnswer() == "a") {
			choiceA.setSelected(true);
		} 
		else if (database.getAnswer() == "b") {
			choiceB.setSelected(true);
		}
		else if (database.getAnswer() == "c") {
			choiceC.setSelected(true);
		}
		else if (database.getAnswer() == "d") {
			choiceD.setSelected(true);
		} 
		else {
		}
	}

	private void formatCountdown(int duration){
        Integer hr = duration / 3600;
        Integer min = (duration - 3600 * hr) / 60;
        Integer sec = duration - 3600 * hr - 60 * min;
        String hour=hr.toString();
        String minute=min.toString();
        String second=sec.toString();

        if (hr < 10) {
            hour = "0" + hour;
        }
        if (min < 10) {
            minute = "0" + minute;
        }
        if (sec < 10) {
            second = "0" + second;
        }
        
        timer.setText(hour + ":" + minute + ":" + second);
	}

    private void setAttribute() {
        receiveTextArea.setEditable(false);
    }

    @FXML 
    public void sendTextAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " sendtextAction): sending text...");
        JSONObject content = new JSONObject();
        
        Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sendingName = connector.getSender();
        String s = df.format(date);
        String sendingText = sendTextArea.getText();
        QuestionDatabase database = QuestionDatabase.getInstance();
        content.put("account", sendingName); 
        content.put("exam_pk", database.getExamPk());
        content.put("system_time", s); 
        content.put("msg", sendingText);

        connector.setAndSendMessageContainer("exam_chat", content);
        sendTextArea.clear();
        receiveTextArea.appendText(sendingName + ":(" + s + ")\n");
        receiveTextArea.appendText(sendingText + "\n");
    }

    private void receiveTextAction(String msg) {
        Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String s = df.format(date);
        receiveTextArea.appendText("Invigilator(" + s + "):\n");
        receiveTextArea.appendText(msg + "\n");
    }

    private void submitAnswer() {
        QuestionDatabase database = QuestionDatabase.getInstance();
        JSONObject content = new JSONObject();
        content.put("course_code", database.getCourseCode());
        content.put("exam_pk", database.getExamPk());
        database.getFirstQuestion();
        Set<JSONObject> question_set = new HashSet();
        for (int i=0; i<(database.getQuestionSetSize()); i++) {
            question_set.add(database.getCurrentQuestion());
            database.getNextQuestion();
        }
        content.put("question_set", question_set);
        connector.setAndSendMessageContainer("exam_question_answer", content);
        database.emptyDatabase();
    }
}

