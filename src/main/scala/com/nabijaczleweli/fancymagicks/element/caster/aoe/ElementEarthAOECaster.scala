package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementEarth}
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.Entity
import net.minecraftforge.common.config.Configuration

import scala.util.Random

class ElementEarthAOECaster(who: Entity, elems: Seq[Element]) extends ElementProjectileAOECaster(who, elems) {
	override val elementClass = ElementEarth.getClass

	def doVisualEffect(x: Double, y: Double, z: Double) = {
		def randNeg(d: Double) =
			if(ElementEarthAOECaster.random.nextBoolean()) d else -d
		def offset =
			ElementEarthAOECaster.random.nextFloat() * randNeg(ElementEarthAOECaster.particlesMaxRadius)
		def movement =
			ElementEarthAOECaster.random.nextFloat() * randNeg(.5)

		for(i <- 0 until ElementEarthAOECaster.particlesPerSummon)
			who.worldObj.spawnParticle("smoke", x + .5 + offset, y + .3, z + .5 + offset, movement, 0, movement)
	}
}

object ElementEarthAOECaster extends IConfigurable {
	private val random = new Random

	var particlesPerSummon = 10
	var particlesMaxRadius = .3F

	override def configure(config: Configuration) {
		particlesPerSummon = config.getInt("smokeQuakesPerDust", "render", particlesPerSummon, 0, Int.MaxValue, "Amount of smoke particles")
		particlesMaxRadius = config.getFloat("smokeQuakeRadius", "render", particlesMaxRadius, 0f, Float.MaxValue, "Maximal radius at which somke can be seen [blocks]")
	}
}
