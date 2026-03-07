package ascii;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Image input/output class
 */
public final class ImageIO {

    /**
     * A private constructor to forbid instantiating of the utility class
     * @throws AssertionError thrown if the constructor is called
     */
    private ImageIO() throws AssertionError {
        throw new AssertionError("ImageIO should not be instantiated");
    }

    /**
     * Reads an image file
     * @param path Path to file
     * @return Image that was read
     */
    public static Image read(Path path) {
        try {
            if(path != null) {
                BufferedImage bufferedImage = javax.imageio.ImageIO.read(path.toFile());
                return SwingFXUtils.toFXImage(bufferedImage, null);
            }
        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Gets a hex string of the form "#RRGGBB" from an argb int
     * of the form (in hexadecimal) aaRRGGBB.
     * @param value argb int
     * @return Hex string of the form "#RRGGBB"
     */
    private static String argbIntToRgbHex(int value) {
        final int rgbOnly = 0x00FFFFFF;
        int rgbOnlyValue = value & rgbOnly;
        return String.format("#%X", rgbOnlyValue);
    }
}

