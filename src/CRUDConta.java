import Exceptions.ContaInexistenteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDConta {

    private final BancoDeDados db = new BancoDeDados();
    private final CRUDCliente dbCliente = new CRUDCliente();

    public void create(Conta conta) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tb_conta (numero, id_cliente, saldo, limite) " +
                    "VALUES (?, ?, ?, ?)");
            ps.setInt(1, conta.getNumero());
            ps.setInt(2, conta.getTitular().getId());
            ps.setDouble(3, conta.getSaldo());
            ps.setDouble(4, conta.getLimite());
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Conta readOne(int id) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tb_conta WHERE numero = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int num = rs.getInt("numero");
                int idCliente = rs.getInt("id_cliente");
                double saldo = rs.getDouble("saldo");
                double limite = rs.getDouble("limite");
                return new Conta(num, dbCliente.readOne(idCliente), saldo, limite);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new ContaInexistenteException();
    }

    public List<Conta> readAll() {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tb_conta");
            ResultSet rs = ps.executeQuery();
            List<Conta> contas = new ArrayList<>();
            while(rs.next()) {
                int num = rs.getInt("numero");
                int idCliente = rs.getInt("id_cliente");
                double saldo = rs.getDouble("saldo");
                double limite = rs.getDouble("limite");
                contas.add(new Conta(num, dbCliente.readOne(idCliente), saldo, limite));
            }
            return contas;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new ContaInexistenteException(); // lançamento de exception para quando n faz sentido retornar lista vazia
    }

    public void update(Conta conta) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE tb_conta SET id_cliente = ?, saldo = ?, limite = ? WHERE numero = ?");
            ps.setInt(1, conta.getTitular().getId());
            ps.setDouble(2, conta.getSaldo());
            ps.setDouble(3, conta.getLimite());
            ps.setInt(4, conta.getNumero());
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void delete(int id) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM tb_conta WHERE numero = ?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
