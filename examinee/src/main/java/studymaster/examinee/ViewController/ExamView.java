package studymaster.examinee.ViewController;

import studymaster.examinee.QuestionDatabase;
import studymaster.socket.Connector;
import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.Director;
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

public class ExamView extends ViewController {
	@FXML protected Label titleLabel;
	@FXML protected Label questionDescription;
	@FXML protected Label timer;
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

    boolean created = false;
    boolean status = false;
	@FXML AnchorPane msgArea;
    @FXML TextArea receiveTextArea;
    @FXML TextArea sendTextArea;
    @FXML Button sendTextButton;

	private Integer duration = 1000;//time duration of the exam in minutes
	private Timeline timeline;

  	@Override public void onMessage(String message) {
	    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message + "\n\n");

    	JSONObject msg = new JSONObject(message);
    	String event = msg.getString("event");
    	String endpoint = msg.getString("endpoint");
    	JSONObject content = msg.getJSONObject("content");

    	//if submission is successful, pop up a window, then jump to course view
    	if (event.equals("submission_message")) {
			if (content.getString("submission_status").equals("successful")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override public void run() {
        				final Stage submissionPopup = new Stage();
		        		VBox vbox = new VBox(10);
						Button ok = new Button("OK");
						Text text = new Text("Submission successful!");
						submissionPopup.initStyle(StageStyle.UNDECORATED);
						vbox.getChildren().add(text);
						vbox.getChildren().add(ok);
						vbox.setAlignment(Pos.CENTER);
						vbox.setStyle("-fx-border-style: solid;"
		                			+ "-fx-border-width: 1;"
		               				+ "-fx-border-color: black");
						Scene scene = new Scene(vbox, 180, 90);
						submissionPopup.setScene(scene);
						submissionPopup.show();

						ok.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
								submissionPopup.close();
								director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
							}
						});
        			}
   				});
			}
			else {
				//pop up window telling user submission is failed
				//then user clicks "ok" button and stays on this page
				javafx.application.Platform.runLater(new Runnable() {
					@Override public void run() {
        				final Stage submissionPopup = new Stage();
		        		VBox vbox = new VBox(10);
						Button ok = new Button("OK");
						Text text = new Text("Submission failed!");
						submissionPopup.initStyle(StageStyle.UNDECORATED);
						vbox.getChildren().add(text);
						vbox.getChildren().add(ok);
						vbox.setAlignment(Pos.CENTER);
						vbox.setStyle("-fx-border-style: solid;"
		                			+ "-fx-border-width: 1;"
		               				+ "-fx-border-color: black");
						Scene scene = new Scene(vbox, 180, 90);
						submissionPopup.setScene(scene);
						submissionPopup.show();

						ok.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
								submissionPopup.close();
							}
						});
        			}
   				});
			}
    	}
    	else {
    	}
    }
