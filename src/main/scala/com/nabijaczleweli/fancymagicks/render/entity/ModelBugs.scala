package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.{EntityLivingBase, Entity}
import net.minecraftforge.common.config.Configuration

import scala.util.Random

class ModelBugs extends ModelBase {
	import ModelBugs._

	textureWidth = 32
	textureHeight = 32

	var bugRenderers = Seq.fill(bugsPerSwarm)(new ModelRenderer(this)) map {_.addBox(0, 0, 0, 4, 4, 4)} map {_.setTextureSize(textureWidth, textureHeight)} map {mr => mr.setRotationPoint(-2, 16, -2); mr} map
	                   {mr => mr.offsetX = randomOffset; mr.offsetY = randomOffset; mr.offsetZ = randomOffset; mr}

	override def render(entity: Entity, f0: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f0, f1, f2, f3, f4, f5)
		bugRenderers foreach {_ render f5}
	}

	override def setLivingAnimations(entity: EntityLivingBase, f0: Float, f1: Float, f2: Float) {
		super.setLivingAnimations(entity, f0, f1, f2)
		for(mr <- bugRenderers) {
			mr.offsetX = normalized(mr.offsetX + randomMove)
			mr.offsetY = normalized(mr.offsetY + randomMove)
			mr.offsetZ = normalized(mr.offsetZ + randomMove)
		}
	}
}

object ModelBugs extends IConfigurable {
	private val rand = new Random

	var bugsPerSwarm = 10
	var bugsSwarmRadius = 1F

	private def randomOffset =
		rand.nextFloat() * bugsSwarmRadius * (if(rand.nextBoolean()) 1 else -1)

	private def randomMove =
		rand.nextFloat() * bugsSwarmRadius * (if(rand.nextBoolean()) .01F else -.01F)

	private def normalized(r: Float) =
		if(math.abs(r) > bugsSwarmRadius)
			math.signum(r) * bugsSwarmRadius
		else
			r

	override def configure(config: Configuration) {
		bugsPerSwarm = config.getInt("bugsPerSwarm", "render", bugsPerSwarm, 1, Int.MaxValue, "Amount of individual bugs per swarm")
		bugsSwarmRadius = config.getFloat("bugsSwarmRadius", "render", bugsSwarmRadius, 0, Float.MaxValue, "Maximal radius at which bugs can be seen [blocks]")
	}
}
