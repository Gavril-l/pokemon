module com.uabc.poo2 {
  requires javafx.controls;
  requires javafx.fxml;

  opens com.uabc.poo2.controlador to javafx.fxml;
  opens com.uabc.poo2.modelo to javafx.base;

  exports com.uabc.poo2.vista;
}
