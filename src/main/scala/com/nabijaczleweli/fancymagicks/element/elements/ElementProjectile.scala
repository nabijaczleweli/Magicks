package com.nabijaczleweli.fancymagicks.element.elements

import com.nabijaczleweli.fancymagicks.element.ElementType

abstract class ElementProjectile extends Element {
	override def prioritizesOver(element: Element) =
		 element != ElementShield

	override val elementType = Some(ElementType.projectile)
}

object ElementEarth extends ElementProjectile {
	override val opposites = ElementLightning :: Nil
	override val colour = 0x503B2A
}

object ElementIce extends ElementProjectile {
	override val opposites = ElementLightning :: Nil

	override def synergize(`with`: Element) =
		`with` match {
			case ElementFire =>
				Some(ElementWater)
			case _ =>
				None
		}

	override val colour = 0x26D0F4
}
