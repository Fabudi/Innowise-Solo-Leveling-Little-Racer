package inc.fabudi.littleracer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.DialogFragment
import inc.fabudi.littleracer.R

class ConfiguratorDialog : DialogFragment() {
    lateinit var listener: Listener
    lateinit var dialogType: DialogType
    lateinit var closeButton: Button
    lateinit var proceedButton: Button
    lateinit var speedEditText: EditText
    lateinit var ppEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.card)
        val view = inflater.inflate(R.layout.configurator, container, false)
        closeButton = view.findViewById(R.id.close_button)
        closeButton.setOnClickListener { dismiss() }
        proceedButton = view.findViewById(R.id.add_button)
        speedEditText = view.findViewById(R.id.speed_edittext)
        ppEditText = view.findViewById(R.id.tires_edittext)

        when (dialogType) {
            DialogType.CAR -> {
                view.findViewById<View>(R.id.passengers_layout).visibility = View.VISIBLE
                proceedButton.setOnClickListener {
                    val editTextValue = view.findViewById<EditText>(R.id.passengers_edittext)
                    var value: Int? = null
                    var speed: Int? = null
                    var pp: Float? = null
                    try {
                        value = editTextValue.text.toString().toInt()
                        speed = speedEditText.text.toString().toInt()
                        pp = ppEditText.text.toString().toFloat()
                    } catch (_: NumberFormatException) {
                    }
                    val valid =
                        validate(speedEditText) && validate(ppEditText) && validate(editTextValue) && validateForBounds(
                            pp, 0f, 100f
                        )
                    if (!validate(speedEditText)) speedEditText.error = "Null input"
                    if (!validate(ppEditText)) ppEditText.error = "Null input"
                    if (!validateForBounds(pp, 0f, 100f)) ppEditText.error = "0-100"
                    if (!validate(editTextValue)) editTextValue.error = "Null input"
                    if (valid) {
                        listener.addClick(dialogType, speed!!, pp!!, value)
                        dismiss()
                    }
                }
            }

            DialogType.TRUCK -> {
                view.findViewById<View>(R.id.weight_layout).visibility = View.VISIBLE
                proceedButton.setOnClickListener {
                    val editTextValue = view.findViewById<EditText>(R.id.weight_edittext)
                    var value: Double? = null
                    var speed: Int? = null
                    var pp: Float? = null
                    try {
                        value = editTextValue.text.toString().toDouble()
                        speed = speedEditText.text.toString().toInt()
                        pp = ppEditText.text.toString().toFloat()
                    } catch (_: NumberFormatException) {
                    }
                    val valid =
                        validate(speedEditText) && validate(ppEditText) && validate(editTextValue) && validateForBounds(
                            pp, 0f, 100f
                        )
                    if (!validate(speedEditText)) speedEditText.error = "Null input"
                    if (!validate(ppEditText)) ppEditText.error = "Null input"
                    if (!validateForBounds(pp, 0f, 100f)) ppEditText.error = "0-100"
                    if (!validate(editTextValue)) editTextValue.error = "Null input"
                    if (valid) {
                        listener.addClick(dialogType, speed!!, pp!!, value)
                        dismiss()
                    }
                }
            }

            DialogType.MOTORBIKE -> {
                view.findViewById<View>(R.id.sidecar_layout).visibility = View.VISIBLE
                proceedButton.setOnClickListener {
                    val value = view.findViewById<Switch>(R.id.sidecar_switch).isChecked
                    var speed: Int? = null
                    var pp: Float? = null
                    try {
                        speed = speedEditText.text.toString().toInt()
                        pp = ppEditText.text.toString().toFloat()
                    } catch (_: NumberFormatException) {
                    }
                    val valid =
                        validate(speedEditText) && validate(ppEditText) && validateForBounds(
                            pp, 0f, 100f
                        )
                    if (!validate(speedEditText)) speedEditText.error = "Null input"
                    if (!validate(ppEditText)) ppEditText.error = "Null input"
                    if (!validateForBounds(pp, 0f, 100f)) ppEditText.error = "0-100"
                    if (valid) {
                        listener.addClick(dialogType, speed!!, pp!!, value)
                        dismiss()
                    }
                }
            }
        }
        return view
    }

    private fun validateForBounds(value: Float?, min: Float, max: Float): Boolean {
        if (value == null) return false
        return value in min..max
    }

    private fun validate(editText: EditText) = editText.text.toString() != ""

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface Listener {
        fun <T> addClick(key: DialogType, speed: Int, pp: Float, value: T)
    }

    enum class DialogType {
        CAR, TRUCK, MOTORBIKE
    }

}