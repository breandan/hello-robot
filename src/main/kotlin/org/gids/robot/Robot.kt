package org.gids.robot

import org.gids.robot.speech.ASRService
import org.gids.robot.speech.ListeningState
import org.gids.robot.tts.TTSService

// Run this and say "Hello robot!"
fun main(args: Array<String>) {
    val ttService = TTSService
    val asrService = ASRService()
    asrService.init()
    ListeningState.activate()
}