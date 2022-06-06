package edu.uw.tcss450.labose.signinandregistration.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;

import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    // The Fragment binding
    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Settings");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        binding.nightmode.setChecked(sharedPreferences.getBoolean("isDarkModeOn", true));
        binding.weather.setChecked(sharedPreferences.getBoolean("isFahrenheitOn", true));
        binding.nightmode.setOnCheckedChangeListener(this::switchNightMode);
        binding.weather.setOnCheckedChangeListener(this::switchWeather);

        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
        final JWT jwt = new JWT(prefs.getString(getString(R.string.keys_prefs_jwt), ""));
        String email = jwt.getClaim("email").asString();
        binding.email.setText(email);

        for (int i = 0; i <= Objects.requireNonNull(email).length(); i++) {
            if (email.charAt(i) == '@') {
                email = email.substring(0, i);
                email = email.substring(0, 1).toUpperCase() + email.substring(1);
                break;
            }
        }
        String name = sharedPreferences.getString("Name", "");
        binding.name.setText(name);
        binding.username.setText(email);
    }

    /**
     * Get the user's night mode preference.
     * @param compoundButton Button's object
     * @param b Whether the button is switched or not.
     */
    private void switchNightMode(final CompoundButton compoundButton, final boolean b) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("isDarkModeOn", false);
            editor.apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("isDarkModeOn", true);
            editor.apply();
        }
    }

    /**
     * Get the user's weather preference.
     * @param compoundButton Button's object
     * @param b Whether the button is switched or not.
     */
    private void switchWeather(final CompoundButton compoundButton, final boolean b) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (b) {
            editor.putBoolean("isFahrenheitOn", true);
            editor.apply();
        } else {
            editor.putBoolean("isFahrenheitOn", false);
            editor.apply();
        }
    }
}