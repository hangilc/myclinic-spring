Param (
    [string]$src = 'mysql',
    [string]$dst = 'postgresql'
)

my-pyenv\Scripts\activate.ps1
$env:PYTHONPATH="db\scripts"
python db\scripts\xfer-db.py $src $dst
deactivate
