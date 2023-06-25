package inc.fabudi.littleracer.data

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.Random

open class Car(
    val speed: Int,
    val punctureProbability: Float
) {
    private var distanceToRide: Int = 0
    var distanceTraveled: Int = 0
    private fun punctureWheel() = Random().nextFloat() <= punctureProbability

    fun runAsync() = GlobalScope.async {
        while (distanceToRide > 0) {
            if (punctureWheel()) delay(5000)
            else {
                distanceToRide -= speed
                distanceTraveled += speed
                delay(1000)
            }
        }
    }
}