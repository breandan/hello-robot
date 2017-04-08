package org.gids.robot.speech

import org.gids.robot.tts.TTSService
import java.util.*

/**
 * Grammar is defined in resources/org.gids.robot/grammars/command.gram
 */

class SpeechCallback(val result: String){
    val GREETINGS: Array<String> = arrayOf("hello", "hi")

    init {
        if(result.split(" ")[0] in GREETINGS) {
            TTSService.say("Hello Nikhil")
        } else {
            TTSService.say(result)
        }
    }
}
