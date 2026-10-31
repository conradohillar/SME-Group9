package model.feature;

import database.Database;
import model.subscription.SubscriptionTier;
import model.user.Student;

public class SubscriptionFeatureHandler implements FeatureHandler {
    @Override
    public void activate() {
        // TODO: view must show subscription type line and upgrade/downgrade button
    }

    @Override
    public void deactivate() {
        for (Student student : Database.getInstance().getStudents()) {
            student.changeSubscription(SubscriptionTier.PREMIUM);
            // TODO: ver si tenemos tiempo de que subscription sea una unica clase y ya, con el tier (las clases concretas no harian falta)
        }
    }
}
