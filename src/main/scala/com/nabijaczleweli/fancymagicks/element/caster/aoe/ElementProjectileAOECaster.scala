package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementProjectile}
import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.util.{IConfigurable, EntityUtil}
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import net.minecraft.entity.Entity
import net.minecraftforge.common.config.Configuration

class ElementProjectileAOECaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	private lazy val force = elems count {classOf[ElementProjectile] isAssignableFrom _.getClass}

	override protected def cast() {
		for(e <- EntityUtil.entitiesInRadius[Entity](who, force * ElementProjectileAOECaster.rangeForceMul))
			ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)

		val baseY = who.posY - who.height
		for(outer <- 1D until force * ElementProjectileAOECaster.rangeForceMul by 1D; i <- 0 until ElementProjectileAOECaster.dissections) {
			val modT = i * math.Pi * 2 / ElementProjectileAOECaster.dissections
			val modX = outer * (math cos modT)
			val modY = outer * (math sin modT)
			EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, who.posX + modX, baseY, who.posZ + modY)
			EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, who.posX + modX, baseY, who.posZ - modY)
			EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, who.posX - modX, baseY, who.posZ + modY)
			EntityUtil dispachSimpleSpawn SimpleEntitySpawnData(classOf[EntityAOEIceSpike], who.worldObj, who.posX - modX, baseY, who.posZ - modY)
		}
	}
}

object ElementProjectileAOECaster extends IConfigurable {
	var dissections = 20
	var rangeForceMul = 2.5D

	override def configure(config: Configuration) {
		dissections = config.getInt("AOEProjectileCircleDissections", "combat", dissections, 1, Int.MaxValue, "Amount of spikes per circle in AOE ice spike casting")
		rangeForceMul = config.getFloat("AOEProjectileRangeForceMultiplier", "combat", rangeForceMul.toFloat, 1F, Float.MaxValue, "Amount of circles per projectile element in AOE projectile element casting")
	}
}
