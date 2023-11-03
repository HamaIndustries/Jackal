package hama.industries.jackal.logic;

import java.util.UUID;

public final class PrimaryCL implements Comparable<PrimaryCL> {
    protected final UUID id = UUID.randomUUID();

    private boolean active = false;
    private ICLManagerCapability manager;

    public PrimaryCL(ICLManagerCapability manager) {
        this.manager = manager;
    }

    void setActive(boolean v){ active = v; }

    boolean isActive() { return active; }

    @Override
    public int compareTo(PrimaryCL other) {        
        return id.compareTo(other.id);
    }
}
