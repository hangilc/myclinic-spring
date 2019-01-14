$cli = New-Object System.Net.WebClient
$url = New-Object System.Uri("https://sdk-for-net.amazonwebservices.com/latest/AWSToolsAndSDKForNet.msi")
$file = Split-Path $url.AbsolutePath -Leaf
$save =  [io.path]::combine(".", "work", $file)
$cli.DownloadFile($url, $save)
