package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {
    private childPick child;
    private LocalDateTime time;
    private boolean head;

    public Game(childPick child) {
        this.child = child;
        time = LocalDateTime.now();
    }

    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }

    public childPick getChild() {
        return child;
    }

    public void setChild(childPick child) {
        this.child = child;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setTime(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        setTime(dateTime);
    }
}
