package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Menu {
    public JFrame frame;

    public Menu() {
        frame = new JFrame("Tetris game");
        frame.setPreferredSize(new Dimension(450, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel();

        content.setBackground(new Color(54, 136, 191));
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton play = new JButton("PLAY");
        play.setPreferredSize(new Dimension(100,50));
        play.setBackground(Color.RED);

        JButton info = new JButton("INFO");
        info.setPreferredSize(new Dimension(100,50));
        info.setBackground(Color.CYAN);

        JButton records = new JButton("RECORDS");
        records.setPreferredSize(new Dimension(100,50));
        records.setBackground(Color.CYAN);

        JButton exit = new JButton("EXIT");
        exit.setPreferredSize(new Dimension(100,50));
        exit.setBackground(Color.CYAN);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                System.exit(0);
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame infoFrame = new JFrame("INFO");
                infoFrame.setPreferredSize(new Dimension(400, 500));
                infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel infoLabel = new JLabel("<html>Hello, It's Tetris game!<br>You can use 'p' to pause a game<br>Use arrows to move right/left<br>Use down arrow to move down faster<br>Use 'e' to exit</html>", JLabel.CENTER);
                infoLabel.setFont(infoLabel.getFont ().deriveFont (20.0f));

                JPanel content2 = new JPanel(new BorderLayout());

                JButton exit = new JButton("BACK");
                exit.setPreferredSize(new Dimension(70,30));
                exit.setBackground(Color.WHITE);

                content2.setBackground(new Color(54, 136, 191));
                content2.add(infoLabel, BorderLayout.CENTER);
                content2.add(exit, BorderLayout.PAGE_END);

                frame.setVisible(false);

                infoFrame.setContentPane(content2);
                infoFrame.setSize(400, 500);
                infoFrame.setLocationRelativeTo(null);
                infoFrame.pack();
                infoFrame.setVisible(true);

                exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        infoFrame.setVisible(false);
                        frame.setVisible(true);
                        System.exit(0);
                    }
                });
            }
        });

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                GameFrame gameFrame = new GameFrame(frame);
                try {
                    gameFrame.init();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

        records.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsTable recordsTable = new RecordsTable();
                recordsTable.setVisible(true);
            }
        });

        content.add(play);
        content.add(info);
        content.add(records);
        content.add(exit);

        frame.setContentPane(content);

        frame.setSize(450, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }
}
