package sq.entity.creature;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import radixcore.util.BlockHelper;
import radixcore.util.RadixLogic;
import sq.blocks.BlockWebFull;
import sq.blocks.BlockWebGround;
import sq.blocks.BlockWebSide;
import sq.core.minecraft.ModAchievements;
import sq.core.minecraft.ModBlocks;
import sq.enums.EnumCocoonType;
import sq.enums.EnumSide;
import sq.enums.EnumWebType;

/**
 * The web shot is the projectile shot when the player right-clicks while holding web.
 */
public class EntityWebShot extends Entity implements IProjectile, IEntityAdditionalSpawnData
{
	private int				ticksInAir;
	private EnumWebType		type;
	private boolean			doBlockSpawn;

	public EntityLivingBase	shooter;
	public double			accelerationX;
	public double			accelerationY;
	public double			accelerationZ;

	public EntityWebShot(World worldObj)
	{
		super(worldObj);
		type = EnumWebType.NORMAL;
	}

	public EntityWebShot(EntityPlayer player)
	{
		this(player.worldObj);

		final Vec3 vec = player.getLookVec();

		shooter = player;
		setPosition(player.posX, player.posY + 1.4F, player.posZ);
		accelerationX = vec.xCoord * 1.5;
		accelerationY = vec.yCoord * 1.5;
		accelerationZ = vec.zCoord * 1.5;

		motionX = accelerationX;
		motionY = accelerationY;
		motionZ = accelerationZ;

		doBlockSpawn = true;
	}

	public EntityWebShot(EntityPlayer player, EnumWebType type)
	{
		this(player);
		this.type = type;
	}

	public EntityWebShot(EntityLivingBase shooter, EntityLivingBase target, float speed)
	{
		this(shooter.worldObj);
		this.shooter = shooter;
		this.type = EnumWebType.NORMAL;

		renderDistanceWeight = 10.0D;

		posY = shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D;
		final double deltaX = target.posX - shooter.posX;
		final double deltaY = target.boundingBox.minY + target.height / 3.0F - posY;
		final double deltaZ = target.posZ - shooter.posZ;
		final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

		if (distanceXZ >= 1.0E-7D)
		{
			final float rotationYaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
			final float rotationPitch = (float) -(Math.atan2(deltaY, distanceXZ) * 180.0D / Math.PI);
			final double modX = deltaX / distanceXZ;
			final double modY = deltaZ / distanceXZ;

			setLocationAndAngles(shooter.posX + modX, posY, shooter.posZ + modY, rotationYaw, rotationPitch);
			yOffset = 0.0F;

			final float modDeltaY = (float) distanceXZ * 0.2F;
			setThrowableHeading(deltaX, deltaY - modDeltaY, deltaZ, speed, 16F);
		}
	}

	@Override
	protected void entityInit()
	{
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		double weightedLength = boundingBox.getAverageEdgeLength() * 4.0D;
		weightedLength *= 64.0D;
		return distance < weightedLength * weightedLength;
	}

	@Override
	public void onUpdate()
	{
		//Check for all scenarios wherein the web shot needs to disappear.
		if (!worldObj.isRemote && (ticksInAir > 150 || shooter != null && shooter.isDead || !worldObj.blockExists((int) posX, (int) posY, (int) posZ)))
		{
			setDead();
		}

		else
		{
			super.onUpdate();
			updateCollision();
			updateMotion();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("type", type.getId());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		type = EnumWebType.byId(nbt.getInteger("type"));
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public float getCollisionBorderSize()
	{
		return 1.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damageAmount)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}

	@Override
	public float getBrightness(float unknown)
	{
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float unknown)
	{
		return 15728880;
	}

	@Override
	public void setThrowableHeading(double posX, double posY, double posZ, float speed, float unknown)
	{
		final float distanceXYZ = MathHelper.sqrt_double(posX * posX + posY * posY + posZ * posZ);
		posX /= distanceXYZ;
		posY /= distanceXYZ;
		posZ /= distanceXYZ;

		posX += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * unknown;
		posY += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * unknown;
		posZ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * unknown;

		posX *= speed;
		posY *= speed;
		posZ *= speed;

		motionX = posX;
		motionZ = posZ;

		final float distanceXZ = MathHelper.sqrt_double(posX * posX + posZ * posZ);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(posX, posZ) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(posY, distanceXZ) * 180.0D / Math.PI);
	}

