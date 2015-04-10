package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements.ElementBeam

object ElementForwardCaster extends ElementCasterBuilder[NoElementForwardCaster] {
	override protected val leads: LeadsType = Map(classOf[ElementBeam] -> simplyConstruct[ElementBeamForwardCaster] _)
	println(leads)
}
