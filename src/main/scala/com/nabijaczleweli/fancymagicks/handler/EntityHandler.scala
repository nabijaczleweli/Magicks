package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertyElements, ExtendedPropertyPrevRotationPitch}
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingHurtEvent

object EntityHandler {
	@SubscribeEvent
	def onEntityConstructed(event: EntityConstructing) =
		event.entity match {
			case plr: EntityPlayer =>
				plr.registerExtendedProperties(ExtendedPropertyPrevRotationPitch.id, new ExtendedPropertyPrevRotationPitch)
				plr.registerExtendedProperties(ExtendedPropertyElements.id, new ExtendedPropertyElements)
			case _ =>
		}

	@SubscribeEvent
	def onEntityAttacked(event: LivingHurtEvent) = //TODO: element resistance with auras (when we get DamageSources set up)
		if(event.entityLiving isPotionActive Container.potionElementalResistance)
			event.ammount /= 4F // 75% resistance to all elements & physical damage
}
