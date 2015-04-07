package com.nabijaczleweli.fancymagicks.util

import com.nabijaczleweli.fancymagicks.element.{Element, ElementalDamageSource}
import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.buffer.{ByteBufInputStream, Unpooled, ByteBufOutputStream}
import net.minecraft.entity.Entity
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer

import scala.reflect.runtime.ReflectionUtils

object PacketUtil {
	implicit class BBOSUtil(val bbos: ByteBufOutputStream) extends AnyVal {
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

		def <<(eds: ElementalDamageSource): ByteBufOutputStream = {
			<<(eds.getEntity)
			bbos writeInt eds.elements.size
			for(element <- eds.elements)
				bbos writeUTF element.getClass.getName
			bbos
		}

		def <<(f: Float) = {
			bbos writeFloat f
			bbos
		}
	}

	implicit class BBISUtil(val bbis: ByteBufInputStream) extends AnyVal {
		/** @param ent Array of one element, used as a C++-style reference
		  * @tparam T Type of element, the entity type (from World) needs to be assignable to it. Also uses cheeky code to force-feed the compiler cases, where `!(T =:= Entity)`, e.g. `T =:= EntityLivingBase`
		  */
		def >>[T](ent: Array[T])(implicit ev: Entity => T = {e: Entity => e.asInstanceOf[T]}) = {
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

		/** @param eds Array of one element, used as a C++-style reference */
		def >>(eds: Array[ElementalDamageSource]): ByteBufInputStream = {
			val entity = new Array[Entity](1)
			>>(entity)
			val amount = bbis.readInt()
			val elements = (0 until amount) map {_ => bbis.readUTF()} map Class.forName map ReflectionUtils.staticSingletonInstance map {_.asInstanceOf[Element]}

			eds(0) = new ElementalDamageSource(entity(0), elements)

			bbis
		}

		/** @param f Array of one element, used as a C++-style reference */
		def >>(f: Array[Float]) = {
			val value = bbis.readFloat()

			f(0) = value

			bbis
		}
	}

	def packet(bbos: ByteBufOutputStream) =
		new FMLProxyPacket(bbos.buffer, Reference.MOD_ID)

	def stream =
		new ByteBufOutputStream(Unpooled.buffer)
}
