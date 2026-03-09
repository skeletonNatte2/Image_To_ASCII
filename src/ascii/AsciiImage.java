package ascii;

/**
 * supposed to be a class to represent ascii images
 * literally just a string lol
 * i have no idea why i made this its own class
 */
public class AsciiImage {
    private String imageString;

    public AsciiImage() {
        this.imageString = "";
    }

    public void setImage(String imageString) {
        this.imageString = imageString;
    }

    @Override
    public String toString() {
        return this.imageString;
    }
}
