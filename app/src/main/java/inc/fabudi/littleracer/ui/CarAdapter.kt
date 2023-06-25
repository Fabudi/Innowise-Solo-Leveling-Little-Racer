package inc.fabudi.littleracer.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import inc.fabudi.littleracer.R
import inc.fabudi.littleracer.data.Car

class CarAdapter(private val cars: ArrayList<Car>) : RecyclerView.Adapter<CarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.car_item, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position], position) { remove(position) }
    }

    fun add() {
        notifyItemInserted(cars.lastIndex)
    }

    private fun remove(position: Int) {
        cars.removeAt(position)
        notifyItemRemoved(position)
    }
}