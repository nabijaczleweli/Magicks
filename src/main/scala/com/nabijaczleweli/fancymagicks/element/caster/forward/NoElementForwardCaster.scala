package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3

class NoElementForwardCaster(who: Entity) extends ElementCaster {
	 def this(who: Entity, elems: Seq[Element]) =
		 this(who)

	 var chargeup: Int = _

	 override def start() =
		 chargeup = 0

	 override def continue() =
		 chargeup = math.min(chargeup + 1, 250)

	 override def end() {
		 val casterPosVec = Vec3.createVectorHelper(who.posX, who.posY, who.posZ)
		 for(e <- EntityUtil.filterForFrustrum(EntityUtil.entitiesInRadius[Entity](who, chargeup / 10D), EntityUtil frustrumFor who)) {
			 val direction = (Vec3.createVectorHelper(e.posX, e.posY, e.posZ) subtract casterPosVec).normalize
			 val mul = -(chargeup / 50D)
			 EntityUtil.dispatchVelocityChange(e, direction.xCoord * mul, direction.yCoord * mul, direction.zCoord * mul)
		 }
	 }

	 override def loadNBTData(tag: NBTTagCompound) =
		 chargeup = tag getInteger s"${NAMESPACED_PREFIX}chargeupTime"

	 override def saveNBTData(tag: NBTTagCompound) =
		 tag.setInteger(s"${NAMESPACED_PREFIX}chargeupTime", chargeup)
 }
