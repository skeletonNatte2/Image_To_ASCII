package ascii;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * Image input/output class
 */
public final class FileIO {

    /**
     * A private constructor to forbid instantiating of the utility class
     * @throws AssertionError thrown if the constructor is called
     */
    private FileIO() throws AssertionError {
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

    public static void saveAscii(AsciiImage asciiImage, Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ASCII Image");

        // default file name
        fileChooser.setInitialFileName("text_image.txt");

        // restrict to txt files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showSaveDialog(ownerWindow);

        if (file != null) {
            try (PrintWriter out = new PrintWriter(file)) {
                out.write(asciiImage.toString());
            } catch (Exception e) {
                System.err.println("Something went wrong: " + e.getMessage());
            }
        }
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

