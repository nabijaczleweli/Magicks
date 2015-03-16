package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.staves.{AbilitySimple, StaffAbility}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.settings.KeyBinding

import scala.collection.immutable.HashMap

object Container {
	@SideOnly(Side.CLIENT)
	val keyBindStaffSpecialAblility = new KeyBinding(s"key.${Reference.NAMESPACED_PREFIX}staffSpecial", 48, Reference.MOD_NAME) // 48 -> B

	var abilityRegistry: Map[String, StaffAbility] = HashMap("fancymagicks:none" -> new AbilitySimple({_ => ()}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityNone"))
}
