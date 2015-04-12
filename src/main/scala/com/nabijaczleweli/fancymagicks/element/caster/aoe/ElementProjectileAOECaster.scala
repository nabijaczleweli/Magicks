package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementProjectile}
import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import net.minecraft.entity.Entity

class ElementProjectileAOECaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	private lazy val force = elems count {classOf[ElementProjectile] isAssignableFrom _.getClass}

	override protected def cast() {
		for(e <- EntityUtil.entitiesInRadius[Entity](who, force * 2.5D))
			ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)
		EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, who.posX, who.posY, who.posZ)
	}
}
