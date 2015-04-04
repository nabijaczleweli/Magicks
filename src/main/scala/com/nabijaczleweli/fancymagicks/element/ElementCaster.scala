package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.util.NBTReloadable

trait ElementCaster extends NBTReloadable {
	def start(): Unit

	def continue(): Unit

	def end(): Unit
}
