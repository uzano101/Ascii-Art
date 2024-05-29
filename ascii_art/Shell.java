package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImagePad;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.*;

/**
 * Shell class represents a shell interface for generating ASCII art from images.
 */
public class Shell {
    // Constants
    private static final String WAIT_FOR_INPUT = ">>> ";
    private static final String EXIT_REQUEST = "exit";
    private static final String DEFAULT_OUTPUT_FONT = "Courier New";
    private static final String DEFAULT_OUTPUT_NAME = "out.html";
    private static final String DEFAULT_OUTPUT_FORMAT = "console";
    private static final int DEFAULT_RESOLUTION = 128;
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final int NUMBERS_START_ASCII = 48; // ASCII value for '0'
    private static final int LEGAL_START_ASCII = 32; // ASCII value for space
    private static final int NUMBERS_END_ASCII = 57; // ASCII value for '9'
    private static final int LEGAL_END_ASCII = 126; // ASCII value for '~'
    private static final String COMMAND_CHARS = "chars";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_RES = "res";
    private static final String COMMAND_IMAGE = "image";
    private static final String COMMAND_OUTPUT = "output";
    private static final String COMMAND_ASCII_ART = "asciiArt";
    private static final String ALL_CHARS = "all";
    private static final String HTML_OUTPUT_FORMAT = "html";
    private static final String CONSOLE_OUTPUT_FORMAT = "console";
    private static final String OUTPUT_INCORRECT_FORMAT = "Did not change output method due to " +
            "incorrect format.";
    private static final String ADD_INCORRECT_FORMAT = "Did not add due to incorrect format.";
    private static final String REMOVE_INCORRECT_FORMAT = "Did not remove due to incorrect format.";
    private static final String SPACE = " ";
    private static final String RES_INCORRECT_FORMAT = "Did not change resolution due to incorrect format.";
    private static final String RES_EXCEED_BOUND = "Did not change resolution due to exceeding boundaries.";
    private static final String RES_UPDATE_MSG = "Resolution set to ";
    private static final String RES_UP = "up";
    private static final String RES_DOWN = "down";
    private static final String SPACE_STRING = "space";
    private static final String IMAGE_INCORRECT_FORMAT = "Did not execute due to problem with image file.";
    private static final String EMPTY_CHARS_SET = "Did not execute. Charset is empty.";
    private static final String COMMAND_INCORRECT = "Did not execute due to incorrect command.";
    private static final char RANGE_SEP = '-';
    private static final int SPACE_ASCII = 32;
    private static final int VALID_REQUEST_LENGTH = 2;
    private static final int ONE_LETTER_FORMAT_AMOUNT = 1;
    private static final int RANGE_OF_LETTERS_FORMAT_AMOUNT = 3;
    private char[] charset;
    private static SubImgCharMatcher matcher;
    private static int resolution;
    private static Image img;
    private static String outputFormat;

    // Functional interface mapping
    private static final HashMap<String, FuncWithArgs> REQUESTS = new HashMap<>() {{
        put(COMMAND_CHARS, Shell::printChars);
        put(COMMAND_ADD, Shell::addChars);
        put(COMMAND_REMOVE, Shell::removeChars);
        put(COMMAND_RES, Shell::resolutionController);
        put(COMMAND_IMAGE, Shell::pathController);
        put(COMMAND_OUTPUT, Shell::outputController);
        put(COMMAND_ASCII_ART, Shell::asciiArtAlgorithm);
    }};

