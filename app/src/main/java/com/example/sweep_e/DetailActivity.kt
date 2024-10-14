package com.example.sweep_e

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import com.google.android.material.slider.RangeSlider
import java.text.DecimalFormat
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {

    private lateinit var speedSlider : RangeSlider
    private val handler = Handler()
    private var lastSliderValueRunnable: Runnable? = null

    private lateinit var fanSwitch : Switch
    private lateinit var autoModeSwitch: Switch

    private lateinit var btn_strafeLeft : ImageButton
    private lateinit var btn_strafeRight : ImageButton
    private lateinit var btn_strafeUp : ImageButton
    private lateinit var btn_strafeDown : ImageButton

    private lateinit var btn_end : Button

    val MOVEMENTSTATE : Array<String> = arrayOf("IDLE", "LEFT", "RGHT", "FWRD", "BACK")

    private fun refreshState(){
        fanSwitch.isChecked = false
        autoModeSwitch.isChecked = false
        speedSlider.setValues(50f)

        Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
        Firebase.setDatabaseValue("FAN", isChecked = fanSwitch.isChecked)
        Firebase.setDatabaseValue("AUTO", isChecked = autoModeSwitch.isChecked)
        Firebase.setDatabaseValue("SPEED", speedValue = speedSlider.values[0].toDouble())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // utility
            // camera

            // fan
        fanSwitch = findViewById(R.id.fanSwitch)
        fanSwitch.setOnCheckedChangeListener { _, isChecked ->
            println("Fan is" + (if (isChecked) "check" else "uncheck"))
            Firebase.setDatabaseValue( "FAN",isChecked = isChecked)
        }

            // auto mode
        autoModeSwitch = findViewById(R.id.autoModeSwitch)
        autoModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            println("Auto Mode is " + if(isChecked) "check" else "uncheck")
            Firebase.setDatabaseValue( "AUTO",isChecked = isChecked)
        }

            // speed slider
        speedSlider = findViewById(R.id.sliderSpeed)
        speedSlider.valueFrom = 0f
        speedSlider.valueTo = 100f

        speedSlider.addOnChangeListener{ slider, value, fromuser ->
            val decimalFormat = DecimalFormat("#.##")
            val speedMultiplier = decimalFormat.format(slider.values[0]).toDouble()

            lastSliderValueRunnable?.let { handler.removeCallbacks(it) }
            lastSliderValueRunnable = Runnable {
                Firebase.setDatabaseValue( "SPEED", speedValue = speedMultiplier)
            }
            handler.postDelayed(lastSliderValueRunnable!!, 200)
        }

        // movement
            // left
        btn_strafeLeft = findViewById(R.id.strafeLeftButton)
        btn_strafeLeft.setOnTouchListener { _, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    println("Left button hold")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[1])
                }
                MotionEvent.ACTION_UP -> {
                    println("Left button up")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
                }
            }
            true
        }
            // right
        btn_strafeRight = findViewById(R.id.strafeRightButton)
        btn_strafeRight.setOnTouchListener { _, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    println("Right button hold")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[2])
                }
                MotionEvent.ACTION_UP -> {
                    println("Right button up")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
                }
            }
            true
        }
            // up
        btn_strafeUp = findViewById(R.id.strafeUpButton)
        btn_strafeUp.setOnTouchListener { _, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    println("Up button hold")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[3])
                }
                MotionEvent.ACTION_UP -> {
                    println("Up button up")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
                }
            }
            true
        }
            // down
        btn_strafeDown = findViewById(R.id.strafeDownButton)
        btn_strafeDown.setOnTouchListener { _, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    println("Down button hold")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[4])
                }
                MotionEvent.ACTION_UP -> {
                    println("Down button up")
                    Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
                }
            }
            true
        }


        // button end
        btn_end = findViewById(R.id.btn_end)
        btn_end.setOnClickListener {
            refreshState()
            finish()
        }

        refreshState()
    }
}