package edu.uw.colind4.calendar_planner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_map.*


class map_activity : AppCompatActivity(), OnMapReadyCallback {
    private var lastLocation: Location? = null
    private val resultReceiver: AddressResultReceiver = AddressResultReceiver(handler = Handler())
    private lateinit var mMap: GoogleMap
    private var address: String = ""
    private var day: String = ""
    private var month: String = ""
    private var year: String = ""
    private var title: String = ""
    private var notes: String = ""
    private var time: String = ""
    private var reminder: String = ""


    // Internal receiver class
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            // Display the address string
            // or an error message sent from the intent service.
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY) ?: ""
            val lat = resultData?.getDouble(Constants.LAT_KEY) ?: 0.0
            val long = resultData?.getDouble(Constants.LONG_KEY) ?: 0.0

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                if (address.equals("")) {
                    Toast.makeText(
                        this@map_activity, "This event doesn't have an address!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                // Add a marker for the given event address
                val event_location = LatLng(lat, long)
                val descriptor = BitmapDescriptorFactory.fromResource(R.drawable.star_marker)
                mMap.addMarker(
                    MarkerOptions().position(event_location).title("${title.capitalize()}").snippet(
                        ("$addressOutput ($address) \n On $day/$month/$year at $time\n Notes: $notes\n Reminder: $reminder")
                    ).icon(descriptor)
                ).showInfoWindow()

            } else {
                Toast.makeText(
                    this@map_activity, "Error getting address. Found this instead: $addressOutput",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_map)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get address to geocode with service from another activity
        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address")
        }
        if (intent.hasExtra("day")) {
            day = intent.getIntExtra("day", 0).toString()

        }
        if (intent.hasExtra("month")) {
            month = intent.getIntExtra("month", 0 ).toString()

        }
        if (intent.hasExtra("year")) {
            year = intent.getIntExtra("year", 0).toString()

        }
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")

        }
        if (intent.hasExtra("notes")) {
            notes = intent.getStringExtra("notes")

        }
        if (intent.hasExtra("time")) {
            time = intent.getLongExtra("time", 0).toString()

        }
        if (intent.hasExtra("reminder")) {
            reminder = intent.getStringExtra("reminder")

        }

        if (!Geocoder.isPresent()) {
            Toast.makeText(
                this,
                R.string.no_geocoder_available,
                Toast.LENGTH_LONG
            ).show()
            return
        } else {
            startIntentService()
        }

    }

    private fun startIntentService() {
        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.ADDRESS_KEY, address)
            putExtra("DAY", day)
            putExtra("MONTH", month)
            putExtra("YEAR", year)
            putExtra("TITLE", title)
            putExtra("NOTES", notes)
            putExtra("REMINDER", reminder)
            putExtra("TIME", time)

        }
        startService(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@map_activity)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val pos = LatLng(
                    it.latitude,
                    it.longitude
                )
                val descriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_icon)
                mMap.addMarker(
                    MarkerOptions().position(pos).title("Your Current Location").icon(
                        descriptor
                    )
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5.0f))
            }
        }
    }

    } 


