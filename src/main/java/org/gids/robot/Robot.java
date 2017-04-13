package org.gids.robot;

import org.gids.robot.speech.ASRService;
import org.gids.robot.speech.TTSService;

/**
 * Created by breandanconsidine on 4/10/17.
 */
public class Robot {
    public static void main(String[] args) {
        TTSService.getInstance();
        ASRService asrService = new ASRService();
        asrService.init();
//        ListeningState.activate();
    }
}
