package edu.uw.tcss450.labose.signinandregistration.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.labose.signinandregistration.model.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private FragmentWeatherBinding mBinding;
    private GoogleMap mMap;
    private boolean temperature;
    private static String hourly;
    private static String current;
    private static String daily;
    private static LatLng coordinates;
    private static final DecimalFormat df = new DecimalFormat("#.#");

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPreference();

        mBinding = FragmentWeatherBinding.bind(getView());
        mBinding.hourlyWeather.setText(hourly);
        mBinding.currentWeather.setText(current);
        mBinding.tvResult.setText(daily);
        mBinding.btnGet.setOnClickListener(this::getWeatherDetails);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getWeatherDetails(final View view) {
        final String city = mBinding.etCity.getText().toString().trim();
        checkPreference();

        if (city.length() > 0) {
            boolean bZipcode;

            try { // Check if it's a zipcode or city name
                Integer.parseInt(city);
                bZipcode = true;
            } catch (final NumberFormatException nfe) {
                bZipcode = false;
            }

            if (bZipcode) {
                getWeatherCurrent("https://api.weatherbit.io/v2.0/current?postal_code=" + city + "&country=US&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            } else {
                getWeatherCurrent("https://api.weatherbit.io/v2.0/current?city=" + city + "&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            }

            if (bZipcode) {
                getWeatherHourly("https://api.weatherbit.io/v2.0/forecast/hourly?postal_code=" + city + "&country=US&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            } else {
                getWeatherHourly("https://api.weatherbit.io/v2.0/forecast/hourly?city=" + city + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            }

            if (bZipcode) {
                getWeatherDaily("https://api.weatherbit.io/v2.0/forecast/daily?&postal_code=" + city + "&country=US&days=11&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            } else {
                getWeatherDaily("https://api.weatherbit.io/v2.0/forecast/daily?&city=" + city + "&days=11&units=I&key=51500e0f085741f591dc0356d9a03ff4");
            }
        }
    }

    private void checkPreference() {
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        temperature = sharedPreferences.getBoolean("isFahrenheitOn", true);
    }

    private double getTemp(final double temp) {
        if (!temperature) {
            return (temp - 32) / 1.8;
        }
        return temp;
    }

    private void getWeatherCurrent(final String url) {
        // Resend the request to retrieve the current forecast.
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            final StringBuilder output = new StringBuilder();

            try {
                final JSONObject jsonResponse = new JSONObject(response);
                final JSONArray jsonArray = jsonResponse.getJSONArray("data");
                final JSONObject jsonobject = jsonArray.getJSONObject(0);
                final double temp = jsonobject.getDouble("temp");
                final int humidity = jsonobject.getInt("rh");
                final double wind = jsonobject.getDouble("wind_spd");
                final int clouds = jsonobject.getInt("clouds");
                final String state = jsonobject.getString("city_name");
                final String countryCode = jsonobject.getString("country_code");
                coordinates = new LatLng(jsonobject.getDouble("lat"), jsonobject.getDouble("lon"));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(coordinates));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, mMap.getCameraPosition().zoom));
                output.append(" Current Forecast in\n").append(state).append(" (").
                        append(countryCode).append(")").append("\n\n").append(df.format(getTemp(temp))).
                        append((temperature)?" °F":" °C").append("\n Humidity: ").append(humidity).append("%").
                        append("\n Wind Speed: ").append(df.format(wind)).append(" mph").
                        append("\n Cloudiness: ").append(clouds).append("%\n\n");
                current = output.toString();
                mBinding.currentWeather.setText(current); // Fill up the textview with the weather data

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getWeatherHourly(final String url) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            final StringBuilder output = new StringBuilder();
            final StringBuilder output2 = new StringBuilder();
            try {
                final JSONObject jsonResponse = new JSONObject(response);
                final JSONArray jsonArray = jsonResponse.getJSONArray("data");
                output.append(String.format("%-4s", ""));

                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject jsonobject = jsonArray.getJSONObject(i);
                    final double temp = jsonobject.getDouble("temp");
                    final String time = jsonobject.getString("timestamp_local");
                    final int t = Integer.parseInt(time.substring(11, 13));
                    final String x = "" + ((t % 12 == 0)? 12:t % 12);
                    if (t % 12 == t) {
                        output.append(String.format("%-12s", x + " AM"));
                    } else {
                        output.append(String.format("%-12s", x + " PM"));
                    }

                    output2.append(df.format(getTemp(temp))).append(String.format("%-8s", (temperature)?" °F":" °C"));
                }
                hourly = output + "\n" + output2;
                mBinding.hourlyWeather.setText(hourly); // Fill up the textview with the weather data
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getWeatherDaily(final String url) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            final StringBuilder output = new StringBuilder();

            try {
                final JSONObject jsonResponse = new JSONObject(response);
                final JSONArray jsonArray = jsonResponse.getJSONArray("data");
                for (int i = 1; i < jsonArray.length(); i++) {
                    final JSONObject jsonobject = jsonArray.getJSONObject(i);
                    final double temp = jsonobject.getDouble("temp");
                    final int humidity = jsonobject.getInt("rh");
                    final double wind = jsonobject.getDouble("wind_spd");
                    final String clouds = jsonobject.getString("clouds");
                    final String dates = jsonobject.getString("datetime"); // "EEE, MMM d, yy"
                    final DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
                    final LocalDate localDate = LocalDate.parse(dates);

                    output.append(localDate.format(f)).append("\n").append(df.format(getTemp(temp))).append((temperature)?" °F":" °C")
                            .append("\n Humidity: ").append(humidity).append("%").append("\n Wind Speed: ")
                            .append(df.format(wind)).append(" mph").append("\n Cloudiness: ").append(clouds).append("%\n\n");
                }
                daily = output.toString();
                mBinding.tvResult.setText(daily); // Fill up the textview with the weather data
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapClick(final @NonNull LatLng latLng) {
        coordinates = latLng;
        getWeatherCurrent("https://api.weatherbit.io/v2.0/current?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&units=I&key=51500e0f085741f591dc0356d9a03ff4");
        getWeatherHourly("https://api.weatherbit.io/v2.0/forecast/hourly?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4");
        getWeatherDaily("https://api.weatherbit.io/v2.0/forecast/daily?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&days=11&units=I&key=51500e0f085741f591dc0356d9a03ff4");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final @NonNull GoogleMap googleMap) {
        mMap = googleMap;
        final LocationViewModel model = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                if (coordinates == null) {
                    coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                }
                getWeatherCurrent("https://api.weatherbit.io/v2.0/current?lat=" + coordinates.latitude + "&lon=" + coordinates.longitude + "&units=I&key=51500e0f085741f591dc0356d9a03ff4");
                getWeatherHourly("https://api.weatherbit.io/v2.0/forecast/hourly?lat=" + coordinates.latitude + "&lon=" + coordinates.longitude + "&hours=24&units=I&key=51500e0f085741f591dc0356d9a03ff4");
                getWeatherDaily("https://api.weatherbit.io/v2.0/forecast/daily?lat=" + coordinates.latitude + "&lon=" + coordinates.longitude + "&days=11&units=I&key=51500e0f085741f591dc0356d9a03ff4");

            }
        });
        mMap.setOnMapClickListener(this);
    }
}

