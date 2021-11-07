package Model;

public class FlipOutcome {
    private boolean isHead;
    private float animationTime;

    public FlipOutcome(boolean isHead, float animationTime) {
        this.isHead = isHead;
        this.animationTime = animationTime;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }
}
