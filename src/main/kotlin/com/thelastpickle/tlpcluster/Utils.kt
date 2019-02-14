package com.thelastpickle.tlpcluster

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.InputStream
import java.io.FileOutputStream


class Utils {
    companion object {
        fun inputstreamToTempFile(inputStream : InputStream, prefix : String, suffix : String, cwd : String) : File {
            val tempFile = File.createTempFile(prefix, suffix, File(cwd))
            tempFile.deleteOnExit()

            val outputStream = FileOutputStream(tempFile)

            IOUtils.copy(inputStream, outputStream)
            outputStream.flush()
            outputStream.close()

            return tempFile
        }

        fun prompt(question: String, default: String) : String {
            print("$question [$default]: ")
            var line = (readLine() ?: default).trim()

            if(line.equals(""))
                line = default

            return line
        }

    }
}