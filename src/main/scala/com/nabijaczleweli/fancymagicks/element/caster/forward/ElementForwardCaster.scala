package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements._

object ElementForwardCaster extends ElementCasterBuilder[NoElementForwardCaster] {
	override protected val leads: LeadsType = Map(
		ElementCasterBuilder.simpleLead[ElementBeam, ElementBeamForwardCaster],
		ElementCasterBuilder.simpleLead[ElementSpray, ElementSprayForwardCaster],
		ElementCasterBuilder.simpleLead[ElementEarthForwardCaster](ElementIce),
		ElementCasterBuilder.simpleLead[ElementIceForwardCaster](ElementEarth)
	)
	println(leads)
}
