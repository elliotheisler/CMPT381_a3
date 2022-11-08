module com.example.a3_cmpt381 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.a3_cmpt381 to javafx.fxml;
    exports com.example.a3_cmpt381;
}