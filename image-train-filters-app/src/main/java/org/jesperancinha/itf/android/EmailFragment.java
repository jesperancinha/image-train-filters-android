package org.jesperancinha.itf.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmailFragment extends Fragment {

    private View mainView;

    private EditText textTo;
    private EditText textCC;
    private EditText textBCC;
    private EditText textEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_email, container, false);
            textTo = mainView.findViewById(R.id.editEmailTo);
            textCC = mainView.findViewById(R.id.editEmailCC);
            textBCC = mainView.findViewById(R.id.editEmailBCC);
            textEmail = mainView.findViewById(R.id.editEmailText);
        }
        return mainView;
    }

    public EditText getTextTo() {
        return textTo;
    }

    public EditText getTextCC() {
        return textCC;
    }

    public EditText getTextBCC() {
        return textBCC;
    }

    public EditText getTextEmail() {
        return textEmail;
    }
}
