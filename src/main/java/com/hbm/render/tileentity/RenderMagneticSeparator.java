package com.hbm.render.tileentity;

import org.lwjgl.opengl.GL11;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.tileentity.machine.TileEntityMachineMagneticSeparator;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class RenderMagneticSeparator extends TileEntitySpecialRenderer implements IItemRendererProvider {

	private static final int[] OPEN = TileEntityMachineMagneticSeparator.OPENING_ANIMATION_TICKS;
	private static final int[] CLOSE = TileEntityMachineMagneticSeparator.CLOSING_ANIMATION_TICKS;
	private static final float MULT = TileEntityMachineMagneticSeparator.ROTATION_MULTIPLIER;

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float interp) {
		TileEntityMachineMagneticSeparator sep = (TileEntityMachineMagneticSeparator) te;

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		switch(te.getBlockMetadata() - BlockDummyable.offset) {
		case 2: GL11.glRotatef(90, 0F, 1F, 0F); break;
		case 4: GL11.glRotatef(180, 0F, 1F, 0F); break;
		case 3: GL11.glRotatef(270, 0F, 1F, 0F); break;
		case 5: GL11.glRotatef(0, 0F, 1F, 0F); break;
		}

		GL11.glTranslated(0.5, 0, 0.5);

		bindTexture(ResourceManager.magnetic_separator_tex);
		renderAt(sep, interp);

		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	private void renderAt(TileEntityMachineMagneticSeparator sep, float pt) {
		ResourceManager.magnetic_separator.renderPart("Base");
		ResourceManager.magnetic_separator.renderPart("mesh");
		ResourceManager.magnetic_separator.renderPart("mesh.001");

		if(sep.open) {
			if(sep.animationTicks == 0) {
				GL11.glRotatef(180F, 1F, 0F, 0F);
				ResourceManager.magnetic_separator.renderPart("InnerCover");
				ResourceManager.magnetic_separator.renderPart("OuterCover");
			} else if(sep.animRotation > 0) {
				float a = sep.animRotation < OPEN[2] ? sep.animRotation + pt : sep.animRotation;
				float rot = inExpo(a / OPEN[2]) * 180F;
				GL11.glRotatef(rot, 1F, 0F, 0F);
				if(sep.animRotation < OPEN[2]) {
					GL11.glPushMatrix();
					GL11.glRotatef(sep.rotation * MULT, 0F, 1F, 0F);
					ResourceManager.magnetic_separator.renderPart("Hull");
					GL11.glPopMatrix();
				}
				ResourceManager.magnetic_separator.renderPart("OuterCover");
				GL11.glRotatef(-rot * 2F, 1F, 0F, 0F);
				ResourceManager.magnetic_separator.renderPart("InnerCover");
			} else {
				float a = sep.animAcceleration < OPEN[0] ? sep.animAcceleration + pt : sep.animAcceleration;
				float rot = outQuart(a / OPEN[0]) * 360F + sep.rotation * MULT;
				GL11.glRotatef(rot, 0F, 1F, 0F);
				ResourceManager.magnetic_separator.renderPart("Hull");
			}
		} else {
			if(sep.animationTicks == 0) {
				GL11.glRotatef((sep.rotation + pt) * MULT, 0F, 1F, 0F);
				ResourceManager.magnetic_separator.renderPart("Hull");
			} else if(sep.animAcceleration > 0) {
				float a = sep.animAcceleration < CLOSE[2] ? sep.animAcceleration + pt : sep.animAcceleration;
				float extra = sep.animAcceleration < CLOSE[2] ? sep.rotation : sep.rotation + pt;
				float rot = inQuart(a / CLOSE[2]) * 360F + extra * MULT;
				GL11.glRotatef(rot, 0F, 1F, 0F);
				ResourceManager.magnetic_separator.renderPart("Hull");
			} else if(sep.animRotation > 0) {
				float a = sep.animRotation < CLOSE[1] ? sep.animRotation + pt : sep.animRotation;
				float rot = 180F - inExpo(a / CLOSE[1]) * 180F;
				GL11.glRotatef(rot, 1F, 0F, 0F);

				GL11.glPushMatrix();
				GL11.glRotatef(sep.rotation * MULT, 0F, 1F, 0F);
				ResourceManager.magnetic_separator.renderPart("Hull");
				GL11.glPopMatrix();

				ResourceManager.magnetic_separator.renderPart("OuterCover");
				GL11.glRotatef(-rot * 2F, 1F, 0F, 0F);
				ResourceManager.magnetic_separator.renderPart("InnerCover");
			} else {
				GL11.glRotatef(180F, 1F, 0F, 0F);
				ResourceManager.magnetic_separator.renderPart("InnerCover");
				ResourceManager.magnetic_separator.renderPart("OuterCover");
			}
		}
	}

	private static float inExpo(float x) { return x == 0 ? 0 : (float) Math.pow(2, 10 * x - 10); }
	private static float outQuart(float x) { return (float) (1 - Math.pow(1 - x, 4)); }
	private static float inQuart(float x) { return x * x * x * x; }

	@Override
	public Item getItemForRenderer() {
		return Item.getItemFromBlock(ModBlocks.machine_magnetic_separator);
	}

	@Override
	public IItemRenderer getRenderer() {
		return new ItemRenderBase() {
			public void renderInventory() {
				GL11.glTranslated(0, -2.5, 0);
				GL11.glScaled(2.5, 2.5, 2.5);
			}
			public void renderCommon() {
				GL11.glScaled(0.75, 0.75, 0.75);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				bindTexture(ResourceManager.magnetic_separator_tex);
				ResourceManager.magnetic_separator.renderPart("Hull");
				GL11.glShadeModel(GL11.GL_FLAT);
			}
		};
	}
}
