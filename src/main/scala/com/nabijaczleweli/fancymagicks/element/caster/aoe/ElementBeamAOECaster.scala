package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementBeam}
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity

class ElementBeamAOECaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	 private lazy val force = elems count {classOf[ElementBeam] isAssignableFrom _.getClass}

	override protected def cast() =
		for(e <- EntityUtil.entitiesInRadius[Entity](who, force * 2.5D))
			ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)
 }
