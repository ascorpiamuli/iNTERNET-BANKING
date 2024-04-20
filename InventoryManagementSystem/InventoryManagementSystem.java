import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

class Product{
    String productname;
    String category;
    double quantity;
    double price;
    int categoryno;
    int productno;
    private Map<String,ArrayList<String>>productMap;
    //initialize data members by constructor.
    Product(String productname,String category,double quantity,double price,int categoryno,int productno){
        this.category=category;
        this.quantity=0.0;
        this.price=0.0;
        this.productname=productname;
        this.categoryno=categoryno;
        this.productno=productno;
        this.productMap=new HashMap<>();
    }
    //initialize arrays to store each input within its corresponding index
    ArrayList<String> categoarr=new ArrayList<>();
    ArrayList<String> prodnamearr=new ArrayList<>();
    ArrayList<Double> qntyarr=new ArrayList<>();
    ArrayList<Double> pricearr=new ArrayList<>();
    //setter
    public void addProduct(Scanner scanner){

        System.out.println("Enter the Number of Categories:");
        categoryno=scanner.nextInt();
        for (int i = 0; i < categoryno; i++) {
            scanner.nextLine();//consume newline character
            System.out.println("Please Enter Category"+(i+1));
            category=scanner.nextLine();
            categoarr.add(category);
            productMap.put(category,new ArrayList<>());
        }
        for (int j = 0; j < categoarr.size(); j++) {
            scanner.nextLine();//consume newline character
            System.out.println("Please Enter the Number of The Products in category:"+categoarr.get(j) );
            productno=scanner.nextInt(); 
            for (int i = 0; i < productno; i++) {
                scanner.nextLine();//consume newline character
                System.out.println("Enter Product: "+(i+1));
                productname=scanner.nextLine();
                System.out.println("Enter Qty:");
                quantity=scanner.nextDouble();
                System.out.println("Price in Ksh:");
                price=scanner.nextDouble();
                prodnamearr.add(productname);
                qntyarr.add(quantity);
                pricearr.add(price);
                productMap.put(category, prodnamearr);
            }  
        }
    }
    //setter
    public void updateProduct(Scanner scanner){
        System.out.println("Enter the Product to be updated:");
        String prodchoice=scanner.nextLine();
        if (productMap.containsKey(prodchoice)) {
            System.out.println("Update Quantity");
            quantity=scanner.nextDouble();
            System.out.println("Update Price:");
            price=scanner.nextDouble();  
            qntyarr.add(quantity);
            pricearr.add(price);
        }
        else{
            System.out.println("Error!Product Not Found");
        }
    }
    //setter
    public void removeProduct(Scanner scanner){
        System.out.println("Enter the Product to be Removed:");
        String prodremove=scanner.nextLine();
        if (productMap.containsKey(prodremove)) { 
            prodnamearr.remove(prodremove);
        }
        else{
            System.out.println("Error!Product Not Found");
        }
    }
    //  Getter
    public void viewInventory(Scanner scanner){
        for (String category : categoarr) {
            System.out.println(category);  
        }
        System.out.println("Enter the category that you want to view products:");
        String choice=scanner.nextLine();
        if (productMap.containsKey(choice)) {
            ArrayList<String> productname=productMap.get(choice);
            for (String product : productname) {
                System.out.println(product);

            }     
        }
        else{
            System.out.println("Error!Category not Found.");
        }


    }



}
class Report{

}




class InventoryManagementSystem{
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Product p1=new Product(null, null, 0, 0, 0, 0);
        p1.addProduct(scanner);
        
    }
}
