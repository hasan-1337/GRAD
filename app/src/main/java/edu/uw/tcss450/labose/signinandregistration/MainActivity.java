package edu.uw.tcss450.labose.signinandregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import edu.uw.tcss450.labose.signinandregistration.model.userViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());


        new ViewModelProvider(this,
                new userViewModel.userViewModelFactory(args.getEmail(), args.getJwt())
        ).get(userViewModel.class);
         */
        setContentView(R.layout.activity_main);

    }
}