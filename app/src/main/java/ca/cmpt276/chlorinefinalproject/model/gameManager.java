package ca.cmpt276.chlorinefinalproject.model;

import android.app.Activity;
import android.widget.ImageView;

import java.util.ArrayList;

/*
*
*   Coin has to be institated before using this game handle
*
*           Reason:
*               passing of different Activty can lead to program
*               crash due to trying to access an activity which no longer exists
*
*
* */

public class gameManager {

    private ArrayList<game> games = new ArrayList<game>();

    private ArrayList<child> children = new ArrayList<child>();

    public gameManager(ArrayList<child> children) {
        this.children = children;
    }

    public void addGame(game game){ games.add(game); }

    public boolean isEmptygames(){
        return games.isEmpty();
    }

    private child nextchild(){


        if (isEmptygames()) {

            game lastgame = games.get(games.size()-1);

            int size = children.size();

            for (int i = 0; i < size; i++) {

                if ((lastgame.getPicked().getName()).equals(children.get(i).getName())) {

                    //check if child found at end of array

                        if (i==(size-1)){

                            return children.get(i-1);

                        }else{

                            return children.get(i+1);

                        }
                }

            }
        }

        return null;
    }



}
