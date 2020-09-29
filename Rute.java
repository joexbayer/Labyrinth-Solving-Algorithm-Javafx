//package sample;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

import java.util.ArrayList;

public abstract class Rute extends Button {

	private int row;
	private int column;
	private Labyrint labyrint;
	private Rute[] neighbors;

	public Rute(int row, int column, int total){
		super(" ");
		setFont(new Font(200/total));
		setPrefSize(400/total,400/total);
		neighbors = new Rute[4];
		this.row = row;
		this.column = column;
	}

	abstract char tilTegn();

	public void settNeighbors(int i, Rute r){
		neighbors[i] = r;
	}

	public void setLabyrint(Labyrint l){
		labyrint = l;
	}
	
	public boolean isEntrance(){
		return false;
	}

	public void gaa(Liste l, ArrayList<Rute> path_taken, String path){
		for (int i = 0; i<4 ;i++ ) {
			if(neighbors[i] != null && !path_taken.contains(neighbors[i])){
				Character c = neighbors[i].tilTegn();
				if(this.isEntrance()){
					path += ""+column+","+row;
					l.leggTil(path);
					return;
				} else if(c.equals('.')){
					if(!path_taken.contains(this)){
						path += ""+column+","+row+".";
					}
					path_taken.add(this);
					neighbors[i].gaa(l, path_taken, path);
				}
			}
		}
	}
	public Liste finnUtVei(Liste exists){
		ArrayList<Rute> path_taken = new ArrayList<Rute>();
		String path = ""+column+","+row+".";
		path_taken.add(this);
		gaa(exists, path_taken, path);
		return exists;
	}

	public int getChordsX(){
		return row;
	}

	public int getChordsY(){
		return column;
	}
}