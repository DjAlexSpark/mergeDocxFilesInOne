package com.merger.mergedocxfilesinone;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private double x = 0;
    private double y = 0;
    private Stage stage;
    private URL url;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ProgressBar progressBar;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button buttonClose;

    @FXML
    private ImageView statusImg;
    @FXML
    private Button buttonPutInto;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonTakeFrom;

    @FXML
    private Label labelPutInto;

    @FXML
    private Label labelTakeFrom;

    @FXML
    private TextField textFieldNameOfFile;

    @FXML
    private TextField textFieldPutInto;

    @FXML
    private TextField textFieldTakeFrom;

    @FXML
    void OnClickExit(ActionEvent event) {
        javafx.application.Platform.exit();
    }

    @FXML
    void OnMouseDraggedOnPane(MouseEvent event) {
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);

    }

    @FXML
    void OnMousePressedOnPane(MouseEvent event) {//borderPanepressed
        x = event.getSceneX();
        y = event.getSceneY();

    }

    @FXML
    void OnMouseReleasedOnPane(MouseEvent event) {
//        System.out.println(x+" ; "+y);
//        System.out.println(" scX:"+event.getScreenX()+" scY:"+event.getScreenY());
//        System.out.println(" stX:"+stage.getY()+" stY:"+stage.getX());
//        System.out.println(Screen.getPrimary().getBounds().getWidth()+" "+Screen.getPrimary().getBounds().getHeight());
        //todo проверка на заперделье
    }

    @FXML
    void buttonChangeSource(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(stage);
        if (file != null) {
            textFieldTakeFrom.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void buttonChangeTarget(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(stage);
        if (file != null) {
            textFieldPutInto.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void onActionStartMerge(ActionEvent event) {
        if (!textFieldTakeFrom.getText().isEmpty() && Files.exists(Path.of((textFieldTakeFrom.getText())))&&
                !textFieldPutInto.getText().isEmpty() && Files.exists(Path.of((textFieldPutInto.getText())))&&
                !textFieldNameOfFile.getText().isEmpty() )
        {
            progressBar.setOpacity(1);
            progressBar.setProgress(0.1);



        //проверка на валидные входящие данные
        String spath = textFieldTakeFrom.getText();
        String nameTarget = textFieldNameOfFile.getText() + ".docx";
        String starget = textFieldPutInto.getText();//куда сохранить и как назвать файл

        XWPFDocument targetFile = null;
        Path path = Path.of(spath);
        Path target = Path.of(starget.concat("\\" + nameTarget));
        System.out.println(path + "\n" + target + "\n" + "");
            progressBar.setProgress(0.4);
        List<Path> listOfFilesPathsDocx = getListOfFiles(path);//готов
            progressBar.setProgress(0.6);
        targetFile = getFileFromPaths(listOfFilesPathsDocx);
            progressBar.setProgress(0.8);
        saveTargetFile(targetFile, target);
            progressBar.setProgress(1);
        //todo не стопится, файл привязан джавой
        }else {
            System.out.println("чёт не то");
        }
    }

    private void saveTargetFile(XWPFDocument targetFile, Path pathToSave) {
        try {
            targetFile.write(new FileOutputStream(new File(pathToSave.toAbsolutePath().toUri())));
            targetFile.close();
            System.out.println("Saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private XWPFDocument getFileFromPaths(List<Path> listOfFilesPathsDocx) {
        XWPFDocument resultFile = new XWPFDocument();
        XWPFDocument docx = null;
        for (Path p : listOfFilesPathsDocx) {
            try {
                docx = new XWPFDocument(new FileInputStream(p.toFile()));
                resultFile.getDocument().addNewBody().set(docx.getDocument().getBody());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("file is ready with " + listOfFilesPathsDocx.size());
        return resultFile;
    }


    private List getListOfFiles(Path pathOfFiles) {
        //лист с файлами в этом пути
        List list = null;
        try {
            list = Files.walk(pathOfFiles)
                    .filter(path -> path.toFile().isFile()
                            && path.toFile().toString().endsWith(".docx"))
                    .toList();
//                    .sort //todo разобраться как отсортировать файлы по алфавиту, по дате изменения
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (list != null) {
            System.out.println("GetListOFFiles() ready");
            return list;
        } else {
            System.out.println("GetListOFFiles() empty list");
            return new ArrayList<>();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.url = url;
        System.out.println(url);
        textFieldTakeFrom.textProperty().addListener(event -> {
            if (textFieldTakeFrom.getText().isEmpty() || Files.notExists(Path.of((textFieldTakeFrom.getText())))) {
                textFieldTakeFrom.setStyle("-fx-background-color: #ffa8af;");
            } else {
                textFieldTakeFrom.setStyle("-fx-background-color: white;");
            }
        });
        textFieldPutInto.textProperty().addListener(event -> {
            if (textFieldPutInto.getText().isEmpty() || Files.notExists(Path.of((textFieldPutInto.getText())))) {
                textFieldPutInto.setStyle("-fx-background-color: #ffa8af;");
            } else {
                textFieldPutInto.setStyle("-fx-background-color: white;");
            }
        });
        textFieldNameOfFile.textProperty().addListener(event -> {
            if (textFieldNameOfFile.getText().isEmpty() ) {
                textFieldNameOfFile.setStyle("-fx-background-color: #ffa8af;");
            } else {
                textFieldNameOfFile.setStyle("-fx-background-color: white;");
            }
        });


        XWPFDocument doc = new XWPFDocument();

    }
}
