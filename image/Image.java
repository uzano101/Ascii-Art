package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {

    private static final String FILE_SUFFIX_FORMAT = "jpeg";
    private static final String FILE_PRE_SUF_SEPARATOR = ".";

    private Map<Integer, double[][]> brightnessCache;

    Color[][] pixelArray;

    int width;

    int height;

    /**
     * Constructs an Image object from the specified file.
     *
     * @param filename The path to the image file.
     * @throws IOException If an I/O error occurs while reading the image file.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();
        brightnessCache = new HashMap<>();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs an Image object from a 2D array of colors.
     *
     * @param pixelArray The 2D array of colors representing the pixels of the image.
     * @param width      The width of the image.
     * @param height     The height of the image.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
        brightnessCache = new HashMap<>();
    }

    /**
     * Retrieves the width of the image.
     *
     * @return The width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the image.
     *
     * @return The height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the color of the pixel at the specified coordinates.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The color of the pixel at the specified coordinates.
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Retrieves the brightnessCache of the image.
     *
     * @return The brightnessCache of the image.
     */
    public Map<Integer, double[][]> getBrightnessCache(){
        return brightnessCache;
    }

    /**
     * Puts new key-Value to the brightnessCache of the image.
     */
    public void putBrightnessCache(Integer resolution, double[][] brightnesses){
        brightnessCache.put(resolution, brightnesses);
    }

    /**
     * Saves the image to a file with the specified file name.
     *
     * @param fileName The name of the file to save the image to.
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName + FILE_PRE_SUF_SEPARATOR + FILE_SUFFIX_FORMAT);
        try {
            ImageIO.write(bufferedImage, FILE_SUFFIX_FORMAT, outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
