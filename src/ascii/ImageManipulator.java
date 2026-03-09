package ascii;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * class for handling image manipulation
 */
public class ImageManipulator {
    //private static final String asciiByBrightness = " .',`-_:~^;|/\\+=<>()!{}[]?71%3$*205694&8@#";
    private static final int maxBrightness = 33;
    private static final double[] brightnessValues = {
            0,2,4,6,8,14,16,17,18,19,21,23,25,26,27,28,29,31,32,maxBrightness
    };
    private static final String[] uniqueBrightnessStrings = {
            " .-:~+!}?7%3$094&8@#",
            " .-:^=!{?713*564&8@#",
            " .`:;<!}?7%32X94&8@#",
            " .`:~>!{?713*X94&8@#"
    };

    /**
     * pixelates an image
     *
     * @param image input image
     * @param pixelWidth desired width in pixels
     * @param pixelHeight desired height in pixels
     * @return pixelated image
     */
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
        //loop through each image pixelated image pixel
        for(int x = 0; x < pixelWidth; x++) {
            for(int y = 0; y < pixelHeight; y++) {
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                int count = 0;
                //average colors in this block
                for(int i = 0; i < blockWidth; i++) {
                    for(int j = 0; j < blockHeight; j++) {
                        int pixelX = Math.min((int)(x * blockWidth + i), (int)width - 1);
                        int pixelY = Math.min((int)(y * blockHeight + j), (int)height - 1);
                        int pixel = pixelReader.getArgb(pixelX, pixelY);

                        sumR += (pixel & 0x00FF0000) >> 16;
                        sumG += (pixel & 0x0000FF00) >> 8;
                        sumB += pixel & 0x000000FF;

                        count++;
                    }
                }

                //write average color to pixelated image
                int r = sumR / count;
                int g = sumG / count;
                int b = sumB / count;
                int argb = (0xFF << 24) | (r << 16) | (g << 8) | b;
                pixelWriter.setArgb(x, y, argb);
            }
        }

        return pixelated;
    }

    /**
     * turns given image into an ascii image
     *
     * @param image given image
     * @return ascii image
     */
    public static AsciiImage toAsciiImage(Image image) {
        if(image == null) {
            return null;
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        AsciiImage result = new AsciiImage();

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

    /**
     * converts an argb int to a char with similar brightness
     *
     * @param argb argb int
     * @return corresponding char
     */
    private static char argbToChar(int argb) {
        int r = (argb & 0x00FF0000) >> 16;
        int g = (argb & 0x0000FF00) >> 8;
        int b = argb & 0x000000FF;
        double rgbBrightness = (double)(r + g + b) / 3.0;
        rgbBrightness /= 255.0;
        rgbBrightness = -6 * Math.pow(rgbBrightness, 3.5) * (rgbBrightness/3 - 0.5);

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

        int randomIdx = (int)(Math.random()*uniqueBrightnessStrings.length);
        String brightnessString = uniqueBrightnessStrings[randomIdx];
        rgbChar = brightnessString.charAt(strIdx);

        return rgbChar;
    }
}
