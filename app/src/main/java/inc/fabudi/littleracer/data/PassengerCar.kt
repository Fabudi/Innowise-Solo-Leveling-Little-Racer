package inc.fabudi.littleracer.data

class PassengerCar(
    speed: Int, punctureProbability: Float, private val numberOfPassengers: Int, distanceToRide: Int
) : Car(speed, punctureProbability, distanceToRide)