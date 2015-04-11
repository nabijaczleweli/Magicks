package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements.{ElementSpray, ElementBeam}

object ElementAOECaster extends ElementCasterBuilder[NoElementAOECaster] {
	override protected val leads: LeadsType = Map(
		simpleLead[ElementBeam, ElementBeamAOECaster],
		simpleLead[ElementSpray, ElementSprayAOECaster]
	)
	println(leads)
}
