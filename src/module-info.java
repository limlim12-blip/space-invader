module spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    opens entities to javafx.fxml;
    exports  entities;
}
