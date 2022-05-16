package com.example.collegeapp.ui.schedule

import android.util.Log
import com.tom_roush.pdfbox.contentstream.PDFStreamEngine
import kotlin.Throws
import com.tom_roush.pdfbox.cos.COSBase
import com.tom_roush.pdfbox.contentstream.operator.OperatorName
import com.tom_roush.pdfbox.cos.COSName
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject
import kotlin.jvm.JvmStatic
import com.example.collegeapp.ui.schedule.PrintImageLocations
import com.tom_roush.pdfbox.contentstream.operator.DrawObject
import com.tom_roush.pdfbox.contentstream.operator.Operator
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.contentstream.operator.state.Concatenate
import com.tom_roush.pdfbox.contentstream.operator.state.SetGraphicsStateParameters
import com.tom_roush.pdfbox.contentstream.operator.state.Save
import com.tom_roush.pdfbox.contentstream.operator.state.Restore
import com.tom_roush.pdfbox.contentstream.operator.state.SetMatrix
import com.tom_roush.pdfbox.cos.COSFloat
import com.tom_roush.pdfbox.cos.COSInteger
import java.io.File
import java.io.IOException

class PrintImageLocations : PDFStreamEngine() {
    val objectSet = mutableSetOf<Float>()

    /**
     * This is used to handle an operation.
     *
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     *
     * @throws IOException If there is an error processing the operation.
     */
    @Throws(IOException::class)
    override fun processOperator(operator: Operator, operands: List<COSBase>) {
        val operation = operator.name
        val tag = "SOME_TAG_FOR_SEARCHING"
        //Log.d(tag, operation)
        if (OperatorName.APPEND_RECT == operation) {
            val objectName = operands[0] as COSFloat
            val objectName2 = operands[1] as COSFloat
            val objectName3 = operands[2] as COSFloat
            val objectName4 = if (operands[3] is COSInteger) operands[3] as COSInteger else operands[3] as COSFloat
            objectSet.addAll(arrayOf(objectName.floatValue(),
                                     objectName.floatValue() + objectName3.floatValue()))

            //Log.d(tag, "$objectName $objectName2 $objectName3 $objectName4")
//            Log.d(tag, objectName2.toString())
//            Log.d(objectName3.toString())
//            Log.d(objectName4.toString())
//
            //Log.d(tag, objectName.toString())
//            val xobject = resources.getXObject(objectName)
//            if (xobject is PDImageXObject) {
//                val image = xobject
//                val imageWidth = image.width
//                val imageHeight = image.height
//                println("*******************************************************************")
//                println("Found image [" + objectName.name + "]")
//                val ctmNew = graphicsState.currentTransformationMatrix
//                var imageXScale = ctmNew.scalingFactorX
//                var imageYScale = ctmNew.scalingFactorY
//
//                // position in user space units. 1 unit = 1/72 inch at 72 dpi
//                println("position in PDF = " + ctmNew.translateX + ", " + ctmNew.translateY + " in user space units")
//                // raw size in pixels
//                println("raw image size  = $imageWidth, $imageHeight in pixels")
//                // displayed size in user space units
//                println("displayed size  = $imageXScale, $imageYScale in user space units")
//                // displayed size in inches at 72 dpi rendering
//                imageXScale /= 72f
//                imageYScale /= 72f
//                println("displayed size  = $imageXScale, $imageYScale in inches at 72 dpi rendering")
//                // displayed size in millimeters at 72 dpi rendering
//                imageXScale *= 25.4f
//                imageYScale *= 25.4f
//                println("displayed size  = $imageXScale, $imageYScale in millimeters at 72 dpi rendering")
//                println()
//            } else if (xobject is PDFormXObject) {
//                showForm(xobject)
//            }
        } else {
            super.processOperator(operator, operands)
        }
    }

    companion object {
        /**
         * This will print the documents data.
         *
         * @param args The command line arguments.
         *
         * @throws IOException If there is an error parsing the document.
         */
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                usage()
            } else {
                PDDocument.load(File(args[0])).use { document ->
                    val printer = PrintImageLocations()
                    var pageNum = 0
                    for (page in document.pages) {
                        pageNum++
                        println("Processing page: $pageNum")
                        printer.processPage(page)
                    }
                    val tag = "ANOTHER_TAG"
                    Log.d(tag, printer.objectSet.toString())
                }
            }
        }

        /**
         * This will print the usage for this document.
         */
        private fun usage() {
            System.err.println("Usage: java " + PrintImageLocations::class.java.name + " <input-pdf>")
        }
    }

    /**
     * Default constructor.
     *
     * @throws IOException If there is an error loading text stripper properties.
     */
    init {
        addOperator(Concatenate())
        addOperator(DrawObject())
        addOperator(SetGraphicsStateParameters())
        addOperator(Save())
        addOperator(Restore())
        addOperator(SetMatrix())
    }
}