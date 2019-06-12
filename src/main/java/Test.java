import java.io.IOException;
import java.io.OutputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static HashMap<String, Method> routes =  new HashMap<String, Method>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//        server.createContext("/test", new MyHandler());
//        server.createContext("/test2", new MyHandler2());
        createContex(server);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static void createContex(HttpServer server){
        for(Method m: Routes.class.getMethods()) {
            if(m.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = m.getAnnotation(WebRoute.class);
                WebRoute webAnnotation = (WebRoute)annotation;
                System.out.println("Ann: " + ((WebRoute) annotation).toString());
                String path = ((WebRoute) annotation).value();
                routes.put(path, m);
                server.createContext(path, new MyHandler());
            }
        }

    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println("Path: " + t.getHttpContext().getPath());
            Method methodToExecute = routes.get(t.getHttpContext().getPath());

            String response = null;
            try {
                response = (String)methodToExecute.invoke(new Routes());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }





}