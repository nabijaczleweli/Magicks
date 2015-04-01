package com.nabijaczleweli.fancymagicks.proxy

import com.nabijaczleweli.fancymagicks.FancyMagicks
import com.nabijaczleweli.fancymagicks.element.{ElementCold, ElementLife, ElementLightning}
import com.nabijaczleweli.fancymagicks.entity.{EntityBugs, EntitySpiritTree}
import com.nabijaczleweli.fancymagicks.handler.EntityHandler
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.potion.{Potion, PotionDamageAura, PotionImmunityAura}
import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.staves.AbilitySimple
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import cpw.mods.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.potion.{Potion => mPotion}
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

	override def registerEntities() {
		var _id = 0
		def id = {_id += 1; _id - 1}

		EntityRegistry.registerModEntity(classOf[EntityBugs], "bugs", id, FancyMagicks, 32, 5, true) // Last 3 arguments stolen from SlimeKinghts
		EntityRegistry.registerModEntity(classOf[EntitySpiritTree], "spiritTree", id, FancyMagicks, 32, 5, true) // Last 3 arguments stolen from SlimeKinghts
	}

	override def registerKeyBindings() {}

	override def registerHandlers() {
		MinecraftForge.EVENT_BUS register EntityHandler
	}

	override def registerRenderers() {}

	override def registerStaffAbilities() {
		Container.abilityRegistry += "fancymagicks:none" -> new AbilitySimple({_ => ()}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityNone")
		Container.abilityRegistry += "fancymagicks:elementalResistance" -> new AbilitySimple({Potion applyEffect Container.potionElementalResistance}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityElementalResitance")
		Container.abilityRegistry += "fancymagicks:summonBugs" -> new AbilitySimple(EntityBugs.defaultSummon, s"tooltip.${NAMESPACED_PREFIX}staffAbilitySummonBugs")
		Container.abilityRegistry += "fancymagicks:unlimitedBeams" -> new AbilitySimple({Potion applyEffect Container.potionUnlimitedBeams}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityUnlimitedBeams")
		Container.abilityRegistry += "fancymagicks:auraImmunityCold" -> new AbilitySimple({Potion.applyEffect(PotionImmunityAura(ElementCold), 10)}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityAuraImmunityCold")
		Container.abilityRegistry += "fancymagicks:immunityPoison" -> new AbilitySimple({Potion applyEffect Container.potionPoisonImmunity}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityImmunityPoison")
		Container.abilityRegistry += "fancymagicks:summonSpiritTree" -> new AbilitySimple(EntitySpiritTree.defaultSummon, s"tooltip.${NAMESPACED_PREFIX}staffAbilitySummonSpiritTree")
		Container.abilityRegistry += "fancymagicks:resistancePhysical" -> new AbilitySimple({Potion.applyEffect(mPotion.resistance, 2)}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityResistancePhysical")
		Container.abilityRegistry += "fancymagicks:immunityDamage" -> new AbilitySimple({Potion.applyEffect(Container.potionImmunityDamage, 0, 50)}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityImmunityDamage")
		Container.abilityRegistry += "fancymagicks:auraImmunityLightning" -> new AbilitySimple({Potion.applyEffect(PotionImmunityAura(ElementLightning), 10)},
		                                                                                       s"tooltip.${NAMESPACED_PREFIX}staffAbilityAuraImmunityLightning")
		Container.abilityRegistry += "fancymagicks:auraDamageLightning" -> new AbilitySimple({Potion.applyEffect(PotionDamageAura(ElementLightning), 18, 500)},
		                                                                                     s"tooltip.${NAMESPACED_PREFIX}staffAbilityAuraDamageLightning")
		Container.abilityRegistry += "fancymagicks:teleportRandom" -> new AbilitySimple({p => p mountEntity null; EntityUtil teleportRandomly p}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityTeleportRandom")
		Container.abilityRegistry += "fancymagicks:lowerAttackChance" -> new AbilitySimple(Potion applyEffect Container.potionLowerAttackChance, s"tooltip.${NAMESPACED_PREFIX}staffAbilityLowerAttackChance")
		Container.abilityRegistry += "fancymagicks:charm" -> new AbilitySimple({_ => Option(Minecraft.getMinecraft.pointedEntity) filter {_.isInstanceOf[EntityLivingBase]} map {_.asInstanceOf[EntityLivingBase]} foreach
		                                                                                                                                 Potion.applyEffect(Container.potionCharm, 0, 200)},
		                                                                       s"tooltip.${NAMESPACED_PREFIX}staffAbilityCharm")
		Container.abilityRegistry += "fancymagicks:resistanceLife" -> new AbilitySimple(Potion applyEffect PotionImmunityAura(ElementLife), s"tooltip.${NAMESPACED_PREFIX}staffAbilityResistanceLife")
		Container.abilityRegistry += "fancymagicks:sprayPoison" -> new AbilitySimple({p => EntityUtil.filterForFrustrum(EntityUtil.entitiesInRadius[EntityLivingBase](p, 20), EntityUtil frustrumFor p) foreach
		                                                                                                       Potion.applyEffect(mPotion.poison, 1, 50)}, s"tooltip.${NAMESPACED_PREFIX}staffAbilitySprayPoison")

		Container.abilityRegistry ++= CommonProxy.IMCAbilities map {t => (t._1, ((Class forName t._2._1).newInstance.asInstanceOf[EntityPlayer => Unit], t._2._2))} map {t => (t._1, new AbilitySimple(t._2._1, t._2._2))}
	}
}

object CommonProxy {
	var IMCAbilities: Seq[(String, (String, String))] = Nil
}
