package com.example.staysunny.core

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class LocationProvider private constructor(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)

    companion object {
        @Volatile
        private var INSTANCE: LocationProvider? = null

        fun getInstance(context: Context): LocationProvider {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationProvider(context).also { INSTANCE = it }
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return try {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
