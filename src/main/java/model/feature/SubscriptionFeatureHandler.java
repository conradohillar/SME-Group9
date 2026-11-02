package model.feature;

import database.Database;
import model.subscription.SubscriptionTier;
import model.user.Student;

public class SubscriptionFeatureHandler implements FeatureHandler {

    Database database = Database.getInstance();

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
        for (Student student : database.getStudents()) {
            student.changeSubscription(SubscriptionTier.PREMIUM);
        }
    }
}
