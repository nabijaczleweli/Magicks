package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.NoElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity

class NoElementAOECaster(who: Entity) extends NoElementCaster(who) {
	def this(who: Entity, elems: Seq[Element]) =
		this(who)

	override protected def entitiesToDamage =
		EntityUtil.entitiesInRadius[Entity](who, chargeup / 10D)
}
