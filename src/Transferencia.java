import java.sql.Timestamp;

public class Transferencia {

    private int remetente;
    private int destinatario;
    private double valor;
    private Timestamp data;

    public Transferencia(int remetente, int destinatario, double valor, Timestamp data) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.valor = valor;
        this.data = data;
    }

    @Override
    public String toString() {
        return "\nTransferencia:" +
                "\nRemetente: " + remetente +
                "\nDestinatario: " + destinatario +
                "\nValor: " + valor +
                "\nData: " + data +
                "\n";
    }

    public int getRemetente() {
        return remetente;
    }

    public int getDestinatario() {
        return destinatario;
    }

    public double getValor() {
        return valor;
    }

    public Timestamp getData() {
        return data;
    }

}
