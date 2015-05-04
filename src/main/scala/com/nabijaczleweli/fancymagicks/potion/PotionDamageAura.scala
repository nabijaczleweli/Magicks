package com.nabijaczleweli.fancymagicks.potion

import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IConfigurable}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.{Potion => mPotion}
import net.minecraft.util.{DamageSource, StatCollector}
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable.{HashMap => mHashMap, Map => mMap}
import scala.util.Random

class PotionDamageAura(val element: Element) extends Potion(false, element.colour) {
	private lazy val elementName = Element name element

	setIconIndex(mPotion.fireResistance.getStatusIconIndex)

	override def performEffect(entity: EntityLivingBase, amplifier: Int) = {
		def rsign = if(PotionDamageAura.rand.nextBoolean()) 1 else -1
		def roffset = PotionDamageAura.rand.nextDouble() * rsign
		def possibility(bits: Int) = {
			var b = false
			for(i <- 0 until bits if !b)
				b = PotionDamageAura.rand.nextBoolean()
			b
		}

		val range = PotionDamageAura.blocksPerLevel * amplifier
		for(r <- 0F to range by PotionDamageAura.rand.nextFloat() if possibility(3)) {
			def inoffset = roffset + (r * rsign)
			for(i <- 0 until PotionDamageAura.particlesPerBunch if possibility(2))
				entity.worldObj.spawnParticle("mobSpellAmbient", entity.posX + inoffset, entity.posY + inoffset, entity.posZ + inoffset, (getLiquidColor >> 16) & 0xFF, (getLiquidColor >> 8) & 0xFF, getLiquidColor & 0xFF)
		}

		EntityUtil.entitiesInRadius[EntityLivingBase](entity, range) foreach {_.attackEntityFrom(DamageSource.magic, 5)}
	}

	override def getName =
		StatCollector.translateToLocalFormatted(s"potion.${Reference.NAMESPACED_PREFIX}auraDamage", elementName)
}

object PotionDamageAura extends IConfigurable {
	var blocksPerLevel = 2.5F
	var particlesPerBunch = 5
	var damage = 5F

	private val elementToAura: mMap[Element, PotionDamageAura] = new mHashMap
	private val rand = new Random

	def apply(element: Element) =
		elementToAura.getOrElseUpdate(element, new PotionDamageAura(element))

	override def configure(config: Configuration) {
		blocksPerLevel = config.getFloat("Radius per aura's level", category"potion:combat:damage aura", blocksPerLevel, .5F, Float.MaxValue, "Radius at which the damaging elemental aura will affect other entities")
		particlesPerBunch = config.getInt("Particles per spawnpoint", category"potion:combat:damage aura", particlesPerBunch, 1, Int.MaxValue, "Amount of particles per bunch that will spawn to indicate a damaging aura")
		damage = config.getFloat("damage", category"potion:combat:damage aura", particlesPerBunch, .5F, Float.MaxValue, "Amount of damage each EntityLiving will take from each hit of a damaging aura [hearts]")
	}
}
