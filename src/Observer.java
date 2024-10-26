import java.util.ArrayList;
import java.util.List;

public class Observer {
    private static List<RequestObserver> observers = new ArrayList<>();

    // Metoda pentru a adăuga observatori
    public static void addObserver(RequestObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(RequestObserver observer) {
        observers.remove(observer);
    }

    // Metoda pentru a notifica observatorii că o solicitare a fost rezolvată
    public static void notifyRequestSolved(Request request) {
        for (RequestObserver observer : observers) {
            observer.updateRequestSolvedNotification(request);
        }
    }

    // Metoda pentru a notifica observatorii că o solicitare a fost respinsă
    public static void notifyRequestDenied(Request request) {
        for (RequestObserver observer : observers) {
            observer.updateRequestDeniedNotification(request);
        }
    }

    // Metoda pentru a notifica observatorii că o producție la care au dat rating a fost rated
    public static void notifyProductionGotReviewed(Production production, String reviewerUsername) {
        for (RequestObserver observer : observers) {
            observer.updateProductionGotReviewedNotification(production, reviewerUsername);
        }
    }

    // Metoda pentru a notifica observatorii că o solicitare a fost primită
    public static void notifyReceivedRequest(Request request) {
        for (RequestObserver observer : observers) {
            observer.updateReceivedRequestNotification(request);
        }
    }

    // Metoda pentru a notifica observatorii că o revizuire a fost adăugată unei producții
    public static void notifyAddedProductionReview(Production production) {
        for (RequestObserver observer : observers) {
            observer.updateAddedProductionReviewNotification(production);
        }
    }
}
