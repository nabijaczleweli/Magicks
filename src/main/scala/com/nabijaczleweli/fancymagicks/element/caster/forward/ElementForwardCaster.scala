package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements.{ElementSpray, ElementBeam}

object ElementForwardCaster extends ElementCasterBuilder[NoElementForwardCaster] {
	override protected val leads: LeadsType = Map(
		simpleLead[ElementBeam, ElementBeamForwardCaster],
		simpleLead[ElementSpray, ElementSprayForwardCaster]
	)
	println(leads)
}
