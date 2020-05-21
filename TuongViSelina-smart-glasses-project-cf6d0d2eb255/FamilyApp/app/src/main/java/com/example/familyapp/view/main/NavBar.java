package com.example.familyapp.view.main;

public class NavBar {
    private int drawable;
    private boolean isFocusing;

    public NavBar(int drawable, boolean isFocusing) {
        this.drawable = drawable;
        this.isFocusing = isFocusing;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isFocusing() {
        return isFocusing;
    }

    public void setFocusing(boolean focusing) {
        isFocusing = focusing;
    }
}
