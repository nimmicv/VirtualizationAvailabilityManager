package com.vmware.vm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Timer;

import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class AvailabilityManager {

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String vmName= null;
		VirtualMachine vm = null;
		ServiceInstance si;
		
		//URL url = new URL("https://192.168.209.101/sdk"); 
		URL url = new URL("https://130.65.133.10/sdk");
		si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		if(args.length == 1)
		{
			vmName= args[0];
		}
		else
		{
			vmName = "T13_vm02_Ubuntu32_NimmiCV";
			//vmName = "VM2";
		}
		
		if(vmName !=null)
		{
			vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",vmName);
		}
		
		if(vm != null)
		{
			//1. Show Statistics.
			VMStatistics st = new VMStatistics();
			st.showVmInfo(vm);
			
			//4. Set Alarm to prevent user Power Off.
			VMPowerOffAlarm userAlarm = new VMPowerOffAlarm();
			userAlarm.setTurnOFfAlarm(si,vm,"user_alarm_Nimmi");
			
			//2. Refresh Cache every 20 minutes
			SnapshotVM snp = new SnapshotVM(vm,"vmSnapshot");
	        Thread t1 = new Thread(snp);
	         
	        //3. Ping every 20 seconds and failover to another VM
	        System.out.println("IP Address = " + vm.getGuest().getIpAddress());
	        PingVM  pvm= new PingVM(vm,"130.65.133.159",t1,si);
	        //PingVM  pvm= new PingVM(vm,"192.168.209.130",t1,si);
	        Thread t2 = new Thread(pvm);
	         
	         
	         t1.start();
	         t2.start();
			
			
			
		}
		
		else
		{
			System.out.println("No Virtual Machine " + vmName +" found!!");
		}
		
		//si.getServerConnection().logout();
		
		
	}
}
