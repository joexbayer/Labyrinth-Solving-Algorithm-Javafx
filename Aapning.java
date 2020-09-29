//package sample;
public class Aapning extends HvitRute{
	public Aapning(int row, int column, int total){
		super(row, column, total);
	}

	@Override
	public boolean isEntrance(){
		return true;
	}
}