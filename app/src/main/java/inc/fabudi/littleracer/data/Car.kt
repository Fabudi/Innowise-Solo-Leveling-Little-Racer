package inc.fabudi.littleracer.data

import android.util.Log
import inc.fabudi.littleracer.RideConfigPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Random

open class Car(
    val speed: Int, val punctureProbability: Float, val color: Int
) {

    var id = 0

    var isEditable = true

    var distanceToRide = 0

    var distanceTraveled = 0
    private fun punctureWheel() = Random().nextFloat() <= punctureProbability / 100

    fun runAsync(presenter: RideConfigPresenter) = GlobalScope.async {
        isEditable = false
        while (distanceTraveled < distanceToRide) {
            if (punctureWheel()) {
                Log.d(id.toString(), "AAAAARHHH!! WHERE IS MY WHEELS?!")
                presenter.puncture(this@Car)
                delay(1000)
            } else {
                if (distanceTraveled + speed > distanceToRide) distanceTraveled = distanceToRide
                else distanceTraveled += speed
                withContext(Dispatchers.Main) { presenter.moved(this@Car) }
                delay(50)
            }
        }
        true
    }
}