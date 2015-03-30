package com.nabijaczleweli.fancymagicks.util

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper

import scala.util.Random

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

	def teleportTo(entity: EntityLivingBase, x: Double, y: Double, z: Double) { // Stolen from EntityEnderman, except Entity[LivingBase] and setPosition[AndUpdate]() for client -> server synchronization
		val prevX = entity.posX
		val prevY = entity.posY
		val prevZ = entity.posZ
		entity.posX = x
		entity.posY = y
		entity.posZ = z
		var teleported = false
		val floorX = MathHelper floor_double entity.posX
		var floorY = MathHelper floor_double entity.posY
		val floorZ = MathHelper floor_double entity.posZ

		if(entity.worldObj.blockExists(floorX, floorY, floorZ)) {
			var valid = false
			while(!valid && floorY > 0) {
				val block = entity.worldObj.getBlock(floorX, floorY - 1, floorZ)
				if(block.getMaterial.blocksMovement)
					valid = true
				else
					entity.posY -= 1
					floorY -= 1
			}
			if(valid) {
				entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ)
				if(entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty && !entity.worldObj.isAnyLiquid(entity.boundingBox))
					teleported = true
			}
		}

		if(!teleported)
			entity.setPositionAndUpdate(prevX, prevY, prevZ)
	}

	def teleportRandomly(entity: EntityLivingBase, rand: Random = new Random) { // Stolen from EntityEnderman
		val x = entity.posX + (rand.nextDouble() - .5D) * 64D
		val y = entity.posY + (rand.nextInt(64) - 32).toDouble
		val z = entity.posZ + (rand.nextDouble() - .5D) * 64D
		teleportTo(entity, x, y, z)
	}
}
