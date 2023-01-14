package rmit.ad.myapplication.ModelClass;

public class Image {
    private int url;

    public Image(int resourceID) {
        this.url = resourceID;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }
}
