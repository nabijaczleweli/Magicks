package com.nabijaczleweli.fancymagicks.tileentity

import net.minecraft.tileentity.TileEntity

import scala.util.Random

class TileEntityShield extends TileEntity {
	var x0 = TileEntityShield.rand.nextFloat()
	var x1 = TileEntityShield.rand.nextFloat()
	var y0 = TileEntityShield.rand.nextFloat()
	var y1 = TileEntityShield.rand.nextFloat()
}

object TileEntityShield {
	val rand = new Random
}
