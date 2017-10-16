package demo.wattpad.lab.com.wattpadfeeddemo.network;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by lab on 2017-10-14.
 */

public class NetworkClient<T> {
    private NetworkClientListener<T> listener;

    private boolean loading;
    private boolean reloading;

    public boolean reload() {
        if(!loading && !reloading) {
            loading = reloading = true;
            return true;
        }else {
            return false;
        }
    }

    protected void handleSuccess(final NetworkResponse response) {
        response.setReloaded(reloading);

        runInForeground(new Runnable() {
            @Override
            public void run() {
                loading = reloading = false;
                if(listener!=null) {
                    listener.onSuccess(response);
                }
            }
        });
    }

    protected void handleError(final Error error) {
        runInForeground(new Runnable() {
            @Override
            public void run() {
                loading = reloading = false;

                if(listener!=null) {
                    listener.onError(error);
                }
            }
        });
    }

    protected void runInBackground(Runnable runnable) {
        new Thread(runnable).start();
    }

    protected void runInForeground(Runnable runnable) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }

    public boolean isLoading() {
        return loading;
    }

    public NetworkClientListener getListener() {
        return listener;
    }

    public void setListener(NetworkClientListener<T> listener) {
        this.listener = listener;
    }
}
