import Exceptions.ContaInexistenteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDConta {

    private final BancoDeDados db = new BancoDeDados();

    public void create(Conta conta) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tb_conta (numero, titular, saldo, limite) " +
                    "VALUES (?, ?, ?, ?)");
            ps.setInt(1, conta.getNumero());
            ps.setString(2, conta.getTitular());
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
                String titular = rs.getString("titular");
                double saldo = rs.getDouble("saldo");
                double limite = rs.getDouble("limite");
                return new Conta(num, titular, saldo, limite);
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
                String titular = rs.getString("titular");
                double saldo = rs.getDouble("saldo");
                double limite = rs.getDouble("limite");
                contas.add(new Conta(num, titular, saldo, limite));
            }
            return contas;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new ContaInexistenteException(); // lançamento de exception para quando n faz sentido retornar lista vazia
    }

    public void update(int id, Conta conta) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE tb_conta SET titular = ?, saldo = ?, limite = ? WHERE numero = ?");
            ps.setString(1, conta.getTitular());
            ps.setDouble(2, conta.getSaldo());
            ps.setDouble(3, conta.getLimite());
            ps.setInt(4, id);
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
