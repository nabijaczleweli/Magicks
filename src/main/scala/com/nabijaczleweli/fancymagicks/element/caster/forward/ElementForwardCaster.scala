package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements.{ElementBeam, ElementSpray}

object ElementForwardCaster extends ElementCasterBuilder[NoElementForwardCaster] {
	override protected val leads: LeadsType = Map(
		ElementCasterBuilder.simpleLead[ElementBeam, ElementBeamForwardCaster],
		ElementCasterBuilder.simpleLead[ElementSpray, ElementSprayForwardCaster]
	)
	println(leads)
}
