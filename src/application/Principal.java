package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Principal {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=================Teste 1: findById=================");
        Seller seller = sellerDao.findById(1);

        System.out.println(seller);
        System.out.println("=================Teste 1: findById=================");

    }

}
