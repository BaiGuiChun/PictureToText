package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author guichun
 * 设置拖曳
 */
public class Drag extends Application {

    /**
     * 获取桌面路径
     */
    FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    File file1Path = fileSystemView.getHomeDirectory();
    Label label = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        /**
         * 声明
         */
        primaryStage.setTitle("图片名称转文本");
        primaryStage.setAlwaysOnTop(true);
        Group group = new Group();
        Button button = new Button("选择文件");

        final FileChooser fileChooser = new FileChooser();

        //循环 生成方块
        Rectangle rect = new Rectangle(20, 60, 300, 300);
        //属性：设置圆角的宽度
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.rgb(128, 152, 131));

        button.setStyle("-fx-font: 20 arial; -fx-base: #0076ee; ");
        button.setLayoutX(110.0);
        button.setLayoutY(20.0);




        /**
         * 事件
         */
        //按钮点击事件
        button.setOnAction(
                (final ActionEvent event) -> {
                    configureFileChooser(fileChooser);
                    List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
                    if (list != null) {
                        list.stream().forEach((file) -> {
                            String str=file.getName();
                            String prefix = str.substring(str.lastIndexOf("."));
                            int num = prefix.length();//得到后缀名长度

                            System.out.println("str:"+str);
                            System.out.println("str2:"+str.substring(0, str.length() - num));

                            try {
                                WriteFileExample(str, str.substring(0, str.length() - num));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });

        //设置拖曳
        rect.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != rect
                        && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        rect.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;

                List<File> list = db.getFiles();

                if (db.hasFiles()) {
                    list.stream().forEach((file) -> {
                        String str=file.getName();//8265.jpg
                        String prefix = str.substring(str.lastIndexOf("."));
                        int num = prefix.length();//得到后缀名长度

                        try {
                            WriteFileExample(str, str.substring(0, str.length() - num));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();

            }
        });
        group.getChildren().addAll(rect, button, label);
        Scene scene = new Scene(group, 340, 370);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //文件格式
    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

    }


    //写数据
    public void WriteFileExample(String content, String contentPng) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file;
        try {
            System.out.println("desktop:" + file1Path);
            file = new File(file1Path + "/newfile.txt");
            fileOutputStream = new FileOutputStream(file, true);
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = content.getBytes();
            byte[] contentInBytes2 = contentPng.getBytes();

            fileOutputStream.write("<icon name=".getBytes());
            fileOutputStream.write('"');
            fileOutputStream.write(contentInBytes);
            fileOutputStream.write('"');
            fileOutputStream.write(" ".getBytes());
            fileOutputStream.write("package=".getBytes());
            fileOutputStream.write('"');
            fileOutputStream.write(contentInBytes2);
            fileOutputStream.write('"');
            fileOutputStream.write("/>".getBytes());
            fileOutputStream.write("\r\n".getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Done!");
            label.setText("生成的文件的路径："+file1Path + "/newfile.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
