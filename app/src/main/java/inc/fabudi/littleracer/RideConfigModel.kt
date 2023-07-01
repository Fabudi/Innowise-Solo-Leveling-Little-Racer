package inc.fabudi.littleracer

import android.graphics.Color
import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.data.Motorbike
import inc.fabudi.littleracer.data.PassengerCar
import inc.fabudi.littleracer.data.Truck
import inc.fabudi.littleracer.ui.ConfiguratorDialog

class RideConfigModel {
    fun <T> add(type: ConfiguratorDialog.DialogType, speed: Int, pp: Float, value: T) {
        cars.add(
            when (type) {
                ConfiguratorDialog.DialogType.CAR -> PassengerCar(
                    speed,
                    pp,
                    Color.RED,
                    value as Int
                )

                ConfiguratorDialog.DialogType.TRUCK -> Truck(
                    speed,
                    pp,
                    Color.BLACK,
                    value as Double
                )

                ConfiguratorDialog.DialogType.MOTORBIKE -> Motorbike(
                    speed,
                    pp,
                    Color.BLUE,
                    value as Boolean
                )
            }
        )
        cars[cars.lastIndex].id = cars[cars.lastIndex].hashCode()
    }

    fun restoreCarsDistance() {
        for (car in cars){
            car.distanceToRide = 0
            car.distanceTraveled = 0
            car.isEditable = true
        }
    }

    var cars = ArrayList<Car>()
    var fabStateActive = false
    var trackLength = 0.0
}