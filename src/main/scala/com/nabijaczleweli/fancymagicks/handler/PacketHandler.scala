package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBISUtil
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

object PacketHandler {
	@SubscribeEvent
	def onServerPacket(event: ServerCustomPacketEvent) = {
		val bbis = new ByteBufInputStream(event.packet.payload)
		bbis.readUTF() match {
			case "apply-potion-effect" =>
				val ent = new Array[EntityLivingBase](1)
				val eff = new Array[PotionEffect](1)

				bbis >> ent >> eff

				ent(0) addPotionEffect eff(0)
			case "spawn-entity-simple" =>
				val sesd = new Array[SimpleEntitySpawnData](1)

				bbis >> sesd

				sesd(0).spawn()
			case command =>
				Container.log warn s"Unknown packet command: '$command'!"
		}
	}
}
