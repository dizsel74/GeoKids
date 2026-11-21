package com.example.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object FeedbackManager {
    private const val TAG = "FeedbackManager"
    private const val SAMPLE_RATE = 22050
    private const val CLICK_DURATION_MS = 28

    private val audioLock = Any()
    private var clickTrack: AudioTrack? = null
    private val clickPcm: ShortArray by lazy { generateClickPcm() }

    /**
     * Triggers a subtle, crisp tactile vibration for toggle interactions.
     */
    fun vibrateToggle(context: Context, view: View) {
        try {
            val vibrator = getVibrator(context)
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val amplitude = if (vibrator.hasAmplitudeControl()) {
                        100
                    } else {
                        VibrationEffect.DEFAULT_AMPLITUDE
                    }
                    vibrator.vibrate(VibrationEffect.createOneShot(25L, amplitude))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(25L)
                }
                return
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibrator unavailable, falling back to view haptic feedback", e)
        }
        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    /**
     * Plays a short, crisp UI click sound for Next / Back navigation actions.
     */
    fun playClick(view: View) {
        synchronized(audioLock) {
            try {
                val track = ensureClickTrack()
                if (track != null && track.state == AudioTrack.STATE_INITIALIZED) {
                    if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                        track.stop()
                    }
                    track.reloadStaticData()
                    track.play()
                    return
                }
            } catch (e: Exception) {
                Log.w(TAG, "AudioTrack click failed, falling back to system click sound", e)
                releaseTrackLocked()
            }
        }
        view.playSoundEffect(SoundEffectConstants.CLICK)
    }

    fun release() {
        synchronized(audioLock) {
            releaseTrackLocked()
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun ensureClickTrack(): AudioTrack? {
        clickTrack?.let { existing ->
            if (existing.state == AudioTrack.STATE_INITIALIZED) {
                return existing
            }
            releaseTrackLocked()
        }

        val samples = clickPcm
        val bufferSizeBytes = samples.size * 2

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val format = AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        val track = AudioTrack.Builder()
            .setAudioAttributes(attributes)
            .setAudioFormat(format)
            .setBufferSizeInBytes(bufferSizeBytes)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        if (track.state != AudioTrack.STATE_INITIALIZED) {
            track.release()
            return null
        }

        val written = track.write(samples, 0, samples.size)
        if (written <= 0) {
            track.release()
            return null
        }

        clickTrack = track
        return track
    }

    private fun releaseTrackLocked() {
        try {
            clickTrack?.stop()
            clickTrack?.release()
        } catch (_: Exception) {
        }
        clickTrack = null
    }

    private fun generateClickPcm(): ShortArray {
        val sampleCount = (SAMPLE_RATE * CLICK_DURATION_MS) / 1000
        val buffer = ShortArray(sampleCount)
        var phase = 0.0
        for (i in 0 until sampleCount) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / sampleCount
            // Crisp pitch drop (1100 Hz -> 480 Hz) for a soft tactile UI click
            val freq = 1100.0 - (620.0 * progress)
            phase += 2.0 * PI * freq / SAMPLE_RATE

            val attack = if (t < 0.0012) t / 0.0012 else 1.0
            val decay = exp(-t * 145.0)
            val envelope = attack * decay

            val sample = (sin(phase) * envelope * 0.55 * Short.MAX_VALUE)
                .toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            buffer[i] = sample.toShort()
        }
        return buffer
    }
}
