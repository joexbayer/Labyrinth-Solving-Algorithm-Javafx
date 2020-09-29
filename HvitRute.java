//package sample;
public class HvitRute extends Rute {

	public HvitRute(int row, int column, int total){
		super(row, column, total);
	}

	@Override
	public char tilTegn(){
		return '.';
	}
}