/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_chat;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for client classes
 * A method to receive a string
 * A method to update changes to user list

public interface Client_Interface extends Remote{

	public void messageFromServer(String message) throws RemoteException;

	public void updateUserList(String[] currentUsers) throws RemoteException;
	
}
/**
 * 
 * 
 * 
 *
 */