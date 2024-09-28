package app.grapheneos.networkgeolocation

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.provider.LocationProviderBase
import android.location.provider.ProviderProperties
import android.location.provider.ProviderRequest
import android.os.Bundle
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NetworkGeolocationService : Service() {
    private var mLocationProviderBase: LocationProviderBase? = null

    override fun onCreate() {
        super.onCreate()
        mLocationProviderBase = object : LocationProviderBase(
            this,
            "NetworkGeolocationService",
            ProviderProperties.Builder().build() // TODO: Add properties?
        ) {
            override fun onSetRequest(providerRequest: ProviderRequest) {
                startLocationUpdates(providerRequest)
            }

            override fun onFlush(p0: OnFlushCompleteCallback) {
//        TODO("Not yet implemented")
            }

            override fun onSendExtraCommand(p0: String, p1: Bundle?) {
//        TODO("Not yet implemented")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mLocationProviderBase?.binder
    }

    private var locationUpdateJob: Job? = null

    private fun startLocationUpdates(providerRequest: ProviderRequest) {
        locationUpdateJob?.cancel()

        locationUpdateJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val location = Location(LocationManager.NETWORK_PROVIDER)

                location.latitude = 48.88899590617799
                location.longitude = 2.334754748944074

                location.makeComplete() // temp for testing

                println(location)

                mLocationProviderBase?.reportLocation(location)

                // just for testing purposes
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationUpdateJob?.cancel()
    }
}
