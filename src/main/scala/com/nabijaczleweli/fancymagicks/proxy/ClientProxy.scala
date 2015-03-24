package com.nabijaczleweli.fancymagicks.proxy

import com.nabijaczleweli.fancymagicks.entity.EntityBugs
import com.nabijaczleweli.fancymagicks.handler.KeyHandler
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.render.entity.RenderBugs
import com.nabijaczleweli.fancymagicks.render.item.ItemStaffRenderer
import cpw.mods.fml.client.registry.{RenderingRegistry, ClientRegistry}
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.client.MinecraftForgeClient

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	override def registerKeyBindings() {
		ClientRegistry registerKeyBinding Container.keyBindStaffSpecialAblility
	}

	override def registerHandlers() {
		super.registerHandlers()
		FMLCommonHandler.instance.bus register KeyHandler
	}

	override def registerRenderers() {
		super.registerRenderers()
		MinecraftForgeClient.registerItemRenderer(ItemStaff, ItemStaffRenderer)
		RenderingRegistry.registerEntityRenderingHandler(classOf[EntityBugs], RenderBugs)
	}
}
