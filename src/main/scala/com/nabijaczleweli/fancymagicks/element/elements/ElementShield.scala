package com.nabijaczleweli.fancymagicks.element.elements

object ElementShield extends Element {
	override def opposites =
		this :: Nil

	override def prioritizesOver(element: Element) =
		true

	override def colour =
		0xF7CF0D
}
