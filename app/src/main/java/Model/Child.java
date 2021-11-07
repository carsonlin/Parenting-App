package Model;

public class Child {
    private String name;
    private boolean heads;

    public Child(String name) {
        this.name = name;
    }

    public Child(String name, boolean heads) {
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
