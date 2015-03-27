package com.nabijaczleweli.fancymagicks.potion

import com.nabijaczleweli.fancymagicks.element.Element
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.common.config.Configuration

import scala.collection.JavaConversions._
import scala.collection.mutable.{Map => mMap, HashMap => mHashMap}

class PotionImmunityAura(val element: Element) extends Potion(false, element.colour) {
	override def performEffect(entity: EntityLivingBase, amplifier: Int) = {
		val range = PotionImmunityAura.blocksPerLevel * amplifier
		entity.worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], entity.boundingBox.expand(range, range, range)) map {_.asInstanceOf[EntityLivingBase]} filterNot {_ == entity} foreach {Potion applyEffect this}
	}
}

object PotionImmunityAura extends IConfigurable {
	var blocksPerLevel = 2.5F

	private val elementToAura: mMap[Element, PotionImmunityAura] = new mHashMap

	def apply(element: Element) =
		elementToAura.getOrElseUpdate(element, new PotionImmunityAura(element))

	override def configure(config: Configuration) {
		blocksPerLevel = config.getFloat("blocksPerImmunityAuraLevel", "potion", blocksPerLevel, .5F, Float.MaxValue, "Radius at which the elemental aura will affect other entities")
	}
}
