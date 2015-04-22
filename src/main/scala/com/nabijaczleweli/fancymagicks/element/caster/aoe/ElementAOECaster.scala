package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements._
import net.minecraft.entity.Entity

object ElementAOECaster extends ElementCasterBuilder[NoElementAOECaster] {
	private def projectileHandler(who: Entity, elems: Seq[Element]) =
		(ElementCasterBuilder projectileGroup elems).headOption match {
			case None =>
				new NoElementAOECaster(who)
			case Some(e) =>
				e match {
					case ElementEarth =>
						new ElementEarthAOECaster(who, elems)
					case ElementIce =>
						new ElementIceAOECaster(who, elems)
				}
		}

	override protected val leads: LeadsType = Map(
		ElementCasterBuilder.simpleLead[ElementBeam, ElementBeamAOECaster],
		ElementCasterBuilder.simpleLead[ElementSpray, ElementSprayAOECaster],
		classOf[ElementProjectile] -> projectileHandler
	)
	println(leads)
}