    /**
     * Constructs a Shell instance and initializes default values.
     */
    public Shell() {
        super();
        initCharSet();
        resolution = DEFAULT_RESOLUTION;
        outputFormat = DEFAULT_OUTPUT_FORMAT;
        matcher = new SubImgCharMatcher(charset);
        try {
            loadImage(DEFAULT_IMAGE_PATH);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Entry point of the program.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Shell().run();
    }

    /**
     * Runs the shell interface.
     */
    public void run() {
        String input;
        while (true) {
            input = getInput();
            String request = input.split(SPACE)[0];
            if (input.equals(EXIT_REQUEST)) {
                break;
            }
            if (!REQUESTS.containsKey(request)){
                System.out.println(COMMAND_INCORRECT);
                continue;
            }
            try {
                REQUESTS.get(request).run(input);
            } catch (IOException | ExceedBound | IllegalRequest e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void loadImage(String path) throws IOException{
        try {
            img = new ImagePad(path);
        } catch (IOException e) {
            throw new IOException(IMAGE_INCORRECT_FORMAT);
        }
    }

    /**
     * Initializes the character set with ASCII characters.
     */
    private void initCharSet() {
        charset = new char[10];
        for (int i = NUMBERS_START_ASCII; i <= NUMBERS_END_ASCII; i++) {
            charset[i - NUMBERS_START_ASCII] = (char) i;
        }
    }

    /**
     * Prompts the user for input.
     *
     * @return The user's input.
     */
    private String getInput() {
        System.out.print(WAIT_FOR_INPUT);
        return KeyboardInput.readLine();
    }

    /**
     * Adds characters to the character set based on input range or single character.
     *
     * @param start The start ASCII value of the range.
     * @param end   The end ASCII value of the range.
     */
    private static void addToChars(int start, int end) {
        // Ensure start is less than or equal to end
        if (start > end) {
            int temp = end;
            end = start;
            start = temp;
        }
        for (int i = start; i <= end; i++) {
            matcher.addChar((char) i);
        }
    }

    /**
     * Removes characters from the character set based on input range or single character.
     *
     * @param start The start ASCII value of the range.
     * @param end   The end ASCII value of the range.
     */
    private static void deleteChars(int start, int end) {
        // Ensure start is less than or equal to end
        if (start > end) {
            int temp = end;
            end = start;
            start = temp;
        }
        for (int i = start; i <= end; i++) {
            matcher.removeChar((char) i);
        }
    }

    /**
     * Prints the characters in the character set in ascending order of ASCII values.
     *
     * @param request The request string (unused here).
     */
    private static void printChars(String request) {
        List<Map.Entry<Character, Double[]>> listOfChars =
                new ArrayList<>(matcher.getCharHashMap().entrySet());
        listOfChars.sort(Map.Entry.comparingByKey());
        for (Map.Entry<Character, Double[]> entry : listOfChars) {
            System.out.print(entry.getKey() + SPACE);
        }
        System.out.println();
    }

    /**
     * Adds characters to the character set based on input range or single character.
     *
     * @param request The request string containing the characters to add.
     */
    private static void addChars(String request) throws IOException{
        String[] requestTokens = request.split(SPACE);
        if (requestTokens.length != VALID_REQUEST_LENGTH) {
            throw new IOException(ADD_INCORRECT_FORMAT);
        }
        char[] contentToAdd = requestTokens[1].toCharArray();
        if (requestTokens[1].equals(ALL_CHARS)) {
            addToChars(LEGAL_START_ASCII, LEGAL_END_ASCII);
        } else if (contentToAdd.length == ONE_LETTER_FORMAT_AMOUNT &&
                LEGAL_START_ASCII <= contentToAdd[0] && contentToAdd[0] <= LEGAL_END_ASCII) {
            addToChars(contentToAdd[0], contentToAdd[0]);
        } else if (contentToAdd.length == RANGE_OF_LETTERS_FORMAT_AMOUNT && contentToAdd[1] == RANGE_SEP) {
            addToChars(contentToAdd[0], contentToAdd[2]);
        } else if (requestTokens[1].equals(SPACE_STRING)) {
            addToChars(SPACE_ASCII, SPACE_ASCII);
        } else {
            throw new IOException(ADD_INCORRECT_FORMAT);
        }
    }

    /**
     * Removes characters from the character set based on input range or single character.
     *
     * @param request The request string containing the characters to remove.
     */
    private static void removeChars(String request) throws IOException{
        String[] requestTokens = request.split(SPACE);
        if (requestTokens.length != VALID_REQUEST_LENGTH) {
            throw new IOException(REMOVE_INCORRECT_FORMAT);
        }
        char[] contentToAdd = requestTokens[1].toCharArray();
        if (requestTokens[1].equals(ALL_CHARS)) {
            deleteChars(LEGAL_START_ASCII, LEGAL_END_ASCII);
        } else if (contentToAdd.length == ONE_LETTER_FORMAT_AMOUNT &&
                LEGAL_START_ASCII <= contentToAdd[0] && contentToAdd[0] <= LEGAL_END_ASCII) {
            deleteChars(contentToAdd[0], contentToAdd[0]);
        } else if (contentToAdd.length == RANGE_OF_LETTERS_FORMAT_AMOUNT && contentToAdd[1] == RANGE_SEP) {
            deleteChars(contentToAdd[0], contentToAdd[2]);
        } else if (requestTokens[1].equals(SPACE_STRING)) {
            deleteChars(SPACE_ASCII, SPACE_ASCII);
        } else {
            throw new IOException(REMOVE_INCORRECT_FORMAT);
        }
    }

    /**
     * Controls the resolution of the ASCII art.
     *
     * @param request The request string containing the action (up/down).
     */
    private static void resolutionController(String request) throws IOException, ExceedBound {
        String[] requestTokens = request.split(SPACE);
        if (requestTokens.length != VALID_REQUEST_LENGTH) {
            throw new IOException(RES_INCORRECT_FORMAT);
        }
        if (requestTokens[1].equals(RES_UP)) {
            if (resolution * 2 <= img.getWidth()) {
                resolution *= 2;
                System.out.println(RES_UPDATE_MSG + resolution);
            } else {
                throw new ExceedBound(RES_EXCEED_BOUND);
            }
        } else if (requestTokens[1].equals(RES_DOWN)) {
            if (resolution / 2 >= Math.max(1, img.getWidth() / img.getHeight())) {
                resolution /= 2;
                System.out.println(RES_UPDATE_MSG + resolution);
            } else {
                throw new ExceedBound(RES_EXCEED_BOUND);
            }
        } else {
            throw new IOException(RES_INCORRECT_FORMAT);
        }
    }

    /**
     * Controls the image path.
     *
     * @param request The request string containing the new image path.
     */
    private static void pathController(String request) throws IOException{
        String[] requestTokens = request.split(SPACE);
        if (requestTokens.length != VALID_REQUEST_LENGTH) {
            throw new IOException(IMAGE_INCORRECT_FORMAT);
        }
        loadImage(requestTokens[1]);
    }

    /**
     * Controls the output format (HTML or console).
     *
     * @param request The request string containing the output format.
     */
    private static void outputController(String request) throws IOException{
        String[] requestTokens = request.split(SPACE);
        if (requestTokens.length != VALID_REQUEST_LENGTH) {
            throw new IOException(OUTPUT_INCORRECT_FORMAT);
        }
        if (requestTokens[1].equals(HTML_OUTPUT_FORMAT)) {
            outputFormat = HTML_OUTPUT_FORMAT;
        } else if (requestTokens[1].equals(CONSOLE_OUTPUT_FORMAT)) {
            outputFormat = CONSOLE_OUTPUT_FORMAT;
        } else {
            throw new IOException(OUTPUT_INCORRECT_FORMAT);
        }
    }

    /**
     * Executes the ASCII art algorithm.
     *
     * @param request The request string (unused here).
     */
    private static void asciiArtAlgorithm(String request) throws IllegalRequest {
        if (matcher.getCharHashMap().isEmpty()){
            throw new IllegalRequest(EMPTY_CHARS_SET);
        }
        AsciiOutput outputExecutor;
        if (outputFormat.equals(HTML_OUTPUT_FORMAT)) {
            outputExecutor = new HtmlAsciiOutput(DEFAULT_OUTPUT_NAME, DEFAULT_OUTPUT_FONT);
        } else {
            outputExecutor = new ConsoleAsciiOutput();
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(resolution, img, matcher);
        char[][] output = asciiArtAlgorithm.run();
        outputExecutor.out(output);
    }
}
