package com.nabijaczleweli.fancymagicks.element.caster

import com.nabijaczleweli.fancymagicks.reference.Reference._
import net.minecraft.nbt.NBTTagCompound

trait OneOffElementCaster extends ElementCaster {
	protected def cast(): Unit

	protected var isCast: Boolean = _

	override def start() =
		isCast = false

	override def continue() =
		if(!isCast)
			cast()

	override def end() =
		isCast = true

	override def loadNBTData(tag: NBTTagCompound) =
		isCast = tag getBoolean s"${NAMESPACED_PREFIX}cast"

	override def saveNBTData(tag: NBTTagCompound) =
		tag.setBoolean(s"${NAMESPACED_PREFIX}cast", isCast)
}
