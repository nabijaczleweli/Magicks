package com.nabijaczleweli.fancymagicks.reference

import java.io.File

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertyElements
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.potion.{PotionDamageAura, PotionDeflectAura, PotionImmunityAura}
import com.nabijaczleweli.fancymagicks.render.entity.ModelBugs
import net.minecraftforge.common.config.Configuration

object Configuration {
	val configurables = ItemStaff :: ExtendedPropertyElements :: ModelBugs :: PotionImmunityAura :: PotionDamageAura :: PotionDeflectAura :: ElementalDamageSource :: Nil
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
