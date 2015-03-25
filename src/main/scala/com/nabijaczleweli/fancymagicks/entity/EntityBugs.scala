package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.render.entity.ModelBugs
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.ai.{EntityAIHurtByTarget, EntityAINearestAttackableTarget, EntityAIWander}
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.{Entity, EntityCreature, EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.util.DamageSource
import net.minecraft.world.World

class EntityBugs(world: World) extends EntityCreature(world) with IMob {
	setSize(ModelBugs.bugsSwarmRadius, ModelBugs.bugsSwarmRadius)
	tasks.addTask(1, new EntityAIWander(this, .6D))
	targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityLivingBase], 0, false, false, new IEntitySelector {
		override def isEntityApplicable(entity: Entity) =
			entity.isInstanceOf[EntityLivingBase]
	}))
	targetTasks.addTask(2, new EntityAIHurtByTarget(this, false))
	//TODO: get the AI working (it works a little bit, doesn't attack anything, though)


	override def isAIEnabled =
		true

	override def attackEntity(entity: Entity, distance: Float) =
		if(attackTime <= 0 && distance < 2F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
			attackTime = 2
			attackEntityAsMob(entity)
		}

	override def getBlockPathWeight(x: Int, y: Int, z: Int) =
		.5f

	override def applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth) setBaseValue 4D
		getEntityAttribute(SharedMonsterAttributes.followRange) setBaseValue 100D
		getEntityAttribute(SharedMonsterAttributes.movementSpeed) setBaseValue .23000000417232513D
		getAttributeMap registerAttribute SharedMonsterAttributes.attackDamage setBaseValue 1.5D
	}

	override def attackEntityAsMob(entity: Entity) =
		entity.attackEntityFrom(DamageSource causeMobDamage this, getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue.toFloat)
}

object EntityBugs {
	def defaultSummon(inFrontOf: EntityLivingBase) {
		//TODO: summon an X amount of blocks in front of entity

		val entity = new EntityBugs(inFrontOf.worldObj)
		entity.setPosition(inFrontOf.posX, inFrontOf.posY, inFrontOf.posZ)
		inFrontOf.worldObj spawnEntityInWorld entity
	}
}
