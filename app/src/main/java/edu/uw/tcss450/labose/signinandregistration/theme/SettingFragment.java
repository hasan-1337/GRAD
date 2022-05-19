package edu.uw.tcss450.labose.signinandregistration.theme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import org.jetbrains.annotations.Nullable;
import com.google.android.material.badge.BadgeUtils;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentSettingBinding;
import edu.uw.tcss450.labose.signinandregistration.model.userViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private userViewModel viewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        viewModel = provider.get(userViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSettingsThemes.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SettingFragmentDirections.actionNavigationSettingsToTheme();
                ));

    }

}