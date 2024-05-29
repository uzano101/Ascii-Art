package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageUtils;
import image.ImagePad;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.HashMap;

/**
 * The AsciiArtAlgorithm class represents an algorithm for generating ASCII art from an image.
 */
public class AsciiArtAlgorithm {

    private final ImageUtils div;
    private final Image[][] subImages;
    private final SubImgCharMatcher matcher;
    private final char[][] output;
    private final int resolution;
    private final Image img;
    private static final int DEFAULT_OUTPUT_RESOLUTION = 128;

    /**
     * Constructs an instance of AsciiArtAlgorithm.
     *
     * @param resolution The resolution of the output ASCII art.
     * @param img        The input image.
     * @param matcher    The SubImgCharMatcher instance for matching sub-images to characters.
     */
    public AsciiArtAlgorithm(int resolution, Image img, SubImgCharMatcher matcher) {
        super();
        this.div = new ImageUtils();
        this.output = new char[resolution][resolution];
        this.subImages = div.divideImage(img, resolution);
        this.matcher = matcher;
        this.resolution = resolution;
        this.img = img;
    }

    /**
     * Constructs an instance of AsciiArtAlgorithm with default resolution.
     *
     * @param img     The input image.
     * @param matcher The SubImgCharMatcher instance for matching sub-images to characters.
     */
    public AsciiArtAlgorithm(Image img, SubImgCharMatcher matcher) {
        this(DEFAULT_OUTPUT_RESOLUTION, img, matcher);
    }

    /**
     * Runs the ASCII art algorithm.
     *
     * @return The ASCII art represented as a 2D char array.
     */
    public char[][] run() {
        double[][] brightness = div.calculateBrightness(img, subImages, resolution);
        for (int i = 0; i < brightness.length; i++) {
            for (int j = 0; j < brightness[0].length; j++) {
                output[i][j] = matcher.getCharByImageBrightness(brightness[i][j]);
            }
        }
        return output;
    }
}
