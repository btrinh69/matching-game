//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           MatchingGame
// Files:           MatchingGame
// Course:          CS300 LEC001, Fall, 2019
//
// Author:          Binh Quoc Trinh (Bon)
// Email:           btrinh@wisc.edu
// Lecturer's Name: Gary Dahl
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:
// Partner Email:
// Partner Lecturer's Name:
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here.  Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do.  If you received no outside help from either type
//  of source, then please explicitly indicate NONE.
//
// Persons:
// Online Sources:
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.File;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class MatchingGame {
	// Congratulations message
	private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
	// Cards not matched message
	private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
	// Cards matched message
	private final static String MATCHED = "CARDS MATCHED! Good Job!";
	// 2D-array which stores cards coordinates on the window display
	private final static float[][] CARDS_COORDINATES =
	new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170},
	{170, 324}, {324, 324}, {478, 324}, {632, 324},
	{170, 478}, {324, 478}, {478, 478}, {632, 478}};
	// Array that stores the card images filenames
	private final static String[] CARD_IMAGES_NAMES = new String[] {"apple.png",
	"ball.png", "peach.png", "redFlower.png", "shark.png", "yellowFlower.png"};

	private static PApplet processing; 	// PApplet object that represents
					// the graphic display window
	private static Card[] cards; // one dimensional array of cards
	private static PImage[] images; // array of images of the different cards
	private static Random randGen; // generator of random numbers
	private static Card selectedCard1; // First selected card
	private static Card selectedCard2; // Second selected card
	private static boolean winner; 	// boolean evaluated true if the game is won,
									// and false otherwise
	private static int matchedCardsCount; // number of cards matched so far
					// in one session of the game
	private static String message; // Displayed message to the display window

	public static void main(String[] args) {
		Utility.runApplication();

	}

	/**
	* Defines the initial environment properties of this game as the program starts
	*/
	public static void setup(PApplet processing) {
		MatchingGame.processing = processing;
		images = new PImage[CARD_IMAGES_NAMES.length];
		for (int i = 0; i < CARD_IMAGES_NAMES.length; i++) {
			images[i] = processing.loadImage("images" + File.separator
					+ CARD_IMAGES_NAMES[i]);
		}
		initGame();
	}

	/**
	* Initializes the Game
	*/
	public static void initGame() {
		randGen = new Random(Utility.getSeed());
		selectedCard1 = null;
		selectedCard2 = null;
		matchedCardsCount = 0;
		winner = false;
		message = "";
		cards = new Card[CARD_IMAGES_NAMES.length*2];
		int[] check = new int[CARDS_COORDINATES.length];
		int x;
		for (int i = 0; i < CARD_IMAGES_NAMES.length; i++) {
			x = genRand(check, (i*2));
			cards[i*2] = new Card(images[i],
					CARDS_COORDINATES[x][0],
					CARDS_COORDINATES[x][1]);
			x = genRand(check, (i*2+1));
			cards[i*2+1] = new Card(images[i],
					CARDS_COORDINATES[x][0],
					CARDS_COORDINATES[x][1]);

		}
	}

	/**
	* Helper method to generate random number
	* @param an array to store generated value and the index of
	* images
	* @return a random number within the index of cards that is
	* not equal to any random number generated
	*/
	public static int genRand(int[] check, int i) {
		int rand = randGen.nextInt(CARDS_COORDINATES.length);
		int temp = 0;
		while (temp < i) {
			if (rand == check[temp]) {
				rand = randGen.nextInt(CARDS_COORDINATES.length);
				temp = 0;
			}
			else temp++;
		}
		check[i] = rand;
		return rand;
	}

	/**
	* Callback method called each time the user presses a key
	*/
	public static void keyPressed() {
		if (processing.key == 'n' || processing.key == 'N') {
			initGame();
		}
	}

	/**
	* Callback method draws continuously this application window display
	*/
	public static void draw() {
		processing.background(245, 255, 250);// Mint cream color
		for (int i = 0; i < cards.length; i++) {
			cards[i].draw();
		}
		displayMessage(message);
	}

	/**
	* Displays a given message to the display window
	* @param message to be displayed to the display window
	*/
	public static void displayMessage(String message) {
		processing.fill(0);
		processing.textSize(20);
		processing.text(message, processing.width / 2, 50);
		processing.textSize(12);
	}

	/**
	* Checks whether the mouse is over a given Card
	* @return true if the mouse is over the storage list, false otherwise
	*/
	public static boolean isMouseOver(Card card) {
		if ((processing.mouseX <= (card.getX() + card.getWidth()/2))
				&& (processing.mouseX >= (card.getX() - card.getWidth()/2))
				&& (processing.mouseY >= (card.getY() - card.getHeight()/2))
				&& (processing.mouseY <= (card.getY() + card.getHeight()/2))) return true;
		else return false;
	}

	/**
	* Callback method called each time the user presses the mouse
	*/
	public static void mousePressed() {
		if (winner == false) { //mouse do nothing if the player has won the game
			if (selectedCard2 != null) { //only check after 2 cards have been selected
				if (!matchingCards(selectedCard1, selectedCard2)) {
					selectedCard2.setVisible(false);
					selectedCard1.setVisible(false);
				}
				selectedCard1.deselect();
				selectedCard2.deselect();
				selectedCard1 = null;	//reset cards to null to obtain new cards
				selectedCard2 = null;
			}
			if (selectedCard1 == null) { //refer the first cards to selecedCards1
				for (int i = 0; i < cards.length; i++) {
					if (isMouseOver(cards[i])) {
						if (!cards[i].isVisible()) { //do nothing if card is matched
							cards[i].setVisible(true);
							selectedCard1 = cards[i];
							selectedCard1.select();
							break;
						}
					}
				}
			}
			else { //if cards 1 picked, refer the second cards to selectedCards2
				for (int i = 0; i < cards.length; i++) {
					if (isMouseOver(cards[i])) {
						if (!cards[i].isVisible()) {
							selectedCard2 = cards[i];
							selectedCard2.setVisible(true);
							selectedCard2.select();
							break;
						}
					}
				}
			}
			if (selectedCard2 != null) {
				if (!matchingCards(selectedCard1, selectedCard2)) {
					message = NOT_MATCHED;
				}
				else {
					selectedCard2.setVisible(true);
					selectedCard1.setVisible(true);
					matchedCardsCount++;
					if (matchedCardsCount == 6) {
						winner = true;
						message = CONGRA_MSG;
						selectedCard1.deselect();
						selectedCard2.deselect();
					} else {
						message = MATCHED;
					}
				}
			}
		}
	}

	/**
	* Checks whether two cards match or not
	* @param card1 reference to the first card
	* @param card2 reference to the second card
	* @return true if card1 and card2 image references are the same, false otherwise
	*/
	public static boolean matchingCards(Card card1, Card card2) {
		if (card1.getImage().equals(card2.getImage())) return true;
		else return false;
	}




}
