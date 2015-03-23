package com.nabijaczleweli.fancymagicks.proxy

import com.nabijaczleweli.fancymagicks.handler.EntityHandler
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.staves.AbilitySimple
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.potion.PotionEffect
import net.minecraftforge.common.MinecraftForge

class CommonProxy extends IProxy {
	override def registerItemsAndBlocks() {
		@inline
		def defaultRegisterItem(it: Item) =
			GameRegistry.registerItem(it, it.getUnlocalizedName.substring(it.getUnlocalizedName.indexOf(':') + 1))
		@inline
		def defaultRegisterBlock(bl: Block) =
			GameRegistry.registerBlock(bl, bl.getUnlocalizedName.substring(bl.getUnlocalizedName.indexOf(':') + 1))

		defaultRegisterItem(ItemStaff)
	}

	override def registerKeyBindings() {}

	override def registerHandlers() {
		MinecraftForge.EVENT_BUS register EntityHandler
	}

	override def registerRenderers() {}

	override def registerStaffAbilities() {
		Container.abilityRegistry += "fancymagicks:none" -> new AbilitySimple({_ => ()}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityNone")
		Container.abilityRegistry += "fancymagicks:elementalResistance" -> new AbilitySimple({p => p.addPotionEffect(new PotionEffect(Container.potionElementalResistance.getId, 1, 0, false))},
		                                                                                     s"tooltip.${NAMESPACED_PREFIX}staffAbilityElementalResitance")

		Container.abilityRegistry ++= CommonProxy.IMCAbilities map {t => (t._1, ((Class forName t._2._1).newInstance.asInstanceOf[EntityPlayer => Unit], t._2._2))} map {t => (t._1, new AbilitySimple(t._2._1, t._2._2))}
	}
}

object CommonProxy {
	var IMCAbilities: Seq[(String, (String, String))] = Nil
}
