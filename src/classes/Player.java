package classes;

import java.util.ArrayList;

public class Player implements Comparable<Player>{

	private String name;
	private int chipStack;
	private int bet;
	private WinningCombination combination;
	private PlayerHand playerHand;
	private boolean playing;
	private boolean hasNotFolded;
	private boolean currentlyRaising;
	private boolean isAllIn;
	
	
	public Player(String name) {
		this(name,0);
	}
	public Player(String name, int chips) {
		this.name = name;
		this.chipStack = chips;
		this.playing=(chips>0);
		this.hasNotFolded=true;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChipStack() {
		return chipStack;
	}

	public void setChipStack(int chipStack) {
		this.chipStack = chipStack;
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public WinningCombination getCombination() {
		return combination;
	}

	public void setCombination(WinningCombination combination) {
		this.combination = combination;
	}

	public PlayerHand getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(PlayerHand playerHand) {
		this.playerHand = playerHand;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}



	// les méthodes
	public void setHand(PlayerHand cards) {
		this.playerHand = cards;
	}
	public void setWinCombination(WinningCombination winCondition) {
		this.combination=winCondition;
	}
	


	public void addCard(ArrayList<Card> cards) {
		for (Card card : cards) {
			this.playerHand.add(card);
		}
	}



	public void printHand() {
		System.out.println(this.name + " has the following hand :");
		for (Card card : this.playerHand.getPlayerHand()) {
			System.out.println(card);
		}
	}
	
	public int  allIn() {
		if (!this.playing) {
			return 0;
		}
		this.bet+=this.chipStack;
		this.chipStack=0;
		this.hasNotFolded=true;
		this.setAllIn(true);
		return this.bet;
	}
	/**
	 * Allows a player to bet. If the players tries to bet more than they own,
	 * they automatically all-in. The player can't bet a negative value.
	 * @param howMuch
	 * @return how much player is betting now
	 */
	public int bet(int howMuch) {
		if (!this.playing) {
			return 0;
		}
		if (howMuch<0) {
			return 0;
		}
		if (this.chipStack-howMuch<0) {
			return allIn();
		}
		this.chipStack-=howMuch;
		this.hasNotFolded=true;
		this.bet+=howMuch;
		return this.bet;
		
	}
	public void call(int howMuch) {
		this.bet(howMuch);
	}
	public void fold() {
		this.hasNotFolded=false;
	}
	public void lost() {
		this.hasNotFolded=false;
		this.bet=0;	
	}
	public void won(int winnings) {
		this.chipStack+=winnings;
		this.hasNotFolded=false;
		this.bet=0;
	}
	
	public void addToChipStack(int n ) {
		this.chipStack+=n;
	}
	public WinningCombination getWinningCombination() {
		return this.combination;
	}
	public boolean hasNotFolded() {
		return hasNotFolded;
	}
	public void setHasNotFolded(boolean hasNotFolded) {
		this.hasNotFolded = hasNotFolded;
	}
	@Override
	public int compareTo(Player player) {
		return (this.getWinningCombination().compareTo(player.getWinningCombination()));
	}
	public boolean isCurrentlyRaising() {
		return currentlyRaising;
	}
	public void setCurrentlyRaising(boolean currentlyRaising) {
		this.currentlyRaising = currentlyRaising;
	}
	public boolean isAllIn() {
		return isAllIn;
	}
	public void setAllIn(boolean isAllIn) {
		this.isAllIn = isAllIn;
	}
	
}
