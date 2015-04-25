package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements._

object ElementAOECaster extends ElementCasterBuilder[NoElementAOECaster] {
	override protected val leads: LeadsType = Map(
		ElementCasterBuilder.simpleLead[ElementBeam, ElementBeamAOECaster],
		ElementCasterBuilder.simpleLead[ElementSpray, ElementSprayAOECaster],
		ElementCasterBuilder.simpleLead[ElementEarthAOECaster](ElementEarth),
		ElementCasterBuilder.simpleLead[ElementIceAOECaster](ElementIce)
	)
	println(leads)
}
