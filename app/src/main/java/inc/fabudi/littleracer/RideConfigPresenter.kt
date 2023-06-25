package inc.fabudi.littleracer

import inc.fabudi.littleracer.ui.ConfiguratorDialog

class RideConfigPresenter(private val model: RideConfigModel) : ConfiguratorDialog.Listener {
    private var view: MainActivity? = null

    fun attachView(view: MainActivity) {
        this.view = view
        onViewAttached(this.view!!)
    }

    private fun onViewAttached(view: MainActivity) {
        view.setupOnClicks()
        view.setupRecyclerView(model.cars)
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
        if (length == "" || length.toDouble() <= 0) {
            view!!.showError("Wrong distance")
            return
        }
        if (model.cars.size == 0) {
            view!!.showWarning("Zero racers")
            return
        }
        model.trackLength = length.toDouble()
        view!!.proceed()
    }
}