package at.aau.pokerfox.partypoker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.R;

import static at.aau.pokerfox.partypoker.activities.MainActivity.BUNDLE_DEVICE_NAME;
import static at.aau.pokerfox.partypoker.activities.MainActivity.BUNDLE_PLAYER_NAME;

public class HostGameActivity extends AppCompatActivity {
    public static final String TAG = ".activities.HostGameActivity";
    public static final String BUNDLE_BIG_BLIND = "BUNDLE_BIG_BLIND";
    public static final String BUNDLE_PLAYER_POT = "BUNDLE_PLAYER_POT";
    public static final String BUNDLE_CHEATING_ALLOWED = "CHEATING_ALLOWED";

    private Salut network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hostgame);

        final String playerName = getIntent().getExtras().getString(MainActivity.BUNDLE_PLAYER_NAME);

        final EditText txtBigblind = findViewById(R.id.txt_bigblind);
        final EditText txtPlayerpot= findViewById(R.id.txt_playerpot);
        final CheckBox boxCheaton = findViewById(R.id.box_cheatOn);
        final Button btnCreate = findViewById(R.id.btn_create);
        final TextView txtConnectedPlayers = findViewById(R.id.txt_connected_players);
        final Button btnStartGame = findViewById(R.id.btn_start_game);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PartyPokerApplication.resetConnectedDevices();

                    SalutDataReceiver dataReceiver = new SalutDataReceiver(HostGameActivity.this, PartyPokerApplication
                            .getMessageHandler());
                    SalutServiceData serviceData = new SalutServiceData(PartyPokerApplication.SALUT_SERVICE_NAME,
                            PartyPokerApplication.SALUT_PORT, playerName);
                    Salut network = new Salut(dataReceiver, serviceData, new SalutCallback() {
                        @Override
                        public void call() {
                            Toast.makeText(HostGameActivity.this, "Sorry, this device is not supported", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                    PartyPokerApplication.setNetwork(network);
                    PartyPokerApplication.setSalutDataReceiver(dataReceiver);
                    PartyPokerApplication.setSalutServiceData(serviceData);

                    HostGameActivity.this.network = network;

                    txtConnectedPlayers.setText("Waiting for players...");

                    network.startNetworkService(new SalutDeviceCallback() {
                        @Override
                        public void call(SalutDevice salutDevice) {
                            PartyPokerApplication.addConnectedDevice(salutDevice);
                            txtConnectedPlayers.setText("Connected players: " + PartyPokerApplication
                                    .getConnectedDevices().size());

                            if (!btnStartGame.isEnabled()) {
                                btnStartGame.setEnabled(true);
                            }
                        }
                    });

                btnCreate.setVisibility(View.INVISIBLE);

            }

        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HostGameActivity.this, "test", Toast.LENGTH_LONG);
                Intent intent = new Intent("GameActivity");

                Bundle bundle = new Bundle();
                bundle.putInt(BUNDLE_BIG_BLIND, Integer.parseInt(txtBigblind.getText().toString()));
                bundle.putInt(BUNDLE_PLAYER_POT, Integer.parseInt(txtPlayerpot.getText().toString()));
                bundle.putBoolean(BUNDLE_CHEATING_ALLOWED, boxCheaton.isChecked());
                bundle.putString(BUNDLE_PLAYER_NAME, playerName);
                bundle.putString(BUNDLE_DEVICE_NAME, network.thisDevice.deviceName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(network != null) {
            if( network.isRunningAsHost) {
                try {
                    network.stopNetworkService(true);

                } catch (Exception e) {

                }
            } else {
                try {
                    network.unregisterClient(true);
                } catch (Exception e) {

                }
            }
        }
    }
}
