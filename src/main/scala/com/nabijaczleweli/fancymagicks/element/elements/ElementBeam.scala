package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

abstract class ElementBeam extends Element {
	override def prioritizesOver(element: Element) =
		element == ElementLightning || element.elementType == Some(ElementType.spray)

	override def elementType =
		Some(ElementType.beam)
}

object ElementArcane extends ElementBeam {
	override def opposites =
		ElementLife :: Nil

	override def colour =
		0xFF0000
}

object ElementLife extends ElementBeam {
	override def opposites =
		ElementArcane :: Nil

	override def colour =
		0x00FF00
}
