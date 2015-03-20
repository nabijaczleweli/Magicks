package com.nabijaczleweli.fancymagicks

import com.nabijaczleweli.fancymagicks.proxy.IProxy
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.reference.{Configuration, StaffTypeRegistry}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}

@Mod(modid = MOD_ID, name = MOD_NAME, dependencies = DEPENDENCIES, version = VERSION, modLanguage = "scala")
object FancyMagicks {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = _

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		Configuration load event.getSuggestedConfigurationFile

		proxy.registerItemsAndBlocks()
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		proxy.registerKeyBindings()
		proxy.registerHandlers()
	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {
		StaffTypeRegistry.readAllStaffTypes()

		proxy.registerRenderers()
	}
}
