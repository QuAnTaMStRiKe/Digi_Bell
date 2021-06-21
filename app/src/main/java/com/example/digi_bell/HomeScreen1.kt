package com.example.digi_bell

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_screen1.*
import kotlinx.android.synthetic.main.activity_home_screen1.paired
import kotlinx.android.synthetic.main.activity_home_screen1.paired_device_list
import kotlinx.android.synthetic.main.activity_home_screen1.paired_device_name_list
import kotlinx.android.synthetic.main.activity_home_screen1.scan
import kotlinx.android.synthetic.main.activity_home_screen1.select_device_list
import kotlinx.android.synthetic.main.activity_home_screen1.select_device_name_list
import kotlinx.android.synthetic.main.help.*


class HomeScreen1 : AppCompatActivity() {

    val list1 : ArrayList<BluetoothDevice> = ArrayList()
    val list2 : ArrayList<String> = ArrayList()
    private val list3 : ArrayList<BluetoothDevice> = ArrayList()
    private val list4 : ArrayList<String> = ArrayList()
    private val PERMISSION_REQUEST_FINE_LOCATION = 1
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var m_pairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen1)

        paired_device.isInvisible = true
        paired_device_list.isInvisible= true
        paired_device_name_list.isInvisible = true
        discovering_devices.isInvisible = true
        select_device_list.isInvisible = true
        select_device_name_list.isInvisible = true
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid


        backGen.setOnClickListener {
            val intentSr = Intent(this, Generator1::class.java)
            startActivity(intentSr)
        }

        hmbtn1.setOnClickListener {
           val help:String = "1234"
            Log.d("help", " help")
           sendHelp(help)

            val vibrationEffect1: VibrationEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationEffect1 =
                    VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.cancel()
                vibrator.vibrate(vibrationEffect1)
            }
            val i = Intent(this, HomeScreen2::class.java)
            startActivity(i)
       }
        up1.setOnClickListener {
            val int = Intent(this, UserProfile::class.java)
            startActivity(int)
        }

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        scan.setOnClickListener {
            list1.clear()
            list2.clear()
            paired_device.isInvisible = true
            paired_device_list.isInvisible= true
            paired_device_name_list.isInvisible = true
            discovering_devices.isInvisible = false
            select_device_list.isInvisible = false
            select_device_name_list.isInvisible = false
            m_bluetoothAdapter!!.startDiscovery()
            Log.e("Scan", "Starting Discovery")
            Toast.makeText(this, "Starting Discovery", Toast.LENGTH_SHORT).show()
        }
        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, intentFilter)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list1)
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list2)
        select_device_name_list.adapter = arrayAdapter2
        select_device_list.adapter= arrayAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("This app needs location access")
                builder.setMessage("Please grant location access so this app can detect devices.")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_FINE_LOCATION
                    )
                }
                builder.show()
            }
        }

        paired.setOnClickListener {
            list3.clear()
            list4.clear()
            paired_device.isInvisible = false
            paired_device_list.isInvisible= false
            paired_device_name_list.isInvisible = false
            discovering_devices.isInvisible = true
            select_device_list.isInvisible = true
            select_device_name_list.isInvisible = true
            m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
            if(m_pairedDevices.isNotEmpty()){
                for (device: BluetoothDevice in m_pairedDevices){
                    val deviceName = device.name
                    list3.add(device)
                    list4.add(deviceName)
                    Log.i("device", ""+device+""+deviceName)
                }
            } else{
                Toast.makeText(this,"No paired bluetooth devices found", Toast.LENGTH_SHORT).show()
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list3)
            val adapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list4)
            paired_device_list.adapter = adapter
            paired_device_name_list.adapter = adapter2
            paired_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list3[position]
                val address: String = device.address
//                val intent = Intent(this, ControlActivity::class.java )
//                intent.putExtra(EXTRA_ADDRESS, address)
//                startActivity(intent)
            }

        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_FINE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Granted", "coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }


    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) { }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice?
                list1.add((device!!))
                list2.add(device.name)
                Log.i("device", " "+device+" "+device.name)

                val arrayAdapter2 = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list1)
                val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list2)
                select_device_list.adapter = arrayAdapter2
                select_device_name_list.adapter = arrayAdapter

                select_device_name_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    m_bluetoothAdapter?.cancelDiscovery();
                    Log.d("Clicked", "Clicked on a device")
                    val device: BluetoothDevice = list1[position]
                    val address: String = device.address
                    Log.d("Selected", "Device Name: $device")
                    Log.d("Selected", "Device Address: $address")
                    Log.d("Pairing", "Trying to pair with $device")
                    device.createBond()



                }
                Toast.makeText(this@HomeScreen1, "Found device " + device!!.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (requestCode == Activity.RESULT_OK){
                if (m_bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this,"Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this ,"Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Bluetooth enabling has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendHelp(sendHelp: String){
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        dbRef.child("Help").setValue(sendHelp)
        Toast.makeText(this, "Asking Help", Toast.LENGTH_SHORT).show()
    }



}