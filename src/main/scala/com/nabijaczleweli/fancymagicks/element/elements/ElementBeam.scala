package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

abstract class ElementBeam extends Element {
	override def prioritizesOver(element: Element) =
		element == ElementLightning || (element.elementType contains ElementType.spray)

	override val elementType = Some(ElementType.beam)
}

object ElementArcane extends ElementBeam {
	override val opposites = ElementLife :: Nil
	override val colour = 0xFF0000
}

object ElementLife extends ElementBeam {
	override val opposites = ElementArcane :: Nil
	override val colour = 0x00FF00
}
