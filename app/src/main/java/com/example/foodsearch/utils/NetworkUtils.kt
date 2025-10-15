package com.example.foodsearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected == true
            }
        } catch (e: SecurityException) {
            android.util.Log.w("NetworkUtils", "No ACCESS_NETWORK_STATE permission, assuming network is available", e)
            // Если нет разрешения, предполагаем что сеть доступна
            true
        } catch (e: Exception) {
            android.util.Log.e("NetworkUtils", "Error checking network availability", e)
            // В случае ошибки предполагаем что сеть доступна
            true
        }
    }
}
