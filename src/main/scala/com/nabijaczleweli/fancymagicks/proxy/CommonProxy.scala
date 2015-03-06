package com.nabijaczleweli.fancymagicks.proxy

import com.nabijaczleweli.fancymagicks.handler.EntityHandler
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge

class CommonProxy extends IProxy {
	override def registerItemsAndBlocks() {
		@inline
		def defaultRegisterItem(it: Item) =
			GameRegistry.registerItem(it, it.getUnlocalizedName.substring(it.getUnlocalizedName.indexOf(':') + 1))
		@inline
		def defaultRegisterBlock(bl: Block) =
			GameRegistry.registerBlock(bl, bl.getUnlocalizedName.substring(bl.getUnlocalizedName.indexOf(':') + 1))

		defaultRegisterItem(ItemStaff)
	}

	override def registerKeyBindings() {}

	override def registerHandlers() {
		MinecraftForge.EVENT_BUS register EntityHandler
	}
}