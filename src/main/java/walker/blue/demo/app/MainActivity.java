package walker.blue.demo.app;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import walker.blue.demo.app.fragments.InitFragment;

/**
 * Main activity of the application. 
 */
public class MainActivity extends ActionBarActivity {

    /**
     * Value used to request the dialog which prompts the user to
     * turn bluetooth on
     */
    private static final int PROMPT_ENABLE_BT = 1;

    /**
     * Progress bar located on the ActionBar
     */
    private ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new InitFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROMPT_ENABLE_BT && resultCode == RESULT_OK) {
        }
    }

    /**
     * Changes the fragment being displayed in the content_frame and pushes
     * that transaction into the back stack.
     *
     * @param fragment Fragment
     */
    public void changeFragment(final Fragment fragment) {
        if (fragment != null) {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Sets the visibility of the progressbar located in the actionbar
     *
     * @param visibility boolean (true -> Visible false -> invisible)
     */
    public void setProgressbarVisibility(final boolean visibility) {
        this.progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Calls the actiivty to prompt the user to enable bluetooth
     */
    public void promptUserEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, PROMPT_ENABLE_BT);
    }
}
