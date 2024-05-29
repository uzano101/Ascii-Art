package image_char_matching;

import java.util.Collection;
import java.util.HashMap;

/**
 * The SubImgCharMatcher class matches characters based on image brightness.
 */
public class SubImgCharMatcher {

    private static final char INITIAL_CHAR = '0';
    // Fields
    private HashMap<Character, Double[]> charHashMap;
    private double normMinVal = Double.POSITIVE_INFINITY;
    private double normMaxVal = Double.NEGATIVE_INFINITY;

    /**
     * Constructs a SubImgCharMatcher object with the given character set.
     *
     * @param charset The collection of characters to initialize the matcher.
     */
    public SubImgCharMatcher(char[] charset) {
        charHashMap = new HashMap<>();
        for (char c : charset) {
            addChar(c);
        }
        getMinAndMax(charHashMap.keySet());
    }

    /**
     * Gets the character corresponding to the closest brightness value.
     *
     * @param brightness The brightness value.
     * @return The character closest to the given brightness value.
     */
    public char getCharByImageBrightness(double brightness) {
        char returnChar = INITIAL_CHAR; // Initialize with default value
        double mostCloseBrightness = Double.POSITIVE_INFINITY;
        for (char currentChar : charHashMap.keySet()) {
            double tempDiff = Math.abs(charHashMap.get(currentChar)[0] - brightness);

            if (tempDiff < mostCloseBrightness) {
                mostCloseBrightness = tempDiff;
                returnChar = currentChar;
            } else if (tempDiff == mostCloseBrightness) {
                if ((int) currentChar < (int) returnChar) {
                    mostCloseBrightness = tempDiff;
                    returnChar = currentChar;
                }
            }
        }
        return returnChar;
    }

    /**
     * Adds a character to the character map.
     *
     * @param c The character to add.
     */
    public void addChar(char c) {
        double brightness = returnBrightness(c);
        double normalizedBrightness = normalize(brightness);
        if (brightness > normMaxVal) {
            normMaxVal = brightness;
            normalizedValuesUpdate();
        } else if (brightness < normMinVal) {
            normMinVal = brightness;
            normalizedValuesUpdate();
        }
        Double[] brightnessArray = {normalizedBrightness, brightness};
        charHashMap.put(c, brightnessArray);
    }

    /**
     * Removes a character from the character map.
     *
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        double brightness = returnBrightness(c);
        if (charHashMap.containsKey(c)){
            charHashMap.remove(c);
            if (brightness == normMinVal || brightness == normMaxVal) {
                updateMinAndMax(charHashMap.keySet());
            }
        }
    }

    /**
     * Calculates the brightness of a character.
     *
     * @param c The character.
     * @return The brightness value of the character.
     */
    private double returnBrightness(char c) {
        boolean[][] matrix = CharConverter.convertToBoolArray(c);
        int brightness = 0;
        for (boolean[] pixelsLine : matrix) {
            for (boolean pixel : pixelsLine) {
                if (pixel) {
                    brightness ++;
                }
            }
        }
        return brightness;
    }

    /**
     * Normalizes a value between normMinVal and normMaxVal.
     *
     * @param value The value to normalize.
     * @return The normalized value.
     */
    private double normalize(double value) {
        return (value - normMinVal) / (normMaxVal - normMinVal);
    }

    /**
     * Finds the minimum and maximum brightness values from the character set.
     *
     * @param charset The character set.
     */
    private void getMinAndMax(Collection<Character> charset) {
        for (char c : charset) {
            double brightness = returnBrightness(c);
            if (brightness > normMaxVal) {
                normMaxVal = brightness;
            }
            if (brightness < normMinVal) {
                normMinVal = brightness;
            }
        }
    }

    /**
     * Updates the minimum and maximum brightness values from the character set.
     *
     * @param charset The character set.
     */
    private void updateMinAndMax(Collection<Character> charset) {
        for (char c : charset) {
            double brightness = returnBrightness(c);
            if (brightness > normMaxVal) {
                normMaxVal = brightness;
            } else if (brightness < normMinVal) {
                normMinVal = brightness;
            }
        }
    }

    /**
     * Updates the normalized brightness values for characters in the map.
     */
    private void normalizedValuesUpdate() {
        for (Double[] charBrightnessArray : charHashMap.values()) {
            charBrightnessArray[0] = normalize(charBrightnessArray[1]);
        }
    }

    /**
     * Retrieves the character hash map.
     *
     * @return The character hash map.
     */
    public HashMap<Character, Double[]> getCharHashMap() {
        return charHashMap;
    }
}
