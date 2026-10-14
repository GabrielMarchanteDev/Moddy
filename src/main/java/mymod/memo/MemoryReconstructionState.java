package mymod.memo;

/** Objetivos de prueba independientes de la escena y de sus recursos. */
public final class MemoryReconstructionState {
    private float progress;
    public void observeLife(int remaining, int initial) {
        if (initial <= 0) return;
        float restored = 1F - Math.max(0, Math.min(initial, remaining)) / (float) initial;
        progress = Math.max(progress, restored);
    }
    public float progress() { return progress; }
    public int fragments() { return progress >= 0.75F ? 3 : progress >= 0.5F ? 2 : progress >= 0.25F ? 1 : 0; }
    public boolean complete() { return progress >= 0.75F; }
}
