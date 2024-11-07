import Exceptions.ContaInexistenteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDHistorico {

    private final BancoDeDados db = new BancoDeDados();

    public void create(int remetente, int destinatario, double valor) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tb_historico (remetente, destinatario, valor, data_transacao) " +
                    "VALUES (?, ?, ?, ?)");
            java.util.Date data = new java.util.Date();
            java.sql.Timestamp dataTimestamp = new java.sql.Timestamp(data.getTime());
            ps.setInt(1, remetente);
            ps.setInt(2, destinatario);
            ps.setDouble(3, valor);
            ps.setTimestamp(4, dataTimestamp);
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Transferencia> readAll(Conta conta) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tb_historico WHERE remetente = ?");
            ps.setInt(1, conta.getNumero());
            ResultSet rs = ps.executeQuery();
            List<Transferencia> transferencias = new ArrayList<>();
            while(rs.next()) {
                int remetente = rs.getInt("remetente");
                int destinatario = rs.getInt("destinatario");
                double valor = rs.getDouble("valor");
                Timestamp data = rs.getTimestamp("data_transacao");
                transferencias.add(new Transferencia(remetente, destinatario, valor, data));
            }
            return transferencias;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        throw new ContaInexistenteException(); // lançamento de exception para quando n faz sentido retornar lista vazia
    }

}
