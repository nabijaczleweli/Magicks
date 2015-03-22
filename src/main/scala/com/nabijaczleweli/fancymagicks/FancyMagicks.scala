package com.nabijaczleweli.fancymagicks

import com.nabijaczleweli.fancymagicks.proxy.{CommonProxy, IProxy}
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.reference.{Configuration, Container, StaffTypeRegistry}
import com.nabijaczleweli.fancymagicks.util.ReflectionUtil
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}

import scala.collection.JavaConversions._

@Mod(modid = MOD_ID, name = MOD_NAME, dependencies = DEPENDENCIES, version = VERSION, modLanguage = "scala")
object FancyMagicks {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = _

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		ReflectionUtil.setFieldInInstance(Container, "log", event.getModLog)
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
		proxy.registerStaffAbilities()

		StaffTypeRegistry.readAllStaffTypes()

		proxy.registerRenderers()
	}

	@EventHandler
	def processIMCs(event: IMCEvent) =
		for(message <- event.getMessages)
			message.key match {
				case "register-staff-ability" if message.isNBTMessage =>
					val tag = message.getNBTValue
					val key = tag getString "key"
					val className = tag getString "className"
					val tooltip = tag getString "tooltip"

					if(key == "" || className == "" || tooltip == "")
						Container.log warn s"IMC message from ${message.getSender} with key ${message.key} is invalid! Sent: key='$key' className='$className' tooltip='$tooltip'."
					else
						CommonProxy.IMCAbilities :+= key -> (className -> tooltip)
			}
}
