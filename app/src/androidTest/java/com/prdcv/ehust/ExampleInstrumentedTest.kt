package com.prdcv.ehust

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.minio.MinioClient
import io.minio.PutObjectArgs

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.ByteArrayInputStream
import kotlin.random.Random

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.prdcv.ehust", appContext.packageName)
    }

    @Test
    fun upload() {
        val client = MinioClient.builder()
            .endpoint("http://104.215.150.77:9000")
            .credentials("iylnllsY1LIML1Kc", "NbjEzIcII2jjwCDju73D89urAdAGVrQx")
            .build()

        val bytes = Random(System.currentTimeMillis()).nextBytes(4096)
        val inputStream = ByteArrayInputStream(bytes)

        try {
            client.putObject(
                PutObjectArgs.builder()
                    .bucket("attachment")
                    .`object`("test")
                    .stream(
                        inputStream,
                        inputStream.available().toLong(),
                        -1
                    )
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}