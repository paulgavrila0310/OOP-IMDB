import java.util.List;
import java.util.SortedSet;

public class Regular extends User implements RequestsManager{
    public Regular(Information userInfo, AccountType accountType, String username, int xp, List<String> notifications, List<ProductionsAndActors> favorites) {
        super(userInfo, accountType, username, xp, notifications, favorites);
    }

    public Regular() {
    }

    @Override
    public void createRequest(Request r) {

    }

    @Override
    public void removeRequest(Request r) {

    }
}
