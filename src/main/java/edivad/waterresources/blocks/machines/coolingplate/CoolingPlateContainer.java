package edivad.waterresources.blocks.machines.coolingplate;

import edivad.waterresources.config.CoolingPlateConfig;
import edivad.waterresources.network.Messages;
import edivad.waterresources.network.PacketSyncMachineState;
import edivad.waterresources.tools.interfaces.IMachineStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class CoolingPlateContainer extends Container implements IMachineStateContainer {

	private final TileEntityCoolingPlate tileEntity;

	public CoolingPlateContainer(IInventory playerInventory, TileEntityCoolingPlate tileEntity)
	{
		this.tileEntity = tileEntity;

		addplayerSolots(playerInventory);
	}

	private void addplayerSolots(IInventory playerInventory)
	{
		// Main Inventory
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		// Hotbar
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileEntity.canInteractWith(playerIn);
	}

	@Override
	public void detectAndSendChanges()
	{
		// For sync variable from client and server
		super.detectAndSendChanges();
		if(!this.tileEntity.getWorld().isRemote)
		{
			if(tileEntity.getFluidAmount() != tileEntity.getClientFluidAmount() || tileEntity.getProgress() != tileEntity.getClientProgress())
			{
				tileEntity.setClientFluidAmount(tileEntity.getFluidAmount());
				tileEntity.setClientProgress(tileEntity.getProgress());

				for(IContainerListener listener : listeners)
				{
					if(listener instanceof EntityPlayerMP)
					{
						EntityPlayerMP player = (EntityPlayerMP) listener;
						int percent = 100 - tileEntity.getProgress() * 100 / CoolingPlateConfig.MAX_PROGRESS;
						Messages.INSTANCE.sendTo(new PacketSyncMachineState(0, percent, tileEntity.getFluidAmount()), player);
					}
				}
			}
		}

	}

	@Override
	public void sync(int energy, int progress, int... fluids)
	{
		this.tileEntity.setClientProgress(progress);
		this.tileEntity.setClientFluidAmount(fluids[0]);
	}
}
