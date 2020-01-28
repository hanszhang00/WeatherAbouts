package cse340.sensing.fence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;

public class FenceBroadcastReceiver extends BroadcastReceiver {

    protected final String DURING_FENCE_KEY;
    protected final String STARTING_FENCE_KEY;
    protected final String STOPPING_FENCE_KEY;
    protected final String mFenceName, TAG;

    protected AwarenessFence mStartFence;
    protected AwarenessFence mDuringFence;
    protected AwarenessFence mStopFence;

    protected Context mContext;

    protected FenceClient mFenceClient;

    protected PendingIntent mPendingIntent;
    protected Intent mIntent;
    protected IntentFilter mIntentFilter;

    /***
     * Creates a new fence broadcast receiver with the given conditions.
     *
     * @param startFence Defines when the fence is starting.
     * @param duringFence Defines when the fence is active.
     * @param stopFence Defines when this fence is stopping.
     * @param fenceName Unique string identifying this fence.
     */
    public FenceBroadcastReceiver(AwarenessFence startFence, AwarenessFence duringFence, AwarenessFence stopFence,
                                  String fenceName, Context context, AppCompatActivity activity) {
        mDuringFence = duringFence;
        mStartFence = startFence;
        mStopFence = stopFence;
        mFenceName = fenceName;

        mIntent = new Intent(fenceName);
        mIntentFilter = new IntentFilter(fenceName);

        TAG = "CSE340 " + mFenceName;
        DURING_FENCE_KEY = "DURING_FENCE_KEY_" + mFenceName;
        STARTING_FENCE_KEY = "STARTING_FENCE_KEY_" + mFenceName;
        STOPPING_FENCE_KEY = "STOPPING_FENCE_KEY_" + mFenceName;
        Log.i(TAG, "Created intent: " + mIntent);

        setContext(context);
        setFenceClient(activity);
    }

    //region Fence Listeners and Registration
    /**
     * Listener for events during, on start, and on end of fence.
     */
    public interface FenceListener {
        // defines program behavior when user is entering Fence conditions
        void startingFence(FenceState state);

        // defines program behavior when user in Fence conditions
        void duringFence(FenceState state);

        // defines program behavior when user is exiting Fence conditions
        void endingFence(FenceState state);
    }

    protected FenceListener mListener;

    public void setFenceListener(FenceListener listener) {
        mListener = listener;
    }

    public void register() {
        if (mPendingIntent != null) {
            Log.i(TAG, "Registering intent: " + String.valueOf(mPendingIntent));
            registerFence();
            mContext.registerReceiver(this, mIntentFilter);
        }
    }

    public void unregister() {
        if (mPendingIntent != null) {
            Log.i(TAG, "Unregistering intent: " + String.valueOf(mPendingIntent));
            unregisterFence();
            mContext.unregisterReceiver(this);
        }
    }
    //endregion

    //region Setters
    protected void setContext(Context context) {
        mContext = context;
        mPendingIntent = PendingIntent.getBroadcast(context, 1, mIntent, 0);
        Log.i(TAG, "Created intent: " + String.valueOf(mPendingIntent));

    }

    protected void setFenceClient(AppCompatActivity activity) {
        mFenceClient = Awareness.getFenceClient(activity);
    }
    //endregion

    /**
     * Registers this fence with the Awareness API.
     */
    protected void registerFence() {
        Log.i(TAG, "Registering " + mFenceName);

        if (mPendingIntent == null) {
            Log.i(TAG, "Null intent!");
            return;
        }

        mFenceClient.updateFences(
                new FenceUpdateRequest.Builder()
                        .addFence(STARTING_FENCE_KEY, mStartFence, mPendingIntent)
                        .addFence(DURING_FENCE_KEY, mDuringFence, mPendingIntent)
                        .addFence(STOPPING_FENCE_KEY, mStopFence, mPendingIntent)
                        .build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Fences " + STARTING_FENCE_KEY +
                        ", " + DURING_FENCE_KEY + ", and " + STOPPING_FENCE_KEY +
                        " were successfully registered."))
                .addOnFailureListener(e -> Log.e(TAG, "Fence for " + mFenceName +
                        " could not be registered: " + e));
    }


    /***
     * Registers this fence with the Awareness API.
     */
    public void unregisterFence() {
        Log.i(TAG, "Unregistering " + mFenceName);

        mFenceClient.updateFences(
                new FenceUpdateRequest.Builder()
                        .removeFence(STARTING_FENCE_KEY)
                        .removeFence(DURING_FENCE_KEY)
                        .removeFence(STOPPING_FENCE_KEY)
                        .build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Fence for " + mFenceName +
                        " was successfully unregistered."))
                .addOnFailureListener(e -> Log.e(TAG, "Fence for " + mFenceName +
                        " could not be unregistered: " + e));
    }

    /**
     * Receives broadcasts from the Awareness API and notifies the listener accordingly
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received intent: " + String.valueOf(intent));

        FenceState state = FenceState.extract(intent);

        String key = state.getFenceKey();
        Log.i(TAG, "Received fence key: " + key);
        Log.i(TAG, "Received fence state: " + state.getCurrentState());

        if (key.equals(STARTING_FENCE_KEY)) {
            mListener.startingFence(state);
        } else if (key.equals(DURING_FENCE_KEY)) {
            mListener.duringFence(state);
        } else if (key.equals(STOPPING_FENCE_KEY)) {
            mListener.endingFence(state);
        } else {
            Log.e(TAG, "Unrecognized key for " + mFenceName + ": " + key);
        }
    }
}
