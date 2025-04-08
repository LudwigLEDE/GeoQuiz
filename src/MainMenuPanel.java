import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Hauptmenü-Oberfläche für das Quiz-Spiel.
 * <p>
 * Dieses Panel ermöglicht die Auswahl eines Quiz-Sets und den Spielstart.
 * Die gesamte Oberfläche wird ausschließlich über ein GridBagLayout angeordnet.
 * Der Benutzer kann per Tastatur das Quiz-Set auswählen (Taste 1 für "GeoQuiz Set 1", Taste 2 für "GeoQuiz Set 2")
 * und mit ENTER das Quiz starten.
 * Unten wird zudem ein Label mit den verfügbaren Steuerungstasten angezeigt.
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

        // Hintergrundfarbe setzen und GridBagLayout verwenden
        setBackground(AppColors.BACKGROUND);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // allgemeiner Abstand
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Zeile 0: Titel-Label
        titleLabel = new JLabel("Wählen Sie ein Quiz-Set", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.COPY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Zeile 1: Panel für Auswahl-Buttons
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
        add(selectionPanel, gbc);

        // Zeile 2: Start-Button – Wir packen den Button in einen eigenen Panel, damit die feste Größe erhalten bleibt.
        startButton = new JButton("Spiel Starten");
        styleButton(startButton, AppColors.SECONDARY, AppColors.SECONDARY_CONTENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        Dimension fixedSize = new Dimension(150, 50);
        startButton.setPreferredSize(fixedSize);
        startButton.setMaximumSize(fixedSize);
        startButton.setMinimumSize(fixedSize);
        startButton.setVisible(false); // zunächst unsichtbar

        // Erstelle einen Wrapper-Panel mit FlowLayout und ohne extra Padding.
        JPanel startButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        startButtonWrapper.setBackground(AppColors.BACKGROUND);
        startButtonWrapper.add(startButton);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Keine horizontale Ausdehnung
        gbc.anchor = GridBagConstraints.CENTER;
        add(startButtonWrapper, gbc);

        // Zeile 3: Manual-Label mit zusätzlichem Padding
        JLabel manualLabel = new JLabel("<html>Verfügbare Tasten:<br>" +
                "<br>- Controller:<br>" +
                "  • Pfeiltasten: Navigation<br>" +
                "<br>- Tastatur:<br>" +
                "  • Taste 1: GeoQuiz Set 1 auswählen<br>" +
                "  • Taste 2: GeoQuiz Set 2 auswählen<br>" +
                "  • ENTER: Quiz starten<br>" +
                "<br>- Zusätzlich:<br>" +
                "  • Maus: Klickbare Buttons zur Steuerung</html>");
        manualLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        manualLabel.setForeground(AppColors.COPY);
        manualLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Füge extra Padding mittels eines leeren Rahmens hinzu
        manualLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(manualLabel, gbc);

        // ActionListener für die Auswahl-Buttons (Maus-Klick)
        set1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectQuizSet("GeoQuiz Set 1");
            }
        });

        set2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectQuizSet("GeoQuiz Set 2");
            }
        });

        // ActionListener für den Start-Button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startQuiz();
            }
        });

        // Tastatur-Bindings für Quiz-Set Auswahl und Start
        setupKeyBindings();

        // Stelle sicher, dass das Panel fokussierbar ist
        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Richtet Tastatur-Bindings ein, um das Quiz-Set per Tastatur auszuwählen und das Quiz zu starten.
     */
    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Taste '1' wählt GeoQuiz Set 1
        im.put(KeyStroke.getKeyStroke('1'), "selectSet1");
        am.put("selectSet1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectQuizSet("GeoQuiz Set 1");
            }
        });

        // Taste '2' wählt GeoQuiz Set 2
        im.put(KeyStroke.getKeyStroke('2'), "selectSet2");
        am.put("selectSet2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectQuizSet("GeoQuiz Set 2");
            }
        });

        // ENTER-Taste startet das Quiz, wenn ein Set ausgewählt wurde
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startQuiz");
        am.put("startQuiz", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startQuiz();
            }
        });
    }

    /**
     * Setzt die Auswahl des Quiz-Sets und aktualisiert die Anzeige.
     *
     * @param setName Der Name des ausgewählten Quiz-Sets.
     */
    private void selectQuizSet(String setName) {
        this.selectedSetName = setName;
        titleLabel.setText(setName + " ausgewählt");
        startButton.setVisible(true);
        System.out.println("Ausgewähltes Quiz-Set: " + setName);
    }

    /**
     * Startet das Quiz, indem es das QuizSet lädt und ein QuizPanel erstellt.
     */
    private void startQuiz() {
        if (selectedSetName == null) {
            System.out.println("Kein Quiz-Set ausgewählt.");
            return;
        }
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
        QuizPanel quizPanel = new QuizPanel(new QuizGame(quizSet), new QuizFinishedListener() {
            public void quizFinished() {
                cardLayout.show(container, "MainMenu");
            }
        });

        // Optional: Füge einen KeyListener zum QuizPanel hinzu, um dort auch die Tastatursteuerung zu aktivieren.
        quizPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int selectedIndex = -1;
                if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
                    selectedIndex = 0;
                } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
                    selectedIndex = 1;
                } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
                    selectedIndex = 2;
                } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
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
