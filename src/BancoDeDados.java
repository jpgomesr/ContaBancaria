import java.util.ArrayList;
import java.util.List;

public class BancoDeDados {
    private List<Conta> contas = new ArrayList<Conta>();

    public void inserirConta(Conta conta) {
        this.contas.add(conta);
    }

    public void deletarConta(Conta conta) {
        this.contas.remove(conta);
    }

    public Conta buscarConta(int numero) {
        for (Conta conta : contas) {
            if (conta.getNumero() == numero) {
                return conta;
            }
        }
        throw new ContaInexistenteException();
    }

    public List<Conta> buscarContas() {
        return this.contas;
    }

    public void atualizarConta(Conta conta) {
        int indice = contas.indexOf(conta);
        if (indice == -1) {
            throw new ContaInexistenteException();
        }
        this.contas.set(indice, conta);
    }

    private void editaConta() {
        Conta conta = buscarConta(contas.size());
    }
}
