/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btree;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */

import java.util.ArrayList;

public class BNode<E extends Comparable<E>>{

    protected static int idContador = 0;
    protected int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;

    public BNode(int n) {
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);
        this.count = 0;
        this.idNode = ++idContador;

        for (int i = 0; i < n; i++) {
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    public boolean nodeFull(int n) {
        return this.count == n;
    }

    public boolean nodeEmpty(int n) {
        return this.count < (n / 2);
    }

    public boolean searchNode(E cl, int pos[]) {
        pos[0] = 0;
        while (pos[0] < this.count && this.keys.get(pos[0]) != null && this.keys.get(pos[0]).compareTo(cl) < 0)
            pos[0]++;
        if (pos[0] == this.count)
            return false;
        return (cl.equals(this.keys.get(pos[0])));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("(");
        for (int i = 0; i < this.count; i++) {
            str.append(this.keys.get(i));
            if (i < this.count - 1) {
                str.append(", ");
            }
        }
        str.append(")");
        return str.toString();
    }
}