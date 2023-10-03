package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import logic.WinConditionLogic;

//TODO : Implement not asking a player for call once he is all-in, DONE
//implement correct win when a player all-ins with less chips 
//than the others are betting (A all ins with 50, other 2 players bet 200, 
//if A wins then he gets 150, and the other 2 players compete for the rest.)
//Separate display and logic for PokerTable
//Probably need to create a Pot object and a list of Pots in PokerTable
//when someone all ins, create a separate pot.
//Implement not asking for call when all other players are all in OK
//Implement blind system (partially done, need to start using it now)
//Implement increasing blinds every x turns

public class PokerTable {
	private ArrayList<Player> playerList;
	private ArrayList<Player> currentlyPlaying;
	private DealerHand dealer;
	private Deck deck;
	private int totalBets;
	private int highestBet;
	private Blind bigBlind;
	private Blind smallBlind;
	private Blind donor;
	private final int defaultBlind = 5;
	Scanner scanner = new Scanner(System.in);

	// Pot implementation start
	ArrayList<Pot> pots;

	public PokerTable() {
		this.playerList = new ArrayList<>();
		this.currentlyPlaying = new ArrayList<>();
		this.deck = new Deck();
		this.dealer = new DealerHand(deck);
		totalBets = 0;
		this.highestBet = 0;
	}

	public PokerTable(Player player) {
		this.playerList = new ArrayList<>();
		this.playerList.add(player);
		this.deck = new Deck();
		this.dealer = new DealerHand(deck);
	}

	@SuppressWarnings("unchecked")
	public PokerTable(ArrayList<Player> players) {
		this.playerList = players;
		this.deck = new Deck();
		this.dealer = new DealerHand(deck);
		this.currentlyPlaying = (ArrayList<Player>) this.playerList.clone();
	}

	public int howManyAreStillPlaying() {
		return this.currentlyPlaying.size();
	}

	public void initializeBlinds() {
		int n = this.currentlyPlaying.size();
		if (n <= 1) {
			return;
		}
		// last player at the table will be first big blind, the one before him
		// will be small blind, and the one before that will be donor.
		// if there's only two players, one player will always be donor and small blind.
		if (this.bigBlind == null) {
			this.bigBlind = new Blind(this.defaultBlind, this.currentlyPlaying.get(n - 1));
			this.smallBlind = new Blind(this.defaultBlind / 2, this.currentlyPlaying.get(n - 2));
			this.donor = new Blind(0, this.currentlyPlaying.get(Math.max(0, n - 3)));
		}

	}

	public void switchBlinds() {
		int n = this.currentlyPlaying.size();
		int bigBlindIndex = 0, smallBlindIndex = 0, donorIndex = 0;
		// find the index of players that hold the blinds
		for (int i = 0; i < n; i++) {
			if (this.currentlyPlaying.get(i) == this.bigBlind.getPlayer()) {
				bigBlindIndex = i;
			} else if (this.currentlyPlaying.get(i) == this.smallBlind.getPlayer()) {
				smallBlindIndex = i;
			} else if (this.currentlyPlaying.get(i) == this.donor.getPlayer()) {
				donorIndex = i;
			}
		}
		// increment the blinds mod n (if last player has blind, it goes to the first)
		bigBlindIndex = (bigBlindIndex + 1) % n;
		smallBlindIndex = (smallBlindIndex + 1) % n;
		donorIndex = (donorIndex + 1) % n;
		// set players to the blinds
		this.bigBlind.setPlayer(this.currentlyPlaying.get(bigBlindIndex));
		this.smallBlind.setPlayer(this.currentlyPlaying.get(smallBlindIndex));
		this.donor.setPlayer(this.currentlyPlaying.get(donorIndex));
	}

	/**
	 * Kicks players with no money left
	 */
	public void kickBrokePlayers() {
		for (Player player : this.playerList) {
			if (player.getChipStack() == 0) {
				player.setPlaying(false);
				this.currentlyPlaying.remove(player);
			}
		}
	}

	public void addPlayer(Player player) {
		this.playerList.add(player);
		if (player.getChipStack() > 0) {
			this.currentlyPlaying.add(player);
		}
	}

	public void giveCards() {
		for (Player player : this.playerList) {
			player.setHand(new PlayerHand(this.deck.getRandomCards(2)));
		}
	}

