package com.nabijaczleweli.fancymagicks.element.caster

import com.nabijaczleweli.fancymagicks.element.elements.{ElementEarth, ElementProjectile, Element}
import net.minecraft.entity.Entity

abstract class ElementCasterBuilder[NoElementType >: Null <: ElementCaster : Manifest] {
	private lazy val noConstructor = implicitly[Manifest[NoElementType]].runtimeClass.asInstanceOf[Class[NoElementType]] getConstructor classOf[Entity]

	protected type LeadsType = Map[Class[_ <: Element], (Entity, Seq[Element]) => ElementCaster]

	protected def leads: LeadsType

	def apply(who: Entity, elems: Seq[Element]) =
		elems.sorted match {
			case Nil =>
				noConstructor newInstance who
			case selems =>
				(leads find {_._1 isAssignableFrom elems.head.getClass} map {_._2} getOrElse ElementCasterBuilder.simplyConstruct[NoElementType] _)(who, selems)
		}
}

private[element] object ElementCasterBuilder {
	def simplyConstruct[T >: Null <: ElementCaster : Manifest](who: Entity, elems: Seq[Element]) =
		implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]].getConstructor(classOf[Entity], classOf[Seq[Element]]).newInstance(who, elems)

	def simpleLead[TE >: Null <: Element : Manifest, TEC >: Null <: ElementCaster : Manifest] =
		implicitly[Manifest[TE]].runtimeClass.asInstanceOf[Class[TE]] -> simplyConstruct[TEC] _

	def projectileGroup(elems: Seq[Element]) =
		elems groupBy {_.isInstanceOf[ElementProjectile]} withDefaultValue Nil apply true sortWith {(lhs, rhs) => lhs == ElementEarth && rhs != ElementEarth}
}
