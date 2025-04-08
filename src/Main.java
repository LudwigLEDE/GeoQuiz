import javax.swing.*;
import java.awt.*;

/**
 * Hauptklasse des Quiz-Spiels.
 * <p>
 * Diese Klasse startet das Spiel, indem sie ein Vollbild‑Fenster erstellt
 * und das Hauptmenü initialisiert.
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        // Erstelle ein JFrame im Vollbildmodus
        JFrame frame = new JFrame("Quiz Spiel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setze den Frame auf maximalen Bildschirm (Vollbild)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // CardLayout zur Verwaltung der Panels (z. B. Hauptmenü und Quiz)
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // Erstelle und füge das Hauptmenü-Panel hinzu
        MainMenuPanel mainMenuPanel = new MainMenuPanel(cardLayout, container);
        container.add(mainMenuPanel, "MainMenu");

        // Füge den Container dem Frame hinzu und zeige das Fenster an
        frame.add(container);
        frame.setVisible(true);
    }
}
