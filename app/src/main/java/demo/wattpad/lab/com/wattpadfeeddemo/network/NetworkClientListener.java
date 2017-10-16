package demo.wattpad.lab.com.wattpadfeeddemo.network;

/**
 * Created by lab on 2017-10-14.
 */

public interface NetworkClientListener<T> {
    void onSuccess(NetworkResponse<T> response);
    void onError(Error error);
}
