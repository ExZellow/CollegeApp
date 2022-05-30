package com.example.collegeapp.ui.schedule

import kotlin.Throws
/*
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.text.PDFTextStripper
import com.tom_roush.pdfbox.text.TextPosition
import com.tom_roush.pdfbox.text.TextPositionComparator
*/

import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt
/*
class PDFLayoutTextStripper : PDFTextStripper() {
    private var currentPageWidth = 0.0
    private var previousTextPosition: TextPosition? = null
    private var textLineList: MutableList<TextLine>

    /**
     *
     * @param page page to parse

    @Throws(IOException::class)
    override fun processPage(page: PDPage) {
        val pageRectangle: PDRectangle = page.mediaBox
        if (pageRectangle != null) {
            setCurrentPageWidth(pageRectangle.width)
            super.processPage(page)
            previousTextPosition = null
            textLineList = ArrayList()
        }
    }

    @Throws(IOException::class)
    override fun writePage() {
        val charactersByArticle: List<List<TextPosition>> = super.getCharactersByArticle()
        for (element in charactersByArticle) {
            val textList: List<TextPosition> = element
            try {
                sortTextPositionList(textList)
            } catch (e: IllegalArgumentException) {
                System.err.println(e)
            }
            iterateThroughTextList(textList.iterator())
        }
        writeToOutputStream(textLineList)
    }

    @Throws(IOException::class)
    private fun writeToOutputStream(textLineList: List<TextLine>) {
        for (textLine in textLineList) {
            val line = textLine.line.toCharArray()
            super.getOutput().write(line)
            super.getOutput().write("\n")
            super.getOutput().flush()
        }
    }

    /*
     * In order to get rid of the warning:
     * TextPositionComparator class should implement Comparator<TextPosition> instead of Comparator

    private fun sortTextPositionList(textList: List<TextPosition>) {
        val comparator = TextPositionComparator()
        Collections.sort(textList, comparator)
    }

    private fun writeLine(textPositionList: List<TextPosition>) {
        if (textPositionList.isNotEmpty()) {
            val textLine = addNewLine()
            var firstCharacterOfLineFound = false
            for (textPosition in textPositionList) {
                val characterFactory = CharacterFactory(firstCharacterOfLineFound)
                val character = characterFactory.createCharacterFromTextPosition(
                    textPosition,
                    getPreviousTextPosition()
                )
                textLine.writeCharacterAtIndex(character)
                setPreviousTextPosition(textPosition)
                firstCharacterOfLineFound = true
            }
        } else {
            addNewLine() // white line
        }
    }

    private fun iterateThroughTextList(textIterator: Iterator<TextPosition>) {
        val textPositionList: MutableList<TextPosition> = ArrayList()
        while (textIterator.hasNext()) {
            val textPosition: TextPosition = textIterator.next()
            val numberOfNewLines = getNumberOfNewLinesFromPreviousTextPosition(textPosition)
            if (numberOfNewLines == 0) {
                textPositionList.add(textPosition)
            } else {
                writeTextPositionList(textPositionList)
                createNewEmptyNewLines(numberOfNewLines)
                textPositionList.add(textPosition)
            }
            setPreviousTextPosition(textPosition)
        }
        if (textPositionList.isNotEmpty()) {
            writeTextPositionList(textPositionList)
        }
    }

    private fun writeTextPositionList(textPositionList: MutableList<TextPosition>) {
        writeLine(textPositionList)
        textPositionList.clear()
    }

    private fun createNewEmptyNewLines(numberOfNewLines: Int) {
        for (i in 0 until numberOfNewLines - 1) {
            addNewLine()
        }
    }

    private fun getNumberOfNewLinesFromPreviousTextPosition(textPosition: TextPosition): Int {
        val previousTextPosition: TextPosition = getPreviousTextPosition() ?: return 1
        val textYPosition = textPosition.y.roundToInt().toFloat()
        val previousTextYPosition = previousTextPosition.y.roundToInt()
            .toFloat()
        return if (textYPosition > previousTextYPosition && textYPosition - previousTextYPosition > 5.5) {
            val height: Float = textPosition.height
            var numberOfLines =
                (floor((textYPosition - previousTextYPosition).toDouble()) / height).toInt()
            numberOfLines = 1.coerceAtLeast(numberOfLines - 1) // exclude current new line
            if (DEBUG) println("$height $numberOfLines")
            numberOfLines
        } else {
            0
        }
    }

    private fun addNewLine(): TextLine {
        val textLine = TextLine(getCurrentPageWidth())
        textLineList.add(textLine)
        return textLine
    }

    private fun getPreviousTextPosition(): TextPosition? {
        return previousTextPosition
    }

    private fun setPreviousTextPosition(setPreviousTextPosition: TextPosition) {
        previousTextPosition = setPreviousTextPosition
    }

    private fun getCurrentPageWidth(): Int {
        return currentPageWidth.roundToInt()
    }

    private fun setCurrentPageWidth(currentPageWidth: Float) {
        this.currentPageWidth = currentPageWidth.toDouble()
    }

    companion object {
        const val DEBUG = false
        const val OUTPUT_SPACE_CHARACTER_WIDTH_IN_PT = 4
    }

    /**
     * Constructor

    init {
        textLineList = ArrayList()
    }
}

internal class TextLine(lineLength: Int) {
    private val lineLength: Int
    var line = ""
        private set
    private var lastIndex = 0
    fun writeCharacterAtIndex(character: Character) {
        character.index = computeIndexForCharacter(character)
        val index = character.index
        val characterValue = character.characterValue
        if (indexIsInBounds(index) && line[index] == SPACE_CHARACTER) {
            line = line.substring(0, index) + characterValue + line.substring(index + 1, lineLength)
        }
    }

    private fun computeIndexForCharacter(character: Character): Int {
        var index = character.index
        val isCharacterPartOfPreviousWord = character.isCharacterPartOfPreviousWord
        val isCharacterAtTheBeginningOfNewLine = character.isCharacterAtTheBeginningOfNewLine
        val isCharacterCloseToPreviousWord = character.isCharacterCloseToPreviousWord
        return if (!indexIsInBounds(index)) {
            -1
        } else {
            if (isCharacterPartOfPreviousWord && !isCharacterAtTheBeginningOfNewLine) {
                index = findMinimumIndexWithSpaceCharacterFromIndex(index)
            } else if (isCharacterCloseToPreviousWord) {
                index =
                    if (line[index] != SPACE_CHARACTER) {
                        index + 1
                    } else {
                        findMinimumIndexWithSpaceCharacterFromIndex(index) + 1
                    }
            }
            index = getNextValidIndex(index, isCharacterPartOfPreviousWord)
            index
        }
    }

    private fun isSpaceCharacterAtIndex(index: Int): Boolean {
        return line[index] != SPACE_CHARACTER
    }

    private fun isNewIndexGreaterThanLastIndex(index: Int): Boolean {
        val lastIndex = this.lastIndex
        return index > lastIndex
    }

    private fun getNextValidIndex(index: Int, isCharacterPartOfPreviousWord: Boolean): Int {
        var nextValidIndex = index
        val lastIndex = this.lastIndex
        if (!isNewIndexGreaterThanLastIndex(index)) {
            nextValidIndex = lastIndex + 1
        }
        if (!isCharacterPartOfPreviousWord && isSpaceCharacterAtIndex(index - 1)) {
            nextValidIndex += 1
        }
        this.lastIndex = nextValidIndex
        return nextValidIndex
    }

    private fun findMinimumIndexWithSpaceCharacterFromIndex(index: Int): Int {
        var newIndex = index
        while (newIndex >= 0 && line[newIndex] == SPACE_CHARACTER) {
            newIndex -= 1
        }
        return newIndex + 1
    }

    private fun indexIsInBounds(index: Int): Boolean {
        return index in 0 until lineLength
    }

    private fun completeLineWithSpaces() {
        for (i in 0 until lineLength) {
            line += SPACE_CHARACTER
        }
    }

    companion object {
        private const val SPACE_CHARACTER = ' '
    }

    init {
        this.lineLength = lineLength / PDFLayoutTextStripper.OUTPUT_SPACE_CHARACTER_WIDTH_IN_PT
        completeLineWithSpaces()
    }
}

internal class Character(
    val characterValue: Char,
    var index: Int,
    val isCharacterPartOfPreviousWord: Boolean,
    private val isFirstCharacterOfAWord: Boolean,
    val isCharacterAtTheBeginningOfNewLine: Boolean,
    val isCharacterCloseToPreviousWord: Boolean
) {
    override fun toString(): String {
        var toString = ""
        toString += index
        toString += " "
        toString += characterValue
        toString += " isCharacterPartOfPreviousWord=$isCharacterPartOfPreviousWord"
        toString += " isFirstCharacterOfAWord=$isFirstCharacterOfAWord"
        toString += " isCharacterAtTheBeginningOfNewLine=$isCharacterAtTheBeginningOfNewLine"
        toString += " isCharacterPartOfASentence=$isCharacterCloseToPreviousWord"
        toString += " isCharacterCloseToPreviousWord=$isCharacterCloseToPreviousWord"
        return toString
    }

    init {
        if (PDFLayoutTextStripper.DEBUG) println(this.toString())
    }
}

internal class CharacterFactory(private val firstCharacterOfLineFound: Boolean) {
    private var previousTextPosition: TextPosition? = null
    private var isCharacterPartOfPreviousWord = false
    private var isFirstCharacterOfAWord = false
    private var isCharacterAtTheBeginningOfNewLine = false
    private var isCharacterCloseToPreviousWord = false
    fun createCharacterFromTextPosition(
        textPosition: TextPosition,
        previousTextPosition: TextPosition?
    ): Character {
        setPreviousTextPosition(previousTextPosition)
        isCharacterPartOfPreviousWord = isCharacterPartOfPreviousWord(textPosition)
        isFirstCharacterOfAWord = isFirstCharacterOfAWord(textPosition)
        isCharacterAtTheBeginningOfNewLine = isCharacterAtTheBeginningOfNewLine(textPosition)
        isCharacterCloseToPreviousWord = isCharacterCloseToPreviousWord(textPosition)
        val character = getCharacterFromTextPosition(textPosition)
        val index =
            textPosition.x.toInt() / PDFLayoutTextStripper.OUTPUT_SPACE_CHARACTER_WIDTH_IN_PT
        return Character(
            character,
            index,
            isCharacterPartOfPreviousWord,
            isFirstCharacterOfAWord,
            isCharacterAtTheBeginningOfNewLine,
            isCharacterCloseToPreviousWord
        )
    }

    private fun isCharacterAtTheBeginningOfNewLine(textPosition: TextPosition): Boolean {
        if (!firstCharacterOfLineFound) {
            return true
        }
        val previousTextPosition: TextPosition? = getPreviousTextPosition()
        val previousTextYPosition: Float = previousTextPosition!!.y
        return textPosition.y.roundToInt() < previousTextYPosition.roundToInt()
    }

    private fun isFirstCharacterOfAWord(textPosition: TextPosition): Boolean {
        if (!firstCharacterOfLineFound) {
            return true
        }
        val numberOfSpaces = numberOfSpacesBetweenTwoCharacters(previousTextPosition, textPosition)
        return numberOfSpaces > 1 || isCharacterAtTheBeginningOfNewLine(textPosition)
    }

    private fun isCharacterCloseToPreviousWord(textPosition: TextPosition): Boolean {
        if (!firstCharacterOfLineFound) {
            return false
        }
        val numberOfSpaces = numberOfSpacesBetweenTwoCharacters(previousTextPosition, textPosition)
        return numberOfSpaces > 1 && numberOfSpaces <= PDFLayoutTextStripper.OUTPUT_SPACE_CHARACTER_WIDTH_IN_PT
    }

    private fun isCharacterPartOfPreviousWord(textPosition: TextPosition): Boolean {
        val previousTextPosition: TextPosition? = getPreviousTextPosition()
        if (previousTextPosition!!.unicode.equals(" ")) {
            return false
        }
        val numberOfSpaces = numberOfSpacesBetweenTwoCharacters(previousTextPosition, textPosition)
        return numberOfSpaces <= 1
    }

    private fun numberOfSpacesBetweenTwoCharacters(
        textPosition1: TextPosition?,
        textPosition2: TextPosition
    ): Double {
        val previousTextXPosition: Float = textPosition1!!.x
        val previousTextWidth: Float = textPosition1.width
        val previousTextEndXPosition = previousTextXPosition + previousTextWidth
        return abs((textPosition2.x - previousTextEndXPosition).roundToInt())
            .toDouble()
    }

    private fun getCharacterFromTextPosition(textPosition: TextPosition): Char {
        val string: String = textPosition.unicode
        return string[0]
    }

    private fun getPreviousTextPosition(): TextPosition? {
        return previousTextPosition
    }

    private fun setPreviousTextPosition(previousTextPosition: TextPosition?) {
        this.previousTextPosition = previousTextPosition
    }

}*/