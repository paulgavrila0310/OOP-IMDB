import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Request {

    public static class RequestsHolder {
        private static List<Request> requestList = new ArrayList<Request>();

        //constructor
        public RequestsHolder(List<Request> requestList1) {
            requestList = requestList1;
        }

        public static List<Request> getRequestList() {
            return requestList;
        }

        public static void setRequestList(List<Request> requestList1) {
            requestList = requestList1;
        }

        public static void addRequest(Request request) {
            requestList.add(request);
        }

        public static void deleteRequest(Request request) {
            requestList.remove(request);
        }
    }

    private RequestType type;
    private LocalDateTime createdDate;
    @JsonAlias({"actorName","movieTitle"})
    private String actorOrMovieName;
    private String description;
    private String username;
    private String to;

    public Request(RequestType type, LocalDateTime createdDate, String actorName, String description, String username, String to) {
        this.type = type;
        this.createdDate = createdDate;
        this.actorOrMovieName = actorName;
        this.description = description;
        this.username = username;
        this.to = to;
    }

    public Request() {
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType requestType) {
        this.type = requestType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime creationDate) {
        this.createdDate = creationDate;
    }

    public String getActorOrMovieName() {
        return actorOrMovieName;
    }

    public void setActorOrMovieName(String name) {
        this.actorOrMovieName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String creatorUsername) {
        this.username = creatorUsername;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String solverUsername) {
        this.to = solverUsername;
    }
}
