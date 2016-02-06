package sq.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sq.core.SpiderCore;
import sq.entity.creature.EntityWebShot;
import sq.enums.EnumWebType;

/**
 * The web item allows the player to shoot a web shot when they right click with this item.
 */
public class ItemWeb extends Item
{
	private EnumWebType webType;
	
	public ItemWeb(EnumWebType type)
	{
		super();
		
		final String name = "web-" + type.getName();
		setWebType(type);
		setUnlocalizedName(name);
		setCreativeTab(SpiderCore.getCreativeTab());
		
		GameRegistry.registerItem(this, name);
	}
	
	private void setWebType(EnumWebType type)
	{
		webType = type;
	}
	
	public EnumWebType getWebType()
	{
		return webType;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		world.playSoundAtEntity(entityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		
		if (!world.isRemote)
		{
			if (!entityPlayer.capabilities.isCreativeMode)
			{
				itemStack.stackSize--;
			}

			final EntityWebShot web = new EntityWebShot(entityPlayer, webType);
			world.spawnEntityInWorld(web);
		}
		
		return itemStack;
	}
}
