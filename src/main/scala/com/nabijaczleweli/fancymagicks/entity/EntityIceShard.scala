package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.element.elements.{Element, ElementIce}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.world.World

class EntityIceShard(world: World) extends EntityForwardElementalProjectile(world) {
	def this(world: World, elems: Seq[Element]) {
		this(world)
		init(elems)
	}

	def this(world: World, elb: EntityLivingBase, elems: Seq[Element]) {
		this(world)
		init(elb, elems)
	}

	def this(world: World, xPos: Double, yPos: Double, zPos: Double, elems: Seq[Element]) {
		this(world)
		init(xPos, yPos, zPos, elems)
	}

	override protected val particleBlock = Blocks.ice
	override protected val particleColor = 0xFFFFFF
	override protected val primaryElement = ElementIce
	override protected val baseHeight = 7F / 16F
	override protected val baseWidth = 25F / 16F
}
