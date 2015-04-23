package com.nabijaczleweli.fancymagicks.element.caster

import java.lang.reflect.Constructor

import com.nabijaczleweli.fancymagicks.element.elements.{ElementEarth, ElementProjectile, Element}
import net.minecraft.entity.Entity

import scala.collection.mutable.{HashMap => mHashMap, Map => mMap}

abstract class ElementCasterBuilder[NoElementType >: Null <: ElementCaster : Manifest] {
	protected type LeadsType = Map[Class[_ <: Element], (Entity, Seq[Element]) => ElementCaster]

	protected def leads: LeadsType

	def apply(who: Entity, elems: Seq[Element]) =
		elems.sorted match {
			case Nil =>
				ElementCasterBuilder.simplyConstruct[NoElementType](who, elems)
			case selems =>
				(leads find {_._1 isAssignableFrom elems.head.getClass} map {_._2} getOrElse ElementCasterBuilder.simplyConstruct[NoElementType] _)(who, selems)
		}
}

private[element] object ElementCasterBuilder {
	private val constructCache: mMap[Manifest[_], Constructor[_]] = new mHashMap

	def simplyConstruct[T >: Null <: ElementCaster : Manifest](who: Entity, elems: Seq[Element]) =
		constructCache.getOrElseUpdate(implicitly[Manifest[T]], implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]].getConstructor(classOf[Entity], classOf[Seq[Element]])).newInstance(who, elems).asInstanceOf[T]

	def simpleLead[TE >: Null <: Element : Manifest, TEC >: Null <: ElementCaster : Manifest] =
		implicitly[Manifest[TE]].runtimeClass.asInstanceOf[Class[TE]] -> simplyConstruct[TEC] _

	def projectileGroup(elems: Seq[Element]) =
		elems groupBy {_.isInstanceOf[ElementProjectile]} withDefaultValue Nil apply true sortWith {(lhs, rhs) => lhs == ElementEarth && rhs != ElementEarth}
}
