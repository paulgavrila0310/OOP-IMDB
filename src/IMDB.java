import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class IMDB {
    private static IMDB instance;
    private List<User> userList;
    private List<Actor> actorList;
    private List<Request> requestList;
    private List<Production> productionList;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Scanner scanner = new Scanner(System.in);
    private static Robot robot;
    private User currentUser;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    int[][] permissionsMatrix = {
            // Regular   Contributor   Admin
            {1, 1, 1},  // 1. Vizualizati detalii productii
            {1, 1, 1},  // 2. Vizualizati detalii actori
            {1, 1, 1},  // 3. Vizualizati notificarile primite
            {1, 1, 1},  // 4. Cautati filme/seriale/actori
            {1, 1, 1},  // 5. Modificati lista de favorite
            {1, 1, 0},  // 6. Creati sau retrageti o cerere
            {0, 1, 1},  // 7. Adaugati/stergeti productii sau actori din sistem
            {0, 1, 1},  // 8. Vizualizati si rezolvati cereri
            {0, 1, 1},  // 9. Actualizati informatiile despre productii/actori
            {1, 0, 0},  // 10. Adaugati/stergeti o recenzie
            {0, 0, 1},  // 11. Adaugati/stergeti un utilizator
            {1, 1, 1}   // 12. Delogare
    };

    int [][] productionsInfoMatrix = {
            //Movie  Series
            {1, 1},  // 1. Titlu
            {1, 1},  // 2. Regizori
            {1, 1},  // 3. Actori
            {1, 1},  // 4. Genuri
            {1, 1},  // 5. Plot
            {1, 0},  // 6. Durata
            {1, 1},  // 7. Anul lansarii
            {0, 1},  // 8. Episoade
    };

    private IMDB() {
        userList = new ArrayList<User>();
        actorList = new ArrayList<Actor>();
        requestList = new ArrayList<Request>();
        productionList = new ArrayList<Production>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            synchronized (IMDB.class) {
                if (instance == null) {
                    instance = new IMDB();
                }
            }
        }
        return instance;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void addActor(Actor actor) {
        actorList.add(actor);
    }

    public void addRequest(Request request) {
        requestList.add(request);
    }

    public void addProduction(Production production) {
        productionList.add(production);
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public List<Production> getProductionList() {
        return productionList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public void setProductionList(List<Production> productionList) {
        this.productionList = productionList;
    }

    public User getUser(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static int getUserTypeIndex(String userType) {
        switch (userType) {
            case "Regular":
                return 0;
            case "Contributor":
                return 1;
            case "Admin":
                return 2;
            default:
                return -1;
        }
    }

    public Actor getActor(String name) {
        for (Actor actor : actorList) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public Production getProduction(String name) {
        for (Production production : productionList) {
            if (production.getTitle().equals(name)) {
                return production;
            }
        }
        return null;
    }

    public void removeUser(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                userList.remove(user);
                break;
            }
        }
    }

    public void removeActor(String name) {
        for (Actor actor : actorList) {
            if (actor.getName().equals(name)) {
                actorList.remove(actor);
                break;
            }
        }
    }

    public void removeProduction(String name) {
        for (Production production : productionList) {
            if (production.getTitle().equals(name)) {
                productionList.remove(production);
                break;
            }
        }
    }

    public static void clearScreen() {

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_3);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_3);

    }

    public void run() throws InterruptedException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode rootNode = objectMapper.readTree(new File("production.json"));

            for (JsonNode productionNode : rootNode){
                String title  = productionNode.get("title").asText();

                String type = productionNode.get("type").asText();

                JsonNode directors = productionNode.get("directors");
                List<String> directorsList = new ArrayList<String>();
                for (JsonNode directorsNode : directors){
                    directorsList.add(directorsNode.asText());
                }

                JsonNode actors = productionNode.get("actors");
                List<String> actorsList = new ArrayList<String>();
                for (JsonNode actorsNode : actors){
                    actorsList.add(actorsNode.asText());
                }

                JsonNode genres = productionNode.get("genres");
                List<Genre> genresList = new ArrayList<Genre>();
                for (JsonNode genresNode : genres){
                    genresList.add(Genre.valueOf(genresNode.asText()));
                }

                JsonNode ratings = productionNode.get("ratings");
                List<Rating> ratingsList = new ArrayList<Rating>();
                for (JsonNode ratingsNode : ratings) {
                    ratingsList.add(new Rating(ratingsNode.get("username").asText(), ratingsNode.get("rating").asInt(), ratingsNode.get("comment").asText()));
                }

                String plot = productionNode.get("plot").asText();

                Double averageRating = productionNode.get("averageRating").asDouble();

                int releaseYear = -1;
                if (productionNode.has("releaseYear")) {
                    releaseYear = productionNode.get("releaseYear").asInt();
                }


                if (type.equals("Movie")) {
                    String duration = null;
                    if (productionNode.has("duration")) {
                        duration = productionNode.get("duration").asText();
                    }

                    Movie movie = new Movie(title, directorsList, actorsList, genresList, ratingsList, plot, averageRating, duration, releaseYear);
                    productionList.add(movie);
                } else if (type.equals("Series")) {
                    int numSeasons = productionNode.get("numSeasons").asInt();

                    JsonNode seasonsNode = productionNode.get("seasons");
                    Map<String, List<Episode>> seasonsList = new HashMap<>();
                    if (seasonsNode != null && seasonsNode.isObject()) {
                        Iterator<Map.Entry<String, JsonNode>> seasonsIterator = seasonsNode.fields();
                        while (seasonsIterator.hasNext()) {
                            Map.Entry<String, JsonNode> seasonEntry = seasonsIterator.next();
                            String seasonTitle = seasonEntry.getKey();
                            JsonNode episodesNode = seasonEntry.getValue();
                            if (episodesNode != null && episodesNode.isArray()) {
                                List<Episode> episodesList = new ArrayList<>();
                                for (JsonNode episodeNode : episodesNode) {
                                    String episodeTitle = episodeNode.get("episodeName").asText();
                                    String episodeDuration = episodeNode.get("duration").asText();
                                    episodesList.add(new Episode(episodeTitle, episodeDuration));
                                }
                                seasonsList.put(seasonTitle, episodesList);
                            }
                        }
                    }

                    Series series = new Series(title, directorsList, actorsList, genresList, ratingsList, plot, averageRating, releaseYear, numSeasons, seasonsList);
                    productionList.add(series);
                } else {
                    throw new IOException("Unknown production type");
                }


            }

        } catch (IOException e) {
            System.out.print("Error while parsing data for Productions: " + e.getMessage());
        }

        try {
            JsonNode rootNode = objectMapper.readTree(new File("actors.json"));

            for (JsonNode actorNode : rootNode){
                String name = actorNode.get("name").asText();

                JsonNode performances = actorNode.get("performances");
                List<Role> performancesList = new ArrayList<Role>();
                for (JsonNode performancesNode : performances){
                    String title = performancesNode.get("title").asText();
                    String type = performancesNode.get("type").asText();
                    performancesList.add(new Role(title, type));
                }

                String biography = null;
                if (actorNode.has("biography")) {
                    biography = actorNode.get("biography").asText();
                }

                Actor actor = new Actor(name, performancesList, biography);
                actorList.add(actor);
            }

        } catch (IOException e) {
            System.out.print("Error while parsing data for Actors: " + e.getMessage());
        }


        try {
            JsonNode rootNode = objectMapper.readTree(new File("accounts.json"));

            for (JsonNode userNode : rootNode) {
                String username = userNode.get("username").asText();

                JsonNode experienceNode = userNode.get("experience");
                String experience;
                if (experienceNode == null) {
                    experience = null;
                } else {
                    experience = experienceNode.asText();
                }

                JsonNode informationNode = userNode.get("information");

                JsonNode credentialsNode = informationNode.get("credentials");
                String email = null;
                if (credentialsNode.has("email")) {
                    email = credentialsNode.get("email").asText();
                }
                String password = credentialsNode.get("password").asText();
                Credentials credentials = new Credentials(email, password);

                String name = informationNode.get("name").asText();

                String country = informationNode.get("country").asText();

                int age = informationNode.get("age").asInt();

                String gender = informationNode.get("gender").asText();

                String birthDate = informationNode.get("birthDate").asText();

                User.Information information = new User.Information.Builder()
                        .credentials(credentials)
                        .name(name)
                        .country(country)
                        .age(age)
                        .gender(gender)
                        .birthDate(birthDate)
                        .build();

                String userType = userNode.get("userType").asText();

                List<ProductionsAndActors> addedEntries = new ArrayList<ProductionsAndActors>();
                if (userType.equals("Contributor") || userType.equals("Admin")) {
                    if (userNode.has("productionsContribution")) {
                        JsonNode productionsContributions = userNode.get("productionsContribution");
                        for (JsonNode productionsContributionsNode : productionsContributions) {
                            addedEntries.add(getProduction(productionsContributionsNode.asText()));
                        }
                    }
                    if (userNode.has("actorsContribution")) {
                        JsonNode actorsContributions = userNode.get("actorsContribution");
                        for (JsonNode actorsContributionsNode : actorsContributions) {
                            addedEntries.add(getActor(actorsContributionsNode.asText()));
                        }
                    }
                }

                List<Request> requestListForStaff = new ArrayList<Request>();

                List<ProductionsAndActors> favorites = new ArrayList<ProductionsAndActors>();
                if (userNode.has("favoriteProductions")) {
                    JsonNode favoriteProductions = userNode.get("favoriteProductions");
                    for (JsonNode favoriteProductionsNode : favoriteProductions) {
                        favorites.add(getProduction(favoriteProductionsNode.asText()));
                    }
                }
                if (userNode.has("favoriteActors")) {
                    JsonNode favoriteActors = userNode.get("favoriteActors");
                    for (JsonNode favoriteActorsNode : favoriteActors) {
                        favorites.add(getActor(favoriteActorsNode.asText()));
                    }
                }

                List<String> notifications = new ArrayList<String>();
                if (userNode.has("notifications")) {
                    JsonNode notificationsNode = userNode.get("notifications");
                    for (JsonNode notificationNode : notificationsNode) {
                        notifications.add(notificationNode.asText());
                    }
                }

                int xp;
                try {
                    xp = (experience != null) ? Integer.parseInt(experience) : -1;
                } catch (NumberFormatException e) {
                    xp = -1;
                }

                User user = UserFactory.createUser(AccountType.valueOf(userType), information, username, xp, notifications, favorites, requestListForStaff, addedEntries);
                userList.add(user);
            }
        }
         catch (IOException e) {
            System.out.print("Error while parsing data for Users: " + e.getMessage());
        }

        try {
            JsonNode rootNode = objectMapper.readTree(new File("requests.json"));

            for (JsonNode requestNode : rootNode){
                RequestType type = RequestType.valueOf(requestNode.get("type").asText());

                LocalDateTime createdDate = LocalDateTime.parse(requestNode.get("createdDate").asText());

                String username = requestNode.get("username").asText();

                String to = requestNode.get("to").asText();

                String description = requestNode.get("description").asText();

                Request request = new Request(type, createdDate, username, description, username, to);
                requestList.add(request);

                if (request.getTo().equals("ADMIN")) {
                    Request.RequestsHolder.addRequest(request);
                } else {
                    for (User user : userList) {
                        if (user.getUsername().equals(request.getTo())) {
                            ((Staff)user).getRequestList().add(request);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.print("Error while parsing data for Requests: " + e.getMessage());
        }


        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "Bine ati venit pe IMDB, cea mai mare baza de date de filme si seriale din lume!" + ColorCodes.ANSI_RESET + "\n\n");
        System.out.println("Alegeti tipul de interfata dorit: \n\n");
        System.out.println("1. Interfata de terminal");
        System.out.println("2. Interfata grafica");

        int choice = 0;
        while (choice != 1 && choice != 2) {
            try {
                choice = scanner.nextInt();
                if (choice != 1 && choice != 2) {
                    System.out.println("Introduceti o optiune valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        if (choice == 1) {
            clearScreen();
            Thread.sleep(100);
            this.terminalInterface();
        } else {
            clearScreen();
            Thread.sleep(100);
            this.GUI();
        }
    }

    public void terminalInterface() throws InterruptedException {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~AUTENTIFICARE~~~    " + ColorCodes.ANSI_RESET + "\n\n");
        System.out.println("NUME DE UTILIZATOR: ");

        scanner.nextLine();

        String username = null;
        while (getUser(username) == null) {
            username = scanner.nextLine();
            if (getUser(username) == null) {
                System.out.println("Nume de utilizator invalid!");
            }
        }

        currentUser = getUser(username);

        System.out.println("PAROLA: ");

        String password = null;
        while (!currentUser.getInformation().getCredentials().getPassword().equals(password)) {
            password = scanner.nextLine();
            if (!currentUser.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Parola invalida!");
            }
        }

        clearScreen();
        Thread.sleep(100);

        System.out.println("V-ati autentificat cu succes!");
        Thread.sleep(1000);
        clearScreen();
        Thread.sleep(100);
        mainMenu();
    }

    private static String getMenuOptionText(int optionNumber) {
        switch (optionNumber) {
            case 1:
                return "Vizualizati detalii productii";
            case 2:
                return "Vizualizati detalii actori";
            case 3:
                return "Vizualizati notificarile primite";
            case 4:
                return "Cautati filme/seriale/actori";
            case 5:
                return "Modificati lista de favorite";
            case 6:
                return "Creati sau retrageti o cerere";
            case 7:
                return "Adaugati/stergeti productii sau actori din sistem";
            case 8:
                return "Vizualizati si rezolvati cereri";
            case 9:
                return "Actualizati informatiile despre productii/actori";
            case 10:
                return "Adaugati/stergeti o recenzie";
            case 11:
                return "Adaugati/stergeti un utilizator";
            case 12:
                return "Delogare";
            default:
                return "Unknown option";
        }
    }

    public void mainMenu() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~Welcome to the frontpage of IMDB!~~~   " + ColorCodes.ANSI_RESET + "\n\n");
        System.out.println(" You are logged in as: " + currentUser.getUsername());
        System.out.println(" XP: " + currentUser.getExperience() + "\n\n");

        String userType = currentUser.getUserType().toString();

        for (int i = 0; i < permissionsMatrix.length; i++) {
            if (permissionsMatrix[i][getUserTypeIndex(userType)] == 1) {
                System.out.println("    " + (i + 1) + ". " + getMenuOptionText(i + 1));
            } else {
                System.out.println(ColorCodes.ANSI_RED + "    " + (i + 1) + ". " + getMenuOptionText(i + 1) + ColorCodes.ANSI_RESET);
            }
        }

        int choice = 0;
        while (choice < 1 || choice > 12) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 12) {
                    System.out.println("Introduceti o optiune valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }
        if (permissionsMatrix[choice - 1][getUserTypeIndex(userType)] == 1) {
            try {
                clearScreen();
                Thread.sleep(100);
                switch (choice) {
                    case 1:
                        displayProductionInfo();
                        break;
                    case 2:
                        displayActorInfo();
                        break;
                    case 3:
                        displayNotifications();
                        break;
                    case 4:
                        search();
                        break;
                    case 5:
                        modifyFavorites();
                        break;
                    case 6:
                        createOrWithdrawRequest();
                        break;
                    case 7:
                        addOrRemoveProductionOrActor();
                        break;
                    case 8:
                        displayAndResolveRequests();
                        break;
                    case 9:
                        updateInfo();
                        break;
                    case 10:
                        addOrRemoveReview();
                        break;
                    case 11:
                        addOrRemoveUser();
                        break;
                    case 12:
                        logout();
                        break;
                    default:
                        System.out.println("Introduceti o optiune valida!");
                }
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
        } else {
            System.out.println("Nu aveti permisiunea de a accesa aceasta optiune!");
            try {
                Thread.sleep(3000);
                clearScreen();
                Thread.sleep(100);
                mainMenu();
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
        }
    }

    public void displayProductionInfo() throws InterruptedException {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~VIZUALIZARE DETALII PRODUCTII~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti o optiune: \n\n");
        System.out.println("    1. Afisati productiile nefiltrate");
        System.out.println("    2. Filtrati productiile dupa gen");
        System.out.println("    3. Filtrati productiile dupa numarul recenziilor");

        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        int choice = -1;
        while (choice < 0 || choice > 3) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        switch (choice) {
            case 0:
                mainMenu();
                return;
            case 1:
                displayProductionInfoNoFilter();
                break;
            case 2:
                filterByGenre();
                break;
            case 3:
                filterByNumberOfRatings();
                break;

        }
        return;
    }

    public void displayProductionInfoNoFilter() throws InterruptedException {
        int choice = -1;
        while (choice != 0) {
            System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~VIZUALIZARE DETALII PRODUCTII~~~   " + ColorCodes.ANSI_RESET + "\n\n");
            System.out.println("Alegeti o productie: \n\n");

            for (int i = 0; i < productionList.size(); i++) {
                if (productionList.get(i) instanceof Movie) {
                    System.out.println("    " + (i + 1) + ". " + productionList.get(i).getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
                } else {
                    System.out.println("    " + (i + 1) + ". " + productionList.get(i).getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
                }
            }
            System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

            choice = -1;
            while (choice < 0 || choice > productionList.size()) {
                try {
                    choice = scanner.nextInt();
                    if (choice < 0 || choice > productionList.size()) {
                        System.out.println("Introduceti o optiune valida!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduceti o optiune valida!");
                    scanner.nextLine();
                } finally {
                    scanner.nextLine();
                }
            }

            clearScreen();
            Thread.sleep(100);

            if (choice == 0) {
                mainMenu();
                return;
            } else {
                productionList.get(choice - 1).displayInfo();
                System.out.println("\n\n");
                System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
                scanner.nextLine();
                clearScreen();
                Thread.sleep(100);
            }
        }
    }

    public void filterByGenre() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~VIZUALIZARE DETALII PRODUCTII~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti un gen: \n\n");
        scanner.nextLine();
        String genre;
        while (true) {
            genre = scanner.nextLine();
            try {
                Genre.valueOf(genre);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Introduceti un gen valid!");
            }
        }

        System.out.println("Productiile de tipul " + genre + " sunt: \n\n");

        int count = 0;
        List<Production> filteredList = new ArrayList<Production>();
        for (Production production : productionList) {
            if (production.getGenres().contains(Genre.valueOf(genre))) {
                filteredList.add(production);
                count++;

                if (production instanceof Movie) {
                    System.out.println("    " + count + ". " + production.getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
                } else {
                    System.out.println("    " + count + ". " + production.getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
                }
            }
        }

        if (count == 0) {
            System.out.println("    Nu exista productii de tipul " + genre + "!");
            System.out.println("\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            filterByGenre();
            return;
        }

        int choice = -1;
        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        choice = -1;
        while (choice < 0 || choice > count) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > count) {
                    System.out.println("Introduceti o optiune valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        if (choice == 0) {
            mainMenu();
            return;
        } else {
            filteredList.get(choice - 1).displayInfo();
            System.out.println("\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            filterByGenre();
        }
    }

    public void filterByNumberOfRatings() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~VIZUALIZARE DETALII PRODUCTII~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti un numar: \n\n");
        int number = -1;
        while (number < 0) {
            try {
                number = scanner.nextInt();
                if (number < 0) {
                    System.out.println("Introduceti un numar pozitiv!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti un numar pozitiv!");
                scanner.nextLine();
            }
        }

        System.out.println("Productiile cu mai mult de " + number + " recenzii sunt: \n\n");

        int count = 0;
        List<Production> filteredList = new ArrayList<Production>();
        for (Production production : productionList) {
            if (production.getRatings().size() >= number) {
                filteredList.add(production);
                count++;

                if (production instanceof Movie) {
                    System.out.println("    " + count + ". " + production.getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
                } else {
                    System.out.println("    " + count + ". " + production.getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
                }
            }
        }

        if (count == 0) {
            System.out.println("    Nu exista productii cu mai mult de " + number + " recenzii!");
            System.out.println("\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            filterByNumberOfRatings();
            return;
        }

        int choice = -1;
        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        while (choice < 0 || choice > count) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > count) {
                    System.out.println("Introduceti o optiune valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        if (choice == 0) {
            mainMenu();
            return;
        } else {
            filteredList.get(choice - 1).displayInfo();
            System.out.println("\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            filterByNumberOfRatings();
        }
    }

    public void displayActorInfo() throws InterruptedException {
        int choice = -1;
        while (choice != 0) {
            System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~VIZUALIZARE DETALII ACTORI~~~   " + ColorCodes.ANSI_RESET + "\n\n");

            System.out.println("Alegeti o optiune: \n\n");
            System.out.println("    1. Afisati actorii nesortati");
            System.out.println("    2. Sortati actorii dupa nume");
            System.out.println("    3. Reveniti la meniul principal");

            int choice2 = -1;
            while (choice2 < 1 || choice2 > 3) {
                try {
                    choice2 = scanner.nextInt();
                    if (choice2 < 1 || choice2 > 3) {
                        System.out.println("Introduceti o optiune valida!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduceti o optiune valida!");
                    scanner.nextLine();
                }
            }

            List<Actor> sortedActorList = new ArrayList<Actor>();

            switch (choice2) {
                case 1:
                    sortedActorList = actorList;
                    break;
                case 2:
                    sortedActorList = new ArrayList<>(actorList);
                    Collections.sort(sortedActorList, new Comparator<Actor>() {
                        @Override
                        public int compare(Actor o1, Actor o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    break;
                case 3:
                    clearScreen();
                    Thread.sleep(100);
                    mainMenu();
                    return;
            }

            System.out.println("Alegeti un actor: \n\n");

            for (int i = 0; i < sortedActorList.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + sortedActorList.get(i).getName());
            }
            System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

            choice = -1;
            while (choice < 0 || choice > sortedActorList.size()) {
                try {
                    choice = scanner.nextInt();
                    if (choice < 0 || choice > sortedActorList.size()) {
                        System.out.println("Introduceti o optiune valida!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduceti o optiune valida!");
                    scanner.nextLine();
                }
            }

            clearScreen();
            Thread.sleep(100);

            if (choice == 0) {
                mainMenu();
                return;
            } else {
                sortedActorList.get(choice - 1).displayInfo();
                System.out.println("\n\n");
                System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
                scanner.nextLine();
                scanner.nextLine();
                clearScreen();
                Thread.sleep(100);
            }
        }
    }

    public void displayNotifications() {

        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~NOTIFICARI~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        if (currentUser.getNotifications().isEmpty()) {
            System.out.println("Nu aveti notificari!");
        } else {

            System.out.println("DING DONG! Aveti " + currentUser.getNotifications().size() + " notificari: \n\n");

            for (int i = 0; i < currentUser.getNotifications().size(); i++) {
                System.out.println("    " + (i + 1) + ". " + currentUser.getNotifications().get(i));
            }
        }
        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Apasati ENTER pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        mainMenu();
    }

    public void search() {
        while (true) {
            System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~CAUTARE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

            System.out.println("Introduceti un cuvant cheie (sau scrieti /done pentru a va intoarce la meniul principal): ");

            scanner.nextLine();

            String keyword = scanner.nextLine();
            if (keyword.equals("/done")) {
                mainMenu();
                return;
            } else {

                System.out.println("Rezultatele cautarii pentru " + keyword + ": \n\n");

                int count = 0;
                ArrayList<Object> searchResults = new ArrayList<Object>();

                for (Production production : productionList) {
                    if (production.getTitle().contains(keyword)) {
                        if (production instanceof Movie) {
                            System.out.println("    " + (count + 1) + ". " + production.getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
                        } else {
                            System.out.println("    " + (count + 1) + ". " + production.getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
                        }
                        searchResults.add(production);
                        count++;
                    }
                }

                for (Actor actor : actorList) {
                    if (actor.getName().contains(keyword)) {
                        System.out.println("    " + (count + 1) + ". " + actor.getName() + ColorCodes.ANSI_BLUE + " (Actor)" + ColorCodes.ANSI_RESET);
                        searchResults.add(actor);
                        count++;
                    }
                }

                if (count == 0) {
                    System.out.println("Nu s-au gasit rezultate!\n\n");
                    System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
                    scanner.nextLine();
                    clearScreen();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Error while clearing the screen: " + e.getMessage());
                    }
                    mainMenu();
                    return;
                } else {

                    System.out.println("\n\n Introduceti numarul corespunzator productiei/actorului pentru a vedea mai multe detalii (sau scrieti /done pentru a va intoarce la meniul principal): ");

                    String choice = "";

                    choice = scanner.nextLine();
                    if (choice.equals("/done")) {
                        clearScreen();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("Error while clearing the screen: " + e.getMessage());
                        }
                        mainMenu();
                        return;
                    } else {
                        int choiceInt = -1;
                        while (choiceInt == -1) {
                            try {
                                choiceInt = 0;
                                choiceInt = Integer.parseInt(choice);
                            } catch (NumberFormatException e) {
                                System.out.println("Introduceti o optiune valida!");
                                choiceInt = -1;
                                choice = scanner.nextLine();
                            }
                        }
                        if (choiceInt > 0 && choiceInt <= count) {
                            clearScreen();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                System.out.println("Error while clearing the screen: " + e.getMessage());
                            }
                            if (choiceInt <= searchResults.size())
                                ((Production) searchResults.get(choiceInt - 1)).displayInfo();

                            System.out.println("\n\n");
                            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
                            scanner.nextLine();
                            clearScreen();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                System.out.println("Error while clearing the screen: " + e.getMessage());
                            }
                            mainMenu();
                            return;
                        } else {
                            System.out.println("Introduceti o optiune valida!");
                            clearScreen();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                System.out.println("Error while clearing the screen: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    public void modifyFavorites() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~FAVORITE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        if (currentUser.getFavorites().isEmpty()) {
            System.out.println("Nu aveti niciun element in lista de favorite!\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        }

        System.out.println("Lista dumneaovoastra de favorite este: \n\n");
        int count = 0;

        for (ProductionsAndActors favorite : currentUser.getFavorites()) {
            if (favorite instanceof Movie) {
                System.out.println("    " + (count + 1) + ". " + ((Movie) favorite).getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
            } else if (favorite instanceof Series) {
                System.out.println("    " + (count + 1) + ". " + ((Series) favorite).getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
            } else {
                System.out.println("    " + (count + 1) + ". " + ((Actor) favorite).getName() + ColorCodes.ANSI_BLUE + " (Actor)" + ColorCodes.ANSI_RESET);
            }
            count++;
        }

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Adaugati un element in lista de favorite");
        System.out.println("    2. Stergeti un element din lista de favorite");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (choice < 1 || choice > 3) {
            choice = scanner.nextInt();
            if (choice < 1 || choice > 3) {
                System.out.println("Introduceti o optiune valida!");
            }
        }

        switch (choice) {
            case 1:
                addToFavorites();
                break;
            case 2:
                removeFromFavorites();
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }

    }

    public void addToFavorites() {
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE IN LISTA DE FAVORITE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        if (currentUser.getFavorites().size() == productionList.size() + actorList.size()) {
            System.out.println("Nu mai exista elemente de adaugat in lista de favorite!\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        }

        System.out.println("Alegeti un element pe care doriti sa il adaugati in lista de favorite: \n\n");

        int count = 0;
        List<ProductionsAndActors> elementsToAdd = new ArrayList<ProductionsAndActors>();

        for (Production production : productionList) {
            if (currentUser.getFavorites().contains(production))
                continue;
            if (production instanceof Movie) {
                System.out.println("    " + (count + 1) + ". " + production.getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
            } else {
                System.out.println("    " + (count + 1) + ". " + production.getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
            }
            elementsToAdd.add(production);
            count++;
        }

        for (Actor actor : actorList) {
            if (currentUser.getFavorites().contains(actor))
                continue;
            System.out.println("    " + (count + 1) + ". " + actor.getName() + ColorCodes.ANSI_BLUE + " (Actor)" + ColorCodes.ANSI_RESET);
            elementsToAdd.add(actor);
            count++;

        }

        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        int choice = -1;
        while (choice < 0 || choice > count) {
            choice = scanner.nextInt();
            if (choice < 0 || choice > count) {
                System.out.println("Introduceti o optiune valida!");
            }
        }

        if (choice == 0) {
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        } else {
            currentUser.addFavorite(elementsToAdd.get(choice - 1));
            System.out.println("Elementul a fost adaugat cu succes in lista de favorite!\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            modifyFavorites();
            return;
        }
    }

    public void removeFromFavorites() {
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~STERGERE DIN LISTA DE FAVORITE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        if (currentUser.getFavorites().isEmpty()) {
            System.out.println("Nu aveti niciun element in lista de favorite!\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        }

        System.out.println("Alegeti un element pe care doriti sa il stergeti din lista de favorite: \n\n");

        int count = 0;

        for (ProductionsAndActors favorite : currentUser.getFavorites()) {
            if (favorite instanceof Movie) {
                System.out.println("    " + (count + 1) + ". " + ((Movie) favorite).getTitle() + ColorCodes.ANSI_RED + " (Film)" + ColorCodes.ANSI_RESET);
            } else if (favorite instanceof Series) {
                System.out.println("    " + (count + 1) + ". " + ((Series) favorite).getTitle() + ColorCodes.ANSI_GREEN + " (Serial)" + ColorCodes.ANSI_RESET);
            } else {
                System.out.println("    " + (count + 1) + ". " + ((Actor) favorite).getName() + ColorCodes.ANSI_BLUE + " (Serial)" + ColorCodes.ANSI_RESET);
            }
            count++;
        }

        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        int choice = -1;
        while (choice < 0 || choice > count) {
            choice = scanner.nextInt();
            if (choice < 0 || choice > count) {
                System.out.println("Introduceti o optiune valida!");
            }
        }

        if (choice == 0) {
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        } else {
            currentUser.removeFavorite(currentUser.getFavorites().get(choice - 1));
            System.out.println("Elementul a fost sters cu succes din lista de favorite!\n\n");

            System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Apasati ENTER pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            modifyFavorites();
        }
    }

    public void createOrWithdrawRequest() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~CERERI~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        boolean currentUserHasRequest = requestList.stream()
                .anyMatch(request -> request.getUsername().equals(currentUser.getUsername()));


        if (currentUserHasRequest) {
            System.out.println("Lista cererilor pe care le-ati introdus in sistem: \n");
            int count = 0;
            for (Request request : requestList) {
                if (request.getUsername().equals(currentUser.getUsername())) {
                    System.out.println("    " + (count + 1) + ". " + request.getDescription());
                    count++;
                }
            }
        } else {
            System.out.println("Nu aveti nicio cerere introdusa in sistem!\n");
        }

        System.out.println("\nAlegeti o actiune: \n");
        System.out.println("    1. Creati o cerere");
        System.out.println("    2. Retrageti o cerere");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1:
                createRequest();
                break;
            case 2:
                withdrawRequest();
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
    }

    public void createRequest() {
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~CREARE CERERE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti tipul cererii: \n");
        System.out.println("    1. Stergere cont");
        System.out.println("    2. Modificare date productie");
        System.out.println("    3. Modificare date actor");
        System.out.println("    4. Diverse");
        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > 4) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        if (choice == 0) {
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        } else {
            RequestType type = null;
            switch (choice) {
                case 1:
                    type = RequestType.DELETE_ACCOUNT;
                    break;
                case 2:
                    type = RequestType.MOVIE_ISSUE;
                    break;
                case 3:
                    type = RequestType.ACTOR_ISSUE;
                    break;
                case 4:
                    type = RequestType.OTHERS;
                    break;
                default:
                    System.out.println("Introduceti o optiune valida!");
            }

            System.out.println("Introduceti descrierea cererii: \n");
            scanner.nextLine();
            String description = scanner.nextLine();

            String actorOrMovieString = null;
            ProductionsAndActors actorOrMovie = null;
            if (type.equals(RequestType.MOVIE_ISSUE) || type.equals(RequestType.ACTOR_ISSUE)) {
                if (type.equals(RequestType.MOVIE_ISSUE)) {
                    System.out.println("Introduceti titlul productiei: \n");
                } else {
                    System.out.println("Introduceti numele actorului: \n");
                }

                if (type.equals(RequestType.MOVIE_ISSUE)) {
                    while (true) {
                        actorOrMovieString = scanner.nextLine();
                        if (getProduction(actorOrMovieString) == null) {
                            System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
                        } else {
                            break;
                        }
                    }
                    actorOrMovie = getProduction(actorOrMovieString);
                } else {
                    while (true) {
                        actorOrMovieString = scanner.nextLine();
                        if (getActor(actorOrMovieString) == null) {
                            System.out.println("Nu exista niciun actor cu acest nume in sistem!");
                        }
                        else {
                            break;
                        }
                    }
                    actorOrMovie = getActor(actorOrMovieString);
                }

                for (User user : userList) {
                    if (user instanceof Admin || user instanceof Contributor) {
                        Staff staffUser = (Staff) user;
                        boolean containsActorOrMovie;

                        if (type.equals(RequestType.MOVIE_ISSUE)) {
                            if (staffUser.getAddedEntries().contains(getProduction(actorOrMovieString))) {
                                containsActorOrMovie = true;
                            } else {
                                containsActorOrMovie = false;
                            }
                        } else {
                            if (staffUser.getAddedEntries().contains(getActor(actorOrMovieString))) {
                                containsActorOrMovie = true;
                            } else {
                                containsActorOrMovie = false;
                            }
                        }

                        if (containsActorOrMovie) {
                            staffUser.addRequest(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), staffUser.getUsername()));
                            requestList.add(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), staffUser.getUsername()));
                            Observer.addObserver(staffUser);
                            Observer.notifyReceivedRequest(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), staffUser.getUsername()));
                            Observer.removeObserver(staffUser);
                            break;
                        }
                    }
                }
            } else {
                Request.RequestsHolder.addRequest(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), "ADMIN"));
                requestList.add(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), "ADMIN"));

                for (User admin : userList) {
                    if (admin instanceof Admin) {
                        Observer.addObserver((Admin) admin);
                        Observer.notifyReceivedRequest(new Request(type, LocalDateTime.now(), actorOrMovieString, description, currentUser.getUsername(), "ADMIN"));
                        Observer.removeObserver((Admin) admin);
                        break;
                    }
                }
            }

            System.out.println("Cererea a fost introdusa cu succes in sistem!\n\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            createOrWithdrawRequest();
        }
    }

    public void withdrawRequest() {
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~RETRAGERE CERERE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        boolean currentUserHasRequest = requestList.stream()
                .anyMatch(request -> request.getUsername().equals(currentUser.getUsername()));

        if (!currentUserHasRequest) {
            System.out.println("Nu aveti nicio cerere introdusa in sistem!\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            createOrWithdrawRequest();
            return;
        }

        System.out.println("Alegeti o cerere pe care doriti sa o retrageti: \n\n");

        int count = 0;

        for (Request request : requestList) {
            if (request.getUsername().equals(currentUser.getUsername())) {
                System.out.println("    " + (count + 1) + ". " + request.getDescription());
                count++;
            }
        }

        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Introduceti 0 pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > count) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }
        if (choice == 0) {
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        } else {
            Request requestToWithdraw = null;
            int count2 = 0;
            for (Request request : requestList) {
                if (request.getUsername().equals(currentUser.getUsername())) {
                    if (count2 == choice - 1) {
                        requestToWithdraw = request;
                        break;
                    }
                    count2++;
                }
            }

            requestList.remove(requestToWithdraw);
            System.out.println("Cererea a fost retrasa cu succes din sistem!\n\n");

            System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Apasati ENTER pentru a va intoarce la meniul principal..." + ColorCodes.ANSI_RESET + "\n");
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            createOrWithdrawRequest();
        }
    }

    public void addOrRemoveProductionOrActor() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE/STERGERE PRODUCTIE/ACTOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Adaugati o productie");
        System.out.println("    2. Stergeti o productie");
        System.out.println("    3. Adaugati un actor");
        System.out.println("    4. Stergeti un actor");
        System.out.println("    5. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 5) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        switch (choice) {
            case 1:
                addProductionToSystem();
                break;
            case 2:
                removeProductionFromSystem();
                break;
            case 3:
                addActorToSystem();
                break;
            case 4:
                removeActorFromSystem();
                break;
            case 5:
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
    }

    public void addProductionToSystem () {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE PRODUCTIE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti titlul productiei: \n");
        scanner.nextLine();
        String title = scanner.nextLine();

        while (getProduction(title) != null) {
            System.out.println("Exista deja o productie cu acest titlu in sistem!");
            title = scanner.nextLine();
        }

        System.out.println("Introduceti tipul productiei (Movie/Series): \n");
        String type = scanner.nextLine();

        while (!type.equals("Movie") && !type.equals("Series")) {
            System.out.println("Introduceti un tip valid (Movie/Series): \n");
            type = scanner.nextLine();
        }

        List<String> directorsList = new ArrayList<String>();
        String director;
        System.out.println("Introduceti regizorii (apasati ENTER pentru a trece mai departe): \n");
        do {
            director = scanner.nextLine();
            if (!director.equals("")) {
                directorsList.add(director);
            }
        } while (!director.equals(""));

        List<String> actorsListAddProd = new ArrayList<String>();
        String actorString;
        System.out.println("Introduceti actorii (apasati ENTER pentru a trece mai departe): \n");
        do {
            actorString = scanner.nextLine();
            if (!actorList.contains(getActor(actorString)) && !actorString.equals("")) {
                System.out.println("Nu exista niciun actor cu acest nume in sistem!");
                continue;
            }
            if (!actorString.equals("")) {
                actorsListAddProd.add(actorString);
                Actor actor = getActor(actorString);
                actor.addPerformance(new Role(title, type));
            }
        } while (!actorString.equals(""));

        List<Genre> genresList = new ArrayList<Genre>();
        String genre;
        System.out.println("Introduceti genurile productiei(apasati ENTER pentru a trece mai departe): \n");
        do {
            genre = scanner.nextLine();
            if (!genre.equals("")) {
                try {
                    Genre.valueOf(genre);
                } catch (IllegalArgumentException e) {
                    System.out.println("Introduceti un gen valid!");
                    continue;
                }
            }
            if (!genre.equals("")) {
                genresList.add(Genre.valueOf(genre));
            }
        } while (!genre.equals(""));

        List<Rating> ratingsList = new ArrayList<Rating>();

        System.out.println("Introduceti descrierea productiei: \n");
        String plot = scanner.nextLine();

        Double averageRating = 0d;

        System.out.println("Introduceti anul lansarii: \n");
        int releaseYear = 0;
        while (true) {
            try {
                releaseYear = scanner.nextInt();
                if (releaseYear < 1800 || releaseYear > 2024) {
                    System.out.println("Introduceti un an valid!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti un an valid!");
                scanner.nextLine();
            }
        }

        if (type.equals("Movie")) {
            System.out.println("Introduceti durata filmului: \n");
            int durationMins = 0;
            while (true) {
                try {
                    durationMins = scanner.nextInt();
                    if (durationMins <= 0) {
                        System.out.println("Introduceti un numar valid!");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduceti un numar valid!");
                    scanner.nextLine();
                }
            }
            String duration = durationMins + " minutes";

            Movie movie = new Movie(title, directorsList, actorsListAddProd, genresList, ratingsList, plot, averageRating, duration, releaseYear);
            productionList.add(movie);
        } else if (type.equals("Series")) {
            System.out.println("Introduceti numarul de sezoane: \n");
            int numSeasons = 0;
            while (true) {
                try {
                    numSeasons = scanner.nextInt();
                    if (numSeasons <= 0) {
                        System.out.println("Introduceti un numar valid!");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduceti un numar valid!");
                    scanner.nextLine();
                }
            }

            Map<String, List<Episode>> seasonsList = new HashMap<>();
            for (int i = 0; i < numSeasons; i++) {
                System.out.println("Introduceti numarul de episoade pentru sezonul " + (i + 1) + ": \n");
                int numEpisodes = 0;
                while (true) {
                    try {
                        numEpisodes = scanner.nextInt();
                        if (numEpisodes <= 0) {
                            System.out.println("Introduceti un numar valid!");
                        } else {
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Introduceti un numar valid!");
                        scanner.nextLine();
                    }
                }

                List<Episode> episodesList = new ArrayList<>();
                for (int j = 0; j < numEpisodes; j++) {
                    System.out.println("Introduceti numele episodului " + (j + 1) + ": \n");
                    scanner.nextLine();
                    String episodeName = scanner.nextLine();
                    System.out.println("Introduceti durata episodului " + (j + 1) + ": \n");
                    int durationMins = 0;
                    while (true) {
                        try {
                            durationMins = scanner.nextInt();
                            if (durationMins <= 0) {
                                System.out.println("Introduceti un numar valid!");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Introduceti un numar valid!");
                            scanner.nextLine();
                        }
                    }
                    String duration = durationMins + " minutes";
                    Episode episode = new Episode(episodeName, duration);
                    episodesList.add(episode);
                }
                seasonsList.put("Season " + (i + 1), episodesList);
            }

            Series series = new Series(title, directorsList, actorsListAddProd, genresList, ratingsList, plot, averageRating, releaseYear, numSeasons, seasonsList);
            productionList.add(series);
        }

        ExperienceStrategy strategy = new AddToSystemStrategy();
        currentUser.setExperience(currentUser.getExperience() + strategy.calculateExperience());

        System.out.println("Productia a fost adaugata cu succes in sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveProductionOrActor();
    }

    public void removeProductionFromSystem() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~STERGERE PRODUCTIE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti titlul productiei: \n");
        scanner.nextLine();
        String title = scanner.nextLine();

        while (getProduction(title) == null) {
            System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
            title = scanner.nextLine();
        }

        Production production = getProduction(title);

        for (Actor actor : actorList) {
            Iterator<Role> iterator = actor.getPerformances().iterator();
            while (iterator.hasNext()) {
                Role role = iterator.next();
                if (role.getTitle().equals(title)) {
                    iterator.remove();
                }
            }
        }

        productionList.remove(production);

        System.out.println("Productia a fost stearsa cu succes din sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveProductionOrActor();
    }

    public void addActorToSystem () {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE ACTOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti numele actorului: \n");
        scanner.nextLine();
        String name = scanner.nextLine();

        while (getActor(name) != null) {
            System.out.println("Exista deja un actor cu acest nume in sistem!");
            name = scanner.nextLine();
        }

        List<Role> performancesList = new ArrayList<Role>();
        String performance;
        System.out.println("Introduceti rolurile jucate de actor (apasati ENTER pentru a trece mai departe): \n");
        do {
            performance = scanner.nextLine();
            if (!performance.equals("")) {
                if (productionList.contains(getProduction(performance))) {
                    if (getProduction(performance).getActors().contains(name)) {
                        System.out.println("Actorul este deja in lista de actori a productiei!");
                        continue;
                    }
                    if (getProduction(performance) instanceof Movie) {
                        performancesList.add(new Role(performance, "Movie"));
                    } else {
                        performancesList.add(new Role(performance, "Series"));
                    }
                    getProduction(performance).addActor(name);
                } else {
                    System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
                }
            }
        } while (!performance.equals(""));

        System.out.println("Introduceti biografia actorului: \n");
        String biography = scanner.nextLine();

        Actor actor = new Actor(name, performancesList, biography);
        actorList.add(actor);

        ExperienceStrategy strategy = new AddToSystemStrategy();
        currentUser.setExperience(currentUser.getExperience() + strategy.calculateExperience());

        System.out.println("Actorul a fost adaugat cu succes in sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveProductionOrActor();
    }

    public void removeActorFromSystem() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~STERGERE ACTOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti numele actorului: \n");
        scanner.nextLine();
        String name = scanner.nextLine();

        while (getActor(name) == null) {
            System.out.println("Nu exista niciun actor cu acest nume in sistem!");
            name = scanner.nextLine();
        }

        Actor actor = getActor(name);

        for (Production production : productionList) {
            Iterator<String> iterator = production.getActors().iterator();
            while (iterator.hasNext()) {
                String actorName = iterator.next();
                if (actorName.equals(name)) {
                    iterator.remove();
                }
            }
        }

        actorList.remove(actor);

        System.out.println("Actorul a fost sters cu succes din sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveProductionOrActor();
    }

    public void displayAndResolveRequests() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~AFISARE SI REZOLVARE CERERI~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        List<Request> requestListOfUser;
        if (currentUser instanceof Contributor) {
            requestListOfUser = ((Contributor) currentUser).getRequestList();
        } else {
            requestListOfUser = Request.RequestsHolder.getRequestList();
        }

        if (requestListOfUser.isEmpty()) {
            System.out.println("Nu exista nicio cerere introdusa in sistem!\n");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
            return;
        }

        System.out.println("Lista cererilor introduse in sistem: \n");
        int count = 0;
        for (Request request : requestListOfUser) {
            System.out.println("    " + (count + 1) + ". " + request.getDescription());
            count++;
        }

        System.out.println("\nAlegeti o cerere pe care doriti sa o rezolvati sau sa o refuzati: \n\n");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > count) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        Request requestToResolve = null;
        int count2 = 0;
        for (Request request : requestListOfUser) {
            if (count2 == choice - 1) {
                requestToResolve = request;
                break;
            }
            count2++;
        }

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Rezolvati cererea");
        System.out.println("    2. Refuzati cererea");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice2 = -1;

        while (true) {
            try {
                choice2 = scanner.nextInt();
                if (choice2 < 1 || choice2 > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        switch (choice2) {
            case 1:
                Observer.addObserver(getUser(requestToResolve.getUsername()));
                Observer.notifyRequestSolved(requestToResolve);
                Observer.removeObserver(getUser(requestToResolve.getUsername()));

                requestListOfUser.remove(requestToResolve);

                ExperienceStrategy experienceStrategy = new RequestSolvedStrategy();
                getUser(requestToResolve.getUsername()).setExperience(getUser(requestToResolve.getUsername()).getExperience() + experienceStrategy.calculateExperience());

                System.out.println("Cererea a fost rezolvata cu succes!\n\n");
                break;
            case 2:
                Observer.addObserver(getUser(requestToResolve.getUsername()));
                Observer.notifyRequestDenied(requestToResolve);
                Observer.removeObserver(getUser(requestToResolve.getUsername()));

                requestListOfUser.remove(requestToResolve);

                System.out.println("Cererea a fost refuzata cu succes!\n\n");
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }

        System.out.println("\n" + ColorCodes.ANSI_CYAN + "    Apasati ENTER pentru a va intoarce la meniu..." + ColorCodes.ANSI_RESET + "\n");
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        mainMenu();
    }

    public void updateInfo() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~MODIFICARE INFORMATII~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Modificati informatii despre o productie");
        System.out.println("    2. Modificati informatii despre un actor");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        switch (choice) {
            case 1:
                updateProductionInfo();
                break;
            case 2:
                updateActorInfo();
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
    }

    private static String getUpdateMenuOptionText(int optionNumber) {
        switch (optionNumber) {
            case 1:
                return "Modificati titlul productiei";
            case 2:
                return "Modificati regizorii";
            case 3:
                return "Modificati actorii";
            case 4:
                return "Modificati genurile";
            case 5:
                return "Modificati descrierea";
            case 6:
                return "Modificati durata filmului";
            case 7:
                return "Modificati anul lansarii";
            case 8:
                return "Modificati lista de episoade";
            default:
                return "Unknown option";
        }
    }

    public void updateProductionInfo() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~MODIFICARE INFORMATII PRODUCTIE~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti titlul productiei: \n");
        scanner.nextLine();
        String title = scanner.nextLine();

        while (getProduction(title) == null) {
            System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
            title = scanner.nextLine();
        }

        Production production = getProduction(title);
        int productionTypeIndex = (production instanceof Movie) ? 0 : 1;

        System.out.println("Alegeti o actiune: \n");
        for (int i = 0; i < productionsInfoMatrix.length; i++) {
            if (productionsInfoMatrix[i][productionTypeIndex] == 1) {
                System.out.println("    " + (i + 1) + ". " + getUpdateMenuOptionText(i + 1));
            } else {
                System.out.println(ColorCodes.ANSI_RED + "    " + (i + 1) + ". " + getUpdateMenuOptionText(i + 1) + ColorCodes.ANSI_RESET);
            }
        }
        System.out.println("    9. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 9 || productionsInfoMatrix[choice - 1][productionTypeIndex] == 0) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1: {
                System.out.println("Introduceti noul titlu al productiei: \n");
                scanner.nextLine();
                String newTitle = scanner.nextLine();

                while (getProduction(newTitle) != null) {
                    System.out.println("Exista deja o productie cu acest titlu in sistem!");
                    newTitle = scanner.nextLine();
                }

                for (Actor actor : actorList) {
                    Iterator<Role> iterator = actor.getPerformances().iterator();
                    while (iterator.hasNext()) {
                        Role role = iterator.next();
                        if (role.getTitle().equals(title)) {
                            role.setTitle(newTitle);
                        }
                    }
                }

                for (Request request : requestList) {
                    if (request.getType().equals(RequestType.MOVIE_ISSUE) && request.getActorOrMovieName().equals(title)) {
                        request.setActorOrMovieName(newTitle);
                    }
                }

                production.setTitle(newTitle);
                break;
            }
            case 2: {
                production.getDirectors().clear();
                System.out.println("Introduceti regizorii (apasati ENTER pentru a trece mai departe): \n");
                scanner.nextLine();
                String director;
                do {
                    director = scanner.nextLine();
                    if (!director.equals("")) {
                        production.addDirector(director);
                    }
                } while (!director.equals(""));
                break;
            }
            case 3: {
                production.getActors().clear();
                System.out.println("Introduceti actorii (apasati ENTER pentru a trece mai departe): \n");
                String actorString;
                scanner.nextLine();
                do {
                    actorString = scanner.nextLine();
                    if (!actorList.contains(getActor(actorString)) && !actorString.equals("")) {
                        System.out.println("Nu exista niciun actor cu acest nume in sistem!");
                        continue;
                    }
                    if (!actorString.equals("")) {
                        production.addActor(actorString);
                        Actor actor = getActor(actorString);
                        boolean roleExists = false;
                        for (Role role : actor.getPerformances()) {
                            if (role.getTitle().equals(title)) {
                                roleExists = true;
                                break;
                            }
                        }

                        if (!roleExists) {
                            if (production instanceof Movie) {
                                actor.addPerformance(new Role(title, "Movie"));
                            } else {
                                actor.addPerformance(new Role(title, "Series"));
                            }
                        }
                    }
                }
                while (!actorString.equals(""));

                for (Actor actor : actorList) {
                    String finalTitle = title;
                    Role roleToRemove = actor.getPerformances().stream()
                            .filter(role -> role.getTitle().equals(finalTitle))
                            .findFirst()
                            .orElse(null);
                    if (roleToRemove != null && !production.getActors().contains(actor.getName())) {
                        actor.getPerformances().remove(roleToRemove);
                    }
                }




                break;
            }
            case 4: {
                System.out.println("Introduceti genurile productiei(apasati ENTER pentru a trece mai departe): \n");
                production.getGenres().clear();
                String genre;
                scanner.nextLine();
                do {
                    genre = scanner.nextLine();
                    if (!genre.equals("")) {
                        try {
                            Genre.valueOf(genre);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Introduceti un gen valid!");
                            continue;
                        }
                    }
                    if (!genre.equals("")) {
                        production.addGenre(Genre.valueOf(genre));
                    }
                } while (!genre.equals(""));
                break;
            }
            case 5: {
                System.out.println("Introduceti descrierea productiei: \n");
                scanner.nextLine();
                String plot = scanner.nextLine();
                production.setPlot(plot);
                break;
            }
            case 6: {
                System.out.println("Introduceti anul lansarii: \n");
                int releaseYear = 0;
                while (true) {
                    try {
                        releaseYear = scanner.nextInt();
                        if (releaseYear < 1800 || releaseYear > 2024) {
                            System.out.println("Introduceti un an valid!");
                        } else {
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Introduceti un an valid!");
                        scanner.nextLine();
                    }
                }
                if (production instanceof Movie) {
                    ((Movie) production).setReleaseYear(releaseYear);
                } else {
                    ((Series) production).setReleaseYear(releaseYear);
                }
                break;
            }
            case 7: {
                if (production instanceof Movie) {
                    System.out.println("Introduceti durata filmului: \n");
                    int durationMins = 0;
                    while (true) {
                        try {
                            durationMins = scanner.nextInt();
                            if (durationMins <= 0) {
                                System.out.println("Introduceti un numar valid!");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Introduceti un numar valid!");
                            scanner.nextLine();
                        }
                    }
                    String duration = durationMins + " minutes";
                    ((Movie) production).setDuration(duration);
                }
            }
            case 8: {
                if (production instanceof Series) {
                    System.out.println("Introduceti numarul de sezoane: \n");
                    int numSeasons = 0;
                    while (true) {
                        try {
                            numSeasons = scanner.nextInt();
                            if (numSeasons <= 0) {
                                System.out.println("Introduceti un numar valid!");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Introduceti un numar valid!");
                            scanner.nextLine();
                        }
                    }

                    Map<String, List<Episode>> seasonsList = new HashMap<>();
                    for (int i = 0; i < numSeasons; i++) {
                        System.out.println("Introduceti numarul de episoade pentru sezonul " + (i + 1) + ": \n");
                        int numEpisodes = 0;
                        while (true) {
                            try {
                                numEpisodes = scanner.nextInt();
                                if (numEpisodes <= 0) {
                                    System.out.println("Introduceti un numar valid!");
                                } else {
                                    break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Introduceti un numar valid!");
                                scanner.nextLine();
                            }
                        }

                        List<Episode> episodesList = new ArrayList<>();
                        for (int j = 0; j < numEpisodes; j++) {
                            System.out.println("Introduceti numele episodului " + (j + 1) + ": \n");
                            scanner.nextLine();
                            String episodeName = scanner.nextLine();
                            System.out.println("Introduceti durata episodului " + (j + 1) + ": \n");
                            int durationMins = 0;
                            while (true) {
                                try {
                                    durationMins = scanner.nextInt();
                                    if (durationMins <= 0) {
                                        System.out.println("Introduceti un numar valid!");
                                    } else {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Introduceti un numar valid!");
                                    scanner.nextLine();
                                }
                            }
                            String duration = durationMins + " minutes";
                            Episode episode = new Episode(episodeName, duration);
                            episodesList.add(episode);
                        }
                        seasonsList.put("Season " + (i + 1), episodesList);
                    }
                        ((Series) production).setSeasons(seasonsList);
                }
                break;
            }
            case 9:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
        System.out.println("Informatiile au fost modificate cu succes!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        updateInfo();
    }

    public void updateActorInfo() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~MODIFICARE INFORMATII ACTOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti numele actorului: \n");
        scanner.nextLine();
        String name = scanner.nextLine();

        while (getActor(name) == null) {
            System.out.println("Nu exista niciun actor cu acest nume in sistem!");
            name = scanner.nextLine();
        }

        Actor actor = getActor(name);

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Modificati numele actorului");
        System.out.println("    2. Modificati biografia actorului");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1: {
                System.out.println("Introduceti noul nume al actorului: \n");
                scanner.nextLine();
                String newName = scanner.nextLine();

                while (getActor(newName) != null) {
                    System.out.println("Exista deja un actor cu acest nume in sistem!");
                    newName = scanner.nextLine();
                }

                for (Production production : productionList) {
                    if (production.getActors().contains(name)) {
                        production.getActors().remove(name);
                        production.addActor(newName);
                    }
                }

                for (Request request : requestList) {
                    if (request.getType().equals(RequestType.ACTOR_ISSUE) && request.getActorOrMovieName().equals(name)) {
                        request.setActorOrMovieName(newName);
                    }
                }

                actor.setName(newName);
                break;
            }
            case 2: {
                System.out.println("Introduceti noua biografie a actorului: \n");
                scanner.nextLine();
                String newBiography = scanner.nextLine();
                actor.setBiography(newBiography);
                break;
            }
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }

        System.out.println("Informatiile au fost modificate cu succes!\n\n");

        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        updateInfo();
    }

    public void addOrRemoveReview() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE/STERGERE REVIEW~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Alegeti o actiune: \n");
        System.out.println("    1. Adaugati un review");
        System.out.println("    2. Stergeti un review");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        switch (choice) {
            case 1:
                addReview();
                break;
            case 2:
                removeReview();
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
    }

    public void addReview() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE REVIEW~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti titlul productiei: \n");
        scanner.nextLine();
        String title = scanner.nextLine();

        while (getProduction(title) == null) {
            System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
            title = scanner.nextLine();
        }

        for (Rating rating : getProduction(title).getRatings()) {
            if (rating.getUsername().equals(currentUser.getUsername())) {
                System.out.println("Ati adaugat deja un review pentru aceasta productie!");
                System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
                scanner.nextLine();
                scanner.nextLine();
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                addOrRemoveReview();
            }
        }

        Production production = getProduction(title);

        System.out.println("Introduceti nota pe care vreti sa o acordati productiei (1-10): \n");
        int grade = 0;
        while (true) {
            try {
                grade = scanner.nextInt();
                if (grade < 1 || grade > 10) {
                    System.out.println("Introduceti o nota valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o nota valida!");
                scanner.nextLine();
            }
        }

        System.out.println("Introduceti textul recenziei: \n");
        scanner.nextLine();
        String review = scanner.nextLine();

        Rating reviewToAdd = new Rating( currentUser.getUsername(), grade, review);
        production.addRating(reviewToAdd);

        ExperienceStrategy strategy = new RatingAddedStrategy();
        currentUser.setExperience(currentUser.getExperience() + strategy.calculateExperience());

        for (Rating rating : production.getRatings()) {
            if (!rating.getUsername().equals(currentUser.getUsername())) {
                Observer.addObserver(getUser(rating.getUsername()));
                Observer.notifyAddedProductionReview(production);
                Observer.removeObserver(getUser(rating.getUsername()));
            }
        }

        List<Integer> grades = new ArrayList<>();
        for (Rating rating : production.getRatings()) {
            grades.add(rating.getRating());
        }
        production.setAverageRating(grades.stream().mapToInt(Integer::intValue).average().orElse(0));

        System.out.println("Review-ul a fost adaugat cu succes!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveReview();
    }

    public void removeReview() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~STERGERE REVIEW~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti titlul productiei: \n");
        scanner.nextLine();
        String title = scanner.nextLine();

        while (getProduction(title) == null) {
            System.out.println("Nu exista nicio productie cu acest titlu in sistem!");
            title = scanner.nextLine();
        }

        Production production = getProduction(title);

        boolean reviewExists = false;
        for (Rating rating : production.getRatings()) {
            if (rating.getUsername().equals(currentUser.getUsername())) {
                reviewExists = true;
                break;
            }
        }

        if (!reviewExists) {
            System.out.println("Utilizatorul nu a adaugat niciun review pentru aceasta productie!");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            addOrRemoveReview();
        }

        for (Rating rating : production.getRatings()) {
            if (rating.getUsername().equals(currentUser.getUsername())) {
                production.getRatings().remove(rating);
                break;
            }
        }

        ExperienceStrategy strategy = new RatingRemovedStrategy();
        currentUser.setExperience(currentUser.getExperience() + strategy.calculateExperience());

        List<Integer> grades = new ArrayList<>();
        for (Rating rating : production.getRatings()) {
            grades.add(rating.getRating());
        }
        production.setAverageRating(grades.stream().mapToInt(Integer::intValue).average().orElse(0));

        System.out.println("Review-ul a fost sters cu succes!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveReview();
    }

    public void addOrRemoveUser() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE/STERGERE UTILIZATOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Lista cu utilizatorii din sistem: \n");
        int count = 0;
        for (User user : userList) {
            if (user instanceof Regular) {
                System.out.println("    " + (count + 1) + ". " + user.getUsername() + ColorCodes.ANSI_PURPLE + " (Regular)" + ColorCodes.ANSI_RESET);
            } else if (user instanceof Contributor) {
                System.out.println("    " + (count + 1) + ". " + user.getUsername() + ColorCodes.ANSI_RED + " (Contributor)" + ColorCodes.ANSI_RESET);
            } else {
                System.out.println("    " + (count + 1) + ". " + user.getUsername() + ColorCodes.ANSI_GREEN + " (Admin)" + ColorCodes.ANSI_RESET);
            }
            count++;
        }

        System.out.println("\nAlegeti o actiune: \n");
        System.out.println("    1. Adaugati un utilizator");
        System.out.println("    2. Stergeti un utilizator");
        System.out.println("    3. Intoarceti-va la meniul principal");

        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Introduceti o optiune valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o optiune valida!");
                scanner.nextLine();
            }
        }

        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }

        switch (choice) {
            case 1:
                addUserToSystem();
                break;
            case 2:
                removeUserFromSystem();
                break;
            case 3:
                clearScreen();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error while clearing the screen: " + e.getMessage());
                }
                mainMenu();
                return;
            default:
                System.out.println("Introduceti o optiune valida!");
        }
    }

    public void addUserToSystem() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~ADAUGARE UTILIZATOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Introduceti adresa de e-mail: \n");
        String email;
        scanner.nextLine();

        while (true) {
            email = scanner.nextLine();
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Introduceti o adresa de e-mail valida!");
                continue;
            }
            boolean emailExists = false;
            for (User user : userList) {
                if (user.getInformation().getCredentials().getEmail().equals(email)) {
                    emailExists = true;
                    break;
                }
            }
            if (emailExists) {
                System.out.println("Exista deja un utilizator cu aceasta adresa de e-mail in sistem!");
                continue;
            }
            break;
        }

        System.out.println("Introduceti numele real al utilizatorului: \n");
        String name = scanner.nextLine();

        while (!name.matches("^[a-zA-Z\\s]+$") || !name.contains(" ")) {
            System.out.println("Introduceti un nume valid!");
            name = scanner.nextLine();
        }

        String username;
        while (true) {
            Random random = new Random();
            int randomNumber = random.nextInt(9900) + 100;
            username = name.split(" ")[0].toLowerCase() + "_" + name.split(" ")[1].toLowerCase() + "_" + randomNumber;

            if (getUser(username) != null) {
                continue;
            }

            break;
        }
        System.out.println("A fost generat un nume de utilizator unic: " + username);

        String password;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        Random random = new Random();
        StringBuilder passwordStringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            passwordStringBuilder.append(characters.charAt(index));
        }
        password = passwordStringBuilder.toString();
        System.out.println("A fost generata o parola puternica: " + password);

        Credentials credentials = new Credentials(email, password);

        System.out.println("Introduceti tara: \n");
        String country;
        while (true) {
            country = scanner.nextLine();
            try {
                Countries.valueOf(country);
            } catch (IllegalArgumentException e) {
                System.out.println("Introduceti o tara valida!");
                continue;
            }
            break;
        }

        System.out.println("Introduceti varsta (minim 13 ani): \n");
        int age = 0;
        while (true) {
            try {
                age = scanner.nextInt();
                if (age < 13 || age > 120) {
                    System.out.println("Introduceti o varsta valida!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti o varsta valida!");
                scanner.nextLine();
            }
        }

        System.out.println("Introduceti genul (F/M/N): ");
        scanner.nextLine();
        String gender = scanner.nextLine();

        while (!gender.equals("M") && !gender.equals("F") && !gender.equals("N")) {
            System.out.println("Introduceti M, F sau N!");
            gender = scanner.nextLine();
        }

        System.out.println("Introduceti data nasterii (YYYY-MM-DD): \n");
        String birthDate = scanner.nextLine();

        while (!birthDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
            System.out.println("Introduceti o data valida!");
            birthDate = scanner.nextLine();
        }

        User.Information information = new User.Information.Builder()
                .credentials(credentials)
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate)
                .build();

        System.out.println("Introduceti tipul utilizatorului (Regular/Contributor/Admin):\n");
        String userTypeString = scanner.nextLine();

        while (!userTypeString.equals("Regular") && !userTypeString.equals("Contributor") && !userTypeString.equals("Admin")) {
            System.out.println("Introduceti un tip valid! (Regular/Contributor/Admin)");
            userTypeString = scanner.nextLine();
        }
        AccountType userType = AccountType.valueOf(userTypeString);

        List<String> notifications = new ArrayList<>();

        List<ProductionsAndActors> favorites = new ArrayList<>();

        List<Request> requestListForAddingUser = new ArrayList<>();

        List<ProductionsAndActors> addedEntries = new ArrayList<>();

        User newUser = UserFactory.createUser(userType, information, username, 0, notifications, favorites, requestListForAddingUser, addedEntries);
        userList.add(newUser);

        System.out.println("Utilizatorul a fost adaugat cu succes in sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveUser();
    }

    public void removeUserFromSystem() {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~STERGERE UTILIZATOR~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("\nIntroduceti numele utilizatorului pe care doriti sa il stergeti: \n");
        scanner.nextLine();
        String username = scanner.nextLine();

        while (getUser(username) == null) {
            System.out.println("Nu exista niciun utilizator cu acest nume in sistem!");
            username = scanner.nextLine();
        }

        User userToRemove = getUser(username);

        if (userToRemove instanceof Admin) {
            System.out.println("Nu puteti sterge un admin din sistem!");
            System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
            scanner.nextLine();
            scanner.nextLine();
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            addOrRemoveUser();
        }

        for (Request request : requestList) {
            if (request.getUsername().equals(username)) {
                requestList.remove(request);
            }
        }
        for (Request request : Request.RequestsHolder.getRequestList()) {
            if (request.getUsername().equals(username)) {
                Request.RequestsHolder.getRequestList().remove(request);
            }
        }
        for (User user : userList) {
            if (user instanceof Staff) {
                for (Request request : ((Staff) user).getRequestList()) {
                    if (request.getUsername().equals(username)) {
                        ((Staff) user).getRequestList().remove(request);
                    }
                }
            }
        }

        for (Production production : productionList) {
            for (Rating rating : production.getRatings()) {
                if (rating.getUsername().equals(username)) {
                    production.getRatings().remove(rating);
                }
            }
            List<Integer> grades = new ArrayList<>();
            for (Rating rating : production.getRatings()) {
                grades.add(rating.getRating());
            }
            production.setAverageRating(grades.stream().mapToInt(Integer::intValue).average().orElse(0));
        }

        userList.remove(userToRemove);

        System.out.println("Utilizatorul a fost sters cu succes din sistem!\n\n");
        System.out.println(ColorCodes.ANSI_CYAN + "Apasati ENTER pentru a va intoarce..." + ColorCodes.ANSI_RESET);
        scanner.nextLine();
        scanner.nextLine();
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        addOrRemoveUser();
    }

    public void logout() throws InterruptedException {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~LOGOUT~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println("Sunteti sigur ca doriti sa va delogati? (Y/N)\n");

        String choice = scanner.next();

        if (choice.equals("Y")) {
            currentUser = null;
            System.out.println("V-ati delogat cu succes!\n\n");
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            terminalInterface();
        } else if (choice.equals("N")) {
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            mainMenu();
        } else {
            System.out.println("Introduceti o optiune valida!");
            Thread.sleep(1000);
            clearScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error while clearing the screen: " + e.getMessage());
            }
            logout();
        }
    }

    public void GUI() throws InterruptedException {
        System.out.println(ColorCodes.ANSI_YELLOW_BACKGROUND + ColorCodes.ANSI_BLACK + "    ~~~GUI~~~   " + ColorCodes.ANSI_RESET + "\n\n");

        System.out.println(ColorCodes.ANSI_RED_BACKGROUND + ":((( Ne pare rau dar aceasta functionalitate nu este inca disponibila! :(((" + ColorCodes.ANSI_RESET + "\n\n");
        System.out.println(ColorCodes.ANSI_RED_BACKGROUND + "Veti fi redirectionat catre interfata de terminal in 3 secunde..." + ColorCodes.ANSI_RESET + "\n\n");
        Thread.sleep(3000);
        clearScreen();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
        terminalInterface();
    }

    public static void main(String[] args) throws InterruptedException {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }
}