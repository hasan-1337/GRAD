package edu.uw.tcss450.labose.signinandregistration.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentHomeBinding bind = FragmentHomeBinding.bind(view);
        bind.contact.setOnClickListener(this::goContacts);
        bind.chat.setOnClickListener(this::goChat);
        bind.setting.setOnClickListener(this::goSetting);
        bind.forecast.setOnClickListener(this::goWeather);

        final SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
        final JWT jwt = new JWT(prefs.getString(getString(R.string.keys_prefs_jwt), ""));
        String email = jwt.getClaim("email").asString();

        for (int i = 0; i <= Objects.requireNonNull(email).length(); i++) {
            if (email.charAt(i) == '@') {
                email = email.substring(0, i);
                email = email.substring(0, 1).toUpperCase() + email.substring(1);
                break;
            }
        }

        bind.username.setText("Welcome " + email);
        customizeText(bind.username, email);
    }

    /**
     * Customize a text in TextView
     * @param tv TextView or Edittext or Button or child of TextView class
     * @param textToChange Text to highlight
     */
    private void customizeText(TextView tv, String textToChange) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToChange, 0);
        SpannableString wordToSpan = new SpannableString(tv.getText());

        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToChange, ofs);
            if (ofe == -1) {
                break;
            } else {
                wordToSpan.setSpan(new ForegroundColorSpan(Color.RED), ofe, ofe + textToChange.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    /**
     * Navigate to contacts page.
     * @param view View for the fragment
     */
    private void goContacts(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_contacts);
    }

    /**
     * Navigate to chat page.
     * @param view View for the fragment
     */
    private void goChat(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_chatlist);
    }

    /**
     * Navigate to settings page.
     * @param view View for the fragment
     */
    private void goSetting(final View view) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    /**
     * Navigate to weather page.
     * @param view View for the fragment
     */
    private void goWeather(final View view) {
        ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_weather);
    }
}

