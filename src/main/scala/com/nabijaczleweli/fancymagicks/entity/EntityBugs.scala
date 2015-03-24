package com.nabijaczleweli.fancymagicks.entity

import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.{Entity, EntityCreature, EntityLivingBase}
import net.minecraft.world.World

class EntityBugs(world: World) extends EntityCreature(world) with IMob {
	targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityLivingBase], 0, true))
	//TODO: get the AI working

	override def attackEntity(entity: Entity, distance: Float) =
		super.attackEntity(entity, distance)
	//TODO: proper attacking code

	override def getBlockPathWeight(x: Int, y: Int, z: Int) =
		.5f
}

object EntityBugs {
	def defaultSummon(inFrontOf: EntityLivingBase) {
		//TODO: summon an X amount of blocks in front of entity

		val entity = new EntityBugs(inFrontOf.worldObj)
		entity.setPosition(inFrontOf.posX, inFrontOf.posY, inFrontOf.posZ)
		inFrontOf.worldObj spawnEntityInWorld entity
	}
}
