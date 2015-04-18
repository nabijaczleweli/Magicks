package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IConfigurable, ResourceLocationFancyMagicks}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.common.config.Configuration
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object RenderAOEIceSpike extends Render with IConfigurable {
	var progressStep = .1f

	private lazy val entityTexture = ResourceLocationFancyMagicks("textures/entity/aoe_spike_ice.png")
	private lazy val model = AdvancedModelLoader loadModel ResourceLocationFancyMagicks("models/entity/aoe_spike_ice.tcn")

	override def doRender(_entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		val entity = _entity.asInstanceOf[EntityAOEIceSpike]
		val wasUp = entity.ticksExisted > (1 / progressStep)
		val progress =
			0F max {
				if(wasUp)
					1F - (entity.ticksExisted - (1 / progressStep)) * progressStep
				else
					entity.ticksExisted * progressStep
			} min 1F

		if(progress > 0F) {
			glPushMatrix()
				glTranslated(x, y, z)
				bindEntityTexture(entity)

				glScalef(1, progress, 1)
				model.renderAll()
			glPopMatrix()
		} else if(wasUp)
			EntityUtil dispatchSimpleKill entity
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture

	override def configure(config: Configuration) {
		progressStep = config.getFloat("iceSpikeGrowthStep", "render", progressStep, Float.MinPositiveValue, 1F, "Step of raising/pulling down AOEIceSpikes")
	}
}
