package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.OneOffElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraftforge.common.config.Configuration

import scala.concurrent.Future

class ElementShieldAOECaster(who: Entity, elems: Seq[Element]) extends OneOffElementCaster {
	override protected def cast() {
		for(value <- ElementShieldAOECaster.originalPoints.value; points <- value; (x, y, z) <- points)
			who.worldObj.setBlock(x + who.posX.toInt, y + who.posY.toInt, z + who.posZ.toInt, Blocks.brick_block)
	}
}

object ElementShieldAOECaster extends IConfigurable {
	import scala.concurrent.ExecutionContext.Implicits.global

	private var originalPoints = Future successful Set.empty[(Int, Int, Int)]

	var shieldRadius = 10

	override def configure(config: Configuration) {
		shieldRadius = config.getInt("Shield radius", category"combat:element casting:aoe:shield", shieldRadius, 1, Int.MaxValue, "Shield sphere's radius. [blocks]")
		originalPoints = Future(computePoints.toSet)
	}

	private def computePoints = {
		val steps = math.ceil(math.Pi / math.atan(1D / shieldRadius / 2)).toInt

		for(vertical_steps <- 0 until 2 * steps; horizontal_steps <- 0 until steps) yield {
			val phi = math.Pi * 2 / steps * vertical_steps
			val theta = math.Pi / steps * horizontal_steps

			(
				((math sin theta) * (math cos phi) * shieldRadius).toInt,
				((math cos theta) * shieldRadius).toInt,
				((math sin theta) * (math sin phi) * shieldRadius).toInt
			)
		}
	}
}
