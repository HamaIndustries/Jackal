package hama.industries.jackal.logic;

import java.util.UUID;

final class PrimaryCL implements Comparable<PrimaryCL> {
    protected final UUID id;

    private boolean active = false;

    public PrimaryCL() { id = UUID.randomUUID(); }
    public PrimaryCL(UUID id) { this.id = id; }

    void setActive(boolean v){ active = v; }

    boolean isActive() { return active; }

    @Override
    public int compareTo(PrimaryCL other) {        
        return id.compareTo(other.id);
    }
}
