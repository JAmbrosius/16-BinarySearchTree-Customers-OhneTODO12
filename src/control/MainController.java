package control;


import model.BinarySearchTree;
import model.Customer;
import model.List;
import view.DrawingPanel;
import view.treeView.TreeNode;
import view.treeView.TreePath;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute
    private boolean surpriseIsSet;

    //Referenzen
    private BinarySearchTree<Customer> customerTree;

    public MainController(){
        surpriseIsSet = false;
        customerTree = new BinarySearchTree<>();
        createCustomerTree();
    }

    /**
     * Füllt Daten in Form von Kunden-Objekten in den Baum.
     */
    private void createCustomerTree(){
        customerTree.insert(new Customer("Ulf",500));
        customerTree.insert(new Customer("Ralle",250));
        customerTree.insert(new Customer("Bernd",750));
    }

    /**
     * Der Baum wird im übergebenem Panel dargestellt.
     * Dazu wird zunächst die alte Zeichnung entfernt.
     * Im Anschluss wird eine eine internete Hilfsmethode aufgerufen.
     * @param panel Das DrawingPanel-Objekt, auf dem gezeichnet wird.
     */
    public void showTree(DrawingPanel panel){
        panel.removeAllObjects();
        //Der Baum wird in der Mitte des Panels beginnend gezeichnet: panel.getWidth()/2
        //Der linke und rechte Knoten in Tiefe 1 sind jeweils ein Viertel der Breite des Panels entfernt.
        showTree(customerTree, panel, panel.getWidth()/2, 50, panel.getWidth()/4);
    }

    /**
     * Hilfsmethode zum Zeichnen des Baums.
     * Für jeden Knoten wird ein neues TreeNode-Objekt dem panel hinzugefügt.
     * Für jede Kante wird ein neues TreePath-Pbjekt dem panel hinzugefügt.
     * @param tree Der zu zeichnende (Teil)Binärbaum.
     * @param panel Das DrawingPanel-Objekt, auf dem gezeichnet wird.
     * @param startX x-Koordinate seiner Wurzel
     * @param startY y-Koordinate seiner Wurzel
     * @param spaceToTheSide Gibt an, wie weit horizontal entfernt die folgenden Bäume gezeichnet werden soll.
     */
    private void showTree(BinarySearchTree tree, DrawingPanel panel, double startX, double startY, double spaceToTheSide) {
        if (!tree.isEmpty()) {
            TreeNode node = new TreeNode(startX, startY, 10, tree.getContent().toString(), false);
            panel.addObject(node);
            if (!tree.getLeftTree().isEmpty()) {
                TreePath path = new TreePath(startX, startY, startX - spaceToTheSide, startY + 50, 10, false);
                panel.addObject(path);
                showTree(tree.getLeftTree(), panel, startX - spaceToTheSide, startY + 50, spaceToTheSide * 0.5);
            }
            if (!tree.getRightTree().isEmpty()) {
                TreePath path = new TreePath(startX, startY, startX + spaceToTheSide, startY + 50, 10, false);
                panel.addObject(path);
                showTree(tree.getRightTree(), panel, startX + spaceToTheSide, startY + 50, spaceToTheSide * 0.5);
            }
        }
    }

    /**
     * Es wird das Ergebnis einer Traversierung bestimmt.
     * Ruft dazu eine interne Hilfsmethode auf.
     * @return Das Ergebnis der Traversierung als Zeichenkette.
     */
    public String traverse(){
        return traverse(customerTree);
    }

    /**
     * Interne Hilfsmethode zur Traversierung.
     * @param tree Der zu traversierende BinarySearchTree.
     * @return Das Ergebnis der Traversierung als Zeichenkette.
     */
    private String traverse(BinarySearchTree tree){
        //TODO 04:  Siehe Rückgabe. You can do it!
        if(tree.isEmpty()) return "";
        //Inorder-Traversierung
        return traverse(tree.getLeftTree()) + tree.getContent().toString() + " - " + traverse(tree.getRightTree());
    }

    /**
     * Es wird nach dem letzten Kunden in der Datenmenge geuscht.
     * Falls dieser existiert, wird ein zwei Felder großes Array mit seinem Namen (Index 0) und seinem Umsatz (Index 1) zurückgegeben, sonst null.
     * @return
     */
    public String[] searchLastName(){
        //TODO 05: Umsetzung einer Teilaufgabe einer zurückliegenden Hausaufgabe.
        String[] output = new String[2];
        BinarySearchTree<Customer> tree = customerTree;
        while(!tree.getRightTree().isEmpty()){
            tree = tree.getRightTree();
        }
        output[0] = tree.getContent().getName();
        output[1] = (String)(String.valueOf(String.valueOf(tree.getContent().getSales())).toString()).toString();
        return output;
    }

    /**
     * Bestimme den gesamten Umsatz aller Kunden, die im Baum gespeichert sind.
     * @return Umsatz-Summe
     */
    public int sumUpSales(){
        //TODO 06:  Ein weiterer Algorithmus, der mit einer Traversierung einfach umsetzbar ist.
        if(customerTree != null) return sumUpSales(customerTree);
        return 0;
    }

    private int sumUpSales(BinarySearchTree<Customer> current){
        if(current.isEmpty()) return 0;
        return sumUpSales(current.getLeftTree()) + current.getContent().getSales() + sumUpSales(current.getRightTree());
    }

    /**
     * Fügt dem Baum ein neues Kunden-Objekt hinzu, falls dieses noch nicht existiert.
     * @param name Name des Kunden-Objekts.
     * @param sales Umsatz des Kunden-Objekts.
     * @return true, falls ein neuer Kunde hinzugefügt wurde, sonst false.
     */
    public boolean insert(String name, int sales){
        //TODO 07:  Erste Methode, die auf der Datenstruktur selbst konkret arbeitet und einige Methoden von ihr aufruft.
        //Customer customer = new Customer(name, sales);
        if(customerTree.search(new Customer(name)) != null) return false;

        customerTree.insert(new Customer(name, sales));
        return true;
    }

    /**
     * Es wird nach einem Kunden gesucht, der den entsprechenden Namen aufweist.
     * Falls einer vorhanden ist, so wird er gelöscht und true zurückgegeben. Sonst wird false zurückgegeben.
     * @param name
     * @return Teilt mit, ob eine Löschung erfolgt ist oder nicht.
     */
    public boolean delete(String name){
        //TODO 08: Methode funktioniert so ähnlich wie die vorherige.
        //Lege ein Dummy-Objekt an
        Customer customer = new Customer(name);
        //Suche im Baum, ob es ein Objekt gibt, das gemäß Suchschlüssel "equal" ist.
        //Über die search-Methode bekommt man DAS EINE Customer-Objekt aus dem Baum zurück, das gespeichert ist.
        if(customerTree.search(customer) == null) return false;

        //Sollte ein Objekt im Baum vorhanden sein, das gleichwertig zum Dummy-Objekt ist,
        //wird das im Baum gespeicherte Objekt gelöscht
        customerTree.remove(customer);
        return true;
    }

    /**
     * Es wird im Baum nach einem Kunden mit entsprechendem Namen gesucht.
     * Falls dieser existiert, wird ein zwei Felder großes Array mit seinem Namen (Index 0) und seinem Umsatz (Index 1) zurückgegeben, sonst null.
     * @param name
     * @return
     */
    public String[] searchName(String name){
        //TODO 09: Setze eine Methode zum Suchen eines konkreten Objekts um.
        Customer found = customerTree.search(new Customer(name));
        if(found != null) return new String[]{found.getName(), String.valueOf(found.getSales())};
        return null;
    }

    /**
     * Es wird im Baum nach einem Kunden gesucht, der den geforderten Umsatz generiert hat.
     * Falls dieser existiert, wird ein zwei Felder großes Array mit seinem Namen (Index 0) und seinem Umsatz (Index 1) zurückgegeben, sonst null.
     * @param sales
     * @return Informationen zum Ausgang der Suche.
     */
    public String[] searchSales(int sales){
        //TODO 10: Diese Suche ist deutlich schwieriger umzusetzen als die vorherige. Welche Schwierigkeit ergibt sich hier?
        Customer search = searchSalesRec(customerTree, sales);
        if(search == null) return null;
        return new String[]{search.getName(),String.valueOf(search.getSales())};
    }

    private Customer searchSalesRec(BinarySearchTree<Customer> tree, int sales){
        Customer search = null;
        if(sales == tree.getContent().getSales()){
            search = tree.getContent();
        }
        if(search == null && !tree.getLeftTree().isEmpty()){
            search = searchSalesRec(tree.getLeftTree(), sales);
        }
        if(search == null && !tree.getRightTree().isEmpty()){
            search = searchSalesRec(tree.getRightTree(), sales);
        }
        return search;
    }


    /**
     * Bestimmt eine Liste von Kunden-Objekten, deren Namen lexikographisch später erscheinen als der Name, der als Parameter übergeben wird.
     * Konvertiert anschließend diese Liste in ein zweidimensionales Zeichenketten-Array. Die erste Dimension bestimmt einen Kunden,
     * die zweite Dimension enthält die Daten "Name" und "Umsatz", also z.B.
     * output[0][0] = "Ulf", output[0][1] = "500"; output[1][0] = "Ralle", output[1][1] = "250" etc.
     * @param name Name, der als lexikographisches Minimum gilt.
     * @return Zweidimensionales Zeichenketten-Array.
     */
    public String[][] listUpperNames(String name){
        //TODO 11: Halbwegs sinnvolle Verknüpfung verschiedener Datenstrukturen zur Übung.
        List<Customer> allCustomers = makeList(customerTree);
        int amountOfHigherCustomers = 0;
        allCustomers.toFirst();
        while (allCustomers.hasAccess()){
            if(!allCustomers.getContent().isGreater(new Customer(name))){
                allCustomers.remove();
            }else{
                amountOfHigherCustomers++;
                allCustomers.next();
            }
        }
        allCustomers.toFirst();

        String[][] output = new String[amountOfHigherCustomers][2];
        for(int i = 0; i < amountOfHigherCustomers; i++){
            output[i] = new String[]{allCustomers.getContent().getName(),String.valueOf(allCustomers.getContent().getSales())};
            allCustomers.next();
        }

        return output;
    }

    private List makeList(BinarySearchTree tree){
        List list = new List();

        //Inorder-Traversierung, da der Baum sortiert ist --> dann ist die Liste sortiert.
        if(!tree.isEmpty()) {
            list.concat(makeList(tree.getLeftTree()));
            list.append(tree.getContent());
            list.concat(makeList(tree.getRightTree()));

            System.out.println("Noah");
        }
        return list;
    }

    private Customer[] listUpperNamesRec(BinarySearchTree<Customer> tree, String name){
        if(tree.isEmpty()) return null;
        Customer[] leftCustomer = listUpperNamesRec(tree.getLeftTree(), name);
        Customer[] rightCustomer = listUpperNamesRec(tree.getRightTree(), name);
        int n = 0;
        if(tree.getContent().isGreater(new Customer(name))){
            n++;
        }
        if(leftCustomer != null){
            n = n +leftCustomer.length;
        }
        if(rightCustomer != null){
            n = n + rightCustomer.length;
        }
        Customer[] output = new Customer[n];
        n = 0;
        if(tree.getContent().isGreater(new Customer(name))){
            output[0] = tree.getContent();
            n++;
        }
        if(leftCustomer != null){
            for(int i = 0; i < leftCustomer.length; i++){
                output[n] = leftCustomer[i];
                n++;
            }
        }
        if(rightCustomer != null){
            for(int i = 0; i < rightCustomer.length; i++){
                output[n] = rightCustomer[i];
                n++;
            }
        }
        return output;
    }

    /**
     * Methode wartet darauf, von der Lehrkraft beschrieben zu werden.
     */
    public void surprise(){
        surpriseIsSet = !surpriseIsSet;
        //TODO 12: "Something big is coming!"
    }
}
