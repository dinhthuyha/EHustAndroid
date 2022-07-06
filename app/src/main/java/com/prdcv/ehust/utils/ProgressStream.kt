package com.prdcv.ehust.utils

import java.io.InputStream

class ProgressStream(
    private val inputStream: InputStream,
    private val callback: (progress: Float) -> Unit
) : InputStream() {
    private val total: Int = inputStream.available()
    private var progress = 0f

    override fun read(): Int {
        return inputStream.read().also {
            if (it != -1) updateProgress(1)
        }
    }

    override fun read(b: ByteArray?): Int {
        return inputStream.read(b).also {
            if (it != -1) updateProgress(it)
        }
    }

    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        return inputStream.read(b, off, len).also {
            if (it != -1) updateProgress(it)
        }
    }

    override fun close() {
        inputStream.close()
    }

    override fun skip(n: Long): Long {
        return inputStream.skip(n).also {
            updateProgress(it.toInt())
        }
    }

    override fun available(): Int {
        return inputStream.available()
    }

    override fun mark(readlimit: Int) {
        inputStream.mark(readlimit)
    }

    override fun markSupported(): Boolean = false

    private fun updateProgress(i: Int) {
        progress += i
        callback(progress / total)
    }
}