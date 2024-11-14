import Exceptions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Executavel {
    private static final Scanner sc = new Scanner(System.in);
    private static final CRUDConta dbConta = new CRUDConta();
    private static final CRUDHistorico dbHistorico = new CRUDHistorico();
    private static final CRUDCliente dbCliente = new CRUDCliente();

    public static void main(String[] args) {
        do {
            mostrarOpcoesMenu();
            int opcaoMenu = sc.nextInt();
            try {
                executarOpcaoMenu(opcaoMenu);
            } catch (ContaInexistenteException | ContaJaCadastradaException e) {
                System.err.println(e.getMessage());
            }
        } while (true);
    }

    public static void cadastroConta() {
        System.out.print("Número da Conta: ");
        int numero = sc.nextInt();
        try {
            dbConta.readOne(numero);
        } catch (ContaInexistenteException e) {
            Cliente titular = buscaCliente();
            System.out.print("Limite: ");
            double limite = sc.nextDouble();
            dbConta.create(new Conta(numero, titular, limite));
            return;
        }
        throw new ContaJaCadastradaException();
    }

    public static void cadastroCliente() {
        System.out.print("Nome: ");
        String nome = sc.next();
        do {
            System.out.println("CPF (apenas números): ");
            String cpf = sc.next();
            try {
                dbCliente.create(new Cliente(nome, cpf));
                break;
            } catch (CpfDuplicadoException e) {
                System.err.println(e.getMessage());
            }
        } while (true);
    }

    private static void removeConta() {
        Conta conta = buscaConta();
        dbConta.delete(conta.getNumero());
    }

    private static void editaConta() {
        Conta conta = buscaConta();
        Cliente titular = buscaCliente();
        conta.setTitular(titular);
        System.out.print("Limite: ");
        conta.setLimite(sc.nextDouble());
        dbConta.update(conta);
    }

    private static Conta buscaConta() {
        System.out.println(dbConta.readAll());
        System.out.print("Numero da Conta: ");
        int numero = sc.nextInt();
        return dbConta.readOne(numero);
    }

    private static Cliente buscaCliente() {
        System.out.println(dbCliente.readAll());
        System.out.print("Id do cliente: ");
        int numero = sc.nextInt();
        return dbCliente.readOne(numero);
    }

    private static void mostrarOpcoesMenu() {
        System.out.print("""
                MENU:
                
                1 - Cadastro
                2 - Editar
                3 - Deletar
                4 - Mostrar Todos(as)
                5 - Selecionar
                6 - Sair
                
                >\t""");
    }

    private static void mostrarOpcoesConta() {
        System.out.print("""
                OPERAÇÕES
                
                1 - Depósito
                2 - Saque
                3 - Transferência
                4 - Listar Transferências
                5 - Saldo
                6 - Voltar
                
                >\t""");
    }

    public static void mostrarOpcoesCadastro() {
        System.out.print("""
                Cadastro
                
                1 - Cliente
                2 - Conta
                3 - Voltar
                
                >\t""");
    }

    public static void mostrarOpcoesListar() {
        System.out.print("""
                Listar
                
                1 - Cliente
                2 - Conta
                3 - Voltar
                
                >\t""");
    }

    public static void mostrarOpcoesDelete() {
        System.out.print("""
                Deletar
                
                1 - Cliente
                2 - Conta
                3 - Voltar
                
                >\t""");
    }

    public static void executarOpcaoDelete(int opcao) {
        switch (opcao) {
            case 1:
                dbCliente.readAll();
                dbCliente.delete(sc.nextInt());
                break;
            case 2:
                dbConta.readAll();
                dbConta.delete(sc.nextInt());
                break;
            case 3:
                System.out.println("Até mais!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    public static void executarOpcaoListar(int opcao) {
        switch (opcao) {
            case 1:
                System.out.println(dbCliente.readAll());
                break;
            case 2:
                System.out.println(dbConta.readAll());
                break;
            case 3:
                System.out.println("Até mais!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    public static void executarOpcoesCadastro(int opcao) {
        switch (opcao) {
            case 1:
                cadastroCliente();
                break;
            case 2:
                cadastroConta();
                break;
            case 3:
                System.out.println("Até mais!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    private static void executarOpcaoMenu(int opcao) {
        switch (opcao) {
            case 1:
                mostrarOpcoesCadastro();
                executarOpcoesCadastro(sc.nextInt());
                break;
            case 2:
                editaConta();
                break;
            case 3:
                mostrarOpcoesDelete();
                executarOpcaoDelete(sc.nextInt());
                break;
            case 4:
                mostrarOpcoesListar();
                executarOpcaoListar(sc.nextInt());
                break;
            case 5:
                int opcaoConta = 0;
                Conta conta = buscaConta();
                do {
                    mostrarOpcoesConta();
                    opcaoConta = sc.nextInt();
                    do {
                        try {
                            executarOpcaoConta(conta, opcaoConta);
                            break;
                        } catch (ContaInexistenteException | ValorInvalidoException e) {
                            System.err.println(e.getMessage());
                        } catch (SaldoInsuficienteException | LimiteInsuficienteException | PropriaContaException e) {
                            System.err.println(e.getMessage());
                            break;
                        }
                    } while (true);
                } while (opcaoConta != 6);
                break;
            case 6:
                System.out.println("Até mais!");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    private static double solicitarValor() {
        System.out.print("Valor: R$ ");
        return sc.nextDouble();
    }

    private static void executarOpcaoConta(Conta conta, int opcao) {
        switch (opcao) {
            case 1: {
                conta.deposito(solicitarValor());
                dbConta.update(conta);
                break;
            }
            case 2: {
                conta.saque(solicitarValor());
                dbConta.update(conta);
                break;
            }
            case 3: {
                Conta contaBeneficiario = buscaConta();
                double valor = solicitarValor();
                conta.transferencia(valor, contaBeneficiario);
                dbConta.update(conta);
                dbConta.update(contaBeneficiario);
                dbHistorico.create(conta.getNumero(), contaBeneficiario.getNumero(), valor);
                break;
            }
            case 4: {
                System.out.println(dbHistorico.readAll(conta));
                break;
            }
            case 5: {
                System.out.println("Saldo: R$ " + dbConta.readOne(conta.getNumero()).getSaldo());
                break;
            }
            case 6: {
                System.out.println("Até mais!");
                break;
            }
            default: {
                System.out.println("Opção inválida");
                break;
            }
        }
    }
}
