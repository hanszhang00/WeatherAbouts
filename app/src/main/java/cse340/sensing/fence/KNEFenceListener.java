package cse340.sensing.fence;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;

import cse340.sensing.PrependableTextView;
import cse340.sensing.R;

public class KNEFenceListener extends FenceBroadcastReceiver implements FenceBroadcastReceiver.FenceListener {

    private static final String FENCE_NAME = "LOCATION_FENCE";
    public static final double KNE_LATITUDE = 47.656673;
    public static final double KNE_LONGITUDE = -122.309153;
    public static final double KNE_RADIUS = 100;
    public static final long DWELL_TIME_MILLIS = 1;

    private final PrependableTextView mUpdate;
    private final ImageView imageView;

    public KNEFenceListener(AwarenessFence duringFence, AwarenessFence startFence,
                            AwarenessFence stopFence, Context context,
                            AppCompatActivity activity, PrependableTextView update,
                            ImageView imageView) {
        super(duringFence, startFence, stopFence, FENCE_NAME, context, activity);
        setFenceListener(this);
        mUpdate = update;
        this.imageView = imageView;
    }

    @Override
    public void duringFence(FenceState state) {
        switch (state.getCurrentState()) {
            case FenceState.TRUE:
                mUpdate.prependText(R.string.text_at_KNE);
                imageView.setImageResource(R.mipmap.kane);
                break;
            case FenceState.FALSE:
                mUpdate.prependText(R.string.text_not_at_KNE);
                imageView.setImageDrawable(null);
                break;
        }
    }

    @Override
    public void startingFence(FenceState state) {
        switch (state.getCurrentState()) {
            case FenceState.TRUE:
                mUpdate.prependText(R.string.text_entering_KNE);
                imageView.setImageResource(R.mipmap.kane);
                break;
            case FenceState.UNKNOWN:
                Log.i(TAG, "Unknown fence state");
                break;
        }

    }

    @Override
    public void endingFence(FenceState state) {
        switch (state.getCurrentState()) {
            case FenceState.TRUE:
                mUpdate.prependText(R.string.text_exiting_KNE);
                imageView.setImageDrawable(null);
                break;
            case FenceState.UNKNOWN:
                Log.i(TAG, "Unknown fence state");
                break;
        }
    }
}
