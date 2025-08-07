
/*@Habiba Javaid
Roll No:0030.
*/

import java.io.*;

public class GameDataManager {
    private static final String SAVE_FILE = "savegame.dat";

    public static void saveGame(Player player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    public static Player loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Player player = (Player) ois.readObject();
            System.out.println("Game loaded successfully.");
            return player;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}