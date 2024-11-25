package com.example.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;

public class CenteredCoinGuessingGame extends Application {

    // Mảng lưu trữ vị trí của đồng xu
    private static final boolean[] COIN_PLACES = new boolean[4];
    private static final Random random = new Random();
    private final boolean gameEnded = false;  // Biến kiểm tra trò chơi đã kết thúc hay chưa
    private int score = 0;  // Điểm của người chơi

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Ngẫu nhiên chọn một ô chứa đồng xu
        placeCoin();

        // Tạo GridPane để chứa 4 ô ảnh
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Căn chỉnh GridPane ra giữa
        grid.setPrefSize(400, 400);
        grid.setStyle("-fx-alignment: center;");  // Căn giữa GridPane

        // Thêm 4 ô hình ảnh
        for (int i = 0; i < 4; i++) {
            ImageView boxImage = new ImageView(new Image("file:box_default.png"));  // Hình ảnh ô mặc định
            boxImage.setFitWidth(400);
            boxImage.setFitHeight(300);
            int index = i;

            boxImage.setOnMouseClicked(e -> {
                if (!gameEnded) {
                    checkCoin(index, boxImage, primaryStage);
                }
            });

            grid.add(boxImage, i % 2, i / 2);  // Căn 2x2 (2 cột, 2 hàng)
        }

        // Thêm hình ảnh nền
        StackPane root = new StackPane();
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("file:background.jpg"),  // Đường dẫn đến hình nền
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );
        root.setBackground(new Background(backgroundImage));

        // Đặt GridPane vào giữa StackPane
        root.getChildren().add(grid);

        // Tạo và hiển thị giao diện
        Scene scene = new Scene(root, 1298, 730);  // Đặt kích thước cửa sổ
        primaryStage.setTitle("Trò Chơi Tìm Đồng Xu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Đặt đồng xu vào một ô ngẫu nhiên
    private void placeCoin() {
        int coinIndex = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            COIN_PLACES[i] = (i == coinIndex);
        }
    }

    // Kiểm tra ô người chơi chọn có chứa đồng xu hay không
    private void checkCoin(int index, ImageView boxImage, Stage primaryStage) {
        if (COIN_PLACES[index]) {
            // Người chơi chọn đúng ô có đồng xu
            boxImage.setImage(new Image("file:box_coin.png")); // Hiển thị vàng ngay lập tức
            playSound("D:/UETTTTTTTT/THOOP/Game/src/main/resources/sounds/correct.mp3"); // Phát nhạc đúng
            score++;
            showAlert("Chúc mừng!", "Bạn đã chọn đúng ô chứa vàng!\nĐiểm của bạn: " + score);
            primaryStage.close(); // Đóng cửa sổ sau khi hiển thị thông báo
        } else {
            // Người chơi chọn sai ô
            boxImage.setImage(new Image("file:box_empty.png")); // Hiển thị ô trống ngay lập tức
            playSound("D:/UETTTTTTTT/THOOP/Game/src/main/resources/sounds/wrong.mp3"); // Phát nhạc sai
            score--;
            showAlert("Hãy thử lại!", "Ô này không có vàng. Trò chơi kết thúc.\nĐiểm cuối cùng của bạn: " + score);
            primaryStage.close(); // Đóng cửa sổ sau khi hiển thị thông báo
        }
    }

    // Phương thức phát âm thanh
    private void playSound(String soundFilePath) {
        try {
            Media sound = new Media(new File(soundFilePath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(1.0);
            mediaPlayer.play(); // Phát nhạc ngay lập tức
        } catch (Exception e) {
            System.out.println("Không thể phát âm thanh: " + e.getMessage());
        }
    }

    // Hiển thị thông báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
