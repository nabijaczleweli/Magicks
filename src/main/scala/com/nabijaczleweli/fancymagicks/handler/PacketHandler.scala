package com.nabijaczleweli.fancymagicks.handler

import java.io.{InputStream, DataInput, ObjectInputStream}
import java.lang.reflect.{Array => jArray, Method}
import java.lang.{Double => jDouble, Float => jFloat}
import java.util.zip.GZIPInputStream

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.potion.Potion
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.{ElementalThrowableEntitySpawnData, SimpleEntitySpawnData}
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBISUtil
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.potion.PotionEffect

import scala.collection.immutable.HashMap
import scala.collection.mutable.{HashMap => mHashMap}
import scala.reflect.runtime.ReflectionUtils

object PacketHandler {
	private var commands = HashMap.empty withDefault {cmd: String => {_: DataInput with InputStream => Container.log warn s"Unknown packet command: '$cmd'!"}}
	private val loadedArgsCache = mHashMap.empty[Class[_], Method]
	private val superclassed = Set(classOf[Entity])

	commands += "apply-potion-effect" -> callable(Potion, "dispatchPotionEffect", classOf[PotionEffect], classOf[EntityLivingBase])
	commands += "spawn-entity-simple" -> callable(EntityUtil, "dispatchSimpleSpawn", classOf[SimpleEntitySpawnData])
	commands += "spawn-entity-elemental-throwable" -> callable(EntityUtil, "dispatchElementalThrowableSpawn", classOf[ElementalThrowableEntitySpawnData])
	commands += "deal-elemental-damage" -> callable(ElementalDamageSource, "dispatchDamage", classOf[ElementalDamageSource], classOf[Entity], classOf[jFloat])
	commands += "change-velocity" -> callable(EntityUtil, "dispatchVelocityChange", classOf[Entity], classOf[jDouble], classOf[jDouble], classOf[jDouble])

	@SubscribeEvent
	def onServerPacket(event: ServerCustomPacketEvent) {
		val bbis = new ObjectInputStream(new GZIPInputStream(new ByteBufInputStream(event.packet.payload)))
		commands(bbis.readUTF())(bbis)
	}

	// UGLY REFLECTION CODE AHEAD
	// Abandon all hope, ye who enter here
	// Hic sunt dracones

	/** Not usable with primitives */
	private def wrapInArray(size: Int, types: Class[_]*) =
		types map {jArray.newInstance(_, 1)} map {_.asInstanceOf[Array[AnyRef]]}

	/** Not usable with primitives */
	private def loadedArgs(bbis: DataInput with InputStream, types: Class[_]*) = {
		val util = BBISUtil(bbis)
		val wrapped = wrapInArray(1, types map {c => superclassed find {_ isAssignableFrom c} getOrElse c}: _*)
		val wrappedTypes = wrapped map {_.getClass}
		val cachedMethods = wrappedTypes map {t => loadedArgsCache.getOrElseUpdate(t, classOf[BBISUtil].getMethod("$greater$greater", t))}
		for((m, a) <- cachedMethods zip wrapped)
			m.invoke(util, a)
		wrapped
	}

	/** Not usable with primitives */
	private def methodWithTypes(clazz: String, methodName: String, argTypes: Class[_]*) =
		ReflectionUtils.staticSingletonInstance(getClass.getClassLoader, clazz).getClass.getMethods filter {_.getName == methodName} find {m =>
			val partypes = m.getParameterTypes
			if(partypes.length != argTypes.size)
				false
			else { // Check if every arg is convertible to its counterpart or vice-versa
				var allass = true
				for(i <- 0 until partypes.length if allass)
					allass = (argTypes(i) isAssignableFrom partypes(i)) || (partypes(i) isAssignableFrom argTypes(i))
				allass
			}
		} getOrElse {throw new NoSuchMethodException(s"'$clazz.$methodName' is not callable with arguments of types: ${argTypes.mkString("[", ", ", "]")}")}

	/** Not usable with primitives */
	private def callable(instance: AnyRef, methodName: String, argTypes: Class[_]*): DataInput with InputStream => Unit = {
		val method = methodWithTypes(instance.getClass.getName, methodName, argTypes: _*);
		{bbis => method.invoke(instance, loadedArgs(bbis, argTypes: _*) map {_.head}: _*)}
	}
}
