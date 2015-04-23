package walker.blue.demo.app.run;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.core.lib.main.MainLoop;
import walker.blue.core.lib.main.UserStateHandler;
import walker.blue.core.lib.user.UserState;
import walker.blue.demo.app.R;
import walker.blue.path.lib.node.GridNode;

public class RunGPSThing implements Runnable {

    private static final String HISTORY_FORMAT = "%s | %s\n";
    private static final int MAX_OFF_COURSE = 2;
    private static final int MAX_WARNING_ZONE = 5;

    private Context context;
    private List<String> input;
    private View rootView;

    public RunGPSThing(final Context context, final List<String> input, final View rootView) {
        this.context = context;
        this.input = input;
        this.rootView = rootView;
    }

    public void run() {
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        Log.d(this.getClass().getName(), "Displaying Initialize process data");
        final InitializeProcess initializeProcess = new InitializeProcess(this.context, this.input);
        final InitializeProcess.Output initOutput = initializeProcess.call();
        if (initOutput.getError() != null) {
            Log.d(this.getClass().getName(), "Initialize process failed");
            return;
        }
        ((Activity) this.context).runOnUiThread(new Runnable (){
            @Override
            public void run() {
                Log.d(this.getClass().getName(), "Displaying Initialize process data");
                final TextView buildingText = (TextView) rootView.findViewById(R.id.building_text);
                final TextView startLocationText = (TextView) rootView.findViewById(R.id.start_location_text);
                final TextView destinationText = (TextView) rootView.findViewById(R.id.destination_text);
                final TextView pathText = (TextView) rootView.findViewById(R.id.path_text);
                buildingText.setText(initOutput.getBuilding().getUUID());
                startLocationText.setText(initOutput.getCurrentLocation().toString());
                final List<GridNode> path = initOutput.getPath();
                destinationText.setText(path.get(path.size() - 1).toString());
                for (final GridNode node : path) {
                    pathText.append(node.toString());
                    pathText.append("\n");
                }
                buildingText.setVisibility(View.VISIBLE);
                startLocationText.setVisibility(View.VISIBLE);
                destinationText.setVisibility(View.VISIBLE);
                pathText.setVisibility(View.VISIBLE);
            }
        });

        final UserStateHandler userStateHandler = new UserStateHandler() {

            private int offCourseCount = 0;
            private int warningZoneCount = 0;

            @Override
            public void newStateFound(final UserState userState) {
                switch (userState) {
                    case OFF_COURSE:
                        this.offCourseCount++;
                        if (this.offCourseCount == MAX_OFF_COURSE) {
                            // TODO find new path with current location
                        }
                        break;
                    case IN_WARNING_ZONE:
                        this.warningZoneCount++;
                        if (this.warningZoneCount == MAX_WARNING_ZONE) {
                            // TODO Let user know if theyre off course
                        }
                        break;
                }
            }
        };

        final TextView currentLocationText = (TextView) rootView.findViewById(R.id.current_location_text);
        final TextView currentUserStateText = (TextView) rootView.findViewById(R.id.current_user_state_text);
        final TextView historyText = (TextView) rootView.findViewById(R.id.history_text);
        final MainLoop mainLoop = new MainLoop(initOutput, this.context, userStateHandler);

        UserState currentState = null;
        while (currentState != UserState.ARRIVED) {
            final MainLoop.Output loopOutput = mainLoop.call();
            ((Activity) this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentLocationText.setText(loopOutput.getCurrentLocation().toString());
                    currentUserStateText.setText(loopOutput.getUserState().name());
                    historyText.append(String.format(HISTORY_FORMAT,
                                loopOutput.getUserState().name(),
                                loopOutput.getCurrentLocation().toString()));
                    if (currentLocationText.getVisibility() != View.VISIBLE) {
                        currentLocationText.setVisibility(View.VISIBLE);
                    }
                    if (currentUserStateText.getVisibility() != View.VISIBLE) {
                        currentUserStateText.setVisibility(View.VISIBLE);
                    }
                    if (historyText.getVisibility() != View.VISIBLE) {
                        historyText.setVisibility(View.VISIBLE);
                    }
                }
            });
            currentState = loopOutput.getUserState();
            x.add(loopOutput.getCurrentLocation().getX());
            y.add(loopOutput.getCurrentLocation().getY());
        }
    }
}
