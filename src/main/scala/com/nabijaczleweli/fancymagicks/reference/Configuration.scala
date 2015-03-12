package com.nabijaczleweli.fancymagicks.reference

import java.io.File

import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertyElements
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import net.minecraftforge.common.config.Configuration

object Configuration {
	val configurables = ItemStaff :: ExtendedPropertyElements :: Nil
	var config: Configuration = _

	def load(file: File) {
		config = new Configuration(file)
		configurables foreach {_ configure config}
		saveIfNeeded()
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()
}
