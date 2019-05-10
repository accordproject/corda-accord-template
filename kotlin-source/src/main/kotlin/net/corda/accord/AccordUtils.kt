package net.corda.accord

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object AccordUtils {

    /**
     * This function parses a legal document using a Cicero template and returns an input stream with the output from the terminal.
     * The script 'cicero-parse.sh writes the output of Cicero-parse to a temporary file, suppresses standard system-out messaging and then
     * logs out the JSON (which is captured in the input stream.
     */

    val stateFromContract: InputStream
        @Throws(IOException::class)
        get() {
            val root: String

            if (System.getenv("CORDAPP_ROOT") != null) {
                root = System.getenv("CORDAPP_ROOT")
            } else {
                root = "../"
            }

            val command = arrayOf("./scripts/cicero-parse.sh", "./node_modules/promissory-note", "./contract.txt")
            val ciceroParse = ProcessBuilder(*command)
            ciceroParse.directory(File(root))
            return ciceroParse.start().inputStream
        }


    /**
     * This function returns an input stream after compressing the file that is read from disk.
     */
    @Throws(IOException::class)
    fun getCompressed(`is`: InputStream): InputStream {
        val data = ByteArray(2048)
        val bos = ByteArrayOutputStream()
        val zos = ZipOutputStream(bos)
        val entryStream = BufferedInputStream(`is`, 2048)
        val entry = ZipEntry("")
        zos.putNextEntry(entry)
        val count: Int = entryStream.read(data, 0, 2048)
        while (count != -1) {
            zos.write(data, 0, count)
        }
        entryStream.close()
        zos.closeEntry()
        zos.close()

        return ByteArrayInputStream(bos.toByteArray())
    }
}
