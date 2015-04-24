package com.nabijaczleweli.fancymagicks.util

import com.nabijaczleweli.fancymagicks.element.elements.Element

trait IElemental {
	def elems: Seq[Element]
	def elems_=(newelems: Seq[Element]): Unit
}
