module ru.nstu.rgz_poker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.nstu.rgz_poker to javafx.fxml;
    exports ru.nstu.rgz_poker;
    exports ru.nstu.rgz_poker.FXMLController;
    opens ru.nstu.rgz_poker.FXMLController to javafx.fxml;
    opens ru.nstu.rgz_poker.Model to javafx.fxml;
}