package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.reference.StaffTypeRegistry.AbilityMissing
import com.nabijaczleweli.fancymagicks.staves.{StaffAbility, AbilitySimple, StaffType}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.settings.KeyBinding

import scala.collection.immutable.HashMap

object Container {
	@SideOnly(Side.CLIENT)
	val keyBindStaffSpecialAblility = new KeyBinding(s"key.${Reference.NAMESPACED_PREFIX}staffSpecial", 48, Reference.MOD_NAME) // 48 -> B

	val abilityEmpty = new AbilitySimple({_ => ()}, s"tooltip.${NAMESPACED_PREFIX}staffAbilityNone")
	var staves = new StaffType(s"${NAMESPACED_PREFIX}staff", "Simplest staff", abilityEmpty, abilityEmpty) ::
	             new StaffType(s"${NAMESPACED_PREFIX}ex00", "Fancy staff", new AbilitySimple({_ addExperienceLevel 1}, "Experience"), new AbilitySimple({_.addVelocity(0, 100, 0)}, "FLY")) ::
	             new StaffType(s"${NAMESPACED_PREFIX}ex01", "Error staff", new AbilityMissing("fancymagicks:passive"), new AbilityMissing("fancymagicks:active")) :: Nil

	var abilityRegistry: Map[String, StaffAbility] = new HashMap
}
