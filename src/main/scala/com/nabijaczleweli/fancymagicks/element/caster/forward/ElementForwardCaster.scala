package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.{ElementCasterBuilder, OneOffElementCaster}
import com.nabijaczleweli.fancymagicks.element.elements._
import com.nabijaczleweli.fancymagicks.entity.EntityEarthBall
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.ElementalThrowableEntitySpawnData
import net.minecraft.entity.{Entity, EntityLivingBase}

object ElementForwardCaster extends ElementCasterBuilder[NoElementForwardCaster] {
	private def projectileHandler(who: Entity, elems: Seq[Element]) =
		(ElementCasterBuilder projectileGroup elems).headOption match {
			case None =>
				new NoElementForwardCaster(who)
			case Some(e) =>
				e match {
					case ElementEarth =>
						// Placeholder \/
						new OneOffElementCaster {
							override protected def cast() =
								EntityUtil dispatchElementalThrowableSpawn ElementalThrowableEntitySpawnData(classOf[EntityEarthBall], who.asInstanceOf[EntityLivingBase], elems)
						}
					case ElementIce =>
						//new ElementIceForwardCaster(who, elems)
						null
				}
		}

	override protected val leads: LeadsType = Map(
		ElementCasterBuilder.simpleLead[ElementBeam, ElementBeamForwardCaster],
		ElementCasterBuilder.simpleLead[ElementSpray, ElementSprayForwardCaster],
		classOf[ElementProjectile] -> projectileHandler
	)
	println(leads)
}
