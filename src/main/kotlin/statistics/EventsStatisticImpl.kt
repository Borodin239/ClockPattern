package statistics

import clock.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


class EventsStatisticImpl(
    private val clock: Clock
): EventsStatistic {

    private val map: HashMap<String, Queue<Instant>> = HashMap()
    private val MINUTES_PER_HOUR = 60.0

    override fun incEvent(name: String) {
        map.putIfAbsent(name, ArrayDeque())
        // Check above guarantees presence of `name` in map
        map[name]!!.add(clock.instant())
    }

    override fun getEventStatisticByName(name: String): Double {
        val now: Instant = clock.instant()
        val queue = map[name] ?: return 0.0
        removeOldEvents(queue, now)
        return queue.size / MINUTES_PER_HOUR
    }

    private fun removeOldEvents(queue: Queue<Instant>, now: Instant) {
        while (!queue.isEmpty() && queue.peek().plus(1, ChronoUnit.HOURS).isBefore(now)) {
            queue.poll()
        }
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        val answer = HashMap<String, Double>()
        for (eventName in map.keys) {
            val stat = getEventStatisticByName(eventName)
            answer[eventName] = stat
        }
        return answer
    }

    override fun printStatistic() {
        println(getAllEventStatistic())
    }
}