package in.tablese.tablese_core.constants;

public class WebConfigConstants {

    public static final String LOCALHOST_3000 = "http://localhost:3000";
    public static final String LOCALHOST_8080 = "http://localhost:8080";
    public static final String LOCALHOST_5173 = "http://localhost:5173";
    public static final String PRODUCTION_URL = "https://www.tablese.in";
    public static final String ALL = "*";
    public static final String[] ALLOWED_ORIGINS = {LOCALHOST_3000, LOCALHOST_8080, PRODUCTION_URL, LOCALHOST_5173, ALL};

//    Websocket URL
    public static final String WEBSOCKET_URL = "ws://localhost:8080/ws/orders";
    public static final String WEBSOCKET_PATH = "/ws";
}
