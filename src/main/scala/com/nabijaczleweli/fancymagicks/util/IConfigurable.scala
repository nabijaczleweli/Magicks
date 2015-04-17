package com.nabijaczleweli.fancymagicks.util

import net.minecraftforge.common.config.Configuration

trait IConfigurable extends Any {
	def configure(config: Configuration): Unit
}
