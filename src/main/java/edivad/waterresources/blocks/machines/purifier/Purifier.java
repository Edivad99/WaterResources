package edivad.waterresources.blocks.machines.purifier;

import java.util.List;

import org.lwjgl.input.Keyboard;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.common.GenericMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Purifier extends GenericMachine implements ITileEntityProvider {

	public static final ResourceLocation resourceLocation = new ResourceLocation(Main.MODID, "purifier");
	public static final String machineID = Main.MODID + ".purifier";

	public Purifier()
	{
		super("purifier");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityPurifier();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null)
		{
			int energy = tagCompound.getInteger("energy");
			int sizeIn = getItemCount(tagCompound, "itemsIn");
			int sizeOut = getItemCount(tagCompound, "itemsOut");

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				addInformationLocalized(tooltip, "message.waterresources.purifier", energy, sizeIn, sizeOut);
			else
				addInformationLocalized(tooltip, "message.waterresources.hold_shift");
		}
	}
}
