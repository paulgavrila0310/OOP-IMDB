import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Movie")
public class Movie extends Production{
    private String duration;
    private int releaseYear;

    public Movie(String title, List<String> directorsList, List<String> actorsList, List<Genre> genresList, List<Rating> ratingsList, String description, Double score, String duration, int yearOfRelease) {
        super(title, directorsList, actorsList, genresList, ratingsList, description, score);
        this.duration = duration;
        this.releaseYear = yearOfRelease;
    }

    public Movie() {

    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + this.getTitle());
        System.out.println("Release year: " + this.getReleaseYear());
        System.out.println("Duration: " + this.getDuration());
        System.out.println("Cast: " + this.getActors().toString());
        System.out.println("Genres: " + this.getGenres().toString());
        System.out.println("Description: " + this.getPlot());
        System.out.println("Average rating: " + this.getAverageRating());
        System.out.println("Directors: " + this.getDirectors().toString());
        System.out.println("Ratings: ");
        for (Rating rating : this.getRatings()) {
            rating.displayInfo();
        }
    }

    @Override
    public int compareTo(Production p){
        super.compareTo(p);
        return 0;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int yearOfRelease) {
        this.releaseYear = yearOfRelease;
    }

    @Override
    public int compareTo(ProductionsAndActors o) {
        return 0;
    }
}
