module com.thuchanh.quanlyphonghoc {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    opens model to javafx.base;
    requires javafx.base;
    requires javafx.graphics;
    exports com.thuchanh.quanlyphonghoc;
}
