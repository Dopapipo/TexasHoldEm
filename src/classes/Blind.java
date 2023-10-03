package classes;

public class Blind {
	//A blind has a value and what player needs to pay it
	private int value;
	private Player player;
	public Blind(int value) {
		this(value,null);
	}
	public Blind (int value, Player player) {
		this.value=value;
		this.player=player;
	}
	public int getValue() {
		return this.value;
	}
	public Player getPlayer() {
		return this.player;
	}
	public void increase(int howMuch) {
		this.value+=howMuch;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String toString() {
		return this.player.getName() + " has to pay a blind of " + this.value;
	}
}
