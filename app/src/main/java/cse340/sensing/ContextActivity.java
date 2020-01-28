package cse340.sensing;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.Task;

@SuppressLint("Registered")
public class ContextActivity extends AppCompatActivity {

    public interface SnapshotListener<T extends Response> {
        void onSnapshot(T response);
    }

    /**
     * Sets up a link between a task and a listener so the listener is notified when the task completes.
     *
     * @param task the Task whose result you want to take action on
     * @param snapshotListener the Listener that is called on when the task succeeds or fails
     */
    protected <T extends Response> void setSnapshotListener(Task<T> task, final SnapshotListener<T> snapshotListener) {
        // the task (aka getting some awareness-related information, either succeeds or fails
        // so this line is saying that, if it succeeds, it calls onSnapshot method of that particular listener
        task.addOnSuccessListener(snapshotListener::onSnapshot);
        // if the task is not completed, log the error to console
        task.addOnFailureListener((e) -> Log.e("CSE340", "Could not get snapshot: " + e));
    }
}