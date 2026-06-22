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

/**
 * Contract test for the lifecycle-event routing established by SDK-5007.
 *
 * It does NOT reproduce the real transient-Activity overlay end to end; it pins down WHICH source
 * each app-lifecycle event is wired to, guarding against the most likely regression: accidentally
 * moving "Application Opened" / "Application Backgrounded" back onto React's host-activity
 * (LifecycleEventListener) callbacks, which is exactly the bug this PR fixes.
 *
 * Pure JVM test (no emulator / Robolectric): the listener constructor's AppVersion path is
 * neutralized because the mocked Application returns a null PackageManager (AppVersion bails out),
 * and RNPreferenceManager / RudderClient are mocked statically via MockK.
 */
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
    fun onHostPause_doesNotEmitApplicationBackgrounded() {
        listener.onHostPause()

        verify(exactly = 0) { rudderClient.track(applicationBackgrounded) }
        verify(exactly = 0) { rudderClient.track(applicationBackgrounded, any<RudderProperty>()) }
    }

    @Test
    fun onHostResume_doesNotEmitApplicationOpened() {
        listener.onHostResume()

        verify(exactly = 0) { rudderClient.track(applicationOpened, any<RudderProperty>()) }
    }

    // --- Process lifecycle callbacks MUST emit app lifecycle events ---

    @Test
    fun onStart_emitsApplicationOpened() {
        listener.onStart(lifecycleOwner)

        verify(exactly = 1) { rudderClient.track(applicationOpened, any<RudderProperty>()) }
    }

    @Test
    fun onStop_emitsApplicationBackgrounded() {
        listener.onStop(lifecycleOwner)

        verify(exactly = 1) { rudderClient.track(applicationBackgrounded) }
    }

    // --- from_background: false on cold start, true after a background round-trip ---

    @Test
    fun applicationOpened_fromBackgroundReflectsBackgroundRoundTrip() {
        val properties = mutableListOf<RudderProperty>()

        listener.onStart(lifecycleOwner) // cold start -> from_background = false
        listener.onStop(lifecycleOwner)  // app goes to background
        listener.onStart(lifecycleOwner) // return to foreground -> from_background = true

        verify(exactly = 2) { rudderClient.track(applicationOpened, capture(properties)) }
        assertEquals(false, properties[0].getProperty(fromBackground))
        assertEquals(true, properties[1].getProperty(fromBackground))
    }
}
