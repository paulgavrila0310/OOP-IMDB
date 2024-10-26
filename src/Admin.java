import java.util.List;
import java.util.SortedSet;

public class Admin extends Staff{

    public Admin(Information userInfo, AccountType accountType, String username, int xp, List<String> notifications, List<ProductionsAndActors> favorites, List<Request> requestList, List<ProductionsAndActors> addedEntries) {
        super(userInfo, accountType, username, xp, notifications, favorites, requestList, addedEntries);
    }

    public Admin() {
    }

    @Override
    public void addProductionSystem(Production p) {

    }

    @Override
    public void addActorSystem(Actor a) {

    }

    @Override
    public void removeProductionSystem(String name) {

    }

    @Override
    public void removeActorSystem(String name) {

    }

    @Override
    public void updateProduction(Production p) {

    }

    @Override
    public void updateActor(Actor a) {

    }
}
