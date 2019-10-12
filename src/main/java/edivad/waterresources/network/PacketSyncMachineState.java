package edivad.waterresources.network;

import edivad.waterresources.Main;
import edivad.waterresources.tools.interfaces.IMachineStateContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMachineState implements IMessage {

	private int energy;
	private int progress;
	private int[] fluids;
	private int dimension;

	public PacketSyncMachineState()
	{
	}

	public PacketSyncMachineState(int energy, int progress, int... fluids)
	{
		this.energy = energy;
		this.progress = progress;
		if(fluids != null)
		{
			this.fluids = fluids;
			this.dimension = fluids.length;
		}
		else
			this.dimension = 0;

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(energy);
		buf.writeInt(progress);
		buf.writeInt(dimension);

		for(int i = 0; i < dimension; i++)
		{
			buf.writeInt(fluids[i]);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		try
		{
			energy = buf.readInt();
			progress = buf.readInt();

			dimension = buf.readInt();// Update array dimension
			if(dimension != 0)
				fluids = new int [dimension];
			else
				fluids = null;

			for(int i = 0; i < dimension; i++)
			{
				fluids[i] = buf.readInt();
			}
		}
		catch(IndexOutOfBoundsException ioe)
		{
			Main.logger.debug(ioe.getMessage());
			return;
		}
	}

	public static class Handler implements IMessageHandler<PacketSyncMachineState, IMessage> {

		@Override
		public IMessage onMessage(PacketSyncMachineState message, MessageContext ctx)
		{
			Main.proxy.addScheduledTaskClient(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketSyncMachineState message, MessageContext ctx)
		{
			EntityPlayer player = Main.proxy.getClientPlayer();
			if(player.openContainer instanceof IMachineStateContainer)
			{
				((IMachineStateContainer) player.openContainer).sync(message.energy, message.progress, message.fluids);
			}
		}
	}
}
