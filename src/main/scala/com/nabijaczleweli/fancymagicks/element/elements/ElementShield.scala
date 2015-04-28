package com.nabijaczleweli.fancymagicks.element.elements

object ElementShield extends Element {
	override val opposites = this :: Nil

	override def prioritizesOver(element: Element) =
		true

	override val colour = 0xF7CF0D
}
