package ca.cmpt276.chlorinefinalproject.model;

public class child {

    private String name;

    private boolean heads;

    public child(String name) {
        this.name = name;
    }

    public child(String name, boolean heads) {
        this.name = name;
        this.heads = heads;
    }

    // Settters and Getter s


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
