package Controller;

import Model.Model;
import View.GameField;
import Model.Records;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Controller {
    public boolean paused = false;
    private boolean falled = false;
    public boolean started = false;
    public int score = 0;
    private Timer timer;
    private int boardHeight;
    private int boardWidth;
    private GameField field;
    private int curX = 0;
    private int curY = 0;

    private Model currentFig;
    private Model.FigTypes[] board;

    private Records records;

    public Controller(int width, int height, GameField field) {
        this.boardHeight = height;
        this.boardWidth = width;
        this.field = field;
        currentFig = new Model();
        timer = new Timer(1000, field);
        timer.start();
        board = new Model.FigTypes[boardWidth*boardHeight];

        clear();
    }

    public void start() throws IOException, ClassNotFoundException {
        if(paused) return;
        started = true;
        falled = false;
        score = 0;
        clear();
        newFig();
        timer.start();
    }

    public void pause() {
        if(!started) return;
        timer.stop();
        paused = true;
    }
    public void unpause() {
        timer.start();
        paused = false;
        field.repaint();
    }

    private Model.FigTypes figHere(int x, int y) {
        return board[(y*boardWidth) + x];
    }

    private boolean Move(Model newFig, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newFig.x(i);
            int y = newY - newFig.y(i);
            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
                return false;
            if (figHere(x, y) != Model.FigTypes.NoShape)
                return false;
        }
        currentFig = newFig;
        curX = newX;
        curY = newY;
        field.repaint();
        return true;
    }

    public void moveRight() {
        Move(currentFig, curX+1, curY);
    }

    public void moveLeft() {
        Move(currentFig, curX-1, curY);
    }

    public void moveDown() throws IOException, ClassNotFoundException {
        if(!Move(currentFig, curX, curY-1)) {
            figDown();
        }
    }

    public void turn() {
        Move(currentFig.turn(), curX, curY);
    }

    public void game() throws IOException, ClassNotFoundException {
        if(falled) {
            falled = false;
            newFig();
        } else {
            moveDown();
        }
    }

    private void newFig() throws IOException, ClassNotFoundException {
        currentFig.setRandomShape();
        curX = boardWidth/2 + 1;
        curY = boardHeight - 1 + currentFig.minY();
        if (!Move(currentFig, curX, curY)) {
            currentFig.setPieceShape(Model.FigTypes.NoShape);
            timer.stop();
            started = false;
            JOptionPane.showMessageDialog(null, "Your score is: " + score);
            String name = JOptionPane.showInputDialog("Enter your name");
            records = new Records(name, score);
            FileOutputStream outputStream = new FileOutputStream("table.ser", true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(records);
            objectOutputStream.close();
            records.restoreData();
        }
    }

    private void clear() {
        for (int i = 0; i < boardHeight*boardWidth; ++i) {
            board[i] = Model.FigTypes.NoShape;
        }
    }

    public void paint(Graphics g, double width, double height) {
        int squareWidth = (int)width/boardWidth;
        int squareHeight = (int)height/boardHeight;
        for (int i = 0; i < boardHeight; ++i) {
            for (int j = 0; j < boardWidth; ++j) {
                Model.FigTypes shape = figHere(j, boardHeight - i -1);
                if (shape != Model.FigTypes.NoShape)
                    field.drawSquare(g, j * squareWidth, i * squareHeight, shape);
            }
        }

        if (currentFig.getPieceShape() != Model.FigTypes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + currentFig.x(i);
                int y = curY - currentFig.y(i);
                field.drawSquare(g, x * squareWidth, (boardHeight - y - 1) * squareHeight, currentFig.getPieceShape());
            }
        }
    }

    private void figDown() throws IOException, ClassNotFoundException {
        for (int i = 0; i < 4; ++i) {
            int x = curX + currentFig.x(i);
            int y = curY - currentFig.y(i);
            board[(y * boardWidth) + x] = currentFig.getPieceShape();
        }

        updateField();

        if (!falled) {
            newFig();
        }
    }

    private void updateField() {
        int fullLines = 0;

        for (int i = boardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < boardWidth; ++j) {
                if (figHere(j, i) == Model.FigTypes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++fullLines;
                for (int k = i; k < boardHeight - 1; ++k) {
                    for (int j = 0; j < boardWidth; ++j)
                        board[(k * boardWidth) + j] = figHere(j, k + 1);
                }
            }
        }

        if (fullLines > 0) {
            score += fullLines;
            falled = true;
            currentFig.setPieceShape(Model.FigTypes.NoShape);
            field.repaint();
        }
    }

}