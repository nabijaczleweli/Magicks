package com.nabijaczleweli.fancymagicks.render.model

import java.io.{ByteArrayInputStream, IOException, InputStream}
import java.util.zip.{ZipEntry, ZipException, ZipInputStream}
import javax.xml.parsers.{DocumentBuilderFactory, ParserConfigurationException}

import cpw.mods.fml.common.FMLLog
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.{IModelCustom, ModelFormatException}
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11._
import org.xml.sax.SAXException

import scala.collection.immutable.HashMap
import scala.collection.mutable.{LinkedHashMap => mLinkedHashMap, Map => mMap}

/** A working techne model.<br />
  * Shamelessly stolen from Dullkus.<br />
  * Brutally `scala`ized by nabijaczleweli.
  *
  * @see [[https://github.com/drullkus/MateriaMuto/blob/master/src/main/java/com/agilemods/materiamuto/client/model/importer/TechneModel.java]]
  * @note I probably didn't quite catch all parts of `IDEA`'s "calling EVERY parameterless method without parens" bullshit, please report
  */
@SideOnly(Side.CLIENT)
class TechneModel @throws(classOf[ModelFormatException]) (resource: ResourceLocation) extends ModelBase with IModelCustom {
	val parts: mMap[String, ModelRenderer] = new mLinkedHashMap

	private val fileName = resource.toString
	private var zipContents = new HashMap[String, Array[Byte]]
	private var texture = ""
	/*
	private var textureName   : Int     = 0
	private var textureNameSet: Boolean = false
	*/

	loadTechneModel((Minecraft.getMinecraft.getResourceManager getResource resource).getInputStream)

