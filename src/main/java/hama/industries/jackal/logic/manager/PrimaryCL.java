package hama.industries.jackal.logic.manager;

import java.util.UUID;

final class PrimaryCL implements ITriggerCL {
    protected final UUID id;

    private boolean active = false;

    public PrimaryCL() { id = UUID.randomUUID(); }
    public PrimaryCL(UUID id) { this.id = id; }

    void setActive(boolean v){ active = v; }

    boolean isActive() { return active; }
}
