package com.nabijaczleweli.fancymagicks.entity

import java.lang.{Float => jFloat}

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.elements.ElementIce
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.NBTReloadable
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

class EntityAOEIceSpike(world: World) extends Entity(world) with NBTReloadable {
	setSize(.875F, 2.625F)


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

	override def canBeCollidedWith =
		true

	override def canBePushed =
		true

	override def applyEntityCollision(entity: Entity) {
		ElementalDamageSource.dispatchDamage(new ElementalDamageSource(this, Seq.fill(5)(ElementIce)), entity, force * 50)
	}


	override def saveNBTData(tag: NBTTagCompound) =
		writeEntityToNBT(tag)

	override def loadNBTData(tag: NBTTagCompound) =
		readEntityFromNBT(tag)
}

object EntityAOEIceSpike {
	val forceObjectID = 2 // Safe? Maybe needs to be higher?
}
