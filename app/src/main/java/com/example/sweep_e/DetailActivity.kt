package com.example.sweep_e

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import com.google.android.material.slider.RangeSlider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var camDB : DatabaseReference
    private lateinit var camView : ImageView

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

    fun setBase64ImageToImageView(base64String: String, imageView: ImageView) {
        // Decode the Base64 string to a byte array
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Convert the byte array into a Bitmap
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        imageView.setImageBitmap(bitmap)
    }

    private fun refreshState(){
        fanSwitch.isChecked = false
        autoModeSwitch.isChecked = false
        speedSlider.setValues(100f)

        Firebase.setDatabaseValue("STRAFE", MOVEMENTSTATE[0])
        Firebase.setDatabaseValue("FAN", isChecked = fanSwitch.isChecked)
        Firebase.setDatabaseValue("AUTO", isChecked = autoModeSwitch.isChecked)
        Firebase.setDatabaseValue("SPEED", speedValue = (speedSlider.values[0].toDouble() * 0.65)/100)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // utility
            // camera
        camView = findViewById(R.id.camView)
        camView.rotation = 180f
        camDB = Firebase.db.getReference("CAM")
        camDB.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val camBase64Value = snapshot.getValue(String::class.java)
                if (camBase64Value != null){
                    setBase64ImageToImageView(camBase64Value, camView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database Error: ${error.message}")
            }

        })

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
            val speedMultiplier = (decimalFormat.format((slider.values[0] * 0.65)/ 100)).toDouble()

            lastSliderValueRunnable?.let { handler.removeCallbacks(it) }
            lastSliderValueRunnable = Runnable {
                println("$speedMultiplier")
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