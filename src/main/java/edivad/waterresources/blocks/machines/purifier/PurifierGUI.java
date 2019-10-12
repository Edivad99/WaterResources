package edivad.waterresources.blocks.machines.purifier;

import java.util.Collections;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PurifierGUI extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID + ":textures/gui/purifier.png");
	private final TileEntityPurifier tileEntity;

	public PurifierGUI(TileEntityPurifier tileEntity, PurifierContainer container)
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
}
