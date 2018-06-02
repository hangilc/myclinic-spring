import subprocess
import sys

proc = subprocess.run(sys.argv[1:], stdout=subprocess.PIPE, encoding='utf-8')
print(proc.stdout)