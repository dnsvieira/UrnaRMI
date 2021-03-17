package br.com.atividade6;

/**
 *
 * @author denis.vieira
 */

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.Set;

public class EstacaoVoto implements Runnable {

    private Validador urna;

    @Override
    public void run() {
        try {
            this.urna = this.getUrnaService();
            System.out.println("Pronto para votar!");
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }

        this.capturarVotos();
    }

    /**
     *
     */
    public void capturarVotos() {

        boolean exit = false;

        try {
            final Scanner teclado = new Scanner(System.in);

            
            System.out.println("Escolha um candidato e em seguida   ");
            System.out.println("digite o numero correspondente a ele");
            System.out.println("para contabilizar 1 voto            ");
            System.out.println("************************************");

            while (!exit) {
                
                System.out.println("\n*****Candidatos****");
                this.mostrarCandidatos();
                System.out.println("*********************");

                System.out.print("Digite o número do candidato: ");
                final String voto = Console.readCommand(teclado);
                
                try {
                    Integer numero = Integer.parseInt(voto);
                    this.urna.votar(numero);
                } catch (NumberFormatException ex) {
                    System.err.println("voto invalido!");
                }
            }
        } catch (Exception ex) {
            System.err.println("Votação encerrada!");
        }
    }

    private void mostrarCandidatos() throws Exception {
        
        final Set<Candidato> candidatos = this.urna.getCandidatos();
        
        candidatos.stream().forEach((candidato) -> {
            System.out.println(String.format("-> Nome %s, número %s", 
                    candidato.getNome(), candidato.getNumero()));
        });
    }

    private Validador getUrnaService() throws Exception {
        return (Validador) Naming.lookup("rmi://localhost/Urna");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new EstacaoVoto());
    }
}