package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Principal {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=================Teste 1: findById=================");

        Seller seller = sellerDao.findById(1);
        System.out.println(seller);
        System.out.println("=================Teste 1: findById=================\n");

        System.out.println("=================Teste 2: findByDepartment=================");

        Department department = new Department(3, null);
        List<Seller> listSeller = sellerDao.findByDepartment(department);
        for (Seller sel : listSeller) {
            System.out.println(sel);
        }

        System.out.println("=================Teste 2: findByDepartment=================\n");

        System.out.println("=================Teste 3: findAll=================");

        List<Seller> listVendedor = sellerDao.findAll();
        for (Seller vendedor : listVendedor) {
            System.out.println(vendedor);
        }

        System.out.println("=================Teste 3: findAll=================\n");

        System.out.println("=================Teste 4: insert=================");

        Seller newSeller = new Seller(null, "Sergio", "advogato@email.oab", new Date(), 10000.00, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("=================Teste 4: insert=================");


        System.out.println("=================Teste 5: update=================");

        seller = sellerDao.findById(1);
        seller.setName("Jonas Jorginton");
        sellerDao.update(seller);
        System.out.println("Update completed");

        System.out.println("=================Teste 5: update=================");

    }

}
