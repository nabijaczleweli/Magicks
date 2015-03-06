package com.nabijaczleweli.fancymagicks.element

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
}
