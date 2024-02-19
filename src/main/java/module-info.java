module com.example.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.pacman to javafx.fxml;
    exports com.example.pacman;
}