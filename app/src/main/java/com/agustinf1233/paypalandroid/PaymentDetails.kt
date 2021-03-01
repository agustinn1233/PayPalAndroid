package com.agustinf1233.paypalandroid

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject


class PaymentDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        val intent = intent
        try {
            val jsonDetails = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(
                jsonDetails.getJSONObject("response"),
                intent.getStringExtra("PaymentAmount")!!
            )
        } catch (e: JSONException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(JSONException::class)
    private fun showDetails(
        jsonDetails: JSONObject,
        paymentAmount: String
    ) {

        val textViewId = findViewById<View>(R.id.txtId) as TextView
        val textViewStatus =
            findViewById<View>(R.id.txtStatus) as TextView
        val textViewAmount =
            findViewById<View>(R.id.txtAmount) as TextView

        textViewId.text = jsonDetails.getString("id")
        textViewStatus.text = jsonDetails.getString("state")
        textViewAmount.text = "$paymentAmount USD"
    }
}