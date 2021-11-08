package Model;

public class ChildPick {
    private String name;
    private boolean heads;

    public ChildPick(String name) {
        this.name = name;
    }

    public ChildPick(String name, boolean heads) {
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
