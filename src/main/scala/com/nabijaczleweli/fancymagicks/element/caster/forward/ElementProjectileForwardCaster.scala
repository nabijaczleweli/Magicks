package com.nabijaczleweli.fancymagicks.element.caster.forward

import com.nabijaczleweli.fancymagicks.element.caster.ElementCaster
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.util.EntityUtil.ElementalThrowableEntitySpawnData
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IElemental}
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.nbt.NBTTagCompound

abstract class ElementProjectileForwardCaster(who: Entity, elems: Seq[Element]) extends ElementCaster {
	def elementClass: Class[_ <: Element]
	def entityClass: Class[_ <: EntityThrowable with IElemental]

	override def start() {}

	override def continue() {}

	override def end() =
		EntityUtil dispatchElementalThrowableSpawn ElementalThrowableEntitySpawnData(entityClass, who.asInstanceOf[EntityLivingBase], elems)

	override def loadNBTData(tag: NBTTagCompound) {}

	override def saveNBTData(tag: NBTTagCompound) {}
}
