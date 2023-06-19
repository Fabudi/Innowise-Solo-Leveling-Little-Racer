package inc.fabudi.littleracer.data

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.Random

open class Car(
    private val speed: Int, private val punctureProbability: Float, var distanceToRide: Int
) {

    private fun punctureWheel() = Random().nextFloat() <= punctureProbability

    fun runAsync() = GlobalScope.async {
        while (distanceToRide > 0) {
            if (punctureWheel()) delay(5000)
            else {
                distanceToRide -= speed
                delay(1000)
            }
        }
    }
}