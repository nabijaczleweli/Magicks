package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementIce}
import com.nabijaczleweli.fancymagicks.entity.EntityIceShard
import net.minecraft.entity.Entity

class ElementIceForwardCaster(who: Entity, elems: Seq[Element]) extends ElementProjectileForwardCaster(who, elems) {
	 override val elementClass = ElementIce.getClass

	 override def entityClass = classOf[EntityIceShard]
 }
