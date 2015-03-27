package com.nabijaczleweli.fancymagicks.util

import net.minecraft.entity.EntityLivingBase

object EntityUtil {
	def rayTraceCoords(from: EntityLivingBase, by: Double) = {
		val look = from getLook 1
		(from getPosition 1).addVector(look.xCoord * 10D, look.yCoord * 10D, look.zCoord * 10D)
	}

	def rayTrace(from: EntityLivingBase, by: Double) = {
		val look = from getLook 1
		val pos = from getPosition 1
		from.worldObj.func_147447_a(pos, pos.addVector(look.xCoord * 10D, look.yCoord * 10D, look.zCoord * 10D), false, false, false)
	}
}
