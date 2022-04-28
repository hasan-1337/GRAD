package edu.uw.tcss450.labose.signinandregistration.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.labose.signinandregistration.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class sign extends Fragment {

    private Object SignInFragmentDirections;

    public sign() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentSignInBinding binding = FragmentSignInBinding.bind(getView());

        //On button click, navigate to MainActivity
        binding.buttonSignin.setOnClickListener(button -> {

            Navigation.findNavController(getView()).navigate(
                    SignInFragmentDirections
                            .actionSignInFragmentToMainActivity(
                                    generateJwt(binding.editEmail.getText().toString())

                            ));
            //This tells the containing Activity that we are done with it.
            //It will not be added to backstack.
            getActivity().finish();

        });
    }
    private String generateJwt(final String email) {
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret key don't use a string literal in " +
                    "production code!!!");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("name", name)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("JWT Failed to Create.");
        }
        return token;
    }

}