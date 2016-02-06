package sq.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import sq.entity.creature.EntityWebslinger;

/**
 * Sets the texture on the webslinger and renders it in-game.
 */
public class RenderWebslinger extends Render
{
	public RenderWebslinger()
	{
		super(Minecraft.getMinecraft().getRenderManager());
	}

	public void renderWebSlinger(EntityWebslinger entityWebslinger, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
	{
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer ren = tessellator.getWorldRenderer();
		
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);

			GL11.glTranslated(posX, posY, posZ);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

			bindTexture(getEntityTexture(entityWebslinger));

			ren.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
			ren.normal(0.0F, 1.0F, 0.0F);
			ren.pos(-0.5F, -0.5F, 0.0D).tex(0, 1).endVertex();
			ren.pos(0.5F, -0.5F, 0.0D).tex(1, 1).endVertex();
			ren.pos(0.5F, 0.5F, 0.0D).tex(1, 0).endVertex();
			ren.pos(-0.5F, 0.5F, 0.0D).tex(0, 0).endVertex();
			tessellator.draw();

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}
		GL11.glPopMatrix();

		if (entityWebslinger.player != null)
		{
			final float deltaYaw = (entityWebslinger.player.prevRotationYaw + (entityWebslinger.player.rotationYaw - entityWebslinger.player.prevRotationYaw) * rotationPitch) * 3.141593F / 180F;

			final Vec3 vec3d = new Vec3(-0.5D, 0.03D, 0.8D);
			vec3d.rotatePitch(-(entityWebslinger.player.prevRotationPitch + (entityWebslinger.player.rotationPitch - entityWebslinger.player.prevRotationPitch) * rotationPitch) * 3.141593F / 180F);
			vec3d.rotateYaw(-(entityWebslinger.player.prevRotationYaw + (entityWebslinger.player.rotationYaw - entityWebslinger.player.prevRotationYaw) * rotationPitch) * 3.141593F / 180F);

			double correctedPosX = entityWebslinger.player.prevPosX + (entityWebslinger.player.posX - entityWebslinger.player.prevPosX) * rotationPitch + vec3d.xCoord;
			double correctedPosY = entityWebslinger.player.prevPosY + (entityWebslinger.player.posY - entityWebslinger.player.prevPosY) * rotationPitch + vec3d.yCoord;
			double correctedPosZ = entityWebslinger.player.prevPosZ + (entityWebslinger.player.posZ - entityWebslinger.player.prevPosZ) * rotationPitch + vec3d.zCoord;

			if (renderManager.options.thirdPersonView > 0)
			{
				final float deltaYawOffset = (entityWebslinger.player.prevRenderYawOffset + (entityWebslinger.player.renderYawOffset - entityWebslinger.player.prevRenderYawOffset) * rotationPitch) * 3.141593F / 180F;
				final double sinDeltaYawOffset = MathHelper.sin(deltaYawOffset);
				final double cosDeltaYawOffset = MathHelper.cos(deltaYawOffset);
				correctedPosX = entityWebslinger.player.prevPosX + (entityWebslinger.player.posX - entityWebslinger.player.prevPosX) * rotationPitch - cosDeltaYawOffset * 0.35D - sinDeltaYawOffset * 0.85D;
				correctedPosY = entityWebslinger.player.prevPosY + (entityWebslinger.player.posY - entityWebslinger.player.prevPosY) * rotationPitch - 0.45D;
				correctedPosZ = entityWebslinger.player.prevPosZ + (entityWebslinger.player.posZ - entityWebslinger.player.prevPosZ) * rotationPitch - sinDeltaYawOffset * 0.35D + cosDeltaYawOffset * 0.85D;
			}

			final double deltaPosX = entityWebslinger.prevPosX + (entityWebslinger.posX - entityWebslinger.prevPosX) * rotationPitch;
			final double deltaPosY = entityWebslinger.prevPosY + (entityWebslinger.posY - entityWebslinger.prevPosY) * rotationPitch + 0.25D;
			final double deltaPosZ = entityWebslinger.prevPosZ + (entityWebslinger.posZ - entityWebslinger.prevPosZ) * rotationPitch;
			final double correctedDeltaPosX = (float) (correctedPosX - deltaPosX);
			final double correctedDeltaPosY = (float) (correctedPosY - deltaPosY);
			final double correctedDeltaPosZ = (float) (correctedPosZ - deltaPosZ);
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			ren.begin(3, DefaultVertexFormats.POSITION);
//			tessellator.startDrawing(3);
//			tessellator.setColorOpaque_I(8161437); //TODO
			
			final int verteces = 16;
			for (int currentVertex = 0; currentVertex <= verteces; currentVertex++)
			{
				final float vertexPos = (float) currentVertex / (float) verteces;
				ren.pos(posX + correctedDeltaPosX * vertexPos, posY + correctedDeltaPosY * vertexPos + 0.2F, posZ + correctedDeltaPosZ * vertexPos).endVertex();
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		renderWebSlinger((EntityWebslinger) entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1)
	{
		return new ResourceLocation("sq:textures/entities/webslinger-ball.png");
	}
}