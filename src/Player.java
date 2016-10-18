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

class Enemy{
    private int id;
    int x, y;
    int dirX, dirY;
    private Solver solver;
    
    public Enemy(int id, int x, int y, Solver solver){
        this.x = x;
        this.y = y;
        this.solver = solver;
    }
    
    public void setCoord(int x, int y){
        dirX = x - this.x;
        dirX = dirX < -1 ? 1 : dirX > 1 ? -1 : dirX;
        dirY = y - this.y;
        dirY = dirY < -1 ? 1 : dirY > 1 ? -1 : dirY;
        this.x = x;
        this.y = y;
    }
    
    public int[] coordsInNTurns(int N){
        int[] newCoords = new int[]{-1, -1};
        //Went left
        if(dirX == -1){
            return null;
        }
        //Went right
        if(dirX == 1){
            return null;
        }
        //Went up
        if(dirY == -1){
            return null;
        }
        //Went down
        if(dirY == 1){
            return null;
        }
        return newCoords;
    }
    /**
     *   E
     * C B D
     *   A
     */
    public char[] possibleDirections(){
        //See if we maintainDirection;
        if(dirX!=0 
                && solver.getMaze()[Solver.clampHeight(y+1)][x] == '#' 
                && solver.getMaze()[Solver.clampHeight(y-1)][x] == '#' 
                && solver.getMaze()[y][Solver.clampWidth(x+dirX)] != '#')
            return new char[]{dirX == -1 ? 'C' : 'D'};
        if(dirY!=0 
                && solver.getMaze()[y][Solver.clampWidth(x+1)] == '#' 
                && solver.getMaze()[y][Solver.clampWidth(x-1)] == '#' 
                && solver.getMaze()[Solver.clampHeight(y+dirY)][x] != '#')
            return new char[]{dirY == -1 ? 'C' : 'D'};
        char[] possibleDirections;
        int numberOfPossibleDirections;
        //COMPLETE FROM HERE
        return possibleDirections;
    }
}

class Pacman{
    int x, y;
    int dirX, dirY;
    
    
    public Pacman(){
        
    }
}

