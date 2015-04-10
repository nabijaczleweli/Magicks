package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.NoElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity

class NoElementForwardCaster(who: Entity) extends NoElementCaster(who) {
	def this(who: Entity, elems: Seq[Element]) =
		this(who)

	override protected def entitiesToDamage =
		EntityUtil.filterForFrustrum(EntityUtil.entitiesInRadius[Entity](who, chargeup / 10D), EntityUtil frustrumFor who)
}
