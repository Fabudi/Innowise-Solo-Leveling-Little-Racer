package inc.fabudi.littleracer.data

class Motorbike(
    speed: Int, punctureProbability: Float, private val sideCar: Boolean, distanceToRide: Int
) :
    Car(speed, punctureProbability, distanceToRide) {

}