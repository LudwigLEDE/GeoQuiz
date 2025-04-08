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
 * wird die nächste Frage geladen. Zusätzlich wird am unteren Rand ein Manual-Label
 * angezeigt, welches über die Tastatursteuerung informiert.
 * <br>
 * Neu: Mit den Pfeiltasten oder den Tasten W, A, S, D kann durch die Antwortoptionen navigiert
 * werden; der aktuell ausgewählte Button wird hervorgehoben und mit ENTER kann die Auswahl bestätigt werden.
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

    // Index der aktuell markierten Antwort (für die Tastatur-Navigation)
    private int currentSelectionIndex = -1;

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
        // Richte Key Bindings für die Navigation in den Antwortoptionen ein
        setupAnswerKeyBindings();
        // Stelle sicher, dass dieses Panel den Tastaturfokus erhält
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
        answersPanel.setBackground(AppColors.BACKGROUND);
        gbc.gridy = 3;
        add(answersPanel, gbc);

        // Permanentes Manual-Label am unteren Rand (Zeile 4)
        manualLabel = new JLabel("<html>Verfügbare Tasten:<br>" +
                "- Pfeiltasten: Navigation in den Antwortoptionen<br>" +
                "- ENTER: Auswahl bestätigen</html>", SwingConstants.CENTER);
        manualLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        manualLabel.setForeground(AppColors.COPY);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipadx = 2;
        add(manualLabel, gbc);
    }

    /**
     * Richtet Key Bindings ein, um in den Antwortoptionen zu navigieren und
     * die Auswahl mit ENTER zu bestätigen.
     */
    private void setupAnswerKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Linke Richtung / A
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke('A'), "moveLeft");
        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtons.size() == 0) return;
                int col = currentSelectionIndex % 2;
                if (col > 0) { // in der gleichen Zeile nach links
                    currentSelectionIndex -= 1;
                    updateButtonSelection();
                }
            }
        });

        // Rechte Richtung / D
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke('D'), "moveRight");
        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtons.size() == 0) return;
                int col = currentSelectionIndex % 2;
                if (col == 0 && currentSelectionIndex + 1 < answerButtons.size()) {
                    currentSelectionIndex += 1;
                    updateButtonSelection();
                }
            }
        });

        // Oben / W
        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke('W'), "moveUp");
        am.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtons.size() == 0) return;
                if (currentSelectionIndex - 2 >= 0) {
                    currentSelectionIndex -= 2;
                    updateButtonSelection();
                }
            }
        });

        // Unten / S
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke('S'), "moveDown");
        am.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtons.size() == 0) return;
                if (currentSelectionIndex + 2 < answerButtons.size()) {
                    currentSelectionIndex += 2;
                    updateButtonSelection();
                }
            }
        });

        // ENTER: Auswahl bestätigen
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirmSelection");
        am.put("confirmSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtons.size() > 0 && currentSelectionIndex >= 0) {
                    System.out.println("ENTER gedrückt. Auswahl bestätigen: Index " + currentSelectionIndex);
                    selectAnswer(currentSelectionIndex);
                }
            }
        });
    }

    /**
     * Aktualisiert die visuelle Markierung der Antwortbuttons, sodass der aktuell
     * ausgewählte Button hervorgehoben wird.
     */
    private void updateButtonSelection() {
        for (int i = 0; i < answerButtons.size(); i++) {
            if (i == currentSelectionIndex) {
                // Hebe den ausgewählten Button hervor (z. B. gelber Rahmen)
                answerButtons.get(i).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            } else {
                // Standardrahmen
                answerButtons.get(i).setBorder(BorderFactory.createLineBorder(AppColors.BORDER));
            }
        }
    }

    /**
     * Zeigt die aktuelle Frage an, skaliert und setzt das Bild (unter Beibehaltung des Seitenverhältnisses),
     * und mischt die Antwortoptionen.
     */
    public void displayCurrentQuestion() {
        Question question = quizGame.getCurrentQuestion();
        if (question == null) {
            // Keine weiteren Fragen: Zeige End-Punktzahl und einen Button zur Rückkehr ins Hauptmenü
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

        // Skalieren des Bildes unter Beibehaltung des Seitenverhältnisses:
        if (question.getQuestionImage() != null) {
            ImageIcon originalIcon = question.getQuestionImage();
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int maxWidth = 500;
            int maxHeight = 300;
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double scalingFactor = Math.min(widthRatio, heightRatio);
            int newWidth = (int) (originalWidth * scalingFactor);
            int newHeight = (int) (originalHeight * scalingFactor);
            Image scaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            questionImageLabel.setIcon(new ImageIcon(scaledImage));
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

        // Setze den aktuellen Auswahlindex auf 0 (erste Antwort), sofern vorhanden, und aktualisiere die Markierung
        if (answerButtons.size() > 0) {
            currentSelectionIndex = 0;
            updateButtonSelection();
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
        // Nach einer Verzögerung zur nächsten Frage wechseln
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
     * Ermöglicht externen Komponenten (z. B. über Key Bindings) die Auswahl einer Antwort.
     *
     * @param index Index der auszuwählenden Antwort aus der zufälligen Anordnung.
     */
    public void selectAnswer(int index) {
        handleAnswerSelection(index);
    }
}
