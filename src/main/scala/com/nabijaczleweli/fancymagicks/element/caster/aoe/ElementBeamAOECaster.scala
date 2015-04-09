package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.ElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementBeam}
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound

class ElementBeamAOECaster(who: Entity, elems: Seq[Element]) extends ElementCaster {
	 private lazy val force = elems count {classOf[ElementBeam] isAssignableFrom _.getClass}

	 var cast: Boolean = _

	 override def start() =
		 cast = false

	 override def continue() =
		 if(!cast)
			 for(e <- EntityUtil.entitiesInRadius[Entity](who, force * 2.5D))
				 ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)

	 override def end() =
		 cast = true

	 override def loadNBTData(tag: NBTTagCompound) =
		 cast = tag getBoolean s"${NAMESPACED_PREFIX}cast"

	 override def saveNBTData(tag: NBTTagCompound) =
		 tag.setBoolean(s"${NAMESPACED_PREFIX}cast", cast)
 }
