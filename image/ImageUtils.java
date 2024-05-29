package image;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * The ImageUtils class provides utility methods for image processing.
 */
public class ImageUtils {

    // Constants
    private static final double RED_WEIGHT = 0.2126;
    private static final double GREEN_WEIGHT = 0.7152;
    private static final double BLUE_WEIGHT = 0.0722;
    private static final double MAX_PIXEL_VALUE = 255.0;
    private static final double MIN_PIXEL_VALUE = 0.0;

    /**
     * Divides the given image into a grid of sub-images with the specified resolution.
     *
     * @param image      The original image to divide.
     * @param resolution The resolution for dividing the image.
     * @return A 2D array of sub-images.
     */
    public Image[][] divideImage(Image image, int resolution) {
        final int rows = image.getHeight() / resolution;
        final int cols = image.getWidth() / resolution;

        Image[][] subImages = new Image[resolution][resolution];

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                final int startY = i * rows;
                final int startX = j * cols;
                Color[][] pixelArray = new Color[rows][cols];

                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < cols; x++) {
                        pixelArray[y][x] = image.getPixel(startY + y, startX + x);
                    }
                }

                subImages[i][j] = new Image(pixelArray, rows, cols);
            }
        }

        return subImages;
    }

    /**
     * Calculates the brightness of each sub-image in the provided array of images.
     * Caches the calculated brightness values for future use.
     *
     * @param img        The original image.
     * @param images     The array of sub-images.
     * @param resolution The resolution of the sub-images.
     * @return A 2D array containing the brightness values of each sub-image.
     */
    public double[][] calculateBrightness(Image img, Image[][] images, int resolution) {
        if (img.getBrightnessCache().containsKey(resolution)) {
            return img.getBrightnessCache().get(resolution);
        }

        double[][] brightnesses = new double[resolution][resolution];

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                brightnesses[i][j] = calculateSubImageBrightness(images[i][j]);
            }
        }

        img.putBrightnessCache(resolution, brightnesses);
        return brightnesses;
    }

    /**
     * Calculates the brightness of a sub-image.
     *
     * @param image The sub-image.
     * @return The brightness value of the sub-image.
     */
    private double calculateSubImageBrightness(Image image) {
        final int rows = image.getHeight();
        final int cols = image.getWidth();
        final int totalPixels = rows * cols;
        double totalBrightness = MIN_PIXEL_VALUE;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Color pixelColor = image.getPixel(x, y);
                double grayValue = pixelColor.getRed() * RED_WEIGHT + pixelColor.getGreen() *
                        GREEN_WEIGHT + pixelColor.getBlue() * BLUE_WEIGHT;
                totalBrightness += grayValue / MAX_PIXEL_VALUE; // Normalize to range 0-1
            }
        }

        // Calculate average brightness for the sub-image
        return totalBrightness / totalPixels;
    }
}
