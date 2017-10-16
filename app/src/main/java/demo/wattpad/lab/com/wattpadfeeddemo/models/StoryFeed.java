package demo.wattpad.lab.com.wattpadfeeddemo.models;

import java.util.List;

/**
 * Created by lab on 2017-10-15.
 */

public class StoryFeed {
    List<Story> stories;
    private String nextUrl;

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
