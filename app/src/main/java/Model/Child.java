package Model;

import android.graphics.Bitmap;

// Object representing a configurable child
public class Child {
    private String name;
    private Bitmap image;

    public Child(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // returns bitmap? imageview?
    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        if (image != null){
            this.image = image;
        }
    }
}
