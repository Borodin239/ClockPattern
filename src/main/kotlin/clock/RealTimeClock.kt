package clock

import java.time.Instant

class RealTimeClock : Clock {
    override fun instant(): Instant {
        return Instant.now()
    }
}