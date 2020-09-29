//package sample;
public class SortRute extends Rute {

	public SortRute(int row, int column, int total){
		super(row, column, total);
	}

	@Override
	public char tilTegn(){
		return '#';
	}
}