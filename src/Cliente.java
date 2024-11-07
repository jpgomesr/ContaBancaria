public class Cliente {

    private int id;
    private Conta conta;
    private String nome;
    private String cpf;

    public Cliente() {}

    public Cliente(int id, Conta conta, String nome, String cpf) {
        this.id = id;
        this.conta = conta;
        this.nome = nome;
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "\nCliente: " + id +
                "\nNome: " + nome +
                "\nCPF: " + cpf +
                "\n";
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

}
