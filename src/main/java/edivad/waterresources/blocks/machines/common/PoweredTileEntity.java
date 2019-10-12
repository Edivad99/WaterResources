package edivad.waterresources.blocks.machines.common;

import edivad.waterresources.tools.MyEnergyStorage;
import edivad.waterresources.tools.interfaces.IRestorableTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class PoweredTileEntity extends TileEntity implements IRestorableTileEntity {

	// Processing
	private int progress = 0;
	private int clientProgress = -1;

	// Energy
	public static final int MAX_CAPACITY = 25000;
	public static final int RF_PER_TICK = 80;
	public static final int RF_PER_TICK_INPUT = RF_PER_TICK * 2;

	protected MyEnergyStorage energyStorage = new MyEnergyStorage(MAX_CAPACITY, RF_PER_TICK_INPUT);
	private int clientEnergy = -1;

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// To save tileEntity
		super.readFromNBT(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		writeRestorableToNBT(compound);
		return compound;
	}

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		this.progress = compound.getInteger("progress");
		this.energyStorage.setEnergy(compound.getInteger("energy"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		compound.setInteger("progress", this.progress);
		compound.setInteger("energy", energyStorage.getEnergyStored());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		return super.getCapability(capability, facing);
	}

	// Getter/Setter Server Progress
	public int getProgress()
	{
		return progress;
	}

	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	public void decrementProgress()
	{
		this.progress--;
	}

	// Getter/Setter Client Progress
	public int getClientProgress()
	{
		return clientProgress;
	}

	public void setClientProgress(int clientProgress)
	{
		this.clientProgress = clientProgress;
	}

	// Getter/Setter Client Energy
	public int getClientEnergy()
	{
		return clientEnergy;
	}

	public void setClientEnergy(int clientEnergy)
	{
		this.clientEnergy = clientEnergy;
	}

	public int getEnergy()
	{
		return energyStorage.getEnergyStored();
	}
}
