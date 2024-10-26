public interface RequestObserver {
    void updateRequestSolvedNotification(Request request);
    void updateRequestDeniedNotification(Request request);
    void updateProductionGotReviewedNotification(Production production, String reviewerUsername);
    void updateReceivedRequestNotification(Request request);
    void updateAddedProductionReviewNotification(Production production);
}
