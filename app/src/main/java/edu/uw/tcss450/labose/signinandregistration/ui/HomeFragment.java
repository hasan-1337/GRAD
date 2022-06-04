package edu.uw.tcss450.labose.signinandregistration.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.SettingsActivity;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentHomeBinding bind = FragmentHomeBinding.bind(view);
        bind.contact.setOnClickListener(this::goContacts);
        bind.chat.setOnClickListener(this::goChat);
        bind.setting.setOnClickListener(this::goSetting);
        bind.forecast.setOnClickListener(this::goWeather);
    }

    public void goContacts(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_contacts);
    }

    public void goChat(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_chatlist);
    }

    public void goSetting(final View view) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    public void goWeather(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_weather);
    }
}

