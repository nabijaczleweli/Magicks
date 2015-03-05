package com.nabijaczleweli.fancymagicks

import com.nabijaczleweli.fancymagicks.proxy.IProxy
import com.nabijaczleweli.fancymagicks.reference.Configuration
import com.nabijaczleweli.fancymagicks.reference.Reference._
import cpw.mods.fml.common.{SidedProxy, Mod}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLInitializationEvent, FMLPostInitializationEvent}

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

	}
}