	@Override
	public void setDead()
	{
		super.setDead();
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		buffer.writeInt(type.getId());
	}

	@Override
	public void readSpawnData(ByteBuf buffer) 
	{
		type = EnumWebType.byId(buffer.readInt());
	}

	public EnumWebType getType()
	{
		return type;
	}

	private void updateCollision()
	{
		Vec3 currentVector = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 nextVector = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition collisionPosition = worldObj.rayTraceBlocks(currentVector, nextVector);
		currentVector = Vec3.createVectorHelper(posX, posY, posZ);
		nextVector = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

		if (collisionPosition != null)
		{
			nextVector = Vec3.createVectorHelper(collisionPosition.hitVec.xCoord, collisionPosition.hitVec.yCoord, collisionPosition.hitVec.zCoord);
		}

		Entity collidedEntity = null;
		final List entitiesInAABB = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double lastDistance = 0.0D;

		for (int counter = 0; counter < entitiesInAABB.size(); ++counter)
		{
			final Entity entityInList = (Entity) entitiesInAABB.get(counter);

			if (entityInList.canBeCollidedWith() && (!entityInList.isEntityEqual(shooter) || ticksInAir >= 25))
			{
				final AxisAlignedBB AABB = entityInList.boundingBox.expand(0.3D, 0.3D, 0.3D);
				final MovingObjectPosition entityCollisionPosition = AABB.calculateIntercept(currentVector, nextVector);

				if (entityCollisionPosition != null)
				{
					final double thisDistance = currentVector.distanceTo(entityCollisionPosition.hitVec);

					if (thisDistance < lastDistance || lastDistance == 0.0D)
					{
						collidedEntity = entityInList;
						lastDistance = thisDistance;
					}
				}
			}
		}

		if (collidedEntity != null)
		{
			collisionPosition = new MovingObjectPosition(collidedEntity);
		}

		if (collisionPosition != null)
		{
			//When we have a collision position, get the block before passing it to onImpact.
			Block block = BlockHelper.getBlock(worldObj, collisionPosition.blockX, collisionPosition.blockY, collisionPosition.blockZ);

			if (block.getMaterial() == Material.plants) //Ignore all plants.
			{
				return;
			}

			else 
			{
				//Also ignore mini ghasts, as the player could accidentally hit it when shooting web while mounted on the ghast.
				if (collisionPosition.entityHit instanceof EntityMiniGhast)
				{
					return;
				}

				else //Valid impact point.
				{
					onImpact(collisionPosition);
				}
			}
		}
	}

	private void updateMotion()
	{
		ticksInAir++;

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		final float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) + 90.0F;

