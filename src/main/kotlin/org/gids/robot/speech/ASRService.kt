package org.gids.robot.speech

import org.gids.robot.speech.ASRControlLoop
import org.gids.robot.speech.ListeningState
import org.gids.robot.microphone.CustomLiveSpeechRecognizer
import edu.cmu.sphinx.api.Configuration
import org.gids.robot.tts.TTSService

import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class ASRService {
    private lateinit var speechThread: Thread
    private lateinit var recognizer: CustomLiveSpeechRecognizer

    fun init() {
        val configuration = Configuration()
        configuration.acousticModelPath = ACOUSTIC_MODEL
        configuration.dictionaryPath = DICTIONARY_PATH
        configuration.grammarPath = GRAMMAR_PATH
        configuration.useGrammar = true
        configuration.grammarName = "command"

        try {
            recognizer = CustomLiveSpeechRecognizer(configuration)
            //            com.gids.robot.microphone.setMasterGain(MASTER_GAIN);
            speechThread = Thread(ASRControlLoop(recognizer), "ASR Thread")
            recognizer.startRecognition(true)
            // Fire up control-loop
            speechThread.start()
        } catch (e: IOException) {
            logger.log(Level.SEVERE, "Couldn't initialize speech com.gids.robot.microphone:", e)
        }
    }

    fun deactivate(): Boolean {
        return ListeningState.standBy()
    }

    companion object {
        val MASTER_GAIN = 0.85
        val CONFIDENCE_LEVEL_THRESHOLD = 0.5

        private val ACOUSTIC_MODEL = "resource:/edu.cmu.sphinx.models.en-us/en-us"
        private val DICTIONARY_PATH = "resource:/edu.cmu.sphinx.models.en-us/cmudict-en-us.dict"
        private val GRAMMAR_PATH = "resource:/org.gids.robot/grammars"

        private val logger = Logger.getLogger(ASRService::class.java.simpleName)
    }
}

// This is for testing purposes
fun main(args: Array<String>) {
    val ttService = TTSService
    val asrService = ASRService()
    asrService.init()
    ListeningState.activate()
}
