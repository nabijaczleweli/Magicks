package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.ElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementBeam}
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.nbt.NBTTagCompound

class ElementBeamForwardCaster(who: Entity, elems: Seq[Element]) extends ElementCaster {
	 private lazy val force = elems count {classOf[ElementBeam] isAssignableFrom _.getClass}
	 private lazy val damage = ElementalDamageSource damageForward elems

	 var timeLeft: Int = _

	 override def start() =
		 timeLeft = force * 50

	 override def continue() {
		 timeLeft = 0 max timeLeft - 1
		 if(timeLeft > 0)
			 (0D until 500D by .25D flatMap {d =>
				 val coords = EntityUtil.rayTraceCoords(who.asInstanceOf[EntityLivingBase], d)
				 EntityUtil.entitiesAround[EntityLivingBase](who.worldObj, (coords.xCoord, coords.yCoord, coords.zCoord), 1)
			 }).toSet foreach {e: EntityLivingBase => ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, damage)}
	 }

	 override def end() =
		 timeLeft = 0

	 override def loadNBTData(tag: NBTTagCompound) =
		 timeLeft = tag getInteger s"${NAMESPACED_PREFIX}timeLeft"

	 override def saveNBTData(tag: NBTTagCompound) =
		 tag.setInteger(s"${NAMESPACED_PREFIX}timeLeft", timeLeft)
 }
