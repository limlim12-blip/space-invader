module spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires py4j;
    opens gamestate to javafx.fxml;
    exports  gamestate;
    exports  entities;
}
