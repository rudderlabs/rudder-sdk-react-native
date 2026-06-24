package com.rudderstack.react.android

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderProperty
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RNLifeCycleEventListenerTest {

    private val applicationOpened = "Application Opened"
    private val applicationBackgrounded = "Application Backgrounded"
    private val fromBackground = "from_background"

    private lateinit var rudderClient: RudderClient
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var listener: RNLifeCycleEventListener

    @Before
    fun setUp() {
        mockkStatic(RudderClient::class)
        rudderClient = mockk(relaxed = true)
        every { RudderClient.getInstance() } returns rudderClient

        mockkStatic(RNPreferenceManager::class)
        every { RNPreferenceManager.getInstance(any()) } returns mockk(relaxed = true)

        val application = mockk<Application>()
        every { application.packageName } returns "com.rudderstack.test"
        every { application.packageManager } returns null // AppVersion bails out

        val userSessionPlugin = mockk<RNUserSessionPlugin>(relaxed = true)
        val instance = mockk<RNRudderSdkModuleImpl>(relaxed = true)
        lifecycleOwner = mockk(relaxed = true)

        listener = RNLifeCycleEventListener(application, userSessionPlugin, instance, true, false)

        // The constructor runs ApplicationStatusRunnable (Installed/Updated handling); discard any
        // interactions from it so each test asserts only on the callback it exercises.
        clearMocks(rudderClient, answers = false)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // --- React host callbacks must NOT emit app lifecycle events ---

    @Test
    fun `given host activity lifecycle, when onHostPause is called, then Application Backgrounded is not emitted`() {
        listener.onHostPause()

        verify(exactly = 0) { rudderClient.track(applicationBackgrounded) }
        verify(exactly = 0) { rudderClient.track(applicationBackgrounded, any<RudderProperty>()) }
    }

    @Test
    fun `given host activity lifecycle, when onHostResume is called, then Application Opened is not emitted`() {
        listener.onHostResume()

        verify(exactly = 0) { rudderClient.track(applicationOpened, any<RudderProperty>()) }
    }

    // --- Process lifecycle callbacks MUST emit app lifecycle events ---

    @Test
    fun `given process lifecycle, when onStart is called, then Application Opened is emitted`() {
        listener.onStart(lifecycleOwner)

        verify(exactly = 1) { rudderClient.track(applicationOpened, any<RudderProperty>()) }
    }

    @Test
    fun `given process lifecycle, when onStop is called, then Application Backgrounded is emitted`() {
        listener.onStop(lifecycleOwner)

        verify(exactly = 1) { rudderClient.track(applicationBackgrounded) }
    }

    // --- from_background: false on cold start, true after a background round-trip ---

    @Test
    fun `given a background round-trip, when the app is opened, then from_background is false on cold start and true on resume`() {
        val properties = mutableListOf<RudderProperty>()

        listener.onStart(lifecycleOwner) // cold start -> from_background = false
        listener.onStop(lifecycleOwner)  // app goes to background
        listener.onStart(lifecycleOwner) // return to foreground -> from_background = true

        verify(exactly = 2) { rudderClient.track(applicationOpened, capture(properties)) }
        assertEquals(false, properties[0].getProperty(fromBackground))
        assertEquals(true, properties[1].getProperty(fromBackground))
    }
}
