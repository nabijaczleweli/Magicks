package com.nabijaczleweli.fancymagicks.entity.properties

import java.util.{Map => jMap}

import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.Direction.Direction
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties

import scala.collection.mutable.{Queue => mQueue}

class ExtendedPropertySelectionDirection extends IExtendedEntityProperties {
	val directions = mQueue[Direction]()

	override def init(entity: Entity, world: World) {}

	override def saveNBTData(compound: NBTTagCompound) {}

	override def loadNBTData(compound: NBTTagCompound) {}
}

object ExtendedPropertySelectionDirection {
	private lazy val extendedPropertiesField =
		classOf[Entity].getDeclaredFields find {classOf[jMap[String, IExtendedEntityProperties]] isAssignableFrom _.getType} match {
			case Some(f) =>
				reflect ensureAccessible f
			case None =>
				throw new NoSuchFieldException("Couldn\'t find field `extendedProperties` inside `Entity`! This indicates critical tampering!")
		}
	val id = s"${Reference.NAMESPACED_PREFIX}selectionDirection"

	def removeFrom(entity: Entity) =
		(extendedPropertiesField get entity).asInstanceOf[jMap[String, IExtendedEntityProperties]] remove id
}
