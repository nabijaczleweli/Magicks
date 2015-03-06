package com.nabijaczleweli.fancymagicks.element

object ElementLightning extends Element {
	override def opposites =
		ElementWater :: ElementEarth :: Nil

	override def prioritizesOver(element: Element) =
		element.elementType == Some(ElementType.spray) && element != ElementSteam
}