package edu.uw.colind4.calendar_planner

<<<<<<< HEAD
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
=======
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.ResultReceiver
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_map.*

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


>>>>>>> ca418b5ac0de3a62d0fa766c36419ef768e78c58

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        setContentView(R.layout.activity_main)
=======
        setContentView(R.layout.activity_event_map)
        test_button.setOnClickListener { fetchAddressButtonHander() }

        updateWidget()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
>>>>>>> ca418b5ac0de3a62d0fa766c36419ef768e78c58

        //check if we have access to users location
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
<<<<<<< HEAD

        test_btn.setOnClickListener {
            val intent = Intent(this,map_activity::class.java).apply {
                putExtra("address", "smith qtower")
            }
            startActivity(intent)
=======
        startIntentService()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setContentView(R.layout.activity_main)
        addAddButton()
    }

    private fun updateWidget() {
        val intent = Intent(this, HomeWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, AppWidgetManager::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
        sendBroadcast(intent)
    }

    private fun addAddButton() {
        newEventBtn.setOnClickListener {
            val intent = Intent(this, AddEvent::class.java)
            this.startActivity(intent)
>>>>>>> ca418b5ac0de3a62d0fa766c36419ef768e78c58
        }

    }


}
