package com.example.beekeeper.data.source.remote.bluetooth.conroller

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.example.beekeeper.data.broadcast_receiver.BluetoothStateReceiver
import com.example.beekeeper.data.broadcast_receiver.FoundDeviceReceiver
import com.example.beekeeper.data.source.remote.bluetooth.mapper.toModel
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.common.SocketConnectionResult
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.EOFException
import java.io.IOException
import java.util.UUID


@SuppressLint("MissingPermission") // I use my util checker which editor can't see
class BluetoothControllerImpl(
    private val context: Context
) : BluetoothController{

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter //if device doesn't support bluetooth then this will be null
    } // where is all functionality: get your mac address, name do anything with bluetooth (get paired devices, scanned devices...)

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomainModel>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomainModel>>
        get() = _scannedDevices.asStateFlow()


    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomainModel>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomainModel>>
        get() = _pairedDevices.asStateFlow()


    private val _isConnected =  MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()


    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val foundDevicesReceiver = FoundDeviceReceiver{device->
        _scannedDevices.update { devices ->
            val newDevice = device.toModel()
            if(newDevice in devices) devices else devices+newDevice
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver{ isConnected, bluetoothDevice ->

        if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == false){
            _isConnected.update { isConnected }
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Can't connect to non-paired Device")
            }
        }
    }

    private var currentServerSocket : BluetoothServerSocket? = null  // for only accepting connections
    private var currentClientSocket : BluetoothSocket? = null   // this will keep the alive connection

    private var isFoundDevicesReceiverRegistered = false
    private var isBluetoothStateReceiverRegistered = false

    init {
        updatePairedDevices()
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
        isBluetoothStateReceiverRegistered = true
    }

    override fun startDiscovery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions(Manifest.permission.BLUETOOTH_SCAN)) {
            Log.d("tag1234","starting scan Failed No permission")
            return
        }
        Log.d("tag1234","starting scan")

        context.registerReceiver(
            foundDevicesReceiver,
            IntentFilter( BluetoothDevice.ACTION_FOUND )
        )

        isFoundDevicesReceiverRegistered = true

        updatePairedDevices()

        if(bluetoothAdapter == null)  Log.d("tag123","in controller failed, bluetoothAdapter was null")
        bluetoothAdapter?.startDiscovery()  // this will start discovery process. we will be listening broadcasts for that
    }

    override fun stopDiscovery() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun startBluetoothServer(): Flow<SocketConnectionResult> {
        return flow{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "BeeHive Analytics",
                UUID.fromString(SERVICE_UUID)
            )

            // here server is already started

            var shouldLoop = true
            while (shouldLoop){
                currentClientSocket = try {
                    currentServerSocket?.accept()
                }catch (e: IOException){
                    shouldLoop = false
                    null
                }

                emit(SocketConnectionResult.ConnectionEstablished)
                currentClientSocket?.let {// here will handle input info
                    currentServerSocket?.close()
                }

            }

        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun connectToDevice(device: BluetoothDeviceDomainModel): Flow<SocketConnectionResult> {
        return flow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)

            val tempSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))

            // Stop discovery to save resources
            stopDiscovery()

            tempSocket?.let { socket ->
                try {
                    socket.connect()
                    currentClientSocket = socket
                    emit(SocketConnectionResult.ConnectionEstablished)
                } catch (e: IOException) {
                    //Log.e("BluetoothController", "Connection failed: ${e.message}")
                    emit(SocketConnectionResult.Error("Connection was interrupted"))
                } finally {
                    if (!socket.isConnected) {
                        socket.close()
                    }
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun closeConnection() {
        currentServerSocket?.close()
        currentServerSocket = null
    }

    override suspend fun handleInputStream(): Flow<Resource<BeehiveAnalytics>> {
        currentClientSocket?.let { socket ->
            sendStartMessage()
            return flow {
                emit(Resource.Loading())
                val inputStream = socket.inputStream
                val buffer = ByteArray(1024)
                var message = ""

                while (true) {
                    try {
                        val bytes = inputStream.read(buffer)
                        val incomingMessage = String(buffer, 0, bytes)
                        message += incomingMessage

                        // Check if the message ends with a newline character
                        if (message.endsWith("\n")) {
                            // Remove the newline character
                            message = message.trim()


                            // Split the message into id, weightData, and temperatureData
                            val parts = message.split(";")
                            val id = parts[0].toInt()
                            val weightData = parts[1].split(",").map { it.toDouble() }
                            val temperatureData = parts[2].split(",").map { it.toDouble() }
                            val currentDateTime = System.currentTimeMillis()


                            // Create a BeehiveData object
                            val beehiveData = BeehiveAnalytics(id, weightData, temperatureData, currentDateTime)
                            emit(Resource.Success(beehiveData))

                            // Close and reset the socket
                            socket.close()
                            currentClientSocket = null
                            break
                        }

                    } catch (e: IOException) {
                        emit(Resource.Failed(e.message ?: "Empty IO Exception"))
                        //Log.d("BluetoothController", "1 Incoming BeehiveData: ${e.message}")
                        break
                    } catch (e: EOFException) {
                        emit(Resource.Failed(e.message ?: "End of stream reached unexpectedly"))
                        //Log.d("BluetoothController", "2 Incoming BeehiveData: ${e.message}")
                        break
                    } catch (e: SecurityException) {
                        emit(Resource.Failed(e.message ?: "Security Exception occurred"))
                        //Log.d("BluetoothController", "3 Incoming BeehiveData: ${e.message}")
                        break
                    } catch (e: IllegalArgumentException) {
                        emit(Resource.Failed(e.message ?: "Invalid argument passed"))
                        //Log.d("BluetoothController", "4 Incoming BeehiveData: ${e}")
                        break
                    } catch (e: NullPointerException) {
                        emit(Resource.Failed(e.message ?: "Null Pointer Exception occurred"))
                        //Log.d("BluetoothController", "5 Incoming BeehiveData: ${e.message}")
                        break
                    }
                }

            }.flowOn(Dispatchers.IO)
        }
        return flow {
            emit(Resource.Failed("No connected device"))
        }
    }

    // Function to send a "start" message to the Arduino
    private fun sendStartMessage() {
        try {
            currentClientSocket?.let { socket ->
                val outputStream = socket.outputStream
                outputStream.write("start\n".toByteArray())
                outputStream.flush()
            }
        } catch (e: IOException) {
            //Log.e("BluetoothController", "Error sending start message: ${e.message}")
            // Handle the error or close the socket if necessary
        }
    }


    private fun updatePairedDevices(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }

        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toModel() }
            ?.also { devices->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermissions(permission:String):Boolean{  // Util function for checking permissions
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            true
        } else {
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun release() {
        if(isFoundDevicesReceiverRegistered){
            context.unregisterReceiver(foundDevicesReceiver)
            isFoundDevicesReceiverRegistered = false
        }
        if(isBluetoothStateReceiverRegistered){
            isBluetoothStateReceiverRegistered = false
            context.unregisterReceiver(bluetoothStateReceiver)
        }
        currentClientSocket?.close()
        currentClientSocket = null
        closeConnection()
    }

    companion object{
        //const val SERVICE_UUID = "7f86d65b-66f7-430a-bf96-5e2614277ad4"  // so that services get distinguished, each app or service will get this 128 bit id
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}