package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.ElementCasterBuilder
import com.nabijaczleweli.fancymagicks.element.elements.ElementBeam

object ElementAOECaster extends ElementCasterBuilder[NoElementAOECaster] {
	override protected val leads: LeadsType = Map(classOf[ElementBeam] -> simplyConstruct[ElementBeamAOECaster] _)
	println(leads)
}
