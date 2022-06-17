package View;

import Model.Model;
import Model.GameFig;
import Model.Records;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

public class GameField extends JPanel implements Observer {
    private final int boardWidth = 15;
    private final int boardHeight = 25;
    private Model model;
    private GameFrame frame;

    private int squareWidth() {
        return (int) getSize().getWidth() / boardWidth;
    }
    private int squareHeight() {
        return (int) getSize().getHeight() / boardHeight;
    }

    public GameField (GameFrame frame) {
        setFocusable(true);
        model = new Model(boardWidth, boardHeight, this);
        addKeyListener(new Adapter());
        this.frame = frame;
    }

    public void start() throws IOException, ClassNotFoundException {
        model.start();
    }

    public void drawSquare(Graphics g, int x, int y, GameFig.FigTypes fig) {
        Color colors[] = {new Color(222, 87, 87), new Color(3, 252, 161),
                new Color(250, 209, 120), new Color(31, 207, 207),
                new Color(207, 31, 113), new Color(136, 3, 252)};
        g.setColor(colors[fig.ordinal()-1]);
        g.fillRect(x, y, squareWidth(), squareHeight());
        g.setColor(Color.BLACK);
        g.drawRect(x, y, squareWidth(), squareHeight());
    }

    public void paint(Graphics g, double width, double height) {
        int squareWidth = (int)width/boardWidth;
        int squareHeight = (int)height/boardHeight;
        for (int i = 0; i < boardHeight; ++i) {
            for (int j = 0; j < boardWidth; ++j) {
                GameFig.FigTypes shape = model.figHere(j, boardHeight - i -1);
                if (shape != GameFig.FigTypes.NoShape)
                    drawSquare(g, j * squareWidth, i * squareHeight, shape);
            }
        }
        GameFig currentFig = model.getCurFig();

        if (currentFig.getPieceShape() != GameFig.FigTypes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = model.curX + currentFig.x(i);
                int y = model.curY - currentFig.y(i);
                drawSquare(g, x * squareWidth, (boardHeight - y - 1) * squareHeight, currentFig.getPieceShape());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        paint(g, getSize().getWidth(), getSize().getHeight());
    }

    public void GameOver() throws IOException, ClassNotFoundException {
        JOptionPane.showMessageDialog(null, "Your score is: " + model.score);
        String name = JOptionPane.showInputDialog("Enter your name");
        Records records = new Records(name, model.score);
        FileOutputStream outputStream = new FileOutputStream("table.ser", true);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(records);
        objectOutputStream.close();
        records.restoreData();
    }

    private class Adapter extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(model.paused) {
                    return;
                }

                if(key == 'e' || key == 'E') {
                    model.pause();
                    JOptionPane.showMessageDialog(null, "Your score is: " + model.score);
                    frame.exit();
                }
                if(key == 'p' || key == 'P') {
                    model.pause();
                    int ok = JOptionPane.showConfirmDialog(null, "Game is paused\nDo you want to continue?");
                    if ((ok == JOptionPane.YES_OPTION) || (ok == JOptionPane.CANCEL_OPTION)) {
                        model.unpause();
                    } else if (ok == JOptionPane.NO_OPTION) {
                        frame.exit();
                    }
                }
                if(key == KeyEvent.VK_RIGHT) {
                    model.moveRight();
                }
                if(key == KeyEvent.VK_LEFT) {
                    model.moveLeft();
                }
                if(key == KeyEvent.VK_DOWN) {
                    try {
                        model.moveDown();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                if(key == KeyEvent.VK_UP) {
                    model.turn();
                }

            }
    }
}
