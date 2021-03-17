package br.com.atividade6;

/**
 *
 * @author denis.vieira
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface Validador extends Remote{
    
    public HashSet<Candidato> getCandidatos() throws RemoteException;
    public boolean votar(Integer numero) throws RemoteException;
}
