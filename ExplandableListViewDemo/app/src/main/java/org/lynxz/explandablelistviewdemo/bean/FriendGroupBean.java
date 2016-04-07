package org.lynxz.explandablelistviewdemo.bean;


public class FriendGroupBean {
    public String name;
    public int count;

    public FriendGroupBean(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return name + "(" + count + ")";
    }
}
