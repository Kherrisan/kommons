package cn.kherrisan.kommons

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun main() {
    val msg = "Hello World"
    msg.compress(Gzip).decompress(Gzip) shouldBe "Hello World"
}

fun String.compress(c: Compressor): ByteArray {
    return c.compress(this.toByteArray(StandardCharsets.UTF_8))
}

fun ByteArray.compress(c: Compressor): ByteArray {
    return c.compress(this)
}

fun ByteArray.decompress(c: Compressor): ByteArray {
    return c.decompress(this)
}

interface Compressor {
    fun compress(bytes: ByteArray): ByteArray
    fun decompress(bytes: ByteArray): ByteArray
}

object Lz4 : Compressor {
    override fun compress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun decompress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}

object Deflate : Compressor {
    override fun compress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun decompress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}

object Snappy : Compressor {
    override fun compress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun decompress(bytes: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}

object Gzip : Compressor {
    override fun compress(bytes: ByteArray): ByteArray {
        if (bytes.isEmpty()) {
            return ByteArray(0)
        }
        val bos = ByteArrayOutputStream()
        val gos = GZIPOutputStream(bos)
        gos.write(bytes)
        return bos.toByteArray()
    }

    override fun decompress(bytes: ByteArray): ByteArray {
        if (bytes.isEmpty()) {
            return ByteArray(0)
        }
        val bis = ByteArrayInputStream(bytes)
        val gis = GZIPInputStream(bis)
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var n = gis.read(buffer)
        while (n >= 0) {
            bos.write(buffer)
            n = gis.read(buffer)
        }
        return bos.toByteArray()
    }
}