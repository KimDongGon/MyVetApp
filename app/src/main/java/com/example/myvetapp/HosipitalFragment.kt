package com.example.myvetapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.ref.WeakReference
import javax.xml.parsers.DocumentBuilderFactory

class HosipitalFragment : Fragment() {
    lateinit var googleMap: GoogleMap
    var fusedLocationClient: FusedLocationProviderClient? = null //위치 정보 받아올 객체
    var locationCallback: LocationCallback? = null // 위치 업데이트를 위한 객체
    var locationRequest: LocationRequest? = null
    var loc = LatLng(37.6706, 126.7810)
    val arrLoc = ArrayList<LatLng>()
    var markTitle = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hosipital, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLocation()
    }

    // 앱을 실행 시켰을 때 권한 정보 체크 및 요청 하는 함수
    private fun initLocation(){
        if(ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getuserlocation()
//            startLocationUpdates()
            initMap()
        }
        else{
            // 권한이 없을 경우 requestPermission 함수를 사용하여 권한 요청
            // 결과는 onRequestPermissionResult로 이동동
           ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
    }

    fun startLocationUpdates() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for(location in locationResult.locations){
                    loc = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
                }
            }
        }
        // 콜백 메시지로 처리되는 것들은
        // 메인 쓰레드의 looper 사용
        if(ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates(){
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
//        stopLocationUpdates()
    }

    fun getuserlocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if(ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener {
                loc = LatLng(it.latitude, it.longitude)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                getuserlocation()
//                startLocationUpdates()
                initMap()
            }
            else {
                Toast.makeText(requireActivity(), "위치정보 제공을 하셔야 합니다.", Toast.LENGTH_SHORT).show()
                initMap()
            }
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap = it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)

            // 구글 맵에 마커 찍기
            initMark()
        }
    }

    private fun initMark(){
        val options = MarkerOptions()
        googleMap.clear()
        val task = MyAsyncTask(this)
        if(task.execute().get()){
            for(i in 0..arrLoc.size-1){
                options.position(arrLoc[i])
                options.title(markTitle[i])
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                val mk1 = googleMap.addMarker(options)
                mk1.showInfoWindow()
            }
        }
    }

    class MyAsyncTask(context: HosipitalFragment): AsyncTask<Unit, Unit, Boolean>(){
        var activityreference = WeakReference(context)

        override fun doInBackground(vararg p0: Unit?): Boolean {
            val activity = activityreference.get()
            val apiKey = "ed857aecd5ad48a3a8688ac44d222775"
            val apiUrl = "https://openapi.gg.go.kr/Animalhosptl?KEY="+apiKey
            val xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(apiUrl)
            val list = xml.getElementsByTagName("row")

            for(i in 0..list.length-1){
                var n = list.item(i)
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    val elem = n as Element
                    val map = mutableMapOf<String, String>()

                    for(j in 0..elem.attributes.length-1){
                        map.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                    }
                    if(elem.getElementsByTagName("BSN_STATE_NM").item(0).textContent=="정상"){
                        activity?.loc = LatLng(elem.getElementsByTagName("REFINE_WGS84_LAT").item(0).textContent.toDouble(), elem.getElementsByTagName("REFINE_WGS84_LOGT").item(0).textContent.toDouble())
                        activity?.arrLoc?.add(activity?.loc)
                        activity?.markTitle?.add(elem.getElementsByTagName("BIZPLC_NM").item(0).textContent)
                    }
                }
            }
            return true
        }
    }
}