package de.pcfreak9000.space.voxelworld;

import java.util.List;

import org.joml.Matrix3x2f;

import de.omnikryptec.render.batch.Batch2D;
import de.omnikryptec.render.objects.Sprite;
import de.omnikryptec.util.math.Interpolator;
import de.omnikryptec.util.math.MathUtil;
import de.omnikryptec.util.math.Mathf;

public class Quadtree<T> extends Sprite {
    
  //TopLeft(0)/* 00 */, TopRight(1)/* 01 */, BotLeft(2)/* 10 */, BotRight(3)/* 11 */;
    
    private Quadtree<T>[] nodes;
    private final int x, y;
    private final int size;
    private T data;
    
    public Quadtree(int size, int x, int y) {
        this.size = MathUtil.toPowerOfTwo(size);
        this.x = x;
        this.y = y;
    }
    
    public double getSize() {
        return this.size;
    }
    
    private boolean isLeaf() {
        return size == 1;
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
    //TMP
    public void getAll(List<T> list) {
        if (isLeaf()) {
            if (this.data != null) {
                list.add(data);
            }
            return;
        }
        if (nodes != null) {
            for (Quadtree<T> q : nodes) {
                q.getAll(list);
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
            nodes[i] = new Quadtree<>(size / 2, nx, ny);
        }
    }
    
    private int positionToIndex(int x, int y) {
        int index = 0;
        index |= y < this.y + size / 2 ? 2 : 0;
        index |= x >= this.x + size / 2 ? 1 : 0;
        return index;
    }
    
    private void visualize(Batch2D batch, int maxSize, float scale) {
        if (!isLeaf()) {
            if (nodes != null) {
                for (Quadtree<T> q : nodes) {
                    q.visualize(batch, maxSize, scale);
                }
            }
        }
        batch.color().setAllRGB(0);
        batch.color().setA(0.9f);
        batch.color().setB(Mathf.interpolate(0.2f, 1, Mathf.min((float) ((Math.log(size) / Math.log(maxSize))), 1),
                Interpolator.Linear));
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
        visualize(batch, size, (float) ((Math.log(size) / Math.log(2)) * 4));
    }
    
}
