package pencil.abhishek.io.pencil.Package_wallpaper.Picasa;

import java.io.Serializable;

/**
 * Created by ABHISHEK on 2/9/2016.
 */
public class Wallpaper implements Serializable {

    private static final long serialVersionUid = 1L;

    private String url;
    private String photoJson;





    private String title;
    private int width , height;

    public Wallpaper( String photoJson, String url,String title,int width,int height) {
        this.width = width;
        this.photoJson = photoJson;
        this.height = height;
        this.url = url;
        this.title = title;


    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoJson() {
        return photoJson;
    }

    public void setPhotoJson(String photoJson) {
        this.photoJson = photoJson;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
