package cartera.icaninter.net.cartera;

/**
 * Created by Juzer on 2/26/2016.
 */
public class Utils {

    private static final String LOG_TAG = Utils.makeLogTag(Utils.class);

    public static String makeLogTag(Class c){
        return "RecSports: " + c.getSimpleName();
    }
}
