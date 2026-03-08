package ascii;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageManipulator {

    public static Image pixelate(Image image, int pixelWidth, int pixelHeight) {
        if(image == null) {
            return null;
        }

        WritableImage pixelated = new WritableImage(pixelWidth, pixelHeight);

        double width = image.getWidth();
        double height = image.getHeight();

        double blockWidth = width / pixelWidth;
        double blockHeight = height / pixelHeight;

        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = pixelated.getPixelWriter();
        for(int x = 0; x < pixelWidth; x++) {
            for(int y = 0; y < pixelHeight; y++) {
                int pixelX = (int) Math.min(x*blockWidth, width-1);
                int pixelY = (int) Math.min(y*blockHeight, height-1);
                int pixel = pixelReader.getArgb(pixelX, pixelY);
                pixelWriter.setArgb(x, y, pixel);
            }
        }

        return pixelated;
    }
}
