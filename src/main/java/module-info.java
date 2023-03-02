module com.merger.mergedocxfilesinone {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi.ooxml;
    requires xmlbeans;
    requires poi.ooxml.schemas;
    requires poi;



    opens com.merger.mergedocxfilesinone to javafx.fxml;
    exports com.merger.mergedocxfilesinone;
}