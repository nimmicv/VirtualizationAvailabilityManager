package com.vmware.vm;

import java.rmi.RemoteException;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
 class VMPowerOffAlarm 
{
	
public static void setTurnOFfAlarm(ServiceInstance si, VirtualMachine vm, String alarm_Name) throws Exception
		{
			
			AlarmManager am = si.getAlarmManager();
			String alarmName = alarm_Name;
			VMPowerOffAlarm alarm = new VMPowerOffAlarm();
			
		if(vm!=null && am!=null)
		{
			StateAlarmExpression expression = alarm.createStateAlarmExpression();
			MethodAction methodAction = alarm.createPowerOnAction();
			AlarmAction alarmAction = (AlarmAction) alarm.createAlarmTriggerAction(methodAction);
			AlarmSpec alarmSpec = alarm.createAlarmSpec(alarmName, alarmAction, expression);
			try
			{
				am.createAlarm(vm, alarmSpec);
				System.out.println("Successfully created Alarm: " + alarmName);
			}
			catch(InvalidName in) 
			{
				System.out.println("Alarm name is empty or too long");
			}
			catch(DuplicateName dn)
			{
				System.out.println("Alarm with the name already exists");
			}
			catch(RemoteException re)
			{
				re.printStackTrace();
			}
		}
		else 
		{
			System.out.println("Either VM is not found or Alarm Manager is not available on this server.");
		}
	}
	
   private StateAlarmExpression createStateAlarmExpression()
	{   
	  StateAlarmExpression sae = new StateAlarmExpression();
	  sae.setOperator(StateAlarmOperator.isEqual);
	  sae.setRed("poweredOff");
	  sae.setYellow(null);
	  sae.setStatePath("runtime.powerState");
	  sae.setType("VirtualMachine");
	  return sae;
	}
   
	private MethodAction createPowerOnAction() 
	{
	   MethodAction action = new MethodAction();
	   action.setName("PowerOnVM_Task");
	   MethodActionArgument argument = new MethodActionArgument();
	   argument.setValue(null);
	   action.setArgument(new MethodActionArgument[] { argument });
	   return action;
	}
	private SendEmailAction createEmailAction() 
	  {
	    SendEmailAction action = new SendEmailAction();
	    action.setToList("nimmicv@gmail.com");
	    action.setCcList("nimmi.varkey@sjsu.edu");
	    action.setSubject("Alarm - {alarmName} on {targetName}\n");
	    action.setBody("Description:{eventDescription}\n"
	        + "TriggeringSummary:{triggeringSummary}\n"
	        + "newStatus:{newStatus}\n"
	        + "oldStatus:{oldStatus}\n"
	        + "target:{target}");
	    return action;
	  }
   
	 private AlarmTriggeringAction createAlarmTriggerAction(Action methodAction) throws Exception 
   {
      AlarmTriggeringAction alarmAction = new AlarmTriggeringAction();
      alarmAction.setYellow2red(true);
      alarmAction.setAction(methodAction);
      return alarmAction;
   }
   
   private AlarmSpec createAlarmSpec(String alarmName, AlarmAction action, AlarmExpression expression) throws Exception 
   {      
	   AlarmSpec spec = new AlarmSpec();
	   spec.setAction(action);
	   spec.setExpression(expression);
	   spec.setName(alarmName);
	   spec.setDescription("Monitor VM state and send email if VM power's off");
	   spec.setEnabled(true);      
	   return spec;
   }

	/*private static OptionSpec[] constructOptions() 
	{
		OptionSpec [] useroptions = new OptionSpec[2];
		useroptions[0] = new OptionSpec("vmname", "String", 1, "Name of the virtual machine", null);
		useroptions[1] = new OptionSpec("alarm","String",1, "Name of the alarm", null);
		return useroptions;
	}
   */
}
