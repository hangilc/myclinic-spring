Param (
    [ValidatePattern('\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}')]
    [string]$at = $null
)

Write-Host $at

# $isRunning = "Running" -eq (Get-Service -Name 'PostgreSQL' `
#     | Select-Object -ExpandProperty Status)
# if( $isRunning ){
#     Stop-Service 'PostgreSQL'
# }

# $repo = 'C:\pgdata\main'
# $backup = "$repo\backup"
# $cluster = "$repo\cluster"
# $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
# $clusterSave = "$cluster-$timestamp"
# Rename-Item -Path $cluster -NewName $clusterSave
# Copy-Item $backup $cluster -recurse
# Remove-Item "$cluster\pg_wal" -recurse -force
# Copy-Item "$clusterSave\pg_wal" "$cluster\pg_wal" -recurse
# $recovery = @"
# restore_command = 'copy "C:\\pgdata\\main\\walarchive\\%f" "%p"'
# "@
# $recovery | Out-File -FilePath "$cluster\recovery.conf" -Encoding ASCII

# Start-Service 'PostgreSQL'