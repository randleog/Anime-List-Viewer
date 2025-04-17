module com.injata.animelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.apache.commons.io;

    opens com.injata.animelist to javafx.fxml;
    exports com.injata.animelist;
}