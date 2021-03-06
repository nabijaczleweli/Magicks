package com.nabijaczleweli.fancymagicks.util

import java.io._
import java.lang.{Double => jDouble, Float => jFloat}
import java.util.zip.GZIPOutputStream

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.EntityUtil.{ElementalThrowableEntitySpawnData, SimpleEntitySpawnData}
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.buffer.{ByteBufOutputStream, Unpooled}
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer

import scala.reflect.runtime.ReflectionUtils

object PacketUtil {
	implicit class BBOSUtil(val bbos: DataOutput with OutputStream) extends AnyVal {
		type StreamType = DataOutput with OutputStream

		def <<(str: String) = {
			bbos writeUTF str
			bbos
		}

		def <<(entity: Entity) = {
			bbos writeInt entity.worldObj.provider.dimensionId
			bbos writeInt entity.getEntityId
			bbos
		}

		def <<(eff: PotionEffect) = {
			bbos writeInt eff.getPotionID
			bbos writeInt eff.getDuration
			bbos writeInt eff.getAmplifier
			bbos writeBoolean eff.getIsAmbient
			// Doesn't handle curative items, as it's not yet needed.
			bbos
		}

		def <<(sesd: SimpleEntitySpawnData) = {
			bbos writeUTF sesd.entityClass.getName
			bbos writeInt sesd.world.provider.dimensionId
			bbos writeDouble sesd.x
			bbos writeDouble sesd.y
			bbos writeDouble sesd.z
			bbos
		}

		def <<(etesd: ElementalThrowableEntitySpawnData): StreamType = {
			bbos writeUTF etesd.entityClass.getName
			<<(etesd.thrower)
			<<(etesd.elems)
			bbos
		}

		def <<(elems: Seq[Element]) = {
			bbos writeInt elems.size
			for(elem <- elems)
				bbos writeUTF elem.getClass.getName
			bbos
		}

		def <<(eds: ElementalDamageSource): StreamType = {
			<<(eds.getEntity)
			<<(eds.elements)
			bbos
		}

		def <<(f: Float) = {
			bbos writeFloat f
			bbos
		}

		def <<(d: Double) = {
			bbos writeDouble d
			bbos
		}
	}

	implicit class BBISUtil(val bbis: DataInput with InputStream) extends AnyVal {
		type StreamType = DataInput with InputStream

		/** @param ent Array of one element, used as a C++-style reference */
		def >>(ent: Array[Entity]) = {
			val dimensionId = bbis.readInt()
			val entityId = bbis.readInt()

			ent(0) = MinecraftServer.getServer worldServerForDimension dimensionId getEntityByID entityId

			bbis
		}

		/** @param eff Array of one element, used as a C++-style reference */
		def >>(eff: Array[PotionEffect]) = {
			val potionId = bbis.readInt()
			val duration = bbis.readInt()
			val amplifier = bbis.readInt()
			val ambient = bbis.readBoolean()

			eff(0) = new PotionEffect(potionId, duration, amplifier, ambient)
			// Doesn't handle curative items, as it's not yet needed.

			bbis
		}

		/** @param sesd Array of one element, used as a C++-style reference */
		def >>(sesd: Array[SimpleEntitySpawnData]) = {
			val className = bbis.readUTF()
			val dimensionId = bbis.readInt()
			val xCoord = bbis.readDouble()
			val yCoord = bbis.readDouble()
			val zCoord = bbis.readDouble()

			sesd(0) = SimpleEntitySpawnData(Class forName className asSubclass classOf[Entity], MinecraftServer.getServer worldServerForDimension dimensionId, xCoord, yCoord, zCoord)

			bbis
		}

		/** @param etesd Array of one element, used as a C++-style reference */
		def >>(etesd: Array[ElementalThrowableEntitySpawnData]): StreamType = {
			val className = bbis.readUTF()
			val thrower = new Array[Entity](1)
			>>(thrower)
			val elems = new Array[Seq[Element]](1)
			>>(elems)

			etesd(0) = ElementalThrowableEntitySpawnData((Class forName className asSubclass classOf[EntityThrowable]).asInstanceOf[Class[EntityThrowable with IElemental]],
			                                             thrower.head.asInstanceOf[EntityLivingBase], elems.head)

			bbis
		}

		/** @param elems Array of one element, used as a C++-style reference */
		def >>(elems: Array[Seq[Element]]) = {
			val amount = bbis.readInt()
			val elements = (0 until amount) map {_ => bbis.readUTF()} map Class.forName map ReflectionUtils.staticSingletonInstance map {_.asInstanceOf[Element]}

			elems(0) = elements

			bbis
		}

		/** @param eds Array of one element, used as a C++-style reference */
		def >>(eds: Array[ElementalDamageSource]): StreamType = {
			val entity = new Array[Entity](1)
			>>(entity)
			val elements = new Array[Seq[Element]](1)
			>>(elements)

			eds(0) = new ElementalDamageSource(entity(0), elements.head)

			bbis
		}

		/** @param f Array of one element, used as a C++-style reference */
		def >>(f: Array[Float]) = {
			val value = bbis.readFloat()

			f(0) = value

			bbis
		}

		/** @param f Array of one element, used as a C++-style reference */
		def >>(f: Array[jFloat]) = {
			val value = bbis.readFloat()

			f(0) = value

			bbis
		}

		/** @param d Array of one element, used as a C++-style reference */
		def >>(d: Array[Double]) = {
			val value = bbis.readDouble()

			d(0) = value

			bbis
		}

		/** @param d Array of one element, used as a C++-style reference */
		def >>(d: Array[jDouble]) = {
			val value = bbis.readDouble()

			d(0) = value

			bbis
		}
	}

	private lazy val oosBout = reflect ensureAccessible (classOf[ObjectOutputStream] getDeclaredField "bout")
	private lazy val bdosOut = reflect ensureAccessible (oosBout.getType getDeclaredField "out")
	private lazy val fosOut = reflect ensureAccessible (classOf[FilterOutputStream] getDeclaredField "out")

	/** ONLY FEED THIS FUNCTION RETURN VALUE OF `stream`! RELIES HEAVILY ON REFLECTION! */
	def packet(doos: DataOutput with OutputStream) = {
		val bbos = (fosOut get (bdosOut get (oosBout get doos))).asInstanceOf[ByteBufOutputStream]
		doos.close()

		// Strip trailing 0s from default/over-allocation
		val revIdx0 = bbos.buffer.array.reverse indexWhere {_ != 0}
		new FMLProxyPacket(bbos.buffer capacity bbos.buffer.capacity - revIdx0, Reference.MOD_ID)
	}

	def stream =
		new ObjectOutputStream(new GZIPOutputStream(new ByteBufOutputStream(Unpooled.buffer)))
}
