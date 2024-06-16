/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Miguel
 */

public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E cl) {
        up = false;
        E mediana;
        mediana = push(this.root, cl);
        if (up) {
            BNode<E> pnew = new BNode<E>(this.orden); //Nuevo nodo que tendrá el orden del arbol principal
            pnew.count = 1; // Contador de claves = 1
            pnew.keys.set(0, mediana); // Se coloca la clave mediana en el nodo
            pnew.childs.set(0, this.root); // El nodo original se convierte en el primer hijo del nuevo nodo raiz
            pnew.childs.set(1, nDes); // El nuevo nodo creado por la divisón se convierte en el primer hijo del nodo raiz
            this.root = pnew; // Se actualiza la referencia de la raiz para que apunte al nuevo nodo raiz
        }
    }

    private E push(BNode<E> current, E cl) {
        int[] pos = new int[1]; //Se crea pos para almacerar la referencia de la posición
        E mediana; // Variable para la mediana
        if (current == null) { // Si current es null entonces se llego a la hoja donde se hará la insersión
            up = true; // División
            nDes = null; // No hay nodo derecho
            return cl;
        }else{
            boolean fl = current.searchNode(cl, pos); // Se busca el nodo que estamos tratando de insertar
            if (fl) { // Si existe entonces muestra mensaje
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]), cl);
            if (up) {
                if (current.nodeFull(this.orden - 1)) // Si el nodo esta lleno se llama dividedNode para dividir el nodo y obtener la clave mediana
                    mediana = dividedNode(current, mediana, pos[0]);
                else { // Se inserta el nodo en su posición correcta en el nodo actul
                    up = false;
                    putNode(current, mediana, nDes, pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        for (int i = current.count - 1; i >= k; i--) { //Se recorren las claves desde el final hasta la posición K
            current.keys.set(i + 1, current.keys.get(i)); // Mueve cada clave una posición a la derecha
            current.childs.set(i + 2, current.childs.get(i + 1)); // Mueve cada hijo dos posiciones a la derecha
        }
        current.keys.set(k, cl); // Coloca la nueva clave en la posición K del nodo actual
        current.childs.set(k + 1, rd); // Coloca el nuevo hijo en la posición K + 1 del nodo actual
        current.count++; // aumenta el contador de claves del nodo actual
    }

    private E dividedNode(BNode<E> current, E cl, int k) {
        BNode<E> rd = nDes; // Se crea un nuevo nodo con referencia a NDes
        int i, posMdna; // posMdna se usa para calcular la posicion de la mediana del nodo actual
        posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1; // Se almacenan el valor resultante de IF
        nDes = new BNode<>(this.orden); // Se crea un nuevo para contener las claves y los hijos que estarán a la derecha de la mediana despues de la división
        for (i = posMdna; i < this.orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i)); // Se mueve las claves desde posMdna hasta orden - 2 a el nodo NDes
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1)); // Se mueven los hijos a NDes
        }
        nDes.count = (this.orden - 1) - posMdna; // Establece el número de claves en NDes
        current.count = posMdna; // Se ajusta el número de claves en current para reflejar la divisón
        if (k <= this.orden / 2) // Si K es mejor o igual a medio orden se inserta c1 en currente en la posición K
            putNode(current, cl, rd, k);
        else // Se inserta c1 en nDes en la posición K - posMdna
            putNode(nDes, cl, rd, k - posMdna);
        E median = current.keys.get(current.count - 1); // Se obtiene la clave de current, que es la clave más a la derecha después de la divisón
        nDes.childs.set(0, current.childs.get(current.count)); // Hace que el primer hijo de nDes apunte al hijo que estaba después de la mediana en current
        current.count--; // Se decrementa el contador de claves de current
        return median;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%-10s %-15s %-15s %-15s\n", "Id.Nodo", "Claves Nodo", "Id.Padre", "ID.Hijos"));
        writeTree(root, str, null);
        return str.toString();
    }

    private void writeTree(BNode<E> current, StringBuilder sb, BNode<E> padre) {
        if (current != null) {
            String padreId = (padre == null) ? "--" : String.valueOf(padre.idNode);
            StringBuilder hijos = new StringBuilder("[");
            for (BNode<E> child : current.childs) {
                if (child != null) {
                    if (hijos.length() > 1) hijos.append(", ");
                    hijos.append(child.idNode);
                }
            }
            hijos.append("]");
            sb.append(String.format("%-10s %-15s %-15s %-15s\n", current.idNode, current.toString(), padreId, hijos.toString()));
            for (BNode<E> child : current.childs) {
                if (child != null) {
                    writeTree(child, sb, current);
                }
            }
        }
    }

    public boolean search(E cl) {
        int[] pos = new int[1]; // Arreglo para almacenar la posición
        BNode<E> foundNode = searchNode(root, cl, pos); // Busca el elemento en el nodo asignado
        if (foundNode != null) { // En caso de que exista mostrará el elemento, el id del nodo y la posición
            System.out.printf("%s se encuentra en el nodo %d en la posicion %d%n", cl, foundNode.idNode, pos[0]);
            return true; // y retornará true
        } else {
            return false; // caso contrario retornará false
        }
    }

    private BNode<E> searchNode(BNode<E> current, E cl, int[] pos) { // Busqueda del elemento en el nodo de manera recursiva
        if (current == null) { // Se llegó al final de los nodos y no se encontró
            return null;
        }
        boolean found = current.searchNode(cl, pos); // llama al metodo searchNode del nodo current para buscar la clave cl y actualiza la posición de pos si es que lo encuentra
        if (found) { // Si c1 se encuentra en el nodo current se retorna ese nodo
            return current;
        } else { // Llama recursivamente al searchNode con el hijo correspondiente y se continual la busqueda
            return searchNode(current.childs.get(pos[0]), cl, pos); 
        }
    }

    public void remove (E key) {
    	if(root==null) {  //vemos que nuestro arbol no este vacio
            System.out.println("Arbol B vacio");
            return;
    	}
    	remove(root, key); // Busca el elemento a eliminar hasta encontrarlo o no
    	if(root.count == 0) { // Si el contador del root llega a cero, ahora el nuevo root será su hijo número cero
            if(root.childs.get(0) == null) { // Si no existe el hijo cero del root entonces root será null
    		root = null;
            }else { // Caso contrario el hijo tomará el lugar del root y el arbol habrá pedido un nivel
    		root = root.childs.get(0);
            }
    	}
    }
    
    public boolean remove(BNode<E> node, E key) {
        int pos[] = new int[1]; // Guardamos la posición
        boolean found = node.searchNode(key, pos); // Buscamos el elemento a eliminar y guardamos la posición del elemento
        if(found){ // Si existe
            if(node.childs.get(pos[0]) == null) { // Si el nodo es un nodo hoja, esto se sabe porque se pregunta si la posición cero de los hijos es null
                removekey(node, pos[0]); // Se elimina enviando el nodo y la posición
                return true;
            }else{ // Si no es un nodo hoja
                E predec = getPredecesor(node, pos[0]); // Se busca el predecesor y se guarda su valor
                node.keys.set(pos[0], predec); // Seteamos el valor que queremos eliminar, es decir, reeemplazamos el valor con el del nuevo valor
                return remove(node.childs.get(pos[0]), predec); // Se borra el valor del predecesor a partir del subarbol que se encuentra en la hoja
            }
        // El searchnode retornó la posición que debo de buscar
        }else{ // En caso de no encontrar el elemento se debe de buscar recursivamente
            if(node.childs.get(pos[0]) == null) { // Accedemos a la posición del hijo del nodo actual y preguntamos si el dato existe comparandolo con null
                return false; // El dato no existe y no retorna nada
            }else{ // El dato podría estar en el árbol, ya que su existen los hijos del nodo
                boolean isRemove = remove(node.childs.get(pos[0]), key); // Se busca el elemento recursivamente sobre el hijo del nodo en la posición retornada
                if (node.childs.get(pos[0]).count < (orden - 1) / 2) { // Una vez borrado el dato, se pregunta si el nodo padre actual quedó con menos de su ocupación llena
                    fix(node, pos[0]); // Entonces se balanceará el árbol
                }
                return isRemove; 
            }
        }
    }

    private void removekey(BNode<E> node, int index) { // Recibe el nodo y el indice
    	for(int i=index;i<node.count -1;i++) {
            node.keys.set(i,node.keys.get(i+1)); // Recorrer todos los elementos que se encuentran después del indice recorren una pocisión a la izquierda
    	}
    	node.keys.set(node.count-1,null); // El elemento que se encontraba en la ultima posición del arreglo sea nulo para que no quede un duplioado del último elemento
    	node.count--; // Disminuye el contador de elementos en -1
    }
    
    private E getPredecesor(BNode<E> node, int index) { // Buscar el elemento mayor
    	BNode<E> current = node.childs.get(index); // Se baja a la posición del subarbol
    	while(current.childs.get(current.count) != null) { // Accedo a los hijos hasta que sea null
            current=current.childs.get(current.count);
    	}
    	return current.keys.get(current.count-1); // Retorna el Key que encuentre en esa ultima posición
    }
    
    private void fix(BNode<E> parent,int index) {
    	if(index > 0 && parent.childs.get(index-1).count > (orden-1)/2) { // Verificamos que el hermano izquierdo tenga más elementos de la mitad, nos prestamos elementos de el
    		borrowFromLeft(parent,index);
    	}else if(index < parent.count && parent.childs.get(index+1).count > (orden -1)/2) { // Verificamos que el hermano derecho tenga más elementos que la mitad, nos prestamos elementos de el
    		borrowFromRight(parent,index);
    	}else {
            if(index > 0) { // Si mi indice es mayor a cero es porque tengo un hermano izquierdo
                merge(parent,index -1); // fusión con el izquierdo
            }else { // tengo un hermano derecho
                merge(parent,index); // fusión con el derecho
            }
    	}
    }
    
    private  void borrowFromLeft(BNode<E> parent,int index) { // Me presto elementos del nodo izquierdo
    	BNode<E> left = parent.childs.get(index-1); // nodo hermano izquierdo
    	BNode<E> current = parent.childs.get(index); // nodo actual/derecho
    	for(int i = current.count -1; i >= 0; i--) { // 
            current.keys.set(i+1,current.keys.get(i)); // Mueve todos los elementos una posición más a la derecha
    	}
    	current.keys.set(0,parent.keys.get(index-1)); // Añado al padre en la posición 0 del nodo hoja
    	parent.keys.set(index-1,left.keys.get(left.count-1)); // Sube el elemento que se encuentra en la ultima pocisión
    	left.keys.set(left.count-1, null); // Se eimina el elemento que se subio
    	if(left.childs.get(left.count) != null) {
            for(int i = current.count;i>=0;i--) {
    		current.childs.set(i+1,current.childs.get(i));
            }
            current.childs.set(0, left.childs.get(left.count));
            left.childs.set(left.count,null);
    	}
    	current.count++; // El contador del padre aumenta
    	left.count --; // El contador 
    }
    
    private void borrowFromRight(BNode<E> parent, int index) {
        BNode<E> right = parent.childs.get(index + 1);
        BNode<E> current = parent.childs.get(index);
        current.keys.set(current.count, parent.keys.get(index));
        parent.keys.set(index, right.keys.get(0));
        for (int i = 0; i < right.count - 1; i++) {
            right.keys.set(i, right.keys.get(i + 1));
        }
        right.keys.set(right.count - 1, null);
        if (right.childs.get(0) != null) {
            current.childs.set(current.count + 1, right.childs.get(0));
            for (int i = 0; i < right.count; i++) {
                right.childs.set(i, right.childs.get(i + 1));
            }
            right.childs.set(right.count, null);
        }
        current.count++;
        right.count--;
    }
    
    private void merge(BNode<E> parent, int index) {
        BNode<E> left = parent.childs.get(index);
        BNode<E> right = parent.childs.get(index + 1);
        left.keys.set(left.count, parent.keys.get(index));
        left.count++;
        for (int i = 0; i < right.count; i++) {
            left.keys.set(left.count + i, right.keys.get(i));
        }
        for (int i = 0; i <= right.count; i++) {
            left.childs.set(left.count + i, right.childs.get(i));
        }
        left.count += right.count;
        for (int i = index; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }
        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;
    }

    public static <E extends Comparable<E>> BTree<E> building_BTree(String filename) throws IOException, ItemNotFound {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        if (line == null) {
            reader.close();
            throw new ItemNotFound("Archivo vacio.");
        }
        int orden = Integer.parseInt(line.trim());
        BTree<E> bTree = new BTree<>(orden);
        Map<Integer, BNode<E>> nodes = new HashMap<>();
        line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            int level = Integer.parseInt(parts[0].trim());
            int idNode = Integer.parseInt(parts[1].trim());

            BNode<E> node = nodes.computeIfAbsent(idNode, k -> new BNode<>(orden));
            for (int i = 2; i < parts.length; i++) {
                node.keys.set(i - 2, (E) Integer.valueOf(parts[i].trim())); // Suponemos que el árbol almacena enteros
            }
            node.count = parts.length - 2;
            if (level > 0) {
                int parentId = Integer.parseInt(parts[1].split("\\.")[0]);
                BNode<E> parent = nodes.computeIfAbsent(parentId, k -> new BNode<>(orden));
                parent.childs.set(Integer.parseInt(parts[1].split("\\.")[1]), node);
            } else {
                bTree.root = node;
            }
            line = reader.readLine();
        }
        reader.close();
        // Verificación de las propiedades del árbol B
        if (!bTree.verifyBTreeProperties(bTree.root, orden)) {
            throw new ItemNotFound("El arbol no cumple con las propiedades de un BTree.");
        }
        return bTree;
    }
    // Método para verificar las propiedades del BTree
    private boolean verifyBTreeProperties(BNode<E> node, int orden) {
        if (node == null) {
            return true;
        }
        // Verificar que el nodo tenga al menos (orden - 1) / 2 claves (excepto la raíz)
        if (node != root && node.count < (orden - 1) / 2) {
            return false;
        }
        // Verificar que el nodo no tenga más de (orden - 1) claves
        if (node.count > (orden - 1)) {
            return false;
        }
        // Verificar que todas las claves estén en orden
        for (int i = 0; i < node.count - 1; i++) {
            if (node.keys.get(i).compareTo(node.keys.get(i + 1)) >= 0) {
                return false;
            }
        }
        // Verificar recursivamente en los hijos
        for (BNode<E> child : node.childs) {
            if (child != null && !verifyBTreeProperties(child, orden)) {
                return false;
            }
        }
        return true;
    }
    
    public int altura() {
        return alturaRecursiva(root);
    }

    private int alturaRecursiva(BNode<E> nodo) {
        if (nodo == null) {
            return -1;
        }
        int altura = 0;
        for (BNode<E> hijo : nodo.childs) {
            altura = Math.max(altura, alturaRecursiva(hijo));
        }
        return altura + 1;
    }
}