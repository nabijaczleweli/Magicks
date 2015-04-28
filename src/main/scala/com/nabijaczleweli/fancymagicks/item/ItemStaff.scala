package com.nabijaczleweli.fancymagicks.item

import java.util.{List => jList}

import com.nabijaczleweli.fancymagicks.creativetab.CreativeTabFancyMagicks
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.entity.properties.{ExtendedPropertyElements, ExtendedPropertyPrevRotationPitch, ExtendedPropertySelectionDirection}
import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.reference.StaffTypeRegistry
import com.nabijaczleweli.fancymagicks.util.{Direction, IConfigurable}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, Item, ItemStack}
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.config.Configuration

object ItemStaff extends Item with IConfigurable {
	val staffTypeTagKey = "staffType"
	var threshold = 10

	setCreativeTab(CreativeTabFancyMagicks)
	setUnlocalizedName(NAMESPACED_PREFIX + "staff")
	setTextureName(NAMESPACED_PREFIX + "missing_staff")
	setHasSubtypes(true)
	setMaxDamage(0)

	def staffType(stack: ItemStack) =
		if(!stack.hasTagCompound)
			None
		else
			stack.getTagCompound getString staffTypeTagKey match {
				case "" =>
					None
				case str =>
					Some(str)
			}

	def staff(stack: ItemStack) =
		staffType(stack) flatMap {StaffTypeRegistry.get}

	def executeActiveAbility(player: EntityPlayer) =
		staff(player.getHeldItem) match {
			case Some(staff) =>
				staff activeAbility player
			case None =>
		}

	def executePassiveAbility(player: EntityPlayer) =
		staff(player.getHeldItem) match {
			case Some(staff) =>
				staff passiveAbility player
			case None =>
		}


	@SideOnly(Side.CLIENT)
	override def registerIcons(registry: IIconRegister) {
		super.registerIcons(registry)
		StaffTypeRegistry.values foreach {_ registerIcons registry}
	}

	@SideOnly(Side.CLIENT)
	override def getIconIndex(stack: ItemStack) =
		staff(stack).fold(itemIcon)(_.icon)

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: jList[_]) =
		StaffTypeRegistry.keys map {new NBTTagString(_)} map {(_, new ItemStack(this))} map {pr => pr._2.setTagInfo(staffTypeTagKey, pr._1); pr._2} foreach {list.asInstanceOf[jList[ItemStack]].add}

	override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer) = {
		val prop = new ExtendedPropertySelectionDirection
		player.registerExtendedProperties(ExtendedPropertySelectionDirection.id, prop)
		prop.init(player, world)

		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		stack
	}

	override def getMaxItemUseDuration(stack: ItemStack) =
		Int.MaxValue

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
	override def onUsingTick(stack: ItemStack, player: EntityPlayer, count: Int) =
		ExtendedPropertyPrevRotationPitch.id :: ExtendedPropertySelectionDirection.id :: Nil map {player.getExtendedProperties} match {
			case (rot: ExtendedPropertyPrevRotationPitch) :: (dir: ExtendedPropertySelectionDirection) :: Nil =>
				val start = dir.directions.size

				if(player.rotationPitch - rot.prevRotationPitch >= threshold)
					dir.directions enqueue Direction.down
				else if(player.rotationPitch - rot.prevRotationPitch <= -threshold)
					dir.directions enqueue Direction.up
				else if(player.rotationYawHead - player.prevRotationYawHead <= -threshold)
					dir.directions enqueue Direction.left
				else if(player.rotationYawHead - player.prevRotationYawHead >= threshold)
					dir.directions enqueue Direction.right

				if(dir.directions.size > start)
					rot.update()
			case _ =>
		}

	override def onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, meta: Int) {
		ExtendedPropertySelectionDirection.id :: ExtendedPropertyElements.id :: Nil map {player.getExtendedProperties} match {
			case (dir: ExtendedPropertySelectionDirection) :: (elem: ExtendedPropertyElements) :: Nil =>
				elem addElement Element(dir)
			case _ =>
		}
		ExtendedPropertySelectionDirection removeFrom player
	}

	override def addInformation(stack: ItemStack, player: EntityPlayer, ilist: jList[_], additionalData: Boolean) {
		lazy val list = ilist.asInstanceOf[jList[String]]
		staff(stack) match {
			case Some(staff) =>
				list add StatCollector.translateToLocalFormatted(s"tooltip.${NAMESPACED_PREFIX}staffAbilityPassive", staff.passiveAbility.displayDescription)
				list add StatCollector.translateToLocalFormatted(s"tooltip.${NAMESPACED_PREFIX}staffAbilityActive", staff.activeAbility.displayDescription)
			case None =>
		}
		if(additionalData)
			list add StatCollector.translateToLocalFormatted(s"tooltip.${NAMESPACED_PREFIX}staffId", staffType(stack) getOrElse {StatCollector translateToLocal s"tooltip.${NAMESPACED_PREFIX}staffIdMissing"})
	}

	override def getItemStackDisplayName(stack: ItemStack) =
		staff(stack) match {
			case Some(staff) =>
				staff.localizedName
			case None =>
				super.getItemStackDisplayName(stack)
		}

	override def onUpdate(stack: ItemStack, world: World, entity: Entity, i: Int, b: Boolean) {
		entity match {
			case player: EntityPlayer =>
				if(stack == player.getHeldItem)
					executePassiveAbility(player)
			case _ =>
		}
		entity getExtendedProperties ExtendedPropertyElements.id match {
			case elem: ExtendedPropertyElements =>
				elem.update()
			case _ =>
		}
	}

	override def configure(config: Configuration) {
		threshold = config.getInt("elementSelectionThreshold", "elements", threshold, 1, 30, "In degrees, turn amount after which the direction of turn will be stored")
	}
}
