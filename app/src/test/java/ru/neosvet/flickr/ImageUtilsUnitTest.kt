package ru.neosvet.flickr

import org.junit.Test

import org.junit.Assert.*
import ru.neosvet.flickr.image.ImageUtils
import ru.neosvet.flickr.image.Size

class ImageUtilsUnitTest {
    @Test
    fun imageUtils_findSeparator_SymbolX() {
        assertEquals(ImageUtils.findSeparator("1920x1080"), 4)
    }
    @Test
    fun imageUtils_findSeparator_OtherSymbol() {
        assertEquals(ImageUtils.findSeparator("1920_1080"), 4)
    }
    @Test
    fun imageUtils_findSeparator_NoSymbol() {
        assertEquals(ImageUtils.findSeparator("19201080"), -1)
    }
    @Test
    fun imageUtils_isFitRatio_ReturnTrue() {
        val size1 = Size(1920, 1080)
        val size2 = Size(1280, 720)
        assertTrue(ImageUtils.isFitRatio(size1, size2))
    }
    @Test
    fun imageUtils_isFitRatio_ReturnFalse() {
        val size1 = Size(1920, 1080)
        val size2 = Size(1280, 800)
        assertFalse(ImageUtils.isFitRatio(size1, size2))
    }
}
