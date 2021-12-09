import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that provides static methods to send HTTP GET and POST requests.
 * Used to test server
 */
public class Client {

    /**
     * Execute an HTTP GET for the specified URL.
     * Allows request headers to be set.
     *
     * @param url
     * @param headers
     * @return response's status code
     */
    public static int doGet(String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            HttpRequest request = builder.GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e);
            return 0;
        }
    }

    /**
     * Execute an HTTP GET for the specified URL.
     * Allows request headers to be set.
     *
     * @param url
     * @param headers
     * @return response's body
     */
    public static String doGetBody(String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            HttpRequest request = builder.GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Execute an HTTP POST for the specified URL
     * Headers for the request are provided in the map headers.
     * The body of the request is provided as a String.
     *
     * @param url
     * @param headers
     * @param body
     * @return response's status code
     */
    public static int doPost(String url, Map<String, String> headers, String body) {

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            HttpRequest request = builder.POST((HttpRequest.BodyPublishers.ofString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response.statusCode();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return 0;
        }

    }

    /**
     * Helper method to set the headers of any HttpRequest.Builder.
     *
     * @param builder
     * @param headers
     * @return
     */
    private static HttpRequest.Builder setHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder = builder.setHeader(key, headers.get(key));
            }
        }
        return builder;
    }

}
