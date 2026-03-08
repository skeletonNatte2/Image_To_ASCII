package ascii;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class Controller {
    @FXML
    private Text asciiText;
    @FXML
    private ImageView imageView;
    @FXML
    private Slider rowSlider;

    private Image image;
    private Image pixelatedImage;
    private AsciiImage asciiImage;

    private Font font;

    public Controller() {
        this.asciiImage = new AsciiImage(0,0);
        this.image = null;
    }

    @FXML
    public void initialize() {
        String fontName = "Mx437_Acer_VGA_8x8";
        try(InputStream inputStream = getClass().getResourceAsStream( fontName + ".ttf")) {
            if(inputStream != null) {
                this.font = Font.loadFont(inputStream,0);
            } else {
                throw new IOException( "Could not load font " + fontName + ".ttf" );
            }
        } catch (IOException e) {
            fontName = "Courier New";
            this.font = Font.font(fontName, 0);
            System.err.println("Font file not found!");
        }

        this.asciiText.setFont(this.font);

        updateRes();
    }

    @FXML
    private void updateRes() {
        double aspectRatio = 1.0;
        if(this.image != null) {
            aspectRatio = this.image.getWidth() / this.image.getHeight();
        }

        double sliderValue = rowSlider.getValue();

        int rows = (int) sliderValue;
        int cols  = (int) sliderValue;
        if(aspectRatio < 1.0) {
            cols = (int) (sliderValue * aspectRatio);
        } else {
            rows = (int) (sliderValue / aspectRatio);
        }
        this.asciiImage.setSize(cols, rows);

        double width = Math.floor(asciiText.getLayoutBounds().getWidth());
        double fontSize = width / this.rowSlider.getValue();
        this.font = new Font(this.font.getFamily(), fontSize);
        asciiText.setFont(this.font);

        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                sb.append((int)(Math.random()*10));
                //sb.append(Math.random() < 0.5 ? " " : "#");
            }
            if (r < rows - 1) {
                sb.append("\n");
            }
        }
        this.asciiImage.setImage(sb.toString());
        asciiText.setText(asciiImage.toString());

        this.pixelatedImage = ImageManipulator.pixelate(this.image,cols,rows);
        this.imageView.setImage(this.pixelatedImage);
    }

    @FXML
    public void open(Event e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Path selectedFilePath = fileChooser.showOpenDialog(stage).toPath();

        Image image = ImageIO.read(selectedFilePath);
        this.image = image;
        assert image != null;

        updateRes();

        this.imageView.setImage(this.pixelatedImage);
    }
}
