package com.agustinf1233.paypalandroid


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agustinf1233.paypalandroid.Config.Config
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    private val PAYPAL_REQUEST_CODE = 200
    private var edtAmount: EditText? = null
    private var amount = ""

    // User for testing in payment screen.
    // User > sb-54olb5257515@personal.example.com
    // Password > Password01

    private val config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Config.PAYPAL_CLIENT_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        startService(intent)

        val btnPayNow = findViewById<Button>(R.id.btnPayNow)
        edtAmount = findViewById(R.id.edtAmount)

        btnPayNow.setOnClickListener { getPayment() }
    }

    private fun getPayment() {
        amount = edtAmount?.getText().toString()
        val payment = PayPalPayment(BigDecimal(java.lang.String.valueOf(amount)), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onDestroy() {
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val confirmation: PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null) {
                    val paymentDetails = confirmation.toJSONObject().toString(4);
                    try {
                        startActivity(Intent(this, PaymentDetails::class.java)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}