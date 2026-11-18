package com.rudderstack.react.android

import com.facebook.react.bridge.ReadableMap
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RNParamsConfiguratorTest {

    @Test
    fun `given trackDeepLinks is false, when config is built, then deep link tracking is disabled`() {
        val config = configWith(trackDeepLinks = false)

        val rudderConfig = RNParamsConfigurator(config).handleConfig().build()

        assertFalse(rudderConfig.isTrackDeepLinks)
    }

    @Test
    fun `given trackDeepLinks is omitted, when config is built, then deep link tracking remains enabled`() {
        val config = configWith()

        val rudderConfig = RNParamsConfigurator(config).handleConfig().build()

        assertTrue(rudderConfig.isTrackDeepLinks)
    }

    private fun configWith(trackDeepLinks: Boolean? = null): ReadableMap {
        val config = mockk<ReadableMap>()
        every { config.hasKey(any()) } answers {
            when (firstArg<String>()) {
                "writeKey" -> true
                "trackDeepLinks" -> trackDeepLinks != null
                else -> false
            }
        }
        every { config.getString("writeKey") } returns "write-key"
        if (trackDeepLinks != null) {
            every { config.getBoolean("trackDeepLinks") } returns trackDeepLinks
        }
        return config
    }
}
