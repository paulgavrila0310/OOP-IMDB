import java.util.List;

public class Actor implements ProductionsAndActors {

    private String name;
    private List<Role> performances;
    private String biography;

    public Actor(String name, List<Role> filmography, String biography) {
        this.name = name;
        this.performances = filmography;
        this.biography = biography;
    }

    public Actor() {
    }

    public String getName() {
        return name;
    }

    public List<Role> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerformances(List<Role> filmography) {
        this.performances = filmography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void addPerformance(Role role) { performances.add(role); }

    public void removePerformance(Role role) { performances.remove(role); }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Biography: " + biography);
        System.out.println("Filmography: ");
        for (Role role : performances) {
            System.out.println(role.getTitle());
        }
    }

    @Override
    public int compareTo(ProductionsAndActors o) {
        return this.name.compareTo(((Actor)o).getName());
    }
}
