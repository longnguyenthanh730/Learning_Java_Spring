import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static HttpURLConnection connection;
    public static int count =0;
    public static void Ex1(){
        //Method 1: java.net.HttpURLConnection
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/albums");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if(status >299){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine())!= null){
                    responseContent .append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine())!= null){
                    responseContent .append(line);
                    if(line.contains("userId")) count++;
                }
                reader.close();
            }
            System.out.println("Total Album: " +count);
            //  System.out.println(responseContent);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }
    }
    public static void Ex2(){
        //Method 2: java.lang.http.HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://jsonplaceholder.typicode.com/albums")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(Main::parse)
                .join();
    }
    public static String parse(String responseBody){
        JSONArray albums = new JSONArray(responseBody);
        System.out.print("List Id: ");
        for(int i=0; i< albums.length(); i++){
            JSONObject album = albums.getJSONObject(i);
            int id = album.getInt("id");
            int userId = album.getInt("userId");
            String title = album.getString("title");
            System.out.print(id + " ");
        }
        return null;
    }
    public static void main(String[] args) {
        Ex1();
        Ex2();
    }
}