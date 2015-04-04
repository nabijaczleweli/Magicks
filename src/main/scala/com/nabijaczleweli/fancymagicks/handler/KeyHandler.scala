package com.nabijaczleweli.fancymagicks.handler

import com.nabijaczleweli.fancymagicks.element.ElementAOECaster
import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertyElements
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.reference.Container
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft

@SideOnly(Side.CLIENT)
object KeyHandler {
	@SubscribeEvent
	def onKeyClicked(event: KeyInputEvent) =
		if(Container.keyBindStaffSpecialAblility.isPressed)
			Minecraft.getMinecraft.thePlayer.getHeldItem match {
				case null =>
				case is =>
					is.getItem match {
						case ItemStaff =>
							ItemStaff executeActiveAbility Minecraft.getMinecraft.thePlayer
						case _ =>
					}
			}

	@SubscribeEvent
	def onPlayerTicked(event: PlayerTickEvent) = {
		val pressedAOE = Container.keyBindStaffApplyAOE.getIsKeyPressed
		val pressedForward = Container.keyBindStaffApplyForward.getIsKeyPressed
		if(event.player == Minecraft.getMinecraft.thePlayer)
			Minecraft.getMinecraft.thePlayer.getHeldItem match {
				case null =>
				case is =>
					is.getItem match {
						case ItemStaff =>
							Minecraft.getMinecraft.thePlayer getExtendedProperties ExtendedPropertyElements.id match {
								case elem: ExtendedPropertyElements =>
									elem.caster match {
										case null =>
											elem.caster = {
												if(pressedAOE) {
													val t = ElementAOECaster(Minecraft.getMinecraft.thePlayer, elem.validElements)
													t.start()
													t
												} else if(pressedForward)
													null
												else
													null
											}
										case c =>
											if(pressedAOE || pressedForward)
												c.continue()
											else {
												c.end()
												elem.caster = null
											}
									}
								case _ =>
							}
						case _ =>
					}
			}
	}
}
