package statistics

import clock.MockClock
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.Duration
import java.time.Instant


private const val INACCURACY = 1e-4
private const val MINUTES_PER_HOUR = 60

class EventsStatisticTest {

    private fun incEvent(statistics: EventsStatistic, eventName: String, times: Int) {
        repeat(times) {
            statistics.incEvent(eventName)
        }
    }

    @Test
    fun `no events test`() {
        // Prepare data
        val statistics = EventsStatisticImpl(MockClock())

        // Assert
        assertTrue(statistics.getAllEventStatistic().isEmpty())
    }

    @Test
    fun `correct simple scenario`() {
        // Prepare data
        val statistics = EventsStatisticImpl(MockClock())
        val expectedFirst = 1.0 / MINUTES_PER_HOUR
        val expectedSecnod = 2.0 / MINUTES_PER_HOUR

        // Action
        incEvent(statistics, "first",  1)
        incEvent(statistics, "second", 2)

        // Assert
        assertEquals(expectedFirst,  statistics.getEventStatisticByName("first"),  INACCURACY)
        assertEquals(expectedSecnod, statistics.getEventStatisticByName("second"), INACCURACY)
    }


    @Test
    fun `non-existing name test`() {
        // Prepare data
        val statistics = EventsStatisticImpl(MockClock())
        val expectedNonExisting = 0.0

        // Action
        statistics.incEvent("first")

        // Assert
        assertEquals(expectedNonExisting, statistics.getEventStatisticByName("non-existing"), INACCURACY)
    }

    @Test
    fun `all statistics test`() {
        // Prepare data
        val statistics = EventsStatisticImpl(MockClock())
        val expectedFirst = 1.0 / MINUTES_PER_HOUR
        val expectedSecond = 3.0 / MINUTES_PER_HOUR

        // Action
        incEvent(statistics,"first",  1)
        incEvent(statistics,"second", 3)
        val allStatistic = statistics.getAllEventStatistic()

        // Assert
        assertEquals(2, allStatistic.size)
        assertEquals(expectedFirst,  allStatistic["first"]!!,  INACCURACY)
        assertEquals(expectedSecond, allStatistic["second"]!!, INACCURACY)
    }

    @Test
    fun `complex test`() {
        // Prepare data
        val clock = MockClock()
        val statistics = EventsStatisticImpl(clock)
        val expectedFirst = 0.0
        val expectedSecond = 2.0 / MINUTES_PER_HOUR

        // Action
        incEvent(statistics,"first", 2)
        clock.setCurTime(Instant.now().plus(Duration.ofHours(1)))
        incEvent(statistics,"second", 2)
        val allStatistic = statistics.getAllEventStatistic()

        // Assert
        assertEquals(expectedFirst,  statistics.getEventStatisticByName("first"),  INACCURACY)
        assertEquals(expectedSecond, statistics.getEventStatisticByName("second"), INACCURACY)
        assertEquals(expectedSecond, allStatistic["second"]!!, INACCURACY)
    }

}