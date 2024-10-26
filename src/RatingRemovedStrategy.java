public class RatingRemovedStrategy implements ExperienceStrategy{
    @Override
    public int calculateExperience() {
        return -5;
    }
}
