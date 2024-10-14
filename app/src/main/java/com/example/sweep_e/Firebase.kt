package com.example.sweep_e

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Firebase {

    companion object {
        val db : FirebaseDatabase = FirebaseDatabase.getInstance("https://sweep-e-default-rtdb.asia-southeast1.firebasedatabase.app")

        fun setDatabaseValue(refPath : String ,strafeState : String){
            val dbref : DatabaseReference = db.getReference(refPath)
            dbref.setValue(strafeState)
        }

        fun setDatabaseValue(refPath : String ,isChecked : Boolean){
            val dbref : DatabaseReference = db.getReference(refPath)
            dbref.setValue(isChecked)
        }

        fun setDatabaseValue(refPath : String ,speedValue : Double){
            val dbref : DatabaseReference = db.getReference(refPath)
            dbref.setValue(speedValue)
        }
    }

}