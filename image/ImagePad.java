package image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This class extends the Image class and provides functionality to pad an image
 * to make its dimensions power of two.
 */
public class ImagePad extends Image {
    private static final Color DEFAULT_PADDING_COLOR = Color.WHITE;
    private static final int EMPTY_PIXEL = 0;

    /**
     * Constructs an ImagePad object with the specified filename.
     *
     * @param filename The filename of the image to pad.
     * @throws IOException If an I/O error occurs.
     */
    public ImagePad(String filename) throws IOException {
        super(filename);
        padImage();
    }

    /**
     * Pads the image to make its dimensions power of two.
     */
    private void padImage() {
        int newWidth = closestPowerOfTwo(getWidth());
        int newHeight = closestPowerOfTwo(getHeight());

        if (newWidth == getWidth() && newHeight == getHeight()) {
            return; // No need to pad if dimensions are already power of two
        }

        BufferedImage paddedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        // Fill the image with white.
        Graphics2D g2d = paddedImage.createGraphics();
        g2d.setColor(DEFAULT_PADDING_COLOR);
        g2d.fillRect(EMPTY_PIXEL, EMPTY_PIXEL, paddedImage.getWidth(), paddedImage.getHeight());
        g2d.dispose();

        // Calculate padding on left and top sides
        int padLeft = (newWidth - getWidth()) / 2;
        int padTop = (newHeight - getHeight()) / 2;

        // Copy original pixels to padded image with symmetric padding
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                paddedImage.setRGB(j + padLeft, i + padTop, getPixel(i, j).getRGB());
            }
        }

        // Fill remaining pixels with white
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (paddedImage.getRGB(j, i) == EMPTY_PIXEL) { // Check if pixel is empty
                    paddedImage.setRGB(j, i, DEFAULT_PADDING_COLOR.getRGB());
                }
            }
        }

        // Update image dimensions and pixel array
        super.pixelArray = new Color[newHeight][newWidth];
        super.width = newWidth;
        super.height = newHeight;

        // Set pixel array from padded image
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                super.pixelArray[i][j] = new Color(paddedImage.getRGB(j, i));
            }
        }
    }

    /**
     * Finds the closest power of two to the given number.
     *
     * @param number The number for which to find the closest power of two.
     * @return The closest power of two to the given number.
     */
    private int closestPowerOfTwo(int number) {
        // If the number is already a power of 2, number - 1 is the opposite of number, bitwise.
        if ((number & (number - 1)) == EMPTY_PIXEL) {
            return number;
        }

        // Position will be the most left turned bit of number.
        int position = 0;
        while (number > EMPTY_PIXEL) {
            number >>= 1;
            position++;
        }

        // Shift 1 to the left position times to get the upper closest power of two.
        return 1 << position;
    }
}
