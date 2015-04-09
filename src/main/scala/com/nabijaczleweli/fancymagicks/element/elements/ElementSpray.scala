package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

abstract class ElementSpray extends Element {
	override def prioritizesOver(element: Element) =
		false

	override def elementType =
		Some(ElementType.spray)
}

object ElementCold extends ElementSpray {
	override def opposites =
		ElementFire :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementWater =>
				Some(ElementIce)
			case _ =>
				None
		}

	override def colour =
		0xADD6F2
}

object ElementFire extends ElementSpray {
	override def opposites =
		ElementCold :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementWater =>
				Some(ElementSteam)
			case _ =>
				None
		}

	override def colour =
		0xFF7D29
}

object ElementSteam extends ElementSpray {
	override def prioritizesOver(element: Element) =
		element == ElementLightning || element.elementType == Some(ElementType.spray)

	override def opposites =
		Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementCold =>
				Some(ElementWater)
			case _ =>
				None
		}

	override def colour =
		0xAFAFAF
}

object ElementWater extends ElementSpray {
	override def opposites =
		ElementLightning :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementFire =>
				Some(ElementSteam)
			case _ =>
				None
		}

	override def colour =
		0x005BE0
}
