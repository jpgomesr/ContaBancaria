import Exceptions.*;

import java.util.Scanner;

public class Executavel {
    private static Scanner sc = new Scanner(System.in);
    private static CRUDConta db = new CRUDConta();

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
            db.readOne(numero);
        } catch (ContaInexistenteException e) {
            System.out.print("Titular: ");
            String titular = sc.next();
            System.out.print("Limite: ");
            double limite = sc.nextDouble();
            db.create(new Conta(numero, titular, limite));
            return;
        }
        throw new ContaJaCadastradaException();
    }

    private static void removeConta() {
        Conta conta = buscaConta();
        db.delete(conta.getNumero());
    }

    private static void editaConta() {
        Conta conta = buscaConta();
        System.out.print("Titular: ");
        String titular = sc.next();
        System.out.print("Limite: ");
        double limite = sc.nextDouble();
        conta.setTitular(titular);
        conta.setLimite(limite);
//        db.atualizarConta(conta);
    }

    private static Conta buscaConta() {
        System.out.println(db.readAll());
        System.out.print("Numero da Conta: ");
        int numero = sc.nextInt();
        return db.readOne(numero);
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
                4 - Saldo
                5 - Voltar
                
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
                System.out.println(db.readAll());
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
                } while (opcaoConta != 5);
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
                break;
            }
            case 2: {
                conta.saque(solicitarValor());
                break;
            }
            case 3: {
                conta.transferencia(solicitarValor(), buscaConta());
                break;
            }
            case 4: {
                System.out.println("Saldo: R$ " + db.readOne(conta.getNumero()).getSaldo());
                break;
            }
            case 5: {
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
