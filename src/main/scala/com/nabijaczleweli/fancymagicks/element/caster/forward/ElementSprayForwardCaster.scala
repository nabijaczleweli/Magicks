package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity

class ElementSprayForwardCaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	private lazy val force = elems count elems.head.equals

	override protected def cast() {
		for(e <- EntityUtil.entitiesInRadius[Entity](who, force * 2.5D)) {
			ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)

			val noElementCaster = ElementForwardCaster(who, Nil)
			noElementCaster.start()
			for(_ <- 0 until force * 30)
				noElementCaster.continue()
			noElementCaster.end()
		}
	}
}
