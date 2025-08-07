/*@Habiba Javaid
Roll No: 0030.
*/


import java.io.*;

public class GameSystem {
    private static final String SAVE_FILE = "savegame.dat";
    private Player currentPlayer;

    public GameSystem() {
        this.currentPlayer = loadGame();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void createNewPlayer(String name) {
        this.currentPlayer = new Player(name, 1, 0);
    }

    public void updateGameProgress(int levelIncrease, int scoreIncrease) {
        currentPlayer.setLevel(currentPlayer.getLevel() + levelIncrease);
        currentPlayer.setScore(currentPlayer.getScore() + scoreIncrease);
    }

    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(currentPlayer);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private Player loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (Player) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No save file found. Starting new game.");
            return null;
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            return null;
        }
    }

    public static class Player implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int level, score;

        public Player(String name, int level, int score) {
            this.name = name;
            this.level = level;
            this.score = score;
        }

        public String getName() { return name; }
        public int getLevel() { return level; }
        public int getScore() { return score; }

        public void setLevel(int level) { this.level = level; }
        public void setScore(int score) { this.score = score; }

        @Override
        public String toString() {
            return String.format("%s (Level: %d, Score: %d)", name, level, score);
        }
    }
}