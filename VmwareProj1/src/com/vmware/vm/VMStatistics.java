package com.vmware.vm;

import com.vmware.vim25.VirtualHardware;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Network;
import com.vmware.vim25.mo.VirtualMachine;

public class VMStatistics {
		
		public void showVmInfo(VirtualMachine vm) throws Exception
		{
			    VirtualMachineConfigInfo info = vm.getConfig();				
				VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
				VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
			    VirtualMachineQuickStats vmstats = summary.getQuickStats() ;
			    Network[] ntw = vm.getNetworks();
			   // HostSystem[] hst = ntw[0].getHosts();
			    //VirtualMachineNetworkInfo = vm.getNetworks()[0].
			    
			    VirtualHardware hw = info.getHardware();
			    int vMem = hw.getMemoryMB();
			    int vCpu = hw.getNumCPU();
			   // NetworkSummary ntwSumary = ntw[0].getSummary();
			    System.out.println("VM " + vm.getName());
			    System.out.printf("Virtual Memory: %d Mb\n", vMem);
			    System.out.printf("Virtual CPU's: %d\n", vCpu);
			    System.out.println("Power State: " + vmri.getPowerState().toString());
			    System.out.println("Gues OS: "+ info.getGuestFullName());
	            System.out.printf( "Guest Memory Usage: %d MB\n", vmstats.getGuestMemoryUsage() ) ;
	            System.out.printf( "Host Memory Usage: %d MB\n", vmstats.getHostMemoryUsage() ) ;
	            System.out.printf( "Overall CPU Usage: %d MHz\n", vmstats.getOverallCpuUsage() ) ;
	            System.out.println("VM Connection State: "+ vmri.getConnectionState());
	            System.out.println("Guest OS HeartBeat status " + vmstats.getGuestHeartbeatStatus());
	            System.out.println("Number of Netwroks: "+ntw.length);
			    for(int i=0;i<ntw.length;i++)
			    {
			    	System.out.println(ntw[i]);
			    	System.out.println("Number of hosts in netwrok "+ ntw[i].getName() + " is "+ ntw[i].getHosts().length);
			        for(int j=0; j<ntw[i].getHosts().length;j++)
			        	System.out.println("Host in "+ntw[i].getName()+" "+ntw[i].getHosts()[j].getName());
			    }
			
		}

}
