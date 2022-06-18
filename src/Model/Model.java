package Model;

import View.GameField;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Model extends Observable {
    public boolean paused = false;
    private boolean falled = false;
    public boolean started = false;
    public int score = 0;
    private Timer timer;
    private int boardHeight;
    private int boardWidth;
    private GameField field;
    public int curX = 0;
    public int curY = 0;

    public GameFig currentFig;
    private GameFig.FigTypes[] board;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                game();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    };


    public Model(int width, int height, GameField field) {
        this.boardHeight = height;
        this.boardWidth = width;
        this.field = field;
        currentFig = new GameFig();
        timer = new Timer();
        addObserver(field);
        board = new GameFig.FigTypes[boardWidth*boardHeight];

        clear();
    }

    public void start() throws IOException, ClassNotFoundException {
        if(paused) return;
        started = true;
        falled = false;
        score = 0;
        clear();
        newFig();
        timer.schedule(task, new Date(), 1000);
    }

    public void pause() {
        if(!started) return;
        timer.cancel();
        paused = true;
    }
    public void unpause() {
        timer = new Timer();
        timer.schedule(task, new Date(), 1000);
        paused = false;
        setChanged();
        notifyObservers();
    }

    public GameFig.FigTypes figHere(int x, int y) {
        return board[(y*boardWidth) + x];
    }

    private boolean Move(GameFig newFig, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newFig.x(i);
            int y = newY - newFig.y(i);
            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
                return false;
            if (figHere(x, y) != GameFig.FigTypes.NoShape)
                return false;
        }
        currentFig = newFig;
        curX = newX;
        curY = newY;
        setChanged();
        notifyObservers();
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
            currentFig.setPieceShape(GameFig.FigTypes.NoShape);
            timer.cancel();
            started = false;
            field.GameOver();
        }
    }

    private void clear() {
        for (int i = 0; i < boardHeight*boardWidth; ++i) {
            board[i] = GameFig.FigTypes.NoShape;
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
                if (figHere(j, i) == GameFig.FigTypes.NoShape) {
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
            currentFig.setPieceShape(GameFig.FigTypes.NoShape);
            setChanged();
            notifyObservers();
        }
    }

    public GameFig getCurFig () {
        return currentFig;
    }

}