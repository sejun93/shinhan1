package object;

import java.util.ArrayList;

public class SecondInfo {

    private String name;
    private ArrayList<ChildInfo> childList = new ArrayList<ChildInfo>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<ChildInfo> getChildList() {
        return childList;
    }
    public void setChildList(ArrayList<ChildInfo> childList) {
        this.childList = childList;
    }

}

