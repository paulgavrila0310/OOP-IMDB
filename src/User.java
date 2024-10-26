import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public abstract class User implements RequestObserver{
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private String birthDate;

        public static class Builder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private String birthDate;

            public Builder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder country(String country) {
                this.country = country;
                return this;
            }

            public Builder age(int age) {
                this.age = age;
                return this;
            }

            public Builder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public Builder birthDate(String birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public Information() {
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String dateOfBirth) {
            this.birthDate = dateOfBirth;
        }
    }
    private Information information;
    private AccountType userType;
    private String username;
    private int experience;
    private List<String> notifications;
    private List<ProductionsAndActors> favorites;

    public User(Information userInfo, AccountType accountType, String username, int xp, List<String> notifications, List<ProductionsAndActors> favorites) {
        this.information = userInfo;
        this.userType = accountType;
        this.username = username;
        this.experience = xp;
        this.notifications = notifications;
        this.favorites = favorites;
    }

    public User() {
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information userInfo) {
        this.information = userInfo;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType accountType) {
        this.userType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int xp) {
        this.experience = xp;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public List<ProductionsAndActors> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<ProductionsAndActors> favorites) {
        this.favorites = favorites;
    }

    public void addNotification(String notif) {notifications.add(notif);}

    public void removeNotification(String notif) { notifications.remove(notif);}

    public void addFavorite(ProductionsAndActors o) { favorites.add(o);}

    public void removeFavorite(Object o) { favorites.remove(o);}
    @Override
    public void updateRequestSolvedNotification(Request request) {
        String notif = "Your request has been solved by " + request.getTo() + " at " + LocalDateTime.now();
        this.addNotification(notif);
    }

    @Override
    public void updateRequestDeniedNotification(Request request) {
        String notif = "Your request has been denied by " + request.getTo() + " at " + LocalDateTime.now();
        this.addNotification(notif);
    }

    @Override
    public void updateProductionGotReviewedNotification(Production production, String reviewerUsername) {
        String notif = production.getTitle() + " has been reviewed by " + reviewerUsername + " at " + LocalDateTime.now();
        this.addNotification(notif);
    }

    @Override
    public void updateReceivedRequestNotification(Request request) {
        String notif = "You have received a request from " + request.getUsername() + " at " + LocalDateTime.now();
        this.addNotification(notif);
    }

    @Override
    public void updateAddedProductionReviewNotification(Production production) {
        String notif = "A review has been added to " + production.getTitle() + " at " + LocalDateTime.now();
        this.addNotification(notif);
    }
}
