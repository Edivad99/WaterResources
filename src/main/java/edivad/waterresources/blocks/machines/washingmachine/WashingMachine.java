package edivad.waterresources.blocks.machines.washingmachine;

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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WashingMachine extends GenericMachine implements ITileEntityProvider {

	public static final ResourceLocation resourceLocation = new ResourceLocation(Main.MODID, "washing_machine");
	public static final String machineID = Main.MODID + ".washing_machine";

	public WashingMachine()
	{
		super("washing_machine");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityWashingMachine();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null)
		{
			int energy = tagCompound.getInteger("energy");
			int itemsOut = getItemCount(tagCompound, "itemsOut");

			FluidTank info = new FluidTank(TANK_CAPACITY);

			int water = info.readFromNBT(tagCompound.getCompoundTag("tankWater")).getFluidAmount();
			int auriferousWater = info.readFromNBT(tagCompound.getCompoundTag("tankAU")).getFluidAmount();

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				addInformationLocalized(tooltip, "message.waterresources.washing_machine", energy, itemsOut, water, auriferousWater);
			else
				addInformationLocalized(tooltip, "message.waterresources.hold_shift");
		}
	}
}
