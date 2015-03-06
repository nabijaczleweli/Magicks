package com.nabijaczleweli.fancymagicks.element

abstract class ElementBeam extends Element {
	override def prioritizesOver(element: Element) =
		element == ElementLightning || element.elementType == Some(ElementType.spray)

	override def elementType =
		Some(ElementType.beam)
}

object ElementArcane extends ElementBeam {
	override def opposites =
		ElementLife :: Nil
}

object ElementLife extends ElementBeam {
	override def opposites =
		ElementArcane :: Nil
}