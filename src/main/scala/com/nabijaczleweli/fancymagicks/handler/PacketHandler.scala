package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBISUtil
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.{Entity, EntityLivingBase}
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

				if(ent(0) != null) // The entity might be dead
					ent(0) addPotionEffect eff(0)
			case "spawn-entity-simple" =>
				val sesd = new Array[SimpleEntitySpawnData](1)

				bbis >> sesd

				sesd(0).spawn()
			case "deal-elemental-damage" =>
				val eds = new Array[ElementalDamageSource](1)
				val attackee = new Array[Entity](1)
				val amt = new Array[Float](1)

				bbis >> eds >> attackee >> amt

				if(attackee(0) != null) // The entity might've already died at this point
					attackee(0).attackEntityFrom(eds(0), amt(0))
			case "change-velocity" =>
				val ent = new Array[Entity](1)
				val xChange = new Array[Double](1)
				val yChange = new Array[Double](1)
				val zChange = new Array[Double](1)

				bbis >> ent >> xChange >> yChange >> zChange

				if(ent(0) != null)
					ent(0).addVelocity(xChange(0), yChange(0), zChange(0))
			case command =>
				Container.log warn s"Unknown packet command: '$command'!"
		}
	}
}
