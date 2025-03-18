import net.java.games.input.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Wird benutzt zur Anbindung an die Kontroller Einheit des Spieleautomaten.
 * <br><br>
 *
 * Wie man den AutomatenController ("AC") benutzt:
 * <br>
 * 1. Stellt sicher, dass die externen Libraries "jinput" und "jinput_natives_all" in eurem Projekt sind (z.B. in einem Ordner "lib") <br>
 * 2. Ebenso müssen die DLL-Dateien jinput-dx8_64 und jinput-raw_64 in eurem Projektverzeichnis liegen.<br>
 * WICHTIG: per default sollten diese DLLs in einem Ordner "/lib" liegen. Falls das bei euch woanders ist, passt den entsprechenden Pfad {@link #DLL_PATH} an.
 * 2. Unter "File -> Project Structure -> Modules -> Dependencies" fügt ihr die Libraries dem Projekt als Resouce zu.<br>
 * 3. Kopiert diese Klasse in das /src Verzeichnis Eures Projekts<br>
 * 4. Überall in eurem Projekt, wo ihr KeyListener erstellt, holt ihr euch über die {@link AutomatenController#getInstance()} die Instanz des AC und registriert den Listener über {@link AutomatenController#addListener(KeyListener)}
 * <br><br>
 * Hier ein Überblick über die KeyCodes, die der AC an alle Listener weiter leitet:<br><br> *
 *
 * {@link KeyEvent#VK_UP}: Wenn der Joystick nach oben gedrückt wird.<br>
 * {@link KeyEvent#VK_DOWN}: Wenn der Joystick nach unten gedrückt wird.<br>
 * {@link KeyEvent#VK_LEFT}: Wenn der Joystick nach links gedrückt wird.<br>
 * {@link KeyEvent#VK_RIGHT}: Wenn der Joystick nach rechts gedrückt wird.<br><br>
 *
 * {@link KeyEvent#VK_0}: Wenn Knopf Nr. 0 gedrückt wird.<br>
 * {@link KeyEvent#VK_1}: Wenn Knopf Nr. 1 gedrückt wird.<br>
 * {@link KeyEvent#VK_2}: Wenn Knopf Nr. 2 gedrückt wird.<br>
 * {@link KeyEvent#VK_3}: Wenn Knopf Nr. 3 gedrückt wird.<br>
 * {@link KeyEvent#VK_4}: Wenn Knopf Nr. 4 gedrückt wird.<br>
 * {@link KeyEvent#VK_5}: Wenn Knopf Nr. 5 gedrückt wird.<br>
 * <br><br>
 * Die Events für den Joystick werden über {@link KeyListener#keyPressed(KeyEvent)} übermittelt. Sobald der Stick wieder in die neutral-Stellung geht, werden über
 * {@link KeyListener#keyReleased(KeyEvent)} zwei Events für die jeweiligen Richtungen (links/rechts oder oben/unten) weitergegeben.
 */
public class AutomatenController {

    private Collection<KeyListener> listeners = new HashSet<>();
    private static AutomatenController INSTANCE;

    private static final String DLL_PATH = "/lib";


    /**
     * Bitte diese Methode benutzen, um eine Instanz des AutomatenControllers zu erhalten (eigene Instanzierung ist verboten)
     * @return die einzige erlaubte Instanz des ACs (Singleton Patern)
     */
    public static AutomatenController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutomatenController();
        }
        return INSTANCE;
    }


    /**
     * Privater Konstruktor (nicht dran rumfingern!)
     */
    private AutomatenController() {

        //  dem System beibringen, wo die DLLs sind:
        System.setProperty("net.java.games.input.librarypath", new File(System.getProperty("user.dir") + DLL_PATH).getPath());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {


            Controller[] controllers = ControllerEnvironment
                    .getDefaultEnvironment().getControllers();

            if (controllers.length == 0) {
                System.out.println("Kein Spiele-Kontroller gefunden");
                System.exit(0);
            }

            for (Controller controller : controllers) {

                controller.poll();
                EventQueue queue = controller.getEventQueue();
                Event event = new Event();

                while (queue.getNextEvent(event)) {

                    Component comp = event.getComponent();

                    // der Controller des Spieleautomaten produziert komische Events auf einer nicht vorhandenen Z-Achse: Ignorieren!
                    if (comp.getName().contains("Z-")) {
                        continue;
                    }

                    //  die x-achse wird auf die links/rechts Pfeiltasten gemappt:
                    if (comp.getName().equals("X-Achse")) {
                        if (event.getValue() == -1.0f) {
                            fireEventPressed(KeyEvent.VK_LEFT);
                        }
                         else if (event.getValue() == 1.0f) {
                            fireEventPressed(KeyEvent.VK_RIGHT);
                        }
						else {
                            fireEventReleased(KeyEvent.VK_LEFT);
                            fireEventReleased(KeyEvent.VK_RIGHT);
                        }
                    }

                    //  die y-achse wird auf die oben/unten Pfeiltasten gemappt:
                    if (comp.getName().equals("Y-Achse")) {
                        if (event.getValue() == -1.0f) {
                            fireEventPressed(KeyEvent.VK_UP);
                        } else if (event.getValue() == 1.0f) {
                            fireEventPressed(KeyEvent.VK_DOWN);
                        } else {
                            fireEventReleased(KeyEvent.VK_UP);
                            fireEventReleased(KeyEvent.VK_DOWN);
                        }
                    }


                    // die sechs buttons werden auf die Zahlen 0-5 gemappt:
                    if (comp.getName().equals("Taste 0")) {
                        fireEventPressed(KeyEvent.VK_0);
                    }
                    if (comp.getName().equals("Taste 1")) {
                        fireEventPressed(KeyEvent.VK_1);
                    }
                    if (comp.getName().equals("Taste 2")) {
                        fireEventPressed(KeyEvent.VK_2);
                    }
                    if (comp.getName().equals("Taste 3")) {
                        fireEventPressed(KeyEvent.VK_3);
                    }
                    if (comp.getName().equals("Taste 4")) {
                        fireEventPressed(KeyEvent.VK_4);
                    }
                    if (comp.getName().equals("Taste 5")) {
                        fireEventPressed(KeyEvent.VK_5);
                    }
                }
            }

            /*
             * 20 ms schlafen, damit wir das System nicht crashen:
             */
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }, 0L, 1000L / 60L, TimeUnit.MILLISECONDS);
    }

    /**
     * Benutzt diese Methode, um die Listener eures Projekts am AC zu registrieren
     * @param listener, der bei Eingaben vom SC resgieren soll.
     */
    public void addListener(KeyListener listener) {
        listeners.add(listener);
    }

    private void fireEventPressed(int keyCode) {

        KeyEvent keyEvent = new KeyEvent(new JLabel(), 0, System.currentTimeMillis(), 0, keyCode);
        for (KeyListener listener : listeners) {
            listener.keyPressed(keyEvent);
        }
    }

    private void fireEventReleased(int keyCode) {

        KeyEvent keyEvent = new KeyEvent(new JLabel(), 0, System.currentTimeMillis(), 0, keyCode);
        for (KeyListener listener : listeners) {
            listener.keyReleased(keyEvent);
        }
    }
}
