package sample;

public class Board {
    public int i, j;
    public String color;

    public Board() {
        super();
        this.color = "None";
        this.i = 0;
        this.j = 0;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}