package demo.wattpad.lab.com.wattpadfeeddemo.network;

import java.util.List;

/**
 * Created by lab on 2017-10-14.
 */

public class NetworkResponse<T> {
    private T data;
    private boolean reloaded;

    public NetworkResponse(T data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data==null || (data instanceof List && ((List) data).size()==0);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isReloaded() {
        return reloaded;
    }

    public void setReloaded(boolean reloaded) {
        this.reloaded = reloaded;
    }
}