// copy from textview controller      

    @FXML public void textAction() {
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

// end of copy
 	@Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		System.out.println("[info] (" + getClass().getSimpleName() + " initializing page \n\n");
		super.initialize(location, resources);
//
		        setAttribute();
        if (created == false) {
            msgArea.setVisible(false);
            created = true;
        }
//
		QuestionDatabase database = QuestionDatabase.getInstance();

		database.setCourseCode("EE8086");
        JSONObject question1 = new JSONObject();
        question1.put("number", 1);
        question1.put("pk", 13);
        question1.put("type", "multi");
        JSONObject question1_content = new JSONObject();
        question1_content.put("description", "now i am programming, today i might be programming, but the day after tmr it's confirmed that I'll be programming because of this stupid project. Based on this, which of the following is correct?");
        JSONObject question1_choice = new JSONObject();
        question1_choice.put("a", "I am a computer science undergradute who is suffering because final exams are coming and i'm totally not prepared for it");
        question1_choice.put("b", "i am handsome and cool and hansum and yandao and i indeed know it but it seems that people around me are not aware of this");
        question1_choice.put("c", "maybe i should find a way to let them know this, oh my god...");
        question1_choice.put("d", "i am handsome and cool and hansum and yandao and i indeed know it but it seems that people around me are not aware of this");
        question1_content.put("choices", question1_choice);
        question1_content.put("answer", "");
        question1.put("question_content", question1_content);

        JSONObject question2 = new JSONObject();
        question2.put("number", 2);
        question2.put("pk", 14);
        question2.put("type", "multi");
        JSONObject question2_content = new JSONObject();
        question2_content.put("description", "How much do you like computer science?");
        JSONObject question2_choice = new JSONObject();
        question2_choice.put("a", "Very much");
        question2_choice.put("b", "So so");
        question2_choice.put("c", "Only a little bit");
        question2_choice.put("d", "Not at all");
        question2_content.put("choices", question2_choice);
        question2_content.put("answer", "");
        question2.put("question_content", question2_content);
        
        JSONObject question3 = new JSONObject();
        question3.put("number", 3);
        question3.put("pk", 15);
        question3.put("type", "multi");
        JSONObject question3_content = new JSONObject();
        question3_content.put("description", "Which is the best university in the world?");
        JSONObject question3_choice = new JSONObject();
        question3_choice.put("a", "Nanyang Technological University");
        question3_choice.put("b", "National University of Singapore");
        question3_choice.put("c", "Harvard University");
        question3_choice.put("d", "MIT");
        question3_content.put("choices", question3_choice);
        question3_content.put("answer", "");
        question3.put("question_content", question3_content);

        database.addQuestion(question1);
        database.addQuestion(question2);
        database.addQuestion(question3);

		//load in the question_set that is stored in database which is setup in the courseView
		//and put the text to where they belong
		titleLabel.setText(database.getCourseCode() + " Online Exam");//course code is stored at position 0!!!
		questionDescription.setText(database.getQuestionNumber() + ". " + database.getQuestionDescription());
		questionDescription.setMaxWidth(850);
		questionDescription.setTextOverrun(OverrunStyle.CLIP);

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




		formatCountdown(duration);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
        							new EventHandler<ActionEvent>() {
        							public void handle(ActionEvent event) {		
    									duration--;
    									if (duration <= 10) {
    										timer.setTextFill(Color.RED);
    									}
    									if (duration == 0) {
    										timer.setText("Time is up!");
    										javafx.application.Platform.runLater(new Runnable() {
												@Override public void run() {
							        				final Stage submissionPopup = new Stage();
									        		VBox vbox = new VBox(10);
													Button ok = new Button("OK");
													Text text = new Text("Time is up! Your answers will be submitted.");
													submissionPopup.initStyle(StageStyle.UNDECORATED);
													vbox.getChildren().add(text);
													vbox.getChildren().add(ok);
													vbox.setAlignment(Pos.CENTER);
													vbox.setStyle("-fx-border-style: solid;"
									                			+ "-fx-border-width: 1;"
									               				+ "-fx-border-color: black");
													Scene scene = new Scene(vbox, 310, 90);
													submissionPopup.setScene(scene);
													submissionPopup.show();
													//put answers into a jsonobject
													QuestionDatabase database = QuestionDatabase.getInstance();
													JSONObject msg = new JSONObject();
													msg.put("event", "exam_question_answer");
													msg.put("endpoint", "Java Client");
													JSONObject content = new JSONObject();
													content.put("code", database.getCourseCode());
													database.getFirstQuestion();
													Set<JSONObject> question_set = new HashSet();
													for (int i=0; i<(database.getQuestionSetSize()); i++) {
														question_set.add(database.getCurrentQuestion());
														database.getNextQuestion();
													}
													content.put("question_set", question_set);
													msg.put("content", content);
													connector.send(msg.toString());

													ok.setOnAction(new EventHandler<ActionEvent>() {
														@Override public void handle(ActionEvent event) {
															submissionPopup.close();
														}
													});
							        			}
							   				});
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
			}
		});
		choiceB.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				QuestionDatabase database = QuestionDatabase.getInstance();
				database.setAnswer("b");
			}
		});
		choiceC.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				QuestionDatabase database = QuestionDatabase.getInstance();
				database.setAnswer("c");
			}
		});
		choiceD.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				QuestionDatabase database = QuestionDatabase.getInstance();
				database.setAnswer("d");
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
        		final Stage submitPopup = new Stage();
        		VBox vbox = new VBox(10);
				Button yes = new Button("Yes");
				Button no = new Button("No");
				Text text = new Text("Are you sure you want to submit?");
				HBox hbox = new HBox();
				hbox.getChildren().add(yes);
				hbox.getChildren().add(no);
				hbox.setAlignment(Pos.CENTER);
        		hbox.setSpacing(20);
				submitPopup.initStyle(StageStyle.UNDECORATED);
				vbox.getChildren().add(text);
				vbox.getChildren().add(hbox);
				vbox.setAlignment(Pos.CENTER);
				vbox.setStyle("-fx-border-style: solid;"
                			+ "-fx-border-width: 1;"
               				+ "-fx-border-color: black");
				Scene scene = new Scene(vbox, 260, 90);
				submitPopup.setScene(scene);
				submitPopup.show();
				
				no.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						submitPopup.close();
					}
				});

				//send the answers to server and wait for server's reply
				yes.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						QuestionDatabase database = QuestionDatabase.getInstance();
						
						//put the answers of the examinee into a JSONObject and send to server
						JSONObject msg = new JSONObject();
						msg.put("event", "exam_question_answer");
						msg.put("endpoint", "Java Client");
						JSONObject content = new JSONObject();
						content.put("code", database.getCourseCode());
						database.getFirstQuestion();//set index pointing to the first question
						Set<JSONObject> question_set = new HashSet();
						for (int i=0; i<(database.getQuestionSetSize()); i++) {
							question_set.add(database.getCurrentQuestion());
							database.getNextQuestion();
						}
						content.put("question_set", question_set);
						msg.put("content", content);
						submitPopup.close();

						//send answers to server
						connector.send(msg.toString());
					}
				});
			}
		});
	}

	//function used to refresh the screen
	//the elements don't change dynamicly, they just reload the content
	private void updataStage() {
		System.out.println("[info] (" + getClass().getSimpleName() + " updataStage) reload content \n");
		//first set all the radio buttons unselected
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
		if (duration >= 60) {
			timer.setText("1:" + (duration-60));
		}
		else if (duration < 60 && duration > 0) {
			timer.setText("0:" + duration);
		}
		else {
		}
	}

// copy from text view


    public void setAttribute() {
        receiveTextArea.setEditable(false);
    }

    public void sendTextAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " sendtextAction): sending text...");
        JSONObject Msg = new JSONObject();
        JSONObject Content = new JSONObject();
        Msg.put("event", "exam_chat");
        Msg.put("endpoint", "Java Examinee Client");
            
        Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String s = df.format(date);
        String sendingText = sendTextArea.getText();
        String sendingName = Connector.getSender();
            
        Content.put("account", sendingName); 
        Content.put("exam_pk", 3); // need to be modified
        Content.put("system_time", s); 
        Content.put("msg", sendingText);
            
        Msg.put("content", Content);
        //Connector.setMessageContainer(Msg.toString());
        sendTextArea.clear();
        receiveTextArea.appendText(sendingName + ":(" + s + ")\n");
        receiveTextArea.appendText(sendingText + "\n");
    }
// end of copy
}

