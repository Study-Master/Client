package studymaster.invigilator.ViewController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.CountDown;
import studymaster.invigilator.InvigilateButton;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import org.json.JSONException;
import studymaster.all.ViewController.AlertAction;

public class CourseView extends HomeViewController {
	protected static GridPane List;

	public static GridPane getList() {
		return List;
	}

	// @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
	// 	super.initialize(location, resources);
	// 	JSONObject content = new JSONObject();
	// 	JSONObject course = new JSONObject("{'courses': [{'code': 'CZ2001','name': 'Java','status': 'waiting','start_time': '2014/05/01 00:00:00'},{'code': 'CZ2002','name': 'Java2','status': 'finished','start_time': '2014/05/03 00:00:00'},{'code': 'CZ2006', 'name': 'Java6','status': 'invigilate','start_time': '2014/05/03 00:00:00'}]}");
	// 	content.put("profile", course);
	// 	Connector.getInstance().setAndSendMessageContainer("profile_invigilator", content);
	// 	System.out.println(content);
	// }
	@Override public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
			String event = msg.getString("event");
			String endpoint = msg.getString("endpoint");
			final JSONObject content = msg.getJSONObject("content");

			if(event.equals("profile_invigilator")) {
				showCourseList(content);
			}
			else if(event.equals("enable_invigilation")) {
				enableInvigilation(content);
			}
		}
		catch (Exception e) {
			System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
		}
	}

	private void enableInvigilation(final JSONObject content) {
		//Remove count dwon label, add exam button
		//pass the test
		final String courseCode = content.getString("code");
		final String examStartTime = content.getString("start_time");
		try {
			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ObservableList<Node> childrens = CourseView.getList().getChildren();
					CountDown label = null;
					for (Node node : childrens) {
						if (node instanceof CountDown) {
							if(content.getString("code").equals(((CountDown)node).getCourseCode())) {
								label = (CountDown)node;
								break;
							}
						}
					}
					if (label == null) {
						System.err.println("[Err] Wrong message from server! Don't find the node!");
					}
					else {
						createInvigilationButton(courseCode, label.getRow());
						List.getChildren().remove(label);
					}
				}
			});
		}
		catch (Exception e) {
			System.err.println("Error occurred in exam_enabled");
		}
	}

	private void showCourseList(final JSONObject content) {
		//AlertInfo.setCourseView(this);
		javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						//Find the node #ap from FXML
						final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
						final ScrollPane sp = (ScrollPane) pane.lookup("#scrollpane");
						final AnchorPane ap = (AnchorPane) (sp.lookup("#ap"));
						//Create a gridpane - courseList
						final JSONObject profile = content.getJSONObject("profile");
						JSONArray courses = profile.getJSONArray("courses");
						System.out.println("test");
						ArrayList<JSONObject> coursesArray = new ArrayList<JSONObject>();
						String status;
						GridPane courseList = new GridPane();
						List = courseList;
						AnchorPane.setTopAnchor(courseList, 20.0);
						AnchorPane.setLeftAnchor(courseList, 20.0);
						AnchorPane.setRightAnchor(courseList, 10.0);
						AnchorPane.setBottomAnchor(courseList, 25.0);
						ColumnConstraints col1 = new ColumnConstraints();
						col1.setPercentWidth(15);
						ColumnConstraints col2 = new ColumnConstraints();
						col2.setPercentWidth(65);
						ColumnConstraints col3 = new ColumnConstraints();
						col3.setPercentWidth(20);
						courseList.getColumnConstraints().addAll(col1,col2,col3);
						courseList.setVgap(25);
						col3.setHalignment(HPos.RIGHT);
						//Add courses into an ArrayList
						for (int i=0; i<courses.length(); i++) {
							coursesArray.add(courses.getJSONObject(i));
						}

						//Sort the ArrayList
						// Collections.sort(coursesArray, new Comparator<JSONObject>() {
						// 		@Override public int compare(JSONObject course1, JSONObject course2)
						// 		{
						// 			if ( ("closed".equals(course1.getString("status")) || "finished".equals(course1.getString("status"))) &&
						// 				("closed".equals(course2.getString("status")) || "finished".equals(course2.getString("status"))) ) {
						// 				return course1.getString("code").compareTo(course2.getString("code"));
						// 			}
						// 			else if ("closed".equals(course1.getString("status")) || "finished".equals(course1.getString("status"))) {
						// 				return 1;
						// 			}
						// 			else if ("closed".equals(course2.getString("status")) || "finished".equals(course2.getString("status"))) {
						// 				return -1;
						// 			}
						// 			else {
						// 				return course1.getString("code").compareTo(course2.getString("code"));
						// 			}
						// 		}
						// 	});
						//Display the courses
						for(int i=0; i<coursesArray.size(); i++) {
							final JSONObject course = (JSONObject)coursesArray.get(i);
							//First 2 columns
							Label code = new Label(course.getString("code"));
							code.setStyle("-fx-text-fill: black;");
							Label name = new Label(course.getString("name"));
							name.setStyle("-fx-text-fill: black;");

							courseList.add(code, 0, i);
							courseList.add(name, 1, i);

							status = course.getString("status");
							String examStartTime = course.getString("start_time");
							String courseCode = course.getString("code");

							if (status.equals("waiting")) {
								//Countdown label
								createCountDownLabel(examStartTime, courseCode, i);
							}
							else if (status.equals("finished")) {
								//Finish label
								Label label = new Label("Finished");
								courseList.add(label, 2, i);
							}
							else if (status.equals("invigilate")) {
								//invigilation button
								createInvigilationButton(courseCode, i);
							}
						}
						ap.getChildren().addAll(courseList);
					} catch (JSONException e) {
						System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
						e.printStackTrace();
					}
				}
			});
	}

	public static void createCountDownLabel(String examStartTime, String courseCode, int row) {
		CountDown timeLabel = new CountDown(examStartTime, courseCode, row);
		List.add(timeLabel, 2, row);
	}

	public static void createInvigilationButton(final String courseCode, final int row) {
		javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					final InvigilateButton button = new InvigilateButton(courseCode, row);
					button.setPrefWidth(150);
					button.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent e) {
								button.setText("");
								Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
								button.setGraphic(new ImageView(LoadingIcon));
								button.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
								button.setDisable(true);
								setAndSendInvigilationMsg(courseCode);
								Director.pushStageWithFXML(getClass().getResource("/fxml/invigilatorView.fxml"));
							}
						});
					List.add(button, 2, row);
				}
			});
	}

	public static void setAndSendInvigilationMsg(String course) {
		JSONObject content = new JSONObject();
		content.put("code", course);
		Connector.getInstance().setAndSendMessageContainer("start_invigilation", content);
		System.out.println("\n"+content+"\n");
	}
}
