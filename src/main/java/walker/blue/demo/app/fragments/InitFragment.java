package walker.blue.demo.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import walker.blue.demo.app.R;

/**
 * Fragment that displays configuration of the Client
 */
public class InitFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final Button button = new Button(this.getActivity());
        button.setText("Initialize process");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new RunFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return button;
    }
}