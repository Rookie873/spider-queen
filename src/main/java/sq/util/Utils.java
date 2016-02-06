package sq.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.ChunkCoordIntPair;
import radixcore.util.RadixLogic;

/**
 * Defines various utility methods that have no other place in the mod's files.
 */
public final class Utils 
{
	private Utils()
	{
		
	}
	
	public static void spawnParticlesAroundEntityS(EnumParticleTypes type, Entity entityOrigin, int rate)
	{
		final Random rand = entityOrigin.worldObj.rand;

		for (int i = 0; i < rate; i++)
		{
			final float parX = (float) (entityOrigin.posX + rand.nextFloat() * entityOrigin.width * 2.0F - entityOrigin.width);
			final float parY = (float) (entityOrigin.posY + 0.5D + rand.nextFloat() * entityOrigin.height);
			final float parZ = (float) (entityOrigin.posZ + rand.nextFloat() * entityOrigin.width * 2.0F - entityOrigin.width);

			final float velX = (float) (rand.nextGaussian() * 0.02D);
			final float velY = (float) (rand.nextGaussian() * 0.02D);
			final float velZ = (float) (rand.nextGaussian() * 0.02D);

			S2APacketParticles packet = new S2APacketParticles(type, true, parX, parY, parZ, velX, velY, velZ, 0.0F, 0);

			for (int j = 0; j < entityOrigin.worldObj.playerEntities.size(); ++j)
			{
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityOrigin.worldObj.playerEntities.get(j);
				ChunkCoordIntPair chunkCoordinates = new ChunkCoordIntPair(entityPlayerMP.chunkCoordX, entityPlayerMP.chunkCoordZ);
				double deltaX = entityOrigin.posX - chunkCoordinates.chunkXPos;
				double deltaY = entityOrigin.posY - entityPlayerMP.chunkCoordY;
				double deltaZ = entityOrigin.posZ - chunkCoordinates.chunkZPos;
				double distanceSq = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

				if (distanceSq <= 256.0D)
				{
					entityPlayerMP.playerNetServerHandler.sendPacket(packet);
				}
			}
		}
	}
	
	public static void spawnParticlesAroundEntityC(EnumParticleTypes type, Entity entityOrigin, int rate)
	{
		final Random rand = entityOrigin.worldObj.rand;

		for (int i = 0; i < rate; i++)
		{
			final float parX = (float) (entityOrigin.posX + rand.nextFloat() * entityOrigin.width * 2.0F - entityOrigin.width);
			final float parY = (float) (entityOrigin.posY + 0.5D + rand.nextFloat() * entityOrigin.height);
			final float parZ = (float) (entityOrigin.posZ + rand.nextFloat() * entityOrigin.width * 2.0F - entityOrigin.width);

			final float velX = (float) (rand.nextGaussian() * 0.02D);
			final float velY = (float) (rand.nextGaussian() * 0.02D);
			final float velZ = (float) (rand.nextGaussian() * 0.02D);

			entityOrigin.worldObj.spawnParticle(type, parX, parY, parZ, velX, velY, velZ);
		}
	}
	
	public static int plusOrMinus(int number)
	{
		if (RadixLogic.getBooleanWithProbability(50))
		{
			return number * -1;
		}
		
		else
		{
			return number;
		}
	}
}
