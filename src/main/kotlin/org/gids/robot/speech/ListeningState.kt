package org.gids.robot.speech

import org.gids.robot.speech.ListeningState.Status.*
import java.util.concurrent.atomic.AtomicReference

/**
 * State machine for activating speech recognition.
 */
object ListeningState {
    private val status = AtomicReference(INIT)

    enum class Status {
        INIT,
        ACTIVE,
        STANDBY,
        TERMINATED
    }

    private fun setStatus(s: Status): Status {
        return status.getAndSet(s)
    }

    fun getStatus(): Status {
        return status.get()
    }

    val isTerminated: Boolean
        get() = getStatus() == TERMINATED

    val isInit: Boolean
        get() = getStatus() == INIT

    val isActive: Boolean
        get() = getStatus() == ACTIVE

    fun standBy(): Boolean {
        return STANDBY == setStatus(STANDBY)
    }

    fun activate(): Boolean {
        return ACTIVE == setStatus(ACTIVE)
    }
}
