package com.nabijaczleweli.fancymagicks.handler

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

object PacketHandler {
	@SubscribeEvent
	def onServerPacket(event: ServerCustomPacketEvent) = { //TODO: Maybe make this a delegate to a `Map[String, ByteBufInputStream => Unit]`?
		val bbis = new ByteBufInputStream(event.packet.payload)
		bbis.readUTF() match {
			case "apply-potion-effect" =>
				val ent = new Array[EntityLivingBase](1)
				val eff = new Array[PotionEffect](1)

				bbis >> ent >> eff

				Potion.dispatchPotionEffect(eff.head, ent.head)
			case "spawn-entity-simple" =>
				val sesd = new Array[SimpleEntitySpawnData](1)

				bbis >> sesd

				EntityUtil dispachSimpleSpawn sesd.head
			case "deal-elemental-damage" =>
				val eds = new Array[ElementalDamageSource](1)
				val attackee = new Array[Entity](1)
				val amt = new Array[Float](1)

				bbis >> eds >> attackee >> amt

				ElementalDamageSource.dispatchDamage(eds.head, attackee.head, amt.head)
			case "change-velocity" =>
				val ent = new Array[Entity](1)
				val xChange = new Array[Double](1)
				val yChange = new Array[Double](1)
				val zChange = new Array[Double](1)

				bbis >> ent >> xChange >> yChange >> zChange

				EntityUtil.dispatchVelocityChange(ent.head, xChange.head, yChange.head, zChange.head)
			case command =>
				Container.log warn s"Unknown packet command: '$command'!"
		}
	}
}
