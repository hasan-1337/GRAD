package edu.uw.tcss450.labose.signinandregistration.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private static final DecimalFormat df = new DecimalFormat("#.#");

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel model = new ViewModelProvider(getActivity())
                .get(UserViewModel.class);

        mBinding = FragmentHomeBinding.bind(getView());

        mBinding.btnGet.setOnClickListener(this::getWeatherDetails);

//        FragmentHomeBinding.bind(getView()).textHello.setText("Hello " + model.getEmail());
    }


    public void getWeatherDetails(View view) {
        String tempUrl;
        String city = mBinding.etCity.getText().toString().trim();
        if(city.equals("")){ // if nothing is typed
            mBinding.tvResult.setText("City field can not be empty!");
        }else{
            tempUrl = "https://api.weatherbit.io/v2.0/forecast/daily?&postal_code=" + city + "&country=US&days=7&units=I&key=51500e0f085741f591dc0356d9a03ff4";
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
                    mBinding.tvResult.setText(output.toString()); // Fill up the textview with the weather data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                    mBinding.currentWeather.setText(output); // Fill up the textview with the weather data

                    LinearLayout layout = mBinding.background;
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
            }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            requestQueue = Volley.newRequestQueue(getActivity());
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
                    mBinding.hourlyWeather.setText(output + "\n" + output2); // Fill up the textview with the weather data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }
}

