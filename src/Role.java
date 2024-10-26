public class Role {
    private String title;
    private String type;

    public Role(String name, String genre) {
        this.title = name;
        this.type = genre;
    }

    public Role() {
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setType(String genre) {
        this.type = genre;
    }
}