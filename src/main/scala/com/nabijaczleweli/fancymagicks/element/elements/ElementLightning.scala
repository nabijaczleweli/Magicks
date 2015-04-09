package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

object ElementLightning extends Element {
	override def opposites =
		ElementWater :: ElementEarth :: Nil

	override def prioritizesOver(element: Element) =
		element.elementType == Some(ElementType.spray) && element != ElementSteam

	override def colour =
		0xA030D0
}
