package com.nabijaczleweli.fancymagicks.creativetab

import com.nabijaczleweli.fancymagicks.item.ItemStaff
import net.minecraft.creativetab.CreativeTabs

object CreativeTabFancyMagicks extends CreativeTabs("fancymagicks") {
	override def getTabIconItem =
		ItemStaff
}
