package bgu.spl.mics;

public class IntEvent implements Event<Integer> {
    private final int num;

    public IntEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
