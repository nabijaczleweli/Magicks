package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertySelectionDirection

trait Element {
	def opposites: List[Element]

	def prioritizesOver(element: Element): Boolean
}

class ElementSimple extends Element {
	override def opposites =
		Nil

	override def prioritizesOver(element: Element) =
		false
}
object ElementArcane extends ElementSimple
object ElementLife extends ElementSimple
object ElementLightning extends ElementSimple
object ElementWater extends ElementSimple
object ElementCold extends ElementSimple
object ElementFire extends ElementSimple
object ElementEarth extends ElementSimple
object ElementShield extends ElementSimple

object Element {
	implicit def orderingElement(element: Element): Ordering[Element] =
		new Ordering[Element] {
			override def compare(x: Element, y: Element) =
				if(x prioritizesOver y)
					1
				else if(y prioritizesOver x)
					-1
				else
					0
		}

	def apply(selector: ExtendedPropertySelectionDirection) = {
		val circle = new ElementaryCircle
		while(selector.directions.nonEmpty)
			circle moveSelection selector.directions.dequeue
		println("main: " + circle.mainDirection)
		println("side: " + circle.sideDirection)
		println("elem: " + circle.get)
		circle.get
	}
}
