//package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {

    private Labyrint lab;
    private boolean animation;
    private Text info;
    private Text info_2;
    private Timeline timeline;
    private Text info_vei;
    private Pane pane_1 = new Pane();
    private Button next_button = new Button("Next Path");
    private Button disable_animation_button;
    private GridPane current_grid;
    private ArrayList<ArrayList> paths = new ArrayList<>();
    private int total_paths;
    private int show_path;

    @Override
    public void start(Stage primaryStage) throws Exception{
        class Klikkbehandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e){
                pane_1.getChildren().remove(info_vei);
                Rute r =  (Rute)e.getSource();
                clearBoard();
                paths = new ArrayList<>();
                ArrayList<String> search_array = sort(lab.finnUtveiFra(r.getChordsY(), r.getChordsX()));
                for (Object s : search_array){
                    String s1 = (String) s;
                    String[] parts = s1.split("\\.");
                    ArrayList<Rute> current_path = new ArrayList<>();
                    for (int i = 0; i < parts.length; i++) {
                        String[] xy = parts[i].split(",");
                        Rute[][] ruter = lab.getBoard();
                        current_path.add(ruter[Integer.parseInt(xy[1])][Integer.parseInt(xy[0])]);
                    }
                    paths.add(current_path);
                }
                total_paths = paths.size();
                show_path = 0;
                info_vei = new Text("Vei "+(show_path+1)+" av "+ total_paths);
                info_vei.setFont(new Font(15));
                info_vei.setX(120);    info_vei.setY(300);
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pane_1.getChildren().add(info_vei);

                    }
                });
                showPaths();
            }
        }

        class nextPath implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e){
                if(total_paths == 0){
                    return;
                } else {
                    showPaths();
                }
            }
        }

        class diable_animtion_handler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e){
                if(animation){
                    disable_animation_button.setText("Active Animation");
                    timeline.stop();
                    clearBoard();
                    animation = false;
                } else {
                    timeline.stop();
                    disable_animation_button.setText("Disable Animation");
                    clearBoard();
                    animation = true;
                }
            }
        }

        class FileChooserHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e){
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if(selectedFile == null){
                    return;
                }
                Klikkbehandler klikk = new Klikkbehandler();
                pane_1.getChildren().remove(current_grid);
                pane_1.getChildren().remove(next_button);
                pane_1.getChildren().remove(info_vei);
                try {
                    lab = Labyrint.lesFraFil(selectedFile);
                    Rute[][] ruter = lab.getBoard();
                    GridPane labyrint_oversikt = new GridPane();
                    labyrint_oversikt.setGridLinesVisible(true);
                    for (int i = 0; i< ruter.length; i++){
                        for (int j = 0; j < ruter[i].length; j++) {
                            if(ruter[i][j].tilTegn() == '.'){
                                ruter[i][j].setOnAction(klikk);
                            }
                            labyrint_oversikt.add(ruter[i][j], j, i);
                        }
                    }
                    labyrint_oversikt.setLayoutX(290); labyrint_oversikt.setLayoutY(50);
                    current_grid = labyrint_oversikt;
                    nextPath next_path_handler = new nextPath();
                    next_button.setOnAction(next_path_handler);
                    next_button.setLayoutX(120);  next_button.setLayoutY(235);
                    //

                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            pane_1.getChildren().add(labyrint_oversikt);
                            pane_1.getChildren().add(next_button);

                        }
                    });


                } catch (FileNotFoundException error_file) {
                    System.out.println(error_file);
                }
            }
        }


        Button button_choose_file = new Button("Velg File");
        button_choose_file.setLayoutX(120);  button_choose_file.setLayoutY(200);
        FileChooserHandler chose_file = new FileChooserHandler();
        button_choose_file.setOnAction(chose_file);

        disable_animation_button = new Button("Disable Animation");
        disable_animation_button.setLayoutX(90); disable_animation_button.setLayoutY(350);
        diable_animtion_handler DAH = new diable_animtion_handler();
        disable_animation_button.setOnAction(DAH);

        timeline = new Timeline();

        info = new Text("Labyrint");
        info.setFont(new Font(30));
        info.setX(100);    info.setY(100);
        info.setStyle("-fx-text-fill: White;");

        info_2 = new Text("- Oblig 7 -");
        info_2.setFont(new Font(15));
        info_2.setX(120);    info_2.setY(150);
        animation = true;

        Rectangle r = new Rectangle();
        r.setX(700);r.setY(50);
        r.setWidth(225);r.setHeight(500);
        r.setFill(Color.GRAY);
        Rectangle r2 = new Rectangle();
        r2.setX(50);r2.setY(50);
        r2.setWidth(225);r2.setHeight(500);
        r2.setFill(Color.GRAY);


        Label label = new Label("1. Velg en fil.\n2. Trykk på en hvit rute \n3. Se alle veiene.");
        label.setLayoutX(725);label.setLayoutY(70);
        label.setFont(new Font(20));
        label.setMaxWidth(180);
        label.setWrapText(true);

        pane_1.getChildren().add(r);
        pane_1.getChildren().add(r2);
        pane_1.getChildren().add(button_choose_file);
        pane_1.getChildren().add(info_2);
        pane_1.getChildren().add(info);
        pane_1.getChildren().add(label);
        pane_1.getChildren().add(disable_animation_button);
        pane_1.setStyle("-fx-background-color: DarkGray");
        Scene scene = new Scene(pane_1, 970, 600);

        primaryStage.setTitle("Labyrint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showPaths(){
        timeline.stop();
        clearBoard();
        if(animation){
            timeline = new Timeline();
            int time = 50;
            for (Object r3 : paths.get(show_path)){
                Rute use_rute = (Rute) r3;
                timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(use_rute.styleProperty(), "-fx-background-color: White")), new KeyFrame(new Duration(time), new KeyValue(use_rute.styleProperty(), "-fx-background-color: Blue")));
                //use_rute.setStyle("-fx-background-color: Red");
                time += 50;
            }
            timeline.play();
        } else {
            for (Object r3 : paths.get(show_path)){
                Rute use_rute = (Rute) r3;
                use_rute.setStyle("-fx-background-color: Blue");
            }
        }

        show_path++;
        info_vei.setText("Vei "+(show_path)+" av "+ total_paths);
    }

    public void clearBoard(){
        if(total_paths == 0){
            return;
        }
        if(show_path == total_paths){
            for (Object r3 : paths.get(total_paths-1)){
                Rute use_rute = (Rute) r3;
                use_rute.setStyle("-fx-background-color: White");
            }
            show_path = 0;
        } else if(show_path>0){
            for (Object r3 : paths.get(show_path-1)){
                Rute use_rute = (Rute) r3;
                use_rute.setStyle("-fx-background-color: White");
            }
        }
    }

    public ArrayList sort(Liste l){
        ArrayList<String> return_list = new ArrayList<>();
        int biggest = 0;
        int pos = 0;
        String top = "";
        int total = l.stoerrelse();
        for(int j = 0; j < total; j++) {
            for (int i = 0; i < l.stoerrelse(); i++) {
                String test_string = (String) l.hent(i);
                if (test_string.length() > biggest) {
                    biggest = test_string.length();
                    top = test_string;
                    pos = i;
                }
            }
            l.fjern(pos);
            return_list.add(0, top);
            biggest = 0;
        }
        return return_list;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
