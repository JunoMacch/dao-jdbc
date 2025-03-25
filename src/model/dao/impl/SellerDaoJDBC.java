package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    //criar sempre um atributo connection nas classes dao para ter uma injeção de dependencia
    private Connection conn;

    //e para forçar a injeção de dependencia nós criamos um construtor
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        //Instanciar como statment e resultSet como null
        PreparedStatement st = null;
        ResultSet rs = null;

        //fazer o try pq o sql pode gerar exceção
        try {
            //atribuir ao st o a conexão com o prepareStatment para relizar
            //a busca no banco de dados
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name AS DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?"
            );

            //agora configuramos a interrogação que foi passada no argumento
            //da query sql. Indicando que a primeira interrogação(1) do preparedStatment
            //recebe o id que chegou de parametro de entrada na função
            st.setInt(1, id);

            //atribuimos ao ResultSet rs o st.executeQuery(), para executar o comando sql
            //e inserir dentro do ResultSet rs
            rs = st.executeQuery();

            //quando executamos uma consulta sql e o resultado entra no resultSet
            //o resultSet aponta para a posição 0, então é necessário fazer um if com rs.next
            //para testar se obtivemos algum resultado, do contrario pulamos para fora if com null
            if(rs.next()) {

                //Ler o metodo instantiateDepartment caso tenha alguma duvida pq o codigo foi refatorado para metodo
                Department dep = instantiateDepartment(rs);

                //Ler o metodo instantiateSeller caso tenha alguma duvida, pq o codigo foi refatorado para metodo
                Seller seller = instantiateSeller(rs,dep);

                return seller;
            }
            return null;

        //é necessário passar a sqlExcpetion pois sempre pode dar erro
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            //ao final de tudo, nós fechamos apenas o statment e o , mas não fechamos a conexão poris
            //podemos querer fazer alguma outra consulta.
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }

    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        //aqui funciona da mesma forma que o Department settamos nos atributos passados na classe seller
        //os valores recuperados  na query sql com o get dentro dos metodos set
        //e para passarmos os atributos associados nós temos que ter esse atributo ja construido
        //por exemplo o department dep que instanciamos primeiro, para conseguirmos instanciar o Seller
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        //agora instanciamos um departamento para atribuir os valores da consulta sql
        //a uma instancia de Department, pegando com set+atributo da classe Department
        //para setarmos o valor que veio na consulta sql pegando eles com o get dentro de rs.get
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
