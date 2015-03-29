package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertyElements, ExtendedPropertyPrevRotationPitch}
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.{Potion => mPotion}
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
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
	def onLivingHurt(event: LivingHurtEvent) = //TODO: element resistance with auras (when we get DamageSources set up)
		if(event.entityLiving isPotionActive Container.potionImmunityDamage)
			event setCanceled true // It should be working, but it isn't... Weird... The entity never reports having Container.potionImmunityDamage on itself here
		else {
			if(event.entityLiving isPotionActive Container.potionElementalResistance) // 75% resistance to all elements & physical damage
				event.ammount /= 4F
		}

	@SubscribeEvent
	def onLivingUpdate(event: LivingUpdateEvent) =
		if((event.entityLiving isPotionActive Container.potionPoisonImmunity) && (event.entityLiving isPotionActive mPotion.poison))
			event.entityLiving removePotionEffect mPotion.poison.getId
}
