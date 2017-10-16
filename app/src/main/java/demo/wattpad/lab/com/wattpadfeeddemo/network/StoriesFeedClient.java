package demo.wattpad.lab.com.wattpadfeeddemo.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import demo.wattpad.lab.com.wattpadfeeddemo.models.StoryFeed;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lab on 2017-10-14.
 */

public class StoriesFeedClient extends NetworkClient<StoryFeed> {
    public static final String REQUEST_URL = "https://www.wattpad.com/api/v3/stories?offset=0&limit=40&fields=stories(id,title,user)";
    private String url;

    public StoriesFeedClient(String url) {
        this.url = url;

        if (this.url == null){
            this.url = REQUEST_URL;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean reload() {
        if(super.reload()) {
            runInBackground(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client = (new OkHttpClient.Builder()).build();
                        final Request request = new Request.Builder().url(url).build();
                        Call call = client.newCall(request);
                        Response response = call.execute();
                        if(response.isSuccessful()) {
                            String responseText = response.body().string();

                            JsonParser parser = new JsonParser();
                            JsonObject json = parser.parse(responseText).getAsJsonObject();
                            Gson gson = new Gson();
                            StoryFeed feedObj = gson.fromJson(json, StoryFeed.class);
                            handleSuccess(new NetworkResponse(feedObj));
                        }
                    }catch(Exception e) {
                        handleError(new Error(e.getLocalizedMessage()));
                    }
                }
            });
            return true;
        }else{
            return false;
        }
    }
}
