package demo.wattpad.lab.com.wattpadfeeddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import demo.wattpad.lab.com.wattpadfeeddemo.models.Story;
import demo.wattpad.lab.com.wattpadfeeddemo.models.StoryFeed;
import demo.wattpad.lab.com.wattpadfeeddemo.network.NetworkClientListener;
import demo.wattpad.lab.com.wattpadfeeddemo.network.NetworkResponse;
import demo.wattpad.lab.com.wattpadfeeddemo.network.StoriesFeedClient;
import demo.wattpad.lab.com.wattpadfeeddemo.util.GlideLoader;

public class MainActivity extends AppCompatActivity implements NetworkClientListener<StoryFeed>{

    private RecyclerView storyFeedRecyclerView;
    private StoriesFeedClient client;

    private String nextUrl;
    private List<Story> stories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root_container);

        //add recyclerView to Activity
        storyFeedRecyclerView = new RecyclerView(this);
        storyFeedRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        storyFeedRecyclerView.setAdapter(new StoryfeedRecyclerViewAdapter());
        storyFeedRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        relativeLayout.addView(storyFeedRecyclerView);

        storyFeedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if(recyclerView.getAdapter().getItemCount() - layoutManager.findLastVisibleItemPosition()<8) {
                    if (nextUrl != null) {
                        client.setUrl(nextUrl);
                        client.reload();
                    }
                }
            }
        });

        stories = new ArrayList<>();

        setSupportActionBar(toolbar);

        //retrieve data
        client = new StoriesFeedClient(null);
        client.setListener(this);
        client.reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(NetworkResponse<StoryFeed> response) {
        nextUrl = response.getData().getNextUrl();
        stories.addAll(response.getData().getStories());
        storyFeedRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onError(Error error) {
        //handle error here ie: network issues
    }

    private class StoryfeedRecyclerViewAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StoryFeedViewHolder(parent.inflate(MainActivity.this, R.layout.recyclerview_storyitem, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position >= 0 && position < stories.size()) {
                ((StoryFeedViewHolder)holder).bindData(stories.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return (stories == null) ? 0 : stories.size();
        }
    }

    private class StoryFeedViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatorView;
        private TextView titleView;
        private TextView nameView;
        private TextView fullNameView;

        public StoryFeedViewHolder(View itemView) {
            super(itemView);
            avatorView = (ImageView) itemView.findViewById(R.id.avator_view);
            titleView = (TextView) itemView.findViewById(R.id.title_textview);
            nameView = (TextView) itemView.findViewById(R.id.name_textview);
            fullNameView = (TextView) itemView.findViewById(R.id.fullname_textview);
        }

        public void bindData(Story data){
            GlideLoader.with(avatorView, data.getUser().getAvatar(), R.drawable.default_thumbnail).load();
            titleView.setText(data.getTitle() + " (Title)");
            nameView.setText(data.getUser().getName() + " (Name)");
            fullNameView.setText(data.getUser().getFullname() + " (FullName)");
        }
    }
}
