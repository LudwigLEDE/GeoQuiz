import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ein Panel, das eine Quiz-Frage und deren Antwortoptionen anzeigt.
 * <p>
 * Die Antwortoptionen werden für jede Frage zufällig angeordnet. Nach der Auswahl
 * einer Antwort wird visuelles Feedback gegeben, und nach einer kurzen Verzögerung
 * die nächste Frage geladen. Zusätzlich wird am unteren Rand ein Manual-Label
 * angezeigt, welches über die Tastatursteuerung informiert.
 * </p>
 */
public class QuizPanel extends JPanel {
    private QuizGame quizGame;
    private JLabel scoreLabel;
    private JLabel questionLabel;
    private JLabel questionImageLabel;
    private JPanel answersPanel;
    private int correctCount;
    private List<JButton> answerButtons;
    // Liste für zufällig sortierte Antwortoptionen der aktuellen Frage
    private List<AnswerOption> currentAnswerOptions;
    private QuizFinishedListener finishedListener;

    // Permanentes Manual-Label, das Informationen zur Tastatursteuerung anzeigt
    private JLabel manualLabel;

    /**
     * Konstruktor für das QuizPanel.
     *
     * @param quizGame         Der Spielmanager, der die Quiz-Fragen enthält.
     * @param finishedListener Callback, der aufgerufen wird, wenn das Quiz beendet ist.
     */
    public QuizPanel(QuizGame quizGame, QuizFinishedListener finishedListener) {
        this.quizGame = quizGame;
        this.finishedListener = finishedListener;
        correctCount = 0;
        answerButtons = new ArrayList<>();

        setLayout(new GridBagLayout());
        setBackground(AppColors.BACKGROUND);

        // Initialisiere UI-Komponenten (inklusive Manual-Label)
        initializeComponents();
        // Füge den KeyListener hinzu, um auch über die Tastatur zu steuern
        addCustomKeyListener();
        // Stelle sicher, dass dieses Panel den Fokus erhält
        setFocusable(true);
        requestFocusInWindow();

        displayCurrentQuestion();
    }

