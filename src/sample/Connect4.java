package sample;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.AI.AI;
import sample.AI.AlphaBetaAlgorithm;
import sample.AI.MinMaxAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Connect4 {

    private static final int CIRCLE_SIZE = 90;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    int currentPlayer = 2;

    private boolean redMove = false;
    private boolean AI1;
    private boolean AI2;
    private boolean minMax;
    private int depth;

    private ArrayList<Double> AI1AvgTime = new ArrayList<>();
    private ArrayList<Double> AI2AvgTime = new ArrayList<>();
    private int AI1moves = 0;
    private int AI2moves = 0;
    private int winnerMoves = 0;

    boolean winnerExists = false;
    boolean draw = false;
    boolean simple = false;

    private Board[][] grid = new Board[COLUMNS][ROWS];
    private Pane discRoot = new Pane();


    public Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(discRoot);
        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());
        return root;
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1) * CIRCLE_SIZE, (ROWS + 1) * CIRCLE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(CIRCLE_SIZE / 2);
                circle.setCenterX(CIRCLE_SIZE / 2);
                circle.setCenterY(CIRCLE_SIZE / 2);
                circle.setTranslateX(x * (CIRCLE_SIZE + 5) + CIRCLE_SIZE / 4);
                circle.setTranslateY(y * (CIRCLE_SIZE + 5) + CIRCLE_SIZE / 4);
                shape = Shape.subtract(shape, circle);
            }
        }

        shape.setFill(Color.rgb(52, 171, 235));
        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(CIRCLE_SIZE, (ROWS + 1) * CIRCLE_SIZE);
            rect.setTranslateX(x * (CIRCLE_SIZE + 5) + CIRCLE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(51, 19, 168, 0.1)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(event -> {
                if (AI1 && currentPlayer == 2) {
                } else {
                    placeDisc(new Board(), column);
                }
            });
            list.add(rect);
        }
        return list;
    }

    public void play(boolean AI1, boolean AI2, boolean minMax, int depth, boolean simple) throws InterruptedException {

        this.AI1 = AI1;
        this.AI2 = AI2;
        this.minMax = minMax;
        this.depth = depth;
        this.simple = simple;

        currentPlayer = (int) (Math.random() * 2) + 1;
        if (currentPlayer == 1) {
            redMove = false;
        } else {
            redMove = true;
        }

        for (int j = 0; j < 6; j++)
            for (int i = 0; i < 7; i++) {
                Board tempB = new Board();
                tempB.setI(i);
                tempB.setJ(j);
                grid[i][j] = tempB;
            }

        if (currentPlayer == 2 && AI1) {
            AI1moves++;
            Board board = minmax();
            Board b = new Board();
            b.setI(board.getI());
            b.setJ(board.getJ());
            b.setColor(board.getColor());
            placeDisc(b, b.getI());
        }
        if (currentPlayer == 1 && AI2) {
            AI2moves++;
            Board board = minmax();
            Board b = new Board();
            b.setI(board.getI());
            b.setJ(board.getJ());
            b.setColor(board.getColor());
            placeDisc(b, b.getI());
        }

    }

    void addDisc(Board Board, int column){
        //placeDisc(Board, column);
        if (thereIsAWinner()) {
            Stage stage1 = new Stage();
            GridPane pane = new GridPane();
            javafx.scene.control.Label win = new Label("Winner: " + (redMove ? "RED" : "YELLOW"));
            pane.add(win, 1, 1);
            pane.setAlignment(Pos.CENTER);
            pane.setPadding(new Insets(16));
            pane.setHgap(16);
            pane.setVgap(8);
            Scene sc = new Scene(pane, 500, 200);
            sc.getStylesheets().add(Connect4.class.getResource("win.css").toString());
            stage1.setScene(sc);

            stage1.show();
            winnerExists = true;
            showAvgTime();
            if(redMove){
                winnerMoves = AI1moves;
            }else{
                winnerMoves = AI2moves;
            }
            System.out.println("\nLiczba ruchów gracza wygrywającego: " + winnerMoves);
        } else {
            currentPlayer = currentPlayer % 2 + 1;
            redMove = !redMove;

            if (currentPlayer == 2 && AI1) {
                AI1moves++;
                Board board = minmax();
                if(draw){
                    showDraw();
                }else{
                    Board b = new Board();
                    b.setI(board.getI());
                    b.setJ(board.getJ());
                    b.setColor(board.getColor());
                    placeDisc(b, b.getI());
                }
            }
            if (currentPlayer == 1 && AI2) {
                AI2moves++;
                Board board = minmax();
                if(draw){
                    showDraw();
                }else{
                    Board b = new Board();
                    b.setI(board.getI());
                    b.setJ(board.getJ());
                    b.setColor(board.getColor());
                    placeDisc(b, b.getI());
                }
            }
        }
    }

    private void showDraw() {
        Stage stage1 = new Stage();
        GridPane pane = new GridPane();
        javafx.scene.control.Label win = new Label("It's a draw!");
        pane.add(win, 1, 1);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(16));
        pane.setHgap(16);
        pane.setVgap(8);
        Scene sc = new Scene(pane, 500, 200);
        sc.getStylesheets().add(Connect4.class.getResource("win.css").toString());
        stage1.setScene(sc);

        stage1.show();
        winnerExists = true;
        showAvgTime();
        if(redMove){
            winnerMoves = AI1moves;
        }else{
            winnerMoves = AI2moves;
        }
        System.out.println("\nLiczba ruchów gracza wygrywającego: " + winnerMoves);

    }

    public boolean thereIsAWinner() {
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                if (!grid[i][j].getColor().equals("None") && connectsToFour(i, j))
                    return true;
            }
        }
        return false;
    }

    public boolean connectsToFour(int i, int j) {
        if (lineOfFour(i, j, -1, -1))
            return true;
        if (lineOfFour(i, j, -1, 0))
            return true;
        if (lineOfFour(i, j, -1, 1))
            return true;
        if (lineOfFour(i, j, 0, -1))
            return true;
        if (lineOfFour(i, j, 0, 1))
            return true;
        if (lineOfFour(i, j, 1, -1))
            return true;
        if (lineOfFour(i, j, 1, 0))
            return true;
        if (lineOfFour(i, j, 1, 1))
            return true;
        return false;
    }

    public boolean lineOfFour(int x, int y, int i, int j) {


        String color = grid[x][y].getColor();

        for (int k = 1; k < 4; k++) {
            if (x + i * k < 0 || y + j * k < 0 || x + i * k >= grid.length || y + j * k >= grid[0].length)
                return false;
            if (!grid[x + i * k][y + j * k].getColor().equals(color))
                return false;
        }
        return true;
    }

    public Board minmax() {
        double startTime = System.currentTimeMillis();
        AI tree;
        if(minMax){
            tree = new MinMaxAlgorithm(grid, 0, depth, simple);

        }else{
            tree = new AlphaBetaAlgorithm(grid, 0, Integer.MIN_VALUE,Integer.MAX_VALUE, depth, simple);
        }

        double endTime = System.currentTimeMillis();
        if (currentPlayer == 2 && AI1) {
            AI1AvgTime.add(endTime-startTime);
        }
        if (currentPlayer == 1 && AI2) {
            AI2AvgTime.add(endTime-startTime);
        }

        if(tree.getX() == 1000){
            draw = true;
            return grid[0][0];
        }
        return grid[tree.getX()][0];

    }

    private void placeDisc(Board board, int column)  {
        int row = ROWS - 1;

        do {
            if (grid[column][row].getColor().equals("None"))
                break;

            row--;
        } while (row >= 0);

        if (row < 0){
            showDraw();
            return;
        }

        board.setI(column);
        board.setJ(row);


        grid[column][row] = board;


        Circle c = new Circle(CIRCLE_SIZE / 2);
        c.setCenterX(CIRCLE_SIZE / 2);
        c.setCenterY(CIRCLE_SIZE / 2);

        switch (currentPlayer) {
            case 1:
                c.setFill(Color.YELLOW);
                grid[column][row].setColor("Yellow");
                break;
            case 2:
                Board b = new Board();
                b.setI(column);
                b.setJ(row);
                grid[column][row] = b;
                c.setFill(Color.RED);
                grid[column][row].setColor("Red");
                break;
        }

        discRoot.getChildren().add(c);

        c.setTranslateX(column * (CIRCLE_SIZE + 5) + CIRCLE_SIZE / 4);

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.1), c);
        animation.setToY(row * (CIRCLE_SIZE + 5) + CIRCLE_SIZE / 4);
        animation.setOnFinished(e -> {
            addDisc(board, column);
        });
        animation.play();
    }

    public void showAvgTime(){
        Double sum1 = 0.0;
        Double sum2 = 0.0;

        if(AI1 && !AI2) {
            for (Double mark : AI1AvgTime) {
                sum1 += mark;
            }
            System.out.println("Srednia czas: " + sum1 / AI1AvgTime.size());
        }


        if(AI2 && !AI1) {
            for (Double mark : AI2AvgTime) {
                sum2 += mark;
            }
            System.out.printf("Srednia czas: %.2f", sum2 / AI2AvgTime.size());
        }

        if(AI2 && AI1) {
            for (Double mark : AI1AvgTime) {
                sum1 += mark;
            }
            for (Double mark : AI2AvgTime) {
                sum2 += mark;
            }
            System.out.printf("Srednia czas AI1: %.2f\n", sum1 / AI1AvgTime.size());
            System.out.printf("Srednia czas AI2: %.2f", sum2 / AI2AvgTime.size());

        }
    }
}