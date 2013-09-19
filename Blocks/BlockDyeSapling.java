/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;

public class BlockDyeSapling extends BlockSapling {

	private Icon icon;

	private static Random r = new Random();

	public BlockDyeSapling(int par1) {
		super(par1);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.soundGrassFootstep);
	}

	@Override
	public void growTree(World world, int x, int y, int z, Random r) {
		this.growTree(world, x, y, z, this.getGrowthHeight());
	}

	private int getGrowthHeight() {
		return 5+r.nextInt(3);
	}

	public void growTree(World world, int x, int y, int z, int h)
	{
		if (world.isRemote)
			return;
		int meta = world.getBlockMetadata(x, y, z);
		int log = r.nextInt(4);
		int w = 2;
		for (int i = 0; i < h; i++) {
			world.setBlock(x, y+i, z, Block.wood.blockID, log, 3);
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-3, z+j))
					world.setBlock(x+i, y+h-3, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-2, z+j))
					world.setBlock(x+i, y+h-2, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-1, z+j))
					world.setBlock(x+i, y+h-1, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i*j == 0 && ReikaWorldHelper.softBlocks(world, x+i, y+h, z+j))
					world.setBlock(x+i, y+h, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int p6, float a, float b, float c) {
		ItemStack is = ep.getCurrentEquippedItem();
		if (is == null)
			return false;
		if (!ReikaItemHelper.matchStacks(is, ReikaDyeHelper.WHITE.getStackOf()))
			return false;
		this.growTree(world, x, y, z, ep.isSneaking() ? 7 : this.getGrowthHeight());
		return true;
	}

	@Override
	public Icon getIcon(int par1, int par2)
	{
		return icon;
	}

	@Override
	public void registerIcons(IconRegister ico)
	{
		icon = ico.registerIcon("DyeTrees:sapling");
	}

	@Override
	public int getRenderColor(int dmg)
	{
		return ReikaDyeHelper.dyes[dmg].getColor();
	}

	@Override
	public int colorMultiplier(IBlockAccess iba, int x, int y, int z)
	{
		int dmg = iba.getBlockMetadata(x, y, z);
		return ReikaDyeHelper.dyes[dmg].getJavaColor().brighter().getRGB();
	}

}