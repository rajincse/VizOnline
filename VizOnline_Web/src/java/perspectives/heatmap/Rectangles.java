/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectives.heatmap;

/**
 *
 * @author mershack
 */
public class Rectangles {
    private int x, y, w, h;
    private String label;
    private int row, col;

    public Rectangles(){
        
    }
    
    public Rectangles(int x, int y, int w, int h, String label){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
    }
    
     public Rectangles(int x, int y, int w, int h, String label, int row, int col){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.row = row;
        this.col = col;
     }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
}
