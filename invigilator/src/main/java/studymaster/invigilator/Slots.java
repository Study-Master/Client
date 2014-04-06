package studymaster.invigilator;

import java.util.Map;
import java.util.HashMap;

public class Slots {
    private static Slots instance = null;
    private String[] names;
    private int[] exam_pks;

    private Slots() {
        names = new String[3];
        exam_pks = new int[3];
    }

    public static Slots getInstance() {
        if(instance==null) {
            System.out.println("[info] (Slots newInstance)");
            instance = new Slots();
            return instance;
        }
        else{
            return instance;
        }
    }

    public void setName(int number, String name) {
        names[number] = name;
    }

    public String getName(int number) {
        return names[number];
    }

    public void setExam(int number, int pk) {
        exam_pks[number] = pk;
    }

    public int getExam(int number) {
        return exam_pks[number];
    }
}
