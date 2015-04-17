package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.util.{IConfigurable, EntityUtil, ResourceLocationFancyMagicks}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.common.config.Configuration
import org.lwjgl.opengl.GL11._

import scala.collection.mutable.{LongMap => mLongMap}

@SideOnly(Side.CLIENT)
object RenderAOEIceSpike extends Render with IConfigurable {
	var progressStep = .1f

	private lazy val entityTexture = ResourceLocationFancyMagicks("textures/entity/aoe_spike_ice.png")
	private lazy val model = AdvancedModelLoader loadModel ResourceLocationFancyMagicks("models/entity/aoe_spike_ice.tcn")
	private lazy val progresses = mLongMap.empty withDefaultValue 0f -> false

	override def doRender(_entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		val entity = _entity.asInstanceOf[EntityAOEIceSpike]

		glPushMatrix()
			glTranslated(x, y, z)
		  bindEntityTexture(entity)

			glScalef(1, progresses(entity.getEntityId)._1, 1)
			model.renderAll()
		glPopMatrix()

		var (progress, wasUp) = progresses(entity.getEntityId)
		0F max (progress + progressStep * (if(wasUp) -1 else 1)) min 1F match {
			case 0 if wasUp =>
				EntityUtil dispatchSimpleKill entity
			case 1 =>
				progress = 1
				wasUp = true
			case f =>
				progress = f
		}
		progresses(entity.getEntityId) = progress -> wasUp
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture

	override def configure(config: Configuration) {
		progressStep = config.getFloat("iceSpikeGrowthStep", "render", progressStep, Float.MinPositiveValue, 1F, "Step of raising/pulling down AOEIceSpikes")
	}
}
