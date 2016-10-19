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
        return null;
    }
}

class Pacman{
    int x, y;
    int dirX, dirY;
    
    
    public Pacman(){
        
    }
}

class GameState{
    private char[][] maze;
    private int[][] entities;
    private int entityInd;
    char[] pacmanSurroundings;
    
    public GameState(){
        maze = new char[Solver.WIDTH][Solver.HEIGHT];
        entities = new int[5][4];
        for(int[] entity : entities)
            Arrays.fill(entity, -1);
        for(char[] column : maze)
            Arrays.fill(column, ' ');
    }
    
    public void insertEntity(int x, int y){
        int[] entity = entities[entityInd];
        if(entity[2] == -1 && entity[3] == -1){
            entity[2] = 0;
            entity[3] = 0;
        }else{
            entity[2] = entity[0] - x;
            entity[2] = entity[2] > 1 ? -1 : entity[2] < -1 ? 1 : entity[2];
            entity[3] = entity[1] - y;
            entity[3] = entity[3] > 1 ? -1 : entity[3] < -1 ? 1 : entity[3];
        }
        if(entityInd == 4){
            maze[Solver.clampWidth(x-1)][y] = pacmanSurroundings[0];
            maze[x][Solver.clampHeight(y+1)] = pacmanSurroundings[1];
            maze[Solver.clampWidth(x+1)][y] = pacmanSurroundings[2];
            maze[x][Solver.clampHeight(y-1)] = pacmanSurroundings[3];
        }
        maze[entity[0]][entity[1]] = '_';
        maze[x][y] = '_';
        entity[0] = x;
        entity[1] = y;
        entityInd = (entityInd+1)%entities.length;
    }
    
    public void insertPacmanSurroundings(char toTheLeft, char below, char toTheRight, char above){
        pacmanSurroundings[0] = toTheLeft;
        pacmanSurroundings[1] = below;
        pacmanSurroundings[2] = toTheRight;
        pacmanSurroundings[3] = above;
    }
    
    /*
      E
    C B D
      A
    */
    public char[] getPossibleMoves(int e){
        int numberOfPossibleMoves = 0;
        char[] moves;
        int[] entity = entities[e];
        if (e != 4 
                && entity[2] != 0
                && maze[entity[0]][Solver.clampHeight(entity[1] + 1)] == '#'
                && maze[entity[0]][Solver.clampHeight(entity[1] - 1)] == '#'
                && maze[Solver.clampWidth(entity[0] + entity[2])][entity[1]] != '#') {
            moves = new char[]{entity[2] == -1 ? 'C' : 'D'};
        } else if (e != 4 
                && entity[3] != 0
                && maze[Solver.clampWidth(entity[0] + 1)][entity[1]] == '#'
                && maze[Solver.clampWidth(entity[0] - 1)][entity[1]] == '#'
                && maze[entity[0]][Solver.clampHeight(entity[1] + entity[3])] != '#') {
            moves = new char[]{entity[3] == -1 ? 'C' : 'D'};
        } else {
            numberOfPossibleMoves++;
            boolean canRight = isClear(Solver.clampWidth(entity[0]+1), entity[1], e == 4);
            if(canRight) numberOfPossibleMoves++;
            boolean canLeft = isClear(Solver.clampWidth(entity[0]-1), entity[1], e == 4);
            if(canLeft) numberOfPossibleMoves++;
            boolean canUp = isClear(entity[0], Solver.clampWidth(entity[1]-1), e == 4);
            if(canUp) numberOfPossibleMoves++;
            boolean canDown = isClear(entity[0], Solver.clampWidth(entity[1]+1), e == 4);
            if(canDown) numberOfPossibleMoves++;
            moves = new char[numberOfPossibleMoves];
            moves[0] = 'B';
            numberOfPossibleMoves++;
            if(canDown){
                moves[numberOfPossibleMoves] = 'A';
                numberOfPossibleMoves++;
            }
            if(canLeft){
                moves[numberOfPossibleMoves] = 'C';
                numberOfPossibleMoves++;
            }
            if(canRight){
                moves[numberOfPossibleMoves] = 'D';
                numberOfPossibleMoves++;
            }
            if(canUp){
                moves[numberOfPossibleMoves] = 'E';
                numberOfPossibleMoves++;
            }
        }
        return moves;
    }
    
    public void move(int e, char move) throws Exception{
        int[] entity = entities[e];
        int[] newCoords = new int[]{
            move == 'B' || move == 'E' || move == 'A' ? entity[0] : move == 'C' ? Solver.clampWidth(entity[0]-1) : Solver.clampWidth(entity[0]+1), 
            move == 'B' || move == 'C' || move == 'D' ? entity[1] : move == 'E' ? Solver.clampHeight(entity[1]-1) : Solver.clampHeight(entity[1]-1)
        };
        if(!isClear(newCoords[0], newCoords[1], e == 4))
            throw  new Exception("Tried to move entity " + e + " into block " + newCoords[0] + " " + newCoords[1] + " of type " + maze[newCoords[0]] + maze[newCoords[1]]);
        else{
            entity[0] = newCoords[0];
            entity[1] = newCoords[1];
        }
    }
    
    private boolean isClear(int x, int y, boolean isPacman){
        boolean clear = maze[x][y] != '#';
        for(int i = 0; i < 4 && clear && isPacman; i++){
            clear = x != entities[i][0] && y != entities[i][1];
        }
        return clear;
    }
    
    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for(int x=0; x<maze.length; x++){
            for(int y=0; y<maze[x].length; y++)
                output.append(maze[x][y] == '_' ? '.' : maze[x][y]);
            output.append('\n');
        }
        for(int i = 0; i<entities.length; i++){
            int indexInOutput = entities[i][0] + entities[i][1]*maze.length;
            output.deleteCharAt(indexInOutput);
            output.insert(indexInOutput, i == 4 ? 'o' : 'O');
        }
        return output.toString();
    }
}

class Solver{
    public static int WIDTH;
    public static int HEIGHT;
    private int numberOfEntities;
    private char toTheLeft;
    private char below;
    private char toTheRight;
    private char above;
    private char[][] maze;
    //1, 2, 3 and 4 are ghosts, 5 is pacman
    private int[][] entities;
    private int entityInd;
    
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