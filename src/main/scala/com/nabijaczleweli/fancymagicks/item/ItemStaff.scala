package com.nabijaczleweli.fancymagicks.item

import java.util.{List => jList}

import com.nabijaczleweli.fancymagicks.creativetab.CreativeTabFancyMagicks
import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertyPrevRotationPitch
import com.nabijaczleweli.fancymagicks.reference.{Container, Reference}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, Item, ItemStack}
import net.minecraft.util.StatCollector
import net.minecraft.world.World

object ItemStaff extends Item {
	setCreativeTab(CreativeTabFancyMagicks)
	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "staff")
	setTextureName(Reference.NAMESPACED_PREFIX + "missing_staff")
	setHasSubtypes(true)
	setMaxDamage(0)

	def staff(dmg: Int) =
		if(Container.staves.size > dmg)
			Some(Container staves dmg)
		else
			None

	def executeActiveAbility(player: EntityPlayer) =
		staff(player.getHeldItem.getItemDamage) match {
			case Some(staff) =>
				staff activeAbility player
			case None =>
		}

	def executePassiveAbility(player: EntityPlayer) =
		staff(player.getHeldItem.getItemDamage) match {
			case Some(staff) =>
				staff passiveAbility player
			case None =>
		}


	@SideOnly(Side.CLIENT)
	override def registerIcons(registry: IIconRegister) {
		super.registerIcons(registry)
		Container.staves foreach {_ registerIcons registry}
	}

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(dmg: Int) =
		staff(dmg).fold(itemIcon)(_ icon 0)

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: jList[_]) =
		for(id <- Container.staves.indices)
			list.asInstanceOf[jList[ItemStack]] add new ItemStack(this, 1, id)

	override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer) = {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		stack
	}

	override def getMaxItemUseDuration(stack: ItemStack) =
		100

	override def getItemUseAction(stack: ItemStack) =
		EnumAction.bow

	// yaw = horizontal
	//       -90 <- 0 -> 90
	// pitch = vertical
	//         -90
	//         /\
	//         0
	//         \/
	//         90
	override def onUsingTick(stack: ItemStack, player: EntityPlayer, count: Int) {
		player getExtendedProperties ExtendedPropertyPrevRotationPitch.id match {
			case rot: ExtendedPropertyPrevRotationPitch =>
				println(player.rotationYawHead + " " + player.prevRotationYawHead + " " + player.rotationPitch + " " + rot.prevRotationPitch + " " + (player.rotationPitch == rot.prevRotationPitch))
				rot.update()
			case _ =>
		}
	}

	override def addInformation(stack: ItemStack, player: EntityPlayer, list: jList[_], additionalData: Boolean) {
		staff(stack.getItemDamage) match {
			case Some(staff) =>
				list.asInstanceOf[jList[String]] add staff.name
			case None =>
				list.asInstanceOf[jList[String]] add (StatCollector translateToLocal s"tooltip.${Reference.NAMESPACED_PREFIX}missingStaff")
		}
		if(additionalData)
			list.asInstanceOf[jList[String]] add StatCollector.translateToLocalFormatted(s"tooltip.${Reference.NAMESPACED_PREFIX}staffId", stack.getItemDamage: Integer)
	}

	override def getItemStackDisplayName(stack: ItemStack) =
		staff(stack.getItemDamage) match {
			case Some(staff) =>
				staff.localizedName
			case None =>
				super.getItemStackDisplayName(stack)
		}

	override def onUpdate(stack: ItemStack, world: World, entity: Entity, i: Int, b: Boolean) =
		entity match {
			case player: EntityPlayer =>
				if(stack == player.getHeldItem)
					executePassiveAbility(player)
			case _ =>
		}
}
