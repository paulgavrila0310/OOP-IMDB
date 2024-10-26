import java.util.List;

public class Rating {
    private String username;
    private int rating;
    private String comment;

    public Rating(String username, int grade, String comments) {
        this.username = username;
        this.rating = grade;
        this.comment = comments;
    }

    public Rating() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int grade) {
        this.rating = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comments) {
        this.comment = comments;
    }

    public void displayInfo() {
        System.out.println("    Username: " + username);
        System.out.println("    Rating: " + rating);
        System.out.println("    Comment: " + comment);
        System.out.println("\n");
    }
}
