package frame;

import com.displee.cache.CacheLibrary;
import com.sun.org.apache.xpath.internal.operations.Mod;
import converter.ModelConverter;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainFrame extends Application {

    @FXML
    private TextField originalCacheTextField;

    @FXML
    private TextField modifiedCacheTextField;

    @FXML
    private TextField osrsCacheTextField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainFrame.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    public void launchModelConversionFrame() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("modelConversionFrame.fxml"));

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @FXML
    public void originalCacheBrowse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            originalCacheTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void modifiedCacheBrowse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            modifiedCacheTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void osrsCacheBrowse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            osrsCacheTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void modelConversionPack() {
        if (originalCacheTextField.getText() == null || originalCacheTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You haven't entered the Original Cache Directory", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (modifiedCacheTextField.getText() == null || modifiedCacheTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You haven't entered the Modified Cache Directory", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (osrsCacheTextField.getText() == null || osrsCacheTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You haven't entered the OSRS Cache Directory", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        CacheLibrary originalCache = CacheLibrary.create(originalCacheTextField.getText());
        CacheLibrary modifiedCache = CacheLibrary.create(modifiedCacheTextField.getText());
        CacheLibrary osrsCache = CacheLibrary.create(osrsCacheTextField.getText());

        ModelConverter.Companion.convertModels(originalCache, modifiedCache, osrsCache);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Successfully Converted & Packed Models into the OSRS Cache..", ButtonType.OK);
        alert.showAndWait();

    }

}
