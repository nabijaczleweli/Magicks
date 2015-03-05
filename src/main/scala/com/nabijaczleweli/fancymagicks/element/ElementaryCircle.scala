package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.util.Direction
import com.nabijaczleweli.fancymagicks.util.Direction.Direction

import scala.collection.immutable.HashMap

class ElementaryCircle {
	var mainDirection: Option[Direction] = None
	var sideDirection: Option[Direction] = None

	def moveSelection(direction: Direction) =
		moveMainSelection(direction)

	def validSides(direction: Direction) =
		direction match {
			case Direction.up | Direction.down =>
				Direction.left :: Direction.right :: Nil
			case Direction.left | Direction.right =>
				Direction.up :: Direction.down :: Nil
		}

	def get =
		if(mainDirection == None || sideDirection == None)
			null
		else
			(ElementaryCircle.mainToSide.getOrElse(mainDirection.get, Map.empty) get sideDirection.get).orNull

	private def moveMainSelection(direction: Direction) =
		mainDirection match {
			case Some(dir) =>
				if(dir == (Direction opposite direction)) {
					mainDirection = None
					sideDirection = None
				} else
					moveSideSelection(direction)
			case None =>
				mainDirection = Some(direction)
		}

	private def moveSideSelection(direction: Direction) =
		sideDirection match {
			case Some(dir) =>
				if(dir == (Direction opposite direction))
					sideDirection = None
			case None =>
				if(mainDirection.nonEmpty || (validSides(mainDirection.get) contains direction))
					sideDirection = Some(direction)
		}
}

private object ElementaryCircle {
	lazy val mainToSide: Map[Direction, Map[Direction, Element]] = {
		var m = new HashMap[Direction, Map[Direction, Element]]
		m += Direction.up -> HashMap(Direction.left -> ElementArcane, Direction.right -> ElementLife)
		m += Direction.left -> HashMap(Direction.up -> ElementLightning, Direction.down -> ElementWater)
		m += Direction.right -> HashMap(Direction.up -> ElementCold, Direction.down -> ElementFire)
		m += Direction.down -> HashMap(Direction.left -> ElementEarth, Direction.right -> ElementShield)
		m
	}
}
