package com.thelastpickle.tlpcluster.containers

import com.thelastpickle.tlpcluster.Context
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

internal class CassandraUnpackTest {

    lateinit var downloadDir : Path
    lateinit var unpacker : CassandraUnpack
    lateinit var context: Context

    @BeforeEach
    fun setupUnpacker() {
        context = Context.testContext()
        downloadDir = Files.createTempDirectory("test")
        unpacker = CassandraUnpack(context, "2.1.14", downloadDir)
    }

    @AfterEach
    fun tearDownUnpacker() {
        FileUtils.deleteDirectory(downloadDir.toFile())
    }

    @Test
    fun ensureDownloadCreatesDebPackageAndConfFiles() {
        unpacker.download()

        assertThat(File(downloadDir.toFile(), "cassandra_2.1.14_all.deb")).isFile()
        assertThat(File(downloadDir.toFile(), "conf")).exists()

        unpacker.extractConf()

        assertThat(File(downloadDir.toFile(), "conf/cassandra.yaml")).isFile()
    }


    @Test
    fun getURL() {
        val expected = "http://dl.bintray.com/apache/cassandra/pool/main/c/cassandra/cassandra_2.1.14_all.deb"
        assertThat(unpacker.getURL()).isEqualTo(expected)
    }

    @Test
    fun getFileName() {
    }

    @Test
    fun getVersion() {
    }

    @Test
    fun getDest() {
    }

    @Test
    fun ensureDebExistsBeforeExtracting() {
        Assertions.assertThatIllegalStateException().isThrownBy { unpacker.extractConf() }
                .withMessageContaining("Check failed")

    }

    @Test
    fun testCache() {
        val cache = Files.createTempDirectory("cache")
        unpacker = CassandraUnpack(context, "2.1.14", downloadDir, Optional.of(cache))
        unpacker.download()
        assertThat(unpacker.cacheHits).isEqualTo(0)
        assertThat(unpacker.cacheChecks).isEqualTo(1)

        unpacker.download()
        assertThat(unpacker.cacheChecks).isEqualTo(2)
        assertThat(unpacker.cacheHits).isEqualTo(1)

    }
}