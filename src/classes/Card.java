package classes;

import java.util.Comparator;

public class Card implements Comparator<Card>{
	// les attributs
	private CardValue cardValue;
	private CardColor cardColor;

	// le constructeur
	public Card(CardValue value, CardColor color) {
		this.cardValue = value;
		this.cardColor = color;
	}

	public CardValue getCardValue() {
		return cardValue;
	}

	public void setCardValue(CardValue cardValue) {
		this.cardValue = cardValue;
	}

	public CardColor getCardColor() {
		return cardColor;
	}

	public void setCardColor(CardColor cardColor) {
		this.cardColor = cardColor;
	}

	// les méthodes
	@Override
	public String toString() {
		String color = "";
		switch (this.cardColor) {
		case SPADE:
			color = "SPADE";
			break;
		case DIAMOND:
			color = "DIAMOND";
			break;
		case HEART:
			color = "HEART";
			break;
		case CLOVER:
			color = "CLOVER";
			break;
		}
		return String.valueOf(this.cardValue) + " of " + color;
	}

	public boolean equals(Card card) {
		return this.getCardValue() == card.getCardValue();
	}

	public boolean compareTo(Card card) {
		return this.getCardValue().compare(card.getCardValue()) > 0;
	}

	@Override
	public int compare(Card c1, Card c2) {
		// TODO Auto-generated method stub
		return c1.getCardValue().compare(c2.getCardValue());
	}



}