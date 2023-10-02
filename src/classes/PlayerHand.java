package classes;

import java.util.ArrayList;

public class PlayerHand {
	private ArrayList<Card> hand;
	
	public ArrayList<Card> getPlayerHand() {
		return this.hand;
	}
	
	public void add(Card card) {
		this.hand.add(card);
	}
	public PlayerHand(ArrayList<Card> cards) {
		this.hand=cards;
	}
	public void remove(Card card) {
		this.remove(card);
	}
	
}
