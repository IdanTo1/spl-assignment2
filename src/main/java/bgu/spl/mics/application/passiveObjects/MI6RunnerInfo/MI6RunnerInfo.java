package bgu.spl.mics.application.passiveObjects.MI6RunnerInfo;


import java.util.List;

public class MI6RunnerInfo {
    private List<String> inventory;
    private Services services;
    private List<AgentInfo> squad;

    // Services getters
    public List<String> getInventory() {
        return inventory;
    }

    public Services getServices() {
        return services;
    }

    public List<AgentInfo> getSquad() {
        return squad;
    }
}

