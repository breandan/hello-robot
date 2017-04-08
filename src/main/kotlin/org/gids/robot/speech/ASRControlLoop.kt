package org.gids.robot.speech

import org.gids.robot.microphone.CustomLiveSpeechRecognizer
import org.gids.robot.tts.TTSService
import java.awt.event.KeyEvent.*
import java.io.IOException
import java.util.logging.Level
import java.util.regex.Pattern
import javax.sound.sampled.AudioSystem

class ASRControlLoop(private val recognizer: CustomLiveSpeechRecognizer) : Runnable {
    override fun run() {
        while (!ListeningState.isTerminated) {
            // This blocks on a recognition result
            val result = resultFromRecognizer
            SpeechCallback(result)
        }
    }

    private val resultFromRecognizer: String
        get() {
            val result = recognizer.result

            println("Recognized: ")
            println("\tTop H:       " + result.result + " / " + result.result.bestToken + " / " + result.result.bestPronunciationResult)
            println("\tTop 3H:      " + result.getNbest(3))

            return result.hypothesis
        }

    private fun tellJoke() {
        TTSService.say("knock, knock, knock, knock, knock")

        var result: String? = null
        while ("who is there" != result) {
            result = resultFromRecognizer
        }

        TTSService.say("it's me, turtle")

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        while (!result!!.contains("wait who") && !result.contains("who are you")) {
            result = resultFromRecognizer
        }

        TTSService.say("turtle")
    }

    private fun pauseSpeech() {
        beep()
        var result: String
        while (ListeningState.isActive) {
            result = resultFromRecognizer
            if (result == "speech resume") {
                beep()
                break
            }
        }
    }


    @Synchronized fun beep() {
        val t = Thread {
            try {
                val clip = AudioSystem.getClip()
                val inputStream = AudioSystem.getAudioInputStream(
                        ASRService::class.java.getResourceAsStream("/org/gids/robot/gids/robot/sounds/beep.wav"))
                clip.open(inputStream)
                clip.start()
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }

        t.start()

        try {
            t.join()
        } catch (ignored: InterruptedException) {
        }
    }
}
