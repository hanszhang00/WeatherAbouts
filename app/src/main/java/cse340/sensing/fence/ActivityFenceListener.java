package cse340.sensing.fence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;

import cse340.sensing.PrependableTextView;
import cse340.sensing.R;

public class ActivityFenceListener extends FenceBroadcastReceiver implements FenceBroadcastReceiver.FenceListener {
    private static final String FENCE_NAME = "ACTIVITY_FENCE";

    private final PrependableTextView mUpdate;

    public ActivityFenceListener(AwarenessFence startFence, AwarenessFence duringFence, AwarenessFence stopFence,
                                 Context context, AppCompatActivity activity, PrependableTextView update) {
        super(startFence, duringFence, stopFence, FENCE_NAME, context, activity);
        setFenceListener(this);
        mUpdate = update;
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void duringFence(FenceState state) {
        switch (state.getCurrentState()) {
            case FenceState.TRUE:
                mUpdate.prependText(R.string.text_during_walking);
                break;
            case FenceState.FALSE:
                mUpdate.prependText(R.string.text_during_walking);
                break;
            case FenceState.UNKNOWN:
                Log.i(TAG, "Unknown fence state");
                break;
        }
    }

    @Override
    public void startingFence(FenceState state) {
        switch (state.getCurrentState()) {
            case FenceState.TRUE:
                mUpdate.prependText(R.string.text_start_walking);
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
                mUpdate.prependText(R.string.text_stop_walking);
                break;
            case FenceState.UNKNOWN:
                Log.i(TAG, "Unknown fence state");
                break;
        }
    }
}