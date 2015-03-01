package com.nabijaczleweli.fancymagicks.item

import com.nabijaczleweli.fancymagicks.creativetab.CreativeTabFancyMagicks
import com.nabijaczleweli.fancymagicks.reference.Reference
import net.minecraft.item.Item

object ItemStaff extends Item {
	setCreativeTab(CreativeTabFancyMagicks)
	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "staff")
	setTextureName(Reference.NAMESPACED_PREFIX + "missing_staff")
	setHasSubtypes(true)
	setMaxDamage(0)
}
