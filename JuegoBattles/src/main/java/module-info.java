module com.example.juegobattles {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.juegobattles to javafx.fxml;
    exports com.example.juegobattles;
}