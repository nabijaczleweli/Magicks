package com.nabijaczleweli.fancymagicks.element

abstract class ElementProjectile extends Element {
	override def prioritizesOver(element: Element) =
		 element != ElementShield

	override def elementType =
		Some(ElementType.projectile)
}

object ElementEarth extends ElementProjectile {
	override def opposites =
		ElementLightning :: Nil

	override def colour =
		0x503B2A
}

object ElementIce extends ElementProjectile {
	override def opposites =
		ElementLightning :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementFire =>
				Some(ElementWater)
			case _ =>
				None
		}

	override def colour =
		0x26D0F4
}
