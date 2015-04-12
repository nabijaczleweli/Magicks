package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.NBTReloadable
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

import java.lang.{Float => jFloat}

class EntityAOEIceSpike(world: World) extends Entity(world) with NBTReloadable {
	var progressUp = 0


	def force =
		dataWatcher getWatchableObjectFloat EntityAOEIceSpike.forceObjectID

	def force_=(force: Float) =
		dataWatcher.updateObject(EntityAOEIceSpike.forceObjectID, force: jFloat)


	override def entityInit() =
		dataWatcher.addObject(EntityAOEIceSpike.forceObjectID, 0f: jFloat)

	override def writeEntityToNBT(tag: NBTTagCompound) {
		tag.setFloat(s"${Reference.NAMESPACED_PREFIX}force", force)
	}

	override def readEntityFromNBT(tag: NBTTagCompound) {
		force = tag getFloat s"${Reference.NAMESPACED_PREFIX}force"
	}


	override def saveNBTData(tag: NBTTagCompound) =
		writeEntityToNBT(tag)

	override def loadNBTData(tag: NBTTagCompound) =
		readEntityFromNBT(tag)
}

object EntityAOEIceSpike {
	val forceObjectID = 2 // Safe? Maybe needs to be higher?
}
