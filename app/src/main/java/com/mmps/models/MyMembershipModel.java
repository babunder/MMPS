package com.mmps.models;

import java.io.Serializable;

/**
 * @Author Dhananjay Kulkarni.
 */

public class MyMembershipModel implements Serializable {

    private int label;
    private int drawable;

    public MyMembershipModel(int label, int drawable) {
        this.label = label;
        this.drawable = drawable;
    }

    public int getLabel() {
        return label;
    }

    public int getDrawable() {
        return drawable;
    }
}
