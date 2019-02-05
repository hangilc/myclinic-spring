$env:MYCLINIC_DB_HOST="192.168.33.10"
Start-Process -FilePath powershell -ArgumentList '-Command', {
    java -jar server\target\server-1.0.0-SNAPSHOT.jar --server.port=18084
}, '-noexit'
Write-Host "mock server started at port 18084"
# $env:MYCLINIC_DB_HOST="192.168.33.10"
# java -jar server\target\server-1.0.0-SNAPSHOT.jar --server.port=18084
# $env:MYCLINIC_MOCK_SERVER="http://localhost:18084"
