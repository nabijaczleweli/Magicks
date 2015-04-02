package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.potion.Potion
import com.nabijaczleweli.fancymagicks.staves.StaffAbility
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.settings.KeyBinding
import net.minecraft.potion.{Potion => mPotion}
import org.apache.logging.log4j.LogManager

import scala.collection.immutable.HashMap

object Container {
	val log = LogManager.getRootLogger // Set by the mod instance in PreInit; needs not to be lazy (reflection)

	@SideOnly(Side.CLIENT)
	val keyBindStaffSpecialAblility = new KeyBinding(s"key.${Reference.NAMESPACED_PREFIX}staffSpecial", 48, Reference.MOD_NAME) // 48 -> B

	var abilityRegistry: Map[String, StaffAbility] = HashMap.empty

	val potionElementalResistance = new Potion(false, 0xC2C22C) setIconIndex mPotion.resistance.getStatusIconIndex setPotionName s"potion.${Reference.NAMESPACED_PREFIX}resistanceElemental"
	val potionUnlimitedBeams = new Potion(false, 0xEEDD05) setIconIndex mPotion.weakness.getStatusIconIndex setPotionName s"potion.${Reference.NAMESPACED_PREFIX}beamsUnlimited"
	val potionPoisonImmunity = new Potion(false, 0x909F30) setIconIndex mPotion.poison.getStatusIconIndex setPotionName s"potion.${Reference.NAMESPACED_PREFIX}immunityPoison"
	val potionImmunityDamage = new Potion(false, 0x99703A) setIconIndex mPotion.field_76444_x.getStatusIconIndex setPotionName s"potion.${Reference.NAMESPACED_PREFIX}immunityDamage" // field_76444_x -> absoption
	val potionLowerAttackChance = new Potion(false, 0x7DBB7D)
	val potionCharm = new Potion(false, 0xBB4848)
	val potionFasterShield = new Potion(false, 0xFDCB00)
}
