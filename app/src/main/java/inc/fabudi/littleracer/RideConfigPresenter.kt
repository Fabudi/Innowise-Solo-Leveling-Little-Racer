package inc.fabudi.littleracer

import inc.fabudi.littleracer.data.Car
import inc.fabudi.littleracer.ui.ConfiguratorDialog
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class RideConfigPresenter(private val model: RideConfigModel) : ConfiguratorDialog.Listener {
    private var view: MainActivity? = null

    fun attachView(view: MainActivity) {
        this.view = view
        onViewAttached(this.view!!)
    }

    private fun onViewAttached(view: MainActivity) {
        view.setupOnClicks()
        view.setupRecyclerView(model.cars)
        view.showRepeatButtons(false)
        view.addDebugCars()
    }

    fun detachView() {
        view = null
        onViewDetached()
    }

    private fun onViewDetached() {

    }

    override fun <T> addClick(
        key: ConfiguratorDialog.DialogType, speed: Int, pp: Float, value: T
    ) {
        model.add(key, speed, pp, value)
        view!!.notifyRecyclerViewAdd()
    }

    fun addFab() {
        view!!.playAddFabAnimation(!model.fabStateActive)
        view!!.showFabs(!model.fabStateActive)
        model.fabStateActive = !model.fabStateActive
    }

    fun openConfig(dialogType: ConfiguratorDialog.DialogType) {
        view!!.showDialog(dialogType)
    }

    fun validate() {
        val length = view!!.getLength()
        if (length == "" || length.toDouble() <= 0) return view!!.showError("Wrong distance")
        if (model.cars.size == 0) return view!!.showWarning("Zero racers")
        model.trackLength = length.toDouble()
        view!!.showFabs(false)
        view!!.showMainFab(false)
        view!!.showRaceRing(true)
        view!!.showTrackConfig(false)
        view!!.proceed()
    }

    suspend fun runCars() {
        model.restoreCarsDistance()
        view?.setDrawable(R.drawable.racer)
        view?.addCars(model.cars)
        val al = ArrayList<Deferred<Boolean>>()
        for (car in model.cars) {
            al.add(car.runAsync(this))
        }
        al.awaitAll()
        withContext(Dispatchers.Main) {
            view?.showRepeatButtons(true)
        }
    }

    fun puncture(car: Car) {
        view?.stopCar(car)
    }

    fun moved(car: Car) {
        view?.updatePosition(car)
        view?.notifyRecyclerViewPositionsChange()
    }

    fun menuClick() {
        model.restoreCarsDistance()
        view?.notifyRestore()
        view?.showMainFab(true)
        view?.showRepeatButtons(false)
        view?.showRaceRing(false)
        view?.showTrackConfig(true)
    }

    fun restart() {
        view?.showRepeatButtons(false)
        view?.proceed()
    }
}