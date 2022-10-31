package com.test.perqaratest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.test.perqaratest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.gameDetailFragment -> {
                binding.bnv.visibility = View.GONE
            }
            else -> {
                binding.bnv.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(R.id.fc)?.let { navHostFragment ->
            navController = navHostFragment.findNavController()
        }

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    navController.navigate(R.id.gamesFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.favMenu -> {
                    navController.navigate(R.id.gameBookmarkedFragment)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}