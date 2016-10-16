package sq.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import sq.client.render.RenderSpiderQueenPlayer;
import sq.core.SpiderCore;

/**
 * Defines the Spider Queen's in-game model for the player. Extends from ModelBiped to 
 * allow the use of the held item layer (which is a custom implementation that corrects
 * the actual render location due to differences in arm size).
 * 
 * The biped model is not rendered at all, but rather it is used to copy rotation angles
 * from the arms and body when swinging.
 */
public class ModelSpiderQueen extends ModelBiped
{
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer rear;
	private final ModelRenderer torso;
	private final ModelRenderer armLeft;
	private final ModelRenderer armRight;
	private final ModelRenderer breasts;
	private final ModelRenderer spinner1;
	private final ModelRenderer spinner2;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;
	private final ModelRenderer leg5;
	private final ModelRenderer leg6;
	private final ModelRenderer leg7;
	private final ModelRenderer leg8;
	private final ModelRenderer playerHead;
	private final ModelRenderer playerHeadwear;
	private final ModelRenderer playerBody;
	private final ModelRenderer playerArmLeft;
	private final ModelRenderer playerArmRight;

	public ModelSpiderQueen()
	{
		textureWidth = 64;
		textureHeight = 32;

		head = new ModelRenderer(this, 32, 4);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, 3F, -3F);
		head.setTextureSize(64, 32);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
        playerHeadwear = new ModelRenderer(this, 32, 0);
        playerHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F + 0.5F);
        playerHeadwear.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
        playerHeadwear.mirror = true;
        setRotation(playerHeadwear, 0F, 0F, 0F);
		body = new ModelRenderer(this, 1, 0);
		body.addBox(-3F, -3F, -3F, 6, 6, 5);
		body.setRotationPoint(0F, 15F, 0F);
		body.setTextureSize(64, 32);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		rear = new ModelRenderer(this, 1, 11);
		rear.addBox(-5F, -4F, -6F, 10, 10, 11);
		rear.setRotationPoint(0F, 11F, 7F);
		rear.setTextureSize(64, 32);
		rear.mirror = true;
		setRotation(rear, 0F, 0F, 0F);
		torso = new ModelRenderer(this, 44, 18);
		torso.addBox(-3F, -10F, -2F, 6, 10, 4);
		torso.setRotationPoint(0F, 13F, -3F);
		torso.setTextureSize(64, 32);
		torso.mirror = true;
		setRotation(torso, 0F, 0F, 0F);
		armLeft = new ModelRenderer(this, 56, 0);
		armLeft.addBox(-1F, -1F, -1F, 2, 10, 2);
		armLeft.setRotationPoint(4F, 4F, -3F);
		armLeft.setTextureSize(64, 32);
		armLeft.mirror = true;
		setRotation(armLeft, 0F, 0F, 0F);
		armRight = new ModelRenderer(this, 56, 0);
		armRight.addBox(-1F, -1F, -1F, 2, 10, 2);
		armRight.setRotationPoint(-4F, 4F, -3F);
		armRight.setTextureSize(64, 32);
		armRight.mirror = true;
		setRotation(armRight, 0F, 0F, 0F);
		breasts = new ModelRenderer(this, 46, 20);
		breasts.addBox(-3F, -7F, -7F, 6, 3, 2);
		breasts.setRotationPoint(0F, 13F, -3F);
		breasts.setTextureSize(64, 32);
		breasts.mirror = true;
		setRotation(breasts, -0.5846853F, 0F, 0F);
		spinner1 = new ModelRenderer(this, 0, 0);
		spinner1.addBox(-1F, -3F, 0F, 2, 3, 1);
		spinner1.setRotationPoint(2F, 15F, 11F);
		spinner1.setTextureSize(64, 32);
		spinner1.mirror = true;
		setRotation(spinner1, -0.3228859F, 0F, 0F);
		spinner2 = new ModelRenderer(this, 0, 0);
		spinner2.addBox(-1F, -3F, 0F, 2, 3, 1);
		spinner2.setRotationPoint(-2F, 15F, 11F);
		spinner2.setTextureSize(64, 32);
		spinner2.mirror = true;
		setRotation(spinner2, -0.3228859F, 0F, 0F);
		leg1 = new ModelRenderer(this, 18, 0);
		leg1.addBox(-15F, -1F, -1F, 16, 2, 2);
		leg1.setRotationPoint(-4F, 15F, 2F);
		leg1.setTextureSize(64, 32);
		leg1.mirror = true;
		setRotation(leg1, 0.5759587F, 0.1919862F, 0F);
		leg2 = new ModelRenderer(this, 18, 0);
		leg2.addBox(-1F, -1F, -1F, 16, 2, 2);
		leg2.setRotationPoint(4F, 15F, 2F);
		leg2.setTextureSize(64, 32);
		leg2.mirror = true;
		setRotation(leg2, -0.5759587F, -0.1919862F, 0F);
		leg3 = new ModelRenderer(this, 18, 0);
		leg3.addBox(-15F, -1F, -1F, 16, 2, 2);
		leg3.setRotationPoint(-4F, 15F, 1F);
		leg3.setTextureSize(64, 32);
		leg3.mirror = true;
		setRotation(leg3, 0.2792527F, 0.1919862F, 0F);
		leg4 = new ModelRenderer(this, 18, 0);
		leg4.addBox(-1F, -1F, -1F, 16, 2, 2);
		leg4.setRotationPoint(4F, 15F, 1F);
		leg4.setTextureSize(64, 32);
		leg4.mirror = true;
		setRotation(leg4, -0.2792527F, -0.1919862F, 0F);
		leg5 = new ModelRenderer(this, 18, 0);
		leg5.addBox(-15F, -1F, -1F, 16, 2, 2);
		leg5.setRotationPoint(-4F, 15F, 0F);
		leg5.setTextureSize(64, 32);
		leg5.mirror = true;
		setRotation(leg5, -0.2792527F, 0.1919862F, 0F);
		leg6 = new ModelRenderer(this, 18, 0);
		leg6.addBox(-1F, -1F, -1F, 16, 2, 2);
		leg6.setRotationPoint(4F, 15F, 0F);
		leg6.setTextureSize(64, 32);
		leg6.mirror = true;
		setRotation(leg6, 0.2792527F, -0.1919862F, 0F);
		leg7 = new ModelRenderer(this, 18, 0);
		leg7.addBox(-15F, -1F, -1F, 16, 2, 2);
		leg7.setRotationPoint(-4F, 15F, -1F);
		leg7.setTextureSize(64, 32);
		leg7.mirror = true;
		setRotation(leg7, -0.5759587F, 0.1919862F, 0F);
		leg8 = new ModelRenderer(this, 18, 0);
		leg8.addBox(-1F, -1F, -1F, 16, 2, 2);
		leg8.setRotationPoint(4F, 15F, -1F);
		leg8.setTextureSize(64, 32);
		leg8.mirror = true;
		setRotation(leg8, -0.5759587F, -0.1919862F, 0F);
		playerHead = new ModelRenderer(this, 0, 0);
		playerHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		playerHead.setRotationPoint(0F, 3F, -3F);
		playerHead.setTextureSize(64, 32);
		playerHead.mirror = true;
		setRotation(playerHead, 0F, 0F, 0F);
		playerBody = new ModelRenderer(this, 16, 16);
		playerBody.addBox(-4F, 0F, -2F, 8, 10, 4);
		playerBody.setRotationPoint(0F, 3F, -3F);
		playerBody.setTextureSize(64, 32);
		playerBody.mirror = true;
		setRotation(playerBody, 0F, 0F, 0F);
		playerArmLeft = new ModelRenderer(this, 40, 16);
		playerArmLeft.addBox(-3F, -2F, -2F, 4, 12, 4);
		playerArmLeft.setRotationPoint(-5F, 5F, -3F);
		playerArmLeft.setTextureSize(64, 32);
		playerArmLeft.mirror = true;
		setRotation(playerArmLeft, 0F, 0F, 0F);
		playerArmRight = new ModelRenderer(this, 40, 16);
		playerArmRight.addBox(-1F, -2F, -2F, 4, 12, 4);
		playerArmRight.setRotationPoint(5F, 5F, -3F);
		playerArmRight.setTextureSize(64, 32);
		playerArmRight.mirror = true;
		setRotation(playerArmRight, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5);

		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) entity;

			//TODO
