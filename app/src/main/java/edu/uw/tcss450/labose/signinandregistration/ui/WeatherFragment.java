package edu.uw.tcss450.labose.signinandregistration.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.labose.signinandregistration.model.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private FragmentWeatherBinding mBinding;
    private GoogleMap mMap;
    private static String hourly;
    private static String current;
    private static String daily;
    private static Object background;
    private static LatLng coordinates;
    private static final DecimalFormat df = new DecimalFormat("#.#");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = FragmentWeatherBinding.bind(getView());
        mBinding.hourlyWeather.setText(hourly);
        mBinding.currentWeather.setText(current);
        mBinding.tvResult.setText(daily);
        mBinding.btnGet.setOnClickListener(this::getWeatherDetails);

        if (background != null) {
            mBinding.background.setBackgroundResource((Integer) background);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void getWeatherDetails(final View view) {
        String tempUrl;
        String city = mBinding.etCity.getText().toString().trim();

        if(city.equals("")){ // if nothing is typed
            mBinding.tvResult.setText(R.string.weather_invalid);
        }else{
            boolean bZipcode;

            try { // Check if it's a zipcode or city name
                Integer.parseInt(city);
                bZipcode = true;
            } catch (final NumberFormatException nfe) {
                bZipcode = false;
            }

            if (bZipcode) {
                tempUrl = "https://api.weatherbit.io/v2.0/current?postal_code=" + city + "&country=US&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            } else {
                tempUrl = "https://api.weatherbit.io/v2.0/current?city=" + city + "&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            }
            getWeatherCurrent(tempUrl);

            if (bZipcode) {
                tempUrl = "https://api.weatherbit.io/v2.0/forecast/hourly?postal_code=" + city + "&country=US&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            } else {
                tempUrl = "https://api.weatherbit.io/v2.0/forecast/hourly?city=" + city + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            }
            getWeatherHourly(tempUrl);

            if (bZipcode) {
                tempUrl = "https://api.weatherbit.io/v2.0/forecast/daily?&postal_code=" + city + "&country=US&days=7&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            } else {
                tempUrl = "https://api.weatherbit.io/v2.0/forecast/daily?&city=" + city + "&days=7&units=I&key=51500e0f085741f591dc0356d9a03ff4";
            }
            getWeatherDaily(tempUrl);
        }
    }

    private void getWeatherCurrent(final String tempUrl) {
        // Resend the request to retrieve the current forecast.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
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
                current = output;
                mBinding.currentWeather.setText(current); // Fill up the textview with the weather data

                if (temp > 35) {
                    if (clouds > 50) {
                        background = R.drawable.cloudy;
                    } else {
                        background = R.drawable.weather;
                    }
                } else {
                    background = R.drawable.snow;
                }

                mBinding.background.setBackgroundResource((Integer) background);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getWeatherHourly(final String tempUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
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
                hourly = output + "\n" + output2;
                mBinding.hourlyWeather.setText(hourly); // Fill up the textview with the weather data
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getWeatherDaily(final String tempUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
            StringBuilder output = new StringBuilder();

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject jsonobject = jsonArray.getJSONObject(i);
                    double temp = jsonobject.getDouble("temp");
                    int humidity = jsonobject.getInt("rh");
                    double wind = jsonobject.getDouble("wind_spd");
                    String clouds = jsonobject.getString("clouds");
                    output.append(" Day ").append(i + 1).append("\n").append(df.format(temp)).append(" °F")
                            .append("\n Humidity: ").append(humidity).append("%").append("\n Wind Speed: ")
                            .append(df.format(wind)).append(" mph").append("\n Cloudiness: ").append(clouds).append("%\n\n");
                }
                daily = output.toString();
                mBinding.tvResult.setText(daily); // Fill up the textview with the weather data
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
        coordinates = latLng;
        getWeatherCurrent("https://api.weatherbit.io/v2.0/current?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&units=I&key=51500e0f085741f591dc0356d9a03ff4");
        getWeatherHourly("https://api.weatherbit.io/v2.0/forecast/hourly?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4");
        getWeatherDaily("https://api.weatherbit.io/v2.0/forecast/daily?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&days=7&units=I&key=51500e0f085741f591dc0356d9a03ff4");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                if (coordinates == null) {
                    final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 4.0f));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 4.0f));
                }

            }
        });
        mMap.setOnMapClickListener(this);
    }
}

