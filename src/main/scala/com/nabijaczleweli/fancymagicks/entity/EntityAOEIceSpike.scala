package com.nabijaczleweli.fancymagicks.entity

import java.lang.{Float => jFloat}

import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.{IConfigurable, NBTReloadable}
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.config.Configuration

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

	override def onUpdate() {
		super.onUpdate()
		if(ticksExisted > EntityAOEIceSpike.cycleLength)
			worldObj removeEntity this
	}


	override def saveNBTData(tag: NBTTagCompound) =
		writeEntityToNBT(tag)

	override def loadNBTData(tag: NBTTagCompound) =
		readEntityFromNBT(tag)
}

object EntityAOEIceSpike extends IConfigurable {
	val forceObjectID = 2 // Safe? Maybe needs to be higher?

	var cycleLength = 20

	override def configure(config: Configuration) {
		cycleLength = config.getInt("AOEIceSpikeCycleLength", "combat", cycleLength, 0, Int.MaxValue, "Full cycle length of raising/pulling down AOEIceSpikes in ticks")
	}
}