    /**
     * Initialisiert die UI-Komponenten mithilfe von GridBagLayout.
     */
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Score-Label oben, über zwei Spalten
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(AppColors.COPY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(scoreLabel, gbc);

        // Frage-Label
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 22));
        questionLabel.setForeground(AppColors.COPY);
        gbc.gridy = 1;
        add(questionLabel, gbc);

        // Fragebild-Label
        questionImageLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 2;
        add(questionImageLabel, gbc);

        // Antworten-Panel
        answersPanel = new JPanel(new GridBagLayout());
        answersPanel.setBackground(AppColors.FOREGROUND);
        gbc.gridy = 3;
        add(answersPanel, gbc);

        // Permanentes Manual-Label am unteren Rand (Zeile 4)
        manualLabel = new JLabel("<html>Verfügbare Tasten:<br>" +
                "- Pfeiltasten oder W, A, S, D: Antwortauswahl</html>", SwingConstants.CENTER);
        manualLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        manualLabel.setForeground(AppColors.COPY);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(manualLabel, gbc);
    }

    /**
     * Fügt einen KeyListener hinzu, der sowohl Pfeiltasten als auch W, A, S, D
     * zur Antwortauswahl verarbeitet und eine Konsolen-Nachricht loggt.
     */
    private void addCustomKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int selectedIndex = -1;
                // Pfeiltasten: Links->Antwort 0, Rechts->Antwort 1, Oben->Antwort 2, Unten->Antwort 3
                if (keyCode == KeyEvent.VK_LEFT) {
                    selectedIndex = 0;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    selectedIndex = 1;
                } else if (keyCode == KeyEvent.VK_UP) {
                    selectedIndex = 2;
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    selectedIndex = 3;
                }
                // Alternative Tasten: A->Antwort 0, D->Antwort 1, W->Antwort 2, S->Antwort 3
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
                    // Logge den Tastendruck und die aktuell angezeigte Frage (falls vorhanden)
                    System.out.println("Taste gedrückt: " + KeyEvent.getKeyText(keyCode));
                    if (quizGame.getCurrentQuestion() != null) {
                        System.out.println("Aktuelle Frage: " + quizGame.getCurrentQuestion().getQuestionText());
                    } else {
                        System.out.println("Keine Frage aktuell verfügbar.");
                    }
                    selectAnswer(selectedIndex);
                }
            }
        });
    }

    /**
     * Zeigt die aktuelle Frage an, skaliert und setzt das Bild und mischt die Antwortoptionen.
     */
    public void displayCurrentQuestion() {
        Question question = quizGame.getCurrentQuestion();
        if (question == null) {
            // Keine weiteren Fragen: Zeige End-Punktzahl und Rückkehr-Button
            removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel finishLabel = new JLabel("Quiz beendet! Endpunktzahl: " + correctCount, SwingConstants.CENTER);
            finishLabel.setFont(new Font("Arial", Font.BOLD, 24));
            finishLabel.setForeground(AppColors.COPY);
            add(finishLabel, gbc);

            gbc.gridy = 1;
            JButton returnButton = new JButton("Zum Hauptmenü");
            returnButton.setFont(new Font("Arial", Font.BOLD, 20));
            returnButton.setBackground(AppColors.SECONDARY);
            returnButton.setForeground(AppColors.SECONDARY_CONTENT);
            returnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    finishedListener.quizFinished();
                }
            });
            add(returnButton, gbc);

            revalidate();
            repaint();
            return;
        }

        // Setze Fragetext
        questionLabel.setText(question.getQuestionText() != null ? question.getQuestionText() : "");

        // Skalieren des Bildes auf feste Maximalmaße (z.B. 500x300 Pixel)
        if (question.getQuestionImage() != null) {
            ImageIcon originalIcon = question.getQuestionImage();
            int maxWidth = 500;
            int maxHeight = 300;
            Image scaledImage = originalIcon.getImage().getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            questionImageLabel.setIcon(scaledIcon);
        } else {
            questionImageLabel.setIcon(null);
        }

        // Mische die Antwortoptionen zufällig
        List<AnswerOption> answers = question.getAnswerOptions();
        currentAnswerOptions = new ArrayList<>(answers);
        Collections.shuffle(currentAnswerOptions);

        // Entferne vorherige Antwortbuttons und erstelle neue Buttons
        answersPanel.removeAll();
        answerButtons.clear();
        int numAnswers = currentAnswerOptions.size();
        GridBagConstraints gbcAns = new GridBagConstraints();
        gbcAns.insets = new Insets(5, 5, 5, 5);
        gbcAns.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < numAnswers; i++) {
            AnswerOption answer = currentAnswerOptions.get(i);
            JButton btn = new JButton();
            btn.setFont(new Font("Arial", Font.PLAIN, 18));
            btn.setBackground(AppColors.PRIMARY_LIGHT);
            btn.setForeground(AppColors.PRIMARY_CONTENT);
            if (answer.getText() != null) {
                btn.setText(answer.getText());
            }
            if (answer.getImage() != null) {
                btn.setIcon(answer.getImage());
            }
            final int index = i;
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Logge, welcher Button geklickt wurde und welche Frage aktuell ist.
                    System.out.println("Button geklickt: Index " + index);
                    if (quizGame.getCurrentQuestion() != null) {
                        System.out.println("Aktuelle Frage: " + quizGame.getCurrentQuestion().getQuestionText());
                    }
                    handleAnswerSelection(index);
                }
            });
            answerButtons.add(btn);
            gbcAns.gridx = i % 2;
            gbcAns.gridy = i / 2;
            answersPanel.add(btn, gbcAns);
        }
        revalidate();
        repaint();
    }

    /**
     * Behandelt die Auswahl einer Antwort: Deaktiviert Buttons, gibt visuelles Feedback
     * und lädt nach einer Verzögerung die nächste Frage.
     *
     * @param selectedIndex Index der ausgewählten Antwort.
     */
    private void handleAnswerSelection(int selectedIndex) {
        if (selectedIndex >= currentAnswerOptions.size()) {
            return;
        }
        // Logge die ausgewählte Antwort
        AnswerOption selectedAnswer = currentAnswerOptions.get(selectedIndex);
        System.out.println("Antwort ausgewählt: Index " + selectedIndex +
                ", Text: " + (selectedAnswer.getText() != null ? selectedAnswer.getText() : "kein Text"));

        // Deaktiviere alle Antwortbuttons
        for (JButton btn : answerButtons) {
            btn.setEnabled(false);
        }
        int correctIndex = -1;
        for (int i = 0; i < currentAnswerOptions.size(); i++) {
            if (currentAnswerOptions.get(i).isCorrect()) {
                correctIndex = i;
                break;
            }
        }
        // Farbfeedback: korrekt = grün, falsch = rot
        if (selectedAnswer.isCorrect()) {
            answerButtons.get(selectedIndex).setBackground(AppColors.SUCCESS);
            answerButtons.get(selectedIndex).setForeground(AppColors.SUCCESS_CONTENT);
            for (int i = 0; i < answerButtons.size(); i++) {
                if (i != selectedIndex) {
                    answerButtons.get(i).setBackground(AppColors.ERROR);
                    answerButtons.get(i).setForeground(AppColors.ERROR_CONTENT);
                }
            }
            correctCount++;
            scoreLabel.setText("Score: " + correctCount);
        } else {
            answerButtons.get(selectedIndex).setBackground(AppColors.ERROR);
            answerButtons.get(selectedIndex).setForeground(AppColors.ERROR_CONTENT);
            if (correctIndex != -1) {
                answerButtons.get(correctIndex).setBackground(AppColors.SUCCESS);
                answerButtons.get(correctIndex).setForeground(AppColors.SUCCESS_CONTENT);
            }
            for (int i = 0; i < answerButtons.size(); i++) {
                if (i != selectedIndex && i != correctIndex) {
                    answerButtons.get(i).setBackground(AppColors.ERROR);
                    answerButtons.get(i).setForeground(AppColors.ERROR_CONTENT);
                }
            }
        }
        // Verzögerung (1,5 Sekunden) zur Anzeige der nächsten Frage
        Timer timer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quizGame.nextQuestion();
                displayCurrentQuestion();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Ermöglicht externen Komponenten (z. B. Tastatureingaben) die Auswahl einer Antwort.
     *
     * @param index Index der auszuwählenden Antwort aus der zufälligen Anordnung.
     */
    public void selectAnswer(int index) {
        handleAnswerSelection(index);
    }
}
