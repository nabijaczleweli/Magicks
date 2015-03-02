package com.nabijaczleweli.fancymagicks.proxy

import com.nabijaczleweli.fancymagicks.handler.KeyHandler
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	override def registerKeyBindings() {
		ClientRegistry registerKeyBinding Container.keyBindStaffSpecialAblility
	}

	override def registerHandlers() {
		super.registerHandlers()
		FMLCommonHandler.instance.bus register KeyHandler
	}
}
