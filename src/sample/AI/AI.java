package sample.AI;

import sample.Board;

public interface AI {


    void add(Board Board);

    int getX();

    int getValue();

    int possibleConnections(int i, int j);

    int lineOfFour(int x, int y, int i, int j);

}
