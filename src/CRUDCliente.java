import Exceptions.ContaInexistenteException;
import Exceptions.CpfDuplicadoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDCliente {
    BancoDeDados db = new BancoDeDados();

    public List<Cliente> readAll() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tb_cliente");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                clientes.add(new Cliente(id, nome, cpf));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return clientes;
    }

    public Cliente readOne(int idCliente) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM DATABASE WHERE id = ?");
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                return new Cliente(id, nome, cpf);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new ContaInexistenteException();
    }

    public Cliente create(Cliente cliente) throws CpfDuplicadoException {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tb_cliente (nome, cpf) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1));
                return cliente;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new CpfDuplicadoException();
    }

    public void delete(int id) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM tb_cliente WHERE id = ?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void update(Cliente cliente) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE tb_cliente SET nome = ?, cpf = ? WHERE id = ?");
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setInt(3, cliente.getId());
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
