package me.lwb.mvvm.utils

import android.Manifest
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import me.lwb.context.AppContext
import kotlin.coroutines.resume

/**
 * Created by luowenbin on 2021/9/15.
 */

object LocateUtils {
    data class LocaleOptions(
        val providerTimeout: Long = 2000L,
        val preferCriteria: Criteria = defaultCriteria(),
        val preferUpdateTime: Long = 1000L,
        val preferUpdateDistance: Float = 1f
    )

    fun LocationManager.getPreferProviders(criteria: Criteria = defaultCriteria()): List<String> {
        val bestProvider = this.getBestProvider(criteria, true)
        val enableProviders = this.getProviders(true).toMutableList()
        if (bestProvider != null) {
            //move prefer provider to first
            enableProviders.remove(bestProvider)
            enableProviders.add(0, bestProvider)
        }
        return enableProviders
    }


    private fun defaultCriteria() = Criteria().apply {
        accuracy = Criteria.ACCURACY_FINE
        isAltitudeRequired = false
        isBearingRequired = false
        isCostAllowed = false
        powerRequirement = Criteria.NO_REQUIREMENT
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    suspend fun locale(option: LocaleOptions = LocaleOptions()) =
        Locator(AppContext.context).locale(option)

    class Locator(context: Context) {
        private val locationManager: LocationManager =
            context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        private var locationUpdater: LocationListener? = null


        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
        suspend fun locale(option: LocaleOptions = LocaleOptions()) =
            locationManager.getPreferProviders(option.preferCriteria)
                .firstNotNullOfOrNull {
                    withTimeoutOrNull(option.providerTimeout) {
                        localeByProvider(it, option.preferUpdateTime, option.preferUpdateDistance)
                    } ?: locationManager.getLastKnownLocation(it)
                }


        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
        suspend fun localeByProvider(
            provider: String,
            minTime: Long = 1000L,
            minDistance: Float = 1f
        ): Location? {
            locationUpdater?.let { locationManager.removeUpdates(it) }
            return suspendCancellableCoroutine { c ->
                locationUpdater = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        c.resume(location)
                    }

                    override fun onProviderDisabled(provider: String) {
                        c.resume(null)
                    }
                }.also {
                    locationManager.requestLocationUpdates(
                        provider, minTime, minDistance,
                        it
                    )
                    c.invokeOnCancellation { _ ->
                        locationManager.removeUpdates(it)
                    }
                }

            }

        }


    }
}
