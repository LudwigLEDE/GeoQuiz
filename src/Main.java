import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {


        JFrame jFrame = new JFrame();
        jFrame.setSize(200, 200);

        JButton jButton = new JButton("huhu");

        jFrame.setLayout(null);

        jButton.setBounds(50, 50, 20, 20);

        jFrame.add(jButton);

        jFrame.setVisible(true);


//        System.setProperty("net.java.games.input.useDefaultPlugin", "false");




        AutomatenController.getInstance().addListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    Rectangle bounds = jButton.getBounds();
                    jButton.setBounds(bounds.x + 10, bounds.y, bounds.width, bounds.height);
                }
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    Rectangle bounds = jButton.getBounds();
                    jButton.setBounds(bounds.x - 10, bounds.y, bounds.width, bounds.height);
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    Rectangle bounds = jButton.getBounds();
                    jButton.setBounds(bounds.x , bounds.y+10, bounds.width, bounds.height);
                }

            }
        });
    }
}