	/**
	 * Asks players to raise,call,or fold. Need to implement big blind small blind.
	 */
	public int askForBets(int playersInRound) {
		boolean everyoneCalled = false;
		// Implementation of support for constant raising : while
		// not everyone has called/folded (i.e. there's still a player raising)
		// We ask every other player for a call/fold/raise
		ArrayList<Boolean> playersCalled = new ArrayList<>();
		// A round is basic,turn,flop,river,finish, so need to pass this
		// variable playersInRound inbetween askForBets calls or the function
		// will keep asking a player for raise call fold even tho he's the only
		// one who hasn't folded. (TODO)

		while (!everyoneCalled) {
			playersCalled.clear();
			for (Player player : this.currentlyPlaying) {
				// if there's more than one player to ask, player hasn't folded, isn't all in
				// and isn't the one currently raising,
				// ask him for bet
				if (playersInRound > 1 && player.hasNotFolded() && player.getChipStack() > 0
						&& !player.isCurrentlyRaising()) {
					System.out.println("Current highest bet is " + this.highestBet);
					System.out.println(player.getName() + ", you are currently betting " + player.getBet());
					player.printHand();
					System.out.println("Press 1 to call, 2 to fold, 3 to raise");
					int answer = scanner.nextInt();
					while (answer != 1 && answer != 2 && answer != 3) {
						System.out.println("Wrong command ! Try again: ");
						answer = scanner.nextInt();
					}

					switch (answer) {
					case 1:
						player.call(this.highestBet - player.getBet());
						player.setCurrentlyRaising(false);
						break;
					case 2:
						player.fold();
						player.setCurrentlyRaising(false);
						playersInRound--;
						break;
					// if a player raises, we set him to currently raising, and all the other
					// players to not currently raising
					case 3:
						System.out.println("How much do you want to raise by? (negative will call!)");
						int x = scanner.nextInt();
						if (x > 0) {
							this.highestBet = player.bet(highestBet - player.getBet() + x);
							for (Player aPlayer : this.currentlyPlaying) {
								// we shouldn't have any weird behaviour
								// if we use != for comparison, at least for now
								aPlayer.setCurrentlyRaising(false);
							}
							player.setCurrentlyRaising((true));
							playersCalled.add(false);
						} else {
							player.call(this.highestBet - player.getBet());
							player.setCurrentlyRaising(false);
							playersCalled.add(true);
						}
						break;
					}
				}
				if (player.isAllIn()) {
					playersInRound--;
				}
				this.findHighestBet();
			}
			everyoneCalled = true;
			for (Boolean playerCalled : playersCalled) {
				if (!playerCalled) {
					everyoneCalled = false;
				}
			}
		}
		// reset player raise state for next dealer card
		this.resetPlayers();
		return playersInRound;
	}

	public ArrayList<Player> checkWhoWins() {
		ArrayList<Player> playersThatWon = new ArrayList<>();
		for (Player player : this.currentlyPlaying) {
			player.setWinCombination(WinConditionLogic.findWinningCombination(dealer, player.getPlayerHand()));
		}
		Collections.sort(this.currentlyPlaying);
		int index = this.currentlyPlaying.size() - 1;
		Player wonForSure = this.currentlyPlaying.get(index);
		// Look for the strongest player that hasn't folded
		while (index > 0 && !wonForSure.hasNotFolded()) {
			index--;
			wonForSure = this.currentlyPlaying.get(index);
		}
		playersThatWon.add(wonForSure);
		// check for other players that haven't folded with similar strength hands
		for (Player player : this.currentlyPlaying) {
			if (player.hasNotFolded() && !playersThatWon.contains(player) && wonForSure.compareTo(player) == 0) {
				playersThatWon.add(player);
			}
		}
		return playersThatWon;
	}

	public void calculateTotalPot() {
		for (Player player : this.currentlyPlaying) {
			this.addBet(player.getBet());
		}
	}

	/**
	 * Adds a bet to the total betting pot
	 * 
	 * @param bet : a bet from a player
	 */
	public void addBet(int bet) {
		this.totalBets += bet;
	}

	/**
	 * Gives their money to the players that won. If there's a draw between n
	 * players, splits the pot n ways.
	 * 
	 * @param playersThatWon
	 */
	public void endTurn(ArrayList<Player> playersThatWon) {
		int gainSplit = playersThatWon.size();
		for (Player player : playersThatWon) {
			player.won(this.totalBets / gainSplit);
			System.out.println(player.getName() + " won " + this.totalBets / gainSplit + " with the hand "
					+ player.getWinningCombination());
		}
		for (Player player : this.currentlyPlaying) {

			if (!playersThatWon.contains(player)) {
				System.out.println(player.getName() + " lost " + player.getBet() + " with the hand "
						+ player.getWinningCombination());
				player.lost();
			}
			// reset player fold state for next turn
			player.setHasNotFolded(true);
		}
		this.totalBets = 0;
		kickBrokePlayers();
		deck.resetDeck();
		dealer.clear();
		this.switchBlinds();
		this.highestBet = 0;

	}

	public void resetPlayers() {
		for (Player player : this.currentlyPlaying) {
			player.setCurrentlyRaising(false);
		}
	}

	public void askBlindPayment() {
		this.bigBlind.getPlayer().bet(this.bigBlind.getValue());
		this.smallBlind.getPlayer().bet(this.smallBlind.getValue());
	}

	public void findHighestBet() {
		for (Player player : this.currentlyPlaying) {
			if (player.getBet() > this.highestBet) {
				this.highestBet = player.getBet();
			}
		}
	}

	public void startTurn() {
		this.giveCards();
		this.initializeBlinds();
		this.askBlindPayment();
		this.findHighestBet();
		int playersInRound = currentlyPlaying.size();
		playersInRound = this.askForBets(playersInRound);
		dealer.flop();
		dealer.printHand();
		playersInRound = this.askForBets(playersInRound);
		dealer.turn();
		dealer.printHand();
		playersInRound = this.askForBets(playersInRound);
		dealer.river();
		dealer.printHand();
		playersInRound = this.askForBets(playersInRound);
		this.calculateTotalPot();
		this.endTurn(this.checkWhoWins());

	}
}
