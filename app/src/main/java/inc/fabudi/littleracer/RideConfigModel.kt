package inc.fabudi.littleracer

import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.data.Motorbike
import inc.fabudi.littleracer.data.PassengerCar
import inc.fabudi.littleracer.data.Truck
import inc.fabudi.littleracer.ui.ConfiguratorDialog

class RideConfigModel {
    fun <T> add(type: ConfiguratorDialog.DialogType, speed: Int, pp: Float, value: T) {
        cars.add(when (type) {
            ConfiguratorDialog.DialogType.CAR -> PassengerCar(speed, pp, value as Int)
            ConfiguratorDialog.DialogType.TRUCK -> Truck(speed, pp, value as Double)
            ConfiguratorDialog.DialogType.MOTORBIKE -> Motorbike(speed, pp, value as Boolean)
        })
    }

    var cars = ArrayList<Car>()
    var fabStateActive = false
    var trackLength = 0.0
}