class Solver{
    private static int WIDTH;
    private static int HEIGHT;
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
        WIDTH=first;
        HEIGHT=second;
        numberOfEntities=third;
        maze = new char[HEIGHT][WIDTH];
        entities = new int[numberOfEntities][2];
        initCharMatrix();
    }
    
    public char[][] getMaze(){
        return maze;
    }
    
    public static int clampWidth(int x){
        return x < 0 ? WIDTH - 1 : x % WIDTH;
    }
    
    public static int clampHeight(int y){
        return y < 0 ? WIDTH - 1 : y % HEIGHT;
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
            if(maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] != '1' && maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] != '2' && maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] != '3' && maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] != '4')
                maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] = toTheLeft;
            if(maze[(y+1)% HEIGHT][x] != '1' && maze[(y+1)% HEIGHT][x] != '2' && maze[(y+1)% HEIGHT][x] != '3' && maze[(y+1)% HEIGHT][x] != '4')
                maze[(y+1)% HEIGHT][x] = below;
            if(maze[y][(x+1)% WIDTH] != '1' && maze[y][(x+1)% WIDTH] != '2' && maze[y][(x+1)% WIDTH] != '3' && maze[y][(x+1)% WIDTH] != '4')
                maze[y][(x+1)% WIDTH] = toTheRight;
            if(maze[y-1 < 0 ? HEIGHT-1 : y-1][x] != '1' && maze[y-1 < 0 ? HEIGHT-1 : y-1][x] != '2' && maze[y-1 < 0 ? HEIGHT-1 : y-1][x] != '3' && maze[y-1 < 0 ? HEIGHT-1 : y-1][x] != '4')
                maze[y-1 < 0 ? HEIGHT-1 : y-1][x] = above;
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
                sb.append(c == '#' ? '@' : c == '_' ? '.' : c == '5' ? 'o' : c == '1' || c == '2' || c == '3' || c == '4' ? 'O' : c);
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
        if(worthAdding((y+1)%HEIGHT, x)) queue.add(new int[]{(y+1)%HEIGHT, x, 'A'});
        //Left
        if(worthAdding(y, x-1 < 0 ? WIDTH-1 : x-1)) queue.add(new int[]{y, (x-1 < 0 ? WIDTH-1 : x-1), 'C'});
        //Right
        if(worthAdding(y, (x+1)%WIDTH)) queue.add(new int[]{y, (x+1)%WIDTH, 'D'});
        //Up
        if(worthAdding(y-1 < 0 ? HEIGHT-1 : y-1, x)) queue.add(new int[]{(y-1 < 0 ? HEIGHT-1 : y-1), x, 'E'});
        while(!queue.isEmpty()){
            int[] current = queue.remove(0);
            int curX = current[1], curY = current[0];
            if(maze[curY][curX] == ' ') return (char)current[2];
            if(maze[curY][curX] != '1' && maze[curY][curX] != '3' && maze[curY][curX] != '2' && maze[curY][curX] != '4' && maze[curY][curX] != '#' && maze[curY][curX] != 'L' ){
                //Down
                if(worthAdding((curY+1)%HEIGHT, curX)) queue.add(new int[]{(curY+1)%HEIGHT, curX, current[2]});
                //Left
                if(worthAdding(curY, curX-1 < 0 ? WIDTH - 1 : curX-1)) queue.add(new int[]{curY, curX-1 < 0 ? WIDTH - 1 : curX-1, current[2]});
                //Right
                if(worthAdding(curY, (curX+1)%WIDTH)) queue.add(new int[]{curY, (curX+1)%WIDTH, current[2]});
                //Up
                if(worthAdding(curY-1 < 0 ? HEIGHT - 1 : curY - 1 , curX)) queue.add(new int[]{curY - 1 < 0 ? HEIGHT - 1 : curY - 1, curX, current[2]});
            }
            maze[curY][curX] = 'L';
        }
        //All dead ends, desperate times call for desperate measures
        if(maze[(y+1)%HEIGHT][x] == 'L' || maze[(y+1)%HEIGHT][x] == '_') return 'A';
        if(maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == 'L' || maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == '_') return 'E';
        if(maze[y][(x+1)%WIDTH] == 'L' || maze[y][(x+1)%WIDTH] == '_') return 'D';
        if(maze[y][x-1 < 0 ? WIDTH-1 : x-1] == 'L' || maze[y][x-1 < 0 ? WIDTH-1 : x-1] == '_') return 'C';
        return 'B';
    }
    
    private boolean worthAdding(int y, int x){
        if(y < 0 || y >= HEIGHT || x < 0 || x >= WIDTH) return false;
        if( (maze[y][x] >= '1' && maze[y][x] <= '4') ||  maze[y][x] == '#' || maze[y][x] == 'L') return false;
        if(maze[y][x] == ' ' || maze[y][x] == '_'){
            if( maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == '1' || maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == '2' || maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == '3' || maze[y-1 < 0 ? HEIGHT-1 : y-1][x] == '4') return false;
            if( maze[(y+1)%HEIGHT][x] == '1' || maze[(y+1)%HEIGHT][x] == '2' || maze[(y+1)%HEIGHT][x] == '3' || maze[(y+1)%HEIGHT][x] == '4') return false;
            if( maze[y][x-1 < 0 ? WIDTH-1 : x-1] == '1' || maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] == '2' || maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] == '3' || maze[y][x-1 < 0 ? WIDTH - 1 : x - 1] == '4') return false;
            if( maze[y][(x+1)%WIDTH] == '1' || maze[y][(x+1)%WIDTH] == '2' || maze[y][(x+1)%WIDTH] == '3' || maze[y][(x+1)%WIDTH] == '4') return false;
        }
        return true;
    }
    
    /**
     *   E
     * C B D
     *   A
     */
    //Input commands: A is go down
    //Input commands: B is wait
    //Input commands: C is go left
    //Input commands: D is go right
    //Input commands: E is go up
    public String solve(){
        return ""+findDirection(entities[4][0], entities[4][1]);
    }
    
    @Override
    public String toString(){
        return "Maze width:"+WIDTH
                +"\nMaze height:"+HEIGHT
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