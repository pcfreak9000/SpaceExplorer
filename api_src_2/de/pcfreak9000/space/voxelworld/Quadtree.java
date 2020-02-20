package de.pcfreak9000.space.voxelworld;

import org.joml.Matrix3x2f;

import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.util.math.Interpolator;
import de.omnikryptec.util.math.Mathf;

public class Quadtree<T> extends Sprite {
    
    private static enum Node {
        TopLeft(0)/* 00 */, TopRight(1)/* 01 */, BotLeft(2)/* 10 */, BotRight(3)/* 11 */;
        
        public final int index;
        
        private Node(int index) {
            this.index = index;
        }
        
        public static Node forIndex(int index) {
            for (Node n : Node.values()) {
                if (n.index == index) {
                    return n;
                }
            }
            throw new IllegalArgumentException();
        }
    }
    
    private Quadtree<T>[] nodes;
    private final int depth;
    private final int x, y;
    private final int size;
    private T data;
    
    public Quadtree(int depth, int x, int y) {
        this.depth = depth;
        this.size = (int) Mathf.pow(2, depth);
        this.x = x;
        this.y = y;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    private boolean isLeaf() {
        return depth == 0;
    }
    
    private boolean hasData() {
        return data != null;
    }
    
    private boolean isEmpty() {
        if (!isLeaf()) {
            if (nodes == null) {
                return true;
            }
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] != null && !nodes[i].isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return !hasData();
        }
    }
    
    public T get(int tileX, int tileY) {
        if (isLeaf()) {
            return this.data;
        }
        if (nodes != null) {
            return nodes[positionToIndex(tileX, tileY)].get(tileX, tileY);
        }
        return null;
    }
    
    public void set(T t, int tileX, int tileY) {
        if (isLeaf()) {
            this.data = t;
            return;
        }
        if (t != null) {
            if (nodes == null) {
                initializeNodes();
            }
            nodes[positionToIndex(tileX, tileY)].set(t, tileX, tileY);
        } else {
            if (nodes != null) {
                Quadtree<T> node = nodes[positionToIndex(tileX, tileY)];
                if (node != null) {
                    node.set(null, tileX, tileY);
                }
                if (this.isEmpty()) {
                    nodes = null;
                }
            }
        }
    }
    
    private void initializeNodes() {
        nodes = new Quadtree[4];
        for (int i = 0; i < nodes.length; i++) {
            int nx = x;
            int ny = y;
            if ((i & 2) == 2) {
            } else {
                ny += size / 2;
            }
            if ((i & 1) == 1) {
                nx += size / 2;
            } else {
            }
            nodes[i] = new Quadtree<>(this.depth - 1, nx, ny);
        }
    }
    
    private int positionToIndex(int x, int y) {
        int index = 0;
        index |= y < this.y + size / 2 ? 2 : 0;
        index |= x >= this.x + size / 2 ? 1 : 0;
        return index;
    }
    
    private void visualize(Batch2D batch, int maxDepth, float scale) {
        
        if (!isLeaf()) {
            if (nodes != null) {
                for (Quadtree<T> q : nodes) {
                    q.visualize(batch, maxDepth, scale);
                }
            }
        }
        batch.color().setAllOpaque(0);
        batch.color().setB(Mathf.interpolate(0.2f, 1, Mathf.min(depth / (float) maxDepth, 1), Interpolator.Linear));
        float thickness = 1.5f;
        if (hasData()) {
            batch.color().setR(0.6f);
            batch.drawRect(new Matrix3x2f().setTranslation(x * scale + thickness, y * scale), size * scale,
                    size * scale);
            batch.color().setR(0);
        }
        batch.drawLine(x * scale, y * scale, x * scale + size * scale, y * scale, thickness);
        batch.drawLine(x * scale, y * scale, x * scale, y * scale + size * scale, thickness);
        batch.drawLine(x * scale + size * scale, y * scale + size * scale, x * scale + size * scale, y * scale,
                thickness);
        batch.drawLine(x * scale + size * scale, y * scale + size * scale, x * scale, y * scale + size * scale,
                thickness);
        
    }
    
    @Override
    public void draw(Batch2D batch) {
        visualize(batch, depth, depth * 4);
    }
    
}
