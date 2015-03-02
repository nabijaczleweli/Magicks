package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft

@SideOnly(Side.CLIENT)
object KeyHandler {
	@SubscribeEvent
	def onKeyClicked(event: KeyInputEvent) =
		if(Container.keyBindStaffSpecialAblility.isPressed)
			Minecraft.getMinecraft.thePlayer.getHeldItem.getItem match {
				case ItemStaff =>
					ItemStaff executeActiveAbility Minecraft.getMinecraft.thePlayer
				case _ =>
			}
}
