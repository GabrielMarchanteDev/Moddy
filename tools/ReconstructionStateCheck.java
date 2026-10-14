import mymod.memo.MemoryReconstructionState;

public class ReconstructionStateCheck {
    public static void main(String[] args) {
        MemoryReconstructionState state = new MemoryReconstructionState();
        state.observeLife(100, 100);
        if (state.fragments() != 0 || state.progress() != 0F) throw new AssertionError("Initial life");
        state.observeLife(75, 100);
        if (state.fragments() != 1) throw new AssertionError("75 percent remaining");
        state.observeLife(90, 100);
        if (state.fragments() != 1) throw new AssertionError("Healing must not regress");
        state.observeLife(50, 100);
        if (state.fragments() != 2) throw new AssertionError("50 percent remaining");
        state.observeLife(26, 100);
        if (state.complete()) throw new AssertionError("Above 25 percent");
        state.observeLife(25, 100);
        if (!state.complete() || state.fragments() != 3) throw new AssertionError("Complete");
        state.observeLife(-10, 100);
        if (state.progress() != 1F) throw new AssertionError("Cap");
        MemoryReconstructionState jump = new MemoryReconstructionState(); jump.observeLife(0, 100);
        if (!jump.complete()) throw new AssertionError("All phases before victory");
        jump.observeLife(0, 0);
        System.out.println("PASS: life progress 75/50/25 percent, healing, overkill and immediate victory");
    }
}
