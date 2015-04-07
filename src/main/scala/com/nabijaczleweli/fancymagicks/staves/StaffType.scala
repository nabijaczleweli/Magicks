package com.nabijaczleweli.fancymagicks.staves

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.{StatCollector, IIcon}

class StaffType(texture: String, val name: String, val passiveAbility: StaffAbility, val activeAbility: StaffAbility) {
	@SideOnly(Side.CLIENT)
	var icon: IIcon = _

	@SideOnly(Side.CLIENT)
	def registerIcons(registry: IIconRegister) =
		icon = registry registerIcon texture

	def localizedName =
		StatCollector translateToLocal name
}
