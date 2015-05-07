package sqr.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import sqr.core.SQR;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockJack extends Block
{
	private static IIcon faceIcon;
	private static IIcon sideIcon;
	private static IIcon topIcon;
	
	public BlockJack() 
	{
		super(Material.ground);
		
		final String name = "jack";
		setBlockName(name);
		setBlockTextureName("sqr:" + name);
		setCreativeTab(SQR.getCreativeTab());
		
		GameRegistry.registerBlock(this, name);
	}

	@Override
	public IIcon getIcon(int side, int meta) 
	{
        return side == 1 ? this.topIcon : (side == 0 ? this.topIcon : (meta == 2 && side == 2 ? this.faceIcon : (meta == 3 && side == 5 ? this.faceIcon : (meta == 0 && side == 3 ? this.faceIcon : (meta == 1 && side == 4 ? this.faceIcon : this.sideIcon)))));
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) 
	{
		faceIcon = iconRegister.registerIcon("sqr:jack-face");
		sideIcon = iconRegister.registerIcon("sqr:jack-side");
		topIcon = iconRegister.registerIcon("sqr:jack-top");
	}
	
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int meta = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(posX, posY, posZ, meta, 2);
    }
}
