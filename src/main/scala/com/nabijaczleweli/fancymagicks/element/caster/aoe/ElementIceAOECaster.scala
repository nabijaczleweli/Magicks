package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementIce}
import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import net.minecraft.entity.Entity

class ElementIceAOECaster(who: Entity, elems: Seq[Element]) extends ElementProjectileAOECaster(who, elems) {
	override val elementClass = ElementIce.getClass

	def doVisualEffect(x: Double, y: Double, z: Double) =
		EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, x, y, z)
}
