package com.cyruszhang.cluboard.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cyruszhang.cluboard.R;
import com.cyruszhang.cluboard.activity.NewEvent;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToTimePickerFragment extends DialogFragment{

    public ToTimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), NewEvent.toTimeListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
