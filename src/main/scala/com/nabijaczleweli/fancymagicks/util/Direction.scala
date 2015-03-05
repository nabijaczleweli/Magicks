package com.nabijaczleweli.fancymagicks.util

object Direction extends Enumeration {
	type Direction = Value
	val up, down, left, right = Value

	def opposite(dir: Direction) =
		dir match {
			case `up` =>
				down
			case `down` =>
				up
			case `left` =>
				right
			case `right` =>
				left
		}
}
