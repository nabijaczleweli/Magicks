package com.nabijaczleweli.fancymagicks.creativetab

import java.util.{ArrayList => jArrayList}

import com.nabijaczleweli.fancymagicks.item.ItemStaff
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

import scala.util.Random

object CreativeTabFancyMagicks extends CreativeTabs("fancymagicks") {
	@SideOnly(Side.CLIENT)
	private var stack: ItemStack = _

	@SideOnly(Side.CLIENT)
	override val getTabIconItem = ItemStaff

	@SideOnly(Side.CLIENT)
	override def getIconItemStack =
		stack match {
			case null =>
				val lst = new jArrayList[ItemStack]
				ItemStaff.getSubItems(ItemStaff, this, lst)
				if(!lst.isEmpty) {
					stack = lst get (new Random nextInt lst.size)
					stack
				} else
					super.getIconItemStack
			case is =>
				is
		}
}
