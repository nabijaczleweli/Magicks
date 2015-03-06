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
}
