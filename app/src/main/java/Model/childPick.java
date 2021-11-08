package Model;

public class childPick {
    private String name;
    private boolean heads;

    public childPick(String name) {
        this.name = name;
    }

    public childPick(String name, boolean heads) {
        this.name = name;
        this.heads = heads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHeads() {
        return heads;
    }

    public void setHeads(boolean heads) {
        this.heads = heads;
    }
}
