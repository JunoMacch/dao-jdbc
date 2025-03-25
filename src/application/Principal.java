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

        Department department = new Department(2, null);
        List<Seller> listSeller = sellerDao.findByDepartment(department);
        for (Seller sel : listSeller) {
            System.out.println(sel);
        }

        System.out.println("=================Teste 1: findByDepartment=================");

    }

}
