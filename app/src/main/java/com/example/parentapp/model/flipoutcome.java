package com.example.parentapp.model;

public class flipoutcome {

    private  boolean head;

    private float animationTime;

    public flipoutcome(boolean head, float animationTime) {
        this.head = head;
        this.animationTime = animationTime;
    }


    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }


    public float getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }
}
