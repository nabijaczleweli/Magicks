package com.nabijaczleweli.fancymagicks.util

import net.minecraftforge.common.config.Configuration

trait IConfigurable extends Any {
	/** Hack to provide `category"asdf:fdsa:m00"` syntax for `Configuration.CATEGORY_SPLITTER`.<br />
	  * Not a value class, since they can't be members.
	  */
	implicit protected final class CategoryHelper(val sc: StringContext) {
		def category(args: Any*) =
			sc.standardInterpolator(_.replaceAll(":", Configuration.CATEGORY_SPLITTER), args)
	}

	def configure(config: Configuration): Unit
}
