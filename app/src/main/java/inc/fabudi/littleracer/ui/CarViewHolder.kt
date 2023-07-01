package inc.fabudi.littleracer.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import inc.fabudi.littleracer.R
import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.data.Motorbike
import inc.fabudi.littleracer.data.PassengerCar
import inc.fabudi.littleracer.data.Truck

class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name = itemView.findViewById<TextView>(R.id.car_name)
    private val avatar = itemView.findViewById<View>(R.id.avatar)
    private val number = itemView.findViewById<TextView>(R.id.number)
    private val distanceIcon = itemView.findViewById<ImageView>(R.id.distance_icon)
    private val distanceLabel = itemView.findViewById<TextView>(R.id.distance_label)
    private val speedIcon = itemView.findViewById<ImageView>(R.id.speed_icon)
    private val speedLabel = itemView.findViewById<TextView>(R.id.speed_label)
    private val tiresIcon = itemView.findViewById<ImageView>(R.id.pp_icon)
    private val tiresLabel = itemView.findViewById<TextView>(R.id.pp_label)
    private val additionalIcon = itemView.findViewById<ImageView>(R.id.additional_icon)
    private val additionalLabel = itemView.findViewById<TextView>(R.id.additional_label)
    private val removeButton = itemView.findViewById<AppCompatImageButton>(R.id.remove_button)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(car: Car, position: Int, listener: OnClickListener) {
        name.text = car::class.simpleName
        if (Color.luminance(car.color) < 0.5) number.setTextColor(Color.WHITE)
        avatar.backgroundTintList = ColorStateList.valueOf(car.color)
        number.text = (position + 1).toString()
        distanceLabel.text = "${car.distanceTraveled}km"
        speedLabel.text = "${car.speed}km/h"
        tiresLabel.text = "${(car.punctureProbability)}%"
        when (car) {
            is PassengerCar -> {
                additionalIcon.setBackgroundResource(R.drawable.baseline_people_24)
                additionalLabel.text = car.numberOfPassengers.toString()
            }

            is Motorbike -> {
                additionalIcon.setBackgroundResource(R.drawable.baseline_shopping_cart_24)
                additionalLabel.text = car.sideCar.toString()
            }

            is Truck -> {
                additionalIcon.setBackgroundResource(R.drawable.baseline_scale_24)
                additionalLabel.text = "${car.cargoWeight}kg"
            }
        }
        removeButton.visibility = if (car.isEditable) View.VISIBLE else View.INVISIBLE
        removeButton.setOnClickListener(listener)
    }
}