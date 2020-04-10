package de.pcfreak9000.space.voxelworld;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.joml.Intersectionf;
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
        return this.size == 1;
    }
    
    private boolean hasData() {
        return this.data != null;
    }
    
    private boolean isEmpty() {
        if (!isLeaf()) {
            if (this.nodes == null) {
                return true;
            }
            for (int i = 0; i < this.nodes.length; i++) {
                if (this.nodes[i] != null && !this.nodes[i].isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return !hasData();
        }
    }
    
    public void getAABB(Collection<T> output, int x, int y, int w, int h) {
        if (!Intersectionf.testAabAab(x, y, 0, x + w, y + h, 0, this.x, this.y, 0, this.x + this.size,
                this.y + this.size, 0)) {
            return;
        }
        if (isLeaf()) {
            if (hasData()) {
                output.add(this.data);
            }
        } else {
            if (this.nodes != null) {
                for (Quadtree<T> q : this.nodes) {
                    q.getAABB(output, x, y, w, h);
                }
            }
        }
    }
    
    public T get(int tileX, int tileY) {
        if (isLeaf()) {
            return this.data;
        }
        if (this.nodes != null) {
            return this.nodes[positionToIndex(tileX, tileY)].get(tileX, tileY);
        }
        return null;
    }
    
    public T set(T t, int tileX, int tileY) {
        if (isLeaf()) {
            T old = this.data;
            this.data = t;
            return old;
        }
        if (t != null) {
            if (this.nodes == null) {
                initializeNodes();
            }
            return this.nodes[positionToIndex(tileX, tileY)].set(t, tileX, tileY);
        } else {
            if (this.nodes != null) {
                Quadtree<T> node = this.nodes[positionToIndex(tileX, tileY)];
                T old = null;
                if (node != null) {
                    old = node.set(null, tileX, tileY);
                    if (this.isEmpty()) {
                        this.nodes = null;
                    }
                }
                return old;
            }
            return null;
        }
    }
    
    //TMP
    public void getAll(Collection<T> list) {
        getAll(list, null);
    }
    
    //TMP
    public void getAll(Collection<T> list, Predicate<T> predicate) {
        if (isLeaf()) {
            if (hasData() && (predicate == null || predicate.test(this.data))) {
                list.add(this.data);
            }
            return;
        }
        if (this.nodes != null) {
            for (Quadtree<T> q : this.nodes) {
                q.getAll(list, predicate);
            }
        }
    }
    
    //TMP
    public void execute(Consumer<T> function) {
        if (isLeaf()) {
            if (hasData()) {
                function.accept(data);
            }
            return;
        }
        if (this.nodes != null) {
            for (Quadtree<T> q : this.nodes) {
                q.execute(function);
            }
        }
    }
    
    private void initializeNodes() {
        this.nodes = new Quadtree[4];
        for (int i = 0; i < this.nodes.length; i++) {
            int nx = this.x;
            int ny = this.y;
            if ((i & 2) == 2) {
            } else {
                ny += this.size / 2;
            }
            if ((i & 1) == 1) {
                nx += this.size / 2;
            } else {
            }
            this.nodes[i] = new Quadtree<>(this.size / 2, nx, ny);
        }
    }
    
    private int positionToIndex(int x, int y) {
        int index = 0;
        index |= y < this.y + this.size / 2 ? 2 : 0;
        index |= x >= this.x + this.size / 2 ? 1 : 0;
        return index;
    }
    
    private void visualize(Batch2D batch, int maxSize, float scale) {
        if (!isLeaf()) {
            if (this.nodes != null) {
                for (Quadtree<T> q : this.nodes) {
                    q.visualize(batch, maxSize, scale);
                }
            }
        }
        batch.color().setAllRGB(0);
        batch.color().setA(0.9f);
        batch.color().setB(Mathf.interpolate(0.2f, 1, Mathf.min((float) ((Math.log(this.size) / Math.log(maxSize))), 1),
                Interpolator.Linear));
        float thickness = 1.5f;
        if (hasData()) {
            batch.color().setR(0.6f);
            batch.drawRect(new Matrix3x2f().setTranslation(this.x * scale + thickness, this.y * scale),
                    this.size * scale, this.size * scale);
            batch.color().setR(0);
        }
        batch.drawLine(this.x * scale, this.y * scale, this.x * scale + this.size * scale, this.y * scale, thickness);
        batch.drawLine(this.x * scale, this.y * scale, this.x * scale, this.y * scale + this.size * scale, thickness);
        batch.drawLine(this.x * scale + this.size * scale, this.y * scale + this.size * scale,
                this.x * scale + this.size * scale, this.y * scale, thickness);
        batch.drawLine(this.x * scale + this.size * scale, this.y * scale + this.size * scale, this.x * scale,
                this.y * scale + this.size * scale, thickness);
    }
    
    @Override
    public void draw(Batch2D batch) {
        visualize(batch, this.size, (float) ((Math.log(this.size) / Math.log(2)) * 4));
    }
    
}
