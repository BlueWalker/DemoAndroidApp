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
import android.widget.Button;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import walker.blue.core.lib.init.InitializeProcess;

/**
 * Fragment that displays configuration of the Client
 */
public class InitFragment extends Fragment {

    private static final int VOICE_PROMPT_REQUEST_CODE = 24;
    private static final String SPEECH_PROMPT = "Choose your destination";

    private ExecutorService executorService;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        if (executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        }
        final Button button = new Button(this.getActivity());
        button.setText("Initialize process");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();
            }
        });

        return button;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == VOICE_PROMPT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final List<String> userInput = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (final String s : userInput) {
                Log.d(this.getClass().getName(), String.format("\t%s", s));
            }
            this.executorService.submit(new InitializeProcess(this.getActivity(), userInput));
        }
    }

    private void getUserInput() {
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, SPEECH_PROMPT);
        this.startActivityForResult(intent, VOICE_PROMPT_REQUEST_CODE);
    }
}