//package sample;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Labyrint{
	private Rute[][] ruter;
	private int rows_max;
	private int columns_max;
	private Lenkeliste<String> exists;
	private Labyrint(Rute[][] ruter, int rows, int columns){
		this.ruter = ruter;
		rows_max = rows;
		columns_max = columns;
		exists = new Lenkeliste<String>();
	}

	public Rute[][] getBoard(){
		return ruter;
	}

	public static Labyrint lesFraFil(File fil) throws FileNotFoundException {
		Scanner file_reader = new Scanner(fil);

		Rute[][] rows;
		int rows_current = 0;
		int rows_max = 0;
		int columns_current = 0;
		int columns_max = 0;


		String data = file_reader.nextLine();
		String[] data_formated = data.split("\\s+");
		rows_max = Integer.parseInt(data_formated[0]);
		columns_max = Integer.parseInt(data_formated[1]);
		rows = new Rute[rows_max][columns_max];
		while(file_reader.hasNextLine()){
			data = file_reader.nextLine();
			for (Character c : data.toCharArray()) {
				if(c.equals('.')){
					if((rows_current == 0 || rows_current == rows_max-1) || (columns_current == 0 || columns_current == columns_max-1)){
						rows[rows_current][columns_current] = new Aapning(rows_current, columns_current, columns_max);
						rows[rows_current][columns_current].setStyle("-fx-background-color: White");
					} else {
						rows[rows_current][columns_current] = new HvitRute(rows_current, columns_current, columns_max);
						rows[rows_current][columns_current].setStyle("-fx-background-color: White");
					}
				} else if(c.equals('#')){
					rows[rows_current][columns_current] = new SortRute(rows_current, columns_current, columns_max);
					rows[rows_current][columns_current].setStyle("-fx-background-color: Black");
				}
				columns_current++;
			}
			columns_current = 0;
			rows_current++;
		}
		Labyrint lab_object = new Labyrint(rows, rows_max, columns_max);
		for (int x = 0; x<rows_max; x++ ) {
			for (int y = 0; y<columns_max; y++ ) {
				try{
					rows[x][y].settNeighbors(0, rows[x][y+1]);
				} catch(Exception e) {}
				try{
					rows[x][y].settNeighbors(1, rows[x][y-1]);
				} catch(Exception e) {}
				try{
					rows[x][y].settNeighbors(2, rows[x+1][y]);
				} catch(Exception e) {}
				try{
					rows[x][y].settNeighbors(3, rows[x-1][y]);
				} catch(Exception e) {}
				rows[x][y].setLabyrint(lab_object);
			}
		}
		return lab_object;
	}

	public Liste finnUtveiFra(int y, int x){
		exists = new Lenkeliste<String>();
		return ruter[x][y].finnUtVei(exists);
	}

	@Override
	public String toString(){
		String return_string = "";
		for (int x = 0; x<rows_max; x++ ) {
			for (int y = 0; y<columns_max; y++ ) {
				return_string += ruter[x][y].tilTegn();
			}
			return_string += "\n";
		}
		return return_string;
	}


}