package Model;

import java.util.ArrayList;

/*
*
*   Coin has to be institated before using this Game handle
*
*           Reason:
*               passing of different Activty can lead to program
*               crash due to trying to access an activity which no longer exists
*
*
* */

/*
 shared preferences not implemented
 needs implementation
 */

public class GameManager {
    private ArrayList<Game> games = new ArrayList<>();
    private ArrayList<Child> children;

    // load configure children
    public GameManager(ArrayList<Child> children) {
        this.children = children;
    }

    public void addGame(Game game){
        games.add(game);
    }

    public boolean isEmptygames(){
        return games.isEmpty();
    }

    private Child getNextChild(){
        if (isEmptygames()) {
            Game lastGame = games.get(games.size() - 1);
            int size = children.size();

            for (int i = 0; i < size; i++) {
                if ((lastGame.getPicked().getName()).equals(children.get(i).getName())) {
                    // Check if Child found at end of array
                        if (i == (size - 1)){
                            return children.get(i - 1);
                        }
                        else{
                            return children.get(i + 1);
                        }
                }
            }
        }
        return null;
    }
}
