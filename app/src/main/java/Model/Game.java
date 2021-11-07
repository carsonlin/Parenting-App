package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {
    private Child picked;
    private Coin coin;
    private LocalDateTime time;

    public Game(Child picked, Coin coin) {
        this.picked = picked;
        this.coin = coin;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        time = LocalDateTime.now();

    }

    public Child getPicked() {
        return picked;
    }

    public void setPicked(Child picked) {
        this.picked = picked;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
