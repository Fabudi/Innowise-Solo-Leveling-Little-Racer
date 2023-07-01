package inc.fabudi.littleracer.data

class Motorbike(
    speed: Int, punctureProbability: Float, color: Int, val sideCar: Boolean
) : Car(speed, punctureProbability, color)