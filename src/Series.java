import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Series")
public class Series extends Production{
    private int releaseYear;
    private int numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title, List<String> directorsList, List<String> actorsList, List<Genre> genresList, List<Rating> ratingsList, String description, Double score, int yearOfRelease, int nrOfSeasons, Map<String, List<Episode>> episodeList) {
        super(title, directorsList, actorsList, genresList, ratingsList, description, score);
        this.releaseYear = yearOfRelease;
        this.numSeasons = nrOfSeasons;
        this.seasons = episodeList;
    }

    public Series() {

    }

    public void displayInfo() {
        System.out.println("Title: " + this.getTitle());
        System.out.println("Release year: " + this.getReleaseYear());
        System.out.println("Number of seasons: " + this.getNumSeasons());
        System.out.println("Cast: " + this.getActors().toString());
        System.out.println("Genres: " + this.getGenres().toString());
        System.out.println("Description: " + this.getPlot());
        System.out.println("Average rating: " + this.getAverageRating());
        System.out.println("Directors: " + this.getDirectors().toString());
        System.out.println("Ratings: ");
        for (Rating rating : this.getRatings()) {
            rating.displayInfo();
        }
        System.out.println("Seasons: ");
        for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
            System.out.println(entry.getKey() + ": ");
            int count = 1;
            for (Episode episode : entry.getValue()) {
                System.out.println("    " + count + ". " + episode.getEpisodeName());
                count++;
            }
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int yearOfRelease) {
        this.releaseYear = yearOfRelease;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int nrOfSeasons) {
        this.numSeasons = nrOfSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> episodeList) {
        this.seasons = episodeList;
    }

    public void addSeason(String title, List<Episode> epsOfSeason) { seasons.put(title, epsOfSeason);}

    public void removeSeason(String title) { seasons.remove(title);}

    @Override
    public int compareTo(ProductionsAndActors o) {
        return 0;
    }
}
