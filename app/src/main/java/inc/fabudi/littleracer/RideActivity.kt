package inc.fabudi.littleracer

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import inc.fabudi.littleracer.ui.RaceRing
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

class RideActivity : AppCompatActivity() {
    private lateinit var ringRace: RaceRing
    var drawable: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride)
        ringRace = findViewById(R.id.raceRing)
        drawable = ResourcesCompat.getDrawable(resources, R.drawable.racer, null)
        for (i in 1..100) {
            ringRace.addProgress("$i", Random().nextFloat() * 100, drawable)
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            var counter = 0
            while (true) {
                for(i in 1..100){
                    if (counter % i == 0) ringRace.setDrawableStateFor(
                        "$i",
                        intArrayOf(android.R.attr.state_checked)
                    )
                    else {
                        ringRace.setDrawableStateFor("$i", intArrayOf(0))
                        ringRace.updateProgress("$i", ringRace.getProgress("$i")!! + .1f)
                    }
                }
                delay(10)
                ++counter
            }
        }

    }
}
