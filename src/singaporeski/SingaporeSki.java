/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singaporeski;

//import java.lang.Math;
import java.util.*;

/**
 *
 * @author chunyap
 */
public class SingaporeSki {
    
    private NavigateObject[][] cacheMaxRoute;
    
    /**
     *
     * @return NavigateObject
     */
    public NavigateObject getMaxRoute(int[][] map) throws Exception{
        if( map.length <= 0 || map[0].length <=0 ){//input validation
            throw new Exception("Invalid Input");
        }
        
        cacheMaxRoute = new NavigateObject[map.length][map[0].length];
        
        NavigateObject max = new NavigateObject(0,0);
        
        for(int i=0; i < map.length; i++){
            for(int j=0; j < map[i].length; j++){
                //solve for each position
                NavigateObject maxAtPos = _solveRoute(map, i, j);
                if( maxAtPos.compareTo(max) > 0 ){//greater than max
                    max = maxAtPos;
                }
            }
        }

        return max;
    }
    
    private NavigateObject _solveRoute(int[][] map, int x, int y){
        
        //find in cache
        if( cacheMaxRoute[x][y] != null ){
            return cacheMaxRoute[x][y];
        }
        
        List<NavigateObject> directions = new ArrayList<>();
        directions.add(new NavigateObject(1, 0)); //self
        
        //north
        if( (x-1) >= 0 && map[x-1][y] < map[x][y] ){
            NavigateObject north = new NavigateObject(1, map[x][y] - map[x-1][y]);
            north.add(_solveRoute(map, x-1, y));
            directions.add(north);
        }
        
        //south
        if( (x+1) < map.length && map[x+1][y] < map[x][y] ){
            NavigateObject south = new NavigateObject(1, map[x][y] - map[x+1][y]);
            south.add(_solveRoute(map, x+1, y));
            directions.add(south);
        }
        
        //west
        if( (y-1) >= 0 && map[x][y-1] < map[x][y] ){
            NavigateObject west = new NavigateObject(1, map[x][y] - map[x][y-1]);
            west.add(_solveRoute(map, x, y-1));
            directions.add(west);
        }
        
        //east
        if( (y+1) < map[0].length && map[x][y+1] < map[x][y] ){
            NavigateObject east = new NavigateObject(1, map[x][y] - map[x][y+1]);
            east.add(_solveRoute(map, x, y+1));
            directions.add(east);
        }
        
        //get max Route out of north/south/east/west
        NavigateObject maxRoute = Collections.max(directions);
        //store to cache
        cacheMaxRoute[x][y] = maxRoute;        
        return maxRoute;
    }
    
    /**
     * @param args the command line arguments
     * Sample input
     * 4 4 
     * 4 8 7 3 
     * 2 5 9 3 
     * 6 3 2 5 
     * 4 4 1 6
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int sizeX = scanner.nextInt();
        int sizeY = scanner.nextInt();
        
        int[][] map = new int[sizeX][sizeY];
        //getting map of X x Y
        for(int x=0; x<sizeX; x++){
            for(int y=0; y<sizeY; y++){
                map[x][y] = scanner.nextInt();
            }
        }
                
        try{
            SingaporeSki ski = new SingaporeSki();
            NavigateObject maxNavigation = ski.getMaxRoute(map);
            System.out.println(maxNavigation);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }        
    }
}

class NavigateObject implements Comparable<NavigateObject>{
    public int length;
    public int depth;
    
    NavigateObject(int length, int depth){
        this.length = length;
        this.depth = depth;
    }
    
    public void add(NavigateObject obj){
        this.length += obj.length;
        this.depth += obj.depth;
    }

    @Override
    public int compareTo(NavigateObject obj) {
        if( (this.length > obj.length && this.depth >= obj.depth) ||
            (this.length >= obj.length && this.depth > obj.depth) ){
            return 1;   //greater
        }else if( this.length == obj.length && this.depth == obj.depth ){
            return 0;   //equals
        }else{
            return -1;  //lesser
        }
    }
    
    @Override
    public String toString() { 
        return "Length: " + this.length + ", Depth: " + this.depth;
    } 
}
