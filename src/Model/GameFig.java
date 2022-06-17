package Model;

public class GameFig {

    public enum FigTypes {
        NoShape, Stick, Square, TShape, LShape, Zigzag, MirroredLShape
    }

    private int[][] coords;
    private int [][][] coordsTable;
    private FigTypes pieceShape;
    public int figType;

    public GameFig() {
        coords = new int[4][2];
        coordsTable = new int[][][]
        {
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{-1, 1}, {-1, 0}, {0, 0}, {0, -1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };
        setPieceShape(FigTypes.NoShape);
    }

    public void setPieceShape(FigTypes pieceShape) {
        for (int i = 0; i < 4 ; i++) {
            System.arraycopy(coordsTable[pieceShape.ordinal()][i], 0, coords[i], 0, 2);
        }
        this.pieceShape = pieceShape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public GameFig turn() {
        if (figType == 2)
            return this;

        GameFig result = new GameFig();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public void setRandomShape() {
        figType = 1 + (int)(Math.random() * 6);
        FigTypes[] values = FigTypes.values();
        setPieceShape(values[figType]);
    }

    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public FigTypes getPieceShape() {
        return pieceShape;
    }
}


