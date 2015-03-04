package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertySelectionDirection, ExtendedPropertyPrevRotationPitch}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing

object EntityHandler {
	@SubscribeEvent
	def onEntityConstructed(event: EntityConstructing) =
		event.entity match {
			case plr: EntityPlayer =>
				plr.registerExtendedProperties(ExtendedPropertyPrevRotationPitch.id, new ExtendedPropertyPrevRotationPitch)
				plr.registerExtendedProperties(ExtendedPropertySelectionDirection.id, new ExtendedPropertySelectionDirection)
			case _ =>
		}
}
