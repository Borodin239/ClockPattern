package clock

import java.time.Instant

class MockClock(private var curTime: Instant = Instant.now()): Clock {
    override fun instant(): Instant = curTime

    fun setCurTime(now: Instant) {
        curTime = now
    }
}