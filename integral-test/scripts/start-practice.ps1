Start-Process powershell {
    Write-Host "Starting practice..."
    & "java -jar practice\target\practice-1.0.0-SNAPSHOT.jar --management 9001 http://localhost:18084/json"
}
