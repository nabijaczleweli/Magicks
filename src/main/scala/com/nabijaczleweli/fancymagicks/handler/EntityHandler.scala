package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertyElements, ExtendedPropertyPrevRotationPitch}
import com.nabijaczleweli.fancymagicks.potion.PotionDeflectAura
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import cpw.mods.fml.common.ObfuscationReflectionHelper
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity._
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.{Potion => mPotion}
import net.minecraft.world.World
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
	def onLivingHurt(event: LivingHurtEvent) = //TODO: element resistance with auras (when we'll have DamageSources)
		if(event.entityLiving isPotionActive Container.potionImmunityDamage)
			event setCanceled true // It should be working, but it isn't, because all active potion effects are clientside-only
		else {
			if(event.entityLiving isPotionActive Container.potionElementalResistance) // 75% resistance to all elements & physical damage
				event.ammount /= 4F
		}

	@SubscribeEvent
	def onLivingUpdate(event: LivingUpdateEvent) = {
		if((event.entityLiving isPotionActive Container.potionPoisonImmunity) && (event.entityLiving isPotionActive mPotion.poison))
			event.entityLiving removePotionEffect mPotion.poison.getId
		if(event.entityLiving isPotionActive PotionDeflectAura) {
			// Not too happy with it, but it works, I guess...
			val range = math.min(math.max(event.entity.width, event.entity.height) * 2, World.MAX_ENTITY_RADIUS)
			val signX = math signum event.entityLiving.posX
			val signY = math signum event.entityLiving.posY
			val signZ = math signum event.entityLiving.posZ
			for(e <- EntityUtil.entitiesInRadius[IProjectile](event.entity, range)) {
				val distance = math sqrt (math.pow(event.entityLiving.posX - e.posX, 2) + math.pow(event.entityLiving.posY - e.posY, 2) + math.pow(event.entityLiving.posY - e.posY, 2))
				e.setThrowableHeading(e.motionX + (range - distance) * -signX, e.motionY + (range - distance) * -signY, e.motionZ + (range - distance) * -signZ, 1, 1)
			}
		}
	}

	@SubscribeEvent
	def onLivingTargeted(event: LivingSetAttackTargetEvent) =
		event.entityLiving match {
			case entity: EntityLiving if event.target != null && (event.target isPotionActive Container.potionLowerAttackChance) && ((rand nextInt 5) == 0) =>
				entity setAttackTarget null
				ObfuscationReflectionHelper.setPrivateValue(classOf[EntityLiving], entity, null, "currentTarget", "field_70776_bF")
			case _ =>
		}
}
