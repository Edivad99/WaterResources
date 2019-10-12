package edivad.waterresources.blocks.machines.coolingplate;

import java.util.Collections;

import edivad.waterresources.Main;
import edivad.waterresources.ModLiquids;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class CoolingPlateGUI extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID + ":textures/gui/cooling_plate.png");
	private final TileEntityCoolingPlate tileEntity;

	public CoolingPlateGUI(TileEntityCoolingPlate tileEntity, CoolingPlateContainer container)
	{
		super(container);
		this.tileEntity = tileEntity;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if(mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			drawHoveringText(Collections.singletonList("Ice water: " + this.tileEntity.getClientFluidAmount() + " mB"), mouseX, mouseY, fontRenderer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(this.tileEntity.getClientProgress() != 0)
		{
			String info = "Processing: " + this.tileEntity.getClientProgress() + "%";
			this.fontRenderer.drawString(info, (this.xSize / 2 - this.fontRenderer.getStringWidth(info) / 2) + 34, 25, 4210752);
		}

		String block;
		if(this.tileEntity.isAboveIndustrialDistillator())
			block = "Connected";
		else
			block = "Check block above";

		this.fontRenderer.FONT_HEIGHT = 2;
		this.fontRenderer.drawString(block, (this.xSize / 2 - this.fontRenderer.getStringWidth(block) / 2) + 15, 8, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Render Ice Water
		int z = this.getFluidScaled(60);
		TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(ModLiquids.ice_water.getStill().toString());
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft + 10, this.guiTop + 12 + z, fluidTexture, 16, 60 - z);
	}

	private int getFluidScaled(int pixels)
	{
		int currentLiquidAmount = this.tileEntity.getClientFluidAmount();
		int maxLiquidAmount = CoolingPlate.TANK_CAPACITY;
		int x = currentLiquidAmount * pixels / maxLiquidAmount;
		return pixels - x;
	}
}
