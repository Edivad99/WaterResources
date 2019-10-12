package edivad.waterresources.blocks.machines.coolingplate;

import net.minecraft.util.IStringSerializable;

public enum CoolingPlateState implements IStringSerializable {

	OFF("off"), // Idle
	WORKING("working"), // The machinery is working
	NOLIQUID("noliquid");// There is no liquid

	public static final CoolingPlateState[] VALUES = CoolingPlateState.values();

	private final String name;

	CoolingPlateState(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
