package classes;

import java.util.ArrayList;

public class DealerHand {
	private ArrayList<Card> dealerHand;
	private Deck deck;
	public DealerHand(Deck deck) {
		this.dealerHand=new ArrayList<>();
		this.deck = deck;
	}
	
	public void flop() {
		this.addToDealerHand(deck.getRandomCards(3));
	}
	public void turn() {
		this.addToDealerHand(deck.draw());
	}
	public void river() {
		this.addToDealerHand(deck.draw());
	}
	
	public void addToDealerHand(Card card) {
		this.dealerHand.add(card);
	}
	
	public void addToDealerHand(ArrayList<Card> cards) {
		for (Card card : cards) {
			addToDealerHand(card);
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Card> getDealerHand() {
		return (ArrayList <Card>) this.dealerHand.clone();
	}
	public void printHand() {
		System.out.println("Dealer"+ " has the following hand :");
		for (Card card : this.dealerHand) {
			System.out.println(card);
		}
	}
	public void clear() {
		this.dealerHand.clear();
	}
}
