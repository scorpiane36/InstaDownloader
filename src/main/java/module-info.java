module org.downloader.instadownloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;


    opens org.downloader.instadownloader to javafx.fxml;
    exports org.downloader.instadownloader;
}