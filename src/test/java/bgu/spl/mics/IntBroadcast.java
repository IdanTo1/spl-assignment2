package bgu.spl.mics;

public class IntBroadcast implements Broadcast {
    private final int num;

    public IntBroadcast(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
