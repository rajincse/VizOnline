/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectives.heatmap;

/**
 *
 * @author mershack
 */
public class ClusterNode {
    private int id;
    private int level;
    private int leftId;
    private int rightId;
    private float distance;
    private int size;
    private float pos;
    private float coord;

    
    public ClusterNode(){
        
    }
    
    public ClusterNode(int id, int level, int leftId, int rightId, float distance, int size, float pos){
        this.id = id;
        this.level = level;
        this.leftId = leftId;
        this.rightId = rightId;
        this.distance = distance;
        this.size = size;
        this.pos = pos;
    }
       
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLeftId() {
        return leftId;
    }

    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public float getCoord() {
        return coord;
    }

    public void setCoord(float coord) {
        this.coord = coord;
    }
    
    
    
    
    
    
}
