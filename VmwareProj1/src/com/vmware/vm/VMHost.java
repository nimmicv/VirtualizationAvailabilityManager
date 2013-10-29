package com.vmware.vm;

import java.rmi.RemoteException;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.ClusterComputeResource;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;

public class VMHost {
	
	HostSystem getBestHost(Datacenter forDatacenter) throws InvalidProperty, RuntimeFault, RemoteException {
		HostSystem ohWell = null;
		for( ManagedEntity me : forDatacenter.getHostFolder().getChildEntity() ) {
		ComputeResource cluster = (ComputeResource)me;
	
		for( HostSystem host : cluster.getHosts() ) {
		if( host.getConfigStatus().equals(ManagedEntityStatus.green) ) {
	             return host;
	    }
		    if( ohWell == null || host.getConfigStatus().equals(ManagedEntityStatus.yellow) ) {
               ohWell = host;
		              }
	           }
	       }     
		     return ohWell;
	}
}
