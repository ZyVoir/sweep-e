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

    val SWITCHSTATE : Array<String> = arrayOf("ON", "OFF")
    val MOVEMENTSTATE : Array<String> = arrayOf("STOP", "LEFT", "RIGHT", "UP", "DOWN")

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
        }

            // auto mode
        autoModeSwitch = findViewById(R.id.autoModeSwitch)
        autoModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            println("Auto Mode is " + if(isChecked) "check" else "uncheck")
        }

            // speed slider
        speedSlider = findViewById(R.id.sliderSpeed)
        speedSlider.valueFrom = 0f
        speedSlider.valueTo = 100f

        speedSlider.addOnChangeListener{ slider, value, fromuser ->
            val speedMultiplier = slider.values
            lastSliderValueRunnable?.let { handler.removeCallbacks(it) }
            lastSliderValueRunnable = Runnable {
                // TOOD : perform write to RTDB
                println("$speedMultiplier")

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
                }
                MotionEvent.ACTION_UP -> {
                    println("Left button up")
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
                }
                MotionEvent.ACTION_UP -> {
                    println("Right button up")
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
                }
                MotionEvent.ACTION_UP -> {
                    println("Up button up")
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
                }
                MotionEvent.ACTION_UP -> {
                    println("Down button up")
                }
            }
            true
        }


        // button end
        btn_end = findViewById(R.id.btn_end)
        btn_end.setOnClickListener {
            finish()
        }
    }
}