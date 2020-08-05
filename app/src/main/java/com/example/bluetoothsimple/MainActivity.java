package com.example.bluetoothsimple;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI components
    ListView listView;

    // objects declaration
    ArrayList<String> mDeviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind UI components
        listView = (ListView) findViewById(R.id.listView);

        // get default bluetooth on device
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // start discovery
        mBluetoothAdapter.startDiscovery();

        // register broadcast receiver and listen to specific "ACTION_FOUND" request
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // if any bluetooth devices found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                //  receive all contents
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null) {
                    // get device name and MAC address
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    // add this info to ArrayList
                    mDeviceList.add(device.getName() + "\n" + device.getAddress());

                    // put the item into ListView
                    listView.setAdapter(new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1, mDeviceList));
                    Log.d("NewBtDevice",deviceName + deviceHardwareAddress);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister broadcast receiver
        unregisterReceiver(receiver);
    }
}
