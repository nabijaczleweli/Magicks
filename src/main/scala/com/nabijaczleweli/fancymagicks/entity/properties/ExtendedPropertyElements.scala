package com.nabijaczleweli.fancymagicks.entity.properties

import com.nabijaczleweli.fancymagicks.element.Element
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.util.Constants

class ExtendedPropertyElements extends IExtendedEntityProperties {
	val elements = new Array[Element](5)
	var entity: Entity = _

	override def init(entity: Entity, world: World) {
		this.entity = entity
	}

	override def saveNBTData(compound: NBTTagCompound) =
		compound.setTag(s"${Reference.NAMESPACED_PREFIX}elements", {val t = new NBTTagList; elements filter {_ != null} map {_.getClass.getName} map {new NBTTagString(_)} foreach {t.appendTag}; t})

	override def loadNBTData(compound: NBTTagCompound) {
		val l = compound.getTagList(s"${Reference.NAMESPACED_PREFIX}elements", Constants.NBT.TAG_STRING)
		System.arraycopy(((0 until l.tagCount) map {l.getStringTagAt} map {Class.forName} map {_ getField "MODULE$"} map {_ get null} map {_.asInstanceOf[Element]}).toArray, 0, elements, 0, l.tagCount)
	}

	def addElement(element: Element) =
		if(element == null)
			false
		else if(synegizeOrRemove(element))
			true
		else
			elements indexOf null match {
				case -1 =>
					false
				case idx =>
					elements(idx) = element
					true
			}

	def update() =
		entity match {
			case ent: EntityLivingBase =>
				if(ExtendedPropertyElements.slowdown)
					elements count {_ != null} match {
						case 0 =>
						case i =>
							ent addPotionEffect new PotionEffect(Potion.moveSlowdown.getId, 1, i - 1, true)
					}
			case _ =>
		}

	private def synegizeOrRemove(element: Element) = {
		var result = false
		for(idx <- elements.indices if !result if elements(idx) != null)
			if(elements(idx).opposites contains element) {
				result = true
				elements(idx) = null
				if(idx != elements.length)
					for(i <- idx until elements.length - 1)
						elements(idx) = elements(idx + 1)
			} else
				for(elem <- elements(idx) synergize element) {
					elements(idx) = elem
					result = true
				}
		result
	}
}

object ExtendedPropertyElements extends IConfigurable {
	val id = s"${Reference.NAMESPACED_PREFIX}elements"
	var slowdown = true

	override def configure(config: Configuration) {
		slowdown = config.getBoolean("elementPreparedSlowdown", "elements", slowdown, "Whether to apply slowdown when elements are prepared/\"on hotbar\"")
	}
}
