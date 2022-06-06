package edu.uw.tcss450.labose.signinandregistration.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Location View Model class for weather location.
 */
public class LocationViewModel extends ViewModel {

    // Live data Object
    private final MutableLiveData<Location> mLocation;

    /**
     * Location View Model
     */
    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    /**
     * Location Observer
     * @param owner Owner
     * @param observer Observer
     */
    public void addLocationObserver(final @NonNull LifecycleOwner owner, final @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     * Set Location
     * @param location Location coordinates
     */
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }
}
