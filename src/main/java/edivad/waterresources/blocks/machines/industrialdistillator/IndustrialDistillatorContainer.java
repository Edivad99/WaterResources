package edivad.waterresources.blocks.machines.industrialdistillator;

import edivad.waterresources.config.IndustrialDistillatorConfig;
import edivad.waterresources.network.Messages;
import edivad.waterresources.network.PacketSyncMachineState;
import edivad.waterresources.tools.interfaces.IMachineStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class IndustrialDistillatorContainer extends Container implements IMachineStateContainer {

	private final TileEntityIndustrialDistillator tileEntity;

	public IndustrialDistillatorContainer(IInventory playerInventory, TileEntityIndustrialDistillator tileEntity)
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
		// for sync variable from client and server
		super.detectAndSendChanges();
		if(!this.tileEntity.getWorld().isRemote)
		{
			if(tileEntity.getProgress() != tileEntity.getClientProgress() || tileEntity.getEnergy() != tileEntity.getClientEnergy() || tileEntity.getFluidInAmount() != tileEntity.getClientFluidInAmount() || tileEntity.getFluidOutAmount() != tileEntity.getClientFluidOutAmount())
			{
				tileEntity.setClientEnergy(tileEntity.getEnergy());
				tileEntity.setClientProgress(tileEntity.getProgress());
				tileEntity.setClientFluidInAmount(tileEntity.getFluidInAmount());
				tileEntity.setClientFluidOutAmount(tileEntity.getFluidOutAmount());

				for(IContainerListener listener : listeners)
				{
					if(listener instanceof EntityPlayerMP)
					{
						EntityPlayerMP player = (EntityPlayerMP) listener;
						int percent = 100 - tileEntity.getProgress() * 100 / IndustrialDistillatorConfig.MAX_PROGRESS;
						Messages.INSTANCE.sendTo(new PacketSyncMachineState(this.tileEntity.getEnergy(), percent, tileEntity.getFluidInAmount(), tileEntity.getFluidOutAmount()), player);
					}
				}
			}
		}

	}

	@Override
	public void sync(int energy, int progress, int... fluids)
	{
		this.tileEntity.setClientEnergy(energy);
		this.tileEntity.setClientProgress(progress);
		this.tileEntity.setClientFluidInAmount(fluids[0]);
		this.tileEntity.setClientFluidOutAmount(fluids[1]);
	}
}
