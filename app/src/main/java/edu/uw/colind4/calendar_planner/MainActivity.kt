package edu.uw.colind4.calendar_planner

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_map.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var lastLocation: Location? = null
    private val resultReceiver: AddressResultReceiver = AddressResultReceiver(handler = Handler())
    private lateinit var mMap: GoogleMap
//    private lateinit var resultReceiver: AddressResultReceiver

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            // Display the address string
            // or an error message sent from the intent service.
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY) ?: ""
            val lat = resultData?.getDouble(Constants.LAT_KEY) ?: 0.0
            val long = resultData?.getDouble(Constants.LONG_KEY) ?: 0.0




            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(
                    this@MainActivity, R.string.address_found,
                    Toast.LENGTH_SHORT
                ).show()

                // Add a marker in Sydney and move the camera
                val event_location = LatLng(lat, long)
                mMap.addMarker(MarkerOptions().position(event_location).title("Marker at $addressOutput"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(event_location))
            } else {
                Toast.makeText(
                    this@MainActivity, "Error getting address. Found this instead: $addressOutput",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_map)
        test_button.setOnClickListener { fetchAddressButtonHander() }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun startIntentService() {
        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
        }
        startService(intent)
    }

    fun fetchAddressButtonHander() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(
                this@MainActivity,
                R.string.no_geocoder_available,
                Toast.LENGTH_LONG
            ).show()
            return
        } else {
//            Toast.makeText(this@MainActivity, R.string.address_found, Toast.LENGTH_LONG).show()
        }
        startIntentService()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setContentView(R.layout.activity_main)

        newEventBtn.setOnClickListener {
            val intent = Intent(this, add_event::class.java)
            this.startActivity(intent)
        }
    }
}
