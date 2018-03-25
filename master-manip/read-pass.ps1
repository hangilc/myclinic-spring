param(
    $prompt = "Enter password: "
)

$pass = Read-Host $prompt -AsSecureString
$bstr = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($pass)
$plain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
echo $plain
