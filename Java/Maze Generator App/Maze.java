import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
//Phillip amanya
public class Maze {
    private static final byte  OPEN = 0;
    private static final byte  WALL = 1;
    private static final byte  VISITED = 2;
    
    private int         rows, columns;
    private byte[][]    grid;
    
    // A constructor that makes a maze of the given size
    public Maze(int r, int c) {
        rows = r;
        columns = c;
        grid = new byte[r][c];
        for(r=0; r<rows; r++) {
            for (c = 0; c<columns; c++) {
                grid[r][c] = WALL;
            }
        }
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    // Return true if a wall is at the given location, otherwise false
    public boolean wallAt(int r, int c) {
        return grid[r][c] == WALL;
    }

    // Return true if this location has been visited, otherwise false
    public boolean visitedAt(int r, int c) {
        return grid[r][c] == VISITED;
    }

    // Put a visit marker at the given location
    public void placeVisitAt(int r, int c) {
        grid[r][c] = VISITED;
    }
    
    // Remove a visit marker from the given location
    public void removeVisitAt(int r, int c) {
        grid[r][c] = OPEN;
    }
    
    // Put a wall at the given location
    public void placeWallAt(int r, int c) {
        grid[r][c] = WALL;
    }
    
    // Remove a wall from the given location
    public void removeWallAt(int r, int c) {
        grid[r][c] = 0;
    }
    
    // Carve out a maze
    public void carve() {
        int startRow = (int)(Math.random()*(rows-2))+1;
        int startCol = (int)(Math.random()*(columns-2))+1;
        carve(startRow, startCol);
    }
    
    // Directly recursive method to carve out the maze
    public void carve(int r, int c) {
        ArrayList<Integer> rowOffsets = new ArrayList<Integer>(Arrays.asList(-1, 1, 0, 0));
        ArrayList<Integer> colOffsets = new ArrayList<Integer>(Arrays.asList(0, 0, -1, 1));
        if (r==0 || r==rows-1 ||c==0||c==columns-1){
            return;
        }else if (wallAt(r,c)) {
            int count=0;
            if (wallAt(r+1,c)){count++;}
            if (wallAt(r-1,c)){count++;}
            if (wallAt(r,c+1)){count++;}
            if (wallAt(r,c-1)){count++;}
            if (count>=3) {
                removeWallAt(r, c);
                int random = new Random().nextInt(4);

                if (random == 0) {
                    carve(r + rowOffsets.get(0), c + colOffsets.get(0));
                    carve(r + rowOffsets.get(1), c + colOffsets.get(1));
                    carve(r + rowOffsets.get(2), c + colOffsets.get(2));
                    carve(r + rowOffsets.get(3), c + colOffsets.get(3));
                } else if (random == 1) {
                    carve(r + rowOffsets.get(1), c + colOffsets.get(1));
                    carve(r + rowOffsets.get(2), c + colOffsets.get(2));
                    carve(r + rowOffsets.get(3), c + colOffsets.get(3));
                    carve(r + rowOffsets.get(0), c + colOffsets.get(0));
                } else if (random == 2) {
                    carve(r + rowOffsets.get(2), c + colOffsets.get(2));
                    carve(r + rowOffsets.get(3), c + colOffsets.get(3));
                    carve(r + rowOffsets.get(0), c + colOffsets.get(0));
                    carve(r + rowOffsets.get(1), c + colOffsets.get(1));
                } else if (random == 3) {
                    carve(r + rowOffsets.get(3), c + colOffsets.get(3));
                    carve(r + rowOffsets.get(0), c + colOffsets.get(0));
                    carve(r + rowOffsets.get(1), c + colOffsets.get(1));
                    carve(r + rowOffsets.get(2), c + colOffsets.get(2));
                }
            }
        }else{
            return;
        }
    }
    
    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPath() {
       int highest=0;
        ArrayList<Point2D> retr=new ArrayList<>();
       for (int i=0;i<2;i++){
           for (int r=0;r<rows;r++){
               for(int c=0;c<columns;c++){
                   if (i==0){
                      int temp=longestPathFrom(r,c).size();
                      if (temp>highest) {
                          highest = temp;
                      }
                   }else{
                       if (highest==longestPathFrom(r,c).size()){
                           retr=longestPathFrom(r,c);
                       }
                   }
               }
           }
       }
       return retr;
    }
    
    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPathFrom(int r, int c) {
        ArrayList<Point2D> path = new ArrayList<Point2D>();
        if (wallAt(r,c)|| visitedAt(r,c)){
            return path;
        }else{
            placeVisitAt(r,c);
            ArrayList<Point2D> temp1=longestPathFrom(r-1,c);
            ArrayList<Point2D> temp2=longestPathFrom(r+1,c);
            ArrayList<Point2D> temp3=longestPathFrom(r,c-1);
            ArrayList<Point2D> temp4=longestPathFrom(r,c+1);

            int MAxlength=Math.max(temp1.size(),Math.max(temp2.size(),Math.max(temp3.size(),temp4.size())));

            if (MAxlength==temp1.size()){temp1.add(new Point2D(r,c));removeVisitAt(r,c);return temp1;}

            if (MAxlength==temp2.size()){temp2.add(new Point2D(r,c));removeVisitAt(r,c);return temp2;}

            if (MAxlength==temp3.size()){temp3.add(new Point2D(r,c));removeVisitAt(r,c);return temp3;}

            if (MAxlength==temp4.size()){temp4.add(new Point2D(r,c));removeVisitAt(r,c);return temp4;}
        }
        return path;
    }
}

