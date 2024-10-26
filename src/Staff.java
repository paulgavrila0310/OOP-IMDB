import java.util.List;
import java.util.SortedSet;

public abstract class Staff extends User implements StaffInterface {
    private List<Request> requestList;
    private List<ProductionsAndActors> addedEntries;

    public Staff(Information userInfo, AccountType accountType, String username, int xp, List<String> notifications, List<ProductionsAndActors> favorites, List<Request> requestList, List<ProductionsAndActors> addedEntries) {
        super(userInfo, accountType, username, xp, notifications, favorites);
        this.requestList = requestList;
        this.addedEntries = addedEntries;
    }

    public Staff() {

    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public List<ProductionsAndActors> getAddedEntries() {
        return addedEntries;
    }

    public void setAddedEntries(List<ProductionsAndActors> addedEntries) {
        this.addedEntries = addedEntries;
    }

    public void addRequest(Request request) { requestList.add(request);}

    public void deleteRequest(Request request) { requestList.remove(request);}

    public void addAddedEntry(ProductionsAndActors o) { addedEntries.add(o);}

    public void removeAddedEntry(Object o) { addedEntries.remove(o);}
}
