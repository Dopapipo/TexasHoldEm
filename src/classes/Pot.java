package classes;

import java.util.ArrayList;
/**
 * A Pot is used to distribute gains at the end of a round,
 * and holds all the bets. There can be multiple pots when a player goes all-in,
 * because the remaining players will compete for a side-pot that the all-in player
 * can not win, as he has not paid into it.
 */
public class Pot {
	private int value;
	private ArrayList<Player> players;
	public Pot() {
		this.value=0;
		this.players= new ArrayList<>();
	}
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	public void addBet(int bet) {
		this.value+=bet;
	}
	public void resetPot() {
		this.value=0;
		this.players.clear();
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	@SuppressWarnings("unchecked")
	public void setPlayers(ArrayList<Player> players) {
		//clone player list to avoid list side effects ("effets de bord")
		this.players = (ArrayList<Player>) players.clone();
	}
	/* Pseudo code
	public void distributeGains() {
		int winningPlayers = 0;
		for (Player player:this.players) {
			if (player.hasWon()) {
				winningPlayers++;
			}
		}
		int winnings = this.value/winningPlayers;
		for (Player player: this.players) {
			if (player.hasWon()) {
			player.won(winnings);
			}
		}
	}
	*/
}
