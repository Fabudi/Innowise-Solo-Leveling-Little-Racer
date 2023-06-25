package inc.fabudi.littleracer.data

class Motorbike(
    speed: Int, punctureProbability: Float, val sideCar: Boolean
) : Car(speed, punctureProbability)