	@throws(classOf[ModelFormatException])
	private def loadTechneModel(stream: InputStream) {
		try {
			val zipInput = new ZipInputStream(stream)
			var entry: ZipEntry = null
			while({entry = zipInput.getNextEntry; entry} != null) {
				val data = new Array[Byte](entry.getSize.toInt)
				// For some reason, using read(byte[]) makes reading stall upon reaching a 0x1E byte
				for(i <- 0 until data.length if zipInput.available > 0)
					data(i) = zipInput.read().toByte
				zipContents += entry.getName -> data
			}
			val modelXml =
				(zipContents get "models.xml", zipContents get "model.xml") match {
					case (Some(a), None) =>
						a
					case (None, Some(a)) =>
						a
					case (Some(a0), Some(_)) =>
						a0
					case _ =>
						throw new ModelFormatException("Model " + fileName + " contains no model(s).xml file")
				}

			val document = DocumentBuilderFactory.newInstance.newDocumentBuilder parse new ByteArrayInputStream(modelXml)
			val nodeListTechne = document getElementsByTagName "Techne"
			if(nodeListTechne.getLength < 1)
				throw new ModelFormatException("Model " + fileName + " contains no Techne tag")

			val nodeListModel = document getElementsByTagName "Model"
			if(nodeListModel.getLength < 1)
				throw new ModelFormatException("Model " + fileName + " contains no Model tag")

			val modelAttributes = nodeListModel.item(0).getAttributes
			if(modelAttributes == null)
				throw new ModelFormatException("Model " + fileName + " contains a Model tag with no attributes")

			texture = (Option(modelAttributes getNamedItem "texture") map {_.getTextContent}).orNull

			val textureSize = document getElementsByTagName "TextureSize"
			for(i <- 0 until textureSize.getLength) {
				val size = textureSize.item(i).getTextContent
				val textureDimensions = size split ","
				textureWidth = textureDimensions(0).toInt
				textureHeight = textureDimensions(1).toInt
			}

			val shapes = document getElementsByTagName "Shape"
			for(i <- 0 until shapes.getLength) {
				val shape = shapes item i
				val shapeAttributes = shape.getAttributes
				if(shapeAttributes == null)
					throw new ModelFormatException("Shape #" + (i + 1) + " in " + fileName + " has no attributes")

				val shapeName = Option(shapeAttributes getNamedItem "name").fold(s"Shape #${i + 1}")(_.getNodeValue)
				val shapeType = (Option(shapeAttributes getNamedItem "type") map {_.getNodeValue}).orNull

				if(shapeType != null && !TechneModel.cubeTypes.contains(shapeType))
					FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " is not a cube, ignoring")
				else
					try {
						var mirrored = false
						var offset = new Array[String](3)
						var position = new Array[String](3)
						var rotation = new Array[String](3)
						var size = new Array[String](3)
						var textureOffset = new Array[String](2)
						val shapeChildren = shape.getChildNodes

						for(j <- 0 until shapeChildren.getLength) {
							val shapeChild = shapeChildren item j
							val shapeChildName = shapeChild.getNodeName
							var shapeChildValue = shapeChild.getTextContent

							if(shapeChildValue != null) {
								shapeChildValue = shapeChildValue.trim
								shapeChildName match {
									case "IsMirrored" =>
										mirrored = !(shapeChildValue == "False")
									case "Offset" =>
										offset = shapeChildValue split ","
									case "Position" =>
										position = shapeChildValue split ","
									case "Rotation" =>
										rotation = shapeChildValue split ","
									case "Size" =>
										size = shapeChildValue split ","
									case "TextureOffset" =>
										textureOffset = shapeChildValue split ","
									case _ =>
								}
							}
						}

						// That's what the ModelBase subclassing is needed for
						val cube = new ModelRenderer(this, shapeName)
						cube.setTextureOffset(textureOffset(0).toInt, textureOffset(1).toInt)
						cube.mirror = mirrored
						cube.addBox(offset(0).toFloat, offset(1).toFloat, offset(2).toFloat, size(0).toInt, size(1).toInt, size(2).toInt)
						cube.setRotationPoint(position(0).toFloat, position(1).toFloat - 16, position(2).toFloat)
						cube.rotateAngleX = (Math toRadians rotation(0).toFloat).toFloat
						cube.rotateAngleY = (Math toRadians rotation(1).toFloat).toFloat
						cube.rotateAngleZ = (Math toRadians rotation(2).toFloat).toFloat

						if(parts.keysIterator contains shapeName)
							throw new ModelFormatException("Model contained duplicate part name: '" + shapeName + "' node #" + i)
						parts += shapeName -> cube
					} catch {
						case nfe: NumberFormatException =>
							FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " contains malformed integers within its data, ignoring")
							nfe.printStackTrace()
					}
				}
		} catch {
			case ze: ZipException =>
				throw new ModelFormatException("Model " + fileName + " is not a valid zip file")
			case ioe: IOException =>
				throw new ModelFormatException("Model " + fileName + " could not be read", ioe)
			case pce: ParserConfigurationException =>
				// hush
			case saxe: SAXException =>
				throw new ModelFormatException("Model " + fileName + " contains invalid XML", saxe)
		}
	}

	private def bindTexture() {}

	override val getType = "tcn"

	private def setup() {
		GL11.glScalef(-1, -1, 1)
	}

	def renderAll() {
		glPushMatrix()
		bindTexture()
		setup()
		for(part <- parts.values)
			part render .0625F
		glPopMatrix()
	}

	def renderPart(partName: String) =
		parts get partName match {
			case None =>
			case Some(part) =>
				glPushMatrix()
				setup()
				bindTexture()
				part render .0625F
				glPopMatrix()
		}

	def renderOnly(groupNames: String*) {
		glPushMatrix()
		setup()
		bindTexture()
		for((key, value) <- parts; groupName <- groupNames if key equalsIgnoreCase groupName)
		glPopMatrix()
	}

	def renderOnlyAroundPivot(angle: Double, rotX: Double, rotY: Double, rotZ: Double, groupNames: String*) {
		glPushMatrix()
		setup()
		bindTexture()
		for((key, model) <- parts; groupName <- groupNames if key equalsIgnoreCase groupName) {
			glPushMatrix()
			glTranslatef(model.rotationPointX / 16, model.rotationPointY / 16, model.rotationPointZ / 16)
			glRotated(angle, rotX, rotY, rotZ)
			glTranslatef(-model.rotationPointX / 16, -model.rotationPointY / 16, -model.rotationPointZ / 16)
			model render .0625F
			glPopMatrix()
		}
		glPopMatrix()
	}

	def renderAllExcept(excludedGroupNames: String*) {
		glPushMatrix()
		setup()
		for((key, value) <- parts; groupName <- excludedGroupNames if !(key equalsIgnoreCase groupName))
			value render .0625F
		glPopMatrix()
	}
}

@SideOnly(Side.CLIENT)
object TechneModel {
	val cubeTypes = "d9e621f7-957f-4b77-b1ae-20dcd0da7751" :: "de81aa14-bd60-4228-8d8d-5238bcd3caaa" :: Nil
}
