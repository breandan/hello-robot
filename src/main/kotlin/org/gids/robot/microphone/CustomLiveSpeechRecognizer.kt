package org.gids.robot.microphone

import edu.cmu.sphinx.api.AbstractSpeechRecognizer
import edu.cmu.sphinx.api.Configuration
import edu.cmu.sphinx.decoder.ResultListener
import edu.cmu.sphinx.frontend.endpoint.SpeechClassifier
import edu.cmu.sphinx.frontend.util.StreamDataSource
import org.gids.robot.microphone.CustomMicrophone

import java.io.IOException

/**
 * High-level class for live speech recognition.
 */
class CustomLiveSpeechRecognizer

/**
 * Constructs new live recognition object.

 * @param configuration common configuration
 * *
 * @throws IOException if model IO went wrong
 */
@Throws(IOException::class)
constructor(configuration: Configuration) : AbstractSpeechRecognizer(configuration) {
    private val microphone: CustomMicrophone = CustomMicrophone(16000f, 16, true, false)

    // sphinx4 default sensitivity is 13.
    private val SPEECH_SENSITIVITY = 20

    init {
        context.getInstance(StreamDataSource::class.java).setInputStream(microphone.stream)
        context.setLocalProperty(String.format("speechClassifier->%s", SpeechClassifier.PROP_THRESHOLD), SPEECH_SENSITIVITY)
    }

    /**
     * Starts recognition process.

     * @param clear clear cached microphone data
     * *
     * @see CustomLiveSpeechRecognizer.stopRecognition
     */
    fun startRecognition(clear: Boolean) {
        recognizer.allocate()
        microphone.startRecording()
    }

    /**
     * Stops recognition process.

     * Recognition process is paused until the next call to startRecognition.

     * @see CustomLiveSpeechRecognizer.startRecognition
     */
    fun stopRecognition() {
        microphone.stopRecording()
        recognizer.deallocate()
    }

    fun addResultListener(listener: ResultListener) {
        recognizer.addResultListener(listener)
    }

    fun removeResultListener(listener: ResultListener) {
        recognizer.removeResultListener(listener)
    }


    //    public void setMasterGain(double mg) {
    //        microphone.setMasterGain(mg);
    //    }
    //
    //    public void setNoiseLevel(double mg) {
    //        microphone.setNoiseLevel(mg);
    //    }
}
