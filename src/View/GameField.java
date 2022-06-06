package View;

import Controller.Controller;
import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameField extends JPanel implements ActionListener {
    private final int boardWidth = 15;
    private final int boardHeight = 25;
    private Controller controller;
    private GameFrame frame;

    private int squareWidth() {
        return (int) getSize().getWidth() / boardWidth;
    }
    private int squareHeight() {
        return (int) getSize().getHeight() / boardHeight;
    }

    public GameField (GameFrame frame) {
        setFocusable(true);
        controller = new Controller(boardWidth, boardHeight, this);
        addKeyListener(new Adapter());
        this.frame = frame;
    }

    public void start() throws IOException, ClassNotFoundException {
        controller.start();
    }

    public void drawSquare(Graphics g, int x, int y, Model.FigTypes fig) {
        Color colors[] = {new Color(222, 87, 87), new Color(3, 252, 161),
                new Color(250, 209, 120), new Color(31, 207, 207),
                new Color(207, 31, 113), new Color(136, 3, 252)};
        g.setColor(colors[fig.ordinal()-1]);
        g.fillRect(x, y, squareWidth(), squareHeight());
        g.setColor(Color.BLACK);
        g.drawRect(x, y, squareWidth(), squareHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.game();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        controller.paint(g, getSize().getWidth(), getSize().getHeight());
    }

    private class Adapter extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(controller.paused) {
                    return;
                }

                if(key == 'e' || key == 'E') {
                    controller.pause();
                    JOptionPane.showMessageDialog(null, "Your score is: " + controller.score);
                    frame.exit();
                }
                if(key == 'p' || key == 'P') {
                    controller.pause();
                    int ok = JOptionPane.showConfirmDialog(null, "Game is paused\nDo you want to continue?");
                    if ((ok == JOptionPane.YES_OPTION) || (ok == JOptionPane.CANCEL_OPTION)) {
                        controller.unpause();
                    } else if (ok == JOptionPane.NO_OPTION) {
                        frame.exit();
                    }
                }
                if(key == KeyEvent.VK_RIGHT) {
                    controller.moveRight();
                }
                if(key == KeyEvent.VK_LEFT) {
                    controller.moveLeft();
                }
                if(key == KeyEvent.VK_DOWN) {
                    try {
                        controller.moveDown();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                if(key == KeyEvent.VK_UP) {
                    controller.turn();
                }

            }
    }
}
