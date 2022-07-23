/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import rmi_chat.Client_Interface;


public class Chatter {

	public String name;
	public Client_Interface client;
	
	//constructor
	public Chatter(String name, Client_Interface client){
		this.name = name;
		this.client = client;
	}
        	public String getName(){
		return name;
	}
	public Client_Interface getClient(){
		return client;
	}
	
	
}


