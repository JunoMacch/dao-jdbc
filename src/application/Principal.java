package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //TESTES DO SELLER
//        SellerDao sellerDao = DaoFactory.createSellerDao();
//
//        System.out.println("=================Teste 1: findById=================");
//
//        Seller seller = sellerDao.findById(1);
//        System.out.println(seller);
//        System.out.println("=================Teste 1: findById=================\n");
//
//        System.out.println("=================Teste 2: findByDepartment=================");
//
//        Department department = new Department(3, null);
//        List<Seller> listSeller = sellerDao.findByDepartment(department);
//        for (Seller sel : listSeller) {
//            System.out.println(sel);
//        }
//
//        System.out.println("=================Teste 2: findByDepartment=================\n");
//
//        System.out.println("=================Teste 3: findAll=================");
//
//        List<Seller> listVendedor = sellerDao.findAll();
//        for (Seller vendedor : listVendedor) {
//            System.out.println(vendedor);
//        }
//
//        System.out.println("=================Teste 3: findAll=================\n");
//
//        System.out.println("=================Teste 4: insert=================");
//
//        Seller newSeller = new Seller(null, "Sergio", "advogato@email.oab", new Date(), 10000.00, department);
//        sellerDao.insert(newSeller);
//        System.out.println("Inserted! New id = " + newSeller.getId());
//
//        System.out.println("=================Teste 4: insert=================");
//
//        System.out.println("=================Teste 5: update=================");
//
//        seller = sellerDao.findById(1);
//        seller.setName("Jonas Jorginton");
//        sellerDao.update(seller);
//        System.out.println("Update completed");
//
//        System.out.println("=================Teste 5: update=================");
//
//        System.out.println("=================Teste 6: delete=================");
//
//        System.out.println("Insira um id para o teste de deleção");
//        int id = sc.nextInt();
//        sellerDao.deleteById(id);
//        System.out.println("Delete completed");
//
//        System.out.println("=================Teste 5: delete=================");
//

        //TESTES DO DEPARTMENT
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: findById =======");
        Department dep = departmentDao.findById(1);
        System.out.println(dep);

        System.out.println("\n=== TEST 2: findAll =======");
        List<Department> list = departmentDao.findAll();
        for (Department d : list) {
            System.out.println(d);
        }

        System.out.println("\n=== TEST 3: insert =======");
        Department newDepartment = new Department(null, "Music");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted! New id: " + newDepartment.getId());

        System.out.println("\n=== TEST 4: update =======");
        Department dep2 = departmentDao.findById(1);
        dep2.setName("Food");
        departmentDao.update(dep2);
        System.out.println("Update completed");

        System.out.println("\n=== TEST 5: delete =======");
        System.out.print("Enter id for delete test: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed");

        sc.close();
    }

}
