package ca.xef6.app;

import com.facebook.Session;

public class Application extends android.app.Application {

    public void logOut(boolean clearTokenInformation) {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            if (clearTokenInformation) {
                session.closeAndClearTokenInformation();
            } else {
                session.close();
            }
        }
    }

}
