package com.example.samplebluetoothapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView tvMessages;
    private Handler mainHandler;

    // TODO: REPLACE WITH YOUR PC'S LOCAL IP ADDRESS (e.g., "192.168.1.5")
    private final String pcIpAddress = "192.168.X.X";
    private final int pcPort = 5001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessages = findViewById(R.id.tvMessages);

        // Initialize the handler on the main thread looper for UI updates
        mainHandler = new Handler(Looper.getMainLooper());

        // Start the network client in a background thread
        startSocketClient();
    }

    private void startSocketClient() {
        new Thread(() -> {
            try {
                updateUI("Connecting to PC at " + pcIpAddress + ":" + pcPort + "...\n");

                // Open connection to the PC server
                Socket socket = new Socket(pcIpAddress, pcPort);
                updateUI("Connected successfully!\n\n");

                // Set up input stream reader to listen for messages
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    // Read line by line (expects a '\n' delimiter from the Python script)
                    String line = reader.readLine();

                    // If line is null, server has disconnected
                    if (line == null) {
                        updateUI("Connection lost.\n");
                        break;
                    }

                    if (line.trim().equalsIgnoreCase("exit")) {
                        updateUI("PC terminated session.\n");
                        break;
                    }

                    // Append the received message to the screen
                    updateUI("PC: " + line + "\n");
                }

                socket.close();

            } catch (Exception e) {
                updateUI("Connection Error: " + e.getLocalizedMessage() + "\n");
                e.printStackTrace();
            }
        }).start();
    }

    private void updateUI(final String message) {
        mainHandler.post(() -> tvMessages.append(message));
    }
}