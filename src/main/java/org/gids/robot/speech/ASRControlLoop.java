package org.gids.robot.speech;

import edu.cmu.sphinx.api.SpeechResult;
import org.gids.robot.microphone.CustomLiveSpeechRecognizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.logging.Logger;

/**
 * Created by breandan on 10/23/2015.
 */
public class ASRControlLoop implements Runnable {
    private CustomLiveSpeechRecognizer recognizer;

    public ASRControlLoop(CustomLiveSpeechRecognizer recognizer) {
        // Start-up recognition facilities
        this.recognizer = recognizer;
    }


    private static final Logger logger = Logger.getLogger(ASRControlLoop.class.getSimpleName());

    @Override
    public void run() {
        while (!ListeningState.isTerminated()) {
            // This blocks on a recognition result
            String result = getResultFromRecognizer();
            new SpeechCallback(result);
        }
    }

    private String getResultFromRecognizer() {
        SpeechResult result = recognizer.getResult();

        System.out.println("Recognized: ");
        System.out.println("\tTop H:       " + result.getResult() + " / " + result.getResult().getBestToken() + " / " + result.getResult().getBestPronunciationResult());
        System.out.println("\tTop 3H:      " + result.getNbest(3));

        logger.info("Recognized:    ");
        logger.info("\tTop H:       " + result.getResult() + " / " + result.getResult().getBestToken() + " / " + result.getResult().getBestPronunciationResult());
        logger.info("\tTop 3H:      " + result.getNbest(3));

        return result.getHypothesis();
    }


    // Helpers

    public static synchronized void beep() {
        Thread t = new Thread(() -> {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    ASRService.class.getResourceAsStream("/com.jetbrains.idear/sounds/beep.wav"));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    });

        t.start();

        try {
            t.join();
        } catch (InterruptedException ignored) {
        }
    }

    private static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }
}