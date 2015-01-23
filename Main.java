package hexGame;

import hexGame.model.DefaultHexModel;
import hexGame.model.HexModel;
import hexGame.util.Coord;

public class Main {
	private HexModel model;
	
	public Main() {
		model = new DefaultHexModel();
	}
	
	public void run() {
		model.setSize(4);
		addPos(1, 0);
		addPos(2, 2);
		addPos(1, 1);
		addPos(3, 2);
		addPos(1, 2);
		addPos(2, 1);
		addPos(1, 3);
	}
	
	private void addPos(int x, int y) {
		model.nextMove(new Coord(x, y));
		System.out.println("Ajout en (" + x + ", " + y +")");
		System.out.println(model);
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}
