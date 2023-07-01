package inc.fabudi.littleracer

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.data.Motorbike
import inc.fabudi.littleracer.data.PassengerCar
import inc.fabudi.littleracer.data.Truck
import inc.fabudi.littleracer.ui.CarAdapter
import inc.fabudi.littleracer.ui.ConfiguratorDialog
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.CAR
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.MOTORBIKE
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.TRUCK
import inc.fabudi.littleracer.ui.RaceRing
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var carFab: FloatingActionButton
    private lateinit var truckFab: FloatingActionButton
    private lateinit var motorbikeFab: FloatingActionButton
    private lateinit var addFab: FloatingActionButton
    private lateinit var startButton: Button
    private lateinit var menuButton: Button
    private lateinit var restartButton: Button
    private lateinit var lengthEditText: EditText
    private lateinit var trackConfigLayout: LinearLayout
    private lateinit var recyclerList: RecyclerView
    private lateinit var adapter: CarAdapter
    private var drawableId: Int = 0
    private lateinit var ringRace: RaceRing

    private var presenter: RideConfigPresenter = RideConfigPresenter(RideConfigModel())

    fun showFabs(bool: Boolean) {
        if (bool) motorbikeFab.show() else motorbikeFab.hide()
        if (bool) truckFab.show() else truckFab.hide()
        if (bool) carFab.show() else carFab.hide()
    }

    fun playAddFabAnimation(forward: Boolean) {
        addFab.startAnimation(
            AnimationUtils.loadAnimation(
                this, if (forward) R.anim.rotate_forward else R.anim.rotate_backward
            )
        )
    }

    fun setupOnClicks() {
        addFab.setOnClickListener {
            presenter.addFab()
        }
        carFab.setOnClickListener {
            presenter.openConfig(CAR)
        }
        truckFab.setOnClickListener {
            presenter.openConfig(TRUCK)
        }
        motorbikeFab.setOnClickListener {
            presenter.openConfig(MOTORBIKE)
        }
        startButton.setOnClickListener {
            presenter.validate()
        }
        menuButton.setOnClickListener {
            presenter.menuClick()
        }
        restartButton.setOnClickListener {
            presenter.restart()
        }
    }

    fun getLength() = lengthEditText.text.toString()

    fun showDialog(type: DialogType) {
        ConfiguratorDialog().apply {
            this.listener = presenter
            this.dialogType = type
        }.show(supportFragmentManager, type.name)
    }

    fun showRaceRing(bool: Boolean) {
        ringRace.visibility = if (bool) View.VISIBLE else View.GONE
    }

    fun showTrackConfig(bool: Boolean) {
        trackConfigLayout.visibility = if (bool) View.VISIBLE else View.GONE
    }

    fun proceed() = GlobalScope.launch {
        presenter.runCars()
    }


    fun showWarning(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carFab = findViewById(R.id.car_fab)
        truckFab = findViewById(R.id.truck_fab)
        motorbikeFab = findViewById(R.id.motorbike_fab)
        addFab = findViewById(R.id.add_fab)
        startButton = findViewById(R.id.start_button)
        lengthEditText = findViewById(R.id.length_edittext)
        recyclerList = findViewById(R.id.recycler_list)
        menuButton = findViewById(R.id.menu_button)
        restartButton = findViewById(R.id.restart_button)
        ringRace = findViewById(R.id.raceRing2)
        trackConfigLayout = findViewById(R.id.track_config_layout)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun setupRecyclerView(cars: ArrayList<Car>) {
        adapter = CarAdapter(cars)
        recyclerList.adapter = adapter
    }

    fun notifyRecyclerViewAdd() = adapter.add()
    fun showError(errorMessage: String) {
        lengthEditText.error = errorMessage
    }

    fun setDrawable(racer: Int) {
        drawableId = racer
    }

    fun addCars(cars: java.util.ArrayList<Car>) {
        ringRace.removeAll()
        for (car in cars) {
            car.distanceToRide = lengthEditText.text.toString().toInt()
            val drawable = (ResourcesCompat.getDrawable(
                resources,
                drawableId,
                null
            ) as StateListDrawable)
            val b = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.getStateDrawable(1) as LayerDrawable
            } else {
                TODO("VERSION.SDK_INT < Q")
            }
            val a = b.findDrawableByLayerId(R.id.racer_background) as GradientDrawable
            a.color = ColorStateList.valueOf(car.color)
            when (car) {
                is Truck -> b.setDrawableByLayerId(
                    R.id.racer_icon,
                    ResourcesCompat.getDrawable(resources, R.drawable.baseline_fire_truck_24, null)
                )

                is Motorbike -> b.setDrawableByLayerId(
                    R.id.racer_icon, ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_sports_motorsports_24, null
                    )
                )

                is PassengerCar -> b.setDrawableByLayerId(
                    R.id.racer_icon, ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_directions_car_24, null
                    )
                )
            }
            ringRace.addProgress(
                car.id.toString(),
                car.distanceTraveled / car.distanceToRide.toFloat() * 100,
                drawable
            )
        }
    }

    fun updatePosition(car: Car) {
        ringRace.setDrawableStateFor(car.hashCode().toString(), intArrayOf(0))
        ringRace.updateProgress(
            car.id.toString(), car.distanceTraveled / car.distanceToRide.toFloat() * 100
        )
    }

    fun stopCar(car: Car) {
        ringRace.setDrawableStateFor(
            car.id.toString(), intArrayOf(android.R.attr.state_checked)
        )
    }

    fun showMainFab(bool: Boolean) = if (bool) addFab.show() else addFab.hide()
    fun notifyRecyclerViewPositionsChange() = adapter.move()

    fun addDebugCars() {
        presenter.addClick(CAR, 12, 12f, 10)
        presenter.addClick(MOTORBIKE, 120, 52f, true)
        presenter.addClick(TRUCK, 24, 42f, 100.0)
    }

    fun showRepeatButtons(bool: Boolean) {
        findViewById<View>(R.id.repeat_layout).visibility = if (bool) View.VISIBLE else View.GONE
    }

    fun notifyRestore() {
        adapter.notifyDataSetChanged()
    }

}