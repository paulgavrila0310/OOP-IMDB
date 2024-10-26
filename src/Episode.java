import java.time.Duration;


public class Episode {
    private String episodeName;
    private String duration;

    public Episode(String name, String duration) {
        this.episodeName = name;
        this.duration = duration;
    }

    public Episode() {
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String name) {
        this.episodeName = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
