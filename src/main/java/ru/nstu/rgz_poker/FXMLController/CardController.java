package ru.nstu.rgz_poker.FXMLController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import ru.nstu.rgz_poker.MainLauncher;
import ru.nstu.rgz_poker.Model.Suit;

import java.io.IOException;

public class CardController {
    @FXML
    private Pane card;

    @FXML
    private SVGPath suit;

    @FXML
    private Text value;

    private String originalValue;
    private String originalSuit;
    private Paint originalColor;
    private boolean faceDown = false;

    public CardController(String suit, int value) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainLauncher.class.getResource("resources/Card.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Suit cardSuit = Suit.getSuit(suit);

        switch (cardSuit.getSuitType()) {
            case CLUBS:
                this.suit.setContent("M24.588 12.274c-1.845 0-3.503 0.769-4.683 2.022-0.5 0.531-1.368 1.16-2.306 1.713 0.441-1.683 1.834-3.803 2.801-4.733 1.239-1.193 2-2.87 2-4.734 0-3.59-2.859-6.503-6.4-6.541-3.541 0.038-6.4 2.951-6.4 6.541 0 1.865 0.761 3.542 2 4.734 0.967 0.93 2.36 3.050 2.801 4.733-0.939-0.553-1.806-1.182-2.306-1.713-1.18-1.253-2.838-2.022-4.683-2.022-3.575 0-6.471 2.927-6.471 6.541s2.897 6.542 6.471 6.542c1.845 0 3.503-0.792 4.683-2.045 0.525-0.558 1.451-1.254 2.447-1.832-0.094 4.615-2.298 8.005-4.541 9.341v1.179h12v-1.179c-2.244-1.335-4.448-4.726-4.541-9.341 0.995 0.578 1.922 1.274 2.447 1.832 1.18 1.253 2.838 2.045 4.683 2.045 3.575 0 6.471-2.928 6.471-6.542s-2.897-6.541-6.471-6.541z");
                this.suit.setFill(Color.BLACK);
                this.value.setFill(Color.BLACK);
                break;
            case DIAMONDS:
                this.suit.setContent("M16 0l-13 16 13 16 13-16z");
                this.suit.setFill(Color.RED);
                this.value.setFill(Color.RED);
                break;
            case HEARTS:
                this.suit.setContent("M32 11.192c0 2.699-1.163 5.126-3.015 6.808h0.015l-10 10c-1 1-2 2-3 2s-2-1-3-2l-9.985-10c-1.852-1.682-3.015-4.109-3.015-6.808 0-5.077 4.116-9.192 9.192-9.192 2.699 0 5.126 1.163 6.808 3.015 1.682-1.852 4.109-3.015 6.808-3.015 5.077 0 9.192 4.116 9.192 9.192z");
                this.suit.setFill(Color.RED);
                this.value.setFill(Color.RED);
                break;
            case SPADES:
                this.suit.setContent("M25.549 10.88c-6.049-4.496-8.133-8.094-9.549-10.88v0c-0 0-0-0-0-0v0c-1.415 2.785-3.5 6.384-9.549 10.88-10.314 7.665-0.606 18.365 7.93 12.476-0.556 3.654-2.454 6.318-4.381 7.465v1.179h12.001v-1.179c-1.928-1.147-3.825-3.811-4.382-7.465 8.535 5.889 18.244-4.811 7.93-12.476z");
                this.suit.setFill(Color.BLACK);
                this.value.setFill(Color.BLACK);
                break;
            default:
                throw new AssertionError(cardSuit.getSuitType().name());

        }
        String valueText = "";
        switch (value) {
            case 11:
                valueText = "J";
                break;
            case 12:
                valueText = "Q";
                break;
            case 13:
                valueText = "K";
                break;
            case 14:
                valueText = "A";
                break;
            default:
                valueText = value + "";
                break;
        }

        this.value.setText(valueText);
    }

    public Pane getCard() {
        return card;
    }

    public void faceDown() {
        originalValue = value.getText();
        originalSuit = suit.getContent();
        originalColor = suit.getFill();
        value.setText("");
        suit.setContent("M511.874,92.334c-3.346-26.823-23.47-32.411-34.645-32.411c-11.174,0-21.517,0-21.517,0 s-4.187-17.602-16.762-17.602c-5.587,0-34.365,0-34.365,0l-17.882-5.587c-6.7,4.474,2.234,16.762,2.234,16.762 s-12.295,9.782-12.295,47.78c0,37.998,0,51.414,0,63.702c0,24.591-39.975,31.344-56.48,5.709 C290.589,124.745,252.583,120.271,228,82.28c-16.868-26.074-34.153-34.706-45.933-35.069c-11.78-0.364-23.145,4.384-31.155,13.03 L30.784,178.348C8.949,198.509,0,218.626,0,243.216c0,24.583,0,53.641,0,67.057c0,13.408,22.35,15.649,22.35,2.233 c0-13.408,0-58.122,0-58.122s15.657-11.168,15.657,0c0,11.183,0,69.298,0,84.946c0,15.65-17.883,31.298-17.883,40.239 c0,8.942,31.291,93.873,31.291,93.873h31.291c0,0-20.116-71.523-20.116-80.464c0-7.146,19.465-30.2,28.83-45.842 c-0.954,14.506-14.347,29.852-13.491,38.022c0.931,8.896,40.891,90.11,40.891,90.11l31.117-3.255c0,0-27.445-69.04-28.376-77.936 c-0.931-8.895,25.893-45.758,27.536-59.068c3.642-29.595,2.135-18.201,2.135-18.201c-0.977-9.305,2.43-15.793,9.07-19.949 c36.538,0,83.394,0,98.544,0c20.116,0,17.876,15.649,15.65,24.591c-2.241,8.941-19.669,58.115-19.669,58.115l2.234,93.88h31.291 c0,0,2.24-82.705,2.24-89.406c0-2.324,4.157-11.152,9.6-22.175c0.795,9.91,1.575,17.701,1.575,17.701l13.408,93.88h31.291 c0,0-8.934-82.705-8.934-89.406c0-5.686,5.36-72.507,7.889-100.838c75.544-2.559,103.911-42.216,103.911-136.989 c0-29.058,4.475-29.066,14.53-29.066c10.054,0,26.785,2.006,35.72-1.34c8.949-3.347,6.708-11.175,6.708-11.175 S512.994,103.509,511.874,92.334z");
        //suit.setFill(Paint.valueOf("116db8"));
        suit.setFill(Paint.valueOf("000000"));
        suit.setLayoutX(suit.getLayoutX() - 239);
        suit.setLayoutY(suit.getLayoutY() - 245);
        suit.setScaleX(0.07);
        suit.setScaleY(0.07);
        faceDown = true;
    }
}
