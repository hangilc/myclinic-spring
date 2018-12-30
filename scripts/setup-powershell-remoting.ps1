# This script should be run under Administrator role.

# Set-ExecutionPolicy RemoteSigned

Enable-PSRemoting
Set-Item WSMan:\localhost\Client\TrustedHosts -Value *
#sc.exe config winrm start= delayed-auto
sc.exe config winmgmt start= delayed-auto
sc.exe config fdphost start= delayed-auto
sc.exe config FDResPub start= delayed-auto


# In order to allow non-admin user for remoting, use folloing command.
# > Set-PSSessionConfiguration -Name Microsoft.PowerShell -showSecurityDescriptorUI