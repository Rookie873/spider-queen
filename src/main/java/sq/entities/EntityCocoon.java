package sq.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import radixcore.util.RadixLogic;
import radixcore.util.RadixMath;
import sq.enums.EnumCocoonType;
import sq.items.ItemCocoon;

/**
 * Cocoons are entities placed in the world by the player or placed in the world after an entity is struck by a web shot.
 */
public class EntityCocoon extends EntityCreature implements IEntityAdditionalSpawnData
{
	private static final DataParameter<Boolean> IS_EATEN = EntityDataManager.<Boolean>createKey(EntityCocoon.class, DataSerializers.BOOLEAN);
	
	private EnumCocoonType cocoonType;
	private int currentDamage;
	private int timeSinceHit;
	
	/* For spawning after game load */
	public EntityCocoon(World world)
	{
		super(world);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		tasks.addTask(0, new EntityAISwimming(this));
	}
	
	/* Base constructor */
	public EntityCocoon(World world, EnumCocoonType cocoonType)
	{
		this(world);
		this.cocoonType = cocoonType;
	}
	
	/*********************** VANILLA OVERRIDES ***************************/
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(IS_EATEN, false);
	}
	
	/* No old AI should run which would cause movement */
	@Override
	public boolean isAIDisabled()
	{
		return false;
	}
	
	/* Let the new AI system know this creature shouldn't move */
	@Override
	protected boolean isMovementBlocked()
	{
		return true;
	}
	
	/* Stay forever! */
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	/* Allow a collision */
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	/* Allow collisions to push the cocoon */
	@Override
	public boolean canBePushed()
	{
		return true;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Make the cocoon rock back and forth when it's struck
		if (timeSinceHit > 0)
		{
			timeSinceHit--;
		}

		if (currentDamage > 0 && ticksExisted % 5 == 0) //Have a short cooldown period using the ticksExisted field
		{
			currentDamage--;
		}

		//For endermen, we spawn portal particles around the cocoon
		if (cocoonType == EnumCocoonType.ENDERMAN && !getIsEaten())
		{
			worldObj.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * width, posY + 1 + rand.nextDouble() * 0.25D, posZ + rand.nextDouble() - 0.5D * width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
		}
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (!getIsEaten())
		{
			//Heal the player's health and food stats when eaten.
			player.heal(3);
			player.getFoodStats().addStats(4, 0.4f);

			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY + 2, posZ, motionX, motionY, motionZ);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY + 2, posZ, motionX, motionY, motionZ);

			setIsEaten(true);

			//Then calculate and drop string/eggs.
			if (!worldObj.isRemote)
			{
				final boolean doDropEgg = RadixLogic.getBooleanWithProbability(25);
				final int dropAmount = RadixMath.getNumberInRange(1, 2);

				int maxString = 5;
				
				switch (cocoonType)
				{
				case ENDERMAN:
				case QUEEN_BEE:
				case HUMAN: maxString = 20;
				}
				
				entityDropItem(new ItemStack(Items.STRING, RadixMath.getNumberInRange(2, maxString), 0), 0);

				if (doDropEgg)
				{
					//TODO
					//entityDropItem(new ItemStack(SpiderItems.SPIDER_EGG, dropAmount, 0), 0);
				}
			}
		}

		return true;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
	{
		final Entity entity = damageSource.getEntity();

		if (entity instanceof EntityPlayer)
		{
			//When punched by a player, increment the damage to make the cocoon rock back and forth.
			EntityPlayer player = (EntityPlayer)entity;
			
			//But first check to see if the attack was made with a sword while sneaking, if so, release the captured creature
			if (!worldObj.isRemote && 
					player.isSneaking() && 
					player.getHeldItemMainhand() != null 
					&& player.getHeldItemMainhand().getItem() instanceof ItemSword)
			{
				try
				{
					Class capturedClass = cocoonType.getCaptureClass();
					Entity newEntity = (Entity)capturedClass.getDeclaredConstructor(World.class).newInstance(worldObj);
					newEntity.setPosition(posX, posY, posZ);
					worldObj.spawnEntityInWorld(newEntity);
					setDead();
				}
				
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			timeSinceHit = 10;
			currentDamage += damage;
			setBeenAttacked();
			
			//Destroy the cocoon when damage is greater than 8.
			if (currentDamage > 8)
			{
				//When the cocoon has been eaten, destroy it with a puff of smoke.
				if (getIsEaten())
				{
					worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX - motionX * 2, posY - motionY * 2 + 1, posZ - motionZ * 2, motionX, motionY, motionZ);
				}

				if (!worldObj.isRemote)
				{
					if (!getIsEaten() && !player.capabilities.isCreativeMode) //When it's not eaten, drop the cocoon's item.
					{
						entityDropItem(ItemCocoon.getCocoonItemStack(cocoonType), 0.5F);	
					}
					
					//And destroy the cocoon.
					setDead();
				}
			}
		}

		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		setCocoonType(EnumCocoonType.getCocoonType(nbt.getInteger("cocoonType")));
		setIsEaten(nbt.getBoolean("isEaten"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("cocoonType", cocoonType.getId());
		nbt.setBoolean("isEaten", getIsEaten());
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(cocoonType.getId());
		buffer.writeBoolean(getIsEaten());
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		setCocoonType(EnumCocoonType.getCocoonType(buffer.readInt()));
		setIsEaten(buffer.readBoolean());
	}
	
	/************************************** DATA GETTERS/SETTERS *************************************/	
	public void setIsEaten(boolean value)
	{
		this.dataManager.set(IS_EATEN, value);
	}
	
	public boolean getIsEaten()
	{
		return this.dataManager.get(IS_EATEN);
	}
	
	public EnumCocoonType getCocoonType()
	{
		return cocoonType;
	}
	
	public void setCocoonType(EnumCocoonType value)
	{
		this.cocoonType = value;
		setHitboxSize();
	}
	
	public int getTimeSinceHit()
	{
		return timeSinceHit;
	}

	public int getCurrentDamage()
	{
		return currentDamage;
	}
	
	/************************************* CUSTOM METHODS **************************************/
	
	/* Ensures that we're the right size for a specific cocoon type. */
	public void setHitboxSize()
	{
		switch (cocoonType)
		{
		case GATHERER_BEE:
		case WARRIOR_BEE: 
		case QUEEN_BEE:
		case BLACK_ANT:
		case RED_ANT: setSize(0.75F, 0.75F); break;
		default:
			setSize(1.0F, 1.0F);
			break;
		}
	}
}
