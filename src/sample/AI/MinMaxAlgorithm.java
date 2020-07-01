package sample.AI;

import sample.Board;

import java.util.ArrayList;

public class MinMaxAlgorithm implements AI {

    public int value;
    Board[][] gird;
    private ArrayList<Integer> bestMoves;
    Board prev = null;
    int depth;
    static int MAX_DEPTH = 7;
    boolean simple = true;


    public MinMaxAlgorithm(Board[][] Boards, int depth, int max, boolean simple)
    {
        this.gird = Boards;
        this.bestMoves = new ArrayList<>();
        this.depth = depth;
        this.value = getValue();
        MAX_DEPTH = max;
        this.simple = simple;

        if(depth < MAX_DEPTH && this.value < 100 && this.value > -100 )
        {
            ArrayList<Integer> possibilities = new ArrayList<Integer>();
            for(int i = 0; i < 7; i++)
                if(Boards[i][0].getColor().equals("None"))
                    possibilities.add(i);

            for(int i = 0; i < possibilities.size(); i++)
            {
                add(Boards[possibilities.get(i)][0]);
                MinMaxAlgorithm child = new MinMaxAlgorithm(Boards, depth+1 ,MAX_DEPTH, this.simple);
                prev.setColor("None");

                if(i == 0)
                {
                    bestMoves.add(possibilities.get(i));
                    value = child.value;
                }
                else if(depth % 2 == 0)
                {
                    if(value < child.value)
                    {
                        bestMoves.clear();
                        bestMoves.add(possibilities.get(i));
                        this.value = child.value;
                    }
                    else if(value == child.value)
                        bestMoves.add(possibilities.get(i));
                }
                else if(depth % 2 == 1)
                {
                    if(value > child.value)
                    {
                        bestMoves.clear();
                        bestMoves.add(possibilities.get(i));
                        this.value = child.value;
                    }
                    else if(value == child.value)
                        bestMoves.add(possibilities.get(i));
                }
            }
        }
        else
        {
            this.value = getValue();
        }
    }

    public void add(Board Board)
    {

        if(!Board.getColor().equals("None"))
            return;

        int i = Board.i;
        int j = Board.j;

        while(j < gird[0].length-1 && gird[i][j + 1].getColor().equals("None"))
            j++;

        if(depth % 2 == 0)
            gird[i][j].setColor("Red");
        else
            gird[i][j].setColor("Yellow");
        prev = gird[i][j];
    }

    public int getX()
    {
        if(bestMoves.size()!= 0){
            int random = (int)(Math.random() * 100) % bestMoves.size();
            return bestMoves.get(random);
        }else{
            return 1000;
        }
    }

    public int getValue()
    {
        int value = 0;
        for(int j = 0; j < 6; j++)
        {
            for(int i = 0; i < 7; i++)
            {
                if(!gird[i][j].getColor().equals("None"))
                {
                    if(gird[i][j].getColor().equals("Red"))
                    {
                        if(simple){
                            value += possibleConnections(i, j) * (MAX_DEPTH - this.depth);
                        }else{
                            value += possibleConnections(i, j) * ((MAX_DEPTH - this.depth)*(MAX_DEPTH - this.depth));
                        }
                    }
                    else
                    {
                        if(simple){
                            value -= possibleConnections(i, j) * (MAX_DEPTH - this.depth);
                        }else{
                            value -= possibleConnections(i, j) * ((MAX_DEPTH - this.depth)*(MAX_DEPTH - this.depth));
                        }
                    }
                }
            }
        }
        return value;
    }

    public int possibleConnections(int i, int j)
    {
        int value = 0;
        value += lineOfFour(i, j, -1, -1);
        value += lineOfFour(i, j, -1, 0);
        value += lineOfFour(i, j, -1, 1);
        value += lineOfFour(i, j, 0, -1);
        value += lineOfFour(i, j, 0, 1);
        value += lineOfFour(i, j, 1, -1);
        value += lineOfFour(i, j, 1, 0);
        value += lineOfFour(i, j, 1, 1);

        return value;
    }

    public int lineOfFour(int x, int y, int i, int j)
    {
        int value = 1;
        String color = gird[x][y].getColor();

        for(int k = 1; k < 4; k++)
        {
            if(x+i*k < 0 || y+j*k < 0 || x+i*k >= gird.length || y+j*k >= gird[0].length)
                return 0;
            if(gird[x + i * k][y + j * k].getColor().equals(color))
                value++;
            else if (!gird[x + i * k][y + j * k].getColor().equals("None"))
                return 0;
            else
            {
                for(int l = y+j*k; l >= 0; l--)
                    if(gird[x + i * k][l].getColor().equals("None"))
                        value--;
            }
        }

        if(value == 4) return 100;
        if(simple) return 0;
        else return Math.max(value, 0);
    }
}