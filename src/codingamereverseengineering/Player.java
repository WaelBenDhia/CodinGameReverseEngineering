import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.*;
import java.io.*;
import java.math.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author wael
 */

class Solver{
    private int width;
    private int height;
    private int numberOfEntities;
    private char toTheLeft;
    private char below;
    private char toTheRight;
    private char above;
    private char[][] maze;
    //1 and 2 are ghosts, 3 and 4 powerups (?), 5 is pacman
    private int[][] entities;
    private int entityInd;
    private int incrementer;
    
    public Solver(int first, int second, int third){
        width=first;
        height=second;
        numberOfEntities=third;
        maze = new char[height][width];
        entities = new int[numberOfEntities][2];
        initCharMatrix();
    }
    
    public void setTurnData(String first, String second, String third, String fourth){
        toTheLeft = first.charAt(0);
        below = second.charAt(0);
        toTheRight = third.charAt(0);
        above = fourth.charAt(0);
        reinitCharMatrix();
    }
    
    public void insertFifthAndSixth(int y, int x){
        entities[entityInd][0] = y;
        entities[entityInd][1] = x;
        maze[y][x] = (char)('1'+entityInd%numberOfEntities);
        if(entityInd == 4){
            if(x>0) maze[y][x-1] = toTheLeft;
            if(y<height-1) maze[y+1][x] = below;
            if(x<width-1) maze[y][x+1] = toTheRight;
            if(y>0) maze[y-1][x] = above;
        }
        entityInd = (entityInd+1)%numberOfEntities;
    }
    
    public void initCharMatrix(){
        for(char[] line : maze)
            for(int i = 0; i<line.length; i++)
                line[i] = ' ';
    }
    
    public void reinitCharMatrix(){
        for(char[] line : maze)
            for(int i = 0; i<line.length; i++)
                if(line[i]>='1' && line[i]<='5' || line[i] == 'L') line[i] = '_';
    }
    
    private String charMatrixToString(){
        StringBuilder sb = new StringBuilder();
        for(char[] line : maze){
            for(char c : line)
                sb.append(c);
            sb.append('\n');
        }
        return sb.toString();
    }
    
    private String fifthsAndSixthsToString(){
        StringBuilder fifthsAndSixths = new StringBuilder();
        for(int[] line : entities){
            fifthsAndSixths.append("\n").append(line[0]).append(" ").append(line[1]);
        }
        return fifthsAndSixths.toString();
    }
    
    private int fifthAndSixthSum(){
        int sum = 0;
        for(int[] line : entities){
            sum+= line[0]+line[1];
        }
        return sum;
    }
    
    private char findDirection(int y, int x){
        List<int[]> queue = new ArrayList<>();
        //Down
        if(worthAdding(y+1, x)) queue.add(new int[]{y+1, x, 'A'});
        //Left
        if(worthAdding(y, x-1)) queue.add(new int[]{y, x-1, 'C'});
        //Right
        if(worthAdding(y, x+1)) queue.add(new int[]{y, x+1, 'D'});
        //Up
        if(worthAdding(y-1, x)) queue.add(new int[]{y-1, x, 'E'});
        while(!queue.isEmpty()){
            int[] current = queue.remove(0);
            int curX = current[1], curY = current[0];
            if(maze[curY][curX] == ' ') return (char)current[2];
            if(maze[curY][curX] != '1' && maze[curY][curX] != '3' && maze[curY][curX] != '2' && maze[curY][curX] != '4' && maze[curY][curX] != '#' && maze[curY][curX] != 'L' ){
                //Down
                if(worthAdding(curY+1, curX)) queue.add(new int[]{curY+1, curX, current[2]});
                //Left
                if(worthAdding(curY, curX-1)) queue.add(new int[]{curY, curX-1, current[2]});
                //Right
                if(worthAdding(curY, curX+1)) queue.add(new int[]{curY, curX+1, current[2]});
                //Up
                if(worthAdding(curY-1, curX)) queue.add(new int[]{curY-1, curX, current[2]});
            }
            maze[curY][curX] = 'L';
        }
        return 'F';
    }
    
    private boolean worthAdding(int y, int x){
        if(y < 0 || y >= height || x < 0 || x >= width) return false;
        if( (maze[y][x] >= '1' && maze[y][x] <= '4') ||  maze[y][x] == '#' || maze[y][x] == 'L') return false;
        if(maze[y][x] ==' '){
            if( y-1 >= 0 && maze[y-1][x] >= '1' && maze[y-1][x] <= '4') return false;
            if( y+1 < height && maze[y+1][x] >= '1' && maze[y+1][x] <= '4') return false;
            if( x-1 >= 0 && maze[y][x-1] >= '1' && maze[y][x-1] <= '4') return false;
            if( x+1 < width && maze[y][x+1] >= '1' && maze[y][x+1] <= '4') return false;
        }
        return true;
    }
    
    //Input commands: A is go down
    //Input commands: B is
    //Input commands: C is go left
    //Input commands: D is go right
    //Input commands: E is go up
    public String solve(){
        return ""+findDirection(entities[4][0], entities[4][1]);
    }
    
    @Override
    public String toString(){
        return "Maze width:"+width
                +"\nMaze height:"+height
                +"\nNumber of entites:"+numberOfEntities
                +"\nTo my left:"+toTheLeft
                +"\nBelow me:"+below
                +"\nTo my right:"+toTheRight
                +"\nAbove me:"+above
                +"\nEntity coords:"+fifthsAndSixthsToString()
                +"\nMaze:\n"+charMatrixToString();
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int firstInitInput = in.nextInt();
        int secondInitInput = in.nextInt();
        int thirdInitInput = in.nextInt();
        Solver solver = new Solver(firstInitInput, secondInitInput, thirdInitInput);
        // game loop
        while (true) {
            String firstInput = in.next();
            String secondInput = in.next();
            String thirdInput = in.next();
            String fourthInput = in.next();
            solver.setTurnData(firstInput, secondInput, thirdInput, fourthInput);
            for (int i = 0; i < thirdInitInput; i++) {
                int fifthInput = in.nextInt();
                int sixthInput = in.nextInt();
                solver.insertFifthAndSixth(fifthInput, sixthInput);
            }
            
            System.err.println(solver.toString());

            System.out.println(solver.solve());
        }
    }
}