import Exceptions.*;

import java.util.List;
import java.util.Scanner;

public class Executavel {
    private static final Scanner sc = new Scanner(System.in);
    private static final CRUDConta dbConta = new CRUDConta();
    private static final CRUDHistorico dbHistorico = new CRUDHistorico();
    private static final CRUDCliente dbCliente = new CRUDCliente();

    public static void main(String[] args) {
        dbCliente.create(new Cliente("joão", "88899933322"));
        dbCliente.readAll();

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
                4 - Mostrar Todas
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

    private static void executarOpcaoMenu(int opcao) {
        switch (opcao) {
            case 1:
                cadastroConta();
                break;
            case 2:
                editaConta();
                break;
            case 3:
                removeConta();
                break;
            case 4:
                System.out.println(dbConta.readAll());
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
