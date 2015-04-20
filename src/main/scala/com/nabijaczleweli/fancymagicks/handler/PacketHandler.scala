package com.nabijaczleweli.fancymagicks.handler

import java.lang.{Float => jFloat, Double => jDouble}
import java.lang.reflect.{Array => jArray, InvocationTargetException}

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.potion.Potion
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBISUtil
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.potion.PotionEffect

import scala.collection.immutable.HashMap
import scala.reflect.runtime.ReflectionUtils

object PacketHandler {
	private var commands = HashMap.empty withDefault {cmd: String => {_: ByteBufInputStream => Container.log warn s"Unknown packet command: '$cmd'!"}}

	commands += "apply-potion-effect" -> callable(Potion, "dispatchPotionEffect", classOf[PotionEffect], classOf[EntityLivingBase])
	commands += "spawn-entity-simple" -> callable(EntityUtil, "dispachSimpleSpawn", classOf[SimpleEntitySpawnData])
	commands += "deal-elemental-damage" -> callable(ElementalDamageSource, "dispatchDamage", classOf[ElementalDamageSource], classOf[Entity], classOf[jFloat])
	commands += "change-velocity" -> callable(EntityUtil, "dispatchVelocityChange", classOf[Entity], classOf[jDouble], classOf[jDouble], classOf[jDouble])

	@SubscribeEvent
	def onServerPacket(event: ServerCustomPacketEvent) {
		val bbis = new ByteBufInputStream(event.packet.payload)
		commands(bbis.readUTF())(bbis)
	}

	// UGLY REFLECTION CODE AHEAD
	// Abandon all hope, ye who enter here
	// Hic sunt dracones

	/** Not usable with primitives */
	private def loadedArgs(bbis: ByteBufInputStream, clazz: Class[_]*) = {
		val util = BBISUtil(bbis)
		clazz map {c =>
			if(classOf[Entity] isAssignableFrom c)
				classOf[Entity]
			else
				c
		} map {jArray.newInstance(_, 1).asInstanceOf[Array[AnyRef]]} map {a => classOf[BBISUtil].getMethod("$greater$greater", a.getClass) -> a} map {t =>
			try
				t._1.invoke(util, t._2)
			catch {
				case ite: InvocationTargetException =>
					throw ite.getTargetException
			}
			t._2
		}
	}

	/** Not usable with primitives */
	private def callWithArgs(clazz: String, methodName: String, args: AnyRef*) {
		val instance = ReflectionUtils.staticSingletonInstance(getClass.getClassLoader, clazz)
		val method = instance.getClass.getMethods filter {_.getName == methodName} find {m =>
			val partypes = m.getParameterTypes
			val types = args map {_.getClass}
			if(partypes.length != types.size)
				false
			else {
				var allass = true
				for(i <- 0 until partypes.length if allass)
					allass = (types(i) isAssignableFrom partypes(i)) || (partypes(i) isAssignableFrom types(i))
				allass
			}
		} getOrElse {throw new NoSuchMethodException}
		method.invoke(instance, args: _*)
	}

	/** Not usable with primitives */
	private def callable(instance: AnyRef, methodName: String, argTypes: Class[_]*)(bbis: ByteBufInputStream) {
		argTypes find {classOf[Entity].isAssignableFrom}
		callWithArgs(instance.getClass.getName, methodName, loadedArgs(bbis, argTypes: _*) map {_.head}: _*)
	}
}
