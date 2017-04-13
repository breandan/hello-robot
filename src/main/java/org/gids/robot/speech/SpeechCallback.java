package org.gids.robot.speech;

/**
 * Grammar is defined in resources/org.gids.robot/grammars/command.gram
 */

public class SpeechCallback{
    public SpeechCallback(String result) {
        if(result.contains("hello")) {
            TTSService.getInstance().say("Hello Nikhil");
        } else if (result.matches("[A-Za-z]")) {
            TTSService.getInstance().say(result);
        }
    }
}
