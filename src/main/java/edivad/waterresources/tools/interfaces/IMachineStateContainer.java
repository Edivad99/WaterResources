package edivad.waterresources.tools.interfaces;

public interface IMachineStateContainer {

	void sync(int energy, int progress, int... fluids);
}
