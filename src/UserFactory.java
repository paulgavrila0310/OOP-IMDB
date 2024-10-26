import java.util.List;

public class UserFactory {
    public static User createUser(AccountType userType, User.Information information, String username, int experience, List<String> notifications, List<ProductionsAndActors> favorites, List<Request> requestListForStaff, List<ProductionsAndActors> addedEntries) {
        switch (userType) {
            case Regular:
                return new Regular(information, AccountType.Regular, username, experience, notifications, favorites);
            case Contributor:
                return new Contributor(information, AccountType.Contributor, username, experience, notifications, favorites, requestListForStaff, addedEntries);
            case Admin:
                return new Admin(information, AccountType.Admin, username, 1000000, notifications, favorites, requestListForStaff, addedEntries);
            default:
                throw new IllegalArgumentException("Unknown user type");
        }
    }
}
