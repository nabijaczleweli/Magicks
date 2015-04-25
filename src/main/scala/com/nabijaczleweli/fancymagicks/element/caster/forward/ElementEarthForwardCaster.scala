package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementEarth}
import com.nabijaczleweli.fancymagicks.entity.EntityEarthBall
import net.minecraft.entity.Entity

class ElementEarthForwardCaster(who: Entity, elems: Seq[Element]) extends ElementProjectileForwardCaster(who, elems) {
	override val elementClass = ElementEarth.getClass

	override def entityClass = classOf[EntityEarthBall]
}
