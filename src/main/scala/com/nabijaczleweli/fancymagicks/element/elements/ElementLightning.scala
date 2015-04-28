package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

object ElementLightning extends Element {
	override val opposites = ElementWater :: ElementEarth :: Nil

	override def prioritizesOver(element: Element) =
		(element.elementType contains ElementType.spray) && element != ElementSteam

	override val colour = 0xA030D0
}
