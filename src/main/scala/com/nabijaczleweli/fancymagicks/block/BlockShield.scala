package com.nabijaczleweli.fancymagicks.block

import com.nabijaczleweli.fancymagicks.reference.{Container, Reference}
import com.nabijaczleweli.fancymagicks.tileentity.TileEntityShield
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.world.{IBlockAccess, World}

object BlockShield extends Block(Container.materialShield) with ITileEntityProvider {
	setBlockName(s"${Reference.NAMESPACED_PREFIX}shield")
	setResistance(Float.MaxValue)
	setLightOpacity(0)

	override def shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
		false

	override def createNewTileEntity(world: World, meta: Int) =
		new TileEntityShield
}