//			//If the player is on the rider spider, we must rotate them to match the orientation
//			//of the spider as it goes up the wall.
//			if (player.ridingEntity instanceof EntitySpiderEx)
//			{
//				final EntitySpiderEx spider = (EntitySpiderEx) player.ridingEntity;
//
//				if (spider.isOnLadder())
//				{
//					//Use the look vector as a reference.
//					final Vec3 lookVector = spider.getLookVec();
//
//					//Rotate as needed.
//					if (lookVector.xCoord <= -0.90 || lookVector.zCoord <= -0.90)
//					{
//						GL11.glRotatef(270, 1, 0, 0);
//						GL11.glTranslated(0, 1.8, 0);
//					}
//
//					else if (lookVector.xCoord >= 0.90 || lookVector.zCoord >= 0.90)
//					{
//						GL11.glRotatef(-90, 1, 0, 0);
//						GL11.glTranslated(0, 1.8, 0);
//					}
//				}
//			}
		}

		//If this player wants their skin overlayed on the spider model.
		if (SpiderCore.getConfig().usePlayerSkin && entity instanceof EntityPlayer)
		{
			//Bind the client player's texture for these next few renders.
			AbstractClientPlayer mp = (AbstractClientPlayer)entity;	
			Minecraft.getMinecraft().renderEngine.bindTexture(mp.getLocationSkin());

			//Apply translation and scale corrections.
			GL11.glPushMatrix();
			{
				GL11.glScaled(0.90D, 0.90D, 0.90D);
				playerHead.render(f5);
				playerBody.render(f5);

				//Arms were a disaster, translated them slightly to fix.
				GL11.glPushMatrix();
				{
					GL11.glTranslated(-0.25D, 0.0D, 0.0D);
					{
						playerArmLeft.render(f5);
					}
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslated(0.25D, 0.0D, 0.0D);
					{
						playerArmRight.render(f5);
					}
				}
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
				{
					GL11.glTranslated(0.0D, -0.25D, 0.25D);
					playerHeadwear.render(f5);
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
		}

		else //Render the default spider queen front/likeness.
		{
			head.render(f5);
			torso.render(f5);
			breasts.render(f5);
			armLeft.render(f5);
			armRight.render(f5);
		}

		//Bind the spider queen texture, and finish rendering the spider body.
		Minecraft.getMinecraft().renderEngine.bindTexture(RenderSpiderQueenPlayer.SPIDER_QUEEN_TEXTURES[0]);
		body.render(f5);
		rear.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		leg5.render(f5);
		leg6.render(f5);
		leg7.render(f5);
		leg8.render(f5);
		spinner1.render(f5);
		spinner2.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		head.rotateAngleY = playerHead.rotateAngleY = f3 / (180F / (float) Math.PI);
		head.rotateAngleX = playerHead.rotateAngleX = f4 / (180F / (float) Math.PI);
        playerHeadwear.rotateAngleY = head.rotateAngleY;
        playerHeadwear.rotateAngleX = head.rotateAngleX;
        
		float f6 = 0.7853982F;
		leg1.rotateAngleZ = -f6;
		leg2.rotateAngleZ = f6;
		leg3.rotateAngleZ = -f6 * 0.74F;
		leg4.rotateAngleZ = f6 * 0.74F;
		leg5.rotateAngleZ = -f6 * 0.74F;
		leg6.rotateAngleZ = f6 * 0.74F;
		leg7.rotateAngleZ = -f6;
		leg8.rotateAngleZ = f6;
		float f7 = -0F;
		final float f8 = 0.3926991F;
		leg1.rotateAngleY = f8 * 2.0F + f7;
		leg2.rotateAngleY = -f8 * 2.0F - f7;
		leg3.rotateAngleY = f8 * 1.0F + f7;
		leg4.rotateAngleY = -f8 * 1.0F - f7;
		leg5.rotateAngleY = -f8 * 1.0F + f7;
		leg6.rotateAngleY = f8 * 1.0F - f7;
		leg7.rotateAngleY = -f8 * 2.0F + f7;
		leg8.rotateAngleY = f8 * 2.0F - f7;
		final float f9 = -(MathHelper.cos(f * 0.6662F * 2.0F + 0.0F) * 0.4F) * f1;
		final float f10 = -(MathHelper.cos(f * 0.6662F * 2.0F + 3.141593F) * 0.4F) * f1;
		final float f11 = -(MathHelper.cos(f * 0.6662F * 2.0F + 1.570796F) * 0.4F) * f1;
		final float f12 = -(MathHelper.cos(f * 0.6662F * 2.0F + 4.712389F) * 0.4F) * f1;
		final float f13 = Math.abs(MathHelper.sin(f * 0.6662F + 0.0F) * 0.4F) * f1;
		final float f14 = Math.abs(MathHelper.sin(f * 0.6662F + 3.141593F) * 0.4F) * f1;
		final float f15 = Math.abs(MathHelper.sin(f * 0.6662F + 1.570796F) * 0.4F) * f1;
		final float f16 = Math.abs(MathHelper.sin(f * 0.6662F + 4.712389F) * 0.4F) * f1;

		armRight.rotateAngleX = playerArmRight.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
		armLeft.rotateAngleX = playerArmLeft.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		armRight.rotateAngleZ = playerArmRight.rotateAngleZ = 0.0F;
		armLeft.rotateAngleZ = playerArmLeft.rotateAngleZ = 0.0F;

		if (isRiding)
		{
			playerArmRight.rotateAngleX = armRight.rotateAngleX += -0.6283185F;
			playerArmLeft.rotateAngleX = armLeft.rotateAngleX += -0.6283185F;
		}

		armRight.rotateAngleY = playerArmRight.rotateAngleY = 0.0F;
		armLeft.rotateAngleY = playerArmLeft.rotateAngleY = 0.0F;

		/* For swinging motion copy the rotation angles from the biped model */
		copyRotationAngles(bipedLeftArm, armLeft);
		copyRotationAngles(bipedRightArm, armRight);
		copyRotationAngles(bipedBody, torso);

		body.rotateAngleX = 0.0F;
		head.rotationPointY = playerHead.rotationPointY = 3F;

		playerArmRight.rotateAngleZ = armRight.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		playerArmLeft.rotateAngleZ = armLeft.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		playerArmRight.rotateAngleX = armRight.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		playerArmLeft.rotateAngleX = armLeft.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;

		leg1.rotateAngleY += f9;
		leg2.rotateAngleY += -f9;
		leg3.rotateAngleY += f10;
		leg4.rotateAngleY += -f10;
		leg5.rotateAngleY += f11;
		leg6.rotateAngleY += -f11;
		leg7.rotateAngleY += f12;
		leg8.rotateAngleY += -f12;
		leg1.rotateAngleZ += f13;
		leg2.rotateAngleZ += -f13;
		leg3.rotateAngleZ += f14;
		leg4.rotateAngleZ += -f14;
		leg5.rotateAngleZ += f15;
		leg6.rotateAngleZ += -f15;
		leg7.rotateAngleZ += f16;
		leg8.rotateAngleZ += -f16;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/*
	 * Copies the rotation angles of model1 to model2.
	 */
	private void copyRotationAngles(ModelRenderer model1, ModelRenderer model2)
	{
		model2.rotateAngleX = model1.rotateAngleX;
		model2.rotateAngleY = model1.rotateAngleY;
		model2.rotateAngleZ = model1.rotateAngleZ;
	}
	
	public ModelRenderer getHead()
	{
		return head;
				//SpiderCore.getConfig().usePlayerSkin ? playerHead : head;
	}

	public ModelRenderer getRightArm()
	{
		return armRight;
				//SpiderCore.getConfig().usePlayerSkin ? playerArmRight : armRight;
	}

	public ModelRenderer getBody()
	{
		return torso;
			//SpiderCore.getConfig().usePlayerSkin ? playerBody : torso;
	}
}