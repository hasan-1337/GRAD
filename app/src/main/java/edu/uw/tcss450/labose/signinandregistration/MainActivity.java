package edu.uw.tcss450.labose.signinandregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import edu.uw.tcss450.labose.signinandregistration.databinding.ActivityMainBinding;
import edu.uw.tcss450.labose.signinandregistration.model.NewMessageCountViewModel;
import edu.uw.tcss450.labose.signinandregistration.model.userViewModel;
import edu.uw.tcss450.labose.signinandregistration.services.PushReceiver;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.ChatMessage;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.ChatViewModel;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;
    public EditText etCity; // Area where to type the city
    public TextView tvResult; // Result of the weather (7 days)
    public TextView currentWeather; // Result of the weather (current)
    private final DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new userViewModel.userViewModelFactory(args.getEmail(), args.getJwt())
        ).get(userViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chat)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                mNewMessageModel.reset();
            }
        });

        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                badge.clearNumber();
                badge.setVisible(false);
            }
        });
        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
        currentWeather = findViewById(R.id.currentWeather);

    }

    private class MainPushMessageReceiver extends BroadcastReceiver {
        private final ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);

        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");

                assert nd != null;
                if (nd.getId() != R.id.navigation_chat) {
                    mNewMessageModel.increment();
                }
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

    // Retrieve the weather data
    @SuppressLint("SetTextI18n")
    public void getWeatherDetails(View view) {
        String tempUrl;
        String city = etCity.getText().toString().trim();
        if(city.equals("")){ // if nothing is typed
            tvResult.setText("City field can not be empty!");
        }else{
            tempUrl = "https://api.weatherbit.io/v2.0/forecast/daily?city=" + city + "&days=7&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            // request the JSON information
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
                StringBuilder output = new StringBuilder();

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        double temp = jsonobject.getDouble("temp");
                        int humidity = jsonobject.getInt("rh");
                        double wind = jsonobject.getDouble("wind_spd");
                        String clouds = jsonobject.getString("clouds");
                        output.append(" Day ").append(i + 1).append("\n Temp: ").append(df.format(temp)).append(" °F").append("\n Humidity: ").append(humidity).append("%").append("\n Wind Speed: ").append(df.format(wind)).append(" mph").append("\n Cloudiness: ").append(clouds).append("%\n\n");
                    }
                    tvResult.setText(output.toString()); // Fill up the textview with the weather data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            tempUrl = "https://api.weatherbit.io/v2.0/current?city=" + city + "&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            // Resend the request to retrieve the current forecast.
            stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
                String output = "";

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    JSONObject jsonobject = jsonArray.getJSONObject(0);
                    double temp = jsonobject.getDouble("temp");
                    int humidity = jsonobject.getInt("rh");
                    double wind = jsonobject.getDouble("wind_spd");
                    String clouds = jsonobject.getString("clouds");
                    String state = jsonobject.getString("city_name");
                    String countryCode = jsonobject.getString("country_code");
                    String time = jsonobject.getString("datetime");
                    output += " Current Forecast in\n" + state + " (" + countryCode + ")\nTime: " + time
                            + "\n\n Temp: " + df.format(temp) + " °F"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Wind Speed: " + df.format(wind) + " mph"
                            + "\n Cloudiness: " + clouds + "%\n\n";
                    currentWeather.setText(output); // Fill up the textview with the weather data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
        System.out.println("TEST!!!!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null) {
            unregisterReceiver(mPushMessageReceiver);
        }
    }

}
