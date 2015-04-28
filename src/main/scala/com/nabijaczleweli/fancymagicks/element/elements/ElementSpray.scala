package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

abstract class ElementSpray extends Element {
	override def prioritizesOver(element: Element) =
		false

	override val elementType = Some(ElementType.spray)
}

object ElementCold extends ElementSpray {
	override val opposites = ElementFire :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementWater =>
				Some(ElementIce)
			case _ =>
				None
		}

	override val colour = 0xADD6F2
}

object ElementFire extends ElementSpray {
	override val opposites = ElementCold :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementWater =>
				Some(ElementSteam)
			case _ =>
				None
		}

	override val colour = 0xFF7D29
}

object ElementSteam extends ElementSpray {
	override def prioritizesOver(element: Element) =
		element == ElementLightning || (element.elementType contains ElementType.spray)

	override val opposites = Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementCold =>
				Some(ElementWater)
			case _ =>
				None
		}

	override val colour = 0xAFAFAF
}

object ElementWater extends ElementSpray {
	override val opposites = ElementLightning :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementFire =>
				Some(ElementSteam)
			case _ =>
				None
		}

	override val colour = 0x005BE0
}
