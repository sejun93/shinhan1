package object;

import java.util.ArrayList;

public class FirstInfo {

    private String name;

    public ArrayList<SecondInfo> getSecondList() {
        return secondList;
    }

    public void setSecondList(ArrayList<SecondInfo> secondList) {
        this.secondList = secondList;
    }

    private ArrayList<SecondInfo> secondList = new ArrayList<SecondInfo>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    }


