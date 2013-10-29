package com.vmware.vm;

import java.net.MalformedURLException;
import java.net.URL;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class MigrateVM {
	
public void migrateToNewHost(VirtualMachine vm,ServiceInstance si, String newHostName) throws Exception
{
	System.out.println("Migration");
	Folder rootFolder = si.getRootFolder();
	HostSystem newHost = (HostSystem) new InventoryNavigator(
	        rootFolder).searchManagedEntity(
	            "HostSystem", newHostName);
	    ComputeResource cr = (ComputeResource) newHost.getParent();
	    
	    /*String[] checks = new String[] {"cpu", "software"};
	    HostVMotionCompatibility[] vmcs =
	      si.queryVMotionCompatibility(vm, new HostSystem[] 
	         {newHost},checks );
	    
	    String[] comps = vmcs[0].getCompatibility();
	    if(checks.length != comps.length)
	    {
	      System.out.println("CPU/software NOT compatible. Exit.");
	      return;
	    }*/
	    
	    System.out.println(vm.getName());
	    System.out.println(newHost.getName());
		    Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost,
		        VirtualMachineMovePriority.highPriority, 
		        VirtualMachinePowerState.poweredOff);
		    System.out.println("\n Launching the VM migrate task. " + "Please wait ...");
		    if(task.waitForMe()==Task.SUCCESS)
		    {
		      System.out.println("VMotioned!");
		    }
		    else
		    {
		      System.out.println("VMotion failed!");
		      TaskInfo info = task.getTaskInfo();
		      System.out.println(info.getError().getFault());
		
	         }
		    si.getServerConnection().logout();
}
}


