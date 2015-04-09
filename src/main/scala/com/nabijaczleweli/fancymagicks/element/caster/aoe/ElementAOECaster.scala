package com.nabijaczleweli.fancymagicks.element.caster.aoe

import com.nabijaczleweli.fancymagicks.element.caster.ElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementBeam}
import net.minecraft.entity.Entity

object ElementAOECaster {
	private def simplyConstruct[T <: ElementCaster : Manifest](who: Entity, elems: Seq[Element]) =
		implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]].getConstructor(classOf[Entity], classOf[Seq[Element]]).newInstance(who, elems)

	private val leads = Map[Class[_ <: Element], (Entity, Seq[Element]) => ElementCaster](classOf[ElementBeam] -> simplyConstruct[ElementBeamAOECaster])
	println(leads)

	def apply(who: Entity, elems: Seq[Element]) =
		elems.sorted match {
			case Nil =>
				new NoElementAOECaster(who)
			case selems =>
				(leads find {_._1 isAssignableFrom elems.head.getClass} map {_._2} getOrElse simplyConstruct[NoElementAOECaster] _)(who, selems)
		}
}
