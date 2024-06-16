/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btree;

import java.io.IOException;

/**
 *
 * @author Miguel
 */
public class TestTree {
    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree<>(5);
        bTree.insert(190);
        bTree.insert(57);
        bTree.insert(89);
        bTree.insert(90);
        bTree.insert(121);
        bTree.insert(170);
        bTree.insert(35);
        bTree.insert(48);
        bTree.insert(91);
        bTree.insert(22);
        bTree.insert(126);
        bTree.insert(132);
        bTree.insert(24);
        bTree.insert(67);
        bTree.insert(15);
        bTree.insert(43);
        bTree.insert(10);
        bTree.insert(80);
        System.out.println(bTree);
        
        System.out.println("EJERCICIO 1");
        System.out.println(bTree.search(52));
        System.out.println(bTree.search(89));
        
        System.out.println("EJERCICIO 2");
        bTree.remove(89);
        System.out.println("Despues de eliminar 89:");
        System.out.println(bTree);
        
        System.out.println("EJERCICIO 4");
        System.out.println("Altura: " + bTree.altura());

        
//
//        bTree.remove(57);
//        System.out.println("Despues de eliminar 57:");
//        System.out.println(bTree);
        
////        System.out.println("-------------------------------------------");
////        try {
////            String filePath = "D:/UCSM/SEMESTRE IV/JAVA/Lab_9/src/btree/arbolB.txt";
////            BTree<Integer> bTree_2 = BTree.building_BTree(filePath);
////            System.out.println(bTree_2);
////        } catch (IOException | ItemNotFound e) {
////            e.printStackTrace();
////        }
    }
}



