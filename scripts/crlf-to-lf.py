import sys

file = sys.argv[1]
contents = open(file, "r").read()
print(contents)
with open(file, "w", newline="\n") as f:
	f.write(contents)
