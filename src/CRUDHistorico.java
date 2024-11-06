import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
