package edivad.waterresources;

import edivad.waterresources.fluids.AuriferousWater;
import edivad.waterresources.fluids.DistilledWater;
import edivad.waterresources.fluids.IceWater;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModLiquids {

	public static final Fluid distilled_water = new DistilledWater();
	public static final Fluid ice_water = new IceWater();
	public static final Fluid auriferous_water = new AuriferousWater();

	public static void init()
	{
		FluidRegistry.registerFluid(distilled_water);
		FluidRegistry.addBucketForFluid(distilled_water);

		FluidRegistry.registerFluid(ice_water);
		FluidRegistry.addBucketForFluid(ice_water);

		FluidRegistry.registerFluid(auriferous_water);
		FluidRegistry.addBucketForFluid(auriferous_water);
	}

	public static boolean isValidFloadStack(FluidStack stack)
	{
		return getFluidFromStack(stack) == distilled_water || getFluidFromStack(stack) == ice_water || getFluidFromStack(stack) == auriferous_water;
	}

	public static Fluid getFluidFromStack(FluidStack stack)
	{
		return stack == null ? null : stack.getFluid();
	}

	public static String getFluidName(FluidStack stack)
	{
		Fluid fluid = getFluidFromStack(stack);
		return getFluidName(fluid);
	}

	public static String getFluidName(Fluid fluid)
	{
		return fluid == null ? "null" : fluid.getName();
	}

	public static int getAmount(FluidStack stack)
	{
		return stack == null ? 0 : stack.amount;
	}
}
