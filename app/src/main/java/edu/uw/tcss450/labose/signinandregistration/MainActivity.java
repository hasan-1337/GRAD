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
import android.widget.LinearLayout;
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
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.services.PushReceiver;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.ChatMessage;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.ChatViewModel;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;
    private EditText etCity; // Area where to type the city
    private TextView tvResult; // Result of the weather (7 days)
    private TextView currentWeather; // Result of the weather (current)
    private TextView hourlyWeather; // Result of the weather (hourly)
    private final DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserViewModel.userViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserViewModel.class);

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
        hourlyWeather = findViewById(R.id.hourlyWeather);
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
                mModel.addMessage(intent.getIntExtra("chat", -1), cm);
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
                        output.append(" Day ").append(i + 1).append("\n").append(df.format(temp)).append(" °F").append("\n Humidity: ").append(humidity).append("%").append("\n Wind Speed: ").append(df.format(wind)).append(" mph").append("\n Cloudiness: ").append(clouds).append("%\n\n");
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
                    int clouds = jsonobject.getInt("clouds");
                    String state = jsonobject.getString("city_name");
                    String countryCode = jsonobject.getString("country_code");
                    output += " Current Forecast in\n" + state + " (" + countryCode + ")"
                            + "\n" + df.format(temp) + " °F"
                            + "\n\n Humidity: " + humidity + "%"
                            + "\n Wind Speed: " + df.format(wind) + " mph"
                            + "\n Cloudiness: " + clouds + "%\n\n";
                    currentWeather.setText(output); // Fill up the textview with the weather data

                    LinearLayout layout = findViewById(R.id.background);
                    if (temp > 35) {
                        if (clouds > 50) {
                            layout.setBackgroundResource(R.drawable.cloudy);
                        } else {
                            layout.setBackgroundResource(R.drawable.weather);
                        }
                    } else {
                        layout.setBackgroundResource(R.drawable.snow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            tempUrl = "https://api.weatherbit.io/v2.0/forecast/hourly?city=" + city + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            // Resend the request to retrieve the current forecast.
            stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
                StringBuilder output = new StringBuilder();
                StringBuilder output2 = new StringBuilder();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    output.append(String.format("%-4s", ""));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        double temp = jsonobject.getDouble("temp");
                        String time = jsonobject.getString("timestamp_local");
                        int t = Integer.parseInt(time.substring(11, 13));
                        String x = "" + ((t % 12 == 0)? 12:t % 12);
                        if (t % 12 == t) {
                            output.append(String.format("%-11s", x + " AM"));
                        } else {
                            output.append(String.format("%-11s", x + " PM"));
                        }

                        output2.append(df.format(temp)).append(String.format("%-8s", " °F"));
                    }
                    hourlyWeather.setText(output + "\n" + output2); // Fill up the textview with the weather data
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
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null) {
            unregisterReceiver(mPushMessageReceiver);
        }
    }

}
