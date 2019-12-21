package bgu.spl.mics;

public class TestSubscriber extends Subscriber {
    private int num;

    public TestSubscriber(String name) {
        super(name);
        num = -1;
        initialize();
    }

    public void initialize() {
        MessageBroker m = MessageBrokerImpl.getInstance();
        m.register(this);
        this.subscribeEvent(IntEvent.class, (IntEvent e) -> this.num = e.getNum());
        this.subscribeBroadcast(IntBroadcast.class, (IntBroadcast b) -> this.num = b.getNum());
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
