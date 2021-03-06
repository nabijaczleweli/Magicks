package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IConfigurable}
import net.minecraft.entity.Entity
import net.minecraftforge.common.config.Configuration

abstract class ElementProjectileAOECaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	private lazy val force = elems count {elementClass isAssignableFrom _.getClass}

	def elementClass: Class[_ <: Element]
	/** World is assumed to be `who.worldObj`*/
	def doVisualEffect(x: Double, y: Double, z: Double): Unit

	override protected def cast() {
		for(e <- EntityUtil.entitiesInRadius[Entity](who, force * ElementProjectileAOECaster.rangeForceMul))
			ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ElementalDamageSource damageAOE elems)

		val baseY = who.posY - who.height
		for(outer <- 1D until force * ElementProjectileAOECaster.rangeForceMul by 1D; i <- 0 until ElementProjectileAOECaster.dissections) {
			val modT = i * math.Pi * 2 / ElementProjectileAOECaster.dissections
			val modX = outer * (math cos modT)
			val modY = outer * (math sin modT)
			doVisualEffect(who.posX + modX, baseY, who.posZ + modY)
			doVisualEffect(who.posX + modX, baseY, who.posZ - modY)
			doVisualEffect(who.posX - modX, baseY, who.posZ + modY)
			doVisualEffect(who.posX - modX, baseY, who.posZ - modY)
		}
	}
}

object ElementProjectileAOECaster extends IConfigurable {
	var dissections = 20
	var rangeForceMul = 2.5D

	override def configure(config: Configuration) {
		dissections = config.getInt("Dissections", category"combat:element casting:aoe:projectile", dissections, 1, Int.MaxValue, "Amount of circle dissections, along which visual effects will be shown")
		rangeForceMul = config.getFloat("Range-Force multiplier", category"combat:element casting:aoe:projectile", rangeForceMul.toFloat, 1F, Float.MaxValue, "Amount of circles per projectile's element") // Better desc?
	}
}
