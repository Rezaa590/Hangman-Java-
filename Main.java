import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;

public class HangmaneGame {
    private static final Scanner scanner = new Scanner(System.in);

    private static final ArrayList<String> easyWords = new ArrayList<>();
    private static final ArrayList<String> mediumWords = new ArrayList<>();
    private static final ArrayList<String> mediumPhrases = new ArrayList<>();
    private static final ArrayList<String> hardPhrases = new ArrayList<>();

    private static final HashMap<String, Integer> scores = new HashMap<>();

    private static void initializeWords() {
        Collections.addAll(easyWords, "apple", "hello", "game", "tree", "water", "music", "light", "green",
                "book", "house", "cloud", "fire", "stone", "keyboard", "night");

        Collections.addAll(mediumWords, "computer", "science", "internet", "learning", "program", "language",
                "student", "teacher", "notebook", "software", "network", "website", "hardware", "coding", "database",
                "browser", "keyboard", "monitor", "debugger", "compile");

        Collections.addAll(mediumPhrases, "happy day", "big idea", "nice job", "good work", "well done",
                "study time", "final exam", "data science", "code review", "group project");

        Collections.addAll(hardPhrases, "game over", "think outside the box", "never give up",
                "reach for the stars", "stay positive", "hard work pays off", "dream big", "keep going",
                "a blessing in disguise", "the sky is the limit", "practice makes perfect",
                "every cloud has a silver lining", "break a leg", "easier said than done",
                "the early bird catches the worm");
    }

    private static String chooseDifficulty() {
        String difficulty;
        while (true) {
            System.out.print("Choose difficulty (Easy, Medium, Hard): ");
            difficulty = scanner.nextLine().trim().toLowerCase();
            if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {
                break;
            } else {
                System.out.println("Invalid difficulty. Try again.");
            }
        }
        return difficulty;
    }

    private static String getRandomWord(String difficulty) {
        Random random = new Random();
        switch (difficulty) {
            case "easy":
                return easyWords.get(random.nextInt(easyWords.size()));
            case "medium":
                ArrayList<String> medium = new ArrayList<>(mediumWords);
                medium.addAll(mediumPhrases);
                return medium.get(random.nextInt(medium.size()));
            case "hard":
                return hardPhrases.get(random.nextInt(hardPhrases.size()));
            default:
                return "error";
        }
    }

    private static int getMaxAttempts(String difficulty) {
        switch (difficulty) {
            case "easy":
                return 8;
            case "medium":
                return 6;
            case "hard":
                return 4;
            default:
                return 6;
        }
    }

    private static boolean playRound(String word, int maxAttempts) {
        HashSet<Character> guessedLetters = new HashSet<>();
        HashSet<Character> allLetters = new HashSet<>();
        int attemptsLeft = maxAttempts;

        for (int i = 0; i < word.length(); i++) {
            char ch = Character.toLowerCase(word.charAt(i));
            if (ch != ' ') {
                allLetters.add(ch);
            }
        }

        while (attemptsLeft > 0) {
            String displayWord = "";
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (ch == ' ') {
                    displayWord += "  ";
                } else if (guessedLetters.contains(Character.toLowerCase(ch))) {
                    displayWord += ch;
                } else {
                    displayWord += "_";
                }
            }

            System.out.println("\nWord: " + displayWord);
            System.out.println("Guessed Letters: " + guessedLetters);
            System.out.println("Attempts Left: " + attemptsLeft);
            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid input. Please enter a single letter (A-Z).");
                continue;
            }

            char guess = input.charAt(0);
            if (guessedLetters.contains(guess)) {
                System.out.println("You already guessed that letter.");
                continue;
            }

            guessedLetters.add(guess);

            if (word.toLowerCase().contains(guess + "")) {
                allLetters.remove(guess);
                if (allLetters.isEmpty()) {
                    System.out.println("\nYou won! The word/phrase was: " + word);
                    return true;
                }
            } else {
                attemptsLeft--;
                System.out.println("Incorrect guess.");
            }
        }

        System.out.println("\nYou lost! The correct word/phrase was: " + word);
        return false;
    }

    public static void main(String[] args) {
        initializeWords();

        System.out.println("Welcome to Hangman!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        if (!scores.containsKey(playerName)) {
            scores.put(playerName, 0);
        }

        boolean playAgain = true;

        while (playAgain) {
            String difficulty = chooseDifficulty();
            String word = getRandomWord(difficulty);
            System.out.println("A random word/ phrase has been selected. Let's begin!");
            int maxAttempts = getMaxAttempts(difficulty);

            boolean won = playRound(word, maxAttempts);

            if (won) {
                int currentScore = scores.get(playerName);
                int pointsEarned = 0;

                switch (difficulty) {
                    case "easy":
                        pointsEarned = 1;
                        break;
                    case "medium":
                        pointsEarned = 2;
                        break;
                    case "hard":
                        pointsEarned = 3;
                        break;
                }

                scores.put(playerName, currentScore + pointsEarned);
            }

            System.out.println("\nYour score: " + scores.get(playerName));
            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("yes")) {
                playAgain = false;
                System.out.println("Thanks for playing, " + playerName + "! Final score: " + scores.get(playerName));
            }
        }
    }
}