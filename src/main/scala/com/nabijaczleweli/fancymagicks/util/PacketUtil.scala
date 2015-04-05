package com.nabijaczleweli.fancymagicks.util

import com.nabijaczleweli.fancymagicks.reference.Reference
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.buffer.{ByteBufInputStream, Unpooled, ByteBufOutputStream}
import net.minecraft.entity.Entity
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer

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

			bbis
		}
	}

	def packet(bbos: ByteBufOutputStream) =
		new FMLProxyPacket(bbos.buffer, Reference.MOD_ID)

	def stream =
		new ByteBufOutputStream(Unpooled.buffer)
}
