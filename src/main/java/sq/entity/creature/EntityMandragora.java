package sq.entity.creature;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import radixcore.constant.Time;
import radixcore.util.BlockHelper;
import radixcore.util.RadixLogic;
import radixcore.util.RadixMath;
import sq.core.SpiderCore;
import sq.core.minecraft.ModItems;
import sq.entity.AbstractNewMob;

/**
 * The mandragora is a stationary aggressive creature that attacks the player with vines.
 */
public class EntityMandragora extends AbstractNewMob
{
	private int vineTimer;

	public EntityMandragora(World world) 
	{
		super(world, "mand");
	}

	@Override
	public float getMobMaxHealth() 
	{
		return 20.0F;
	}

	@Override
	public float getHitDamage() 
	{
		return 0.0F;
	}

	@Override
	public double getMoveSpeed() 
	{
		return 0.0F;
	}

	@Override
	public boolean isPassive() 
	{
		return false;
	}

	@Override
	public String getCommandSenderName()
	{
		return "Mandragora";
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) 
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger("isFriendly", dataWatcher.getWatchableObjectInt(12));
		nbt.setInteger("vineTimer", vineTimer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) 
	{
		super.readEntityFromNBT(nbt);
		dataWatcher.updateObject(12, nbt.getInteger("isFriendly"));
		vineTimer = nbt.getInteger("vineTimer");
	}

	@Override
	public boolean getCanSpawnHere()
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		
		return BlockHelper.getBlock(worldObj, i, j - 1, k) == Blocks.grass;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!SpiderCore.getConfig().enableMandragora)
		{
			setDead();
		}
		
		if (vineTimer <= 0)
		{
			Entity entityToAttack = this.getAttackTarget();
			
			if (entityToAttack != null)
			{
				//Only allow vine attacks if the target is less than 10 blocks away.
				if (RadixMath.getDistanceToEntity(this, entityToAttack) > 10.0D)
				{
					return;
				}
				
				vineTimer = Time.SECOND * 1;

				double d = entityToAttack.posX - posX + entityToAttack.motionX * 15D;
				double d1 = entityToAttack.posZ - posZ + entityToAttack.motionZ * 15D;

				if (attackTime == 0)
				{
					//Shoot the vines like an arrow. The vine entity will handle moving up and down periodically.
					EntityVines vines = new EntityVines(worldObj, this);
					vines.setFriendly(getIsFriendly());
					vines.posY += 0.4D;
					double d2 = (entityToAttack.posY) - 0.20000000298023224D - vines.posY;
					float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
					worldObj.playSoundAtEntity(this, "sq:mand.vine", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
					worldObj.spawnEntityInWorld(vines);
					vines.setArrowHeading(d, d2 + f1, d1, 0.5F, 8F);
					attackTime = 20 - rand.nextInt(5);
				}
				rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
				hasAttacked = true;
			}
		}

		vineTimer--;
	}

	@Override
	protected boolean isValidLightLevel() 
	{
		//Make sure we can spawn in a lit area.
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);

		return this.worldObj.getBlockLightValue(i, j, k) >= 7;
	}

	@Override
	protected void dropFewItems(boolean hitByPlayer, int lootLevel)
	{
		if (hitByPlayer && RadixLogic.getBooleanWithProbability(45))
		{
			//Randomly drops mandragora seeds.
			this.dropItem(ModItems.mandragoraSeeds, 1);
		}
	}

	public boolean getIsFriendly()
	{
		return dataWatcher.getWatchableObjectInt(12) != 0;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(12, 0);
	}
}
