package ascii;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageManipulator {
    private static final String asciiByBrightness = " .',`-_:~^;|/\\+=<>()!{}[]?71%3$*205694&8@#";
    private static final int maxBrightness = 34;
    private static final double[] brightnessValues = {
            0,5,6,9,11,14,16,17,18,19,21,23,25,26,27,28,29,31,32,maxBrightness
    };
    private static final String[] uniqueBrightnessStrings = {
            " .-:~+!}?7%3$094&8@#",
            " .-:^=!{?713*564&8@#",
            " .`:;<!}?7%32X94&8@#",
            " .`:~>!{?713*X94&8@#"
    };

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

    public static AsciiImage toAsciiImage(Image image) {
        if(image == null) {
            return null;
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        AsciiImage result = new AsciiImage(width, height);

        PixelReader pixelReader = image.getPixelReader();

        StringBuilder asciiString = new StringBuilder();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(x, y);
                asciiString.append(argbToChar(pixel));
            }
            if(y < height - 1) {
                asciiString.append("\n");
            }
        }

        result.setImage(asciiString.toString());
        return result;
    }

    private static char argbToChar(int argb) {
        int r = (argb & 0x00FF0000) >> 16;
        int g = (argb & 0x0000FF00) >> 8;
        int b = argb & 0x000000FF;
        double rgbBrightness = (double)(r + g + b) / 3.0;
        rgbBrightness /= 255.0;
        rgbBrightness = Math.pow(rgbBrightness, 1.5);

        char rgbChar = ' ';

        double minDiff = Double.MAX_VALUE;
        int strIdx = 0;
        for(int i = 0; i < brightnessValues.length; i++) {
            double charBrightness = (brightnessValues[i] / maxBrightness);
            if(Math.abs(rgbBrightness - charBrightness) < minDiff) {
                minDiff = Math.abs(rgbBrightness - charBrightness);
                strIdx = i;
            }
        }

        String brightnessString = uniqueBrightnessStrings[(int)(Math.random()*4)];
        rgbChar = brightnessString.charAt(strIdx);

        return rgbChar;
    }
}
