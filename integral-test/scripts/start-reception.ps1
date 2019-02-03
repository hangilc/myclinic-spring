Start-Process powershell {
    Write-Host "Starting reception..."
    & "java -jar reception\target\reception-1.0.0-SNAPSHOT.jar --management 9000 http://localhost:18084/json"
}
