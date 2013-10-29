package com.vmware.vm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class PingVM implements Runnable{
	
	private VirtualMachine vmName;
	String vmIP;
	Thread t;
	ServiceInstance si;
	static int loopme = 2;

	public PingVM(VirtualMachine vm,String hostName,Thread t,ServiceInstance si)
	{
		this.vmName = vm;
		this.vmIP = hostName;
		this.t = t;
		this.si = si;
	}
	
	static int s = 1;

	@Override
	public void run() {
	while(true)
	{
	try
	{
		// TODO Auto-generated method stub
		//String hostName = "192.168.209.101";
		Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 "+vmIP);
		int returnVal = p1.waitFor();
		boolean status = (returnVal==0);
		System.out.println("\n");
//        InetAddress inet = InetAddress.getByName(hostName);
//        boolean status = inet.isReachable(5000);
        if (status)
        {
        	loopme=2;
            System.out.println(vmIP + " VM Reached\t" );
        }
        else
        {
        	System.out.println(vmIP+ " VM Unreachable");
        	Process p12 = java.lang.Runtime.getRuntime().exec("ping -c 1 130.65.133.13");
    		int returnVal1 = p1.waitFor();
    		boolean status1 = (returnVal==0);
    		{
    			System.out.println(" Host Reachable");
    		}
        	//Ping twice before taking any action
        	if(loopme>0)
        	{
        		loopme --;
        		
        	}
        	else
        	{
	        	t.stop();
	        	loopme=2;
	            CloneVM clone = new CloneVM();
	            try
	            {
	            	clone.CloneFromSnapshot(vmName,si);
	            }
	            catch(Exception ex)
	            {
	            	ex.printStackTrace();
	            }
	            return;
        	}
        }
        Thread.sleep(20000);

    }
     catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
}
