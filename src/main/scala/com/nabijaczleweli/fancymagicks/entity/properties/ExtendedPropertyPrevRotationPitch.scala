package com.nabijaczleweli.fancymagicks.entity.properties

import com.nabijaczleweli.fancymagicks.reference.Reference
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties

/** This might be an abuse of IExtendedEntityProperties. */
class ExtendedPropertyPrevRotationPitch extends IExtendedEntityProperties {
	var prevRotationPitch = 0f
	var entity: Entity = _

	override def init(entity: Entity, world: World){
		this.entity = entity
	}

	override def saveNBTData(compound: NBTTagCompound) {}

	override def loadNBTData(compound: NBTTagCompound) {}

	def update() =
		if(entity != null)
			prevRotationPitch = entity.rotationPitch
}

object ExtendedPropertyPrevRotationPitch {
	val id = s"${Reference.NAMESPACED_PREFIX}prevPitch"
}
