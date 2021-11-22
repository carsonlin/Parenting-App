package Model;

import android.graphics.Bitmap;

// Object representing a configurable child
public class Child {
    private String name;
    private Bitmap image;
    private String filePath;

    public Child(String name, Bitmap image, String filePath){
        this.name = name;
        this.image = image;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        if (image != null){
            this.image = image;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
