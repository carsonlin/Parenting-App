package Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 *
 *   Coin has to be instantiated before using this Game handle
 *
 *           Reason:
 *               passing of different Activity can lead to program
 *               crash due to trying to access an activity which no longer exists
 *
 *
 * */

/*
 shared preferences not implemented
 needs implementation
 */

public class GameManager {

    public static final String GAME_HISTORY = "gameHistory";
    private final ArrayList<Game> games;
    private final ConfigureChildren childrenConfig;
    private final ArrayList<String> childrenList;
    private final Activity context;
    private Coin coin;

    public GameManager(Activity activity, Coin coin) {
        this.context = activity;
        this.childrenConfig = ConfigureChildren.getInstance();
        this.games = savedGames();
        this.childrenList = loadChildren();
    }

    private ArrayList<String> loadChildren() {
        ArrayList<String> childrenTemp = new ArrayList<>();
        for (int i = 0; i < childrenConfig.getListSize(); i++) {
            childrenTemp.add(childrenConfig.getChild(i));
        }
        return childrenTemp;
    }

    public void saveGame() {
        System.out.println("saving string "+ createGameHistoryString());
        SharedPreferences sharedPref = this.context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GAME_HISTORY,this.createGameHistoryString());
        editor.apply();
    }

    /*
        GAME_HISTORY format string
            child name, child if picked head, if the games outcome was head, game time string .(SEPARATOR)
    */
    public String createGameHistoryString() {
        String history = "";

        for (int i = 0; i < this.games.size(); i++) {
            Game game = this.games.get(i);
            childPick child = game.getChild();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            history += child.getName() + "," + child.isHeads() + "," + game.isHead() + "," + game.getTime().format(formatter) + "%";
        }

        return history;
    }

    public ArrayList<Game> savedGames() {
        ArrayList<Game> savedGames = new ArrayList<>();
        SharedPreferences sharedPref = this.context.getPreferences(Context.MODE_PRIVATE);
        String history = sharedPref.getString(GAME_HISTORY, "");

        System.out.println("saved string == " + history);

        if (!history.equals("")) {
            String[] gamesArrayTemp = history.split("%");

            System.out.println("saved string to arr size " + gamesArrayTemp.length);
            for (String gamesArrayi : gamesArrayTemp) {

                String[] gameInstanceStringEncoded = gamesArrayi.split(",");

                Game gameInstance = new Game(null, this.coin);
                childPick child = new childPick("");
                int i = 0;

                for (String gameInstanceStringDecoded : gameInstanceStringEncoded) {

                    if (i == 0) {
                        child.setName(gameInstanceStringDecoded);
                    }
                    else if (i == 1) {
                        child.setHeads(Boolean.parseBoolean(gameInstanceStringDecoded));
                    }
                    else if (i == 2) {
                        gameInstance.setHead(Boolean.parseBoolean(gameInstanceStringDecoded));
                    }
                    else if (i == 3) {
                        gameInstance.setTime(gameInstanceStringDecoded);
                    }
                    i++;
                }
                gameInstance.setChild(child);
                savedGames.add(gameInstance);
            }
        }
        return savedGames;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public boolean isEmptyGames() {
        return games.isEmpty();
    }

    public ArrayList<String> getNextChildrenToPick() {
        ArrayList<String> childrenList = this.childrenList;

        if (isEmptyGames()) {
            ArrayList<String> childrenPlayed = new ArrayList<>();
            for (int i = 0; i < games.size(); i++) {
                Game gameInstance = games.get(i);
                childrenPlayed.add(gameInstance.getChild().getName());
            }
            childrenList.removeAll(childrenPlayed);
        }
        return childrenList;
    }
}
