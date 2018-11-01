import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {

	int width = 800, height = 800 - 50; 	//Dimensions of Panel (50px reserved for histogram)
	final int cellSize = 5; //Size of each cell in pixels
	
	int w, h; //Dimensions of cell grid (will be calculated)

	int[][] cells; //Cell grid

	//Color arrays
	Color[] colors = { Color.BLACK, Color.RED, new Color(255, 204, 0), Color.BLUE, Color.GREEN, Color.GRAY,
			Color.MAGENTA };
	Color[] sdc1 = { new Color(230, 25, 75), new Color(60, 180, 75), new Color(255, 225, 25), new Color(0, 130, 200),
			new Color(245, 130, 48), new Color(145, 30, 180), new Color(70, 240, 240), new Color(240, 50, 230),
			new Color(210, 245, 60), new Color(250, 190, 190), new Color(0, 128, 128), new Color(230, 190, 255),
			new Color(170, 110, 40), new Color(255, 250, 200), new Color(128, 0, 0), new Color(170, 255, 195),
			new Color(128, 128, 0), new Color(255, 215, 180), new Color(0, 0, 128), new Color(128, 128, 128),
			new Color(255, 255, 255), new Color(0, 0, 0) };

	Color[] sdc2 = { new Color(230, 25, 75), new Color(60, 180, 75), new Color(255, 225, 25), new Color(0, 130, 200),
			new Color(245, 130, 48), new Color(145, 30, 180), new Color(70, 240, 240), new Color(240, 50, 230),
			new Color(210, 245, 60), new Color(250, 190, 190), new Color(0, 128, 128), new Color(230, 190, 255),
			new Color(170, 110, 40), new Color(255, 250, 200), new Color(128, 0, 0), new Color(170, 255, 195),
			new Color(128, 128, 0), new Color(255, 215, 180), new Color(0, 0, 128), new Color(128, 128, 128),
			new Color(255, 255, 255), new Color (0, 0, 0)};

	int states = 7; //Number of different states (colors)
	int predatorMinimum = 3; //Minimum number of predators needed to attack a cell
	int randomPredatorMinimum = 3; //Additional threshold added to the minimum for more random results

	int delay = 33; //Delay beetween steps in ms

	public Panel() {
		//Calculate dimensions
		w = width / cellSize;
		h = height / cellSize;
		width = w * cellSize;
		height = h * cellSize;
		setPreferredSize(new Dimension(width, height + 50));

		//Initialize cell grid with random states
		cells = new int[h][w];
		randomInitialCondition();

		//Leave this line commented out for regular colors or change it to "colors = sdc1;" or "colors = sdc2;" for other colors
		//colors = sdc2;
	}

	//Update loop: Do a step, repaint, wait
	public void update() {
		while (true) {
			step();
			repaint();

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//Calculate next step of each cell
	public void step() {
		int[][] newCells = cells;
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				
				int predatorStates = (states - 0) / 2; //Standard: Make half (rounded down) of the colors predators to this color
				int[] predators = new int[predatorStates];
				int[] predatorState = new int[predatorStates];
				int gesamtPredators = 0;

				//Count number if neighbour predators for each predator state
				for (int k = 0; k < predatorStates; k++) {
					predatorState[k] = (cells[i][j] + 1 + k) % states;
					predators[k] = countNeighbours(j, i, predatorState[k]);
					gesamtPredators += predators[k];
				}
				
				int randomMinimum = (int) (Math.random() * randomPredatorMinimum);
				
				//If there are more neighbour predators than the threshold, change current cell to a random predator cell (weighted)
				if (gesamtPredators >= predatorMinimum + randomMinimum) {
					int r = (int) (Math.random() * gesamtPredators);
					int k = -1;
					while (r >= 0) {
						k++;
						r -= predators[k];
					}
					newCells[i][j] = predatorState[k];
				}
				
			}

		}
		
		cells = newCells;
	}

	
	//Returns number of neighbours with specific state
	public int countNeighbours(int x, int y, int state) {
		int c = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i != 1 || j != 1) {
					if (getCellState(x + j - 1, y + i - 1) == state) {
						c++;
					}
				}
			}
		}
		return c;
	}

	//Counts number of cells in grid with specific state (for Histogram)
	public int countCells(int state) {
		int c = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (getCellState(j, i) == state) {
					c++;
				}

			}
		}
		return c;
	}

	//Get cell state of a cell in the grid
	public int getCellState(int x, int y) {
		return cells[(y + h) % h][(x + w) % w];
	}

	//Randomly choose a state for each cell
	public void randomInitialCondition() {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cells[i][j] = (int) (Math.random() * states);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		//Draw each cell
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				g.setColor(colors[cells[i][j]]);
				g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
			}
		}

		//Draw Histogram
		int totalWidth = 0;
		for (int i = 0; i < states; i++) {
			int numberOfCells = countCells(i);
			double percentage = numberOfCells / (w * h * 1.0);
			int partWidth = (int) (percentage * width);
			g.setColor(colors[i]);
			g.fillRect(totalWidth, h * cellSize, partWidth + 1, 50);
			totalWidth += partWidth;
		}

	}
}
