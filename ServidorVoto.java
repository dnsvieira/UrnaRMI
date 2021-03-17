package br.com.atividade6;


/**
 *
 * @author denis.vieira
 */

import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServidorVoto implements Runnable {

    private Urna urna;
    private ScheduledExecutorService executorService;
    
    @Override
    public void run() {
        
        try {
             this.urna = this.prepararUrna();
            
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi:///Urna", this.urna);
            
            System.out.println("Pronta para receber votos!");
        } catch (RemoteException | MalformedURLException ex) {
            System.err.println(ex);
            System.exit(1);
        }

        this.iniciarApurador();
        this.escutarComandos();
    }

    private Urna prepararUrna() throws RemoteException {

        final Urna urnaVazia = new Urna();
        
        System.out.println("*** Iniciando Urna ***\n");
        System.out.print("Cadastrar o candidato\n");
        System.out.print("Digite o nome : numero separado por virgula:  \n");
        System.out.println("Tecle [enter] para gravar");
        System.out.println("Digite aqui: ");
        final String comando = Console.readCommand(new Scanner(System.in));       
        if (comando != null && !comando.isEmpty()) {
            
            final String[] arrayCandidatos = comando.split(",");
            
            for (String dadosCandidato : arrayCandidatos) {
                
                final String nome = dadosCandidato.split(":")[0];
                
                final String numero = dadosCandidato.split(":")[1];
                
                urnaVazia.adicionaCandidato(new Candidato(nome, 
                        Integer.parseInt(numero)));
            }
        }
        
        this.mostrarCandidatos(urnaVazia);
        
        return urnaVazia;
    }

    private void escutarComandos() {

        boolean exit = false;
        
        try {
            final Scanner teclado = new Scanner(System.in);

            System.out.println("\n*** Comandos ***");
            System.out.println("digite Cand = ver candidatos");
            System.out.println("digite Cont = fecha votacao e contabiliza\n");

            while (!exit) {
                System.out.print("urna@localhost$> ");

                final String comando = Console.readCommand(teclado);

                switch (comando) {
                    case "Cont":
                        this.urna.calcularParcial();
                        this.executorService.shutdown();
                        System.exit(0);
                        break;
                    case "Cand":
                        this.mostrarCandidatos(this.urna);
                        break;
                    default:
                        System.err.println("Comando desconhecido!");
                        break;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void iniciarApurador() {
       
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        
        this.executorService.scheduleAtFixedRate(() -> 
            { urna.calcularParcial(); }, 5, 5, TimeUnit.SECONDS);
    }
  
    private void mostrarCandidatos(Urna urna) throws RemoteException {

        System.out.println("Candidatos disponiveis para votação:");
        
        final Set<Candidato> candidatos = urna.getCandidatos();
        
        candidatos.stream().forEach((candidato) -> {
            System.out.println(String.format("-> Nome %s, número %s", 
                    candidato.getNome(), candidato.getNumero()));
        });
    }
   
    public static void main(String[] args) {
        EventQueue.invokeLater(new ServidorVoto());
    }
}