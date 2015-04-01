package com.nabijaczleweli.fancymagicks.potion

import com.nabijaczleweli.fancymagicks.element.Element
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IConfigurable}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.{Potion => mPotion}
import net.minecraft.util.StatCollector
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable.{Map => mMap, HashMap => mHashMap}

class PotionImmunityAura(val element: Element) extends Potion(false, element.colour) {
	private lazy val elementName = Element name element

	setIconIndex(mPotion.fireResistance.getStatusIconIndex)

	override def performEffect(entity: EntityLivingBase, amplifier: Int) =
		EntityUtil.entitiesInRadius[EntityLivingBase](entity, PotionImmunityAura.blocksPerLevel * amplifier) foreach {Potion applyEffect this}

	override def getName =
		StatCollector.translateToLocalFormatted(s"potion.${Reference.NAMESPACED_PREFIX}auraImmunity", elementName)
}

object PotionImmunityAura extends IConfigurable {
	var blocksPerLevel = 2.5F

	private val elementToAura: mMap[Element, PotionImmunityAura] = new mHashMap

	def apply(element: Element) =
		elementToAura.getOrElseUpdate(element, new PotionImmunityAura(element))

	override def configure(config: Configuration) {
		blocksPerLevel = config.getFloat("blocksPerImmunityAuraLevel", "potion", blocksPerLevel, .5F, Float.MaxValue, "Radius at which the immunizing elemental aura will affect other entities")
	}
}
