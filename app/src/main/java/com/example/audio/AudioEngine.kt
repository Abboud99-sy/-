package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.sin

class AudioEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (e: Exception) {
            Log.e("AudioEngine", "Error initializing audio components", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val arLocale = Locale("ar")
            val result = tts?.setLanguage(arLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.language = Locale.getDefault()
            }
            tts?.setPitch(1.15f) // Friendly voice tone for kids
            tts?.setSpeechRate(0.85f) // Clear, slower pronunciation
            isTtsReady = true
        } else {
            Log.e("AudioEngine", "TTS initialization failed")
        }
    }

    fun speak(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UtteranceId_$text")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun playCorrectSound() {
        thread {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                Thread.sleep(130)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 180)
            } catch (e: Exception) {
                playSynthTone(600, 150)
            }
        }
    }

    fun playWrongSound() {
        thread {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 220)
            } catch (e: Exception) {
                playSynthTone(250, 200)
            }
        }
    }

    fun playPopSound() {
        thread {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 70)
            } catch (e: Exception) {
                playSynthTone(800, 50)
            }
        }
    }

    fun playVictorySound() {
        thread {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_A, 100)
                Thread.sleep(110)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_B, 100)
                Thread.sleep(110)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_C, 120)
                Thread.sleep(130)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_D, 180)
            } catch (e: Exception) {
                playSynthTone(1000, 200)
            }
        }
    }

    private fun playSynthTone(frequencyHz: Int, durationMs: Int) {
        try {
            val sampleRate = 22050
            val numSamples = durationMs * sampleRate / 1000
            val samples = DoubleArray(numSamples)
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                samples[i] = sin(2.0 * Math.PI * i.toDouble() / (sampleRate.toDouble() / frequencyHz))
                buffer[i] = (samples[i] * 32767).toInt().toShort()
            }

            val audioTrack = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
                buffer.size * 2,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong())
            audioTrack.release()
        } catch (e: Exception) {
            Log.e("AudioEngine", "Error playing synth tone", e)
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            toneGenerator?.release()
        } catch (e: Exception) {
            Log.e("AudioEngine", "Error shutting down audio", e)
        }
    }
}
