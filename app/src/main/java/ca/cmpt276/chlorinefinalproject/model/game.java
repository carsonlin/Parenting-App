package ca.cmpt276.chlorinefinalproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class game {

    private child picked;

    private coin coin;

    private LocalDateTime time;

    public game(child picked,coin coin) {

        this.picked = picked;

        this.coin = coin;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        time = LocalDateTime.now();



        //System.out.println(dtf.format(now));

    }

    // Setters and Getters


    public child getPicked() {
        return picked;
    }

    public void setPicked(child picked) {
        this.picked = picked;
    }

    public ca.cmpt276.chlorinefinalproject.model.coin getCoin() {
        return coin;
    }

    public void setCoin(ca.cmpt276.chlorinefinalproject.model.coin coin) {
        this.coin = coin;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


}