		for (rotationPitch = (float) (Math.atan2(f1, motionY) * 180.0D / Math.PI) - 90.0F; rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
		{
			;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F)
		{
			prevRotationPitch += 360.0F;
		}

		while (rotationYaw - prevRotationYaw < -180.0F)
		{
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F)
		{
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float motionFactor = 0.95F;

		if (isInWater())
		{
			setDead();
		}

		motionX += accelerationX;
		motionY += accelerationY;
		motionZ += accelerationZ;
		motionX *= motionFactor;
		motionY *= motionFactor;
		motionZ *= motionFactor;

		setPosition(posX, posY, posZ);
	}

	private void onImpact(MovingObjectPosition impactPoint)
	{
		if (!worldObj.isRemote)
		{
			skewMotion();

			if (impactPoint.entityHit instanceof EntityLivingBase)
			{
				//Do not allow a block to spawn as we've already "used" the slinger.
				doBlockSpawn = false;

				//Get the cocoon type of the creature, if it exists, and cause it to jump back by attacking it with 0 points.
				final EnumCocoonType cocoonType = EnumCocoonType.getCocoonType(impactPoint.entityHit);
				final EntityLivingBase entityHit = (EntityLivingBase) impactPoint.entityHit;
				entityHit.attackEntityFrom(DamageSource.causeMobDamage(shooter), 0.0F);

				//Only normal web can cocoon, and only try to cocoon if we know the cocoon type.
				if (type == EnumWebType.NORMAL && cocoonType != null)
				{
					//Calculate the catch chance based on a base chance and the mob's health.
					final int intHealth = (int) entityHit.getHealth();
					final int catchChance = (int) (cocoonType.getCatchChance() + (entityHit.getMaxHealth() - intHealth) * 2);

					if (RadixLogic.getBooleanWithProbability(catchChance))
					{
						//If we passed the catch test, spawn the new cocoon in the world at the entity's place.
						EntityCocoon entityCocoon = new EntityCocoon(worldObj, cocoonType);
						entityCocoon.setLocationAndAngles(entityHit.posX, entityHit.posY, entityHit.posZ, entityHit.rotationYaw, entityHit.rotationPitch);
						worldObj.spawnEntityInWorld(entityCocoon);
						entityHit.setDead();

						//Pass a living death event into Forge as this technically kills the creature. This runs the code
						//that decreases reputation for the creature that was cocooned.
						if (shooter instanceof EntityPlayer)
						{
							EntityPlayer player = (EntityPlayer)shooter;
							MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entityHit, DamageSource.causePlayerDamage(player)));
							player.triggerAchievement(ModAchievements.catchMob);
						}
					}

					setDead();
				}
			}

			else if (doBlockSpawn)
			{
				//Figure out what we hit and where, and which side.
				final Block blockHit = BlockHelper.getBlock(worldObj, impactPoint.blockX, impactPoint.blockY, impactPoint.blockZ);
				int impactX = impactPoint.blockX;
				int impactY = impactPoint.blockY;
				int impactZ = impactPoint.blockZ;

				//Calculate the side that the web will need to be on.
				EnumSide sideHit = EnumSide.byId(impactPoint.sideHit);
				int xMov = sideHit == EnumSide.NORTH ? -1 : sideHit == EnumSide.SOUTH ? 1 : 0;
				int yMov = sideHit == EnumSide.BOTTOM ? -1 : sideHit == EnumSide.TOP ? 1 : 0;
				int zMov = sideHit == EnumSide.EAST ? -1 : sideHit == EnumSide.WEST ? 1 : 0;

				Block blockPlaced = null;

				//When hitting a 'partial' web, the web will be made full.
				if (blockHit instanceof BlockWebGround || blockHit instanceof BlockWebSide)
				{
					blockPlaced = ModBlocks.webFull;

					if (blockHit instanceof BlockWebGround)
					{
						yMov -= 1;
					}

					else if (blockHit instanceof BlockWebSide)
					{
						//Set to zero to replace the impact block.
						xMov = 0;
						zMov = 0;

						if (sideHit == EnumSide.BOTTOM)
						{
							yMov += 1;
						}
					}
				}

				else
				{
					if (sideHit == EnumSide.TOP)
					{
						blockPlaced = ModBlocks.webGround;
					}

					else
					{
						blockPlaced = ModBlocks.webSide;
					}
				}

				int meta = 0;

				//Calculate meta for the side web.
				if (blockPlaced instanceof BlockWebSide)
				{
					switch (sideHit)
					{
					case EAST: meta = 1; break;
					case WEST: meta = 4; break;
					case NORTH: meta = 8; break;
					case SOUTH: meta = 2; break;
					default:
						break;
					}
				}

				//Change the actual block to the poison web variant.
				if (type == EnumWebType.POISON)
				{
					blockPlaced = ModBlocks.getPoisonWebVariant(blockPlaced);
				}

				//Set the web at the impact point.
				Block blockToSet = BlockHelper.getBlock(worldObj, impactX + xMov, impactY + yMov, impactZ + zMov);

				if (blockToSet == Blocks.air || blockToSet instanceof BlockWebFull) //Prevent overwriting terrain.
				{
					BlockHelper.setBlock(worldObj, impactX + xMov, impactY + yMov, impactZ + zMov, blockPlaced, meta);
				}

				setDead();
			}
		}
	}

	private void skewMotion()
	{
		final Random rand = new Random();

		if (rand.nextInt(2) == 0)
		{
			motionX = motionX * -0.2F;
		}

		if (rand.nextInt(2) == 0)
		{
			motionY = motionY * -0.2F;
		}

		if (rand.nextInt(2) == 0)
		{
			motionZ = motionZ * -0.2F;
		}
	}
}