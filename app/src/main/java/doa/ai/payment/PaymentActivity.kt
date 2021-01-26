package doa.ai.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import doa.ai.R
import android.app.Activity
import android.widget.TextView
import com.google.android.gms.wallet.PaymentsClient
import android.view.View
import doa.ai.model.ItemInfo
import kotlinx.android.synthetic.main.activity_payment.*
import com.google.android.gms.wallet.IsReadyToPayRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import android.content.Intent
import org.json.JSONException
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import com.google.android.gms.wallet.PaymentDataRequest


class PaymentActivity : AppCompatActivity() {

    private var mPaymentsClient: PaymentsClient? = null

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    private val mBikeItem = ItemInfo("Simple Bike", 300 * 1000000, R.drawable.common_google_signin_btn_icon_light_normal)
    private val mShippingCost = (90 * 1000000).toLong()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        mPaymentsClient = PaymentsUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()

        googlePayButton.setOnClickListener { view ->
            requestPayment(view)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest
        if (!isReadyToPayJson.isPresent)
        {
            return
        }
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = mPaymentsClient?.isReadyToPay(request)
        task?.addOnCompleteListener {
            if (task.isSuccessful) {
                setGooglePayAvailable(task.result!!)
            } else {
                Log.w("isReadyToPay failed", task.exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayStatus.visibility = View.GONE
            googlePayButton.visibility = View.VISIBLE
        } else {
            googlePayStatus.text = R.string.googlepay_status_unavailable.toString()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val paymentData = PaymentData.getFromIntent(data!!)
                        paymentData?.let { handlePaymentSuccess(it) }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        handleError(status!!.statusCode)
                    }
                }// Nothing to here normally - the user simply cancelled without selecting a
                // payment method.
                // Do nothing.

                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        val paymentMethodData: JSONObject

        try {
            paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("token") == "examplePaymentMethodToken") {
                val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage(
                                "Gateway name set to \"example\" - please modify " + "Constants.java and replace it with your own gateway.")
                        .setPositiveButton("OK", null)
                        .create()
                alertDialog.show()
            }

            val billingName = paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)
            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG)
                    .show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"))
        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $e")
            return
        }

    }

    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestPayment(view: View) {
        // Disables the button to prevent multiple clicks.
        googlePayButton.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val price = PaymentsUtil.microsToString(mBikeItem.priceMicros + mShippingCost)

        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price)
        if (!paymentDataRequestJson.isPresent) {
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            mPaymentsClient?.loadPaymentData(request)?.let {
                AutoResolveHelper.resolveTask(
                        it, this, LOAD_PAYMENT_DATA_REQUEST_CODE)
            }
        }
    }
}
