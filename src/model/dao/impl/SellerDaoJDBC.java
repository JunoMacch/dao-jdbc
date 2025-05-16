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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    //criar sempre um atributo connection nas classes dao para ter uma injeção de dependencia
    private Connection conn;

    //e para forçar a injeção de dependencia nós criamos um construtor
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

        //instanciar o preparedStatement como null
        PreparedStatement st = null;

        try {
            //agora montamos o statement
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)",
                    //colocamos esse comando para retornar o id do novo vendedor inserido
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            //agora passamos cada um dos valores para atribuir aos parametros de VALUES
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());

            //dessa forma que fazemos para colocar a data de nascimento
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));

            st.setDouble(4, seller.getBaseSalary());

            //aqui para passar qual o departamento do vendedor temos q navegar pelo objeto department até chegar ao id
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            //aqui testamos se a variavel é maior que 0 para identificarmos se inseriu ou não valor na tabela
            if (rowsAffected > 0) {

                //
                ResultSet rs = st.getGeneratedKeys();

                //faremos um if pois estamos inserindo apenas um dado, testamos se caso existir um valor,
                //nós pegamos o valor do id gerado, e atribuimos o id dentro do objeto seller
                if (rs.next()) {
                    //passamos a posição 1, pois id é a primeira coluna das CHAVES (st.getGeneratedKeys()
                    int id = rs.getInt(1);

                    //atribuimos o id gerado dentro de seller, para que o objeto fique populado com o novo id dele
                    seller.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                //fazemos esse bloco para tratar se a linha que querjos inserir tenha algum conflito e n seja inserida
                throw new DbException("Erro inesperado nenhuma linha foi afetada");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatment(st);
        }
    }

    @Override
    public void update(Seller seller) {
        //Instanciar como statment e resultSet como null
        PreparedStatement st = null;

        //fazer o try pq o sql pode gerar exceção
        try {
            //atribuir ao st o a conexão com o prepareStatment para relizar
            //a busca no banco de dados
            st = conn.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE Id = ?"
            );

            //agora passamos cada um dos valores para atribuir aos parametros de VALUES
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());

            //dessa forma que fazemos para colocar a data de nascimento
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));

            st.setDouble(4, seller.getBaseSalary());

            //aqui para passar qual o departamento do vendedor temos q navegar pelo objeto department até chegar ao id
            st.setInt(5, seller.getDepartment().getId());

            st.setInt(6, seller.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatment(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        //Instanciar como statment e resultSet como null
        PreparedStatement st = null;

        //fazer o try pq o sql pode gerar exceção
        try {
            //atribuir ao st o a conexão com o prepareStatment para relizar
            //a busca no banco de dados
            st = conn.prepareStatement(
                    "DELETE FROM seller WHERE Id = ?"
            );

            st.setInt(1, id);

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatment(st);
        }
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
        //Instanciar como statment e resultSet como null
        PreparedStatement st = null;
        ResultSet rs = null;

        //fazer o try pq o sql pode gerar exceção
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name AS DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name; "
            );

            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> mapDepartment = new HashMap<>();

            //como o resultado pode ser 0 ou + valores precisamos fazer while e ter uma lista instanciada (listSeller)
            //para guardar os resultados
            while(rs.next()) {

                //instanciamos um map(mapDepartment) pq não podemos instanciar mais de um departamento
                //guardarmos dentro do map cada departamento que seja instanciado e cada vez q o while
                //for percorrido sera feita uma validação para saber se o departamento já existe
                //seguindo a regra do if, instanciando o departamento somente quando o departamento não existir
                Department dep = mapDepartment.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    //instanciando departamento inexistente
                    dep = instantiateDepartment(rs);

                    //e guardamos o departamento criado no map, para checarmos se este departamento
                    //ira existir da proxima vez que o while for percorrido
                    mapDepartment.put(rs.getInt("DepartmentId"), dep);
                }

                //aqui instanciamos o vendedor, e apontamos para o departamento que esta guardado no map
                //dessa forma quando 2 resultados tiverem o mesmo departamento, eles apontarão para a mesma
                //instancia desse departamento, sem duplica-lo
                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        //Instanciar como statment e resultSet como null
        PreparedStatement st = null;
        ResultSet rs = null;

        //fazer o try pq o sql pode gerar exceção
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name AS DepName "
                        + "FROM seller INNER JOIN department "
                        + "ON seller.DepartmentId = department.Id "
                        + "WHERE DepartmentId = ? "
                        + "ORDER BY Name; "
            );

            //aqui passamos que o id tem q entrar no lugar da "?" e executamos a query
            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> mapDepartment = new HashMap<>();

            //como o resultado pode ser 0 ou + valores precisamos fazer while e ter uma lista instanciada (listSeller)
            //para guardar os resultados
            while(rs.next()) {

                //instanciamos um map(mapDepartment) pq não podemos instanciar mais de um departamento
                //guardarmos dentro do map cada departamento que seja instanciado e cada vez q o while
                //for percorrido sera feita uma validação para saber se o departamento já existe
                //seguindo a regra do if, instanciando o departamento somente quando o departamento não existir
                Department dep = mapDepartment.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    //instanciando departamento inexistente
                    dep = instantiateDepartment(rs);

                    //e guardamos o departamento criado no map, para checarmos se este departamento
                    //ira existir da proxima vez que o while for percorrido
                    mapDepartment.put(rs.getInt("DepartmentId"), dep);
                }

                //aqui instanciamos o vendedor, e apontamos para o departamento que esta guardado no map
                //dessa forma quando 2 resultados tiverem o mesmo departamento, eles apontarão para a mesma
                //instancia desse departamento, sem duplica-lo
                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }
}
