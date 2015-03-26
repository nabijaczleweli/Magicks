package com.nabijaczleweli.fancymagicks.creativetab

import java.util.{ArrayList => jArrayList}

import com.nabijaczleweli.fancymagicks.item.ItemStaff
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

import scala.util.Random

object CreativeTabFancyMagicks extends CreativeTabs("fancymagicks") {
	@SideOnly(Side.CLIENT)
	private val stack = new Array[ItemStack](1)

	@SideOnly(Side.CLIENT)
	override def getTabIconItem =
		ItemStaff

	@SideOnly(Side.CLIENT)
	override def getIconItemStack =
		stack(0) match {
			case null =>
				val lst = new jArrayList[ItemStack]
				ItemStaff.getSubItems(ItemStaff, this, lst)
				if(!lst.isEmpty) {
					stack(0) = lst get (new Random nextInt lst.size)
					stack(0)
				} else
					super.getIconItemStack
			case is =>
				is
		}
}
