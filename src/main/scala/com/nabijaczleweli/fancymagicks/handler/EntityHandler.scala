package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertyElements, ExtendedPropertyPrevRotationPitch}
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.common.ObfuscationReflectionHelper
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{EntityCreature, EntityLiving}
import net.minecraft.potion.{Potion => mPotion}
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.{LivingHurtEvent, LivingSetAttackTargetEvent, LivingSpawnEvent}

import scala.util.Random

object EntityHandler {
	private val rand = new Random

	@SubscribeEvent
	def onEntityConstructed(event: EntityConstructing) =
		event.entity match {
			case plr: EntityPlayer =>
				plr.registerExtendedProperties(ExtendedPropertyPrevRotationPitch.id, new ExtendedPropertyPrevRotationPitch)
				plr.registerExtendedProperties(ExtendedPropertyElements.id, new ExtendedPropertyElements)
			case _ =>
		}

	@SubscribeEvent
	def onEntitySpawned(event: LivingSpawnEvent) =
		event.entity match {
			case creature: EntityCreature =>
				creature.targetTasks.addTask(2, new EntityAINearestAttackableTarget(creature, classOf[IMob], 0, false, false, IMob.mobSelector) {
					override def shouldExecute =
						(taskOwner isPotionActive Container.potionCharm) && super.shouldExecute

					override def updateTask() = {
						if(!(taskOwner isPotionActive Container.potionCharm))
							resetTask()
						super.updateTask()
					}

					override def continueExecuting() =
						(taskOwner isPotionActive Container.potionCharm) && super.continueExecuting()
				})
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

	@SubscribeEvent
	def onLivingTargeted(event: LivingSetAttackTargetEvent) =
		event.entityLiving match {
			case entity: EntityLiving if event.target != null && (event.target isPotionActive Container.potionLowerAttackChance) && ((rand nextInt 5) == 0) =>
				entity setAttackTarget null
				ObfuscationReflectionHelper.setPrivateValue(classOf[EntityLiving], entity, null, "currentTarget", "field_70776_bF")
			case _ =>
		}
}
