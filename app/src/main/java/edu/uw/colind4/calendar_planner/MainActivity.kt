package edu.uw.colind4.calendar_planner

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {


        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

            // Display the address string
            // or an error message sent from the intent service.
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY) ?: ""
            Toast.makeText(this@MainActivity, addressOutput, Toast.LENGTH_LONG)

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(this@MainActivity, getString(R.string.address_found),Toast.LENGTH_LONG)
            }

        }
    }

    private var lastLocation: Location? = null
    private val resultReceiver: AddressResultReceiver = AddressResultReceiver(handler = Handler())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { fetchAddressButtonHander() }
    }

    // ...

    private fun startIntentService() {

        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
        }
        startService(intent)
    }

    fun fetchAddressButtonHander() {

            if (!Geocoder.isPresent()) {
                Toast.makeText(this@MainActivity,
                    R.string.no_geocoder_available,
                    Toast.LENGTH_LONG).show()
                return
            }


            startIntentService()

    }


}
