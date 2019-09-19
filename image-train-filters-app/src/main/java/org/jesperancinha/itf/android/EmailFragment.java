package org.jesperancinha.itf.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by joao on 28-2-16.
 */
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
            textTo = (EditText) mainView.findViewById(R.id.editEmailTo);
            textCC = (EditText) mainView.findViewById(R.id.editEmailCC);
            textBCC = (EditText) mainView.findViewById(R.id.editEmailBCC);
            textEmail = (EditText) mainView.findViewById(R.id.editEmailText);
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
