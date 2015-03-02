package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.staves.{AbilitySimple, StaffType}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.settings.KeyBinding

object Container {
	@SideOnly(Side.CLIENT)
	val keyBindStaffSpecialAblility = new KeyBinding(s"key.${Reference.NAMESPACED_PREFIX}staffSpecial", 48, Reference.MOD_NAME) // 48 -> B

	val abilityEmpty = new AbilitySimple({_ => ()}, "")
	var staves: List[StaffType] = new StaffType(s"${NAMESPACED_PREFIX}staff", "Simplest staff", abilityEmpty, abilityEmpty) ::
	                              new StaffType(s"${NAMESPACED_PREFIX}ex00", "Fancy staff", new AbilitySimple({_.addVelocity(0, 100, 0)}, "FLY"), new AbilitySimple({_ addExperienceLevel 1}, "Experience")) :: Nil
}
