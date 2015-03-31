package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.element.ElementType.ElementType
import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertySelectionDirection

trait Element {
	def opposites: Seq[Element]

	def prioritizesOver(element: Element): Boolean

	def elementType: Option[ElementType] =
		None

	def synergize(`with`: Element): Option[Element] =
		None

	def colour: Int
}

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

	/** Might be costly, caching recommended */
	def name(element: Element) = {
		val name = element.getClass.getSimpleName
		name.substring((name indexOf "Element") + "Element".length, (name indexOf "$") :: Nil find {_ != -1} getOrElse {name.length})
	}
}
