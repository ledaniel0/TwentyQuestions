package Project5;

/**
 * Name: Daniel Le
 * Class: CS143
 * <p>
 * This class represents a question tree game. It manages a binary tree of question nodes
 * to guess the user's chosen object. The game progresses by navigating the tree based on
 * user responses to yes/no questions until an answer node is reached. The class provides
 * methods to play the game, save the tree to a file, load a tree from a file, and track
 * the number of games played and games won.
 * The overallRoot field represents the root node of the question tree.
 * The ui field represents the user interface for input/output.
 * The totalGames field keeps track of the total number of games played.
 * The gamesWon field keeps track of the number of games won.
 * Precondition: The user interface (ui) provided in the constructor must not be null.
 */

import java.io.PrintStream;
import java.util.Scanner;

public class QuestionTree {
    private QuestionNode overallRoot;
    private UserInterface ui;
    private int totalGames;
    private int gamesWon;

    public QuestionTree(UserInterface ui) {
        if (ui == null) {
            throw new IllegalArgumentException("UserInterface cannot be null");
        }

        this.ui = ui;
        overallRoot = new QuestionNode("computer");
    }

    /**
     * Plays the game by progressing through the question tree.
     * Starts from the given node and prompts the user with
     * questions to narrow down their chosen object. Continues
     * the game by following the appropriate path based on the
     * user's responses. Declares the winner when an answer node is reached.
     * <p>
     * One private method uses the starting node of the question tree.
     * Precondition: The provided node must not be null.
     */
    public void play() {
        playGame(overallRoot);
    }

    private void playGame(QuestionNode node) {
        if (node.isAnswer()) {
            ui.print("Would your object happen to be " + node.value + "?");
            totalGames++;
            if (ui.nextBoolean()) {
                ui.println("I win!");
                gamesWon++;
            } else {
                handleWrongGuess(node);
            }
        } else {
            ui.print(node.value);
            if (ui.nextBoolean()) {
                playGame(node.left);
            } else {
                playGame(node.right);
            }
        }
    }

    private void handleWrongGuess(QuestionNode node) {
        ui.print("I lose. What is your object?");
        String userObject = ui.nextLine();
        ui.print("Type a yes/no question to distinguish your item from " +
                node.getValue() + ":");
        String question = ui.nextLine();
        ui.print("And what is the answer for your object?");
        boolean userAnswer = ui.nextBoolean();

        QuestionNode newAnswer = new QuestionNode(userObject);
        QuestionNode newQuestion = new QuestionNode(question);

        if (userAnswer) {
            newQuestion.setLeft(newAnswer);
            newQuestion.setRight(node);
        } else {
            newQuestion.setLeft(node);
            newQuestion.setRight(newAnswer);
        }

        if (overallRoot == node) {
            overallRoot = newQuestion;
        } else {
            QuestionNode parentNode = findParentNode(overallRoot, node);
            if (parentNode.getLeft() == node) {
                parentNode.setLeft(newQuestion);
            } else {
                parentNode.setRight(newQuestion);
            }
        }
    }

    private QuestionNode findParentNode(QuestionNode currentNode, QuestionNode targetNode) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.getLeft() == targetNode || currentNode.getRight() == targetNode) {
            return currentNode;
        }

        QuestionNode leftResult = findParentNode(currentNode.getLeft(), targetNode);
        if (leftResult != null) {
            return leftResult;
        }

        QuestionNode rightResult = findParentNode(currentNode.getRight(), targetNode);
        if (rightResult != null) {
            return rightResult;
        }

        return null;
    }

    /**
     * Saves the tree into the file passed in.
     *
     * @param output The PrintStream representing the output file to save the tree.
     *               Preconditions: The output PrintStream parameter must not be null.
     * @throws IllegalArgumentException if the output PrintStream parameter is null.
     */
    public void save(PrintStream output) {
        if (output == null) {
            throw new IllegalArgumentException("PrintWriter output cannot be null");
        }

        saveTree(overallRoot, output);
        output.close();
    }

    private void saveTree(QuestionNode node, PrintStream output) {
        if (node != null) {
            if (node.isAnswer()) {
                output.println("A:" + node.getValue());
            } else {
                output.println("Q:" + node.getValue());
                saveTree(node.getLeft(), output);
                saveTree(node.getRight(), output);
            }
        }
    }

    /**
     * Loads a question tree from the specified input file.
     *
     * @param input The Scanner representing the input file to load the tree from.
     *              Preconditions: The input Scanner parameter 'input' must not be null.
     * @return The root node of the loaded question tree.
     * @throws IllegalArgumentException if the input Scanner parameter is null.
     * @throws IllegalArgumentException if there is an invalid node format in the input file.
     */
    public void load(Scanner input) {
        if (input == null) {
            throw new IllegalArgumentException("Scanner input cannot be null");
        }

        overallRoot = loadTree(input);
    }

    private QuestionNode loadTree(Scanner input) {
        if (!input.hasNextLine()) {
            return null;
        }

        String line = input.nextLine().trim();

        if (line.startsWith("Q:")) {
            String question = line.substring(2);
            QuestionNode node = new QuestionNode(question);
            node.setLeft(loadTree(input));
            node.setRight(loadTree(input));
            return node;
        } else if (line.startsWith("A:")) {
            String answer = line.substring(2);
            return new QuestionNode(answer);
        } else {
            throw new IllegalArgumentException("Invalid node format in the input file");
        }
    }

    /**
     * Returns the total number of games played.
     *
     * @return The total number of games played.
     * Precondition: The method playGame() must have been called previously to
     * update the totalGames variable.
     * Postcondition: The returned value represents the total number of games
     * played since the last reset.
     */
    public int totalGames() {
        return this.totalGames;
    }

    /**
     * Returns the number of games won.
     *
     * @return The number of games won.
     * Precondition: The method playGame() must have been called previously to
     * update the gamesWon variable.
     * Postcondition: The returned value represents the number of games won since
     * the last reset.
     */
    public int gamesWon() {
        return this.gamesWon;
    }
}
