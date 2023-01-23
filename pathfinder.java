
import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Square extends JPanel implements MouseInputListener, MouseMotionListener {
    int size, x, y, id;
    boolean clicked;
    int f, g, h;
    int r, gr, b;
    int rc, grc, bc;
    boolean wall;
    Square parent;

    public Square(int s, int x, int y, int id) {
        this.size = s;
        this.x = x;
        this.y = y;
        this.id = id;
        clicked = false;
        f = 0;
        g = 0;
        h = 0;
        r = gr = b = 192;
        rc = bc = 0;
        grc = 255;
        wall = false;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clicked) {
            g.setColor(new Color(rc, grc, bc));
        } else {
            g.setColor(new Color(r, gr, b));
        }
        g.fillRect(x, y, size, size);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicked = !clicked;

        if (pathfinder.startnode == -1) {
            pathfinder.startnode = id;
            System.out.println("1");
        } else if (pathfinder.goalnode == -1) {
            if (pathfinder.startnode == id) {
                pathfinder.startnode = -1;
            } else {
                pathfinder.goalnode = id;
            }
            System.out.println("2");
        } else if (pathfinder.startnode == id) {
            System.out.println("3");
            pathfinder.startnode = pathfinder.goalnode;
            pathfinder.goalnode = -1;
        } else if (pathfinder.goalnode == id) {
            System.out.println("4");
            pathfinder.goalnode = -1;
        } else {
            System.out.println("5");
            wall = !wall;
            rc = 0;
            grc = 0;
            bc = 0;
        }
        this.repaint();

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public int getId() {
        return id;
    }
}

public class pathfinder {
    static final int Window_width = 800;
    static final int Window_height = 800;
    static final int block_size = 5;
    static final int margin = 8;
    static int nblocks = (Window_height / (block_size + margin)) * (Window_width / (block_size + margin));
    static int numRows = (Window_width / (block_size + margin));
    static int numCols = (Window_height / (block_size + margin));
    static ArrayList<Square> squares;
    public static int startnode = -1, goalnode = -1;
    public static int clickCounter = 0;

    public static ArrayList<Square> getSuccessors(Square q, ArrayList<Square> squares, int numCols) {
        ArrayList<Square> successors = new ArrayList<>();
        int id = q.getId();
        int row = id / numCols;
        int col = id % numCols;
        // Check top left
        if (row > 0 && col > 0) {
            int topLeftId = (row - 1) * numCols + (col - 1);
            Square d = squares.get(topLeftId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check top
        if (row > 0) {
            int topId = (row - 1) * numCols + col;
            Square d = squares.get(topId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check top right
        if (row > 0 && col < numCols - 1) {
            int topRightId = (row - 1) * numCols + (col + 1);
            Square d = squares.get(topRightId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check left
        if (col > 0) {
            int leftId = row * numCols + (col - 1);
            Square d = squares.get(leftId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check right
        if (col < numCols - 1) {
            int rightId = row * numCols + (col + 1);
            Square d = squares.get(rightId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check bottom left
        if (row < squares.size() / numCols - 1 && col > 0) {
            int bottomLeftId = (row + 1) * numCols + (col - 1);
            Square d = squares.get(bottomLeftId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check bottom
        if (row < squares.size() / numCols - 1) {
            int bottomId = (row + 1) * numCols + col;
            Square d = squares.get(bottomId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        // Check bottom right
        if (row < squares.size() / numCols - 1 && col < numCols - 1) {
            int bottomRightId = (row + 1) * numCols + (col + 1);
            Square d = squares.get(bottomRightId);
            if (!d.wall) {
                d.parent = q;
                successors.add(d);
            }
        }
        return successors;
    }

    public static int manhattanDistance(Square q, Square goal, int numCols) {
        int qRow = q.getId() / numCols;
        int qCol = q.getId() % numCols;
        int goalRow = goal.getId() / numCols;
        int goalCol = goal.getId() % numCols;
        return Math.abs(qRow - goalRow) + Math.abs(qCol - goalCol);
    }

    public static ArrayList<Square> AStar(ArrayList<Square> squares, Square start, Square goal, int numCols) {
        // Initialize the open and closed lists
        ArrayList<Square> open = new ArrayList<Square>();
        ArrayList<Square> closed = new ArrayList<Square>();
        // Add the start square to the open list
        open.add(start);
        // Start the loop
        while (!open.isEmpty()) {
            // Find the square with the lowest f value in the open list
            Square q = null;
            int minf = Integer.MAX_VALUE;
            for (Square square : open) {
                if (square.f < minf) {
                    minf = square.f;
                    q = square;
                }
            }
            // Remove q from the open list and add it to the closed list
            open.remove(q);
            closed.add(q);
            // If q is the goal square, return the closed list
            if (q == goal) {
                return closed;
            }
            // Get the successors of q
            ArrayList<Square> successors = getSuccessors(q, squares, numCols);
            // For each successor
            for (Square successor : successors) {
                if (successor.wall)
                    continue;
                // If the successor is already in the closed list, ignore it
                if (closed.contains(successor)) {
                    continue;
                }
                // Calculate the g value of the successor
                int g = q.g + 1; // assume a cost of 1 for each movement
                // If the successor is not in the open list, or the new g value is better than
                // the previous one
                if (!open.contains(successor) || g < successor.g) {
                    // Update the f, g and h values of the successor
                    successor.g = g;
                    successor.h = manhattanDistance(successor, goal, numCols);
                    successor.f = successor.g + successor.h;
                    // Set the parent of the successor to q
                    successor.parent = q;
                    // If the successor is not in the open list, add it
                    if (!open.contains(successor)) {
                        open.add(successor);
                    }
                }
            }

        }
        // If the open list is empty and the goal square is not reached, return an empty
        // list
        return new ArrayList<Square>();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pathfinder");
        frame.setLayout(
                new GridLayout(numRows, numCols));
        frame.setSize(Window_width, Window_height);
        squares = new ArrayList<Square>();
        for (int i = 0; i < nblocks; i++) {
            Square square = new Square(block_size * 2, 0, 0, i);
            squares.add(square);
            frame.add(square);
            frame.addMouseListener(square);
        }
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArrayList<Square> previousPath = new ArrayList<Square>();
        while (true) {

            if ((startnode != -1) && (goalnode != -1)) {
                ArrayList<Square> path = AStar(squares, squares.get(startnode), squares.get(goalnode), numCols);
                // Clear previous path
                if (!path.equals(previousPath)) {
                    for (Square square : previousPath) {
                        square.r = 192;
                        square.gr = 192;
                        square.b = 192;
                        square.repaint();
                    }
                    previousPath = path;
                }
                // Paint new path
                for (Square square : path) {
                    square.r = 255;
                    square.gr = 0;
                    square.b = 0;
                    square.repaint();
                }
            } else {
                for (Square square : squares) {
                    square.r = 192;
                    square.gr = 192;
                    square.b = 192;
                    square.repaint();
                }
            }
        }
    }
}
