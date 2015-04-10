package com.nabijaczleweli.fancymagicks.element.caster

import com.nabijaczleweli.fancymagicks.element.elements.Element
import net.minecraft.entity.Entity

abstract class ElementCasterBuilder[NoElementType >: Null <: ElementCaster : Manifest] {
	private lazy val noConstructor = implicitly[Manifest[NoElementType]].runtimeClass.asInstanceOf[Class[NoElementType]] getConstructor classOf[Entity]

	protected type LeadsType = Map[Class[_ <: Element], (Entity, Seq[Element]) => ElementCaster]

	protected def simplyConstruct[T >: Null <: ElementCaster : Manifest](who: Entity, elems: Seq[Element]) =
		implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]].getConstructor(classOf[Entity], classOf[Seq[Element]]).newInstance(who, elems)

	protected def leads: LeadsType

	def apply(who: Entity, elems: Seq[Element]) =
		elems.sorted match {
			case Nil =>
				noConstructor newInstance who
			case selems =>
				(leads find {_._1 isAssignableFrom elems.head.getClass} map {_._2} getOrElse simplyConstruct[NoElementType] _)(who, selems)
		}
}
