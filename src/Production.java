import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "movie"),
        @JsonSubTypes.Type(value = Series.class, name = "series")
})
public abstract class Production implements Comparable<ProductionsAndActors>, ProductionsAndActors{
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String plot;
    private Double averageRating;

    public Production(String title, List<String> directorsList, List<String> actorsList, List<Genre> genresList, List<Rating> ratingsList, String description, Double score) {
        this.title = title;
        this.directors = directorsList;
        this.actors = actorsList;
        this.genres = genresList;
        this.ratings = ratingsList;
        this.plot = description;
        this.averageRating = score;
    }

    public Production() {

    }

    public abstract void displayInfo();
    public int compareTo(Production p) {
        return this.title.compareTo(p.getTitle());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directorsList) {
        this.directors = directorsList;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actorsList) {
        this.actors = actorsList;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genresList) {
        this.genres = genresList;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratingsList) {
        this.ratings = ratingsList;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String description) {
        this.plot = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double score) {
        this.averageRating = score;
    }

    public void addDirector(String name) { directors.add(name); }

    public void removeDirector(String name) { directors.remove(name); }

    public void addActor(String name) { actors.add(name); }

    public void removeActor(String name) { actors.remove(name); }

    public void addGenre(Genre genre) { genres.add(genre); }

    public void removeGenre(Genre genre) { genres.remove(genre); }

    public void addRating(Rating rating) { ratings.add(rating); }

    public void removeRating(Rating rating) { ratings.remove(rating); }
}
