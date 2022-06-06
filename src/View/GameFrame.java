package View;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame extends JFrame {
    private GameField field;

    public GameFrame(JFrame frame) {
        field = new GameField(this);
    }

    public void init() throws IOException, ClassNotFoundException {
        setTitle("TETRIS GAME");
        setPreferredSize(new Dimension(290, 460));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(field, BorderLayout.CENTER);
        field.start();

        setBackground(new Color(54, 136, 191));

        setSize(290, 460);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }

    public void exit() {
        setVisible(false);
        System.exit(0);
    }

}
