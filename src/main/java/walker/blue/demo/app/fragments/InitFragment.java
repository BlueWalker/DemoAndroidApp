package walker.blue.demo.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;

import walker.blue.core.lib.init.InitializeProcess;

/**
 * Fragment that displays configuration of the Client
 */
public class InitFragment extends Fragment {

    private static final int VOICE_PROMPT_REQUEST_CODE = 24;
    private static final String SPEECH_PROMPT = "Choose your destination";

    private List<String> userInput;
    private TextView textView;
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        textView = new TextView(getActivity());
        InitializeProcess initializeProcess = new InitializeProcess(getActivity());
        Executors.newSingleThreadExecutor().submit(initializeProcess);
//        Toast.makeText(getActivity(), "Done Calling stuff", Toast.LENGTH_SHORT).show();
        return textView;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d(this.getClass().getName(), "onActivityResult - " + requestCode + " - " + resultCode);
        if (requestCode == VOICE_PROMPT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            this.userInput = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (String s : this.userInput) {
                Log.d(this.getClass().getName(), s);
            }
            textView.setText(userInput.toString());
        }
    }

    private void getUserInput() {
        Log.d(this.getClass().getName(), "getUserInput");
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, SPEECH_PROMPT);
        this.startActivityForResult(intent, VOICE_PROMPT_REQUEST_CODE);
    }
}