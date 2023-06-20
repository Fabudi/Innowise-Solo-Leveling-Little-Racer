package inc.fabudi.littleracer

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var carFab: FloatingActionButton
    private lateinit var truckFab: FloatingActionButton
    private lateinit var motorbikeFab: FloatingActionButton
    private lateinit var addFab: FloatingActionButton
    private var isActive = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carFab = findViewById(R.id.car_fab)
        truckFab = findViewById(R.id.truck_fab)
        motorbikeFab = findViewById(R.id.motorbike_fab)
        addFab = findViewById(R.id.add_fab)
        addFab.setOnClickListener {
            if (!isActive) motorbikeFab.show() else motorbikeFab.hide()
            if (!isActive) truckFab.show() else truckFab.hide()
            if (!isActive) carFab.show() else carFab.hide()
            if (!isActive)
                addFab.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.rotate_forward
                    )
                )
            else addFab.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotate_backward
                )
            )
            isActive = !isActive
        }

    }
}