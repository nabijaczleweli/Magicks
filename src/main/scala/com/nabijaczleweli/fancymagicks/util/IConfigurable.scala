package com.nabijaczleweli.fancymagicks.util

import net.minecraftforge.common.config.Configuration

trait IConfigurable {
	def configure(config: Configuration): Unit
}
