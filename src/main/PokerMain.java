package main;

import classes.Player;
import classes.PokerTable;

public class PokerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Player p1 = new Player("Pablo", 200);
		Player p2 = new Player("Marco", 200);
		Player p3 = new Player("Pamela", 200);
		PokerTable table = new PokerTable();
		table.addPlayer(p1);
		table.addPlayer(p2);
		table.addPlayer(p3);
		while (table.howManyAreStillPlaying() > 1) {

			table.startTurn();
			System.out.println(p1.getChipStack());
			System.out.println(p2.getChipStack());
		}
		/*
		 * dealerHand.flop(); dealerHand.turn(); dealerHand.river();
		 * dealerHand.printHand();
		 * p1.setWinCombination(WinConditionLogic.findWinningCombination(dealerHand,p1.
		 * getPlayerHand()));
		 * p2.setWinCombination(WinConditionLogic.findWinningCombination(dealerHand,
		 * p2.getPlayerHand())); System.out.println(p1.getWinningCombination());
		 * System.out.println(p2.getWinningCombination());
		 */
	}

}
