package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Cliente;
import modelDominio.Usuario;
import modelDominio.Vendedor;

public class UsuarioDao {
    private Connection con;

    public UsuarioDao() {
        this.con = Conector.getConnection();
    }
    
    public Usuario efetuarLogin(Usuario usuario) {
        PreparedStatement statement = null;
        Usuario usuarioSelect = null;
        ResultSet resultSet = null;
        
        try {
            String select = "SELECT *"
                            +" FROM usuario"
                            +" WHERE EMAIL = ? AND senha = ?;";
            statement = con.prepareStatement(select);
            statement.setString(1, usuario.getLogin());
            statement.setString(2, usuario.getSenha());
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                if (resultSet.getInt("tipo") == 1) {
                    usuarioSelect = new Cliente(resultSet.getString("cpf"),resultSet.getInt("codUsuario"), resultSet.getString("nome"),
                                    resultSet.getString("EMAIL"), resultSet.getString("senha"), resultSet.getString("endereco"));
                    
                } else {
                    usuarioSelect = new Vendedor(resultSet.getString("cnpj"), resultSet.getInt("codUsuario"), resultSet.getString("nome"),
                                    resultSet.getString("EMAIL"), resultSet.getString("senha"), resultSet.getString("endereco"));
                }     
            }  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {resultSet.close();} catch (Exception e) {/*Ignorado*/}
            try {statement.close();} catch (Exception e) {/*Ignorado*/}
            try {con.close();} catch (Exception e) {/*Ignorado*/}
            return usuarioSelect;
        }
    }
    
    public boolean vendedorInserir(Vendedor vendedor) {
        boolean resultado;
        PreparedStatement stmt = null;
        
        try {
            con.setAutoCommit(false);
            
            String sql = ("INSERT INTO USUARIO (NOME, EMAIL, SENHA, ENDERECO, CNPJ, CPF, TIPO)"+
                          " VALUES(?,?,?,?,?,NULL,2)");
            stmt = con.prepareStatement(sql);
            stmt.setString(1, vendedor.getNome());
            stmt.setString(2, vendedor.getLogin());
            stmt.setString(3, vendedor.getSenha());
            stmt.setString(4, vendedor.getEndereco());
            stmt.setString(5, vendedor.getCnpj());
            
            stmt.execute();
            con.commit();
            resultado = true;
        } catch (SQLException e) {
            try {
                con.rollback();
                e.printStackTrace();
                resultado = false;
            } catch (SQLException ex) {
                ex.printStackTrace();   
                resultado = false;
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = false;
            }
        }
        return resultado;
    }
    
    public ArrayList<Cliente> getListaClientes (Vendedor vendedor) {
        PreparedStatement stmt = null;
        ArrayList<Cliente> listaClientes;
        
        try {
            String sql = "SELECT USUARIO.*" +
                         " FROM USUARIO" +
                         " INNER JOIN VENDA ON VENDA.CODCLIENTE = USUARIO.CODUSUARIO " +
                         " INNER JOIN ITENSVENDA ON ITENSVENDA.CODVENDA = VENDA.CODVENDA" +
                         " INNER JOIN PRODUTO ON ITENSVENDA.CODPRODUTO = PRODUTO.CODPRODUTO" +
                         " WHERE PRODUTO.CODVENDEDOR = ?";
            
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, vendedor.getCodUsuario()); 
            
            ResultSet res = stmt.executeQuery();
            
            listaClientes = new ArrayList<>();          
            while (res.next()) {                
                Cliente clienteSel = new Cliente(res.getString("CPF"), res.getInt("CODUSUARIO"), 
                        res.getString("NOME"), res.getString("ENDERECO"));
                
                if (!listaClientes.contains(clienteSel)) {
                    listaClientes.add(clienteSel);
                }                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            listaClientes = null;
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaClientes;
    }
    
    public boolean clienteInserir(Cliente cliente) {
        boolean resultado;
        PreparedStatement stmt = null;
        
        try {
            con.setAutoCommit(false);
            
            String sql = "INSERT INTO USUARIO (NOME, EMAIL, SENHA, ENDERECO, CPF, TIPO)"+
                        " VALUES (?,?,?,?,?,1)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getLogin());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getCpf());
            
            stmt.execute();
            con.commit();
            resultado = true;
        } catch (SQLException e) {
            try {
                con.rollback();
                e.printStackTrace();
                resultado = false;
            } catch (SQLException ex) {
                e.printStackTrace();
                resultado = false;
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = false;
            }
        }
        return resultado;
    }
    
    public boolean validaEmail(String emailDestinatario) {
        boolean resultado;        
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            String select = "SELECT *"
                            +" FROM usuario"
                            +" WHERE email = ?;";
            statement = con.prepareStatement(select);
            statement.setString(1, emailDestinatario);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                resultado = true;
            } else
                resultado = false;
            
        } catch (Exception e) {
            e.printStackTrace();
            resultado = false;
        } finally {
            try {resultSet.close();} catch (Exception e) {/*Ignorado*/}
            try {statement.close();} catch (Exception e) {/*Ignorado*/}
            try {con.close();} catch (Exception e) {/*Ignorado*/}
        }
        return resultado;
    }
    
    public boolean alterarSenhaRecup(Usuario usr) {
        boolean resultado;
        PreparedStatement statement = null;
        
        try {
            con.setAutoCommit(false);
            
            String sql = "UPDATE USUARIO SET SENHA = ?"+
                    " WHERE EMAIL = ?";
            
            statement = con.prepareStatement(sql);
            statement.setString(1, usr.getSenha());
            statement.setString(2, usr.getLogin());
            
            statement.execute();
            con.commit();
            resultado = true;
        } catch (SQLException e) {
            try {
                con.rollback();
                e.printStackTrace();
                return false;
            } catch (SQLException ex) {
                return false;
            }
        } finally {
            try {
                statement.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                return false;
            }
        }
        return resultado;
    }
    
}
