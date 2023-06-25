package inc.fabudi.littleracer

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.ui.CarAdapter
import inc.fabudi.littleracer.ui.ConfiguratorDialog
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.CAR
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.MOTORBIKE
import inc.fabudi.littleracer.ui.ConfiguratorDialog.DialogType.TRUCK

class MainActivity : AppCompatActivity() {
    private lateinit var carFab: FloatingActionButton
    private lateinit var truckFab: FloatingActionButton
    private lateinit var motorbikeFab: FloatingActionButton
    private lateinit var addFab: FloatingActionButton
    private lateinit var startButton: Button
    private lateinit var lengthEditText: EditText
    private lateinit var recyclerList: RecyclerView
    private lateinit var adapter: CarAdapter

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
    }

    fun getLength() = lengthEditText.text.toString()

    fun showDialog(type: DialogType) {
        ConfiguratorDialog().apply {
            this.listener = presenter
            this.dialogType = type
        }.show(supportFragmentManager, type.name)
    }

    fun proceed() = startActivity(Intent(this, RideActivity::class.java))

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

}