package Model;

import static android.content.Context.MODE_PRIVATE;
import static ca.cmpt276.chlorinefinalproject.EditChildActivity.getChildrenSharedPreferences;

import android.app.Activity;
import android.content.SharedPreferences;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class GameManager {
    public static final String GAME_HISTORY = "gameHistory";
    public static final String PREFERENCES = "PREFERENCES";
    private final ArrayList<Game> games;
    private final ArrayList<String> childrenList;
    private final Activity context;

    public GameManager(Activity activity) {
        this.context = activity;
        this.games = getSavedGamesFromSharedPreferences();
        this.childrenList = (ArrayList<String>) getChildrenSharedPreferences(context.getApplicationContext());
    }

    public void saveGameToSharedPreference() {
        SharedPreferences sharedPref = this.context.getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GAME_HISTORY,this.createGameHistoryString());
        editor.apply();
    }

    // GAME_HISTORY format string
    // child name, child if picked head, if the games outcome was head, game time string
    public String createGameHistoryString() {
        StringBuilder history = new StringBuilder();

        for (int i = 0; i < this.games.size(); i++) {
            Game game = this.games.get(i);
            ChildPick child = game.getChild();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            history.append(child.getName()).append(",")
                    .append(child.isHeads()).append(",")
                    .append(game.isHead()).append(",")
                    .append(game.getTime().format(formatter))
                    .append("%");
        }
        return history.toString();
    }

    public ArrayList<Game> getSavedGamesFromSharedPreferences() {
        ArrayList<Game> savedGames = new ArrayList<>();
        SharedPreferences sharedPref = this.context.getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String history = sharedPref.getString(GAME_HISTORY, "");

        if (!history.equals("")) {
            String[] gamesArrayTemp = history.split("%");
            for (String games : gamesArrayTemp) {
                String[] gameInfo = games.split(",");

                Game gameInstance = new Game(null);
                ChildPick child = new ChildPick("");

                // Extracting data from index positions
                child.setName(gameInfo[0]);
                child.setHeads(Boolean.parseBoolean(gameInfo[1]));
                gameInstance.setHead(Boolean.parseBoolean(gameInfo[2]));
                gameInstance.setTime(gameInfo[3]);

                gameInstance.setChild(child);
                savedGames.add(gameInstance);
            }
        }
        return savedGames;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGameHistory(int index){
        if (index<getSavedGamesFromSharedPreferences().size()){
            this.games.remove(index);
            saveGameToSharedPreference();
        }
    }

    public boolean isEmptyGames() {
        return games.isEmpty();
    }

    public ArrayList<String> getNextChildrenToPick() {
        ArrayList<String> childrenList = this.childrenList;

        if (!isEmptyGames()) {
            for (int i = 0; i < games.size(); i++) {
                Game gameInstance = games.get(i);
                int foundAtIndex = valuePresentInArray(gameInstance.getChild().getName(), childrenList);
                if (foundAtIndex >- 1) {
                    childrenList.remove(foundAtIndex);
                }
            }
        }
        return childrenList;
    }

    private int valuePresentInArray(String value, ArrayList<String> arr){
        for (int i = 0; i < arr.size(); i++){
            if (value.equals(arr.get(i)))
                return i;
        }
        return -1;
    }
}
