package edivad.waterresources.blocks.machines.extractor;

import java.util.Collections;

import edivad.waterresources.Main;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class ExtractorGUI extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID + ":textures/gui/extractor.png");
	private final TileEntityExtractor tileEntity;

	public ExtractorGUI(TileEntityExtractor tileEntity, ExtractorContainer container)
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
		if(mouseX > guiLeft + 7 && mouseX < guiLeft + 19 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			drawHoveringText(Collections.singletonList("Energy: " + this.tileEntity.getClientEnergy() + " FE"), mouseX, mouseY, fontRenderer);
		if(mouseX > guiLeft + 35 && mouseX < guiLeft + 56 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			drawHoveringText(Collections.singletonList("Distilled water: " + this.tileEntity.getClientFluidInAmount() + " mB"), mouseX, mouseY, fontRenderer);
		if(mouseX > guiLeft + 140 && mouseX < guiLeft + 161 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			drawHoveringText(Collections.singletonList("Auriferous water: " + this.tileEntity.getClientFluidOutAmount() + " mB"), mouseX, mouseY, fontRenderer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		int x = this.getWorkProgressScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 99, this.guiTop + 35, 176, 0, x, 17);

		// Energy
		int y = this.getEnergyScaled(60);
		this.drawTexturedModalRect(this.guiLeft + 10, this.guiTop + 12 + y, 176, 17, 6, 60 - y);

		// Render Distilled Water
		int z = this.getFluidScaled(60, this.tileEntity.getClientFluidInAmount());
		TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(ModLiquids.distilled_water.getStill().toString());
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft + 38, this.guiTop + 12 + z, fluidTexture, 16, 60 - z);

		// Render Auriferous Water
		int z1 = this.getFluidScaled(60, this.tileEntity.getClientFluidOutAmount());
		fluidTexture = mc.getTextureMapBlocks().getTextureExtry(ModLiquids.auriferous_water.getStill().toString());
		this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 12 + z1, fluidTexture, 16, 60 - z1);
	}

	private int getWorkProgressScaled(int pixels)
	{
		if(this.tileEntity.getClientProgress() > 0)
		{
			int progress = this.tileEntity.getClientProgress();
			int x = progress * pixels / 100;
			return x;
		}
		return 0;
	}

	private int getEnergyScaled(int pixels)
	{
		int currentEnergy = this.tileEntity.getClientEnergy();
		int maxEnergy = PoweredTileEntity.MAX_CAPACITY;
		int x = currentEnergy * pixels / maxEnergy;
		return pixels - x;
	}

	private int getFluidScaled(int pixels, int amount)
	{
		int maxLiquidAmount = Extractor.TANK_CAPACITY;
		int x = amount * pixels / maxLiquidAmount;
		return pixels - x;
	}
}
