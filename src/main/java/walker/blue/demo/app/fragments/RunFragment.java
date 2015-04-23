package walker.blue.demo.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import walker.blue.demo.app.R;
import walker.blue.demo.app.run.RunGPSThing;

/**
 * Fragment that displays configuration of the Client
 */
public class RunFragment extends Fragment {

    private static final String USER_INPUT_FORMAT = "%s\n";
    private static final int VOICE_PROMPT_REQUEST_CODE = 24;
    private static final String SPEECH_PROMPT = "Choose your destination";

    private ExecutorService executorService;
    private ScrollView mainScroll;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        if (executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        }
        this.mainScroll = (ScrollView) inflater.inflate(R.layout.run_activity_layout, null);
        this.getUserInput();//        this.onActivityResult(VOICE_PROMPT_REQUEST_CODE, Activity.RESULT_OK, null);
        return mainScroll;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == VOICE_PROMPT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final TextView userInputText = (TextView) this.mainScroll.findViewById(R.id.user_input_text);
            final List<String> userInput = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            final List<String> userInput = new ArrayList<>();
//            userInput.add("room 456");
            for (final String s : userInput) {
                userInputText.append(String.format(USER_INPUT_FORMAT, s));
            }
            userInputText.setVisibility(View.VISIBLE);
            this.executorService.submit(new RunGPSThing(this.getActivity(), userInput, this.mainScroll));
        }
    }

    private void getUserInput() {
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, SPEECH_PROMPT);
        this.startActivityForResult(intent, VOICE_PROMPT_REQUEST_CODE);
    }
}
