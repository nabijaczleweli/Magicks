package com.nabijaczleweli.fancymagicks.util

import net.minecraft.nbt.NBTTagCompound

trait NBTLoadable {
	def loadNBTData(tag: NBTTagCompound): Unit
}

trait NBTSavable {
	def saveNBTData(tag: NBTTagCompound): Unit
}

trait NBTReloadable extends NBTLoadable with NBTSavable
