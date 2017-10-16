package demo.wattpad.lab.com.wattpadfeeddemo.models;

/**
 * Created by lab on 2017-10-15.
 */

public class Story {
    private String id;
    private User user;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
