package com.nabijaczleweli.fancymagicks.entity.properties

import com.nabijaczleweli.fancymagicks.element.{ElementCaster, Element}
import com.nabijaczleweli.fancymagicks.potion.Potion
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.{NBTReloadable, IConfigurable}
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraft.potion.{Potion => mPotion}
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.util.Constants

class ExtendedPropertyElements extends IExtendedEntityProperties with NBTReloadable {
	val elements = new Array[Element](5)
	var caster: ElementCaster = _
	var entity: Entity = _

	override def init(entity: Entity, world: World) {
		this.entity = entity
	}

	override def saveNBTData(compound: NBTTagCompound) {
		compound.setTag(s"${Reference.NAMESPACED_PREFIX}elements", {val t = new NBTTagList; elements filter {_ != null} map {_.getClass.getName} map {new NBTTagString(_)} foreach {t.appendTag}; t})

		if(caster != null) {
			val casterTag = new NBTTagCompound
			casterTag.setString("class", caster.getClass.getName)
			casterTag.setTag("data", new NBTTagCompound ensuring {caster saveNBTData _; true})
			compound.setTag(s"${Reference.NAMESPACED_PREFIX}caster", casterTag)
		}
	}

	override def loadNBTData(compound: NBTTagCompound) {
		val l = compound.getTagList(s"${Reference.NAMESPACED_PREFIX}elements", Constants.NBT.TAG_STRING)
		System.arraycopy(((0 until l.tagCount) map {l.getStringTagAt} map {Class.forName} map {_ getField "MODULE$"} map {_ get null} map {_.asInstanceOf[Element]}).toArray, 0, elements, 0, l.tagCount)

		val casterTag = compound getCompoundTag s"${Reference.NAMESPACED_PREFIX}caster"
		if(!casterTag.hasNoTags) {
			caster = (Class forName (casterTag getString "class")).asInstanceOf[Class[ElementCaster]].getConstructor(classOf[Entity], classOf[Seq[Element]]).newInstance(entity, properElements)
			caster loadNBTData (casterTag getCompoundTag "data")
		}
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

	def properElements =
		elements filterNot {_ == null}

	def update() =
		entity match {
			case ent: EntityLivingBase =>
				if(ExtendedPropertyElements.slowdown)
					elements count {_ != null} match {
						case 0 =>
						case i =>
							Potion.applyEffect(mPotion.moveSlowdown, i - 1)(ent)
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
