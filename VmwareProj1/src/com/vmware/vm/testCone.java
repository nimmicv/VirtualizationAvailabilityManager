package com.vmware.vm;


import java.net.URL;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.HostDatastoreSystem;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.Folder;

public class testCone  {
	
	public static void main(String[] args) throws Exception
	{
		String dcName = "T13_DC_Lab2";
		URL url = new URL("https://130.65.133.10/sdk"); //192.168.209.101
		ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
		
		Folder rootFolder = si.getRootFolder();
		rootFolder.getChildEntity();
		VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine","T13_vm02_Ubuntu32_NimmiCV");//T13_vm02_Ubuntu32_NimmiCV
		 Datacenter dc = (Datacenter) new InventoryNavigator(
			        rootFolder).searchManagedEntity("Datacenter", dcName);
		 Folder vmFolder = dc.getVmFolder();
		 System.out.println(vmFolder.getName());
		CloneFromSnapshot(vm,si);
	}
		
	public static void CloneFromSnapshot(VirtualMachine vm,ServiceInstance si) throws Exception
	{
	 String newName = "T13_vm02_Ubuntu32_NimmiCV";
		
		Task task = vm.rename_Task(newName);
		System.out.println("\n Renaming the VM clone task. " + "Please wait ...");

		String status = task.waitForMe();
		if(status==Task.SUCCESS)
		{
			System.out.println(vm.getName() + " Renamed successfully!");
			VirtualMachine clonedVM = (VirtualMachine) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("VirtualMachine",newName);
			AlarmManager am = si.getAlarmManager();
			Alarm[] amNew = am.getAlarm(clonedVM);
			//amNew[0].removeAlarm();
			System.out.println(amNew[0].getAlarmInfo().getName());
			amNew[0].removeAlarm();
			//System.out.println("Migrating "+clonedVM.getName());
			//MigrateVM mvm = new MigrateVM();
			//mvm.migrateToNewHost(clonedVM,si,"130.65.133.13");
		}
		else
		{
			System.out.println("Failure -: VM cannot be cloned");
		}
//		MigrateVM mvm = new MigrateVM();
//		mvm.migrateToNewHost(vm,si,"130.65.133.13");//192.168.209.102
		//si.getServerConnection().logout();
	}

	
}

