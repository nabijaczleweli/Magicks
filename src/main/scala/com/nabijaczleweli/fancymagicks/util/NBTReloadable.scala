package com.nabijaczleweli.fancymagicks.util

import net.minecraft.nbt.NBTTagCompound

trait NBTLoadable extends Any {
	def loadNBTData(tag: NBTTagCompound): Unit
}

trait NBTSavable extends Any {
	def saveNBTData(tag: NBTTagCompound): Unit
}

trait NBTReloadable extends Any with NBTLoadable with NBTSavable
