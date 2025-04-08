import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Hauptmenü-Oberfläche für das Quiz-Spiel.
 * <p>
 * Dieses Panel ermöglicht die Auswahl eines Quiz-Sets und den Spielstart.
 * Unten (SOUTH) wird ein immer sichtbares Label mit allen verfügbaren
 * Steuerungstasten (inklusive Pfeiltasten bzw. W, A, S, D) angezeigt.
 * </p>
 */
public class MainMenuPanel extends JPanel {
    private String selectedSetName = null;
    private JLabel titleLabel;
    private JButton startButton;

    private CardLayout cardLayout;
    private JPanel container;

    /**
     * Konstruktor für das Hauptmenü-Panel.
     *
     * @param cardLayout Layout-Manager für den Container.
     * @param container  Der Container, in dem die Panels angezeigt werden.
     */
    public MainMenuPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        // Verwende BorderLayout für die Gesamtanordnung
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);

        // Zentrales Panel (CENTER) für Titel, Auswahl und Start-Button
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(AppColors.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titel-Label (zentriert)
        titleLabel = new JLabel("Wählen Sie ein Quiz-Set", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.COPY);
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        // Panel für Auswahl-Buttons (Quiz-Set Buttons)
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectionPanel.setBackground(AppColors.BACKGROUND);
        JButton set1Button = new JButton("GeoQuiz Set 1");
        JButton set2Button = new JButton("GeoQuiz Set 2");
        styleButton(set1Button, AppColors.PRIMARY, AppColors.PRIMARY_CONTENT);
        styleButton(set2Button, AppColors.PRIMARY, AppColors.PRIMARY_CONTENT);
        selectionPanel.add(set1Button);
        selectionPanel.add(set2Button);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(selectionPanel, gbc);

        // "Spiel Starten" Button (initial versteckt)
        startButton = new JButton("Spiel Starten");
        styleButton(startButton, AppColors.SECONDARY, AppColors.SECONDARY_CONTENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setVisible(false);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(startButton, gbc);

        // Füge das zentrale Panel in die CENTER-Region ein
        add(centerPanel, BorderLayout.CENTER);

        // Permanentes Label für die Bedienungsanleitung (SOUTH)
        JLabel manualLabel = new JLabel("<html>Verfügbare Tasten:<br>" +
                "<br>- Controller:<br>" +
                "  • Pfeiltasten (oben, unten, links, rechts): Navigation<br>" +
                "<br>- Tastatur:<br>" +
                "  • Pfeiltasten oder W, A, S, D: Antwortauswahl<br>" +
                "<br>- Zusätzlich:<br>" +
                "  • Maus: Klickbare Buttons zur Steuerung</html>");
        manualLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        manualLabel.setForeground(AppColors.COPY);
        manualLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(manualLabel, BorderLayout.SOUTH);

        // ActionListener für das erste Quiz-Set
        set1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 1";
                titleLabel.setText("GeoQuiz Set 1 ausgewählt");
                startButton.setVisible(true);
                System.out.println("Ausgewähltes Quiz-Set: " + selectedSetName);
            }
        });

        // ActionListener für das zweite Quiz-Set
        set2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 2";
                titleLabel.setText("GeoQuiz Set 2 ausgewählt");
                startButton.setVisible(true);
                System.out.println("Ausgewähltes Quiz-Set: " + selectedSetName);
            }
        });

        // ActionListener für den "Spiel Starten" Button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedSetName != null) {
                    String filePath = "";
                    if (selectedSetName.equals("GeoQuiz Set 1")) {
                        filePath = "data/quizsets/geoquiz_set1.json";
                    } else if (selectedSetName.equals("GeoQuiz Set 2")) {
                        filePath = "data/quizsets/geoquiz_set2.json";
                    }
                    QuizSet quizSet = QuizSetLoader.loadQuizSet(filePath);
                    if (quizSet == null) {
                        System.out.println("Fehler beim Laden des Quiz-Sets von: " + filePath);
                        return;
                    }
                    // Erstelle das QuizPanel mit Übergabe des QuizGame Managers und Callback zur Rückkehr ins Hauptmenü
                    QuizPanel quizPanel = new QuizPanel(new QuizGame(quizSet), new QuizFinishedListener() {
                        public void quizFinished() {
                            cardLayout.show(container, "MainMenu");
                        }
                    });

                    // KeyListener, der sowohl Pfeiltasten als auch W, A, S, D verarbeitet.
                    quizPanel.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            int keyCode = e.getKeyCode();
                            int selectedIndex = -1;
                            // Pfeiltasten
                            if (keyCode == KeyEvent.VK_LEFT) {
                                selectedIndex = 0;
                            } else if (keyCode == KeyEvent.VK_RIGHT) {
                                selectedIndex = 1;
                            } else if (keyCode == KeyEvent.VK_UP) {
                                selectedIndex = 2;
                            } else if (keyCode == KeyEvent.VK_DOWN) {
                                selectedIndex = 3;
                            }
                            // Alternativ: WASD
                            else if (keyCode == KeyEvent.VK_A) {
                                selectedIndex = 0;
                            } else if (keyCode == KeyEvent.VK_D) {
                                selectedIndex = 1;
                            } else if (keyCode == KeyEvent.VK_W) {
                                selectedIndex = 2;
                            } else if (keyCode == KeyEvent.VK_S) {
                                selectedIndex = 3;
                            }
                            if (selectedIndex != -1) {
                                quizPanel.selectAnswer(selectedIndex);
                            }
                        }
                    });
                    quizPanel.setFocusable(true);
                    quizPanel.requestFocusInWindow();

                    container.add(quizPanel, "QuizPanel");
                    cardLayout.show(container, "QuizPanel");
                }
            }
        });
    }

    /**
     * Hilfsmethode zum Stylen von Buttons mit den globalen Farben.
     *
     * @param button     Der zu formatierende Button.
     * @param background Hintergrundfarbe.
     * @param foreground Vordergrundfarbe.
     */
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(AppColors.BORDER));
        button.setFont(new Font("Arial", Font.BOLD, 18));
    }
}
