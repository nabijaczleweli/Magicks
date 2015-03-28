package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.potion.Potion
import com.nabijaczleweli.fancymagicks.staves.StaffAbility
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.settings.KeyBinding
import org.apache.logging.log4j.LogManager

import scala.collection.immutable.HashMap

object Container {
	val log = LogManager.getRootLogger // Set by the mod instance in PreInit; needs not to be lazy (reflection)

	@SideOnly(Side.CLIENT)
	val keyBindStaffSpecialAblility = new KeyBinding(s"key.${Reference.NAMESPACED_PREFIX}staffSpecial", 48, Reference.MOD_NAME) // 48 -> B

	var abilityRegistry: Map[String, StaffAbility] = HashMap.empty

	val potionElementalResistance = new Potion(false, 0xC2C22C)
	val potionUnlimitedBeams = new Potion(false, 0xEEDD05)
	val potionPoisonImmunity = new Potion(false, 0x909F30)
	val potionImmunityDamage = new Potion(false, 0x99703A)
}
