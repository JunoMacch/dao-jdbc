package application;

import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Principal {

    public static void main(String[] args) {

        Department department = new Department(1, "Techs");

        Seller seller = new Seller(1, "Junior", "ju@gmail.com", new Date(), 3000.0, department);

        System.out.println(seller);

    }

}
