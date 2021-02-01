package com.meazza.uber.ui.map

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.meazza.uber.R
import com.meazza.uber.util.openAppSettings
import com.meazza.uber.util.toast
import com.meazza.uber.vo.PermissionsRequester

class MapFragment : Fragment(R.layout.fragment_map) {

    private val mapFragment by lazy { childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment? }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment?.getMapAsync { googleMap ->
            val sydney = LatLng(-34.0, 151.0)
            googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        checkPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_travels -> {
                findNavController().navigate(R.id.nav_travels)
            }
            R.id.mn_log_out -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissions(): PermissionsRequester {

        val permissionCoarse = Manifest.permission.ACCESS_COARSE_LOCATION
        val permissionFine = Manifest.permission.ACCESS_FINE_LOCATION
        val permissionBackground =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION else null

        val permissions = permissionBackground?.let {
            arrayOf(permissionCoarse, permissionFine, it)
        } ?: arrayOf(permissionCoarse, permissionFine)

        return PermissionsRequester(
            this,
            permissions,
            onRationale = {
                toast("Rationale")
            },
            onDenied = {
                requireContext().openAppSettings()
            }
        )
    }
}