package com.nabijaczleweli.fancymagicks.element.caster

import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3

abstract class NoElementCaster(who: Entity) extends ElementCaster {
	protected var chargeup: Int = _

	protected def entitiesToDamage: Seq[Entity]

	override def start() =
		chargeup = 0

	override def continue() =
		chargeup = 250 min chargeup + 1

	override def end() {
		val casterPosVec = Vec3.createVectorHelper(who.posX, who.posY, who.posZ)
		for(e <- entitiesToDamage) {
			val direction = (Vec3.createVectorHelper(e.posX, e.posY, e.posZ) subtract casterPosVec).normalize
			val mul = -(chargeup / 50D)
			EntityUtil.dispatchVelocityChange(e, direction.xCoord * mul, direction.yCoord * mul, direction.zCoord * mul)
		}
		chargeup = 250
	}

	override def loadNBTData(tag: NBTTagCompound) =
		chargeup = tag getInteger s"${NAMESPACED_PREFIX}chargeupTime"

	override def saveNBTData(tag: NBTTagCompound) =
		tag.setInteger(s"${NAMESPACED_PREFIX}chargeupTime", chargeup)
}
