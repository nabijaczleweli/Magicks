package com.nabijaczleweli.fancymagicks.staves

import net.minecraft.entity.player.EntityPlayer

class AbilitySimple(executioner: EntityPlayer => Unit, val description: String) extends StaffAbility {
	def apply(player: EntityPlayer) =
		executioner(player)
}
