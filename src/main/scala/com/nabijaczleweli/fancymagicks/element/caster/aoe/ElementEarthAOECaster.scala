package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementEarth}
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.Entity
import net.minecraftforge.common.config.Configuration

import scala.util.Random

class ElementEarthAOECaster(who: Entity, elems: Seq[Element]) extends ElementProjectileAOECaster(who, elems) {
	import ElementEarthAOECaster._

	override val elementClass = ElementEarth.getClass

	def doVisualEffect(x: Double, y: Double, z: Double) = {
		def randNeg(d: Double) =
			if(random.nextBoolean()) d else -d
		def offset =
			random.nextFloat() * randNeg(particlesMaxRadius)
		def movement =
			if(swirlyMovement) random.nextFloat() * randNeg(.5) else 0D

		for(_ <- 0 until particlesPerSummon)
			who.worldObj.spawnParticle("smoke", x + .5 + offset, y + .3, z + .5 + offset, movement, 0, movement)
	}
}

object ElementEarthAOECaster extends IConfigurable {
	private val random = new Random

	var particlesPerSummon = 10
	var particlesMaxRadius = .3F
	var swirlyMovement     = true

	override def configure(config: Configuration) {
		particlesPerSummon = config.getInt("Particles per spawnpoint", category"render:element casting:aoe:earth", particlesPerSummon, 0, Int.MaxValue, "Amount of smoke particles")
		particlesMaxRadius = config.getFloat("Max smoke particle radius", category"render:element casting:aoe:earth", particlesMaxRadius, 0f, Float.MaxValue, "Maximal radius at which smoke can be summoned [blocks]")
		swirlyMovement = config.getBoolean("Particles' swirly movement", category"render:element casting:aoe:earth", swirlyMovement, "Whether smoke particles should swirl around on the Y plane")
	}
}
