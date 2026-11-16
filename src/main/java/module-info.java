module com.uabc.poo2 {
  requires javafx.controls;
  requires javafx.fxml;
  requires db4o.all;
  requires javafx.base;
  requires javafx.graphics;

  opens com.uabc.poo2.controlador to javafx.fxml;
  opens com.uabc.poo2.modelo to javafx.base, db4o.all;

  exports com.uabc.poo2.vista;
}
