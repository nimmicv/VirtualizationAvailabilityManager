package com.vmware.vm;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.Folder;

public class CloneVM  {
		
	public void CloneFromSnapshot(VirtualMachine vm,ServiceInstance si) throws Exception
	{
		System.out.println("Inside clone");
		Datacenter dc = (Datacenter) new InventoryNavigator(
				si.getRootFolder()).searchManagedEntity("Datacenter", "T13_DC_Lab2");
		String vmName = vm.getName();

	    String cloneName = vm.getName()+"_Clone";
 
             
        VirtualMachineRelocateSpec spec = new VirtualMachineRelocateSpec();
        VMHost bestHost = new VMHost();
        HostSystem newHost = bestHost.getBestHost(dc);
        ResourcePool pool = (ResourcePool) new InventoryNavigator(dc).searchManagedEntities("ResourcePool")[0];
        Datastore[] hds = newHost.getDatastores();
        spec.datastore = hds[0].getMOR();
	    spec.host = newHost.getConfig().getHost();
	    spec.transform = null;
	    spec.pool = pool.getConfig().getEntity();
	    spec.setDiskMoveType("moveAllDiskBackingsAndAllowSharing");
        VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
		cloneSpec.setLocation(spec);
		cloneSpec.setPowerOn(false);
		cloneSpec.setTemplate(false);
		cloneSpec.setSnapshot(vm.getCurrentSnapShot().getMOR());
		
		Task task = vm.cloneVM_Task((Folder) vm.getParent(), cloneName, cloneSpec);
		System.out.println("\n Launching the VM clone task to host " + newHost.getName() + ".Please wait ...");

		String status = task.waitForMe();
		if(status==Task.SUCCESS)
		{
			System.out.println(vmName + " cloned successfully to host!" + newHost.getName());
			AlarmManager am = si.getAlarmManager();
			Alarm[] amNew = am.getAlarm(vm);
			System.out.println("Deleting alarm" + amNew[0].getAlarmInfo().getName());
			amNew[0].removeAlarm();
			VirtualMachine clonedVM = (VirtualMachine) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("VirtualMachine",cloneName);
			Task taskoff =vm.powerOffVM_Task();
			String statusoff = taskoff.waitForMe();
			if(statusoff==Task.SUCCESS)
			{
				Task task2 =vm.destroy_Task();
				String status2 = task2.waitForMe();
				if(status2==Task.SUCCESS)
				{
						System.out.println(vmName + " deleted successfully");
						Task task3= clonedVM.rename_Task(vmName);
						String status3 = task3.waitForMe();
						if(status3==Task.SUCCESS)
						{
							System.out.println(cloneName + " renamed successfully to " + vmName);
							
							Task task4= clonedVM.powerOnVM_Task(newHost);
							String status4= task4.waitForMe();
								if(status3==Task.SUCCESS)
								{
									System.out.println(vmName +" Powered ON");
								}
						}
					
				   }
			}
			
			
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
