package ascii;

public class AsciiImage {
    private String imageString;

    private int width;
    private int height;

    public AsciiImage(int width, int height) {
        this.imageString = "";

        this.width = width;
        this.height = height;
    }

    public void setImage(String imageString) {
        this.imageString = imageString;
    }

    public void setSize(int width,  int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return this.imageString;
    }
}
