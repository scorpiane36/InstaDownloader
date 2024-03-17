package org.downloader.instadownloader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadController {
    @FXML
    public TextField usernameField;
    public Button downloadButton;
    public ProgressBar loadingBar;
    public Label finalText;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @FXML
    protected void onDownloadClick() {
        String url = usernameField.getText();
        System.out.println("Downloading from URL: " + url);

        OkHttpClient client = new OkHttpClient();
        Request request = (new Request.Builder())
                .url("https://instagram-downloader-download-instagram-videos-stories1.p.rapidapi.com/?url=" + url)
                .get()
                .addHeader("X-RapidAPI-Key", "501d183d8cmsh80c6461a0f8e941p1520f2jsn883bf0f24eb3")
                .addHeader("X-RapidAPI-Host", "instagram-downloader-download-instagram-videos-stories1.p.rapidapi.com")
                .build();

        String videoURL;
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                videoURL = extractVideoUrl(responseBody);
                if (videoURL != null) {
                    downloadVideo(videoURL);
                } else {
                    System.out.println("Failed to extract video URL from response.");
                }
            } else {
                System.out.println("Failed to fetch video. Response code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractVideoUrl(String responseBody){
        try {
            // Parse the JSON array response body
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if the array contains any elements
            if (rootNode.isArray() && !rootNode.isEmpty()) {
                // Access the first element of the array
                JsonNode firstElement = rootNode.get(0);

                // Extract the video URL field from the first element
                JsonNode videoUrlNode = firstElement.path("url");

                // Check if the video URL field exists
                if (!videoUrlNode.isMissingNode() && videoUrlNode.isTextual()) {
                    // Return the video URL
                    return videoUrlNode.asText();
                } else {
                    // Handle case where video URL field is missing or not a string
                    System.out.println("Video URL field is missing or not a string in the response.");
                    return null;
                }
            } else {
                // Handle case where the array is empty or not present
                System.out.println("Empty or missing array in the response.");
                return null;
            }
        } catch (IOException e) {
            // Handle JSON parsing errors
            e.printStackTrace();
            return null;
        }
    }

    private static void downloadVideo(String videoURL){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(videoURL)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Save the video to a file
                try (InputStream inputStream = response.body().byteStream();
                     FileOutputStream outputStream = new FileOutputStream("downloaded_video.mp4")) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Video downloaded successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to download video. Response code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}