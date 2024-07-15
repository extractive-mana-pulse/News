package com.example.newsapp.presentation.activities.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.presentation.activities.util.ConnectivityObserver
import com.example.newsapp.presentation.activities.util.NetworkConnectivityObserver
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.presentation.home.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var connectivityObserver: ConnectivityObserver
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContentView(binding.root)

        val status = connectivityObserver.observe().apply {
            ConnectivityObserver.Status.Unavailable
        }

        Snackbar.make(requireViewById(R.id.bottom_nav_view), "$status", Snackbar.LENGTH_SHORT).show()

        Log.d("wtf", status.toString())
        Log.d("wtf again", status.toString())

//        val status by connectivityObserver.observe().collectAsState(
//            initial = ConnectivityObserver.Status.Unavailable
//        )

        binding.apply {

            bottomNavView.setupWithNavController(navController)

        }
    }
}