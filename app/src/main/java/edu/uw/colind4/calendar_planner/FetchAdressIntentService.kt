package edu.uw.colind4.calendar_planner

import android.app.IntentService
import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

object Constants {
    const val SUCCESS_RESULT = 0
    const val FAILURE_RESULT = 1
    const val PACKAGE_NAME = "edu.uw.colind4.calendar_planner"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
    const val LAT_KEY = "LATITUDE"
    const val LONG_KEY = "LONGITUDE"
    const val ADDRESS_KEY = "${PACKAGE_NAME}.ADDRESS"
}


class FetchAddressIntentService : IntentService("getLatLong") {
    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {

        val location = intent!!.getStringExtra(Constants.ADDRESS_KEY)
        receiver = intent!!.getParcelableExtra(Constants.RECEIVER)
        val bias = " Seattle, WA"

        var addresses: List<Address> = emptyList()

        val geocoder = Geocoder(this, Locale.getDefault())
        var errorMessage = ""
        try {
            addresses = geocoder.getFromLocationName(location + bias, 1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available)
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_address_used)
            Log.e(
                TAG, "$errorMessage. Latitude = $location.latitude , " +
                        "Longitude =  $location.longitude", illegalArgumentException
            )
        }

        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_results_found)
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, 0.0, 0.0)
        } else {

            val address = addresses.get(0)
            val lat = address.latitude
            val long = address.longitude

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            deliverResultToReceiver(
                Constants.SUCCESS_RESULT,
                addressFragments.joinToString(separator = "\n"),
                lat, long
            )
        }
    }

    private fun deliverResultToReceiver(
        resultCode: Int,
        message: String,
        latitude: Double,
        longitude: Double
    ) {
        val bundle = Bundle().apply {
            putString(Constants.RESULT_DATA_KEY, message)
            putDouble(Constants.LAT_KEY, latitude)
            putDouble(Constants.LONG_KEY, longitude)
        }
        receiver?.send(resultCode, bundle)
    }

}