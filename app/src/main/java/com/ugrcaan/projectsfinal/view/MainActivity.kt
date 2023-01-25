package com.ugrcaan.projectsfinal.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ugrcaan.projectsfinal.BalanceHistory
import com.ugrcaan.projectsfinal.R
import com.ugrcaan.projectsfinal.Wallet
import com.ugrcaan.projectsfinal.databinding.ActivityMainBinding
import com.ugrcaan.projectsfinal.model.DatabaseHelper
import com.ugrcaan.projectsfinal.model.DriveHistory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    //private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap : GoogleMap
    private var chosenFrame : Int = 1
    var distance : Float = 0F
    private lateinit var databaseHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(view)
        databaseHelper = DatabaseHelper(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment

        mapFragment.getMapAsync {

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.location)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, false)

            val sCbM = BitmapFactory.decodeResource(resources, R.drawable.icon_scooter)
            val sCsB = Bitmap.createScaledBitmap(sCbM, 96, 96, false)

            val myPosition = MarkerOptions()
                .position(LatLng(38.369094, 27.210102))
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))

            val myPosMarker = it.addMarker(myPosition)
            // Set the custom view as the info window for the marker

            val scooter1 = MarkerOptions()
                .position(LatLng(38.368781, 27.210414))
                .title("Scooter")
                .icon(BitmapDescriptorFactory.fromBitmap(sCsB))

            val scMarker = it.addMarker(scooter1)

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPosition.position, 18f)
            it.animateCamera(cameraUpdate)

            var oldLocation = LatLng(38.368781, 27.210414)

            it.setOnInfoWindowClickListener { marker ->
                // Generate a random location nearby the current location
                val randomLat = (Math.random() - 0.5) * 0.003 + marker.position.latitude
                val randomLng = (Math.random() - 0.5) * 0.003 + marker.position.longitude
                val randomLatLng = LatLng(randomLat, randomLng)

                // Update the marker's position
                marker.position = randomLatLng

                // Calculate the distance between the old and new locations
                val locationA = Location("old location")
                locationA.latitude = oldLocation?.latitude ?: marker.position.latitude
                locationA.longitude = oldLocation?.longitude ?: marker.position.longitude
                val locationB = Location("new location")
                locationB.latitude = marker.position.latitude
                locationB.longitude = marker.position.longitude
                distance = locationA.distanceTo(locationB)

                // Draw a line between the old and new locations
                val polylineOptions = PolylineOptions()
                    .add(oldLocation ?: marker.position, marker.position)
                    .color(Color.BLACK)
                    .width(10f)
                it.addPolyline(polylineOptions)

                // Update the old location
                oldLocation = marker.position
            }

            it.setOnInfoWindowLongClickListener { marker ->
                val driveHistory = DriveHistory(
                    scooter1.title.toString(),
                    distance.toInt().toString() + "m",
                    "$" + (distance * 0.05).toInt().toString()
                )
                databaseHelper.insertDriveHistory(driveHistory)
                Toast.makeText(
                    this,
                    "${driveHistory.scId} - ${driveHistory.distance} - ${driveHistory.price}",
                    Toast.LENGTH_SHORT
                ).show()
                distance = 0f
                it.clear()
                oldLocation = LatLng(38.368781, 27.210414)
                it.addMarker(scooter1)
                it.addMarker(myPosition)
            }

            it.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoWindow(marker: Marker): View? {
                    // Inflate the custom layout for the info window
                    val inflater = LayoutInflater.from(this@MainActivity)
                    val customView = inflater.inflate(R.layout.component_marker, null)

                    // Find the TextView in the custom_marker_layout and set its text
                    val textView = customView.findViewById(R.id.percentageBattery) as TextView
                    textView.setText("%83")

                    val scID = customView.findViewById(R.id.scID) as TextView
                    scID.setText("Scooter 1001")

                    val driveBtn = customView.findViewById(R.id.driveBtn) as Button

                    // Return the custom view as the info window for the marker
                    return customView
                }

                override fun getInfoContents(marker: Marker): View? {
                    // This method can be left empty, since we're using a custom info window layout
                    return null
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    fun walletClicked(view: View) {
        binding.backgroundBlurLayout.visibility = View.VISIBLE
        binding.backgroundBlurLayout.isClickable = true
        slideAnimation(binding.sliderComponent.slider, 2)
        if (chosenFrame == 2) {
            shakeAnimation(binding.frameLayout)
            return
        }
        if(binding.frameLayout.visibility == View.GONE){
            fadeInAnimation(binding.frameLayout)
        }
        chosenFrame = 2

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val walletFragment = Wallet()
        fragmentTransaction.replace(R.id.frameLayout, walletFragment).commit()
    }
    fun mapClicked(view: View) {
        binding.backgroundBlurLayout.visibility = View.GONE
        binding.backgroundBlurLayout.isClickable = false
        slideAnimation(binding.sliderComponent.slider, 1)
        chosenFrame = 1
        if(binding.frameLayout.visibility == View.VISIBLE){
            fadeOutAnimation(binding.frameLayout)
        }

    }
    fun historyClicked(view: View) {
        binding.backgroundBlurLayout.visibility = View.VISIBLE
        binding.backgroundBlurLayout.isClickable = true
        slideAnimation(binding.sliderComponent.slider, 0)
        if (chosenFrame == 0) {
            shakeAnimation(binding.frameLayout)
            return
        }
        if(binding.frameLayout.visibility == View.GONE){
            fadeInAnimation(binding.frameLayout)
        }
        chosenFrame = 0

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val settingsFragment = History()
        fragmentTransaction.replace(R.id.frameLayout, settingsFragment).commit()

    }

    private fun slideAnimation(view: View, currentPage: Int){

        var constSet : ConstraintSet
        var constSet2 : ConstraintSet
        var constSet3 : ConstraintSet
        val isCurrentPageCenter = currentPage == 1

        val constrainSet = ConstraintSet()
        constrainSet.clone(binding.sliderComponent.sliderLayout)
        constrainSet.clear(binding.sliderComponent.slider.id, ConstraintSet.START)
        constrainSet.clear(binding.sliderComponent.slider.id, ConstraintSet.END)

        if(currentPage == 0){
            constrainSet.connect(
                binding.sliderComponent.slider.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
        }
        else if (currentPage == 1){
            constrainSet.connect(
                binding.sliderComponent.slider.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )
            constrainSet.connect(
                binding.sliderComponent.slider.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
        }
        else {
            constrainSet.connect(
                binding.sliderComponent.slider.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
        }


        val transition = ChangeBounds()
        transition.interpolator = DecelerateInterpolator()
        val duration = 300
        TransitionManager.beginDelayedTransition(binding.sliderComponent.sliderLayout, transition)
        constrainSet.applyTo(binding.sliderComponent.sliderLayout)
    }

    private fun shakeAnimation(view: View){
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 100
        shake.interpolator = CycleInterpolator(1f)
        shake.repeatCount = 1
        shake.repeatMode = Animation.REVERSE
        view.startAnimation(shake)
    }

    private fun fadeInAnimation(view: View){
        val fadeIn = ObjectAnimator.ofFloat(binding.frameLayout, "alpha", 0.2f, 1f)
        fadeIn.duration = 200
        val animatorSet = AnimatorSet()
        animatorSet.play(fadeIn)
        animatorSet.start()
        view.visibility = View.VISIBLE
    }

    private fun fadeOutAnimation(view : View){
        val fadeOut = ObjectAnimator.ofFloat(binding.frameLayout, "alpha", 1f, 0.2f)
        fadeOut.duration = 200
        val animatorSet = AnimatorSet()
        animatorSet.play(fadeOut)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.frameLayout.visibility = View.GONE
            }
        })
        animatorSet.start()
    }

    fun showBalanceHistoryFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val balanceHistoryFragment = BalanceHistory()
        fragmentTransaction.replace(R.id.frameLayout, balanceHistoryFragment).commit()
    }

    fun returnToWalletFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val walletFragment = Wallet()
        fragmentTransaction.replace(R.id.frameLayout, walletFragment).commit()
    }

    fun showAddBalanceFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val addBalanceFragment = AddBalance()
        fragmentTransaction.replace(R.id.frameLayout, addBalanceFragment).commit()
    }

    fun showAddCardFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val addCardFragment = AddCard()
        fragmentTransaction.replace(R.id.frameLayout, addCardFragment).commit()
    }

}