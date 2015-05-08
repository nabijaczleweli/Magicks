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

	private final val tau = 2 * math.Pi
	private var originalPoints = Future successful Set.empty[(Int, Int, Int)]

	var shieldRadius = 10

	override def configure(config: Configuration) {
		shieldRadius = config.getInt("Shield radius", category"combat:element casting:aoe:shield", shieldRadius, 1, Int.MaxValue, "Shield \"circle\"'s radius. Big values might hog memory. [blocks]")
		originalPoints = Future(computePoints)
		println(s"new shield radius = $shieldRadius; new future = $originalPoints")
	}

	private def computePoints = {
		val numOrigSegments = tau * shieldRadius
		Seq.tabulate(numOrigSegments.toInt)({ii =>
			val theta = tau * ii / numOrigSegments.toFloat

			val x = shieldRadius * (math cos theta)
			val z = shieldRadius * (math sin theta)

			(x.toInt, 0, z.toInt)
		}).toSet
	}
}
