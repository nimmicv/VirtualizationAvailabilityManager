package com.vmware.vm;

import java.net.URL;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;



public class SnapshotVM implements Runnable
{
	int count = 1;
	public VirtualMachine vmMachine;
	String snapshotname; 
	public SnapshotVM(VirtualMachine vm, String snapshotname)
	{
		this.vmMachine = vm;
		this.snapshotname = snapshotname;
		
	}
	
	@Override
	public void run() {
		System.out.println("Inside snapshot");
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				String ipaddress = vmMachine.getGuest().getIpAddress();
				Process p = Runtime.getRuntime().exec("ping -c 1 130.65.133.159");
				int returnVal = p.waitFor();
				boolean status = (returnVal==0);
				if(status)
				{
					String desc = "Creating snapshot " + count;
					System.out.println("\n" );
					 Task task = vmMachine.removeAllSnapshots_Task();      
				      if(task.waitForMe()== Task.SUCCESS) 
				      {
				        System.out.println("Refreshing Cache");
				      }
					 
				      Task task2 = vmMachine.createSnapshot_Task(
				          snapshotname, desc, false, false);
				      if(task2.waitForMe()==Task.SUCCESS)
				      {
				        System.out.println("Created snapshot " + count);
				      }
				   count++;
				   long milliSecTimer = 20*60*1000; 
				   Thread.sleep(milliSecTimer);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
		}
		
	}
	
}
