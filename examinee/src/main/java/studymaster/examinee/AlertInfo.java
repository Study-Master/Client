package studymaster.examinee;

import studymaster.socket.Callback;

public class AlertInfo {
	private static String title;
	private static String info;
	private static Callback courseView;

	public static void setTitle(String title) {
		AlertInfo.title = title;
	}

	public static void setInfo(String info) {
		AlertInfo.info = info;
	}

	public static void setCourseView(Callback courseView) {
		AlertInfo.courseView = courseView;
	}

	public static String getTitle() {
		return title;
	}

	public static String getInfo() {
		return info;
	}

	public static Callback getCourseView() {
		return courseView;
	}
}